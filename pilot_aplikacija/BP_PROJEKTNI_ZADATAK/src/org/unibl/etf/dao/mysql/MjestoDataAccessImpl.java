package org.unibl.etf.dao.mysql;

import java.sql.*;
import java.util.*;
import org.unibl.etf.dao.*;
import org.unibl.etf.entity.*;

public class MjestoDataAccessImpl implements MjestoDataAccess {
	
	@Override
	public Mjesto mjesto(int postanskiBroj) {
		Mjesto retVal = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT PostanskiBroj, NazivMjesta "
				+ "FROM mjesto "
				+ "WHERE PostanskiBroj= ? ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			ps.setInt(1, postanskiBroj);
			rs = ps.executeQuery();

			if (rs.next())
				retVal = new Mjesto(rs.getInt(1), rs.getString(2));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().checkIn(conn);
			MySQLUtilities.getInstance().close(ps, rs);
		}
		return retVal;
	}

    @Override
    public List<Mjesto> mjesta() {
        List<Mjesto> retVal = new ArrayList<>();
        String query = "SELECT * FROM mjesto";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int pb = rs.getInt("PostanskiBroj");
                String naziv = rs.getString("NazivMjesta");
                retVal.add(new Mjesto(pb, naziv));
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return retVal;
    }

    @Override
    public boolean dodajMjesto(Mjesto mjesto) {
        String query = "INSERT IGNORE INTO mjesto (PostanskiBroj, NazivMjesta) VALUES (?, ?)";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, mjesto.getPostanskiBroj());
            ps.setString(2, mjesto.getNaziv());
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean azurirajMjesto(Mjesto mjesto) {
        String query = "UPDATE mjesto SET NazivMjesta = ? WHERE PostanskiBroj = ?";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, mjesto.getNaziv());
            ps.setInt(2, mjesto.getPostanskiBroj());
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean obrisiMjesto(int postanskiBroj) {
        String query = "DELETE FROM mjesto WHERE PostanskiBroj = ?";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, postanskiBroj);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return false;
    }
}
