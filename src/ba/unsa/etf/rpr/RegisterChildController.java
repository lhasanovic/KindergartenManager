package ba.unsa.etf.rpr;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

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

		} else {
			teacherChoiceBox.setValue(teacherChoiceBox.getItems().get(0));
		}
	}

	public void actionOk(ActionEvent actionEvent) {

	}

	public void actionCancel(ActionEvent actionEvent) {

	}

	public Child getChild() {
		return child;
	}
}
