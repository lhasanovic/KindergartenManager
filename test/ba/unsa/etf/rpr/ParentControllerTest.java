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

import java.io.File;
import java.util.ResourceBundle;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class ParentControllerTest {
	Stage theStage;
	ParentController ctrl;

	@Start
	public void start (Stage stage) throws Exception {
		KindergartenDAO.removeInstance();
		File dbfile = new File("database.db");
		dbfile.delete();
		KindergartenDAO dao = KindergartenDAO.getInstance();
		Child niko = dao.getChild(10001);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/parent.fxml"), ResourceBundle.getBundle("Translate"));
		ctrl = new ParentController(dao.getDiaryForChild(niko));
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
	void testLoaded(FxRobot robot) {
		Label activityLabel = robot.lookup("#activityLabel").queryAs(Label.class);

		KindergartenDAO dao = KindergartenDAO.getInstance();
		KindergartenTeacher teacher = dao.getChild(10001).getTeacher();

		assertEquals(ChildActivity.getEnumName(dao.getDiaryForChild(dao.getChild(10001)).getLatestDiaryEntry().getActivity()).toLowerCase(),
				activityLabel.getText());
	}

	@Test
	void viewDiary(FxRobot robot) {
		robot.clickOn("#viewDiaryBtn");
		robot.lookup("#tableViewDiary").tryQuery().isPresent();

		TableView tableView = robot.lookup("#tableViewDiary").queryAs(TableView.class);

		assertEquals(2, tableView.getItems().size());
	}
}
