package ba.unsa.etf.rpr;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

import java.util.ResourceBundle;

public class PasswordDialog extends Dialog<String> {
	private PasswordField passwordField;
	private Label errorLabel;

	public PasswordDialog() {
		setTitle(ResourceBundle.getBundle("Translate").getString("admin_password"));
		setHeaderText(ResourceBundle.getBundle("Translate").getString("please_enter_admin_password"));

		ButtonType passwordButtonType = new ButtonType(ResourceBundle.getBundle("Translate").getString("confirm"), ButtonData.OK_DONE);
		getDialogPane().getButtonTypes().addAll(passwordButtonType, ButtonType.CANCEL);

		passwordField = new PasswordField();
		passwordField.setPromptText(ResourceBundle.getBundle("Translate").getString("password"));

		errorLabel = new Label();
		errorLabel.setText(ResourceBundle.getBundle("Translate").getString("password_incorrect"));
		errorLabel.setTextFill(Color.RED);
		errorLabel.setVisible(false);

		HBox hBox = new HBox();
		hBox.getChildren().add(passwordField);
		hBox.getChildren().add(errorLabel);
		hBox.setPadding(new Insets(20));

		HBox.setHgrow(passwordField, Priority.ALWAYS);

		getDialogPane().setContent(hBox);

		Platform.runLater(() -> passwordField.requestFocus());

		setResultConverter(dialogButton -> {
			if (dialogButton == passwordButtonType) {
				return passwordField.getText();
			}
			return null;
		});

		passwordField.setOnKeyTyped(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				errorLabel.setVisible(false);
			}
		});
	}

	public PasswordField getPasswordField() {
		return passwordField;
	}
	public Label getErrorLabel() { return errorLabel; }
}