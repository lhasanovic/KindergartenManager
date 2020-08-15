package ba.unsa.etf.rpr;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class TeacherController {
	public TableView<Child> tableViewChildren;
	public TableColumn colChildId, colChildFirstName, colChildLastName, colChildActivity;
	public TableColumn<Child, String> colChildBirth, colChildSpecialNeeds;

	private ObservableList<Child> teacherClass;
	private KindergartenDAO dao;

	public TeacherController(ArrayList<Child> teacherClass) {
		this.teacherClass = FXCollections.observableArrayList(teacherClass);
		dao = KindergartenDAO.getInstance();
	}

	@FXML
	public void initialize() {
		tableViewChildren.setItems(teacherClass);
		colChildId.setCellValueFactory(new PropertyValueFactory("id"));
		colChildFirstName.setCellValueFactory(new PropertyValueFactory("firstName"));
		colChildLastName.setCellValueFactory(new PropertyValueFactory("lastName"));
		colChildBirth.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDateOfBirthString()));
		colChildActivity.setCellValueFactory(new PropertyValueFactory("activity"));
		colChildSpecialNeeds.setCellValueFactory(data -> new SimpleStringProperty(
				data.getValue() instanceof SpecialNeedsChild ? dao.getBundle().getString("yes") :
						dao.getBundle().getString("no")
		));
	}

	public void actionSetActivity(ActionEvent actionEvent) {
		Child child = tableViewChildren.getSelectionModel().getSelectedItem();
		if(child == null)
			return;

		Stage stage = new Stage();
		Parent root = null;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/set_activity.fxml"));
			SetActivityController setActivityController = new SetActivityController(child);
			loader.setController(setActivityController);
			root = loader.load();
			stage.setTitle(dao.getBundle().getString("change_activity"));
			stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
			stage.setResizable(false);
			stage.show();

			stage.setOnHiding( event -> {
				Child editedChild = setActivityController.getChild();
				if (editedChild != null) {
					child.setActivity(editedChild.getActivity());
					tableViewChildren.refresh();
				}
			} );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void actionAddDiaryEntry(ActionEvent actionEvent) {
		Child child = tableViewChildren.getSelectionModel().getSelectedItem();
		if(child == null)
			return;

		Stage stage = new Stage();
		Parent root = null;

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/add_diary_entry.fxml"), dao.getBundle());
			AddDiaryEntryController addDiaryEntryController = new AddDiaryEntryController(child, teacherClass, null);
			loader.setController(addDiaryEntryController);
			root = loader.load();
			stage.setTitle(dao.getBundle().getString("add_diary_entry"));
			stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
			stage.setResizable(false);
			stage.show();

			stage.setOnHiding( event -> {
				DiaryEntry diaryEntry = addDiaryEntryController.getDiaryEntry();
				if (diaryEntry != null) {
					child.setActivity(diaryEntry.getActivity());
					dao.insertDiaryEntry(child, diaryEntry);
					tableViewChildren.refresh();
				}
			} );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void actionViewDiary(ActionEvent actionEvent) {
		Child child = tableViewChildren.getSelectionModel().getSelectedItem();
		if(child == null)
			return;

		Stage stage = new Stage();
		Parent root = null;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/view_diary.fxml"), dao.getBundle());
			ViewDiaryController viewDiaryController = new ViewDiaryController(dao.getDiaryForChild(child));
			loader.setController(viewDiaryController);
			root = loader.load();
			stage.setTitle(dao.getBundle().getString("view_diary"));
			stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
			stage.setResizable(false);
			stage.show();

			stage.setOnHiding( event -> {
				ChildDiary childDiary = viewDiaryController.getChildDiary();
				if (childDiary != null) {
					dao.deleteDiaryForChild(child);
					for(DiaryEntry de : childDiary.getDiary())
						dao.insertDiaryEntry(child, de);
					tableViewChildren.refresh();
				}
			} );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
