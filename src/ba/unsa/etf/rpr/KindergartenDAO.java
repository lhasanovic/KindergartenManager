package ba.unsa.etf.rpr;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class KindergartenDAO {
	private static KindergartenDAO instance;
	private Connection conn;

	private String adminPassword = "11111111";
	private int maximumCapacity = 100;
	private int maximumClassSize = 10;

	private PreparedStatement insertChildStatement, getChildStatement, findChildStatement, deleteChildStatement, editChildStatement, getChildrenStatement, getNextChildIdStatement;
	private PreparedStatement insertTeacherStatement, getTeacherStatement, findTeacherStatement, deleteTeacherStatement, editTeacherStatement, getTeachersStatement, getNextTeacherIdStatement;
	private PreparedStatement getTeacherClassStatement;

	public static KindergartenDAO getInstance() {
		if (instance == null) instance = new KindergartenDAO();
		return instance;
	}

	private KindergartenDAO() {
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:database.db");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			insertChildStatement = conn.prepareStatement("INSERT INTO children VALUES (?,?,?,?,?,?,?,?)");
		} catch (SQLException e) {
			regenerateBase();
			try {
				insertChildStatement = conn.prepareStatement("INSERT INTO children VALUES (?,?,?,?,?,?,?,?)");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

		try {
			getChildStatement = conn.prepareStatement("SELECT * FROM children WHERE id=?");
			findChildStatement = conn.prepareStatement("SELECT * FROM children WHERE first_name=? AND last_name=? AND parent_name=?");
			deleteChildStatement = conn.prepareStatement("DELETE FROM children WHERE id=?");
			editChildStatement = conn.prepareStatement("UPDATE children SET first_name=?, last_name=?, birth_date=?, parent_name=?, phone_number=?, special_need=?, teacher=? WHERE id=?");
			getChildrenStatement = conn.prepareStatement("SELECT * FROM children ORDER BY last_name");
			getNextChildIdStatement = conn.prepareStatement("SELECT MAX(id)+1 FROM children");

			insertTeacherStatement = conn.prepareStatement("INSERT INTO teachers VALUES (?,?,?,?,?)");
			getTeacherStatement = conn.prepareStatement("SELECT * FROM teachers WHERE id=?");
			findTeacherStatement = conn.prepareStatement("SELECT * FROM teachers WHERE first_name=? AND last_name=? AND phone_number=?");
			deleteTeacherStatement = conn.prepareStatement("DELETE FROM teachers WHERE id=?");
			editTeacherStatement = conn.prepareStatement("UPDATE teachers SET first_name=?, last_name=?, phone_number=?, special_needs=? WHERE id=?");
			getTeachersStatement = conn.prepareStatement("SELECT * FROM teachers ORDER BY last_name");
			getNextTeacherIdStatement = conn.prepareStatement("SELECT MAX(id)+1 FROM teachers");

			getTeacherClassStatement = conn.prepareStatement("SELECT * FROM children WHERE teacher=?");
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public Child getChild(int id) {
		try {
			getChildStatement.setInt(1, id);
			ResultSet rs = getChildStatement.executeQuery();
			if (!rs.next()) return null;
			return getChildFromResultSet(rs);
		} catch (SQLException | InvalidChildBirthDateException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Child findChild(Child child) {
		try {
			findChildStatement.setString(1, child.getFirstName());
			findChildStatement.setString(2, child.getLastName());
			findChildStatement.setString(3, child.getParent().getFirstName());
			ResultSet rs = findChildStatement.executeQuery();
			if(!rs.next()) return null;
			return getChildFromResultSet(rs);
		} catch (SQLException | InvalidChildBirthDateException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void insertChild(Child child) {
		try {
			int id = getNextChildId();

			String dateOfBirth = child.getDateOfBirth().getDayOfMonth() + "." + child.getDateOfBirth().getMonthValue() + "." + child.getDateOfBirth().getYear();
			insertChildStatement.setInt(1, id);
			insertChildStatement.setString(2, child.getFirstName());
			insertChildStatement.setString(3, child.getLastName());
			insertChildStatement.setString(4, dateOfBirth);
			insertChildStatement.setString(5, child.getParent().getFirstName());
			insertChildStatement.setString(6, child.getParent().getPhoneNumber());

			if(child instanceof SpecialNeedsChild)
				insertChildStatement.setString(7, ((SpecialNeedsChild) child).getSpecialNeedDescription());
			else
				insertChildStatement.setString(7, "None");

			insertChildStatement.setInt(8, child.getTeacher().getId());

			insertChildStatement.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteChild(Child child) {
		try {
			deleteChildStatement.setInt(1, child.getId());
			deleteChildStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void editChild(Child child) {
		try {
			String dateOfBirth = child.getDateOfBirth().getDayOfMonth() + "." + child.getDateOfBirth().getMonthValue() + "." + child.getDateOfBirth().getYear();

			editChildStatement.setString(1, child.getFirstName());
			editChildStatement.setString(2, child.getLastName());
			editChildStatement.setString(3, dateOfBirth);
			editChildStatement.setString(4, child.getParent().getFirstName());
			editChildStatement.setString(5, child.getParent().getPhoneNumber());

			if(child instanceof SpecialNeedsChild)
				editChildStatement.setString(6, ((SpecialNeedsChild) child).getSpecialNeedDescription());
			else
				editChildStatement.setString(6, "None");

			editChildStatement.setInt(7, child.getId());

			editChildStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Child> getChildren() {
		ArrayList<Child> children = new ArrayList<>();
		try {
			ResultSet rs = getChildrenStatement.executeQuery();
			while (rs.next()) {
				Child child = getChildFromResultSet(rs);
				children.add(child);
			}
		} catch (SQLException | InvalidChildBirthDateException e) {
			e.printStackTrace();
		}
		return children;
	}

	public int getNextChildId() {
		try {
			ResultSet rs = getNextChildIdStatement.executeQuery();
			int id = 1;
			if (rs.next()) {
				id = rs.getInt(1);
			}
			return id;
		} catch(SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public KindergartenTeacher getTeacher(int id) {
		try {
			getTeacherStatement.setInt(1, id);
			ResultSet rs = getTeacherStatement.executeQuery();
			if (!rs.next()) return null;
			return getTeacherFromResultSet(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public KindergartenTeacher findTeacher(KindergartenTeacher teacher) {
		try {
			findTeacherStatement.setString(1, teacher.getFirstName());
			findTeacherStatement.setString(2, teacher.getLastName());
			findTeacherStatement.setString(3, teacher.getPhoneNumber());

			ResultSet rs = findTeacherStatement.executeQuery();
			if(!rs.next()) return null;

			return getTeacherFromResultSet(rs);
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void insertTeacher(KindergartenTeacher teacher) {
		try {
			int id = getNextChildId();

			insertTeacherStatement.setInt(1, id);
			insertTeacherStatement.setString(2, teacher.getFirstName());
			insertTeacherStatement.setString(3, teacher.getLastName());
			insertTeacherStatement.setString(4, teacher.getPhoneNumber());

			if(teacher instanceof SpecialNeedsKindergartenTeacher)
				insertTeacherStatement.setString(5, "Yes");
			else
				insertTeacherStatement.setString(5, "No");

			insertTeacherStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteTeacher(KindergartenTeacher teacher) {
		try {
			deleteTeacherStatement.setInt(1, teacher.getId());
			deleteTeacherStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void editTeacher(KindergartenTeacher teacher) {
		try {
			editTeacherStatement.setString(1, teacher.getFirstName());
			editTeacherStatement.setString(2, teacher.getLastName());
			editTeacherStatement.setString(3, teacher.getPhoneNumber());

			if(teacher instanceof SpecialNeedsKindergartenTeacher)
				editTeacherStatement.setString(4, "Yes");
			else
				editTeacherStatement.setString(4, "No");

			editTeacherStatement.setInt(5, teacher.getId());

			editTeacherStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<KindergartenTeacher> getTeachers() {
		ArrayList<KindergartenTeacher> teachers = new ArrayList<>();
		try {
			ResultSet rs = getTeachersStatement.executeQuery();
			while (rs.next()) {
				KindergartenTeacher teacher = getTeacherFromResultSet(rs);
				teachers.add(teacher);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return teachers;
	}

	public int getNextTeacherId() {
		try {
			ResultSet rs = getNextTeacherIdStatement.executeQuery();
			int id = 1;
			if (rs.next()) {
				id = rs.getInt(1);
			}
			return id;
		} catch(SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public ArrayList<Child> getTeacherClass(int teacherID) throws InvalidTeacherDataException {
		KindergartenTeacher teacher = getTeacher(teacherID);
		if(teacher == null)
			throw new InvalidTeacherDataException("Teacher with this ID not found!");

		ArrayList<Child> teacherClass = new ArrayList<>();
		try {
			getTeacherClassStatement.setInt(1, teacherID);
			ResultSet rs = getTeacherClassStatement.executeQuery();
			while (rs.next()) {
				Child child = getChildFromResultSet(rs);
				teacherClass.add(child);
			}
		} catch (SQLException | InvalidChildBirthDateException e) {
			e.printStackTrace();
		}
		return teacherClass;
	}

	public List<KindergartenTeacher> getAvailableTeachers() {
		ArrayList<KindergartenTeacher> teachers = getTeachers();

		List<KindergartenTeacher> filteredTeachers = teachers.stream().filter(t -> {
			try {
				return getTeacherClass(t.getId()).size() < maximumClassSize;
			} catch (InvalidTeacherDataException e) {
				e.printStackTrace();
				return false;
			}
		}).sorted(Comparator.comparingInt(t -> {
			try {
				return getTeacherClass(t.getId()).size();
			} catch (InvalidTeacherDataException e) {
				return 0;
			}
		})).collect(Collectors.toList());

		return filteredTeachers;
	}

	private Child getChildFromResultSet(ResultSet rs) throws SQLException, InvalidChildBirthDateException {
		String dateOfBirth = rs.getString(4);
		String[] dateParts = dateOfBirth.split("\\.");
		int day = Integer.parseInt(dateParts[0]);
		int month = Integer.parseInt(dateParts[1]);
		int year = Integer.parseInt(dateParts[2]);

		if(rs.getString(7).equals("None"))
			return new Child(rs.getInt(1), rs.getString(2), rs.getString(3), day, month, year, rs.getString(5), rs.getString(6), getTeacher(rs.getInt(7)));
		else
			return new SpecialNeedsChild(rs.getInt(1), rs.getString(2), rs.getString(3), day, month, year, rs.getString(5), rs.getString(6), getTeacher(rs.getInt(7)), rs.getString(8));
	}

	private KindergartenTeacher getTeacherFromResultSet(ResultSet rs) throws SQLException {
		if(rs.getString(5).equals("Yes"))
			return new KindergartenTeacher(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
		else
			return new SpecialNeedsKindergartenTeacher(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
	}

	public static void removeInstance() {
		if (instance == null) return;
		instance.close();
		instance = null;
	}

	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void regenerateBase() {
		Scanner input = null;
		try {
			input = new Scanner(new FileInputStream("database.db.sql"));
			String sqlStatement = "";
			while (input.hasNext()) {
				sqlStatement += input.nextLine();
				if ( sqlStatement.charAt( sqlStatement.length()-1 ) == ';') {
					try {
						Statement stmt = conn.createStatement();
						stmt.execute(sqlStatement);
						sqlStatement = "";
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			input.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void setAdminPassword(String newPassword) {
		this.adminPassword = newPassword;
	}

	public String getAdminPassword() {
		return adminPassword;
	}
}
