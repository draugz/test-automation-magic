package lv.ctco.zephyr.beans.zapi;

import lv.ctco.zephyr.beans.Metafield;

public class Execution {

    private int id;
    private String issueKey;
    private String projectId;
    private String versionId;
    private int issueId;
    private int orderId;
    private String cycleId;
    private int method;
    private Metafield status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIssueKey() {
        return issueKey;
    }

    public void setIssueKey(String issueKey) {
        this.issueKey = issueKey;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public int getIssueId() {
        return issueId;
    }

    public void setIssueId(int issueId) {
        this.issueId = issueId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getCycleId() {
        return cycleId;
    }

    public void setCycleId(String cycleId) {
        this.cycleId = cycleId;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public Metafield getStatus() {
        return status;
    }

    public void setStatus(Metafield status) {
        this.status = status;
    }
}
