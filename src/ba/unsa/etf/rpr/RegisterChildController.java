package ba.unsa.etf.rpr;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;

public class RegisterChildController {
	public TextField firstNameField;
	public TextField lastNameField;
	public TextField parentNameField;
	public DatePicker birthDatePicker;
	public TextField phoneNumberField;
	public ChoiceBox<KindergartenTeacher> teacherChoiceBox;
	public TextField specialNeedsField;

	private Child child;
	private ObservableList<KindergartenTeacher> teacherList;

	public RegisterChildController(Child child, ArrayList<KindergartenTeacher> teachers) {
		this.child = child;
		this.teacherList = FXCollections.observableArrayList(teachers);
	}

	@FXML
	public void initialize() {
		teacherChoiceBox.setItems(teacherList);

		if(child != null) {
			firstNameField.setText(child.getFirstName());
			lastNameField.setText(child.getLastName());
			parentNameField.setText(child.getParent().getFirstName());
			birthDatePicker.setValue(child.getDateOfBirth());
			phoneNumberField.setText(child.getParent().getPhoneNumber());

			for(KindergartenTeacher t : teacherList) {
				if(t.getId() == child.getTeacher().getId()) {
					teacherChoiceBox.setValue(t);
					break;
				}
			}

			if(child instanceof SpecialNeedsChild)
				specialNeedsField.setText(((SpecialNeedsChild) child).getSpecialNeedDescription());
			else
				specialNeedsField.setText("");

			firstNameField.focusedProperty().addListener((obs, oldVal, newVal) -> {
				if(!newVal) {
					if(containsOnlyLetters(firstNameField.getText())) {
						firstNameField.getStyleClass().removeAll();
						firstNameField.getStyleClass().add("fieldRight");
					} else {
						firstNameField.getStyleClass().removeAll();
						firstNameField.getStyleClass().add("fieldWrong");
					}
				}
			});

			lastNameField.focusedProperty().addListener((obs, oldVal, newVal) -> {
				if(!newVal) {
					if(containsOnlyLetters(lastNameField.getText())) {
						lastNameField.getStyleClass().removeAll();
						lastNameField.getStyleClass().add("fieldRight");
					} else {
						lastNameField.getStyleClass().removeAll();
						lastNameField.getStyleClass().add("fieldWrong");
					}
				}
			});

			parentNameField.focusedProperty().addListener((obs, oldVal, newVal) -> {
				if(!newVal) {
					if(containsOnlyLetters(parentNameField.getText())) {
						parentNameField.getStyleClass().removeAll();
						parentNameField.getStyleClass().add("fieldRight");
					} else {
						parentNameField.getStyleClass().removeAll();
						parentNameField.getStyleClass().add("fieldWrong");
					}
				}
			});

			phoneNumberField.focusedProperty().addListener((obs, oldVal, newVal) -> {
				if(!newVal) {
					if(isPhoneNumberValid(phoneNumberField.getText())) {
						phoneNumberField.getStyleClass().removeAll();
						phoneNumberField.getStyleClass().add("fieldRight");
					} else {
						phoneNumberField.getStyleClass().removeAll();
						phoneNumberField.getStyleClass().add("fieldWrong");
					}
				}
			});


		} else {
			teacherChoiceBox.setValue(teacherChoiceBox.getItems().get(0));
		}
	}

	public void actionOk(ActionEvent actionEvent) {

	}

	public void actionCancel(ActionEvent actionEvent) {
		child = null;
		Stage stage = (Stage) firstNameField.getScene().getWindow();
		stage.close();
	}

	public Child getChild() {
		return child;
	}

	private boolean containsOnlyLetters(String s) {
		return ((s != null)
				&& (!s.equals(""))
				&& (s.matches("^[a-zA-Z]*$")));
	}

	private boolean isPhoneNumberValid(String s) {
		return ((s != null)
				&& (s.length() >= 7)
				&& (s.length() <= 12)
				&& (s.matches("^[0-9\\\\-]*$")));
	}
}
