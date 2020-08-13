package ba.unsa.etf.rpr;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;

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
	public RadioButton ukRadio;
	public RadioButton bihRadio;
	public ImageView ukImage;
	public ImageView bihImage;

	private KindergartenDAO dao;
	private ResourceBundle bundle = ResourceBundle.getBundle("Translation");

	public HomeScreenController() {
		dao = KindergartenDAO.getInstance();
	}

	@FXML
	public void initialize() {
		idField.setVisible(false);
		nameField.setVisible(false);
		confirmBtn.setVisible(false);
		enterIdLabel.setVisible(false);

		welcomeLabel.setText(welcomeLabel.getText() + " " + dao.getName());

		ToggleGroup language = new ToggleGroup();
		language.getToggles().add(bihRadio);
		language.getToggles().add(ukRadio);

		language.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Alert");
			alert.setHeaderText("Change Language");
			alert.setContentText("You'll need to reopen the app in order to change the language");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){

			} else {
				return;
			}
		});
	}

	public void actionTeacherBtn(ActionEvent actionEvent) {
		teacherOrParentLabel.setVisible(false);
		idField.setVisible(true);
		idField.setPromptText(bundle.getString("enter_your_teacher_id"));
		nameField.setVisible(true);
		confirmBtn.setVisible(true);

		teacherBtn.setDisable(true);
		parentBtn.setDisable(false);

		enterIdLabel.setVisible(true);
		confirmBtn.setDefaultButton(true);
	}

	public void actionParentBtn(ActionEvent actionEvent) {
		teacherOrParentLabel.setVisible(false);
		idField.setVisible(true);
		idField.setPromptText(bundle.getString("enter_your_child_id"));
		nameField.setVisible(true);
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
			String title = bundle.getString("please_enter_valid_id");
			String text = bundle.getString("id_5_digit_number");
			notify(title, text);
		} catch (InvalidTeacherDataException | InvalidChildDataException e1) {
			String title = "";
			String text = "";
			if(e1.getMessage().equals("Name")) {
				title = bundle.getString("please_enter_first_name");
			} else if(e1.getMessage().equals("ID")) {
				title = bundle.getString("id_name_combination_not_registered");
				text = bundle.getString("check_for_possible_mistakes");
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
			stage.setTitle(bundle.getString("teacher_dashboard"));
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
			ParentController teacherController = new ParentController(dao.getDiaryForChild(child));
			loader.setController(teacherController);
			root = loader.load();
			stage.setTitle(bundle.getString("parent_bashboard"));
			stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void startAppAsAdmin() {
		Stage stage = new Stage();
		Parent root = null;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin.fxml"));
			AdminController adminController = new AdminController();
			loader.setController(adminController);
			root = loader.load();
			stage.setTitle("Admin panel");
			stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
