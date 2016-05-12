package lv.ctco.tmm;

import lv.ctco.zephyr.enums.TestLevel;
import lv.ctco.zephyr.enums.TestStatus;

import java.util.Date;

import static lv.ctco.zephyr.enums.TestStatus.NOT_EXECUTED;

public class CucumberTestCase {

    private Integer id;
    private String key;
    private String uniqueId;
    private String name;
    private TestStatus status = NOT_EXECUTED;
    private TestLevel severity;
    private TestLevel priority = TestLevel.MEDIUM;
    private Date creationDate;

    public CucumberTestCase(String name, TestStatus status, TestLevel priority, String key, Date creationDate) {
        this.name = name;
        this.status = status;
        this.priority = priority;
        this.key = key;
        this.creationDate = creationDate;
    }

    public CucumberTestCase(String name, TestStatus status, String key) {

        this.name = name;
        this.status = status;
        this.key = key;
    }

    public CucumberTestCase() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String id) {
        this.key = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public TestStatus getStatus() {
        return status;
    }

    public void setStatus(TestStatus status) {
        this.status = status;
    }

    public TestLevel getSeverity() {
        return severity;
    }

    public void setSeverity(TestLevel severity) {
        this.severity = severity;
    }

    public TestLevel getPriority() {
        return priority;
    }

    public void setPriority(TestLevel priority) {
        this.priority = priority;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
