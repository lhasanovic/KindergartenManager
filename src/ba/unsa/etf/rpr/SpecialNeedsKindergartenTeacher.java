package ba.unsa.etf.rpr;

public class SpecialNeedsKindergartenTeacher extends KindergartenTeacher{
	public SpecialNeedsKindergartenTeacher(int id, String firstName, String lastName, String phoneNumber) {
		super(id, firstName, lastName, phoneNumber);
	}

	public SpecialNeedsKindergartenTeacher() {}

	@Override
	public String toString() {
		return super.toString() + " (SP)";
	}
}
