package lv.ctco.zephyr.ui.model;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;

/**
 * Created by S7T4M5 on 2016.05.23..
 */
public class PasswordBar{

    private HBox box = new HBox(10);
    private Label lblPassword = new Label("Password:");
    private PasswordField pswPassword = new PasswordField();

    public PasswordBar() {
        box.getChildren().add(lblPassword);
        box.getChildren().add(pswPassword);
    }

    public HBox getBox() {
        return box;
    }

    public String getPswPasswordValue() {
        return pswPassword.getText();
    }

}
