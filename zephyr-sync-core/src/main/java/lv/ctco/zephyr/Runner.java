package lv.ctco.zephyr;

import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import lv.ctco.zephyr.beans.Metafield;
import lv.ctco.zephyr.beans.TestCase;
import lv.ctco.zephyr.beans.TestStep;
import lv.ctco.zephyr.beans.jira.Fields;
import lv.ctco.zephyr.beans.jira.Issue;
import lv.ctco.zephyr.beans.jira.IssueLink;
import lv.ctco.zephyr.beans.jira.Project;
import lv.ctco.zephyr.beans.jira.SearchResponse;
import lv.ctco.zephyr.beans.zapi.Cycle;
import lv.ctco.zephyr.beans.zapi.CycleList;
import lv.ctco.zephyr.beans.zapi.Execution;
import lv.ctco.zephyr.beans.zapi.ExecutionRequest;
import lv.ctco.zephyr.beans.zapi.ExecutionResponse;
import lv.ctco.zephyr.beans.zapi.ZapiTestStep;
import lv.ctco.zephyr.enums.ReportType;
import lv.ctco.zephyr.enums.TestStatus;
import lv.ctco.zephyr.transformer.AllureTransformer;
import lv.ctco.zephyr.transformer.CucumberTransformer;
import lv.ctco.zephyr.transformer.JUnitTransformer;
import lv.ctco.zephyr.util.Utils;
import org.apache.http.HttpResponse;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static lv.ctco.zephyr.Config.getValue;
import static lv.ctco.zephyr.Config.loadConfigProperties;
import static lv.ctco.zephyr.enums.ConfigProperty.FORCE_STORY_LINK;
import static lv.ctco.zephyr.enums.ConfigProperty.ORDERED_STEPS;
import static lv.ctco.zephyr.enums.ConfigProperty.PROJECT_KEY;
import static lv.ctco.zephyr.enums.ConfigProperty.RELEASE_VERSION;
import static lv.ctco.zephyr.enums.ConfigProperty.REPORT_PATH;
import static lv.ctco.zephyr.enums.ConfigProperty.REPORT_TYPE;
import static lv.ctco.zephyr.enums.ConfigProperty.TEST_CYCLE;
import static lv.ctco.zephyr.enums.IssueType.TEST;
import static lv.ctco.zephyr.util.HttpUtils.getAndReturnBody;
import static lv.ctco.zephyr.util.HttpUtils.post;
import static lv.ctco.zephyr.util.HttpUtils.put;
import static lv.ctco.zephyr.util.ObjectTransformer.deserialize;
import static lv.ctco.zephyr.util.Utils.log;
import static lv.ctco.zephyr.util.Utils.readAllureReport;
import static lv.ctco.zephyr.util.Utils.readCucumberReport;
import static lv.ctco.zephyr.util.Utils.readInputStream;
import static lv.ctco.zephyr.util.Utils.readJUnitReport;

public class Runner {

    private static int TOP = 20;
    private static int SKIP = 0;

    private static String projectId;
    private static String versionId;
    private static String cycleId;

    public static void main(String[] args) throws Exception {
        loadConfigProperties(args);

        List<TestCase> testCases = resolveTestCases(getValue(REPORT_PATH));
        if (testCases == null || testCases.size() == 0) throw new RuntimeException("No Test Cases extracted from the Test Report");

        List<Issue> issues = fetchTestIssues();
        if (issues == null) throw new RuntimeException("Unable to fetch JIRA test issues");

        mapToIssues(testCases, issues);

        for (TestCase testCase : testCases) {
            if (testCase.getId() == null) {
                createTestIssue(testCase);
                addStepsToTestIssue(testCase);
                linkToStory(testCase);
            }
        }

        retrieveProjectMetaInfo();

        cycleId = getTestCycleId();
        if (cycleId == null) throw new RuntimeException("Unable to retrieve JIRA test cycle");

        Map<String, Execution> executions = fetchAllExecutions();
        List<String> keys = new ArrayList<String>();

        for (TestCase testCase : testCases) {
            if (executions == null || executions.get(testCase.getKey()) == null) {
                keys.add(testCase.getKey());
            }
        }
        if (keys.size() > 0) {
            linkTestToCycle(keys);
        } else {
            log("All Test cases are already linked to the Test cycle.\n");
        }

        executions = fetchAllExecutions();
        updateExecutionStatuses(executions, testCases);

        System.out.println();
    }

    private static List<Issue> fetchTestIssues() throws Exception {
        log("Fetching JIRA Test issues for the project");
        String search = "project='" + getValue(PROJECT_KEY) + "'%20and%20issueType=Test";
        SearchResponse searchResults = searchInJQL(search, SKIP);
        if (searchResults == null) return null;

        List<Issue> issues = searchResults.getIssues();

        int totalCount = searchResults.getTotal();
        if (totalCount > TOP) {
            while (issues.size() != totalCount) {
                SKIP += TOP;
                issues.addAll(searchInJQL(search, SKIP).getIssues());
            }
        }
        log(format("Retrieved %s Test issues\n", issues.size()));
        return issues;
    }

    private static SearchResponse searchInJQL(String search, int skip) throws Exception {
        String response = getAndReturnBody("api/2/search?jql=" + search + "&maxResults=" + TOP + "&startAt=" + skip);
        return deserialize(response, SearchResponse.class);
    }

    private static void mapToIssues(List<TestCase> resultTestCases, List<Issue> issues) {
        Map<String, Issue> uniqueKeyMap = new HashMap<String, Issue>(issues.size());
        for (Issue issue : issues) {
            uniqueKeyMap.put(issue.getKey(), issue);
            String environment = issue.getFields().getEnvironment();
            if (environment != null) {
                uniqueKeyMap.put(environment, issue);
            }
        }

        for (TestCase testCase : resultTestCases) {
            if (testCase.getKey() != null) {
                if (uniqueKeyMap.containsKey(testCase.getKey())) {
                    testCase.setId(uniqueKeyMap.get(testCase.getKey()).getId());
                } else {
                    log(format("Key: %s, is not found, new Test Case should be created", testCase.getKey()));
                    testCase.setId(null);
                    testCase.setKey(null);
                }
            } else {
                Issue issue = uniqueKeyMap.get(testCase.getUniqueId());
                if (issue == null) continue;

                testCase.setId(issue.getId());
                testCase.setKey(issue.getKey());
            }
        }
    }

    private static void createTestIssue(TestCase testCase) throws Exception {
        log("Creating JIRA test issue with Unique ID " + testCase.getUniqueId());
        Issue issue = new Issue();
        Fields fields = issue.getFields();
        fields.setSummary(testCase.getName());
        fields.setEnvironment(testCase.getUniqueId());
        fields.setLabels(new String[]{"Automation"});

        Metafield project = new Metafield();
        project.setKey(Config.getValue(PROJECT_KEY));
        fields.setProject(project);

        Metafield issueType = new Metafield();
        issueType.setName(TEST.getName());
        fields.setIssuetype(issueType);

        Metafield assignee = new Metafield();
        assignee.setName("");
        fields.setAssignee(assignee);

        if (testCase.getPriority() != null) {
            Metafield priority = new Metafield();
            priority.setName(testCase.getPriority().getName());
            fields.setPriority(priority);
        }

        if (testCase.getSeverity() != null) {
            Metafield severity = new Metafield();
            severity.setId(testCase.getSeverity().getIndex().toString());
            fields.setSeverity(severity);
        }

        List<Metafield> versions = new ArrayList<Metafield>(1);
        Metafield version = new Metafield();
        version.setName(Config.getValue(RELEASE_VERSION));
        versions.add(version);
        fields.setVersions(versions);

        HttpResponse response = post("api/2/issue", issue);
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

    private static void addStepsToTestIssue(TestCase testCase) throws Exception {
        log("Adding Test steps to Test issue: " + testCase.getKey());
        List<TestStep> testSteps = testCase.getSteps();

        Map<Integer, TestStep> map = new HashMap<Integer, TestStep>();
        prepareTestSteps(map, testSteps, 0, "", Boolean.valueOf(getValue(ORDERED_STEPS)));

        for (TestStep step : map.values()) {
            HttpResponse response = post("zapi/latest/teststep/" + testCase.getId(), new ZapiTestStep(step.getDescription()));
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new InternalException("Could not add Test Steps for Test Case: " + testCase.getId() + "\n");
            }
        }
    }

    private static void linkToStory(TestCase testCase) throws Exception {
        List<String> storyKeys = testCase.getStoryKeys();
        if (Boolean.valueOf(getValue(FORCE_STORY_LINK))) {
            if (storyKeys == null || storyKeys.size() == 0) {
                throw new InternalException("Linking Test issues to Story is mandatory, please check if Story marker exists in " + testCase.getKey());
            }
        }
        if (storyKeys == null) return;

        log("Linking Test issue " + testCase.getKey() + " to Stories " + testCase.getStoryKeys());
        for (String storyKey : storyKeys) {
            HttpResponse response = post("api/2/issueLink", new IssueLink(testCase.getKey(), storyKey.toUpperCase()));
            if (response.getStatusLine().getStatusCode() != 201) {
                throw new InternalException("Could not link Test issue: " + testCase.getId() + " to Story " + storyKey + ". " +
                        "Please check if Story issue exists and is valid\n");
            }
        }
    }

    private static void prepareTestSteps(Map<Integer, TestStep> map, List<TestStep> testSteps, int level, String prefix, Boolean isOrdered) {
        for (int i = 1; i <= testSteps.size(); i++) {
            TestStep testStep = testSteps.get(i - 1);
            String description = testStep.getDescription();
            testStep.setDescription(isOrdered ? format("%s %s", prefix + i + ".", description) : description);
            map.put(map.size() + 1, testStep);

            if (testStep.getSteps() != null && testStep.getSteps().size() > 0) {
                prepareTestSteps(map, testStep.getSteps(), level + 1, prefix + i + ".", isOrdered);
            }
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

    private static String getTestCycleId() throws Exception {
        if (projectId == null || versionId == null)
            throw new RuntimeException("JIRA projectID or versionID are missing");

        String response = getAndReturnBody(format("zapi/latest/cycle?projectId=%s&versionId=%s", projectId, versionId));
        CycleList cycleList = deserialize(response, CycleList.class);
        if (cycleList == null || cycleList.getCycleMap().size() == 0) return null;

        for (Map.Entry<String, Cycle> entry : cycleList.getCycleMap().entrySet()) {
            Cycle value = entry.getValue();
            if (value != null
                    && value.getProjectKey().equals(getValue(PROJECT_KEY))
                    && value.getVersionId().toString().equals(versionId)
                    && value.getName().equals(getValue(TEST_CYCLE))) {
                String cycleId = entry.getKey();
                Utils.log("Retrieved target Test Cycle ID - " + cycleId + "\n");
                return cycleId;
            }
        }
        return null;
    }

    private static Map<String, Execution> fetchAllExecutions() throws Exception {
        log("Fetching JIRA Test Executions for the project");
        String search = "project='" + getValue(PROJECT_KEY) + "'%20and%20fixVersion='"
                + URLEncoder.encode(getValue(RELEASE_VERSION), "UTF-8") + "'%20and%20cycleName='" + getValue(TEST_CYCLE) + "'";

        ExecutionResponse executionResponse = searchInZQL(search, SKIP);
        if (executionResponse == null || executionResponse.getExecutions().size() == 0) return null;

        List<Execution> executions = executionResponse.getExecutions();

        int totalCount = executionResponse.getTotalCount();
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
        log(format("Retrieved %s Test executions\n", executions.size()));
        return result;
    }

    private static ExecutionResponse searchInZQL(String search, int skip) throws Exception {
        String response = getAndReturnBody("zapi/latest/zql/executeSearch?zqlQuery=" + search + "&offset=" + skip);
        return deserialize(response, ExecutionResponse.class);
    }

    private static void linkTestToCycle(List<String> keys) throws Exception {
        log("Linking Test cases " + keys.toString() + " to Test Cycle");

        Execution execution = new Execution();
        execution.setProjectId(projectId);
        execution.setVersionId(versionId);
        execution.setCycleId(cycleId);
        execution.setMethod(1);
        execution.setIssues(keys);

        HttpResponse response = post("zapi/latest/execution/addTestsToCycle", execution);
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new InternalException("Could not link Test cases\n");
        }
    }

    private static void updateExecutionStatuses(Map<String, Execution> executions, List<TestCase> resultTestCases) throws Exception {
        Map<TestStatus, List<String>> statusMap = new HashMap<TestStatus, List<String>>();

        for (TestCase testCase : resultTestCases) {
            TestStatus status = testCase.getStatus();
            List<String> ids = statusMap.get(status);
            if (ids == null) statusMap.put(status, new ArrayList<String>());
            Execution execution = executions.get(testCase.getKey());
            if (execution != null) {
                statusMap.get(status).add(execution.getId().toString());
            }
        }

        for (Map.Entry<TestStatus, List<String>> entry : statusMap.entrySet()) {
            for (String id : entry.getValue()) {
                updateExecutionStatus(entry.getKey(), id);
            }
        }
    }

    private static void updateExecutionStatus(TestStatus status, String id) throws Exception {
        log("Setting status " + status.name() + " to " + id + " test case");

        ExecutionRequest request = new ExecutionRequest();
        request.setStatus(status.getId());

        HttpResponse response = put("zapi/latest/execution/" + id + "/execute", request);
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new InternalException("Could not successfully update execution status");
        }
    }

    private static List<TestCase> resolveTestCases(String path) throws Exception {
        ReportType type = ReportType.findById(getValue(REPORT_TYPE));
        if (type == null) throw new RuntimeException("Report type is not recognized!");

        switch (type) {
            case JUNIT:
                return JUnitTransformer.transform(readJUnitReport(path));
            case CUCUMBER:
                return CucumberTransformer.transform(readCucumberReport(path));
            case ALLURE:
                return AllureTransformer.transform(readAllureReport(path));
            default:
                return null;
        }
    }
}
