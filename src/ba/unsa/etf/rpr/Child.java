package ba.unsa.etf.rpr;

import java.time.LocalDate;

public class Child {
	private String firstName;
	private String lastName;
	private String parentName;
	private LocalDate dateOfBirth;

	public Child(String firstName, String lastName, String parentName, LocalDate dateOfBirth) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.parentName = parentName;
		this.dateOfBirth = dateOfBirth;
	}

	public Child(String firstName, String lastName, String parentName, int day, int month, int year) throws InvalidChildBirthDateException {
		this.firstName = firstName;
		this.lastName = lastName;
		this.parentName = parentName;
		setDateOfBirth(year, month, day);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public void setDateOfBirth(int year, int month, int day) throws InvalidChildBirthDateException {
		if(year < 0 || year > LocalDate.now().getYear())
			throw new InvalidChildBirthDateException("Invalid year!");
		if(month < 1 || month > 12)
			throw new InvalidChildBirthDateException("Invalid  month!");
		if(day < 1 || day > 31)
			throw new InvalidChildBirthDateException("Invalid day!");

		LocalDate tempDate = LocalDate.of(year, month, day);

		if(tempDate.plusYears(5).isBefore(LocalDate.now()))
			throw new InvalidChildBirthDateException("Child is older than 4 years!");
		if(tempDate.plusYears(2).isAfter(LocalDate.now()))
			throw new InvalidChildBirthDateException("Child is younger than 2 years!");

		this.dateOfBirth = LocalDate.of(year, month, day);
	}
}
