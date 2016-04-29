package lv.ctco.zephyr;

import lv.ctco.tmm.TestCase;
import lv.ctco.zephyr.beans.ResultTestCase;
import lv.ctco.zephyr.beans.ResultTestSuite;
import lv.ctco.zephyr.beans.jira.Issue;
import lv.ctco.zephyr.beans.jira.SearchResult;

import java.util.ArrayList;
import java.util.List;

import static lv.ctco.zephyr.Config.getValue;
import static lv.ctco.zephyr.enums.ConfigProperty.PROJECT_KEY;
import static lv.ctco.zephyr.util.HttpTransformer.deserialize;
import static lv.ctco.zephyr.util.HttpUtils.getAndReturnBody;
import static lv.ctco.zephyr.util.Utils.generateJiraKey;
import static lv.ctco.zephyr.util.Utils.log;
import static lv.ctco.zephyr.util.Utils.readCucumberReport;

public class Runner {

    public static int TOP = 100;
    public static int SKIP = 0;

    public static void main(String[] args) throws Exception {
        List<TestCase> resultTestCases = transform(readCucumberReport("junit.xml"));
        if (resultTestCases.size() == 0) return;

        List<Issue> issues = fetchTestIssues();

        for (Issue issue : issues) {

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
