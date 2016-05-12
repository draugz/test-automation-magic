package lv.ctco.tmm;

import lv.ctco.tmm.utils.ConfigReader;
import lv.ctco.tmm.utils.RestHelper;
import lv.ctco.zephyr.beans.TestCase;
import lv.ctco.zephyr.enums.TestStatus;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static lv.ctco.zephyr.util.ReportTransformer.transform;
import static lv.ctco.zephyr.util.Utils.readAllureReport;

public class JiraTCCreationRunner {

    private static String REPORT_DIRECTORY = ConfigReader.getValueByKey("reportDirectory");
    private RestHelper restHelper;
    private List<TestCase> testCasesList = new ArrayList<TestCase>();
    protected static final Logger LOG = Logger.getLogger(JiraTCCreationRunner.class);

    public static void main(String[] args) throws Exception {
        JiraTCCreationRunner j = new JiraTCCreationRunner();
        j.run();
    }

    public void run() throws URISyntaxException {
        restHelper = new RestHelper();
        LOG.info("Create list of Test Suites");
        if (ConfigReader.getValueByKey("reportType").toLowerCase().equals("allure")) {
            try {
                testCasesList = transform(readAllureReport(REPORT_DIRECTORY));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LOG.info("For test case");
        for (TestCase currentTestCase : testCasesList) {
            createTestCaseAndLinkToTestCycleAndSetStatus(currentTestCase);
        }
    }

    private void updateTestCaseExecutionStatus(String testCaseId, TestStatus testCaseStatus) {
        LOG.info("Update test execution status");
        restHelper.updateExecutionStatus(testCaseStatus.getId(), restHelper.getExecutionId(testCaseId));
    }

    public void createTestCaseAndLinkToTestCycleAndSetStatus(TestCase testCase) {
        String testCaseId = restHelper.getIssueId(getTestCaseKey(testCase));
        assignToTestCycleAndSetStatus(testCaseId, testCase);
    }


    public void assignToTestCycleAndSetStatus(String testCaseId, TestCase testCase) {
        LOG.info("Assign Test Case to Test cycle");
        restHelper.addToTestCycle(testCaseId);
        updateTestCaseExecutionStatus(testCaseId, testCase.getStatus());
    }

    private String getTestCaseKey(TestCase testCase) {
        LOG.info("Get Jira Key from the report");
        if (!(testCase.getKey() == null)) {
            return testCase.getKey();
        }
        LOG.info("Jira Key does not exists, new Test Case should be created");
        return restHelper.createTestCase(testCase.getName(), testCase.getSeverity().getIndex());
    }
}
