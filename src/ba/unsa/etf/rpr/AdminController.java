package ba.unsa.etf.rpr;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class AdminController {
	public TableView<KindergartenTeacher> teacherTableView;
	public TableColumn colTeacherId, colTeacherFirstName, colTeacherLastName, colTeacherPhone;
	public TableColumn<KindergartenTeacher, String> colClassSize;

	public TableView<Child> childrenTableView;
	public TableColumn colChildId, colChildFirstName, colChildLastName;
	public TableColumn<Child, String> colChildBirth, colChildParentName, colChildTeacher;

	private KindergartenDAO dao;
	private ObservableList<KindergartenTeacher> teachers;
	private ObservableList<Child> children;

	public AdminController() {
		this.dao = KindergartenDAO.getInstance();
		this.teachers = FXCollections.observableArrayList(dao.getTeachers());
		this.children = FXCollections.observableArrayList(dao.getChildren());
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

		childrenTableView.setItems(children);
		colChildId.setCellValueFactory(new PropertyValueFactory("id"));
		colChildFirstName.setCellValueFactory(new PropertyValueFactory("firstName"));
		colChildLastName.setCellValueFactory(new PropertyValueFactory("lastName"));
		colChildBirth.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDateOfBirthString()));
		colChildParentName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getParent().getFirstName()));
		colChildTeacher.setCellValueFactory(data ->
				new SimpleStringProperty(data.getValue().getTeacher().getFirstName() + " " +
				data.getValue().getTeacher().getLastName()));
	}

	public void actionHireTeacher(ActionEvent actionEvent) {
		Stage stage = new Stage();
		Parent root = null;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/hire_teacher.fxml"), dao.getBundle());
			HireEditTeacherController hireEditTeacherController = new HireEditTeacherController(null);
			loader.setController(hireEditTeacherController);
			root = loader.load();
			stage.setTitle(dao.getBundle().getString("hire_a_teacher"));
			stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
			stage.setResizable(false);
			stage.show();

			stage.setOnHiding( event -> {
				KindergartenTeacher teacher = hireEditTeacherController.getTeacher();
				if (teacher != null) {
					dao.insertTeacher(teacher);
					teachers.setAll(dao.getTeachers());
					children.setAll(dao.getChildren());
				}
			} );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void actionFireTeacher(ActionEvent actionEvent) {
		KindergartenTeacher teacher = teacherTableView.getSelectionModel().getSelectedItem();
		if(teacher == null)
			return;

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle(dao.getBundle().getString("alert"));
		alert.setHeaderText(dao.getBundle().getString("fire_teacher"));
		alert.setContentText(dao.getBundle().getString("firing_this_teacher"));

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			dao.deleteTeacher(teacher);
			teachers.setAll(dao.getTeachers());
			children.setAll(dao.getChildren());
		} else {
			return;
		}
	}

	public void actionRegisterChild(ActionEvent actionEvent) {
		Stage stage = new Stage();
		Parent root = null;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/register_child.fxml"), dao.getBundle());
			RegisterEditChildController registerEditChildController = new RegisterEditChildController(null, (ArrayList<KindergartenTeacher>) dao.getAvailableTeachers());
			loader.setController(registerEditChildController);
			root = loader.load();
			stage.setTitle(dao.getBundle().getString("register_a_child"));
			stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
			stage.setResizable(false);
			stage.show();

			stage.setOnHiding( event -> {
				Child child = registerEditChildController.getChild();
				if (child != null) {
					dao.insertChild(child);
					teachers.setAll(dao.getTeachers());
					children.setAll(dao.getChildren());
				}
			} );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void actionEditChild(ActionEvent actionEvent) {
		Stage stage = new Stage();
		Parent root = null;
		try {
			Child child = childrenTableView.getSelectionModel().getSelectedItem();
			if(child == null)
				return;
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/register_child.fxml"), dao.getBundle());
			RegisterEditChildController registerEditChildController = new RegisterEditChildController(child, (ArrayList<KindergartenTeacher>) dao.getAvailableTeachers());
			loader.setController(registerEditChildController);
			root = loader.load();
			stage.setTitle(dao.getBundle().getString("edit_child"));
			stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
			stage.setResizable(false);
			stage.show();

			stage.setOnHiding( event -> {
				Child editedChild = registerEditChildController.getChild();
				if (editedChild != null) {
					dao.editChild(editedChild);
					teachers.setAll(dao.getTeachers());
					children.setAll(dao.getChildren());
				}
			} );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void actionEditTeacher(ActionEvent actionEvent) {
		Stage stage = new Stage();
		Parent root = null;
		KindergartenTeacher teacher = teacherTableView.getSelectionModel().getSelectedItem();
		if(teacher == null)
			return;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/hire_teacher.fxml"), dao.getBundle());
			HireEditTeacherController hireEditTeacherController = new HireEditTeacherController(teacher);
			loader.setController(hireEditTeacherController);
			root = loader.load();
			stage.setTitle(dao.getBundle().getString("edit_teacher"));
			stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
			stage.setResizable(false);
			stage.show();

			stage.setOnHiding( event -> {
				KindergartenTeacher editedTeacher = hireEditTeacherController.getTeacher();
				if (editedTeacher != null) {
					dao.editTeacher(editedTeacher);
					teachers.setAll(dao.getTeachers());
					children.setAll(dao.getChildren());
				}
			} );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void actionChangePassword(ActionEvent actionEvent) {
		Stage stage = new Stage();
		Parent root = null;
		try {
			String currentPassword = dao.getAdminPassword();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/change_password.fxml"), dao.getBundle());
			PasswordController passwordController = new PasswordController(currentPassword);
			loader.setController(passwordController);
			root = loader.load();
			stage.setTitle(dao.getBundle().getString("change_password"));
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

	public void actionDeleteChild(ActionEvent actionEvent) {
		Child child = childrenTableView.getSelectionModel().getSelectedItem();
		if(child == null)
			return;

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle(dao.getBundle().getString("alert"));
		alert.setHeaderText(dao.getBundle().getString("delete_child"));
		alert.setContentText(dao.getBundle().getString("deleting_this_child"));

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			dao.deleteChild(child);
			teachers.setAll(dao.getTeachers());
			children.setAll(dao.getChildren());
		} else {
			return;
		}
	}
}
