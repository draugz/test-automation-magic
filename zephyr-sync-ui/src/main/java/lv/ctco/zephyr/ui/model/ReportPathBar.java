package lv.ctco.zephyr.ui.model;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Created by S7T4M5 on 2016.05.23..
 */
public class ReportPathBar extends BaseModel{

    private HBox box = new HBox(10);
    private Label lblProvideReportPath = new Label("Please provide path for report directory:");
    private TextField txtReportPath = new TextField();

    public ReportPathBar() {
        this.box.setPadding(new Insets(0, 0, 0, 0));
        box.getChildren().add(lblProvideReportPath);
        box.getChildren().add(txtReportPath);
    }

    public HBox getBox() {
        return box;
    }

    public void setBox(HBox box) {
        this.box = box;
    }

    public Label getLblProvideReportPath() {
        return lblProvideReportPath;
    }

    public void setLblProvideReportPath(Label lblProvideReportPath) {
        this.lblProvideReportPath = lblProvideReportPath;
    }

    public TextField getTxtReportPath() {
        return txtReportPath;
    }

    public void setTxtReportPath(TextField txtReportPath) {
        this.txtReportPath = txtReportPath;
    }
}
