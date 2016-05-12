package lv.ctco.zephyr.beans.testcase;

import lv.ctco.zephyr.beans.TestCase;
import lv.ctco.zephyr.enums.TestLevel;
import lv.ctco.zephyr.enums.TestStatus;
import ru.yandex.qatools.allure.model.Label;
import ru.yandex.qatools.allure.model.SeverityLevel;
import ru.yandex.qatools.allure.model.TestCaseResult;

import java.util.List;

public class AllureTestCase implements TestCase {

    private TestCaseResult testCaseResult;
    private static String TEST_CASE_ID_LABEL = "testId";

    public AllureTestCase(TestCaseResult testCaseResult){
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

    public String getKey() {
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

    public Integer getId() {
        return null;
    }

    public void setId(Integer id) {

    }

    public void setStatus(TestStatus status) {

    }

    public String getUniqueId() {
        return null;
    }

    public void setUniqueId(String uniqueId) {

    }

    public void setKey(String key) {

    }

    public void setSeverity(TestLevel severity) {

    }

    public void setName(String name) {

    }

    public TestLevel getPriority() {
        return null;
    }

    public void setPriority(TestLevel priority) {

    }
}
