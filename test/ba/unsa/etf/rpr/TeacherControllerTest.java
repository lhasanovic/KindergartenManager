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
class TeacherControllerTest {
	Stage theStage;
	TeacherController ctrl;

	@Start
	public void start (Stage stage) throws Exception {
		KindergartenDAO.removeInstance();
		File dbfile = new File("database.db");
		dbfile.delete();
		KindergartenDAO dao = KindergartenDAO.getInstance();
		KindergartenTeacher teacher = dao.getTeacher(10004);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/teacher.fxml"), ResourceBundle.getBundle("Translate"));
		ctrl = new TeacherController(dao.getTeacherClass(teacher.getId()));
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
	void testLoaded(FxRobot robot) {
		ctrl.resetDatabase();

		TableView tableView = robot.lookup("#tableViewChildren").queryAs(TableView.class);
		assertEquals(2, tableView.getItems().size());
	}

	@Test
	void setActivity(FxRobot robot) {
		ctrl.resetDatabase();

		robot.clickOn("Niko");
		robot.clickOn(("#setActivityBtn"));
		robot.lookup("#chooseActivityBox").tryQuery().isPresent();

		robot.clickOn("#chooseActivityBox");
		robot.clickOn("Sleeping");
		robot.clickOn("#okBtn");

		robot.clickOn("Niko");
		TableView<Child> childTableView = robot.lookup("#tableViewChildren").queryAs(TableView.class);
		Child niko = childTableView.getSelectionModel().getSelectedItem();
		assertEquals(ChildActivity.Sleeping, niko.getActivity());
	}

	@Test
	void addDiaryEntry(FxRobot robot) {
		ctrl.resetDatabase();

		robot.clickOn("Niko");
		robot.clickOn("#addDiaryEntryBtn");

		while(!robot.lookup("#childActivityBox").tryQuery().isPresent());

		robot.clickOn("#childActivityBox");
		robot.clickOn("Eating");

		robot.clickOn("#descriptionArea");
		robot.write("Testing");

		robot.clickOn("#okBtn");

		robot.clickOn("Niko");
		TableView<Child> childTableView = robot.lookup("#tableViewChildren").queryAs(TableView.class);
		Child niko = childTableView.getSelectionModel().getSelectedItem();

		ChildDiary diary = KindergartenDAO.getInstance().getDiaryForChild(niko);
		assertEquals("Testing", diary.getDiary().get(diary.getDiary().size()-1).getDescription());
	}

	@Test
	void viewDiary(FxRobot robot) {
		ctrl.resetDatabase();

		robot.clickOn("Niko");
		robot.clickOn("#viewDiaryBtn");
		robot.lookup("#addDiaryEntryBtn").tryQuery().isPresent();

		robot.clickOn("Crying");
		robot.clickOn("#editDiaryEntryBtn");
		robot.lookup("#childBox").tryQuery().isPresent();

		robot.clickOn("#childActivityBox");
		robot.clickOn("Group activity");

		robot.clickOn("#descriptionArea");
		robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.CONTROL).release(KeyCode.A);
		robot.write("Testing");

		robot.clickOn("#okBtn");
		robot.sleep(300);
		robot.clickOn("Group activity");

		TableView<DiaryEntry> tableView = robot.lookup("#tableViewDiary").queryAs(TableView.class);
		DiaryEntry diaryEntry = tableView.getSelectionModel().getSelectedItem();

		assertEquals("Testing", diaryEntry.getDescription());
	}

	@Test
	void viewDiary2(FxRobot robot) {
		ctrl.resetDatabase();

		robot.clickOn("Niko");
		robot.clickOn("#viewDiaryBtn");
		robot.lookup("#addDiaryEntryBtn").tryQuery().isPresent();

		robot.clickOn("Crying");
		robot.clickOn("#deleteDiaryEntryBtn");

		robot.lookup(".dialog-pane").tryQuery().isPresent();

		DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
		Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
		robot.clickOn(okButton);

		while(!robot.lookup("#tableViewDiary").tryQuery().isPresent());

		TableView tableView = robot.lookup("#tableViewDiary").queryAs(TableView.class);
		assertEquals(1, tableView.getItems().size());

		robot.clickOn("#addDiaryEntryBtn");
		robot.lookup("#childBox").tryQuery().isPresent();

		robot.clickOn("#childActivityBox");
		robot.clickOn("Eating");

		robot.clickOn("#descriptionArea");
		robot.write("Testing");

		robot.clickOn("#okBtn");
		assertEquals(2, tableView.getItems().size());
	}
}
