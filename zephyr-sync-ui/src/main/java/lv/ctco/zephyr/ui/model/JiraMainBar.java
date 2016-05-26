package lv.ctco.zephyr.ui.model;

import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Created by S7T4M5 on 2016.05.23..
 */
public class JiraMainBar{

    private HBox box = new HBox(10);
    private VBox keyBox=new VBox(10);
    private VBox versionBox=new VBox(10);
    private VBox cycleBox=new VBox(10);

    private Label lblProjectKey = new Label("Project key: ");
    private TextField txtProjectKey = new TextField();
    private Label lblProjectVersion = new Label("Project version: ");
    private TextField txtProjectVersion = new TextField();
    private Label lblTestCycle = new Label("Test cycle:");
    private TextField txtTestCycle = new TextField();

    public JiraMainBar() {
        box.getChildren().addAll(keyBox, versionBox, cycleBox);
        txtProjectKey.setPrefWidth(178);
        txtProjectVersion.setPrefWidth(178);
        txtTestCycle.setPrefWidth(178);

        keyBox.getChildren().addAll(lblProjectKey, txtProjectKey);
        versionBox.getChildren().addAll(lblProjectVersion, txtProjectVersion);
        cycleBox.getChildren().addAll(lblTestCycle, txtTestCycle);
    }

    public HBox getBox() {
        return box;
    }

    public String getProjectKeyValue() {
        return txtProjectKey.getText();
    }

    public String getProjectVersionValue() {
        return txtProjectVersion.getText();
    }

    public String getTestCycleValue() {
        return txtTestCycle.getText();
    }
}
