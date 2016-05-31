package lv.ctco.zephyr.enums;

public enum CssStyle {

    ERROR_STYLE("error-massage");

    private String name;

    CssStyle(String s) {
        this.name = s;
    }

    public String getName() {
        return name;
    }
}
