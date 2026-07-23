package org.unibl.etf.dao.mysql;

import java.sql.*;
import java.util.*;
import org.unibl.etf.dao.*;
import org.unibl.etf.entity.*;

public class UlicaDataAccessImpl implements UlicaDataAccess {

    @Override
    public Ulica ulica(int postanskiBroj, String nazivUlice) {
        Ulica retVal = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT u.NazivUlice, m.PostanskiBroj, m.NazivMjesta " +
                       "FROM ulica u JOIN mjesto m ON u.PostanskiBroj = m.PostanskiBroj " +
                       "WHERE u.PostanskiBroj = ? AND u.NazivUlice = ?";

        try {
            conn = ConnectionPool.getInstance().checkOut();
            ps = conn.prepareStatement(query);
            ps.setInt(1, postanskiBroj);
            ps.setString(2, nazivUlice);
            rs = ps.executeQuery();

            if (rs.next()) {
                Mjesto mjesto = new Mjesto(rs.getInt("PostanskiBroj"), rs.getString("NazivMjesta"));
                retVal = new Ulica(rs.getString("NazivUlice"), mjesto);
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
    public List<Ulica> uliceUMjestu(int postanskiBroj) {
        List<Ulica> retVal = new ArrayList<>();
        String query = "SELECT u.NazivUlice, m.PostanskiBroj, m.NazivMjesta " +
                       "FROM ulica u JOIN mjesto m ON u.PostanskiBroj = m.PostanskiBroj " +
                       "WHERE u.PostanskiBroj = ?";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, postanskiBroj);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Mjesto mjesto = new Mjesto(rs.getInt("PostanskiBroj"), rs.getString("NazivMjesta"));
                    retVal.add(new Ulica(rs.getString("NazivUlice"), mjesto));
                }
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return retVal;
    }

    @Override
    public boolean dodajUlicu(Ulica ulica) {
        String query = "INSERT INTO ulica (NazivUlice, PostanskiBroj) VALUES (?, ?)";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, ulica.getNazivUlice());
            ps.setInt(2, ulica.getMjesto().getPostanskiBroj());
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean azurirajUlicu(Ulica ulica) {
        String query = "UPDATE ulica SET PostanskiBroj = ? WHERE NazivUlice = ?";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, ulica.getMjesto().getPostanskiBroj());
            ps.setString(2, ulica.getNazivUlice());
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean obrisiUlicu(int postanskiBroj, String nazivUlice) {
        String query = "DELETE FROM ulica WHERE NazivUlice = ? AND PostanskiBroj = ?";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, nazivUlice);
            ps.setInt(2, postanskiBroj);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return false;
    }
}
