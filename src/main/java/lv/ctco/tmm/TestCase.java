package lv.ctco.tmm;

import lv.ctco.zephyr.enums.TestLevel;
import lv.ctco.zephyr.enums.TestStatus;

import java.util.Date;

import static lv.ctco.zephyr.enums.TestStatus.NOT_EXECUTED;

/**
 * Created by S7T4M5 on 2016.04.22..
 */
public class TestCase {

    private String id;
    private int internalId;
    private String uniqueId;
    private String name;
    private TestStatus status = NOT_EXECUTED;
    private TestLevel severity;
    private TestLevel priority = TestLevel.MEDIUM;
    private Date creationDate;

    public TestCase(String name, TestStatus status, TestLevel priority, String id, Date creationDate) {
        this.name = name;
        this.status = status;
        this.priority = priority;
        this.id = id;
        this.creationDate = creationDate;
    }

    public TestCase(String name, TestStatus status, String id) {

        this.name = name;
        this.status = status;
        this.id = id;
    }

    public TestCase() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getInternalId() {
        return internalId;
    }

    public void setInternalId(int internalId) {
        this.internalId = internalId;
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
