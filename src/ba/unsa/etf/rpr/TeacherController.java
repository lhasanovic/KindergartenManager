package ba.unsa.etf.rpr;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class TeacherController {
	public TableView<Child> tableViewChildren;
	public TableColumn colChildId, colChildFirstName, colChildLastName, colChildActivity;
	public TableColumn<Child, String> colChildBirth;
	private ObservableList<Child> teacherClass;
	private KindergartenDAO dao;

	public TeacherController(ArrayList<Child> teacherClass) {
		this.teacherClass = FXCollections.observableArrayList(teacherClass);
		dao = KindergartenDAO.getInstance();
		System.out.println(teacherClass.size());
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
}
