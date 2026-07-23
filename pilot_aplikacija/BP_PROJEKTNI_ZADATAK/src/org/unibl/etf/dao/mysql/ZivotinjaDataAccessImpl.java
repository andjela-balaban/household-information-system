package org.unibl.etf.dao.mysql;

import java.sql.*;
import java.util.*;
import org.unibl.etf.dao.*;
import org.unibl.etf.entity.*;

public class ZivotinjaDataAccessImpl implements ZivotinjaDataAccess {

	@Override
	public Zivotinja zivotinja(int idZivotinje) {
		Zivotinja retVal = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT z.IdZivotinje, z.Ime, z.IdTipaZivotinje, tz.NazivTipaZivotinje, z.JIB  " +
        				"FROM zivotinja z " + 
        				"JOIN tip_zivotinje tz ON tz.IdTipaZivotinje = z.IdTipaZivotinje " +
        				"JOIN domacinstvo d ON d.JIB = z.JIB " +
        				"WHERE z.IdZivotinje = ? ";
        
        try {
            conn = ConnectionPool.getInstance().checkOut();
            ps = conn.prepareStatement(query);
            ps.setInt(1, idZivotinje);
            rs = ps.executeQuery();

            if (rs.next()) {
            	
                String jib = rs.getString("JIB");
                DomacinstvoDataAccess domacinstvoDAO = new DomacinstvoDataAccessImpl();
                Domacinstvo domacinstvo = domacinstvoDAO.domacinstvo(jib); 
                retVal = new Zivotinja(
                		idZivotinje,
                		rs.getString("Ime"),
                		TipZivotinje.fromId(rs.getInt("IdTipaZivotinje")),
                		domacinstvo
                		);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionPool.getInstance().checkIn(conn);
            MySQLUtilities.getInstance().close(ps, rs);
        }

        return retVal;
	}

	@Override
	public List<Zivotinja> sveZivotinje() {
		List<Zivotinja> retVal = new ArrayList<>();
        String query = "SELECT z.IdZivotinje, z.Ime, z.IdTipaZivotinje, tz.NazivTipaZivotinje, z.JIB " +
						"FROM zivotinja z " + 
						"JOIN tip_zivotinje tz ON tz.IdTipaZivotinje = z.IdTipaZivotinje " +
						"JOIN domacinstvo d ON d.JIB = z.JIB";

        
        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

        	while (rs.next()) {

                String jib = rs.getString("JIB");
                DomacinstvoDataAccess domacinstvoDAO = new DomacinstvoDataAccessImpl();
                Domacinstvo domacinstvo = domacinstvoDAO.domacinstvo(jib); 
                Zivotinja zivotinja = new Zivotinja(
                		rs.getInt("IdZivotinje"),
                		rs.getString("Ime"),
                		TipZivotinje.fromId(rs.getInt("IdTipaZivotinje")),
                		domacinstvo
                		);

                retVal.add(zivotinja);
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return retVal;
	}

	@Override
	public boolean dodajZivotinju(Zivotinja zivotinja) {
		String query = "INSERT INTO zivotinja (Ime, IdTipaZivotinje, JIB) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, zivotinja.getIme());
            ps.setInt(2, zivotinja.getTip().getId());
            ps.setString(3, zivotinja.getDomacinstvo().getJib());
            
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
	}

	@Override
	public boolean azurirajZivotinju(Zivotinja zivotinja) {
		String query = "UPDATE zivotinja SET Ime = ?, IdTipaZivotinje = ?, JIB = ? WHERE IdZivotinje = ?";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

        	ps.setString(1, zivotinja.getIme());
            ps.setInt(2, zivotinja.getTip().getId());
            ps.setString(3, zivotinja.getDomacinstvo().getJib());

            ps.setInt(4, zivotinja.getIdZivotinje());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
	}

	@Override
	public boolean obrisiZivotinju(int idZivotinje) {
		String query = "DELETE FROM zivotinja WHERE IdZivotinje = ?";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idZivotinje);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return false;
	}

	@Override
	public double kolicinaMlijeka(int idZivotinje) {
	    double ukupno = 0;
	    String query = "SELECT UkupnaKolicinaMlijeka " +
	    				"FROM kolicina_mlijeka_po_zivotinji " +
	    				"WHERE IdZivotinje = ? ";

	    try (Connection conn = ConnectionPool.getInstance().checkOut();
	         PreparedStatement ps = conn.prepareStatement(query)) {

	        ps.setInt(1, idZivotinje);

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                ukupno = rs.getDouble("UkupnaKolicinaMlijeka");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return ukupno;
	}
}
