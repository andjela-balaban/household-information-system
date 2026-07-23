package org.unibl.etf.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import org.unibl.etf.dao.*;
import org.unibl.etf.entity.*;

public class NarudzbaDataAccessImpl implements NarudzbaDataAccess {

	@Override
	public Narudzba narudzba(int idNarudzbe) {
		Narudzba retVal = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT n.IdNarudzbe, sn.IdStatusaNarudzbe, sn.StatusNarudzbe, n.DatumNarudzbe, " +
        				"n.IdKupca, ko.Ime, ko.Prezime, p.IdPlacanja, p.Iznos, i.IdIsporuke, i.DatumIsporuke " +
        				"FROM narudzba n " +
        				"JOIN status_narudzbe sn ON sn.IdStatusaNarudzbe = n.IdStatusaNarudzbe " +
        				"JOIN kupac k ON k.IdOsobe = n.IdKupca " +
        				"LEFT JOIN osoba ko ON ko.IdOsobe = k.IdOsobe " +
        				"JOIN placanje p ON p.IdPlacanja = n.IdPlacanja " +
        				"LEFT JOIN isporuka i ON i.IdIsporuke = n.IdIsporuke " +
        				"WHERE n.IdNarudzbe = ? ";
        
        try {
            conn = ConnectionPool.getInstance().checkOut();
            ps = conn.prepareStatement(query);
            ps.setInt(1, idNarudzbe);
            rs = ps.executeQuery();

            if (rs.next()) {
                
                int idKupca = rs.getInt("IdKupca");
                KupacDataAccess kupacDAO = new KupacDataAccessImpl();
                Kupac kupac = kupacDAO.kupac(idKupca);
                
                int idPlacanja = rs.getInt("IdPlacanja");
                PlacanjeDataAccess placanjeDAO = new PlacanjeDataAccessImpl();
                Placanje placanje = placanjeDAO.placanje(idPlacanja);
                
                Integer idIsporuke = rs.getObject("IdIsporuke", Integer.class); 
                Isporuka isporuka = null;
                if (idIsporuke != null) {
                    IsporukaDataAccess isporukaDAO = new IsporukaDataAccessImpl();
                    isporuka = isporukaDAO.isporuka(idIsporuke);
                }
                
                retVal = new Narudzba(
                		idNarudzbe,
                		StatusNarudzbe.fromId(rs.getInt("IdStatusaNarudzbe")),
                		rs.getDate("DatumNarudzbe").toLocalDate(),
                		kupac,
                		placanje,
                		isporuka
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
	public List<Narudzba> narudzbe() {
		List<Narudzba> retVal = new ArrayList<>();
        String query = "SELECT n.IdNarudzbe, sn.IdStatusaNarudzbe, sn.StatusNarudzbe, n.DatumNarudzbe, " +
						"n.IdKupca, ko.Ime, ko.Prezime, p.IdPlacanja, p.Iznos, i.IdIsporuke, i.DatumIsporuke " +
						"FROM narudzba n " +
						"JOIN status_narudzbe sn ON sn.IdStatusaNarudzbe = n.IdStatusaNarudzbe " +
						"JOIN kupac k ON k.IdOsobe = n.IdKupca " +
						"LEFT JOIN osoba ko ON ko.IdOsobe = k.IdOsobe " +
						"JOIN placanje p ON p.IdPlacanja = n.IdPlacanja " +
						"LEFT JOIN isporuka i ON i.IdIsporuke = n.IdIsporuke";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

        	while (rs.next()) {

        		int idKupca = rs.getInt("IdKupca");
                KupacDataAccess kupacDAO = new KupacDataAccessImpl();
                Kupac kupac = kupacDAO.kupac(idKupca);
                
                int idPlacanja = rs.getInt("IdPlacanja");
                PlacanjeDataAccess placanjeDAO = new PlacanjeDataAccessImpl();
                Placanje placanje = placanjeDAO.placanje(idPlacanja);
                
                Integer idIsporuke = rs.getObject("IdIsporuke", Integer.class);
                Isporuka isporuka = null;
                if (idIsporuke != null) {
                    IsporukaDataAccess isporukaDAO = new IsporukaDataAccessImpl();
                    isporuka = isporukaDAO.isporuka(idIsporuke);
                }
                
                Narudzba narudzba = new Narudzba(
                		rs.getInt("IdNarudzbe"),
                		StatusNarudzbe.fromId(rs.getInt("IdStatusaNarudzbe")),
                		rs.getDate("DatumNarudzbe").toLocalDate(),
                		kupac,
                		placanje,
                		isporuka
                		);
                retVal.add(narudzba);
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return retVal;
	}

	@Override
	public boolean dodajNarudzbu(Narudzba narudzba) {
	    String query = "INSERT INTO narudzba (IdStatusaNarudzbe, DatumNarudzbe, IdKupca, IdPlacanja, IdIsporuke) VALUES (?, ?, ?, ?, ?)";

	    try (Connection conn = ConnectionPool.getInstance().checkOut();
	         PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

	    	if(narudzba.getStatus() != null)
	    		ps.setInt(1, narudzba.getStatus().getId());
	    	else
	    		ps.setInt(1, StatusNarudzbe.NEPLACENA.getId());
	        ps.setDate(2, java.sql.Date.valueOf(narudzba.getDatumNarudzbe()));
	        ps.setInt(3, narudzba.getKupac().getOsoba().getIdOsobe());
	        ps.setInt(4, narudzba.getPlacanje().getIdPlacanja());

	        if (narudzba.getIsporuka() != null) {
	            ps.setInt(5, narudzba.getIsporuka().getIdIsporuke());
	        } else {
	            ps.setNull(5, java.sql.Types.INTEGER);
	        }

	        int rows = ps.executeUpdate();

	        if (rows == 1) {
	            try (ResultSet rs = ps.getGeneratedKeys()) {
	                if (rs.next()) {
	                    narudzba.setIdNarudzbe(rs.getInt(1));
	                    return true;
	                }
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return false;
	}


	@Override
	public boolean azurirajNarudzbu(Narudzba narudzba) {
		String query = "UPDATE narudzba SET IdStatusaNarudzbe = ?, DatumNarudzbe = ?, IdKupca = ?, IdPlacanja = ?, IdIsporuke = ? WHERE IdNarudzbe = ?";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

        	ps.setInt(1, narudzba.getStatus().getId());
        	ps.setDate(2, java.sql.Date.valueOf(narudzba.getDatumNarudzbe()));
        	ps.setInt(3, narudzba.getKupac().getOsoba().getIdOsobe());
        	ps.setInt(4, narudzba.getPlacanje().getIdPlacanja());
        	
        	if (narudzba.getIsporuka() != null) {
                ps.setInt(5, narudzba.getIsporuka().getIdIsporuke());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            
        	ps.setInt(6, narudzba.getIdNarudzbe());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
	}

	@Override
	public boolean obrisiNarudzbu(int idNarudzbe) {
		String query = "DELETE FROM narudzba WHERE IdNarudzbe = ?";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idNarudzbe);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return false;
	}

}
