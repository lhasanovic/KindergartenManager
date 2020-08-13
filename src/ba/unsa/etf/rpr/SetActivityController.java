package ba.unsa.etf.rpr;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class SetActivityController {
	public Label childLabel;
	public ChoiceBox<ChildActivity> chooseActivityBox;

	private Child child;

	public SetActivityController(Child child) {
		this.child = child;
	}

	@FXML
	public void initialize() {
		chooseActivityBox.getItems().setAll(ChildActivity.values());
		chooseActivityBox.getSelectionModel().selectFirst();
		childLabel.setText(child.getFirstName() + " new activity:");
	}

	public void actionOk(ActionEvent actionEvent) {
		child.setActivity(chooseActivityBox.getValue());

		Stage stage = (Stage) childLabel.getScene().getWindow();
		stage.close();
	}

	public void actionCancel(ActionEvent actionEvent) {
		child = null;

		Stage stage = (Stage) childLabel.getScene().getWindow();
		stage.close();
	}


	public Child getChild() {
		return child;
	}
}
