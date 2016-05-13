package lv.ctco.zephyr.enums;

public enum ReportType {
    JUNIT("junit"),
    ALLURE("allure");

    private String name;

    ReportType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ReportType findById(String id) {
        for (ReportType type : values()) {
            if (type.getName().equalsIgnoreCase(id)) return type;
        }
        return null;
    }
}