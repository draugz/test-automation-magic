package lv.ctco.zephyr.ui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ZephyrMagicUIDemo extends Application {

    private static final String ERROR_STYLE = "error-massage";
    private static final String TITLE = "Test Automation Magic";
    private static final String CSS_CLASS="controlStyle.css";

    public static void main(String[] args) {
        launch(args);
    }

    private Label lblSelectReportType = new Label("Please select report type:");
    private RadioButton rbtAllureReport = new RadioButton("Allure");
    private RadioButton rbtJunitReport = new RadioButton("JUnit");
    private ToggleGroup toggleGroup = new ToggleGroup();

    private Label lblProvideJiraUrl = new Label("Please provide JIRA URL:");
    private TextField txtJiraUrl = new TextField("http://swissre.com");

    private Label lblProvideReportPath = new Label("Please provide path for report directory:");
    private TextField txtReportPath = new TextField();

    private Label lblLogin = new Label("Login:");
    private TextField txtLogin = new TextField();

    private Label lblPassword = new Label("Password:");
    private PasswordField pswPassword = new PasswordField();

    private Button btnUpdateJira = new Button("Update Jira");

    @Override
    public void start(Stage stage) {
        stage.setTitle(TITLE);
        GridPane root = new GridPane();
        Scene scene = new Scene(root, 600, 350, Color.LIGHTGRAY);
        stage.setScene(scene);
        scene.getStylesheets().add(CSS_CLASS);

        VBox mainInformationBar = new VBox(5);
        mainInformationBar.setPadding(new Insets(5, 10, 50, 10));
        root.getChildren().add(mainInformationBar);

        HBox reportTypeBar = new HBox(10);
        reportTypeBar.setPadding(new Insets(0, 0, 0, 0));
        mainInformationBar.getChildren().add(reportTypeBar);

        HBox jiraUrlBar = new HBox(10);
        jiraUrlBar.setPadding(new Insets(10, 0, 0, 0));
        mainInformationBar.getChildren().add(jiraUrlBar);

        HBox reportPathBar = new HBox(10);
        reportPathBar.setPadding(new Insets(0, 0, 0, 0));
        mainInformationBar.getChildren().add(reportPathBar);

        HBox loginBar = new HBox(10);
        loginBar.setPadding(new Insets(0, 0, 0, 0));
        mainInformationBar.getChildren().add(loginBar);

        final HBox passwordBar = new HBox(10);
        passwordBar.setPadding(new Insets(0, 0, 0, 0));
        mainInformationBar.getChildren().add(passwordBar);

        reportTypeBar.getChildren().add(lblSelectReportType);
        rbtAllureReport.setToggleGroup(toggleGroup);
        rbtJunitReport.setToggleGroup(toggleGroup);

        reportTypeBar.getChildren().add(rbtAllureReport);
        reportTypeBar.getChildren().add(rbtJunitReport);
        rbtAllureReport.setSelected(true);

        jiraUrlBar.getChildren().add(lblProvideJiraUrl);
        txtJiraUrl.setPrefWidth(300);
        jiraUrlBar.getChildren().add(txtJiraUrl);

        reportPathBar.getChildren().add(lblProvideReportPath);
        txtReportPath.setPrefWidth(218);
        reportPathBar.getChildren().add(txtReportPath);

        loginBar.getChildren().add(lblLogin);
        loginBar.getChildren().add(txtLogin);

        passwordBar.getChildren().add(lblPassword);
        passwordBar.getChildren().add(pswPassword);

        btnUpdateJira.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                verifyMandatoryFields();

//                String[] parametrs = new String[5];
//                parametrs[0] = "546";
//                try {
//                    lv.ctco.zephyr.Runner.main(parametrs);
//                } catch (Exception e1) {
//                    e1.printStackTrace();
//                }

            }
        });
        mainInformationBar.getChildren().add(btnUpdateJira);
        stage.show();

    }

    public void verifyMandatoryFields() {
        boolean errorShouldBeDisplayed = false;
        if (txtJiraUrl.getText().isEmpty()) {
            errorShouldBeDisplayed = true;
            txtJiraUrl.getStyleClass().add(ERROR_STYLE);
        }
        if (txtReportPath.getText().isEmpty()) {
            errorShouldBeDisplayed = true;
            txtReportPath.getStyleClass().add(ERROR_STYLE);
        }
        if (txtLogin.getText().isEmpty()) {
            errorShouldBeDisplayed = true;
            txtLogin.getStyleClass().add(ERROR_STYLE);
        }
        if (pswPassword.getText().isEmpty()) {
            errorShouldBeDisplayed = true;
            pswPassword.getStyleClass().add(ERROR_STYLE);
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
