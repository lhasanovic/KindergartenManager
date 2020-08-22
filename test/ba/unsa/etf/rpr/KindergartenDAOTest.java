package ba.unsa.etf.rpr;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class KindergartenDAOTest {
	@Test
	void regenerateFile() {
		KindergartenDAO.removeInstance();
		File dbfile = new File("baza.db");
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
		File dbfile = new File("baza.db");
		dbfile.delete();
		KindergartenDAO dao = KindergartenDAO.getInstance();

		ArrayList<KindergartenTeacher> teachers = dao.getTeachers();
		KindergartenTeacher teacher = teachers.get(0);
		assertEquals("Adnan", teacher.getFirstName());
		dao.deleteTeacher(teacher);
		teachers = dao.getTeachers();
		assertEquals("Luka", teachers.get(0).getFirstName());
	}


}