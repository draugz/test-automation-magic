package lv.ctco.zephyr.ui.model;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Created by S7T4M5 on 2016.05.23..
 */
public class LoginBar{

    private HBox box = new HBox(10);
    private VBox labels = new VBox(10);
    private VBox textfields = new VBox(10);

    private Label lblLogin = new Label("Login:");
    private TextField txtLogin = new TextField();

    private Label lblPassword = new Label("Password:");
    private PasswordField pswPassword = new PasswordField();

    public LoginBar() {
        box.getChildren().addAll(labels, textfields);
        labels.setAlignment(Pos.CENTER_LEFT);
        labels.getChildren().addAll(lblLogin, lblPassword);
        textfields.getChildren().addAll(txtLogin, pswPassword);
    }

    public HBox getBox() {
        return box;
    }

    public String getTxtLoginValue(){
        return txtLogin.getText();
    }

    public String getPswPasswordValue() {
        return pswPassword.getText();
    }

}
