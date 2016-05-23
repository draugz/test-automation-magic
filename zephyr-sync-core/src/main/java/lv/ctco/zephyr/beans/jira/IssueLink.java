package lv.ctco.zephyr.beans.jira;

import lv.ctco.zephyr.beans.Metafield;

public class IssueLink {

    private Metafield type;
    private Metafield inwardIssue;
    private Metafield outwardIssue;

    public IssueLink(String source, String target) {
        this.type = new Metafield();
        this.type.setName("Reference");
        this.inwardIssue = new Metafield();
        this.inwardIssue.setKey(source);
        this.outwardIssue = new Metafield();
        this.outwardIssue.setKey(target);
    }
}
