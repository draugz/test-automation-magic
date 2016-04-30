package lv.ctco.zephyr.enums;

public enum TestLevel {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High"),
    CRITICAL("Critical");

    private String name;

    TestLevel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}