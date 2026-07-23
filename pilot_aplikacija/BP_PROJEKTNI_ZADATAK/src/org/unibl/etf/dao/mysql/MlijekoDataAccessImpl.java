package org.unibl.etf.dao.mysql;

import java.sql.*;
import java.util.*;
import org.unibl.etf.dao.*;
import org.unibl.etf.entity.*;

public class MlijekoDataAccessImpl implements MlijekoDataAccess {

	@Override
	public Mlijeko mlijeko(int idMlijeka) {
		Mlijeko retVal = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT m.IdMlijeka, m.DatumMuze, m.Kolicina, m.IdMjere, mj.Jedinica, m.Kvalitet, z.IdZivotinje, z.Ime " +
        			   "FROM mlijeko m " + 
        			   "JOIN zivotinja z ON m.IdZivotinje = z.IdZivotinje " +
        			   "JOIN jedinica_mjere mj ON mj.IdMjere = m.IdMjere " +
        			   "WHERE m.IdMlijeka = ? ";
        
        try {
            conn = ConnectionPool.getInstance().checkOut();
            ps = conn.prepareStatement(query);
            ps.setInt(1, idMlijeka);
            rs = ps.executeQuery();

            if (rs.next()) {
            	
            	int idZivotinje = rs.getInt("IdZivotinje");
            	ZivotinjaDataAccess zivotinjaDAO = new ZivotinjaDataAccessImpl();
            	Zivotinja zivotinja = zivotinjaDAO.zivotinja(idZivotinje);
                retVal = new Mlijeko(
                		idMlijeka,
                		rs.getDate("DatumMuze").toLocalDate(),
                		rs.getDouble("Kolicina"),
                		JedinicaMjere.fromId(rs.getInt("IdMjere")),
                		rs.getInt("Kvalitet"),
                		zivotinja
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
	public List<Mlijeko> mlijeko() {
		List<Mlijeko> retVal = new ArrayList<>();
        String query = "SELECT m.IdMlijeka, m.DatumMuze, m.Kolicina, m.IdMjere, mj.Jedinica, m.Kvalitet, z.IdZivotinje, z.Ime " +
 			   			"FROM mlijeko m " + 
 			   			"JOIN zivotinja z ON m.IdZivotinje = z.IdZivotinje " +
 			   			"JOIN jedinica_mjere mj ON mj.IdMjere = m.IdMjere";

        
        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

        	while (rs.next()) {

            	int idZivotinje = rs.getInt("IdZivotinje");
            	ZivotinjaDataAccess zivotinjaDAO = new ZivotinjaDataAccessImpl();
            	Zivotinja zivotinja = zivotinjaDAO.zivotinja(idZivotinje);
                Mlijeko mlijeko = new Mlijeko(
                		rs.getInt("IdMlijeka"),
                		rs.getDate("DatumMuze").toLocalDate(),
                		rs.getDouble("Kolicina"),
                		JedinicaMjere.fromId(rs.getInt("IdMjere")),
                		rs.getInt("Kvalitet"),
                		zivotinja
                		);

                retVal.add(mlijeko);
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return retVal;
	}

	@Override
	public boolean dodajMlijeko(Mlijeko mlijeko) {
		String query = "INSERT INTO mlijeko (DatumMuze, Kolicina, IdMjere, Kvalitet, IdZivotinje) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setDate(1, java.sql.Date.valueOf(mlijeko.getDatumMuze())); 
            ps.setDouble(2, mlijeko.getKolicina());
            ps.setInt(3, mlijeko.getMjera().getId());
            ps.setInt(4, mlijeko.getKvalitet());
            ps.setInt(5, mlijeko.getZivotinja().getIdZivotinje());
            
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
	}

	@Override
	public boolean azurirajMlijeko(Mlijeko mlijeko) {
		String query = "UPDATE mlijeko SET DatumMuze = ?, Kolicina = ?, IdMjere = ?, Kvalitet = ?, IdZivotinje = ? WHERE IdMlijeka = ?";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

        	ps.setDate(1, java.sql.Date.valueOf(mlijeko.getDatumMuze())); 
            ps.setDouble(2, mlijeko.getKolicina());
            ps.setInt(3, mlijeko.getMjera().getId());
            ps.setInt(4, mlijeko.getKvalitet());
            ps.setInt(5, mlijeko.getZivotinja().getIdZivotinje());
            
            ps.setInt(6, mlijeko.getIdMlijeka());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
	}

	@Override
	public boolean obrisiMlijeko(int idMlijeka) {
		String query = "DELETE FROM mlijeko WHERE IdMlijeka = ?";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idMlijeka);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return false;
	}

}
