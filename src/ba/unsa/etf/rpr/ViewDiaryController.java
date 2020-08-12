package ba.unsa.etf.rpr;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ViewDiaryController {
	public TableView<DiaryEntry> tableViewDiary;
	public TableColumn<DiaryEntry, String> colDate;
	public TableColumn<DiaryEntry, String> colTime;
	public TableColumn colChildActivity;
	public TableColumn colDescription;

	private ChildDiary childDiary;

	public ViewDiaryController(ChildDiary childDiary) {
		this.childDiary = childDiary;
	}


	public ChildDiary getChildDiary() {
		return childDiary;
	}
}
