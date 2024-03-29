package ba.unsa.etf.rpr;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class ParentController {
	private ChildDiary childDiary;

	public ImageView activityImg;
	public ImageView homeImg;
	public Label activityLabel;
	public Label teacherFirstNameLabel;
	public Label teacherLastNameLabel;
	public Label teacherPhoneNumberLabel;
	public Label kgNameLabel;

	public ParentController(ChildDiary childDiary) {
		this.childDiary = childDiary;
	}

	@FXML
	public void initialize() {
		if(childDiary.getDiary().isEmpty()) {
			activityImg.setImage(new Image("/img/" + ChildActivity.getEnumName(ChildActivity.Not_present) + ".jpg"));
			activityLabel.setText("No recent activity");
		} else {
			activityImg.setImage(new Image("/img/" + ChildActivity.getEnumName(childDiary.getLatestDiaryEntry().getActivity()) + ".jpg"));
			activityLabel.setText(childDiary.getLatestDiaryEntry().getActivity().toString().toLowerCase());
		}
		homeImg.setImage(new Image("/img/home.png"));
		teacherFirstNameLabel.setText(childDiary.getChild().getTeacher().getFirstName());
		teacherLastNameLabel.setText(childDiary.getChild().getTeacher().getLastName());
		teacherPhoneNumberLabel.setText(childDiary.getChild().getTeacher().getPhoneNumber());
		kgNameLabel.setText(KindergartenDAO.getInstance().getName());
	}

	public void actionViewDiary(ActionEvent actionEvent) {
		Stage stage = new Stage();
		Parent root = null;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/view_diary_parent.fxml"), ResourceBundle.getBundle("Translate"));
			ViewDiaryController viewDiaryController = new ViewDiaryController(childDiary);
			loader.setController(viewDiaryController);
			root = loader.load();
			stage.setTitle(ResourceBundle.getBundle("Translate").getString("view_diary"));
			stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
			stage.setResizable(false);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void actionHome(ActionEvent actionEvent) {
		Stage stage = (Stage) homeImg.getScene().getWindow();
		Parent root = null;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/home_screen.fxml"), ResourceBundle.getBundle("Translate"));
			HomeScreenController homeScreenController = new HomeScreenController(KindergartenDAO.getInstance());
			loader.setController(homeScreenController);
			root = loader.load();
			stage.setTitle("Kindergarten App");
			stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
