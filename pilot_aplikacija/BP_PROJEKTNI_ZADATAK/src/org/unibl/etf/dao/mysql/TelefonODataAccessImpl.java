package org.unibl.etf.dao.mysql;

import java.sql.*;
import java.util.*;
import org.unibl.etf.dao.*;
import org.unibl.etf.entity.*;

public class TelefonODataAccessImpl implements TelefonODataAccess {

    @Override
    public TelefonO telefon(String brojTelefona, int idOsobe) {
        TelefonO retVal = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT tel.BrojTelefona, o.IdOsobe, o.Ime, o.Prezime " +
        				"FROM telefon_o tel " +
        				"JOIN osoba o ON tel.IdOsobe = o.IdOsobe " + 
        				"WHERE tel.BrojTelefona = ? AND tel.IdOsobe = ? ";

        try {
            conn = ConnectionPool.getInstance().checkOut();
            ps = conn.prepareStatement(query);
            ps.setString(1, brojTelefona);
            ps.setInt(2, idOsobe);
            rs = ps.executeQuery();

            if (rs.next()) {
                Osoba osoba = new Osoba(rs.getInt("IdOsobe"), rs.getString("Ime"), rs.getString("Prezime"));
                retVal = new TelefonO(osoba, rs.getString("BrojTelefona"));
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
    public List<TelefonO> sviTelefoni() {
        List<TelefonO> retVal = new ArrayList<>();
        String query = "SELECT tel.BrojTelefona, o.IdOsobe, o.Ime, o.Prezime " +
                       "FROM telefon_o tel " +
                       "JOIN osoba o ON tel.IdOsobe = o.IdOsobe";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Osoba osoba = new Osoba(
                        rs.getInt("IdOsobe"),
                        rs.getString("Ime"),
                        rs.getString("Prezime")
                );
                TelefonO telefon = new TelefonO(osoba, rs.getString("BrojTelefona"));
                retVal.add(telefon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return retVal;
    }

    @Override
    public List<TelefonO> telefoniZaOsobu(int idOsobe) {
        List<TelefonO> retVal = new ArrayList<>();
        String query = "SELECT tel.BrojTelefona, o.IdOsobe, o.Ime, o.Prezime " +
        				"FROM telefon_o tel " +
        				"JOIN osoba o ON tel.IdOsobe = o.IdOsobe " + 
        				"WHERE tel.IdOsobe = ? ";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idOsobe);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                	Osoba osoba = new Osoba(rs.getInt("IdOsobe"), rs.getString("Ime"), rs.getString("Prezime"));
                	TelefonO telefon = new TelefonO(osoba, rs.getString("BrojTelefona"));
                    retVal.add(telefon);
                }
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        return retVal;
    }

    @Override
    public boolean dodajTelefon(TelefonO telefon) {
        String query = "INSERT INTO telefon_o (IdOsobe, BrojTelefona) VALUES (?, ?)";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, telefon.getOsoba().getIdOsobe());
            ps.setString(2, telefon.getBrojTelefona());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean azurirajTelefon(TelefonO telefon, String stariBrojTelefona) {
        String query = "UPDATE telefon_o SET BrojTelefona = ? WHERE IdOsobe = ? AND BrojTelefona = ?";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, telefon.getBrojTelefona());
            ps.setInt(2, telefon.getOsoba().getIdOsobe());
            ps.setString(3, stariBrojTelefona);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean obrisiTelefon(int idOsobe, String brojTelefona) {
        String query = "DELETE FROM telefon_o WHERE IdOsobe = ? AND BrojTelefona = ?";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idOsobe);
            ps.setString(2, brojTelefona);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return false;
    }
}
