package lv.ctco.zephyr.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import lv.ctco.zephyr.Runner;
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
        mainBar.addPathBar();
        mainBar.addJiraMainBar();
        mainBar.addCheckBoxBar();
        mainBar.addLoginBar();
        mainBar.addButton();
        mainBar.addListenerForAllTextFields();
        stage.show();
    }

    public static void updateJira(String[] parameters) throws Exception{
        Runner.main(parameters);
    }
}
