package lv.ctco.zephyr.transformer;

import lv.ctco.zephyr.beans.TestCase;
import lv.ctco.zephyr.beans.testresult.cucumber.Feature;
import lv.ctco.zephyr.beans.testresult.cucumber.Scenario;
import lv.ctco.zephyr.beans.testresult.cucumber.Step;
import lv.ctco.zephyr.beans.testresult.cucumber.Tag;
import lv.ctco.zephyr.enums.TestStatus;
import lv.ctco.zephyr.util.ObjectTransformer;

import java.util.ArrayList;
import java.util.List;

import static lv.ctco.zephyr.util.Utils.normalizeKey;

public class CucumberTransformer extends ObjectTransformer {

    public static List<TestCase> transform(String response) {
        List<Feature> features = deserializeList(response, Feature.class);
        List<TestCase> testCases = new ArrayList<TestCase>();

        for (Feature feature : features) {
            for (Scenario scenario : feature.getScenarios()) {
                TestCase test = new TestCase();
                test.setName(scenario.getName());
                test.setUniqueId(generateUniqueId(feature, scenario));
                test.setStoryKeys(resolveStoryKeys(scenario));
                test.setStatus(resolveStatus(scenario));
                testCases.add(test);
            }
        }
        return testCases;
    }

    public static String generateUniqueId(Feature feature, Scenario scenario) {
        return String.join("-", normalizeKey(feature.getName()), normalizeKey(scenario.getName()));
    }

    private static TestStatus resolveStatus(Scenario scenario) {
        for (Step step : scenario.getSteps()) {
            if (!step.getResult().getStatus().equals("passed")) {
                return TestStatus.FAILED;
            }
        }
        return TestStatus.PASSED;
    }

    private static List<String> resolveStoryKeys(Scenario scenario) {
        Tag[] tags = scenario.getTags();
        if (tags == null || tags.length == 0) return null;

        List<String> result = new ArrayList<String>();
        for (Tag tag : tags) {
            String tagName = tag.getName();

            String prefix = "@Story=";
            if (tagName.toLowerCase().startsWith(prefix.toLowerCase())) {
                result.add(tagName.substring(prefix.length()).trim());
            }
        }
        return result.size() != 0 ? result : null;
    }
}