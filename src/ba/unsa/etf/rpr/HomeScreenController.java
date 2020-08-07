package ba.unsa.etf.rpr;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.util.Optional;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class HomeScreenController {
	public Label welcomeLabel;
	public Label teacherOrParentLabel;
	public Button teacherBtn;
	public Button parentBtn;
	public Label enterIdLabel;
	public TextField idField;
	public TextField nameField;
	public Button confirmBtn;

	private KindergartenDAO dao;

	public HomeScreenController() {
		dao = KindergartenDAO.getInstance();
	}

	@FXML
	public void initialize() {
		idField.setVisible(false);
		nameField.setVisible(false);
		confirmBtn.setVisible(false);
		enterIdLabel.setVisible(false);
	}

	public void actionTeacherBtn(ActionEvent actionEvent) {
		teacherOrParentLabel.setVisible(false);
		idField.setVisible(true);
		idField.setPromptText("Enter your teacher ID");
		nameField.setVisible(true);
		nameField.setPromptText("Enter your first name");
		confirmBtn.setVisible(true);

		teacherBtn.setDisable(true);
		parentBtn.setDisable(false);

		enterIdLabel.setVisible(true);
		confirmBtn.setDefaultButton(true);
	}

	public void actionParentBtn(ActionEvent actionEvent) {
		teacherOrParentLabel.setVisible(false);
		idField.setVisible(true);
		idField.setPromptText("Enter your child's ID");
		nameField.setVisible(true);
		nameField.setPromptText("Enter your first name");
		confirmBtn.setVisible(true);

		parentBtn.setDisable(true);
		teacherBtn.setDisable(false);

		enterIdLabel.setVisible(true);
		confirmBtn.setDefaultButton(true);
	}

	public void actionConfirmBtn(ActionEvent actionEvent) {
		try {
			if(teacherBtn.isDisable()) {
				String name = nameField.getText();
				if(name == null || name.isEmpty())
					throw new InvalidTeacherDataException("Name");

				String idText = idField.getText();
				int id = Integer.parseInt(idText);
				if(idText.length() != 5)
					throw new NumberFormatException();

				KindergartenTeacher teacher = dao.getTeacher((int)id);
				if(teacher == null || !teacher.getFirstName().equals(name))
					throw new InvalidTeacherDataException("ID");

				startAppAsTeacher(teacher);
			} else if(parentBtn.isDisable()) {
				String name = nameField.getText();
				if(name == null || name.isEmpty())
					throw new InvalidChildDataException("Name");

				int id = Integer.parseInt(idField.getText());
				if(idField.getText().length() != 5)
					throw new NumberFormatException();

				Child child = dao.getChild(id);
				if(child == null || !child.getParent().getFirstName().equals(name))
					throw new InvalidChildDataException("ID");

				startAppAsParent(child);
			}
		} catch (NumberFormatException e) {
			String title = "Please enter a valid ID!";
			String text = "ID is a 5-digit number";
			notify(title, text);
		} catch (InvalidTeacherDataException e1) {
			String title = "";
			String text = "";
			if(e1.getMessage().equals("Name")) {
				title = "Please enter your first name!";
			} else if(e1.getMessage().equals("ID")) {
				title = "This ID/name combination isn't registered!";
				text = "Check for possible mistakes";
			}
			notify(title, text);
		} catch (InvalidChildDataException e2) {
			String title = "";
			String text = "";
			if(e2.getMessage().equals("Name")) {
				title = "Please enter YOUR first name!";
			} else if(e2.getMessage().equals("ID")) {
				title = "This ID/parent name combination isn't registered!";
				text = "Check for possible mistakes";
			}
			notify(title, text);
		}
	}

	public void actionAdminBtn(ActionEvent actionEvent) {
		PasswordDialog pd = new PasswordDialog();
		Optional<String> result = pd.showAndWait();
		result.ifPresent(password -> {
			if(password.equals(dao.getAdminPassword())) {
				startAppAsAdmin();
			} else {
				actionAdminBtn(new ActionEvent());
			}
		});
	}

	private void notify(String title, String text) {
		Notifications notificationBuilder = Notifications.create()
				.title(title)
				.text(text)
				.hideAfter(Duration.seconds(2))
				.position(Pos.BOTTOM_RIGHT);
		notificationBuilder.showError();
	}

	private void startAppAsTeacher(KindergartenTeacher teacher) {
		Stage stage = new Stage();
		Parent root = null;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/teacher.fxml"));
			TeacherController teacherController = new TeacherController(dao.getTeacherClass(teacher.getId()));
			loader.setController(teacherController);
			root = loader.load();
			stage.setTitle("Teacher Dashboard");
			stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
			stage.show();
		} catch (IOException | InvalidTeacherDataException e) {
			e.printStackTrace();
		}
	}

	private void startAppAsParent(Child child) {
		Stage stage = new Stage();
		Parent root = null;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/parent.fxml"));
			ParentController teacherController = new ParentController(child, child.getTeacher());
			loader.setController(teacherController);
			root = loader.load();
			stage.setTitle("Parent Dashboard");
			stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void startAppAsAdmin() {
		System.exit(0);
	}
}
