package ba.unsa.etf.rpr;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.ArrayList;
import java.util.ResourceBundle;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class AdminControllerTest {
	Stage theStage;
	AdminController ctrl;

	@Start
	public void start (Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin.fxml"), ResourceBundle.getBundle("Translate"));
		ctrl = new AdminController();
		ctrl.resetDatabase();
		loader.setController(ctrl);
		Parent root = loader.load();
		stage.setTitle("TEST");
		stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
		stage.setResizable(false);
		stage.show();

		stage.toFront();

		theStage = stage;
	}

	@Test
	public void testLoaded(FxRobot robot) {
		ctrl.resetDatabase();

		TableView childTableView = robot.lookup("#childrenTableView").queryAs(TableView.class);
		TableView teacherTableView = robot.lookup("#teacherTableView").queryAs(TableView.class);

		assertEquals(2, teacherTableView.getItems().size());
		assertEquals(3, childTableView.getItems().size());
	}

	@Test
	public void testFireTeacher(FxRobot robot) {
		ctrl.resetDatabase();

		robot.clickOn("Luka");
		robot.clickOn("#fireTeacherBtn");

		robot.lookup(".dialog-pane").tryQuery().isPresent();

		DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
		Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
		robot.clickOn(okButton);

		TableView teacherTableView = robot.lookup("#teacherTableView").queryAs(TableView.class);

		assertEquals(1, teacherTableView.getItems().size());
		assertEquals(1, KindergartenDAO.getInstance().getTeachers().size());
	}

	@Test
	public void testHireTeacher(FxRobot robot) {
		ctrl.resetDatabase();

		robot.clickOn("#hireTeacherBtn");
		robot.lookup("#firstNameField").tryQuery().isPresent();

		robot.clickOn("#firstNameField");
		robot.write("Test");

		robot.clickOn("#lastNameField");
		robot.write("Teacher");

		robot.clickOn("#phoneNumberField");
		robot.write("012abc0122");

		robot.clickOn("#specialNeedsNo");
		robot.clickOn("#okBtn");

		robot.clickOn("#phoneNumberField");
		robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
		robot.write("123123123");

		robot.clickOn("#okBtn");

		KindergartenDAO dao = KindergartenDAO.getInstance();
		assertEquals(3, dao.getTeachers().size());
		TableView teacherTableView = robot.lookup("#teacherTableView").queryAs(TableView.class);
		assertEquals(3, teacherTableView.getItems().size());
	}

	@Test
	void editChild(FxRobot robot) {
		ctrl.resetDatabase();

		int mujoID = 10002;

		robot.clickOn("10002");
		robot.clickOn("#editChildBtn");
		robot.lookup("#firstNameField").tryQuery().isPresent();

		robot.clickOn("#firstNameField");
		robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
		robot.write("Test");

		robot.clickOn("#lastNameField");
		robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
		robot.write("Child");

		robot.clickOn("#parentNameField");
		robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
		robot.write("Parent");

		robot.clickOn("#phoneNumberField");
		robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
		robot.write("123123123");

		robot.clickOn("#teacherChoiceBox");
		robot.clickOn("Adnan Adnanovic (SP)");

		robot.clickOn("#okBtn");

		KindergartenDAO dao = KindergartenDAO.getInstance();
		assertEquals("Test", dao.getChild(mujoID).getFirstName());
	}

	@Test
	void deleteChild(FxRobot robot) {
		ctrl.resetDatabase();

		robot.clickOn("10002");
		robot.clickOn("#deleteChildBtn");
		robot.lookup(".dialog-pane").tryQuery().isPresent();

		DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
		Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
		robot.clickOn(okButton);

		boolean mujo = false;

		ArrayList<Child> childArrayList = KindergartenDAO.getInstance().getChildren();
		for(Child c : childArrayList) {
			if(c.getFirstName().equals("Mujo"))
				mujo = true;
		}

		assertFalse(mujo);
	}

	@Test
	void changePassword(FxRobot robot) {
		ctrl.resetDatabase();

		robot.clickOn("#changePasswordBtn");
		robot.lookup("#oldPasswordField").tryQuery().isPresent();

		robot.clickOn("#oldPasswordField");
		robot.write("11112222");

		robot.clickOn("#newPasswordField");
		robot.write("22221111");

		robot.clickOn("#okBtn");

		KindergartenDAO dao = KindergartenDAO.getInstance();
		assertEquals("22221111", dao.getAdminPassword());

		robot.lookup("#changePasswordBtn").tryQuery().isPresent();
		robot.clickOn("#changePasswordBtn");
		robot.lookup("#oldPasswordField").tryQuery().isPresent();

		robot.clickOn("#oldPasswordField");
		robot.write("22221111");

		robot.clickOn("#newPasswordField");
		robot.write("11112222");

		robot.clickOn("#okBtn");
		assertEquals("11112222", dao.getAdminPassword());
	}

	@Test
	void editTeacher(FxRobot robot) {
		ctrl.resetDatabase();

		robot.clickOn("10004");
		robot.clickOn("#editTeacherBtn");

		robot.lookup("#firstNameField").tryQuery().isPresent();

		TextField firstNameField = robot.lookup("#firstNameField").queryAs(TextField.class);
		assertEquals("Adnan", firstNameField.getText());

		robot.clickOn("#phoneNumberField");
		robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.CONTROL).release(KeyCode.A);
		robot.write("987654321");

		robot.clickOn("#okBtn");

		KindergartenDAO dao = KindergartenDAO.getInstance();
		KindergartenTeacher teacher = dao.getTeacher(10004);
		assertEquals("987654321", teacher.getPhoneNumber());
	}
}