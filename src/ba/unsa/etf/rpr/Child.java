package ba.unsa.etf.rpr;

import java.time.LocalDate;
import java.util.Date;

public class Child {
	private String firstName;
	private String lastName;
	private String parentName;
	private LocalDate dateOfBirth;
	private boolean hasSpecialNeeds;

	public Child(String firstName, String lastName, String parentName, LocalDate dateOfBirth, boolean hasSpecialNeeds) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.parentName = parentName;
		this.dateOfBirth = dateOfBirth;
		this.hasSpecialNeeds = hasSpecialNeeds;
	}

	public Child(String firstName, String lastName, String parentName, int day, int month, int year, boolean hasSpecialNeeds) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.parentName = parentName;
		this.dateOfBirth = LocalDate.of(year, month, day);
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

	public void setDateOfBirth(int year, int month, int day) {
		this.dateOfBirth = LocalDate.of(year, month, day);
	}

	public boolean isHasSpecialNeeds() {
		return hasSpecialNeeds;
	}

	public void setHasSpecialNeeds(boolean hasSpecialNeeds) {
		this.hasSpecialNeeds = hasSpecialNeeds;
	}
}
