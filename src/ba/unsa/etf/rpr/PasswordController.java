package ba.unsa.etf.rpr;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class PasswordController {
	public PasswordField oldPasswordField;
	public PasswordField newPasswordField;

	private final String currentPassword;
	private String newPassword;

	public PasswordController(String currentPassword) {
		this.currentPassword = currentPassword;
		this.newPassword = "";
	}

	public void actionOk(ActionEvent actionEvent) {
		if(!oldPasswordField.getText().equals(currentPassword)) {
			String title = "Old password incorrect!";
			String text = "";
			notify(title, text);
		} else if(newPasswordField.getText().length() < 8) {
			String title = "New password too short!";
			String text = "Password must contain at least 8 characters";
			notify(title, text);
		} else if(newPasswordField.getText().length() > 16) {
			String title = "New password too long";
			String text = "Password should contain no more than 16 characters";
			notify(title, text);
		} else {
			newPassword = newPasswordField.getText();
			Stage stage = (Stage) oldPasswordField.getScene().getWindow();
			stage.close();
		}
	}

	public void actionCancel(ActionEvent actionEvent) {
		newPassword = "";
		Stage stage = (Stage) oldPasswordField.getScene().getWindow();
		stage.close();
	}

	public String getNewPassword() {
		return newPassword;
	}

	private void notify(String title, String text) {
		Notifications notificationBuilder = Notifications.create()
				.title(title)
				.text(text)
				.hideAfter(Duration.seconds(2))
				.position(Pos.BOTTOM_RIGHT);
		notificationBuilder.showError();
	}
}
