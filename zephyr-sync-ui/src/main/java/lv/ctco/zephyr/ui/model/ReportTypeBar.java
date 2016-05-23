package lv.ctco.zephyr.ui.model;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

/**
 * Created by S7T4M5 on 2016.05.23..
 */
public class ReportTypeBar {

    private HBox box = new HBox(10);
    private Label lblSelectReportType = new Label("Please select report type:");
    private RadioButton rbtAllureReport = new RadioButton("Allure");
    private RadioButton rbtJunitReport = new RadioButton("JUnit");
    private RadioButton rbtCucumberReport = new RadioButton("Cucumber");

    public ReportTypeBar() {
        this.box.setPadding(new Insets(0, 0, 0, 0));
        box.getChildren().add(lblSelectReportType);
        box.getChildren().add(rbtAllureReport);
        box.getChildren().add(rbtJunitReport);
        box.getChildren().add(rbtCucumberReport);
    }

    public HBox getBox() {
        return box;
    }

    public void setBox(HBox box) {
        this.box = box;
    }

    public Label getLblSelectReportType() {
        return lblSelectReportType;
    }

    public void setLblSelectReportType(Label lblSelectReportType) {
        this.lblSelectReportType = lblSelectReportType;
    }

    public RadioButton getRbtAllureReport() {
        return rbtAllureReport;
    }

    public void setRbtAllureReport(RadioButton rbtAllureReport) {
        this.rbtAllureReport = rbtAllureReport;
    }

    public RadioButton getRbtJunitReport() {
        return rbtJunitReport;
    }

    public void setRbtJunitReport(RadioButton rbtJunitReport) {
        this.rbtJunitReport = rbtJunitReport;
    }

    public RadioButton getRbtCucumberReport() {
        return rbtCucumberReport;
    }

    public void setRbtCucumberReport(RadioButton rbtCucumberReport) {
        this.rbtCucumberReport = rbtCucumberReport;
    }

}
