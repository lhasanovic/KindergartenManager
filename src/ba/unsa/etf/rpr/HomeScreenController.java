package ba.unsa.etf.rpr;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
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
	public RadioButton usRadio;
	public RadioButton bihRadio;
	public ImageView usImage;
	public ImageView bihImage;
	public ImageView helpImage;

	private KindergartenDAO dao;

	public HomeScreenController(KindergartenDAO dao) {
		this.dao = dao;
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
		language.getToggles().add(usRadio);

		if(Locale.getDefault().getLanguage().equals("bs"))
			language.selectToggle(bihRadio);
		else if(Locale.getDefault().getLanguage().equals("en"))
			language.selectToggle(usRadio);

		bihImage.setImage(new Image("/img/bih.png"));
		bihImage.setFitHeight(40);
		bihImage.setFitWidth(40);
		usImage.setImage(new Image("/img/us.png"));
		usImage.setFitWidth(40);
		usImage.setFitWidth(40);
		helpImage.setImage(new Image("/img/question-mark-32.png"));

		language.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
			if(oldVal.equals(newVal))
				return;

			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle(dao.getBundle().getString("alert"));
			alert.setHeaderText(dao.getBundle().getString("change_language"));
			alert.setContentText(dao.getBundle().getString("reopen_the_app"));

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				if(newVal.equals(bihRadio))
					dao.setLanguage("bs", "BA");
				else if(newVal.equals(usRadio))
					dao.setLanguage("en", "US");
				System.exit(0);
			} else {
				return;
			}
		});
	}

	public void actionTeacherBtn(ActionEvent actionEvent) {
		teacherOrParentLabel.setVisible(false);
		idField.setVisible(true);
		idField.setPromptText(dao.getBundle().getString("enter_your_teacher_id"));
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
		idField.setPromptText(dao.getBundle().getString("enter_your_child_id"));
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
			String title = dao.getBundle().getString("please_enter_valid_id");
			String text = dao.getBundle().getString("id_5_digit_number");
			notify(title, text);
		} catch (InvalidTeacherDataException | InvalidChildDataException e1) {
			String title = "";
			String text = "";
			if(e1.getMessage().equals("Name")) {
				title = dao.getBundle().getString("please_enter_first_name");
			} else if(e1.getMessage().equals("ID")) {
				title = dao.getBundle().getString("id_name_combination_not_registered");
				text = dao.getBundle().getString("check_for_possible_mistakes");
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
		Stage stage = (Stage) welcomeLabel.getScene().getWindow();
		Parent root = null;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/teacher.fxml"), dao.getBundle());
			TeacherController teacherController = new TeacherController(dao.getTeacherClass(teacher.getId()));
			loader.setController(teacherController);
			root = loader.load();
			stage.setTitle(dao.getBundle().getString("teacher_dashboard"));
			stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
			stage.show();
		} catch (IOException | InvalidTeacherDataException e) {
			e.printStackTrace();
		}
	}

	private void startAppAsParent(Child child) {
		Stage stage = (Stage) welcomeLabel.getScene().getWindow();
		Parent root = null;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/parent.fxml"), dao.getBundle());
			ParentController teacherController = new ParentController(dao.getDiaryForChild(child));
			loader.setController(teacherController);
			root = loader.load();
			stage.setTitle(dao.getBundle().getString("parent_dashboard"));
			stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void startAppAsAdmin() {
		Stage stage = (Stage) welcomeLabel.getScene().getWindow();
		Parent root = null;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin.fxml"), dao.getBundle());
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

	public void actionHelp(ActionEvent actionEvent) {
		File helpFile;
		if(bihRadio.isSelected())
			helpFile = new File(getClass().getResource("/help/Help_bs/Help.html").getFile());
		else
			helpFile = new File(getClass().getResource("/help/Help_en/Help.html").getFile());

		try {
			Desktop.getDesktop().open(helpFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
