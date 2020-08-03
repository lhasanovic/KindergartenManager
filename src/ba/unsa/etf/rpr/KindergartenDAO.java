package ba.unsa.etf.rpr;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class KindergartenDAO {
	private static KindergartenDAO instance;
	private Connection conn;

	private PreparedStatement insertChildStatement, getChildStatement, findChildStatement, deleteChildStatement, editChildStatement, getChildrenStatement, getNextChildIdStatement;

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
			insertChildStatement = conn.prepareStatement("INSERT INTO children VALUES (?,?,?,?,?,?,?)");
		} catch (SQLException e) {
			regenerateBase();
			try {
				insertChildStatement = conn.prepareStatement("INSERT INTO children VALUES (?,?,?,?,?,?,?)");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

		try {
			getChildStatement = conn.prepareStatement("SELECT * FROM children WHERE id=?");
			findChildStatement = conn.prepareStatement("SELECT * FROM children WHERE first_name=? AND last_name=?");
			deleteChildStatement = conn.prepareStatement("DELETE FROM children WHERE id=?");
			editChildStatement = conn.prepareStatement("UPDATE children SET first_name=?, last_name=?, birth_date=?, parent_name=?, phone_number=?, special_need=? WHERE id=?");
			getChildrenStatement = conn.prepareStatement("SELECT * FROM children ORDER BY last_name");
			getNextChildIdStatement = conn.prepareStatement("SELECT MAX(id)+1 FROM children");
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

	public Child findChild(String firstName, String lastName) {
		try {
			findChildStatement.setString(1, firstName);
			findChildStatement.setString(2, lastName);
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
		ArrayList<Child> children = new ArrayList();
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

	private Child getChildFromResultSet(ResultSet rs) throws SQLException, InvalidChildBirthDateException {
		String dateOfBirth = rs.getString(4);
		String[] dateParts = dateOfBirth.split("\\.");
		int day = Integer.parseInt(dateParts[0]);
		int month = Integer.parseInt(dateParts[1]);
		int year = Integer.parseInt(dateParts[2]);

		if(rs.getString(7).equals("None"))
			return new Child(rs.getInt(1), rs.getString(2), rs.getString(3), day, month, year, rs.getString(5), rs.getString(6));
		else
			return new SpecialNeedsChild(rs.getInt(1), rs.getString(2), rs.getString(3), day, month, year, rs.getString(5), rs.getString(6), rs.getString(7));
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
}
