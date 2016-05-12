package lv.ctco.tmm;

import lv.ctco.tmm.utils.ConfigReader;
import lv.ctco.tmm.utils.RestHelper;
import lv.ctco.zephyr.beans.ITestCase;
import lv.ctco.zephyr.enums.TestStatus;
import lv.ctco.zephyr.util.Utils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JiraTCCreationRunner {

    private static String REPORT_DIRECTORY = ConfigReader.getValueByKey("reportDirectory");
    private RestHelper restHelper;
    private List<ITestCase> testCasesList = new ArrayList<ITestCase>();
    protected static final Logger LOG = Logger.getLogger(JiraTCCreationRunner.class);

    public static void main(String[] args) throws Exception {
        JiraTCCreationRunner j = new JiraTCCreationRunner();
        j.run();
    }

    public void run() {
        restHelper = new RestHelper();
        LOG.info("Create list of Test Suites");
        if (ConfigReader.getValueByKey("reportType").toLowerCase().equals("allure")) {
            try {
                testCasesList = Utils.readAllureReport(new File(REPORT_DIRECTORY));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LOG.info("For test case");
        for (ITestCase currentTestCase : testCasesList) {
            createTestCaseAndLinkToTestCycleAndSetStatus(currentTestCase);
        }
    }

    private void updateTestCaseExecutionStatus(String testCaseId, TestStatus testCaseStatus) {
        LOG.info("Update test execution status");
        restHelper.updateExecutionStatus(testCaseStatus.getId(), restHelper.getExecutionId(testCaseId));
    }

    public void createTestCaseAndLinkToTestCycleAndSetStatus(ITestCase testCase) {
        String testCaseId = restHelper.getIssueId(getTestCaseKey(testCase));
        assignToTestCycleAndSetStatus(testCaseId, testCase);
    }


    public void assignToTestCycleAndSetStatus(String testCaseId, ITestCase testCase) {
        LOG.info("Assign Test Case to Test cycle");
        restHelper.addToTestCycle(testCaseId);
        updateTestCaseExecutionStatus(testCaseId, testCase.getStatus());
    }

    private String getTestCaseKey(ITestCase testCase) {
        LOG.info("Get Jira Key from the report");
        if (!(testCase.getTestCaseKey() == null)) {
            return testCase.getTestCaseKey();
        }
        LOG.info("Jira Key does not exists, new Test Case should be created");
        return restHelper.createTestCase(testCase.getName(), testCase.getSeverity().getIndex());
    }
}
