package ba.unsa.etf.rpr;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.File;
import java.util.ResourceBundle;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

@ExtendWith(ApplicationExtension.class)
class HomeScreenControllerTest {
	Stage theStage;
	HomeScreenController ctrl;

	@Start
	public void start (Stage stage) throws Exception {
		KindergartenDAO.removeInstance();
		File dbfile = new File("database.db");
		dbfile.delete();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/home_screen.fxml"), ResourceBundle.getBundle("Translate"));
		ctrl = new HomeScreenController(KindergartenDAO.getInstance());
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
	public void testTeacherLogin(FxRobot robot) {
		KindergartenDAO.removeInstance();
		File dbfile = new File("database.db");
		dbfile.delete();

		robot.clickOn("#teacherBtn");
		robot.lookup("#nameField").tryQuery().isPresent();

		robot.clickOn("#nameField");
		robot.write("Adnan");
		robot.clickOn("#idField");
		robot.write("10004");

		robot.clickOn("#confirmBtn");
	}

	@Test
	public void testParentLogin(FxRobot robot) {
		KindergartenDAO.removeInstance();
		File dbfile = new File("database.db");
		dbfile.delete();

		robot.clickOn("#parentBtn");
		robot.lookup("#nameField").tryQuery().isPresent();

		robot.clickOn("#nameField");
		robot.write("Ivan");
		robot.clickOn("#idField");
		robot.write("10001");

		robot.clickOn("#confirmBtn");
	}
}