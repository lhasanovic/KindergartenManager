package ba.unsa.etf.rpr;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
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
