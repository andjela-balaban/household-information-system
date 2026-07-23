package org.unibl.etf.dao.mysql;

import java.sql.*;
import java.util.*;
import org.unibl.etf.dao.*;
import org.unibl.etf.entity.*;

public class OsobaDataAccessImpl implements OsobaDataAccess {

    @Override
    public Osoba osoba(int idOsobe) {
        Osoba retVal = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT o.IdOsobe, o.Ime, o.Prezime, a.IdAdrese, a.KucniBroj, " +
                       "u.NazivUlice, m.PostanskiBroj, m.NazivMjesta " +
                       "FROM osoba o " +
                       "LEFT JOIN adresa a ON o.IdAdrese = a.IdAdrese " +
                       "LEFT JOIN ulica u ON a.NazivUlice = u.NazivUlice AND a.PostanskiBroj = u.PostanskiBroj " +
                       "LEFT JOIN mjesto m ON u.PostanskiBroj = m.PostanskiBroj " +
                       "WHERE o.IdOsobe = ?";

        try {
            conn = ConnectionPool.getInstance().checkOut();
            ps = conn.prepareStatement(query);
            ps.setInt(1, idOsobe);
            rs = ps.executeQuery();

            if (rs.next()) {
                Adresa adresa = null;

                if (rs.getObject("IdAdrese") != null) {
                    Mjesto mjesto = new Mjesto(rs.getInt("PostanskiBroj"), rs.getString("NazivMjesta"));
                    Ulica ulica = new Ulica(rs.getString("NazivUlice"), mjesto);
                    adresa = new Adresa(rs.getInt("IdAdrese"), ulica, rs.getInt("KucniBroj"));
                }

                retVal = new Osoba(
                    idOsobe,
                    rs.getString("Ime"),
                    rs.getString("Prezime"),
                    adresa
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
    public List<Osoba> osobe() {
        List<Osoba> retVal = new ArrayList<>();
        String query = "SELECT o.IdOsobe, o.Ime, o.Prezime, a.IdAdrese, a.KucniBroj, " +
                       "u.NazivUlice, m.PostanskiBroj, m.NazivMjesta " +
                       "FROM osoba o " +
                       "LEFT JOIN adresa a ON o.IdAdrese = a.IdAdrese " +
                       "LEFT JOIN ulica u ON a.NazivUlice = u.NazivUlice AND a.PostanskiBroj = u.PostanskiBroj " +
                       "LEFT JOIN mjesto m ON u.PostanskiBroj = m.PostanskiBroj";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

        	while (rs.next()) {
                Adresa adresa = null;

                if (rs.getObject("IdAdrese") != null) {
                    Mjesto mjesto = new Mjesto(rs.getInt("PostanskiBroj"), rs.getString("NazivMjesta"));
                    Ulica ulica = new Ulica(rs.getString("NazivUlice"), mjesto);
                    adresa = new Adresa(rs.getInt("IdAdrese"), ulica, rs.getInt("KucniBroj"));
                }

                Osoba osoba = new Osoba(
                    rs.getInt("IdOsobe"),
                    rs.getString("Ime"),
                    rs.getString("Prezime"),
                    adresa
                );

                retVal.add(osoba);
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return retVal;
    }

    @Override
    public boolean dodajOsobu(Osoba osoba) {
        String query = "INSERT INTO osoba (Ime, Prezime, IdAdrese) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, osoba.getIme());
            ps.setString(2, osoba.getPrezime());

            if (osoba.getAdresa() != null)
                ps.setInt(3, osoba.getAdresa().getIdAdrese());
            else
                ps.setNull(3, java.sql.Types.INTEGER); 

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean azurirajOsobu(Osoba osoba) {
        String query = "UPDATE osoba SET Ime = ?, Prezime = ?, IdAdrese = ? WHERE IdOsobe = ?";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, osoba.getIme());
            ps.setString(2, osoba.getPrezime());

            if (osoba.getAdresa() != null)
                ps.setInt(3, osoba.getAdresa().getIdAdrese());
            else
                ps.setNull(3, Types.INTEGER);

            ps.setInt(4, osoba.getIdOsobe());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean obrisiOsobu(int idOsobe) {
        String query = "DELETE FROM osoba WHERE IdOsobe = ?";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idOsobe);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return false;
    }
}
