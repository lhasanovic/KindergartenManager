package ba.unsa.etf.rpr;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
	public ChoiceBox teacherChoiceBox;
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
	}
}
