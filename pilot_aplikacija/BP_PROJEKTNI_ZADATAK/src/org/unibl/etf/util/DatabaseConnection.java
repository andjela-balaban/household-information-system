package org.unibl.etf.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/inf_sis_atom";
    private static final String USER = "root";
    private static final String PASSWORD = "andjela2";
    
    private DatabaseConnection() { super(); }
	
	@SuppressWarnings("exports")
	static public Connection connection; 
	
	static {
		try {
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
			if (connection != null) {
				System.out.println("Uspjesno uspostavljena konekcija sa bazom podataka!");
            }
		
		} catch (SQLException e) {
			System.out.println("Greska pri povezivanju sa bazom: " + e.getMessage());
		}
	}
	
	public void closeConnection() throws SQLException {
		connection.close();
	}
}