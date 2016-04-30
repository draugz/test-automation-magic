package lv.ctco.zephyr;

import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import lv.ctco.tmm.TestCase;
import lv.ctco.zephyr.beans.Metafield;
import lv.ctco.zephyr.beans.ResultTestCase;
import lv.ctco.zephyr.beans.ResultTestSuite;
import lv.ctco.zephyr.beans.jira.Fields;
import lv.ctco.zephyr.beans.jira.Issue;
import lv.ctco.zephyr.beans.jira.SearchResult;
import lv.ctco.zephyr.util.HttpUtils;
import org.apache.http.HttpResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static lv.ctco.zephyr.Config.getValue;
import static lv.ctco.zephyr.enums.ConfigProperty.PROJECT_KEY;
import static lv.ctco.zephyr.enums.ConfigProperty.RELEASE_VERSION;
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

    public static void main(String[] args) throws Exception {
        List<TestCase> resultTestCases = transform(readCucumberReport("junit.xml"));
        if (resultTestCases.size() == 0) return;

        List<Issue> issues = fetchTestIssues();
        if (issues != null) {
            mapToIssues(resultTestCases, issues);
        }

        for (TestCase testCase : resultTestCases) {
            if (testCase.getId() == null) {
                createTestIssue(testCase);
            }
        }
        System.out.println();
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
        SearchResult searchResults = searchInJira(SKIP);
        if (searchResults == null) return null;

        List<Issue> issues = searchResults.getIssues();

        int totalCount = searchResults.getTotal();
        if (totalCount > TOP) {
            while (issues.size() != totalCount) {
                SKIP += TOP;
                issues.addAll(searchInJira(SKIP).getIssues());
            }
        }
        log(String.format("Retrieved %s Test issues", issues.size()));
        return issues;
    }

    private static SearchResult searchInJira(int skip) throws Exception {
        String response = getAndReturnBody("api/2/search?jql=project=" + getValue(PROJECT_KEY) + "%20and%20issueType=Test" +
                "&maxResults=" + TOP + "&startAt=" + skip);
        return deserialize(response, SearchResult.class);
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
