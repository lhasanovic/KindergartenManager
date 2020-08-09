package ba.unsa.etf.rpr;

import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminController {
	public TableView<KindergartenTeacher> teacherTableView;
	public TableColumn colTeacherId, colTeacherFirstName, colTeacherLastName, colTeacherPhone;
	public TableColumn<KindergartenTeacher, String> colClassSize;

	private KindergartenDAO dao;
	private ObservableList<KindergartenTeacher> teachers;

	public AdminController() {
		this.dao = KindergartenDAO.getInstance();
		this.teachers = FXCollections.observableArrayList(dao.getTeachers());
	}

	@FXML
	public void initialize() {
		teacherTableView.setItems(teachers);
		colTeacherId.setCellValueFactory(new PropertyValueFactory("id"));
		colTeacherFirstName.setCellValueFactory(new PropertyValueFactory("firstName"));
		colTeacherLastName.setCellValueFactory(new PropertyValueFactory("lastName"));
		colTeacherPhone.setCellValueFactory(new PropertyValueFactory("phoneNumber"));
		colClassSize.setCellValueFactory(data -> {
			try {
				return new SimpleStringProperty(String.valueOf(dao.getTeacherClass(data.getValue().getId()).size()));
			} catch (InvalidTeacherDataException e) {
				e.printStackTrace();
				return null;
			}
		});
	}


}
