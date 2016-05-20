package lv.ctco.zephyr.transformer;

import lv.ctco.zephyr.beans.TestCase;
import lv.ctco.zephyr.beans.TestStep;
import lv.ctco.zephyr.beans.testresult.cucumber.Feature;
import lv.ctco.zephyr.beans.testresult.cucumber.Scenario;
import lv.ctco.zephyr.beans.testresult.cucumber.Step;
import lv.ctco.zephyr.beans.testresult.cucumber.Tag;
import lv.ctco.zephyr.enums.TestStatus;
import lv.ctco.zephyr.util.ObjectTransformer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static lv.ctco.zephyr.util.Utils.normalizeKey;

public class CucumberTransformer extends ObjectTransformer {

    public static List<TestCase> transform(String response) {
        List<Feature> features = deserializeList(response, Feature.class);
        List<TestCase> testCases = new ArrayList<TestCase>();

        for (Feature feature : features) {
            for (Scenario scenario : feature.getScenarios()) {
                TestCase test = new TestCase();
                List<String> jiraKeys = resolveJiraKeys(scenario, "@TestCaseId=");
                if (jiraKeys != null && jiraKeys.size() == 1) {
                    test.setKey(jiraKeys.get(0));
                }
                test.setName(scenario.getName());
                test.setUniqueId(generateUniqueId(feature, scenario));
                test.setStoryKeys(resolveJiraKeys(scenario, "@Story="));
                test.setStatus(resolveStatus(scenario));
                test.setSteps(resolveTestSteps(scenario));
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

    private static List<String> resolveJiraKeys(Scenario scenario, String tagPrefix) {
        Tag[] tags = scenario.getTags();
        if (tags == null || tags.length == 0) return null;

        List<String> result = new ArrayList<String>();
        for (Tag tag : tags) {
            String tagName = tag.getName();

            if (tagName.toLowerCase().startsWith(tagPrefix.toLowerCase())) {
                String[] keys = tagName.substring(tagPrefix.length()).trim().split(",");
                Collections.addAll(result, keys);
            }
        }
        return result.size() != 0 ? result : null;
    }

    private static List<TestStep> resolveTestSteps(Scenario scenario) {
        List<TestStep> result = new ArrayList<TestStep>();
        for (Step step : scenario.getSteps()) {
            result.add(new TestStep(step.getKeyword() + step.getName()));
        }
        return result;
    }
}