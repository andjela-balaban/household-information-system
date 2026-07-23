package org.unibl.etf.dao.mysql;

import java.sql.*;
import java.util.*;
import org.unibl.etf.dao.*;
import org.unibl.etf.entity.*;

public class TelefonDDataAccessImpl implements TelefonDDataAccess {

    @Override
    public TelefonD telefon(String brojTelefona, String jib) {
        TelefonD retVal = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT d.JIB, d.Naziv, d.IdAdrese, a.KucniBroj, " +
                		"u.NazivUlice, m.PostanskiBroj, m.NazivMjesta, " +
                		"ko.IdOsobe AS KontaktId, ko.Ime AS KontaktIme, ko.Prezime AS KontaktPrezime, " +
                		"td.BrojTelefona " +
                		"FROM telefon_d td " +
                		"JOIN domacinstvo d ON td.JIB = d.JIB " +
                		"JOIN adresa a ON d.IdAdrese = a.IdAdrese " +
                		"JOIN ulica u ON a.NazivUlice = u.NazivUlice AND a.PostanskiBroj = u.PostanskiBroj " +
                		"JOIN mjesto m ON u.PostanskiBroj = m.PostanskiBroj " +
                		"LEFT JOIN osoba ko ON d.IdKontaktOsobe = ko.IdOsobe " +
                		"WHERE td.BrojTelefona = ? AND td.JIB = ?";

        try {
            conn = ConnectionPool.getInstance().checkOut();
            ps = conn.prepareStatement(query);
            ps.setString(1, brojTelefona);
            ps.setString(2, jib);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                Mjesto mjesto = new Mjesto(rs.getInt("PostanskiBroj"), rs.getString("NazivMjesta"));
                Ulica ulica = new Ulica(rs.getString("NazivUlice"), mjesto);
                Adresa adresa = new Adresa(rs.getInt("IdAdrese"), ulica, rs.getInt("KucniBroj"));
                
                Osoba kontaktOsoba = null;
                int kontaktId = rs.getInt("KontaktId");
                if (!rs.wasNull()) {
                	kontaktOsoba = new Osoba(kontaktId, rs.getString("KontaktIme"), rs.getString("KontaktPrezime"));
                }
               
                Domacinstvo domacinstvo = new Domacinstvo(rs.getString("JIB"), rs.getString("Naziv"), adresa, kontaktOsoba);
                retVal = new TelefonD(domacinstvo, rs.getString("BrojTelefona"));
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
    public List<TelefonD> telefoniZaDomacinstvo(String jib) {
        List<TelefonD> retVal = new ArrayList<>();
        
        String query = "SELECT d.JIB, d.Naziv, d.IdAdrese, a.KucniBroj, " +
        				"u.NazivUlice, m.PostanskiBroj, m.NazivMjesta, " +
        				"ko.IdOsobe AS KontaktId, ko.Ime AS KontaktIme, ko.Prezime AS KontaktPrezime, " +
        				"td.BrojTelefona " +
        				"FROM telefon_d td " +
        				"JOIN domacinstvo d ON td.JIB = d.JIB " +
        				"JOIN adresa a ON d.IdAdrese = a.IdAdrese " +
        				"JOIN ulica u ON a.NazivUlice = u.NazivUlice AND a.PostanskiBroj = u.PostanskiBroj " +
        				"JOIN mjesto m ON u.PostanskiBroj = m.PostanskiBroj " +
        				"LEFT JOIN osoba ko ON d.IdKontaktOsobe = ko.IdOsobe " +
        				"WHERE td.JIB = ?";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, jib);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                	Mjesto mjesto = new Mjesto(rs.getInt("PostanskiBroj"), rs.getString("NazivMjesta"));
                    Ulica ulica = new Ulica(rs.getString("NazivUlice"), mjesto);
                    Adresa adresa = new Adresa(rs.getInt("IdAdrese"), ulica, rs.getInt("KucniBroj"));

                    Osoba kontaktOsoba = null;
                    int kontaktId = rs.getInt("KontaktId");
                    if (!rs.wasNull()) {
                        kontaktOsoba = new Osoba(kontaktId, rs.getString("KontaktIme"), rs.getString("KontaktPrezime"));
                    }

                    Domacinstvo domacinstvo = new Domacinstvo(rs.getString("JIB"), rs.getString("Naziv"), adresa, kontaktOsoba);
                    TelefonD telefon = new TelefonD(domacinstvo, rs.getString("BrojTelefona"));

                    retVal.add(telefon);
                }
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        return retVal;
    }

    @Override
    public boolean dodajTelefon(TelefonD telefon) {
        String query = "INSERT INTO telefon_d (JIB, BrojTelefona) VALUES (?, ?)";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, telefon.getDomacinstvo().getJib());
            ps.setString(2, telefon.getBrojTelefona());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean azurirajTelefon(TelefonD telefon, String stariBrojTelefona) {
        String query = "UPDATE telefon_d SET BrojTelefona = ? WHERE JIB = ? AND BrojTelefona = ?";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, telefon.getBrojTelefona());
            ps.setString(2, telefon.getDomacinstvo().getJib());
            ps.setString(3, stariBrojTelefona);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean obrisiTelefon(String jib, String brojTelefona) {
        String query = "DELETE FROM telefon_d WHERE JIB = ? AND BrojTelefona = ?";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, jib);
            ps.setString(2, brojTelefona);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return false;
    }
}
