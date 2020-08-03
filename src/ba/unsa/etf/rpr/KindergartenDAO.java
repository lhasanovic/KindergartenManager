package ba.unsa.etf.rpr;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class KindergartenDAO {
	private static KindergartenDAO instance;
	private Connection conn;

	public static KindergartenDAO getInstance() {
		if (instance == null) instance = new KindergartenDAO();
		return instance;
	}
}
