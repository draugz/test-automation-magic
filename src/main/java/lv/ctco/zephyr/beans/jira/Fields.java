package lv.ctco.zephyr.beans.jira;

import lv.ctco.zephyr.beans.Metafield;

import java.util.List;

public class Fields {
    private String summary;
    private String environment;
    private Metafield project;
    private Metafield issuetype;
    private Metafield priority;
    private List<Metafield> versions;

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
}
