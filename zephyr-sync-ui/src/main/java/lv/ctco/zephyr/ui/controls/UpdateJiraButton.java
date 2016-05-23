package lv.ctco.zephyr.ui.controls;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

/**
 * Created by S7T4M5 on 2016.05.23..
 */
public class UpdateJiraButton {

    final Button btnUpdateJira = new Button("Update Jira");

    public UpdateJiraButton(){
         btnUpdateJira.setOnAction(new EventHandler<ActionEvent>() {
             public void handle(ActionEvent e) {

             }
         });
    }
}
