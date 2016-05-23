package lv.ctco.zephyr.ui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lv.ctco.zephyr.ui.model.*;

public class ZephyrMagicUIDemo extends Application {

    private static final String TITLE = "Test Automation Magic";

    final ReportTypeBar reportTypeBar = new ReportTypeBar();
    final JiraURLBar jiraURLBar = new JiraURLBar();
    final ReportPathBar reportPathBar = new ReportPathBar();
    final JiraMainBar jiraMainBar = new JiraMainBar();
    final CheckBoxBar checkBoxBar = new CheckBoxBar();
    final LoginBar loginBar = new LoginBar();
    final PasswordBar passwordBar = new PasswordBar();
    final Button btnUpdateJira = new Button("Update Jira");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        MainBar mainBar = new MainBar(stage, TITLE);

        mainBar.getBox().getChildren().add(reportTypeBar.getBox());
        mainBar.getBox().getChildren().add(jiraURLBar.getBox());
        mainBar.getBox().getChildren().add(reportPathBar.getBox());
        mainBar.getBox().getChildren().add(jiraMainBar.getBox());
        mainBar.getBox().getChildren().add(checkBoxBar.getBox());
        mainBar.getBox().getChildren().add(loginBar.getBox());
        mainBar.getBox().getChildren().add(passwordBar.getBox());
        mainBar.getBox().getChildren().add(btnUpdateJira);

         btnUpdateJira.setOnAction(new EventHandler<ActionEvent>() {
             public void handle(ActionEvent e) {
                 verifyMandatoryFields();

             }
         });

        stage.show();
    }

    public void verifyMandatoryFields() {
        boolean errorShouldBeDisplayed = false;
        if (jiraURLBar.getTxtJiraUrl().getText().isEmpty()) {
            errorShouldBeDisplayed = true;
            jiraURLBar.setErrorStyle(jiraURLBar.getTxtJiraUrl());
        }
        if (reportPathBar.getTxtReportPath().getText().isEmpty()) {
            errorShouldBeDisplayed = true;
            reportPathBar.setErrorStyle(reportPathBar.getTxtReportPath());
        }

        if (loginBar.getTxtLogin().getText().isEmpty()) {
            errorShouldBeDisplayed = true;
            loginBar.setErrorStyle(loginBar.getTxtLogin());
        }
        if (passwordBar.getPswPassword().getText().isEmpty()) {
            errorShouldBeDisplayed = true;
            passwordBar.setErrorStyle(passwordBar.getPswPassword());
        }
        if (jiraMainBar.getTxtProjectKey().getText().isEmpty()) {
            errorShouldBeDisplayed = true;
            jiraMainBar.setErrorStyle(jiraMainBar.getTxtProjectKey());
        }
        if (jiraMainBar.getTxtProjectVersion().getText().isEmpty()) {
            errorShouldBeDisplayed = true;
            jiraMainBar.setErrorStyle(jiraMainBar.getTxtProjectVersion());
        }
        if (jiraMainBar.getTxtTestCycle().getText().isEmpty()) {
            errorShouldBeDisplayed = true;
            jiraMainBar.setErrorStyle(jiraMainBar.getTxtTestCycle());
        }
        if (errorShouldBeDisplayed == true) {
            pleaseFillMandatoryFieldsAllert();
        }
    }

    public void pleaseFillMandatoryFieldsAllert() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill mandatory fields", ButtonType.YES);
        alert.showAndWait();
    }
}
