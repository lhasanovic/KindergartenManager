package ba.unsa.etf.rpr;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class HomeScreenController {
	public Label welcomeLabel;
	public Label teacherOrParentLabel;
	public Button teacherBtn;
	public Button parentBtn;
	public Label enterIdLabel;
	public PasswordField idField;
	public Button confirmBtn;

	private KindergartenDAO dao;

	public HomeScreenController() {
		dao = KindergartenDAO.getInstance();
	}

	@FXML
	public void initialize() {
		idField.setVisible(false);
		confirmBtn.setVisible(false);
	}

	public void actionTeacherBtn(ActionEvent actionEvent) {
		teacherOrParentLabel.setVisible(false);
		idField.setVisible(true);
		confirmBtn.setVisible(true);

		teacherBtn.setDisable(true);
		parentBtn.setDisable(false);

		enterIdLabel.setText("Enter your personal teacher ID: ");
	}

	public void actionParentBtn(ActionEvent actionEvent) {
		teacherOrParentLabel.setVisible(false);
		idField.setVisible(true);
		confirmBtn.setVisible(true);

		parentBtn.setDisable(true);
		teacherBtn.setDisable(false);

		enterIdLabel.setText("Enter your child's personal ID: ");
	}

	public void actionConfirmBtn(ActionEvent actionEvent) {
		try {
			int id = Integer.parseInt(idField.getText());

			if(idField.getText().length() != 8)
				throw new NumberFormatException();

			if(teacherBtn.isDisable()) {
				KindergartenTeacher teacher = dao.getTeacher(id);
				if(teacher == null)
					throw new InvalidTeacherIdException();

				startAppAsTeacher(teacher);
			} else if(parentBtn.isDisable()) {
				Child child = dao.getChild(id);
				if(child == null)
					throw new InvalidChildIdException();

				startAppAsParent(child);
			}

		} catch (NumberFormatException e) {
			Notifications notificationBuilder = Notifications.create()
					.title("Please enter a valid ID!")
					.text("ID is an 8-digit number")
					.hideAfter(Duration.seconds(3))
					.position(Pos.BOTTOM_RIGHT);
			notificationBuilder.showError();
		} catch (InvalidTeacherIdException e1) {
			Notifications notificationBuilder = Notifications.create()
					.title("This teacher ID isn't registered!")
					.text("Check for possible mistakes")
					.hideAfter(Duration.seconds(3))
					.position(Pos.BOTTOM_RIGHT);
			notificationBuilder.showError();
		} catch (InvalidChildIdException e2) {
			Notifications notificationBuilder = Notifications.create()
					.title("This child ID isn't registered!")
					.text("Check for possible mistakes")
					.hideAfter(Duration.seconds(3))
					.position(Pos.BOTTOM_RIGHT);
			notificationBuilder.showError();
		}
	}

	private void startAppAsTeacher(KindergartenTeacher teacher) {
	}

	private void startAppAsParent(Child child) {
	}
}
