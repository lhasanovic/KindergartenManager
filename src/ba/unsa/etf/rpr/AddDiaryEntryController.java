package ba.unsa.etf.rpr;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class AddDiaryEntryController {
	public ChoiceBox<Child> childBox;
	public DatePicker diaryDatePicker;
	public TextField hourField, minuteField;
	public ChoiceBox<ChildActivity> childActivityBox;
	public TextArea descriptionArea;

	private Child child;
	private ObservableList<Child> children;

	public AddDiaryEntryController(Child child, ObservableList<Child> children) {
		this.child = child;
		this.children = children;
	}

	@FXML
	public void initialize() {
		childBox.setItems(children);
		diaryDatePicker.setValue(LocalDate.now());
		hourField.setText("" + LocalDateTime.now().getHour());
		minuteField.setText("" + LocalDateTime.now().getMinute());
		childActivityBox.getItems().setAll(ChildActivity.values());

		hourField.textProperty().addListener((obs, oldVal, newVal) -> {
			if(matchesHourFormat(newVal)) {
				hourField.getStyleClass().removeAll("fieldWrong");
				hourField.getStyleClass().add("fieldRight");
			} else {
				hourField.getStyleClass().removeAll("fieldRight");
				hourField.getStyleClass().add("fieldWrong");
			}
		});

		minuteField.textProperty().addListener((obs, oldVal, newVal) -> {
			if(matchesMinuteFormat(newVal)) {
				hourField.getStyleClass().removeAll("fieldWrong");
				hourField.getStyleClass().add("fieldRight");
			} else {
				hourField.getStyleClass().removeAll("fieldRight");
				hourField.getStyleClass().add("fieldWrong");
			}
		});
	}

	public void actionOk(ActionEvent actionEvent) {

	}

	public boolean matchesHourFormat(String s) {
		boolean matches =  s != null &&
				s.length() != 0 &&
				s.matches("^[0-9]{1,2}?$");

		if(s.charAt(0) == '0')
			s = s.substring(1);

		int hours = Integer.parseInt(s);

		return matches && hours >= 0 && hours <= 23;
	}

	public boolean matchesMinuteFormat(String s) {
		boolean matches =  s != null &&
				s.length() != 0 &&
				s.matches("^[0-9]{1,2}?$");

		if(s.charAt(0) == '0')
			s = s.substring(1);

		int minutes = Integer.parseInt(s);

		return matches && minutes >= 0 && minutes <= 59;

	}
}
