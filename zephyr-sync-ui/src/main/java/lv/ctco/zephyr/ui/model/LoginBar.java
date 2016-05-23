package lv.ctco.zephyr.ui.model;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Created by S7T4M5 on 2016.05.23..
 */
public class LoginBar extends BaseModel{

    private HBox box = new HBox(10);
    private Label lblLogin = new Label("Login:");
    private TextField txtLogin = new TextField();

    public LoginBar() {
        this.box.setPadding(new Insets(0, 0, 0, 0));
        box.getChildren().add(lblLogin);
        box.getChildren().add(txtLogin);
    }

    public HBox getBox() {
        return box;
    }

    public void setBox(HBox box) {
        this.box = box;
    }

    public Label getLblLogin() {
        return lblLogin;
    }

    public void setLblLogin(Label lblLogin) {
        this.lblLogin = lblLogin;
    }

    public TextField getTxtLogin() {
        return txtLogin;
    }

    public void setTxtLogin(TextField txtLogin) {
        this.txtLogin = txtLogin;
    }
}
