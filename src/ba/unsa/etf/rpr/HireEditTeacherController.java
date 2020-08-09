package ba.unsa.etf.rpr;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class HireEditTeacherController {
	public TextField firstNameField;
	public TextField lastNameField;
	public TextField phoneNumberField;
	public RadioButton specialNeedsYes;
	public RadioButton specialNeedsNo;
	public ToggleGroup specialNeeds;

	private KindergartenTeacher teacher;

	public HireEditTeacherController(KindergartenTeacher teacher) {
		this.teacher = teacher;
	}

	@FXML
	public void initialize() {
		specialNeeds = new ToggleGroup();
		specialNeeds.getToggles().add(specialNeedsYes);
		specialNeeds.getToggles().add(specialNeedsNo);
	}

	public KindergartenTeacher getTeacher() {
		return teacher;
	}
}
