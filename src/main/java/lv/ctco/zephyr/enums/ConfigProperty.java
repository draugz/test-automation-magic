package lv.ctco.zephyr.enums;

public enum ConfigProperty {
    JIRA_URL("jira.base.path"),
    PROJECT_KEY("jira.project.key"),
    RELEASE_VERSION("jira.release.version"),
    TEST_CYCLE("jira.test.cycle"),
    USERNAME("auth.username"),
    PASSWORD("auth.password");

    private String name;

    ConfigProperty(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}