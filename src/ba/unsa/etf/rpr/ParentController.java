package ba.unsa.etf.rpr;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ParentController {
	private ChildDiary childDiary;

	public ImageView activityImg;
	public Label activityLabel;

	public ParentController(ChildDiary childDiary) {
		this.childDiary = childDiary;
	}

	@FXML
	public void initialize() {
		activityImg.setImage(new Image("/img/" + childDiary.getChild().toString() + ".jpg"));
		activityLabel.setText("Your child is currently " + childDiary.getChild().getActivity().toString().toLowerCase());
	}
}
