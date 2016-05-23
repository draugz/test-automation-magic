package lv.ctco.zephyr.enums;

public enum ConfigProperty {
    USERNAME(0),
    PASSWORD(1),
    REPORT_TYPE(2),
    PROJECT_KEY(3),
    RELEASE_VERSION(4),
    TEST_CYCLE(5),
    JIRA_URL(6),
    REPORT_PATH(7),
    ORDERED_STEPS(8),
    FORCE_STORY_LINK(9);

    private int id;

    ConfigProperty(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static ConfigProperty findById(int id) {
        for (ConfigProperty property : values()) {
            if (property.getId() == id) {
                return property;
            }
        }
        throw new RuntimeException("Unsupported parameter is passed");
    }
}