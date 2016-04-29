package lv.ctco.tmm;

import java.util.Date;

/**
 * Created by S7T4M5 on 2016.04.22..
 */
public class TestCase {

    private int jiraId;
    private String jiraKey;
    private String name;
    private String status;
    private String severity;
    private String priority;
    private Date creationDate;

    public TestCase(String name, String status, String priority, String jiraKey, Date creationDate) {
        this.name = name;
        this.status = status;
        this.priority = priority;
        this.jiraKey = jiraKey;
        this.creationDate = creationDate;
    }

    public TestCase(String name, String status, String jiraKey) {

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
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

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }
}
