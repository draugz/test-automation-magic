package lv.ctco.zephyr.ui.model;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Created by S7T4M5 on 2016.05.23..
 */
public class MainBar {

    private VBox box;
    private static final String CSS_CLASS="controlStyle.css";
    protected static final String ERROR_STYLE = "error-massage";

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

    public VBox getBox(){
        return box;
    }
}
