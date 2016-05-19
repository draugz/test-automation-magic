package lv.ctco.zephyr.transformer;

import lv.ctco.zephyr.beans.TestCase;
import lv.ctco.zephyr.beans.testresult.cucumber.CucumberResult;
import lv.ctco.zephyr.beans.testresult.cucumber.Element;
import lv.ctco.zephyr.beans.testresult.cucumber.Step;
import lv.ctco.zephyr.enums.TestStatus;
import lv.ctco.zephyr.util.ObjectTransformer;

import java.util.ArrayList;
import java.util.List;

import static lv.ctco.zephyr.util.Utils.normalizeKey;

public class CucumberTransformer extends ObjectTransformer {

    public static List<TestCase> transform(String response) {
        List<CucumberResult> features = deserializeList(response, CucumberResult.class);
        List<TestCase> testCases = new ArrayList<TestCase>();

        for (CucumberResult feature : features) {
            for (Element scenario : feature.getElements()) {
                TestCase test = new TestCase();
                test.setName(scenario.getName());
                test.setUniqueId(generateUniqueId(feature, scenario));
                test.setStatus(resolveStatus(scenario));
                testCases.add(test);
            }
        }
        return testCases;
    }

    public static String generateUniqueId(CucumberResult feature, Element scenario) {
        return String.join("-", normalizeKey(feature.getName()), normalizeKey(scenario.getName()));
    }

    private static TestStatus resolveStatus(Element scenario) {
        for (Step step : scenario.getSteps()) {
            if (!step.getResult().getStatus().equals("passed")) {
                return TestStatus.FAILED;
            }
        }
        return TestStatus.PASSED;
    }
}