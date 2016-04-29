package lv.ctco.zephyr;

import lv.ctco.zephyr.beans.ResultTestCase;
import lv.ctco.zephyr.beans.ResultTestSuite;
import lv.ctco.zephyr.util.Utils;

import java.util.List;

public class Runner {
    public static void main(String[] args) throws Exception {
        ResultTestSuite resultTestSuite = Utils.readJunitXML("junit.xml");

        List<ResultTestCase> testCases = resultTestSuite.getTestcase();
        if (testCases == null || testCases.size() == 0) return;

        for (ResultTestCase testCase : testCases) {
            String name = testCase.getName();
        }
    }
}
