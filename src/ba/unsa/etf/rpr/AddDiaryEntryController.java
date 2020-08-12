package ba.unsa.etf.rpr;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

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
		childBox.getSelectionModel().select(child);
		diaryDatePicker.setValue(LocalDate.now());
		hourField.setText("" + LocalDateTime.now().getHour());
		minuteField.setText("" + LocalDateTime.now().getMinute());
		childActivityBox.getItems().setAll(ChildActivity.values());
		childActivityBox.getSelectionModel().select(child.getActivity());

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
		if(allGood()) {
			if(diaryDatePicker.getValue().isAfter(LocalDate.now())) {
				String title = "Invalid date!";
				String text = "Date can't be in the future";
				notify(title, text);
			} else if(diaryDatePicker.getValue().isBefore(child.getDateOfBirth())) {
				String title = "Invalid date!";
				String text = "Date can't be older than the child!";
				notify(title, text);
			} else {

			}
		}
	}

	private boolean matchesHourFormat(String s) {
		boolean matches =  s != null &&
				s.length() != 0 &&
				s.matches("^[0-9]{1,2}?$");

		if(s.charAt(0) == '0')
			s = s.substring(1);

		int hours = Integer.parseInt(s);

		return matches && hours >= 0 && hours <= 23;
	}

	private boolean matchesMinuteFormat(String s) {
		boolean matches =  s != null &&
				s.length() != 0 &&
				s.matches("^[0-9]{1,2}?$");

		if(s.charAt(0) == '0')
			s = s.substring(1);

		int minutes = Integer.parseInt(s);

		return matches && minutes >= 0 && minutes <= 59;
	}

	private boolean allGood() {
		return hourField.getStyleClass().contains("fieldRight") &&
				minuteField.getStyleClass().contains("fieldRight");
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
