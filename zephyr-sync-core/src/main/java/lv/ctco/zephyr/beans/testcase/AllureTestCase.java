package lv.ctco.zephyr.beans.testcase;

import lv.ctco.zephyr.beans.TestCase;
import lv.ctco.zephyr.enums.TestLevel;
import lv.ctco.zephyr.enums.TestStatus;
import ru.yandex.qatools.allure.model.Label;
import ru.yandex.qatools.allure.model.SeverityLevel;
import ru.yandex.qatools.allure.model.TestCaseResult;

import java.util.List;

import static lv.ctco.zephyr.enums.TestStatus.NOT_EXECUTED;

public class AllureTestCase implements TestCase {

    private TestLevel priority = TestLevel.MEDIUM;
    private Integer id;
    private String key;
    private String uniqueId;
    private String name;
    private TestStatus status = NOT_EXECUTED;
    private TestLevel severity;


    public TestStatus getStatus() {
        return status;
    }

    public String getKey() {
        return key;
    }

    public TestLevel getSeverity() {
        return severity;
    }

    public TestLevel getPriority() {
        return priority;
    }

    public void setPriority(TestLevel priority) {
        this.priority = priority;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(TestStatus status) {
        this.status = status;
    }

    public void setSeverity(TestLevel severity) {
        this.severity = severity;
    }
}
