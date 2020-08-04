package ba.unsa.etf.rpr;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

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


	}

	public void actionConfirmBtn(ActionEvent actionEvent) {

	}
}
