package org.unibl.etf.dao.mysql;

import java.sql.*;
import java.util.*;
import org.unibl.etf.dao.*;
import org.unibl.etf.entity.*;

public class ZaposleniDataAccessImpl implements ZaposleniDataAccess{

	@Override
	public Zaposleni zaposleni(int idOsobe) {
		Zaposleni retVal = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT z.IdOsobe, o.Ime, o.Prezime, a.IdAdrese, a.KucniBroj, u.NazivUlice, m.PostanskiBroj, d.JIB, d.Naziv, " +
        				"m.NazivMjesta, z.Plata, z.IdFunkcije, zf.NazivFunkcije " + 
        				"FROM zaposleni z " +
        				"JOIN osoba o ON z.IdOsobe = o.IdOsobe " +
        				"JOIN domacinstvo d ON d.JIB = z.JIB " +
        				"LEFT JOIN adresa a ON o.IdAdrese = a.IdAdrese " +
        				"LEFT JOIN ulica u ON a.NazivUlice = u.NazivUlice AND a.PostanskiBroj = u.PostanskiBroj " +
        				"LEFT JOIN mjesto m ON u.PostanskiBroj = m.PostanskiBroj " +
        				"JOIN zaposleni_funkcija zf ON zf.IdFunkcije = z.IdFunkcije " +
        				"WHERE z.IdOsobe = ? ";
        
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
                ZaposleniFunkcija funkcija = ZaposleniFunkcija.fromId(rs.getInt("IdFunkcije"));
                double plata = rs.getDouble("Plata");
                String jib = rs.getString("JIB");
                DomacinstvoDataAccess domacinstvoDAO = new DomacinstvoDataAccessImpl();
                Domacinstvo domacinstvo = domacinstvoDAO.domacinstvo(jib); 
                retVal = new Zaposleni(new Osoba(
                    idOsobe,
                    rs.getString("Ime"),
                    rs.getString("Prezime"),
                    adresa
                ),
                	domacinstvo, funkcija, plata);
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
	public List<Zaposleni> sviZaposleni() {
		List<Zaposleni> retVal = new ArrayList<>();
        String query = "SELECT z.IdOsobe, o.Ime, o.Prezime, a.IdAdrese, a.KucniBroj, u.NazivUlice, m.PostanskiBroj, d.JIB, d.Naziv, " +
					   "m.NazivMjesta, z.Plata, z.IdFunkcije, zf.NazivFunkcije " + 
					   "FROM zaposleni z " +
					   "JOIN osoba o ON z.IdOsobe = o.IdOsobe " +
					   "JOIN domacinstvo d ON d.JIB = z.JIB " +
					   "LEFT JOIN adresa a ON o.IdAdrese = a.IdAdrese " +
					   "LEFT JOIN ulica u ON a.NazivUlice = u.NazivUlice AND a.PostanskiBroj = u.PostanskiBroj " +
					   "LEFT JOIN mjesto m ON u.PostanskiBroj = m.PostanskiBroj " +
					   "JOIN zaposleni_funkcija zf ON zf.IdFunkcije = z.IdFunkcije";

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

                ZaposleniFunkcija funkcija = ZaposleniFunkcija.fromId(rs.getInt("IdFunkcije"));
                double plata = rs.getDouble("Plata");
                String jib = rs.getString("JIB");
                DomacinstvoDataAccess domacinstvoDAO = new DomacinstvoDataAccessImpl();
                Domacinstvo domacinstvo = domacinstvoDAO.domacinstvo(jib); 
                Zaposleni zaposleni = new Zaposleni(new Osoba(
                	rs.getInt("IdOsobe"),
                    rs.getString("Ime"),
                    rs.getString("Prezime"),
                    adresa
                ),
                	domacinstvo, funkcija, plata);
                

                retVal.add(zaposleni);
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return retVal;
	}
	
	@Override
	public boolean dodajZaposlenog(Zaposleni zaposleni) {
	    Osoba osoba = zaposleni.getOsoba();

	    String insertOsobaQuery = "INSERT INTO osoba (Ime, Prezime, IdAdrese) VALUES (?, ?, ?)";
	    String insertZaposleniQuery = "INSERT INTO zaposleni (IdOsobe, JIB, IdFunkcije, Plata) VALUES (?, ?, ?, ?)";

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
	                    osoba.setIdOsobe(noviId);   // postavljanje id-a u objekat
	                } else {
	                    conn.rollback();
	                    return false;
	                }
	            }
	        }
	        // sada insert-ujemo i zaposlenog pomocu id-a osobe 
	        try (PreparedStatement psZaposleni = conn.prepareStatement(insertZaposleniQuery)) {
	            psZaposleni.setInt(1, osoba.getIdOsobe());
	            psZaposleni.setString(2, zaposleni.getDomacinstvo().getJib());
	            psZaposleni.setInt(3, zaposleni.getFunkcija().getId());
	            psZaposleni.setDouble(4, zaposleni.getPlata());

	            int affectedRows = psZaposleni.executeUpdate();
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
	public boolean azurirajZaposlenog(Zaposleni zaposleni) {
	    String updateOsobaQuery = "UPDATE osoba SET Ime = ?, Prezime = ?, IdAdrese = ? WHERE IdOsobe = ?";
	    String updateZaposleniQuery = "UPDATE zaposleni SET JIB = ?, IdFunkcije = ?, Plata = ? WHERE IdOsobe = ?";

	    try (Connection conn = ConnectionPool.getInstance().checkOut()) {
	        conn.setAutoCommit(false);

	        try (PreparedStatement psOsoba = conn.prepareStatement(updateOsobaQuery)) {
	            psOsoba.setString(1, zaposleni.getOsoba().getIme());
	            psOsoba.setString(2, zaposleni.getOsoba().getPrezime());
	            if (zaposleni.getOsoba().getAdresa() != null)
	                psOsoba.setInt(3, zaposleni.getOsoba().getAdresa().getIdAdrese());
	            else
	                psOsoba.setNull(3, java.sql.Types.INTEGER);
	            psOsoba.setInt(4, zaposleni.getOsoba().getIdOsobe());

	            if (psOsoba.executeUpdate() == 0) {
	                conn.rollback();
	                return false;
	            }
	        }

	        try (PreparedStatement psZaposleni = conn.prepareStatement(updateZaposleniQuery)) {
	            psZaposleni.setString(1, zaposleni.getDomacinstvo().getJib());
	            psZaposleni.setInt(2, zaposleni.getFunkcija().getId());
	            psZaposleni.setDouble(3, zaposleni.getPlata());
	            psZaposleni.setInt(4, zaposleni.getOsoba().getIdOsobe());

	            if (psZaposleni.executeUpdate() == 0) {
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
	public boolean obrisiZaposlenog(int idOsobe) {
		OsobaDataAccessImpl osobaDAO = new OsobaDataAccessImpl();
	    return osobaDAO.obrisiOsobu(idOsobe);
	}
}