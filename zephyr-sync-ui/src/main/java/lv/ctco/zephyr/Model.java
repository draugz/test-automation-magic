package lv.ctco.zephyr;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lv.ctco.zephyr.enums.CssStyle;

import java.util.Set;

import static java.lang.String.valueOf;

public class Model {

    private ToggleGroup toggleGroup = new ToggleGroup();

    public boolean allMandatoryInformationIsProvided(VBox vboxmain) {
        addListenerForAllTextFields(vboxmain);
        boolean isProvided = true;
        Set<Node> nodes = vboxmain.lookupAll(".text-field");
        for (Node node : nodes) {
            if (((TextField) node).getText().isEmpty()) {
                node.getStyleClass().add(CssStyle.ERROR_STYLE.getName());
                isProvided = false;
            }
        }
        return isProvided;
    }

    private void addListenerForAllTextFields(VBox vboxmain) {
        Set<Node> nodes = vboxmain.lookupAll(".text-field");
        nodes.forEach(node -> addListenerForField((TextField) node));
    }

    private void addListenerForField(final TextField field) {
        field.focusedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                field.getStyleClass().clear();
                field.getStyleClass().addAll("text-field", "text-input");
            }
        });
    }

    public void addToToggleGroup(HBox box) {
        Set<Node> nodes = box.lookupAll(".radio-button");
        nodes.forEach(node -> ((RadioButton) node).setToggleGroup(toggleGroup));
    }

    public void sendRequest(String [] parameters) throws Exception{
        Runner.main(parameters);
    }

    public String[] getParameters(HBox rbnBox, TextField txtJiraURL, TextField reportPath, TextField projectKey, TextField projectCycle, TextField projectVersion, CheckBox orderedSteps, CheckBox forcedStoryLink, TextField login, TextField password) {
        String parameters[]= {login.getText(),
                password.getText(),
                getValueFromRbnBox(rbnBox),
                projectKey.getText(),
                projectVersion.getText(),
                projectCycle.getText(),
                txtJiraURL.getText(),
                reportPath.getText(),
                valueOf(orderedSteps.isSelected()),
                valueOf(forcedStoryLink.isSelected())};
        return parameters;
    }

    private String getValueFromRbnBox(HBox box){
        Set<Node> nodes = box.lookupAll(".radio-button");
        for (Node node:nodes){
            if ( ((RadioButton) node).isSelected() ){
                return ((RadioButton) node).getText().toLowerCase();
            }
        }
        return null;
    }
}