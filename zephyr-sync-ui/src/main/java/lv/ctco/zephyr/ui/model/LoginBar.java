package lv.ctco.zephyr.ui.model;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

/**
 * Created by S7T4M5 on 2016.05.23..
 */
public class LoginBar{

    private HBox box = new HBox(10);
    private Label lblLogin = new Label("Login:");
    private TextField txtLogin = new TextField();

    public LoginBar() {
        box.getChildren().add(lblLogin);
        box.getChildren().add(txtLogin);
    }

    public HBox getBox() {
        return box;
    }

    public String getTxtLoginValue(){
        return txtLogin.getText();
    }

}
