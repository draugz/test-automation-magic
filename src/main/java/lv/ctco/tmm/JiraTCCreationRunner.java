package lv.ctco.tmm;

import lv.ctco.tmm.utils.ConfigReader;
import lv.ctco.tmm.utils.RestHelper;
import lv.ctco.zephyr.util.Utils;
import org.apache.log4j.Logger;
import ru.yandex.qatools.allure.model.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by S7T4M5 on 2016.04.23..
 */

public class JiraTCCreationRunner {

    private static String REPORT_DIRECTORY = ConfigReader.getValueByKey("reportDirectory");
    private static String TEST_CASE_ID_LABEL = "testId";
    private RestHelper restHelper;
    private List<TestSuiteResult> testSuiteResult = new ArrayList<TestSuiteResult>();
    protected static final Logger LOG = Logger.getLogger(JiraTCCreationRunner.class);

    public static void main(String[] args) throws Exception {
        JiraTCCreationRunner j = new JiraTCCreationRunner();
        j.run();
    }

    public void run() {
        restHelper=new RestHelper();
        LOG.info("Create list of Test Suites");
        try {
            testSuiteResult = Utils.reatAllureReport(new File(REPORT_DIRECTORY));
        } catch (IOException e) {
            e.printStackTrace();
        }

        LOG.info("For test suite");
        for (TestSuiteResult currentTestSuite : testSuiteResult) {
            LOG.info("For test case");
            for (TestCaseResult currentTestCase : currentTestSuite.getTestCases()) {
                createTestCaseAndLinkToTestCycleAndSetStatus(currentTestCase);
            }
        }
    }

    private void updateTestCaseExecutionStatus(String testCaseId, Status testCaseStatus) {
        LOG.info("Update test execution status");
        switch (testCaseStatus){
            case FAILED: restHelper.updateExecutionStatus(2, restHelper.getExecutionId(testCaseId));
                break;
            case BROKEN: restHelper.updateExecutionStatus(2, restHelper.getExecutionId(testCaseId));
                break;
            case PASSED: restHelper.updateExecutionStatus(1, restHelper.getExecutionId(testCaseId));
                break;
            default: restHelper.updateExecutionStatus(0, restHelper.getExecutionId(testCaseId));
        }
    }

    public void createTestCaseAndLinkToTestCycleAndSetStatus(TestCaseResult testCaseResult) {
        String testCaseId=restHelper.getIssueId(getTestCaseKey(testCaseResult));
        assignToTestCycleAndSetStatus(testCaseId, testCaseResult);
    }


    public void assignToTestCycleAndSetStatus(String testCaseId, TestCaseResult testCaseResult){
        LOG.info("Assign Test Case to Test cycle");
        restHelper.addToTestCycle(testCaseId);
        updateTestCaseExecutionStatus(testCaseId, testCaseResult.getStatus());
    }

    private String getTestCaseKey(TestCaseResult testCaseResult) {
        LOG.info("Get Jira Key from the report");
        List<Label> labels = testCaseResult.getLabels();
        for (Label currentLabel : labels) {
            if (currentLabel.getName().equals(TEST_CASE_ID_LABEL) && !currentLabel.getValue().isEmpty()) {
                return currentLabel.getValue();
            }
        }
        LOG.info("Jira Key does not exists, new Test Case should be created");
        int severityLevel=10122;

        String severity="";
        for (Label currentLabel : labels) {
            if (currentLabel.getName().equals("severity") && !currentLabel.getValue().isEmpty()) {
                severity=currentLabel.getValue();
            }
        }
        if (!(severity.isEmpty())){
            switch (SeverityLevel.fromValue(severity)) {
                case TRIVIAL:
                    severityLevel = 10124;
                    break;
                case MINOR:
                    severityLevel = 10123;
                    break;
                case CRITICAL:
                    severityLevel = 10121;
                    break;
                case BLOCKER:
                    severityLevel = 10120;
                    break;
                default:
                    severityLevel = 10122;
            }

        }
        return restHelper.createTestCase(testCaseResult.getName(), severityLevel);
    }
}
