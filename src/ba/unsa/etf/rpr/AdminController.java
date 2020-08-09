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

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

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

	public void actionHireTeacher(ActionEvent actionEvent) {

	}

	public void actionFireTeacher(ActionEvent actionEvent) {

	}

	public void actionRegisterChild(ActionEvent actionEvent) {

	}

	public void actionEditTeacher(ActionEvent actionEvent) {

	}

	public void actionChangePassword(ActionEvent actionEvent) {
		Stage stage = new Stage();
		Parent root = null;
		try {
			String currentPassword = dao.getAdminPassword();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/change_password.fxml"));
			PasswordController passwordController = new PasswordController(currentPassword);
			loader.setController(passwordController);
			root = loader.load();
			stage.setTitle("Change Password");
			stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
			stage.show();

			stage.setOnHiding( event -> {
				String newPassword = passwordController.getNewPassword();
				if (newPassword.length() != 0) {
					dao.setAdminPassword(newPassword);
				}
			} );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}




}
