package lv.ctco.zephyr;

import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import lv.ctco.tmm.TestCase;
import lv.ctco.zephyr.beans.Metafield;
import lv.ctco.zephyr.beans.ResultTestCase;
import lv.ctco.zephyr.beans.ResultTestSuite;
import lv.ctco.zephyr.beans.jira.Fields;
import lv.ctco.zephyr.beans.jira.Issue;
import lv.ctco.zephyr.beans.jira.Project;
import lv.ctco.zephyr.beans.jira.SearchResult;
import lv.ctco.zephyr.beans.zapi.Cycle;
import lv.ctco.zephyr.beans.zapi.CycleList;
import lv.ctco.zephyr.beans.zapi.Execution;
import lv.ctco.zephyr.beans.zapi.ExecutionResult;
import lv.ctco.zephyr.util.HttpUtils;
import lv.ctco.zephyr.util.Utils;
import org.apache.http.HttpResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static lv.ctco.zephyr.Config.getValue;
import static lv.ctco.zephyr.enums.ConfigProperty.PROJECT_KEY;
import static lv.ctco.zephyr.enums.ConfigProperty.RELEASE_VERSION;
import static lv.ctco.zephyr.enums.ConfigProperty.TEST_CYCLE;
import static lv.ctco.zephyr.enums.IssueType.TEST;
import static lv.ctco.zephyr.util.HttpTransformer.deserialize;
import static lv.ctco.zephyr.util.HttpUtils.getAndReturnBody;
import static lv.ctco.zephyr.util.Utils.generateJiraKey;
import static lv.ctco.zephyr.util.Utils.log;
import static lv.ctco.zephyr.util.Utils.readCucumberReport;
import static lv.ctco.zephyr.util.Utils.readInputStream;

public class Runner {

    public static int TOP = 100;
    public static int SKIP = 0;

    public static String projectId;
    public static String versionId;
    public static String cycleId;

    public static void main(String[] args) throws Exception {
        List<TestCase> resultTestCases = transform(readCucumberReport("junit.xml"));
        if (resultTestCases.size() == 0) return;

        List<Issue> issues = fetchTestIssues();
        if (issues == null) throw new RuntimeException("Unable to fetch JIRA issues");

        mapToIssues(resultTestCases, issues);

        for (TestCase testCase : resultTestCases) {
            if (testCase.getId() == null) {
                createTestIssue(testCase);
            }
        }

        retrieveProjectMetaInfo();

        cycleId = getTestCycleId();
        if (cycleId == null) throw new RuntimeException("Unable to retrieve JIRA test cycle");

        Map<String, Execution> executions = fetchExecutions();
        for (TestCase testCase : resultTestCases) {
            if (executions == null || executions.get(testCase.getKey()) == null) {
                linkTestToCycle(testCase);
            }
        }

        System.out.println();
    }

    private static String getTestCycleId() throws Exception {
        if (projectId == null || versionId == null) throw new RuntimeException("JIRA projectID or versionID are missing");

        String response = getAndReturnBody(String.format("zapi/latest/cycle?projectId=%s&versionId=%s", projectId, versionId));
        CycleList cycleList = deserialize(response, CycleList.class);
        if (cycleList == null || cycleList.getCycleMap().size() == 0) return null;

        for (Map.Entry<String, Cycle> entry : cycleList.getCycleMap().entrySet()) {
            Cycle value = entry.getValue();
            if (value != null
                    && value.getProjectKey().equals(getValue(PROJECT_KEY))
                    && value.getVersionId().toString().equals(versionId)
                    && value.getName().equals(getValue(TEST_CYCLE))) {
                String cycleId = entry.getKey();
                Utils.log("Retrieved target Test Cycle ID - " + cycleId);
                return cycleId;
            }
        }
        return null;
    }

    private static void linkTestToCycle(TestCase testCase) throws Exception {
        log("Linking Test case " + testCase.getKey() + " to Test Cycle");

        Execution execution = new Execution();
        execution.setProjectId(projectId);
        execution.setVersionId(versionId);
        execution.setCycleId(cycleId);
        execution.setMethod(2);
        execution.setIssueId(testCase.getId());

        HttpResponse response = HttpUtils.post("zapi/latest/execution", execution);
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new InternalException("Could not link " + testCase.getKey());
        }
    }

    private static void retrieveProjectMetaInfo() throws Exception {
        String projectKey = getValue(PROJECT_KEY);
        String response = getAndReturnBody("api/2/project/" + projectKey);
        Project project = deserialize(response, Project.class);

        if (project == null || !project.getKey().equals(projectKey)) {
            throw new RuntimeException("Improper JIRA project retrieved");
        }

        projectId = project.getId();
        log("Retrieved project ID - " + projectId);

        for (Metafield version : project.getVersions()) {
            if (version.getName().equals(getValue(RELEASE_VERSION))) {
                versionId = version.getId();
                log("Retrieved version ID - " + versionId);
            }
        }
    }

    private static void createTestIssue(TestCase testCase) throws Exception {
        Issue issue = new Issue();
        Fields fields = issue.getFields();
        fields.setSummary(testCase.getName());
        fields.setEnvironment(testCase.getUniqueId());

        Metafield project = new Metafield();
        project.setKey(Config.getValue(PROJECT_KEY));
        fields.setProject(project);

        Metafield issueType = new Metafield();
        issueType.setName(TEST.getName());
        fields.setIssuetype(issueType);

        Metafield priority = new Metafield();
        priority.setName(testCase.getPriority().getName());
        fields.setPriority(priority);

        List<Metafield> versions = new ArrayList<Metafield>(1);
        Metafield version = new Metafield();
        version.setName(Config.getValue(RELEASE_VERSION));
        versions.add(version);
        fields.setVersions(versions);

        HttpResponse response = HttpUtils.post("api/2/issue", issue);
        if (response.getStatusLine().getStatusCode() != 201) {
            throw new InternalException("Could not create a Test issue in JIRA");
        }

        String responseBody = readInputStream(response.getEntity().getContent());
        Metafield result = deserialize(responseBody, Metafield.class);
        if (result != null) {
            testCase.setId(Integer.valueOf(result.getId()));
            testCase.setKey(result.getKey());
        }
    }

    private static void mapToIssues(List<TestCase> resultTestCases, List<Issue> issues) {
        Map<String, Issue> uniqueKeyMap = new HashMap<String, Issue>(issues.size());
        for (Issue issue : issues) {
            String environment = issue.getFields().getEnvironment();
            if (environment != null) {
                uniqueKeyMap.put(environment, issue);
            }
        }

        for (TestCase testCase : resultTestCases) {
            Issue issue = uniqueKeyMap.get(testCase.getUniqueId());
            if (issue == null) continue;

            testCase.setId(issue.getId());
            testCase.setKey(issue.getKey());
        }
    }

    private static List<Issue> fetchTestIssues() throws Exception {
        log("Fetching JIRA Test issues for the project");
        String search = "project='" + getValue(PROJECT_KEY) + "'%20and%20issueType=Test";
        SearchResult searchResults = searchInJQL(search, SKIP);
        if (searchResults == null) return null;

        List<Issue> issues = searchResults.getIssues();

        int totalCount = searchResults.getTotal();
        if (totalCount > TOP) {
            while (issues.size() != totalCount) {
                SKIP += TOP;
                issues.addAll(searchInJQL(search, SKIP).getIssues());
            }
        }
        log(String.format("Retrieved %s Test issues", issues.size()));
        return issues;
    }

    private static Map<String, Execution> fetchExecutions() throws Exception {
        log("Fetching JIRA Test Executions for the project");
        String search = "project='" + getValue(PROJECT_KEY) + "'%20and%20fixVersion='"
                + getValue(RELEASE_VERSION) + "'%20and%20cycleName='" + getValue(TEST_CYCLE) + "'";

        ExecutionResult executionResult = searchInZQL(search, SKIP);
        if (executionResult == null || executionResult.getExecutions().size() == 0) return null;

        List<Execution> executions = executionResult.getExecutions();

        int totalCount = executionResult.getTotalCount();
        if (totalCount > TOP) {
            while (executions.size() != totalCount) {
                SKIP += TOP;
                executions.addAll(searchInZQL(search, SKIP).getExecutions());
            }
        }
        Map<String, Execution> result = new HashMap<String, Execution>(executions.size());
        for (Execution execution : executions) {
            result.put(execution.getIssueKey(), execution);
        }
        log(String.format("Retrieved %s Test executions", executions.size()));
        return result;
    }

    private static SearchResult searchInJQL(String search, int skip) throws Exception {
        String response = getAndReturnBody("api/2/search?jql=" + search + "&maxResults=" + TOP + "&startAt=" + skip);
        return deserialize(response, SearchResult.class);
    }

    private static ExecutionResult searchInZQL(String search, int skip) throws Exception {
        String response = getAndReturnBody("zapi/latest/zql/executeSearch?zqlQuery=" + search + "&maxResults=" + TOP + "&startAt=" + skip);
        return deserialize(response, ExecutionResult.class);
    }

    private static List<TestCase> transform(ResultTestSuite resultTestSuite) {
        if (resultTestSuite.getTestcase() == null) {
            return new ArrayList<TestCase>();
        }

        List<TestCase> result = new ArrayList<TestCase>();
        for (ResultTestCase testCase : resultTestSuite.getTestcase()) {
            TestCase test = new TestCase();
            test.setName(testCase.getName());
            test.setUniqueId(generateJiraKey(testCase));
            result.add(test);
        }
        return result;
    }
}
