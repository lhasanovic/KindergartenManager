package ba.unsa.etf.rpr;

public class HireEditTeacherController {


	private KindergartenTeacher teacher;

	public HireEditTeacherController(KindergartenTeacher teacher) {
		this.teacher = teacher;
	}

	public KindergartenTeacher getTeacher() {
		return teacher;
	}
}
