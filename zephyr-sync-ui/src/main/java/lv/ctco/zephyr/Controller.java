package lv.ctco.zephyr;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Controller {

    @FXML
    private VBox vboxmain;

    @FXML
    private HBox rbnBox;

    @FXML
    private TextField txtJiraURL;

    @FXML
    private TextField reportPath;

    @FXML
    private TextField projectKey;

    @FXML
    private TextField projectCycle;

    @FXML
    private TextField projectVersion;

    @FXML
    private CheckBox orderedSteps;

    @FXML
    private CheckBox forcedStoryLink;

    @FXML
    private TextField login;

    @FXML
    private TextField password;

    private final Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill mandatory fields", ButtonType.YES);

    private Model model = new Model();

    @FXML
    private void sentRequest() {
        if (model.allMandatoryInformationIsProvided(vboxmain)) {
            try {
                model.sendRequest(model.getParameters(rbnBox, txtJiraURL, reportPath, projectKey, projectCycle, projectVersion, orderedSteps, forcedStoryLink, login, password));
            }catch (Exception e){
                new Alert(Alert.AlertType.ERROR, e.toString(), ButtonType.YES).showAndWait();
            }

        } else {
            alert.showAndWait();
        }
    }

    @FXML
    private void addToToggleGroup() {
        model.addToToggleGroup(rbnBox);
    }
}

