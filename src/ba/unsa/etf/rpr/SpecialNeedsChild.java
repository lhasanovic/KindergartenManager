package ba.unsa.etf.rpr;

import java.time.LocalDate;

public class SpecialNeedsChild extends Child {
	private String specialNeedDescription;

	public SpecialNeedsChild(int id, String firstName, String lastName, LocalDate dateOfBirth, String parentName, String phoneNumber, String specialNeedDescription) {
		super(id, firstName, lastName, dateOfBirth, parentName, phoneNumber);
		this.specialNeedDescription = specialNeedDescription;
	}

	public SpecialNeedsChild(int id, String firstName, String lastName, int day, int month, int year, String parentName, String phoneNumber, String specialNeedDescription) throws InvalidChildBirthDateException {
		super(id, firstName, lastName, day, month, year,  parentName, phoneNumber);
		this.specialNeedDescription = specialNeedDescription;
	}

	public String getSpecialNeedDescription() {
		return specialNeedDescription;
	}

	public void setSpecialNeedDescription(String specialNeedDescription) {
		this.specialNeedDescription = specialNeedDescription;
	}
}