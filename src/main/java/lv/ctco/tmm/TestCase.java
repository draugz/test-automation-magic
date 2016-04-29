package lv.ctco.tmm;

import lv.ctco.zephyr.enums.TestLevel;
import lv.ctco.zephyr.enums.TestStatus;

import java.util.Date;

import static lv.ctco.zephyr.enums.TestStatus.NOT_EXECUTED;

/**
 * Created by S7T4M5 on 2016.04.22..
 */
public class TestCase {

    private int jiraId;
    private String jiraKey;
    private String name;
    private TestStatus status = NOT_EXECUTED;
    private TestLevel severity;
    private TestLevel priority = TestLevel.MEDIUM;
    private Date creationDate;

    public TestCase(String name, TestStatus status, TestLevel priority, String jiraKey, Date creationDate) {
        this.name = name;
        this.status = status;
        this.priority = priority;
        this.jiraKey = jiraKey;
        this.creationDate = creationDate;
    }

    public TestCase(String name, TestStatus status, String jiraKey) {

        this.name = name;
        this.status = status;
        this.jiraKey = jiraKey;
    }

    public TestCase() {
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

    public TestLevel getPriority() {
        return priority;
    }

    public void setPriority(TestLevel priority) {
        this.priority = priority;
    }

    public String getJiraKey() {
        return jiraKey;
    }

    public void setJiraKey(String jiraKey) {
        this.jiraKey = jiraKey;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public int getJiraId() {
        return jiraId;
    }

    public void setJiraId(int jiraId) {
        this.jiraId = jiraId;
    }

    public TestLevel getSeverity() {
        return severity;
    }

    public void setSeverity(TestLevel severity) {
        this.severity = severity;
    }
}
