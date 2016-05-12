package lv.ctco.zephyr.util;

import lv.ctco.zephyr.beans.TestCase;
import lv.ctco.zephyr.beans.testcase.AllureTestCase;
import lv.ctco.zephyr.beans.testcase.JUnitTestCase;
import lv.ctco.zephyr.beans.testresult.junit.JUnitResult;
import lv.ctco.zephyr.beans.testresult.junit.JUnitResultTestSuite;
import lv.ctco.zephyr.enums.TestStatus;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import java.util.ArrayList;
import java.util.List;

import static lv.ctco.zephyr.util.Utils.generateJiraKey;

public class ReportTransformer {

    public static List<TestCase> transform(JUnitResultTestSuite resultTestSuite) {
        if (resultTestSuite.getTestcase() == null) {
            return new ArrayList<TestCase>();
        }

        List<TestCase> result = new ArrayList<TestCase>();
        for (JUnitResult testCase : resultTestSuite.getTestcase()) {
            TestCase test = new JUnitTestCase();
            test.setName(testCase.getName());
            test.setUniqueId(generateJiraKey(testCase));
            test.setStatus(testCase.getError() != null || testCase.getFailure() != null ? TestStatus.FAILED : TestStatus.PASSED);
            result.add(test);
        }
        return result;
    }

    public static List<TestCase> transform(List<TestSuiteResult> result) {
        List<TestCase> testCases = new ArrayList<TestCase>();
        for (TestSuiteResult currentTestSuiteResult: result){
            for (TestCaseResult currentTestCaseResult : currentTestSuiteResult.getTestCases()) {
                testCases.add(new AllureTestCase(currentTestCaseResult));
            }
        }
        return testCases;
    }
}