package ba.unsa.etf.rpr;

import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class ViewDiaryController {
	public TableView<DiaryEntry> tableViewDiary;
	public TableColumn<DiaryEntry, String> colDate;
	public TableColumn<DiaryEntry, String> colTime;
	public TableColumn colChildActivity;
	public TableColumn colDescription;
	public ChoiceBox<Timeframe> timeframeBox;

	private ChildDiary childDiary;

	public ViewDiaryController(ChildDiary childDiary) {
		this.childDiary = childDiary;
	}

	@FXML
	public void initialize() {
		tableViewDiary.setItems(FXCollections.observableArrayList(childDiary.getDiary()));
		colDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTimeDate().getDayOfMonth() + "." +
					data.getValue().getTimeDate().getMonthValue() + "." +
					data.getValue().getTimeDate().getYear())
		);
		colTime.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTimeDate().getHour() + ":" +
				data.getValue().getTimeDate().getMinute()));
		colChildActivity.setCellValueFactory(new PropertyValueFactory("activity"));
		colDescription.setCellValueFactory(new PropertyValueFactory("description"));

		timeframeBox.getItems().setAll(Timeframe.values());
		timeframeBox.getSelectionModel().select(Timeframe.All_time);
		timeframeBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			filterList(newVal);
		});
	}

	public void actionAddDiaryEntry(ActionEvent actionEvent) {
		Child child = childDiary.getChild();
		if(child == null)
			return;

		Stage stage = new Stage();
		Parent root = null;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/add_diary_entry.fxml"));
			AddDiaryEntryController addDiaryEntryController = new AddDiaryEntryController(child, null, null);
			loader.setController(addDiaryEntryController);
			root = loader.load();
			stage.setTitle("Add Diary Entry");
			stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
			stage.setResizable(false);
			stage.show();

			stage.setOnHiding( event -> {
				DiaryEntry diaryEntry = addDiaryEntryController.getDiaryEntry();
				if (diaryEntry != null) {
					child.setActivity(diaryEntry.getActivity());
					childDiary.addDiaryEntry(diaryEntry);
					tableViewDiary.setItems(FXCollections.observableArrayList(childDiary.getDiary()));
				}
			} );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void actionEditDiaryEntry(ActionEvent actionEvent) {
		Child child = childDiary.getChild();
		if(child == null)
			return;

		DiaryEntry diaryEntry = tableViewDiary.getSelectionModel().getSelectedItem();
		if(diaryEntry == null)
			return;

		Stage stage = new Stage();
		Parent root = null;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/add_diary_entry.fxml"));
			AddDiaryEntryController addDiaryEntryController = new AddDiaryEntryController(child, null, diaryEntry);
			loader.setController(addDiaryEntryController);
			root = loader.load();
			stage.setTitle("Edit Diary Entry");
			stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
			stage.setResizable(false);
			stage.show();

			stage.setOnHiding( event -> {
				DiaryEntry editedDiaryEntry = addDiaryEntryController.getDiaryEntry();
				if (editedDiaryEntry != null) {
					child.setActivity(editedDiaryEntry.getActivity());
					childDiary.getDiary().remove(diaryEntry);
					childDiary.addDiaryEntry(editedDiaryEntry);
					tableViewDiary.setItems(FXCollections.observableArrayList(childDiary.getDiary()));
				}
			} );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void actionDeleteDiaryEntry(ActionEvent actionEvent) {
		DiaryEntry diaryEntry = tableViewDiary.getSelectionModel().getSelectedItem();
		if(diaryEntry == null)
			return;

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Alert");
		alert.setHeaderText("Delete the Diary Entry");
		alert.setContentText("Deleting this diary entry will delete it completely from the database?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			childDiary.getDiary().remove(diaryEntry);
			tableViewDiary.setItems(FXCollections.observableArrayList(childDiary.getDiary()));
		} else {
			return;
		}
	}

	private void filterList(Timeframe timeframe) {
		ObservableList<DiaryEntry> filteredList = FXCollections.observableList(childDiary.getDiary().stream().filter(de -> {
			switch (timeframe) {
				case All_time:
					return true;
				case This_year:
					return de.getTimeDate().getYear() == LocalDate.now().getYear();
				case Last_30_days:
					return de.getTimeDate().toLocalDate().plusDays(30).isAfter(LocalDate.now());
				case Last_7_days:
					return de.getTimeDate().toLocalDate().plusDays(7).isAfter(LocalDate.now());
				case Today:
					return de.getTimeDate().toLocalDate().isEqual(LocalDate.now());
				default:
					return false;
			}
		}).collect(Collectors.toList()));

		tableViewDiary.setItems(filteredList);
	}

	public ChildDiary getChildDiary() {
		return childDiary;
	}
}
