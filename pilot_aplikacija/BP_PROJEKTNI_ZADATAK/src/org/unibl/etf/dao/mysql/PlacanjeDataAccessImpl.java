package org.unibl.etf.dao.mysql;

import java.sql.*;
import java.util.*;
import org.unibl.etf.dao.*;
import org.unibl.etf.entity.*;

public class PlacanjeDataAccessImpl implements PlacanjeDataAccess {

	@Override
	public Placanje placanje(int idPlacanja) {
		Placanje retVal = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT p.IdPlacanja, k.IdOsobe, o.Ime, o.Prezime, p.Iznos, tp.IdTipaPlacanja, tp.TipPlacanja " +
        				"FROM placanje p " +
        				"JOIN kupac k ON k.IdOsobe = p.IdOsobe " +
        				"LEFT JOIN osoba o ON o.IdOsobe = k.IdOsobe " +
        				"JOIN tip_placanja tp ON tp.IdTipaPlacanja = p.IdTipaPlacanja " +
        				"WHERE p.IdPlacanja = ? ";
        
        
        try {
            conn = ConnectionPool.getInstance().checkOut();
            ps = conn.prepareStatement(query);
            ps.setInt(1, idPlacanja);
            rs = ps.executeQuery();

            if (rs.next()) {
            	
            	int idOsobe = rs.getInt("IdOsobe");
            	KupacDataAccess kupacDAO = new KupacDataAccessImpl();
            	Kupac kupac = kupacDAO.kupac(idOsobe);
                retVal = new Placanje(
                		idPlacanja,
                		kupac,
                		rs.getDouble("Iznos"),
                		TipPlacanja.fromId(rs.getInt("IdTipaPlacanja"))
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
	public List<Placanje> svaPlacanja() {
		List<Placanje> retVal = new ArrayList<>();
        String query = "SELECT p.IdPlacanja, k.IdOsobe, o.Ime, o.Prezime, p.Iznos, tp.IdTipaPlacanja, tp.TipPlacanja " +
						"FROM placanje p " +
						"JOIN kupac k ON k.IdOsobe = p.IdOsobe " +
						"LEFT JOIN osoba o ON o.IdOsobe = k.IdOsobe " +
						"JOIN tip_placanja tp ON tp.IdTipaPlacanja = p.IdTipaPlacanja";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

        	while (rs.next()) {

        		int idOsobe = rs.getInt("IdOsobe");
            	KupacDataAccess kupacDAO = new KupacDataAccessImpl();
            	Kupac kupac = kupacDAO.kupac(idOsobe);
                Placanje placanje = new Placanje(
                		rs.getInt("IdPlacanja"),
                		kupac,
                		rs.getDouble("Iznos"),
                		TipPlacanja.fromId(rs.getInt("IdTipaPlacanja"))
                		);

                retVal.add(placanje);
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return retVal;
	}

	@Override
	public boolean dodajPlacanje(Placanje placanje) {
		String query = "INSERT INTO placanje (IdOsobe, Iznos, IdTipaPlacanja) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

        	ps.setInt(1, placanje.getKupac().getOsoba().getIdOsobe());
        	ps.setDouble(2, placanje.getIznos());
        	ps.setInt(3, placanje.getTipPlacanja().getId());
            
            
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
	}

	@Override
	public boolean azurirajPlacanje(Placanje placanje) {
		String query = "UPDATE placanje SET IdOsobe = ?, Iznos = ?, IdTipaPlacanja = ? WHERE IdPlacanja = ?";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

        	ps.setInt(1, placanje.getKupac().getOsoba().getIdOsobe());
        	ps.setDouble(2, placanje.getIznos());
        	ps.setInt(3, placanje.getTipPlacanja().getId());
            
            ps.setInt(4, placanje.getIdPlacanja());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
	}

	@Override
	public boolean obrisiPlacanje(int idPlacanja) {
		String query = "DELETE FROM placanje WHERE IdPlacanja = ?";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idPlacanja);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return false;
	}

}
