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

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class TeacherController {
	public TableView<Child> tableViewChildren;
	public TableColumn colChildId, colChildFirstName, colChildLastName, colChildActivity;
	public TableColumn<Child, String> colChildBirth;

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
			stage.setTitle("Change Activity");
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
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/set_activity.fxml"));
			SetActivityController setActivityController = new SetActivityController(child);
			loader.setController(setActivityController);
			root = loader.load();
			stage.setTitle("Change Activity");
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
}
