package lv.ctco.zephyr.ui.model;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Created by S7T4M5 on 2016.05.23..
 */
public class ReportPathBar{

    private HBox box = new HBox(10);
    private Label lblProvideReportPath = new Label("Please provide path for report directory:");
    private TextField txtReportPath = new TextField();

    public ReportPathBar() {
        box.getChildren().add(lblProvideReportPath);
        box.getChildren().add(txtReportPath);
    }

    public HBox getBox() {
        return box;
    }

    public String getTxtReportPathValue() {
        return txtReportPath.getText();
    }

}
