package lv.ctco.zephyr.ui.model;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import lv.ctco.zephyr.enums.ReportType;

/**
 * Created by S7T4M5 on 2016.05.23..
 */
public class ReportTypeBar {

    private HBox box = new HBox(20);
    private HBox label=new HBox(10);
    private HBox checkboxes =new HBox(10);
    private Label lblSelectReportType = new Label("Please select report type:");
    private RadioButton rbtAllureReport = new RadioButton("Allure");
    private RadioButton rbtJunitReport = new RadioButton("JUnit");
    private RadioButton rbtCucumberReport = new RadioButton("Cucumber");
    private ToggleGroup toggleGroup = new ToggleGroup();

    public ReportTypeBar() {
        attachRadioButtonsToGroup();
        box.getChildren().addAll(label, checkboxes);
        label.getChildren().add(lblSelectReportType);
        checkboxes.getChildren().addAll(rbtAllureReport, rbtJunitReport, rbtCucumberReport);
        rbtAllureReport.setSelected(true);
        label.setAlignment(Pos.CENTER_LEFT);
    }

    public HBox getBox() {
        return box;
    }

    private void attachRadioButtonsToGroup(){
        rbtAllureReport.setToggleGroup(toggleGroup);
        rbtJunitReport.setToggleGroup(toggleGroup);
        rbtCucumberReport.setToggleGroup(toggleGroup);
    }

    public String getReportType(){
        if (rbtAllureReport.isSelected()){
            return ReportType.ALLURE.getName();
        } else if (rbtJunitReport.isSelected()){
            return ReportType.JUNIT.getName();
        } else {
            return ReportType.CUCUMBER.getName();
        }
    }

}
