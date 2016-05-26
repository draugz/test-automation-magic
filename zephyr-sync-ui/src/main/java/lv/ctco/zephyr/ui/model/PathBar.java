package lv.ctco.zephyr.ui.model;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Created by S7T4M5 on 2016.05.23..
 */
public class PathBar {

    private HBox box = new HBox(10);

    private VBox labels = new VBox(10);
    private VBox textFields = new VBox(10);
    private Label lblProvideJiraUrl = new Label("Please provide JIRA URL:");
    private Label lblProvideReportPath = new Label("Please provide report path:");
    private TextField txtJiraUrl = new TextField("http://jira.swissre.com/rest/");
    private TextField txtReportPath = new TextField();

    public PathBar() {
        box.getChildren().addAll(labels, textFields);
        labels.getChildren().addAll(lblProvideJiraUrl, lblProvideReportPath);
        textFields.getChildren().addAll(txtJiraUrl, txtReportPath);
        labels.setAlignment(Pos.CENTER_LEFT);
        textFields.setPrefWidth(400);
    }

    public HBox getBox() {
        return box;
    }

    public String getTxtReportPathValue() {
        return txtReportPath.getText();
    }
    public String getTxtJiraUrlValue() {
        return txtJiraUrl.getText();
    }

}
