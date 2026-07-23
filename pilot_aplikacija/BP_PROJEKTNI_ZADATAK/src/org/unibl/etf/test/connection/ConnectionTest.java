package org.unibl.etf.test.connection;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.unibl.etf.util.*;
import java.sql.*;

public class ConnectionTest {

    @Test
    public void testConnection() throws SQLException {
    	
    	String actualMessage = null;
    	Connection conn = DatabaseConnection.connection;
    	
        String sql = "select * from osoba";  // SQL upit
        Statement stmt = conn.createStatement();	// Kreiranje Statement objekta
        ResultSet rs = stmt.executeQuery(sql);	// Izvrsavanje upita

        // Prikaz rezultata
        while (rs.next()) {
            int id = rs.getInt("IdOsobe");
            String ime = rs.getString("Ime");
            System.out.println(id + " | " + ime);
        }

        rs.close();
        stmt.close();
    	
    	try {
    		conn.close();
    		if(conn.isClosed()) {
    			 actualMessage = "Konekcija je uspijesno zatvorena!";
    		}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	String expectedMessage = "Konekcija je uspijesno zatvorena!";
        
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
