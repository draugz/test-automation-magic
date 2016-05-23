package lv.ctco.zephyr.ui.model;

import javafx.scene.control.TextField;
import lv.ctco.zephyr.ui.enums.CssStyle;

import javax.naming.ldap.Control;

/**
 * Created by S7T4M5 on 2016.05.23..
 */
public class BaseModel {

    public void setErrorStyle(TextField control){
        control.getStyleClass().add(CssStyle.ERROR_STYLE.getName());
    }
}
