package lv.ctco.zephyr;

import lv.ctco.zephyr.beans.ResultTestCase;
import lv.ctco.zephyr.beans.ResultTestSuite;
import lv.ctco.zephyr.beans.jira.Issue;
import lv.ctco.zephyr.beans.jira.SearchResult;
import lv.ctco.zephyr.util.Utils;

import java.util.List;

import static lv.ctco.zephyr.Config.getValue;
import static lv.ctco.zephyr.enums.ConfigProperty.PROJECT_KEY;
import static lv.ctco.zephyr.util.HttpTransformer.deserialize;
import static lv.ctco.zephyr.util.HttpUtils.getAndReturnBody;
import static lv.ctco.zephyr.util.Utils.log;

public class Runner {

    public static int TOP = 20;
    public static int SKIP = 0;

    public static void main(String[] args) throws Exception {
        ResultTestSuite resultTestSuite = Utils.readCucumberReport("junit.xml");

        List<ResultTestCase> testCases = resultTestSuite.getTestcase();
        if (testCases == null || testCases.size() == 0) return;

        log("Fetching JIRA test issues for the project");
        SearchResult searchResults = searchJiraIssues(SKIP);
        if (searchResults == null) return;

        List<Issue> issues = searchResults.getIssues();

        int totalCount = searchResults.getTotal();
        if (totalCount > TOP) {
            while (issues.size() != totalCount) {
                SKIP += TOP;
                issues.addAll(searchJiraIssues(SKIP).getIssues());

            }
        }
        log(String.format("Retrieved %s issues", issues.size()));
    }

    private static SearchResult searchJiraIssues(int skip) throws Exception {
        String response = getAndReturnBody(String.format("api/2/search?jql=project=%s&maxResults=%s&startAt=%s", getValue(PROJECT_KEY), TOP, skip));
        return deserialize(response, SearchResult.class);
    }
}
