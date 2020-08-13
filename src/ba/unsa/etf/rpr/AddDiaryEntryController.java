package ba.unsa.etf.rpr;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class AddDiaryEntryController {
	public ChoiceBox<Child> childBox;
	public DatePicker diaryDatePicker;
	public TextField hourField, minuteField;
	public ChoiceBox<ChildActivity> childActivityBox;
	public TextArea descriptionArea;

	private Child child;
	private ObservableList<Child> children;
	private DiaryEntry diaryEntry;

	public AddDiaryEntryController(Child child, ObservableList<Child> children, DiaryEntry diaryEntry) {
		if(child == null)
			this.child = children.get(0);
		else
			this.child = child;

		this.children = children;
		this.diaryEntry = diaryEntry;
	}

	@FXML
	public void initialize() {
		if(children != null)
			childBox.setItems(children);
		else
			childBox.setItems(FXCollections.observableArrayList(Collections.singletonList(child)));

		childBox.getSelectionModel().select(child);
		childActivityBox.getItems().setAll(ChildActivity.values());

		if(diaryEntry != null) {
			diaryDatePicker.setValue(diaryEntry.getTimeDate().toLocalDate());
			hourField.setText(String.valueOf(diaryEntry.getTimeDate().getHour()));
			minuteField.setText(String.valueOf(diaryEntry.getTimeDate().getMinute()));
			childActivityBox.getSelectionModel().select(diaryEntry.getActivity());
			descriptionArea.setText(diaryEntry.getDescription());
		} else {
			diaryDatePicker.setValue(LocalDate.now());
			hourField.setText("" + LocalDateTime.now().getHour());
			minuteField.setText("" + LocalDateTime.now().getMinute());
			childActivityBox.getSelectionModel().select(child.getActivity());
		}

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

		if(matchesHourFormat(hourField.getText()))
			hourField.getStyleClass().add("fieldRight");
		else
			hourField.getStyleClass().add("fieldWrong");

		if(matchesMinuteFormat(minuteField.getText()))
			minuteField.getStyleClass().add("fieldRight");
		else
			minuteField.getStyleClass().add("fieldWrong");
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
				LocalDateTime localDateTime = LocalDateTime.of(diaryDatePicker.getValue(), LocalTime.of(parseHourOrMinute(hourField.getText()), parseHourOrMinute(minuteField.getText())));
				diaryEntry = new DiaryEntry(localDateTime, childActivityBox.getSelectionModel().getSelectedItem(), descriptionArea.getText());

				Stage stage = (Stage) childBox.getScene().getWindow();
				stage.close();
			}
		}
	}

	public void actionCancel(ActionEvent actionEvent) {
		diaryEntry = null;

		Stage stage = (Stage) childBox.getScene().getWindow();
		stage.close();
	}

	private boolean matchesHourFormat(String s) {
		boolean matches =  s != null &&
				s.length() != 0 &&
				s.matches("^[0-9]{1,2}?$");

		if (!matches)
			return false;

		int hours = parseHourOrMinute(s);

		return matches && hours >= 0 && hours <= 23;
	}

	private boolean matchesMinuteFormat(String s) {
		boolean matches =  s != null &&
				s.length() != 0 &&
				s.matches("^[0-9]{1,2}?$");

		if (!matches)
			return false;

		int minutes = parseHourOrMinute(s);

		return matches && minutes >= 0 && minutes <= 59;
	}

	private int parseHourOrMinute(String s) {
		if(s.charAt(0) == '0')
			s = s.substring(1);

		return Integer.parseInt(s);
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

	public DiaryEntry getDiaryEntry() {
		return diaryEntry;
	}
}
