package lv.ctco.zephyr.beans;

import java.util.List;

public class TestStep {

    private int level;
    private String description;
    private List<TestStep> steps = null;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TestStep> getSteps() {
        return steps;
    }

    public void setSteps(List<TestStep> steps) {
        this.steps = steps;
    }
}