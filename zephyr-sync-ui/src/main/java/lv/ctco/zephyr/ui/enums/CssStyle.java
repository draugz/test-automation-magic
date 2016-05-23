package lv.ctco.zephyr.ui.enums;

/**
 * Created by S7T4M5 on 2016.05.23..
 */
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