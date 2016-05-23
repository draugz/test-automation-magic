package lv.ctco.zephyr.ui.model;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Created by S7T4M5 on 2016.05.23..
 */
public class PasswordBar extends BaseModel{

    private HBox box = new HBox(10);
    private Label lblPassword = new Label("Password:");
    private PasswordField pswPassword = new PasswordField();

    public PasswordBar() {
        this.box.setPadding(new Insets(0, 0, 0, 0));
        box.getChildren().add(lblPassword);
        box.getChildren().add(pswPassword);
    }

    public HBox getBox() {
        return box;
    }

    public void setBox(HBox box) {
        this.box = box;
    }

    public Label getLblPassword() {
        return lblPassword;
    }

    public void setLblPassword(Label lblPassword) {
        this.lblPassword = lblPassword;
    }

    public PasswordField getPswPassword() {
        return pswPassword;
    }

    public void setPswPassword(PasswordField pswPassword) {
        this.pswPassword = pswPassword;
    }
}
