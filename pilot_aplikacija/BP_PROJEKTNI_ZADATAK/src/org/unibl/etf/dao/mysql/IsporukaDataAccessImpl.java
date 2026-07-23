package org.unibl.etf.dao.mysql;

import java.sql.*;
import java.util.*;
import org.unibl.etf.dao.*;
import org.unibl.etf.entity.*;

public class IsporukaDataAccessImpl implements IsporukaDataAccess {

	@Override
	public Isporuka isporuka(int idIsporuke) {
		Isporuka retVal = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT i.IdIsporuke, i.DatumIsporuke, a.IdAdrese, a.PostanskiBroj, a.NazivUlice, a.KucniBroj, m.NazivMjesta, " +
        				"i.IdKupca, ko.Ime, ko.Prezime, i.IdIsporucioca, zo.Ime, zo.Prezime " +
        				"FROM isporuka i " +
        				"JOIN adresa a ON a.IdAdrese = i.IdAdrese " +
        				"LEFT JOIN mjesto m ON a.PostanskiBroj = m.PostanskiBroj " +
        				"JOIN kupac k ON k.IdOsobe = i.IdKupca " +
        				"LEFT JOIN osoba ko ON ko.IdOsobe = k.IdOsobe " +
        				"JOIN zaposleni z ON z.IdOsobe = i.IdIsporucioca " +
        				"LEFT JOIN osoba zo ON zo.IdOsobe = z.IdOsobe " +
        				"WHERE i.IdIsporuke = ? ";
        
        try {
            conn = ConnectionPool.getInstance().checkOut();
            ps = conn.prepareStatement(query);
            ps.setInt(1, idIsporuke);
            rs = ps.executeQuery();

            if (rs.next()) {
            	
            	Adresa adresa = null;

                if (rs.getObject("IdAdrese") != null) {
                    Mjesto mjesto = new Mjesto(rs.getInt("PostanskiBroj"), rs.getString("NazivMjesta"));
                    Ulica ulica = new Ulica(rs.getString("NazivUlice"), mjesto);
                    adresa = new Adresa(rs.getInt("IdAdrese"), ulica, rs.getInt("KucniBroj"));
                }
                
                int idKupca = rs.getInt("IdKupca");
                KupacDataAccess kupacDAO = new KupacDataAccessImpl();
                Kupac kupac = kupacDAO.kupac(idKupca);
                
                int idIsporucioca = rs.getInt("IdIsporucioca");
                ZaposleniDataAccess zaposleniDAO = new ZaposleniDataAccessImpl();
                Zaposleni zaposleni = zaposleniDAO.zaposleni(idIsporucioca);
                
                retVal = new Isporuka(
                		idIsporuke,
                		rs.getDate("DatumIsporuke").toLocalDate(),
                		adresa,
                		kupac,
                		zaposleni
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
	public List<Isporuka> isporuke() {
		List<Isporuka> retVal = new ArrayList<>();
        String query = "SELECT i.IdIsporuke, i.DatumIsporuke, a.IdAdrese, a.PostanskiBroj, a.NazivUlice, a.KucniBroj, m.NazivMjesta, " +
						"i.IdKupca, ko.Ime, ko.Prezime, i.IdIsporucioca, zo.Ime, zo.Prezime " +
						"FROM isporuka i " +
						"JOIN adresa a ON a.IdAdrese = i.IdAdrese " +
						"LEFT JOIN mjesto m ON a.PostanskiBroj = m.PostanskiBroj " +
						"JOIN kupac k ON k.IdOsobe = i.IdKupca " +
						"LEFT JOIN osoba ko ON ko.IdOsobe = k.IdOsobe " +
						"JOIN zaposleni z ON z.IdOsobe = i.IdIsporucioca " +
						"LEFT JOIN osoba zo ON zo.IdOsobe = z.IdOsobe";

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
                
                int idKupca = rs.getInt("IdKupca");
                KupacDataAccess kupacDAO = new KupacDataAccessImpl();
                Kupac kupac = kupacDAO.kupac(idKupca);
                
                int idIsporucioca = rs.getInt("IdIsporucioca");
                ZaposleniDataAccess zaposleniDAO = new ZaposleniDataAccessImpl();
                Zaposleni zaposleni = zaposleniDAO.zaposleni(idIsporucioca);
                
                Isporuka isporuka = new Isporuka(
                		rs.getInt("IdIsporuke"),
                		rs.getDate("DatumIsporuke").toLocalDate(),
                		adresa,
                		kupac,
                		zaposleni
                		);
                retVal.add(isporuka);
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return retVal;
	}

	@Override
	public boolean dodajIsporuku(Isporuka isporuka) {
		String query = "INSERT INTO isporuka (Datumisporuke, IdAdrese, IdKupca, IdIsporucioca) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

        	ps.setDate(1, java.sql.Date.valueOf(isporuka.getDatumIsporuke()));
        	ps.setInt(2, isporuka.getAdresa().getIdAdrese());
        	ps.setInt(3, isporuka.getKupac().getOsoba().getIdOsobe());
        	ps.setInt(4, isporuka.getZaposleniIsporucilac().getOsoba().getIdOsobe());
            
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
	}

	@Override
	public boolean azurirajIsporuku(Isporuka isporuka) {
		String query = "UPDATE isporuka SET Datumisporuke = ?, IdAdrese = ?, IdKupca = ?, IdIsporucioca = ? WHERE IdIsporuke = ?";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

        	ps.setDate(1, java.sql.Date.valueOf(isporuka.getDatumIsporuke()));
        	ps.setInt(2, isporuka.getAdresa().getIdAdrese());
        	ps.setInt(3, isporuka.getKupac().getOsoba().getIdOsobe());
        	ps.setInt(4, isporuka.getZaposleniIsporucilac().getOsoba().getIdOsobe());
            
            ps.setInt(5, isporuka.getIdIsporuke()); 

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
	}

	@Override
	public boolean obrisiIsporuku(int idIsporuke) {
		String query = "DELETE FROM isporuka WHERE IdIsporuke = ?";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idIsporuke);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return false;
	}

}
