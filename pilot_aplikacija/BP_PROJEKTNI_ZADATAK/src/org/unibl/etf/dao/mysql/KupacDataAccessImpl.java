package org.unibl.etf.dao.mysql;

import java.sql.*;
import java.util.*;
import org.unibl.etf.dao.*;
import org.unibl.etf.entity.*;

public class KupacDataAccessImpl implements KupacDataAccess{

	@Override
	public Kupac kupac(int idOsobe) {
		Kupac retVal = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT k.IdOsobe, o.Ime, o.Prezime, a.IdAdrese, a.KucniBroj, u.NazivUlice, m.PostanskiBroj, m.NazivMjesta " +
        			   "FROM kupac k " +
        			   "JOIN osoba o ON k.IdOsobe = o.IdOsobe " + 
        			   "LEFT JOIN adresa a ON o.IdAdrese = a.IdAdrese " +
        			   "LEFT JOIN ulica u ON a.NazivUlice = u.NazivUlice AND a.PostanskiBroj = u.PostanskiBroj " +
        			   "LEFT JOIN mjesto m ON u.PostanskiBroj = m.PostanskiBroj " +
        			   "WHERE k.IdOsobe = ? ";

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

                retVal = new Kupac(new Osoba(
                    idOsobe,
                    rs.getString("Ime"),
                    rs.getString("Prezime"),
                    adresa
                ));
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
	public List<Kupac> kupci() {
		List<Kupac> retVal = new ArrayList<>();
        String query = "SELECT k.IdOsobe, o.Ime, o.Prezime, a.IdAdrese, a.KucniBroj, u.NazivUlice, m.PostanskiBroj, m.NazivMjesta " +
        			   "FROM kupac k " +
        			   "JOIN osoba o ON k.IdOsobe = o.IdOsobe " + 
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

                Kupac kupac = new Kupac(new Osoba(
                    rs.getInt("IdOsobe"),
                    rs.getString("Ime"),
                    rs.getString("Prezime"),
                    adresa
                ));

                retVal.add(kupac);
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return retVal;
	}
	
	@Override
	public boolean dodajKupca(Kupac kupac) {
	    Osoba osoba = kupac.getOsoba();

	    String insertOsobaQuery = "INSERT INTO osoba (Ime, Prezime, IdAdrese) VALUES (?, ?, ?)";
	    String insertKupacQuery = "INSERT INTO kupac (IdOsobe) VALUES (?)";

	    try (Connection conn = ConnectionPool.getInstance().checkOut()) {
	        conn.setAutoCommit(false);
	        // prvo insert-ujemo osobu u bazu
	        try (PreparedStatement psOsoba = conn.prepareStatement(insertOsobaQuery, Statement.RETURN_GENERATED_KEYS)) {
	            psOsoba.setString(1, osoba.getIme());
	            psOsoba.setString(2, osoba.getPrezime());
	            if (osoba.getAdresa() != null)
	                psOsoba.setInt(3, osoba.getAdresa().getIdAdrese());
	            else
	                psOsoba.setNull(3, java.sql.Types.INTEGER);

	            int affectedRows = psOsoba.executeUpdate();
	            if (affectedRows == 0) {
	                conn.rollback();
	                return false;
	            }

	            try (ResultSet generatedKeys = psOsoba.getGeneratedKeys()) {
	                if (generatedKeys.next()) {
	                    int noviId = generatedKeys.getInt(1);
	                    osoba.setIdOsobe(noviId);  // postavljanje id-a u objekat
	                } else {
	                    conn.rollback();
	                    return false;
	                }
	            }
	        }
	        // sada postavljamo kupca sa novim id-em
	        try (PreparedStatement psKupac = conn.prepareStatement(insertKupacQuery)) {
	            psKupac.setInt(1, osoba.getIdOsobe());
	            int affectedRows = psKupac.executeUpdate();
	            if (affectedRows == 0) {
	                conn.rollback();
	                return false;
	            }
	        }

	        conn.commit();
	        return true;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	@Override
	public boolean azurirajKupca(Kupac kupac) {
	    OsobaDataAccessImpl osobaDAO = new OsobaDataAccessImpl();
	    return osobaDAO.azurirajOsobu(kupac.getOsoba());
	}

	@Override
	public boolean obrisiKupca(int idOsobe) {
	    OsobaDataAccessImpl osobaDAO = new OsobaDataAccessImpl();
	    return osobaDAO.obrisiOsobu(idOsobe);
	}
	
	@Override
	public int zadnjiIdOsobe() {
	    String query = "SELECT MAX(IdOsobe) AS ZadnjiId FROM osoba";

	    try (Connection conn = ConnectionPool.getInstance().checkOut();
	         PreparedStatement ps = conn.prepareStatement(query);
	         ResultSet rs = ps.executeQuery()) {

	        if (rs.next()) {
	            return rs.getInt("ZadnjiId");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return -1; 
	}

}