package lv.ctco.zephyr.beans.testresult.cucumber;

public class CucumberResult {
    public long line;
    public Element elements[];
    public String name;
    public String description;
    public String id;
    public String keyword;
    public String uri;

    public CucumberResult() {
    }

    public long getLine() {
        return line;
    }

    public Element[] getElements() {
        return elements;
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

    public String getKeyword() {
        return keyword;
    }

    public String getUri() {
        return uri;
    }
}

