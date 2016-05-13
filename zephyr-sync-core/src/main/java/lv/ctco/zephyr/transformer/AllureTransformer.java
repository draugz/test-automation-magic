package lv.ctco.zephyr.transformer;

import lv.ctco.zephyr.beans.TestCase;
import lv.ctco.zephyr.beans.testcase.AllureTestCase;
import lv.ctco.zephyr.enums.TestLevel;
import lv.ctco.zephyr.enums.TestStatus;
import ru.yandex.qatools.allure.model.Label;
import ru.yandex.qatools.allure.model.SeverityLevel;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import java.util.ArrayList;
import java.util.List;

public class AllureTransformer {

    public static List<TestCase> transform(List<TestSuiteResult> result) {
        List<TestCase> testCases = new ArrayList<TestCase>();
        for (TestSuiteResult currentTestSuiteResult : result) {
            for (TestCaseResult currentTestCaseResult : currentTestSuiteResult.getTestCases()) {
                TestCase currentTestCase = new AllureTestCase();
                currentTestCase.setName(currentTestCaseResult.getName());
                currentTestCase.setUniqueId(currentTestCaseResult.getName());
                currentTestCase.setStatus(getStatusFromAllureTestCase(currentTestCaseResult));
                currentTestCase.setSeverity(getSeverityFromAllureTestCase(currentTestCaseResult));
                testCases.add(currentTestCase);
            }
        }
        return testCases;
    }

    private static String getUniqueIdFromAllureTestCase(TestCaseResult testCaseResult) {
        List<Label> labels = testCaseResult.getLabels();
        for (Label currentLabel : labels) {
            if (currentLabel.getName().equals("testId") && !currentLabel.getValue().isEmpty()) {
                return currentLabel.getValue();
            }
        }
        return null;
    }

    private static TestStatus getStatusFromAllureTestCase(TestCaseResult testCaseResult) {
        switch (testCaseResult.getStatus()) {
            case FAILED:
                return TestStatus.FAILED;
            case BROKEN:
                return TestStatus.FAILED;
            case PASSED:
                return TestStatus.PASSED;
            default:
                return TestStatus.NOT_EXECUTED;
        }
    }

    private static TestLevel getSeverityFromAllureTestCase(TestCaseResult testCaseResult) {
        String severity = "";

        for (Label currentLabel : testCaseResult.getLabels()) {
            if (currentLabel.getName().equals("severity") && !currentLabel.getValue().isEmpty()) {
                severity = currentLabel.getValue();
            }
        }
        if (!(severity.isEmpty())) {
            switch (SeverityLevel.fromValue(severity)) {
                case TRIVIAL:
                    return TestLevel.TRIVIAL;
                case MINOR:
                    return TestLevel.MINOR;
                case CRITICAL:
                    return TestLevel.CRITICAL;
                case BLOCKER:
                    return TestLevel.BLOCKER;
                default:
                    return TestLevel.MAJOR;
            }

        }
        return null;
    }
}