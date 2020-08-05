package ba.unsa.etf.rpr;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class TeacherController {
	private ObservableList<Child> teacherClass;

	public TeacherController(ArrayList<Child> teacherClass) {
		this.teacherClass = FXCollections.observableArrayList(teacherClass);
	}
}
