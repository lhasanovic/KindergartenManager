package ba.unsa.etf.rpr;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ParentController {
	private Child child;
	private KindergartenTeacher teacher;

	public ImageView activityImg;
	public Label activityLabel;

	public ParentController(Child child, KindergartenTeacher teacher) {
		this.child = child;
		this.child.setActivity(ChildActivity.Crying);
		this.teacher = teacher;
	}

	@FXML
	public void initialize() {
		activityImg.setImage(new Image("/img/" + child.getActivity().toString() + ".jpg"));
		activityLabel.setText("Your child is currently " + child.getActivity().toString().toLowerCase());
	}
}
