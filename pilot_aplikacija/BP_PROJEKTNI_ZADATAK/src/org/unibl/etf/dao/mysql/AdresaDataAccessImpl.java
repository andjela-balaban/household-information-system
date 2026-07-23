package org.unibl.etf.dao.mysql;

import java.sql.*;
import java.util.*;
import org.unibl.etf.dao.*;
import org.unibl.etf.entity.*;

public class AdresaDataAccessImpl implements AdresaDataAccess {

    @Override
    public Adresa adresa(int idAdrese) {
        Adresa retVal = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT a.IdAdrese, a.KucniBroj, u.NazivUlice, m.PostanskiBroj, m.NazivMjesta " +
                       "FROM adresa a " +
                       "JOIN ulica u ON a.NazivUlice = u.NazivUlice AND a.PostanskiBroj = u.PostanskiBroj " +
                       "JOIN mjesto m ON u.PostanskiBroj = m.PostanskiBroj " +
                       "WHERE a.IdAdrese = ?";

        try {
            conn = ConnectionPool.getInstance().checkOut();
            ps = conn.prepareStatement(query);
            ps.setInt(1, idAdrese);
            rs = ps.executeQuery();

            if (rs.next()) {
                Mjesto mjesto = new Mjesto(rs.getInt("PostanskiBroj"), rs.getString("NazivMjesta"));
                Ulica ulica = new Ulica(rs.getString("NazivUlice"), mjesto);
                retVal = new Adresa(rs.getInt("IdAdrese"), ulica, rs.getInt("KucniBroj"));
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
    public List<Adresa> adrese() {
        List<Adresa> retVal = new ArrayList<>();
        String query = "SELECT a.IdAdrese, a.KucniBroj, u.NazivUlice, m.PostanskiBroj, m.NazivMjesta " +
                       "FROM adresa a " +
                       "JOIN ulica u ON a.NazivUlice = u.NazivUlice AND a.PostanskiBroj = u.PostanskiBroj " +
                       "JOIN mjesto m ON u.PostanskiBroj = m.PostanskiBroj";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Mjesto mjesto = new Mjesto(rs.getInt("PostanskiBroj"), rs.getString("NazivMjesta"));
                Ulica ulica = new Ulica(rs.getString("NazivUlice"), mjesto);
                Adresa adresa = new Adresa(rs.getInt("IdAdrese"), ulica, rs.getInt("KucniBroj"));
                retVal.add(adresa);
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return retVal;
    }

    @Override
    public boolean dodajAdresu(Adresa adresa) {
        String query = "INSERT INTO adresa (NazivUlice, PostanskiBroj, KucniBroj) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, adresa.getUlica().getNazivUlice());
            ps.setInt(2, adresa.getUlica().getMjesto().getPostanskiBroj());
            ps.setInt(3, adresa.getKucniBroj());
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean azurirajAdresu(Adresa adresa) {
        String query = "UPDATE adresa SET NazivUlice = ?, PostanskiBroj = ?, KucniBroj = ? WHERE IdAdrese = ?";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, adresa.getUlica().getNazivUlice());
            ps.setInt(2, adresa.getUlica().getMjesto().getPostanskiBroj());
            ps.setInt(3, adresa.getKucniBroj());
            ps.setInt(4, adresa.getIdAdrese());
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean obrisiAdresu(int idAdrese) {
        String query = "DELETE FROM adresa WHERE IdAdrese = ?";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idAdrese);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return false;
    }
}
