package ba.unsa.etf.rpr;

import java.time.LocalDate;

public class SpecialNeedsChild extends Child {
	public SpecialNeedsChild(String firstName, String lastName, String parentName, LocalDate dateOfBirth) {
		super(firstName, lastName, parentName, dateOfBirth);
	}

	public SpecialNeedsChild(String firstName, String lastName, String parentName, int day, int month, int year) throws InvalidChildBirthDateException {
		super(firstName, lastName, parentName, day, month, year);
	}
}