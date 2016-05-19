package lv.ctco.zephyr.beans.testresult.cucumber;

public class Element {
    public Before before[];
    public long line;
    public String name;
    public String description;
    public String id;
    public After after[];
    public String type;
    public String keyword;
    public Step steps[];

    public Element() {
    }

    public Before[] getBefore() {
        return before;
    }

    public long getLine() {
        return line;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public After[] getAfter() {
        return after;
    }

    public String getType() {
        return type;
    }

    public String getKeyword() {
        return keyword;
    }

    public Step[] getSteps() {
        return steps;
    }
}
