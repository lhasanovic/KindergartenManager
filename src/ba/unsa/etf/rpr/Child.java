package ba.unsa.etf.rpr;

import java.time.LocalDate;

public class Child {
	private int id;
	private String firstName;
	private String lastName;
	private LocalDate dateOfBirth;
	private Parent parent;
	private KindergartenTeacher teacher;
	private ChildActivity activity = ChildActivity.Not_present;

	public Child(int id, String firstName, String lastName, LocalDate dateOfBirth, String parentName, String phoneNumber, KindergartenTeacher teacher) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.parent = new Parent(parentName, lastName, phoneNumber);
		this.teacher = teacher;
	}

	public Child(int id, String firstName, String lastName, int day, int month, int year, String parentName, String phoneNumber, KindergartenTeacher teacher) throws InvalidChildBirthDateException {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		setDateOfBirth(year, month, day);
		this.parent = new Parent(parentName, lastName, phoneNumber);
		this.teacher = teacher;
	}

	public Child() {}

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

	public String getDateOfBirthString() {
		return "" + dateOfBirth.getDayOfMonth() + "." + dateOfBirth.getMonthValue() + "." + dateOfBirth.getYear();
	}

	public void setDateOfBirth(LocalDate dateOfBirth) throws InvalidChildBirthDateException {
		setDateOfBirth(dateOfBirth.getYear(), dateOfBirth.getMonthValue(), dateOfBirth.getDayOfMonth());
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

	public KindergartenTeacher getTeacher() {
		return teacher;
	}

	public void setTeacher(KindergartenTeacher teacher) {
		this.teacher = teacher;
	}

	public ChildActivity getActivity() {
		return activity;
	}

	public void setActivity(ChildActivity activity) {
		this.activity = activity;
	}

	@Override
	public String toString() {
		return firstName + " (" + parent.getFirstName() + ") " + lastName;
	}
}
