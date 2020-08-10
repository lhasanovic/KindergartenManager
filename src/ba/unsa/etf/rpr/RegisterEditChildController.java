package ba.unsa.etf.rpr;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.util.ArrayList;

public class RegisterEditChildController {
	public TextField firstNameField;
	public TextField lastNameField;
	public TextField parentNameField;
	public DatePicker birthDatePicker;
	public TextField phoneNumberField;
	public ChoiceBox<KindergartenTeacher> teacherChoiceBox;
	public TextField specialNeedsField;

	private Child child;
	private ObservableList<KindergartenTeacher> teacherList;

	public RegisterEditChildController(Child child, ArrayList<KindergartenTeacher> teachers) {
		this.child = child;
		this.teacherList = FXCollections.observableArrayList(teachers);
	}

	@FXML
	public void initialize() {
		teacherChoiceBox.setItems(teacherList);
		birthDatePicker.setEditable(false);

		if(child != null) {
			firstNameField.setText(child.getFirstName());
			lastNameField.setText(child.getLastName());
			parentNameField.setText(child.getParent().getFirstName());
			birthDatePicker.setValue(child.getDateOfBirth());
			phoneNumberField.setText(child.getParent().getPhoneNumber());

			firstNameField.getStyleClass().add("fieldRight");
			lastNameField.getStyleClass().add("fieldRight");
			parentNameField.getStyleClass().add("fieldRight");
			phoneNumberField.getStyleClass().add("fieldRight");

			for(KindergartenTeacher t : teacherList) {
				if(t.getId() == child.getTeacher().getId()) {
					teacherChoiceBox.getSelectionModel().select(t);
					break;
				}
			}

			if(child instanceof SpecialNeedsChild)
				specialNeedsField.setText(((SpecialNeedsChild) child).getSpecialNeedDescription());
			else
				specialNeedsField.setText("");

		} else {
			teacherChoiceBox.getSelectionModel().selectFirst();
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

		parentNameField.textProperty().addListener((obs, oldVal, newVal) -> {
			if(containsOnlyLetters(newVal)) {
				parentNameField.getStyleClass().removeAll("fieldWrong");
				parentNameField.getStyleClass().add("fieldRight");
			} else {
				parentNameField.getStyleClass().removeAll("fieldRight");
				parentNameField.getStyleClass().add("fieldWrong");
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
		if(areAllTextFieldsValid() && birthDatePicker.getValue() != null) {
			try {
				new Child().setDateOfBirth(birthDatePicker.getValue());

				int oldId = -1;

				if(child != null)
					oldId = child.getId();

				if(specialNeedsField.getText().isEmpty())
					child = new Child();
				else
					child = new SpecialNeedsChild();

				if(oldId != -1)
					child.setId(oldId);

				child.setFirstName(firstNameField.getText());
				child.setLastName(lastNameField.getText());
				child.setParent(new Parent(parentNameField.getText(), child.getLastName(), phoneNumberField.getText()));
				child.setDateOfBirth(birthDatePicker.getValue());
				child.setTeacher(teacherChoiceBox.getValue());

				if(child instanceof SpecialNeedsChild)
					((SpecialNeedsChild) child).setSpecialNeedDescription(specialNeedsField.getText());

				Stage stage = (Stage) firstNameField.getScene().getWindow();
				stage.close();

			} catch (InvalidChildBirthDateException e) {
				String title = e.getMessage();
				String text = "";
				notify(title, text);
			}
		}
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
		return isTextFieldValid(firstNameField) && isTextFieldValid(lastNameField) && isTextFieldValid(parentNameField)
				&& isTextFieldValid(phoneNumberField);
	}

	private void notify(String title, String text) {
		Notifications notificationBuilder = Notifications.create()
				.title(title)
				.text(text)
				.hideAfter(Duration.seconds(2))
				.position(Pos.BOTTOM_RIGHT);
		notificationBuilder.showError();
	}
}
