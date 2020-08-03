package ba.unsa.etf.rpr;

import java.time.LocalDate;

public class Child {
	private int id;
	private String firstName;
	private String lastName;
	private LocalDate dateOfBirth;
	private Parent parent;

	public Child(int id, String firstName, String lastName, LocalDate dateOfBirth, String parentName, String phoneNumber) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.parent = new Parent(parentName, lastName, phoneNumber);
	}

	public Child(int id, String firstName, String lastName, int day, int month, int year, String parentName, String phoneNumber) throws InvalidChildBirthDateException {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		setDateOfBirth(year, month, day);
		this.parent = new Parent(parentName, lastName, phoneNumber);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Parent getParent() {
		return parent;
	}

	public void setParent(Parent parent) {
		this.parent = parent;
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
			throw new InvalidChildBirthDateException("Child is older than 5 years!");
		if(tempDate.plusYears(2).isAfter(LocalDate.now()))
			throw new InvalidChildBirthDateException("Child is younger than 2 years!");

		this.dateOfBirth = LocalDate.of(year, month, day);
	}

	@Override
	public String toString() {
		return firstName + " (" + parent.getFirstName() + ") " + lastName;
	}
}
