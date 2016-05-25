package lv.ctco.zephyr.beans.jira;

import lv.ctco.zephyr.beans.Metafield;

import java.util.List;

public class Fields {
    private String summary;
    private String environment;
    private Metafield project;
    private Metafield assignee;
    private Metafield issuetype;
    private Metafield priority;
    private Metafield customfield_10067;
    private List<Metafield> versions;
    private String[] labels;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public Metafield getProject() {
        return project;
    }

    public void setProject(Metafield project) {
        this.project = project;
    }

    public Metafield getAssignee() {
        return assignee;
    }

    public void setAssignee(Metafield assignee) {
        this.assignee = assignee;
    }

    public Metafield getIssuetype() {
        return issuetype;
    }

    public void setIssuetype(Metafield issuetype) {
        this.issuetype = issuetype;
    }

    public List<Metafield> getVersions() {
        return versions;
    }

    public void setVersions(List<Metafield> versions) {
        this.versions = versions;
    }

    public Metafield getPriority() {
        return priority;
    }

    public void setPriority(Metafield priority) {
        this.priority = priority;
    }

    public Metafield getSeverity() {
        return customfield_10067;
    }

    public void setSeverity(Metafield severity) {
        this.customfield_10067 = severity;
    }

    public String[] getLabels() {
        return labels;
    }

    public void setLabels(String[] labels) {
        this.labels = labels;
    }
}
