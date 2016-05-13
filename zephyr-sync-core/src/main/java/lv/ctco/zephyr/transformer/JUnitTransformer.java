package lv.ctco.zephyr.transformer;

import lv.ctco.zephyr.beans.TestCase;
import lv.ctco.zephyr.beans.testcase.JUnitTestCase;
import lv.ctco.zephyr.beans.testresult.junit.JUnitResult;
import lv.ctco.zephyr.beans.testresult.junit.JUnitResultTestSuite;
import lv.ctco.zephyr.enums.TestStatus;

import java.util.ArrayList;
import java.util.List;

import static lv.ctco.zephyr.util.Utils.normalizeKey;

public class JUnitTransformer {

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

    public static String getUniqueIdFromJUnitTestCase(JUnitResult testCase) {
        return String.join("-", normalizeKey(testCase.getClassname()), normalizeKey(testCase.getName()));
    }
}