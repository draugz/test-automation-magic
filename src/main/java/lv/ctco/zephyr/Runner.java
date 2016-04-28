package lv.ctco.zephyr;

import lv.ctco.zephyr.beans.ResultTestCase;
import lv.ctco.zephyr.beans.ResultTestSuite;

import java.util.List;

public class Runner {
    public static void main(String[] args) throws Exception {
        ResultTestSuite resultTestSuite = Utils.readJunitXML("junit.xml");

        List<ResultTestCase> testcase = resultTestSuite.getTestcase();
        for (ResultTestCase test : testcase) {
            String name = test.getName();
            if (test.getFailure() != null || test.getError() != null) {
                name += " > FAILED";
            }
            System.out.println(name);
        }
    }
}
