package lv.ctco.zephyr.util;

import lv.ctco.zephyr.beans.TestCase;
import lv.ctco.zephyr.beans.testcase.AllureTestCase;
import lv.ctco.zephyr.beans.testcase.JUnitTestCase;
import lv.ctco.zephyr.beans.testresult.junit.JUnitResult;
import lv.ctco.zephyr.beans.testresult.junit.JUnitResultTestSuite;
import lv.ctco.zephyr.enums.TestLevel;
import lv.ctco.zephyr.enums.TestStatus;
import ru.yandex.qatools.allure.model.Label;
import ru.yandex.qatools.allure.model.SeverityLevel;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import java.util.ArrayList;
import java.util.List;

import static lv.ctco.zephyr.util.Utils.normalizeKey;

public class ReportTransformer {

    public static List<TestCase> transform(JUnitResultTestSuite resultTestSuite) {
        if (resultTestSuite.getTestcase() == null) {
            return new ArrayList<TestCase>();
        }

        List<TestCase> result = new ArrayList<TestCase>();
        for (JUnitResult testCase : resultTestSuite.getTestcase()) {
            TestCase test = new JUnitTestCase();
            test.setName(testCase.getName());
            test.setUniqueId(getUniqueIdFromJUnitTestCase(testCase));
            test.setStatus(testCase.getError() != null || testCase.getFailure() != null ? TestStatus.FAILED : TestStatus.PASSED);
            result.add(test);
        }
        return result;
    }

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

    public static String getUniqueIdFromJUnitTestCase(JUnitResult testCase) {
        return String.join("-", normalizeKey(testCase.getClassname()), normalizeKey(testCase.getName()));
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
        return TestLevel.MINOR;
    }
}