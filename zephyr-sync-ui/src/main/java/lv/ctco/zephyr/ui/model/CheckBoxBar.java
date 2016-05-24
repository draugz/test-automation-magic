package lv.ctco.zephyr.ui.model;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Created by S7T4M5 on 2016.05.23..
 */
public class CheckBoxBar {

    private VBox box = new VBox(10);
    private HBox orderStepsBox = new HBox(10);
    private HBox forcedStoryBox = new HBox(10);
    private Label lblOrderedSteps = new Label("Ordered steps: ");
    private CheckBox cbxOrderedSteps = new CheckBox();
    private Label lblForcedStoryLink = new Label("Forces Story Link: ");
    private CheckBox cbxForcedStoryLink = new CheckBox();

    public CheckBoxBar() {
        box.getChildren().add(orderStepsBox);
        box.getChildren().add(forcedStoryBox);
        orderStepsBox.getChildren().add(lblOrderedSteps);
        orderStepsBox.getChildren().add(cbxOrderedSteps);
        forcedStoryBox.getChildren().add(lblForcedStoryLink);
        forcedStoryBox.getChildren().add(cbxForcedStoryLink);
    }

    public VBox getBox() {
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
