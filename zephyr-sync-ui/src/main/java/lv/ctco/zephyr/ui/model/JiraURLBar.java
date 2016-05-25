package lv.ctco.zephyr.ui.model;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import lv.ctco.zephyr.ui.enums.CssStyle;

/**
 * Created by S7T4M5 on 2016.05.23..
 */
public class JiraURLBar{

    private HBox box = new HBox(10);
    public Label lblProvideJiraUrl = new Label("Please provide JIRA URL:");
    public TextField txtJiraUrl = new TextField("http://jira.swissre.com/rest/");

    public JiraURLBar() {
        this.box.setPadding(new Insets(10, 0, 0, 0));
        box.getChildren().add(lblProvideJiraUrl);
        txtJiraUrl.setPrefWidth(300);
        box.getChildren().add(txtJiraUrl);
    }

    public HBox getBox() {
        return box;
    }

    public String getTxtJiraUrlValue() {
        return txtJiraUrl.getText();
    }


}
