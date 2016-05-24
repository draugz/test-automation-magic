package lv.ctco.zephyr.ui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lv.ctco.zephyr.ui.model.*;

public class ZephyrMagicUIDemo extends Application {

    private static final String TITLE = "Test Automation Magic";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        MainBar mainBar = new MainBar(stage, TITLE);
        mainBar.addReportTypeBar();
        mainBar.addJiraURLBar();
        mainBar.addReportPathBar();
        mainBar.addJiraMainBar();
        mainBar.addCheckBoxBar();
        mainBar.addLoginBar();
        mainBar.addPasswordBar();
        mainBar.addButton();
        stage.show();
    }
}
