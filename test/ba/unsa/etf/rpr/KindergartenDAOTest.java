package ba.unsa.etf.rpr;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class KindergartenDAOTest {
	@Test
	void regenerateFile() {
		KindergartenDAO.removeInstance();
		File dbfile = new File("database.db");
		dbfile.delete();
		KindergartenDAO dao = KindergartenDAO.getInstance();

		ArrayList<Child> children = dao.getChildren();
		assertEquals("Ana", children.get(0).getFirstName());
		ArrayList<KindergartenTeacher> teachers = dao.getTeachers();
		assertEquals("Adnan", teachers.get(0).getFirstName());
		assertEquals("Adnan", children.get(0).getTeacher().getFirstName());
	}

	@Test
	void deleteTeacher() {
		KindergartenDAO.removeInstance();
		File dbfile = new File("database.db");
		dbfile.delete();
		KindergartenDAO dao = KindergartenDAO.getInstance();

		ArrayList<KindergartenTeacher> teachers = dao.getTeachers();
		KindergartenTeacher teacher = teachers.get(0);
		assertEquals("Adnan", teacher.getFirstName());
		dao.deleteTeacher(teacher);
		teachers = dao.getTeachers();
		assertEquals("Luka", teachers.get(0).getFirstName());
		assertEquals(1, teachers.size());
	}

	@Test
	void deleteTeacher2() {
		KindergartenDAO.removeInstance();
		File dbfile = new File("database.db");
		dbfile.delete();
		KindergartenDAO dao = KindergartenDAO.getInstance();

		ArrayList<KindergartenTeacher> teachers = dao.getTeachers();
		KindergartenTeacher teacher = teachers.get(0);
		assertEquals("Adnan", teacher.getFirstName());
		dao.deleteTeacher(teacher);

		ArrayList<Child> children = dao.getChildren();
		for(Child c : children)
			assertEquals("Luka", c.getTeacher().getFirstName());
	}

	@Test
	void deleteChild() {
		KindergartenDAO.removeInstance();
		File dbfile = new File("database.db");
		dbfile.delete();
		KindergartenDAO dao = KindergartenDAO.getInstance();

		ArrayList<Child> children = dao.getChildren();
		int originalSize = children.size();

		dao.deleteChild(children.get(0));

		assertEquals(originalSize - 1, dao.getChildren().size());
	}

	@Test
	void editTeacher() {
		KindergartenDAO.removeInstance();
		File dbfile = new File("database.db");
		dbfile.delete();
		KindergartenDAO dao = KindergartenDAO.getInstance();

		KindergartenTeacher teacher = dao.getTeacher(10004);
		String oldPhoneNumber = teacher.getPhoneNumber();
		String newPhoneNumber = "000111222";
		teacher.setPhoneNumber(newPhoneNumber);

		dao.editTeacher(teacher);
		assertEquals(newPhoneNumber, dao.getTeacher(10004).getPhoneNumber());
	}

	@Test
	void editChild() {
		KindergartenDAO.removeInstance();
		File dbfile = new File("database.db");
		dbfile.delete();
		KindergartenDAO dao = KindergartenDAO.getInstance();

		Child child = dao.getChild(10001);

		assertThrows(InvalidChildBirthDateException.class, () -> child.setDateOfBirth(2020, 8, 22));
		try {
			child.setDateOfBirth(2017, 2, 2);
		} catch (InvalidChildBirthDateException e) {
			e.printStackTrace();
		}

		dao.editChild(child);
		assertEquals("2.2.2017", dao.getChild(10001).getDateOfBirthString());
	}

	@Test
	void insertTeacherChild() throws InvalidChildBirthDateException {
		KindergartenDAO.removeInstance();
		File dbfile = new File("database.db");
		dbfile.delete();
		KindergartenDAO dao = KindergartenDAO.getInstance();

		Child child = new Child();

		child.setFirstName("Test");
		child.setLastName("Testic");
		child.setDateOfBirth(2017, 2, 2);
		Parent parent = new Parent();
		parent.setFirstName("Testov");
		parent.setLastName(child.getLastName());
		parent.setPhoneNumber("123456789");
		KindergartenTeacher teacher = new KindergartenTeacher();
		teacher.setFirstName("Teacher");
		teacher.setLastName("Test");
		teacher.setPhoneNumber("000111222");
		child.setParent(parent);
		dao.insertTeacher(teacher);
		child.setTeacher(dao.findTeacher(teacher));
		dao.insertChild(child);

		Child newChild = dao.findChild(child);
		KindergartenTeacher newTeacher = dao.findTeacher(teacher);

		assertEquals(newChild.toString(), child.toString());
		assertEquals(newChild.getTeacher().getFirstName(), teacher.getFirstName());
		assertEquals(newTeacher.getPhoneNumber(), teacher.getPhoneNumber());
	}
}