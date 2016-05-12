package lv.ctco.tmm.impl;

import lv.ctco.zephyr.beans.ITestCase;
import lv.ctco.zephyr.enums.TestLevel;
import lv.ctco.zephyr.enums.TestStatus;
import ru.yandex.qatools.allure.model.Label;
import ru.yandex.qatools.allure.model.SeverityLevel;
import ru.yandex.qatools.allure.model.TestCaseResult;

import java.util.List;

public class TestCaseAllureImpl implements ITestCase{

    private TestCaseResult testCaseResult;
    private static String TEST_CASE_ID_LABEL = "testId";

    public TestCaseAllureImpl(TestCaseResult testCaseResult){
        this.testCaseResult=testCaseResult;
    }

    public TestStatus getStatus() {
        switch (testCaseResult.getStatus()){
            case FAILED: return TestStatus.FAILED;
            case BROKEN: return TestStatus.FAILED;
            case PASSED: return TestStatus.PASSED;
            default: return TestStatus.NOT_EXECUTED;
        }
    }

    public String getTestCaseKey() {
        List<Label> labels = testCaseResult.getLabels();
        for (Label currentLabel : labels) {
            if (currentLabel.getName().equals(TEST_CASE_ID_LABEL) && !currentLabel.getValue().isEmpty()) {
                return currentLabel.getValue();
            }
        }
        return null;
    }

    public TestLevel getSeverity() {
        String severity="";

        for (Label currentLabel : testCaseResult.getLabels()) {
            if (currentLabel.getName().equals("severity") && !currentLabel.getValue().isEmpty()) {
                severity=currentLabel.getValue();
            }
        }
        if (!(severity.isEmpty())){
            switch (SeverityLevel.fromValue(severity)) {
                case TRIVIAL: return TestLevel.TRIVIAL;
                case MINOR: return TestLevel.MINOR;
                case CRITICAL: return TestLevel.CRITICAL;
                case BLOCKER: return TestLevel.BLOCKER;
                default: return TestLevel.MAJOR;
            }

        }
        return TestLevel.MAJOR;
    }

    public String getName() {
        return testCaseResult.getName();
    }
}
