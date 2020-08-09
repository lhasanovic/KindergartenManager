package ba.unsa.etf.rpr;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

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

		if(teacher != null) {
			firstNameField.setText(teacher.getFirstName());
			lastNameField.setText(teacher.getLastName());
			phoneNumberField.setText(teacher.getPhoneNumber());

			if(teacher instanceof SpecialNeedsKindergartenTeacher)
				specialNeeds.selectToggle(specialNeedsYes);
			else
				specialNeeds.selectToggle(specialNeedsNo);
		} else {
			specialNeeds.selectToggle(specialNeedsNo);
		}

		firstNameField.textProperty().addListener((obs, oldVal, newVal) -> {
			if(containsOnlyLetters(newVal)) {
				firstNameField.getStyleClass().removeAll("fieldWrong");
				firstNameField.getStyleClass().add("fieldRight");
			} else {
				firstNameField.getStyleClass().removeAll("fieldRight");
				firstNameField.getStyleClass().add("fieldWrong");
			}
		});

		lastNameField.textProperty().addListener((obs, oldVal, newVal) -> {
			if(containsOnlyLetters(newVal)) {
				lastNameField.getStyleClass().removeAll("fieldWrong");
				lastNameField.getStyleClass().add("fieldRight");
			} else {
				lastNameField.getStyleClass().removeAll("fieldRight");
				lastNameField.getStyleClass().add("fieldWrong");
			}
		});

		phoneNumberField.textProperty().addListener((obs, oldVal, newVal) -> {
			if(isPhoneNumberValid(newVal)) {
				phoneNumberField.getStyleClass().removeAll("fieldWrong");
				phoneNumberField.getStyleClass().add("fieldRight");
			} else {
				phoneNumberField.getStyleClass().removeAll("fieldRight");
				phoneNumberField.getStyleClass().add("fieldWrong");
			}
		});
	}

	public void actionOk(ActionEvent actionEvent) {
		if(areAllTextFieldsValid()) {
			int oldId = -1;

			if(teacher != null)
				oldId = teacher.getId();

			if(specialNeeds.getSelectedToggle().toString().equals("No"))
				teacher = new KindergartenTeacher();
			else
				teacher = new SpecialNeedsKindergartenTeacher();

			if(oldId != -1)
				teacher.setId(oldId);

			teacher.setFirstName(firstNameField.getText());
			teacher.setLastName(lastNameField.getText());
			teacher.setPhoneNumber(phoneNumberField.getText());

			Stage stage = (Stage) firstNameField.getScene().getWindow();
			stage.close();
		}
	}

	public void actionCancel(ActionEvent actionEvent) {
		teacher = null;
		Stage stage = (Stage) firstNameField.getScene().getWindow();
		stage.close();
	}

	private boolean containsOnlyLetters(String s) {
		if(s == null) return false;

		String bosnianSpecificLetters = "čćžšđČĆŽŠĐ";
		if(s.length() < 3) return false;
		for(int i = 0; i < s.length(); i++) {
			if(!((s.charAt(i) >= 'A' && s.charAt(i) <= 'Z') || (s.charAt(i) >= 'a' && s.charAt(i) <= 'z')
					|| (s.charAt(i) == '-') || (s.charAt(i) == ' ') || bosnianSpecificLetters.contains(s.charAt(i) + ""))) return false;
		}
		return true;
	}

	private boolean isPhoneNumberValid(String s) {
		return ((s != null)
				&& (s.length() >= 7)
				&& (s.length() <= 12)
				&& (s.matches("^[0-9\\\\-]*$")));
	}

	private boolean isTextFieldValid(TextField t) {
		for (String s : t.getStyleClass()) {
			if(s.equals("fieldRight"))
				return true;
		}
		return false;
	}

	private boolean areAllTextFieldsValid() {
		return isTextFieldValid(firstNameField) && isTextFieldValid(lastNameField) && isTextFieldValid(phoneNumberField);
	}

	public KindergartenTeacher getTeacher() {
		return teacher;
	}
}
