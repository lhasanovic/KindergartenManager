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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class TeacherController {
	public TableView<Child> tableViewChildren;
	public TableColumn colChildId, colChildFirstName, colChildLastName, colChildActivity;
	public TableColumn<Child, String> colChildBirth, colChildSpecialNeeds;
	public ImageView homeImg;

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
		colChildBirth.setComparator((s1, s2) -> {
			String[] dateSplit1 = s1.split("\\.");
			String[] dateSplit2 = s2.split("\\.");

			if(!dateSplit1[2].equals(dateSplit2[2]))
				return Integer.compare(Integer.parseInt(dateSplit1[2]), Integer.parseInt(dateSplit2[2]));
			else if(!dateSplit1[1].equals(dateSplit2[1]))
				return Integer.compare(Integer.parseInt(dateSplit1[1]), Integer.parseInt(dateSplit2[1]));
			else
				return Integer.compare(Integer.parseInt(dateSplit1[0]), Integer.parseInt(dateSplit2[0]));
		});
		colChildActivity.setCellValueFactory(new PropertyValueFactory("activity"));
		colChildSpecialNeeds.setCellValueFactory(data -> new SimpleStringProperty(
				data.getValue() instanceof SpecialNeedsChild ? dao.getBundle().getString("yes") :
						dao.getBundle().getString("no")
		));
		homeImg.setImage(new Image("/img/home_small.png"));
	}

	public void actionSetActivity(ActionEvent actionEvent) {
		Child child = tableViewChildren.getSelectionModel().getSelectedItem();
		if(child == null)
			return;

		Stage stage = new Stage();
		Parent root = null;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/set_activity.fxml"), dao.getBundle());
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
					dao.insertDiaryEntry(child, new DiaryEntry(LocalDateTime.now(), child.getActivity(), ""));
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

	public void actionHome(ActionEvent actionEvent) {
		Stage stage = (Stage) homeImg.getScene().getWindow();
		Parent root = null;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/home_screen.fxml"), dao.getBundle());
			HomeScreenController homeScreenController = new HomeScreenController(dao);
			loader.setController(homeScreenController);
			root = loader.load();
			stage.setTitle("Kindergarten App");
			stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void resetDatabase() {
		KindergartenDAO.removeInstance();
		File dbfile = new File("database.db");
		dbfile.delete();
		dao = KindergartenDAO.getInstance();
	}
}
