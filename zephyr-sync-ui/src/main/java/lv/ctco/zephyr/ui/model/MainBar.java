package lv.ctco.zephyr.ui.model;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lv.ctco.zephyr.ui.ZephyrMagicUIDemo;
import lv.ctco.zephyr.ui.enums.CssStyle;

import java.util.Set;

/**
 * Created by S7T4M5 on 2016.05.23..
 */
public class MainBar {

    private VBox box;
    private static final String CSS_CLASS = "controlStyle.css";

    private final ReportTypeBar reportTypeBar = new ReportTypeBar();
    private final JiraURLBar jiraURLBar = new JiraURLBar();
    private final ReportPathBar reportPathBar = new ReportPathBar();
    private final JiraMainBar jiraMainBar = new JiraMainBar();
    private final CheckBoxBar checkBoxBar = new CheckBoxBar();
    private final LoginBar loginBar = new LoginBar();
    private final PasswordBar passwordBar = new PasswordBar();

    private final Button btnUpdateJira = new Button("Update Jira");
    private final Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill mandatory fields", ButtonType.YES);

    public MainBar(Stage stage, String title) {
        stage.setTitle(title);
        GridPane root = new GridPane();
        Scene scene = new Scene(root, 600, 350, Color.LIGHTGRAY);
        stage.setScene(scene);
        scene.getStylesheets().add(CSS_CLASS);

        box = new VBox(5);
        box.setPadding(new Insets(5, 10, 50, 10));
        root.getChildren().add(box);
    }

    public VBox getBox() {
        return box;
    }

    public void addReportTypeBar() {
        getBox().getChildren().add(reportTypeBar.getBox());
    }

    public void addJiraURLBar() {
        getBox().getChildren().add(jiraURLBar.getBox());
    }

    public void addReportPathBar() {
        getBox().getChildren().add(reportPathBar.getBox());
    }

    public void addJiraMainBar() {
        getBox().getChildren().add(jiraMainBar.getBox());
    }

    public void addCheckBoxBar() {
        getBox().getChildren().add(checkBoxBar.getBox());
    }

    public void addLoginBar() {
        getBox().getChildren().add(loginBar.getBox());
    }

    public void addPasswordBar() {
        getBox().getChildren().add(passwordBar.getBox());
    }

    public void addButton() {
        getBox().getChildren().add(btnUpdateJira);
        setButtonRule();
    }

    public void setButtonRule() {
        btnUpdateJira.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (!allMandatoryInformationIsProvided()) {
                    showError();
                } else {
                    try {
                        ZephyrMagicUIDemo.updateJira(getParameters());
                    } catch (Exception ex) {
                        new Alert(Alert.AlertType.ERROR, ex.toString(), ButtonType.YES);
                    }
                }
            }
        });
    }

    public void addListenerForAllTextFields() {
        Set<Node> nodes = getBox().lookupAll(".text-field");
        for (Node node : nodes) {
            addListenerForField((TextField) node);
        }
    }

    public void addListenerForField(final TextField field) {
        field.focusedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                field.getStyleClass().clear();
                field.getStyleClass().addAll("text-field", "text-input");
            }
        });
    }


    public boolean allMandatoryInformationIsProvided() {
        boolean isProvided = true;
        Set<Node> nodes = getBox().lookupAll(".text-field");
        for (Node node : nodes) {
            if (((TextField) node).getText().isEmpty()) {
                node.getStyleClass().add(CssStyle.ERROR_STYLE.getName());
                isProvided = false;
            }
        }
        return isProvided;
    }

    public void showError() {
        alert.showAndWait();
    }

    public String[] getParameters(){
        String parameters[]= {loginBar.getTxtLoginValue(),
                passwordBar.getPswPasswordValue(),
                reportTypeBar.getReportType(),
                jiraMainBar.getProjectKeyValue(),
                jiraMainBar.getProjectVersionValue(),
                jiraMainBar.getTestCycleValue(),
                jiraURLBar.getTxtJiraUrlValue(),
                reportPathBar.getTxtReportPathValue(),
                checkBoxBar.getOrderedStepsValue(),
                checkBoxBar.getForcedStoryLinkValue()};
        return parameters;

    }

}
