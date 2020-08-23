package ba.unsa.etf.rpr;

import org.junit.jupiter.api.Test;

import java.io.File;
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
	void xmlInfo() {
		KindergartenDAO.removeInstance();
		File dbfile = new File("database.db");
		dbfile.delete();
		KindergartenDAO dao = KindergartenDAO.getInstance();

		assertEquals("Lamija's Kindergarten", dao.getName());
		assertEquals("11112222", dao.getAdminPassword());
		assertEquals(50, dao.getTotalCapacity());
		assertEquals(10, dao.getMaximumClassSize());

		dao.setAdminPassword("22221111");
		KindergartenDAO.removeInstance();
		dbfile = new File("database.db");
		dbfile.delete();
		dao = KindergartenDAO.getInstance();

		assertEquals("22221111", dao.getAdminPassword());

		dao.setAdminPassword("11112222");
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
		KindergartenTeacher teacher = teachers.get(1);
		assertEquals("Luka", teacher.getFirstName());
		dao.deleteTeacher(teacher);

		ArrayList<Child> children = dao.getChildren();
		for(Child c : children)
			assertEquals("Adnan", c.getTeacher().getFirstName());
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

	@Test
	void diary() {
		KindergartenDAO.removeInstance();
		File dbfile = new File("database.db");
		dbfile.delete();
		KindergartenDAO dao = KindergartenDAO.getInstance();

		ChildDiary diary = dao.getDiaryForChild(dao.getChild(10001));
		assertEquals(2, diary.getDiary().size());
	}

	@Test
	void diary2() {
		KindergartenDAO.removeInstance();
		File dbfile = new File("database.db");
		dbfile.delete();
		KindergartenDAO dao = KindergartenDAO.getInstance();

		DiaryEntry diaryEntry = new DiaryEntry();
		diaryEntry.setTimeDate(LocalDateTime.now());
		diaryEntry.setActivity(ChildActivity.Crying);
		diaryEntry.setDescription("Test");

		Child child = dao.getChild(10002);
		dao.insertDiaryEntry(child, diaryEntry);

		ChildDiary diary = dao.getDiaryForChild(child);
		assertEquals("Test", diary.getDiary().get(0).getDescription());

		dao.deleteDiaryForChild(child);
		assertEquals(0, dao.getDiaryForChild(child).getDiary().size());
	}
}