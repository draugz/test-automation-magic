package lv.ctco.zephyr.enums;

public enum ConfigProperty {
    JIRA_URL("jira.base.path"),
    PROJECT_KEY("jira.project.key"),
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