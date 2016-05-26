package lv.ctco.zephyr.ui.model;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

/**
 * Created by S7T4M5 on 2016.05.23..
 */
public class CheckBoxBar {

    private HBox box = new HBox(10);
    private VBox labels = new VBox(10);
    private VBox checkboxes = new VBox(10);

    private Label lblOrderedSteps = new Label("Ordered steps: ");
    private CheckBox cbxOrderedSteps = new CheckBox();
    private Label lblForcedStoryLink = new Label("Forces Story Link: ");
    private CheckBox cbxForcedStoryLink = new CheckBox();


    public CheckBoxBar() {
        box.getChildren().addAll(labels, checkboxes);
        labels.getChildren().addAll(lblOrderedSteps, lblForcedStoryLink);
        checkboxes.getChildren().addAll(cbxOrderedSteps, cbxForcedStoryLink);
        cbxOrderedSteps.setSelected(true);
    }

    public HBox getBox() {
        return box;
    }

    public String getOrderedStepsValue() {
        if (cbxOrderedSteps.isSelected()) {
            return "true";
        }
        return "false";
    }

    public String getForcedStoryLinkValue() {
        if (cbxForcedStoryLink.isSelected()) {
            return "true";
        }
        return "false";
    }
}
