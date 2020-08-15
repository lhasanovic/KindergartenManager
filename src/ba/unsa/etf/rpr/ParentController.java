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
	public Label activityLabel;
	public Label teacherFirstNameLabel;
	public Label teacherLastNameLabel;
	public Label teacherPhoneNumberLabel;

	public ParentController(ChildDiary childDiary) {
		this.childDiary = childDiary;
	}

	@FXML
	public void initialize() {
		activityImg.setImage(new Image("/img/" + childDiary.getChild().getActivity().toString() + ".jpg"));
		activityLabel.setText(ResourceBundle.getBundle("Translate").getString("current_activity") + " " +
				childDiary.getChild().getActivity().toString().toLowerCase());
		teacherFirstNameLabel.setText(childDiary.getChild().getTeacher().getFirstName());
		teacherLastNameLabel.setText(childDiary.getChild().getTeacher().getLastName());
		teacherPhoneNumberLabel.setText(childDiary.getChild().getTeacher().getPhoneNumber());
	}

	public void actionViewDiary(ActionEvent actionEvent) {
		Stage stage = new Stage();
		Parent root = null;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/view_diary_parent.fxml"));
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
}
