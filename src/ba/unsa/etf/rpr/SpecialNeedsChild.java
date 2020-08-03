package ba.unsa.etf.rpr;

import java.time.LocalDate;

public class SpecialNeedsChild extends Child {
	private String specialNeedDescription;

	public SpecialNeedsChild(int id, String firstName, String lastName, String parentName, LocalDate dateOfBirth, String phoneNumber, String specialNeedDescription) {
		super(id, firstName, lastName, parentName, dateOfBirth, phoneNumber);
		this.specialNeedDescription = specialNeedDescription;
	}

	public SpecialNeedsChild(int id, String firstName, String lastName, String parentName, int day, int month, int year, String phoneNumber, String specialNeedDescription) throws InvalidChildBirthDateException {
		super(id, firstName, lastName, parentName, day, month, year, phoneNumber);
		this.specialNeedDescription = specialNeedDescription;
	}

	public String getSpecialNeedDescription() {
		return specialNeedDescription;
	}

	public void setSpecialNeedDescription(String specialNeedDescription) {
		this.specialNeedDescription = specialNeedDescription;
	}
}