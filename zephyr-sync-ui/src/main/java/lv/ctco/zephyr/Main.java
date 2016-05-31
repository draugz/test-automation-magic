package lv.ctco.zephyr;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static final String CSS_CLASS = "controlStyle.css";

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        root.getStylesheets().add(CSS_CLASS);
        primaryStage.setTitle("Test Automation Magic");
        primaryStage.setScene(new Scene(root, 625, 370));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
