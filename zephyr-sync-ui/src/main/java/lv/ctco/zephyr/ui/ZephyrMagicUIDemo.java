package lv.ctco.zephyr.ui;

import com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElementRef;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ZephyrMagicUIDemo extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private Label lblSelectReportType=new Label("Please select report type:");
    private RadioButton rbtAllureReport=new RadioButton("Allure");
    private RadioButton rbtJunitReport=new RadioButton("JUnit");

    private Label provideJiraUrl=new Label("Please provide JIRA URL:");
    private TextField txtJiraUrl=new TextField("http://swissre.com");

    private Label provideReportPath=new Label("Please provide path for report directory:");
    private TextField txtReportPath=new TextField();

    private Label lblLogin=new Label("Login:");
    private TextField txtLogin=new TextField();

    private Label lblPassword=new Label("Password:");
    private PasswordField pswPassword=new PasswordField();

    private Button btnUpdateJira=new Button("Update Jira");

    @Override
    public void start(Stage stage) {
        stage.setTitle("Test Automation Magic");
        GridPane root = new GridPane();
        Scene scene = new Scene(root, 600, 350, Color.LIGHTGRAY);
        stage.setScene(scene);

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
        reportTypeBar.getChildren().add(rbtAllureReport);
        reportTypeBar.getChildren().add(rbtJunitReport);

        jiraUrlBar.getChildren().add(provideJiraUrl);
        txtJiraUrl.setPrefWidth(300);
        jiraUrlBar.getChildren().add(txtJiraUrl);

        reportPathBar.getChildren().add(provideReportPath);
        txtReportPath.setPrefWidth(218);
        reportPathBar.getChildren().add(txtReportPath);

        loginBar.getChildren().add(lblLogin);
        loginBar.getChildren().add(txtLogin);

        passwordBar.getChildren().add(lblPassword);
        passwordBar.getChildren().add(pswPassword);

        btnUpdateJira.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {

            }
        });
        mainInformationBar.getChildren().add(btnUpdateJira);

        stage.show();

    }

    public void verifyMandatoryFields(){
        if (!rbtAllureReport.isSelected()&&!rbtJunitReport.isSelected()){
            System.out.print("Please provide mandatory information");
        }
    }
}
