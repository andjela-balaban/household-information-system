package org.unibl.etf.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import org.unibl.etf.dao.*;
import org.unibl.etf.entity.*;

public class PreradaMlijekaDataAccessImpl implements PreradaMlijekaDataAccess {

	@Override
	public PreradaMlijeka preradaMlijeka(int idPrerade) {
		PreradaMlijeka retVal = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT p.IdPrerade, m.IdMlijeka, d.JIB, z.IdOsobe, o.Ime, o.Prezime, p.DatumPrerade, p.KolicinaMlijekaZaPreradu, " +
        				"p.IdMjere, mj.Jedinica " +
        				"FROM prerada_mlijeka p " +
        				"JOIN mlijeko m ON m.IdMlijeka = p.IdMlijeka " +
        				"JOIN domacinstvo d ON d.JIB = p.JIB " +
        				"JOIN zaposleni z ON z.IdOsobe = p.IdOsobe " +
        				"JOIN osoba o ON o.IdOsobe = p.IdOsobe " +
        				"JOIN jedinica_mjere mj ON mj.IdMjere = p.IdMjere " +
        				"WHERE p.IdPrerade = ? ";
        
        try {
            conn = ConnectionPool.getInstance().checkOut();
            ps = conn.prepareStatement(query);
            ps.setInt(1, idPrerade);
            rs = ps.executeQuery();

            if (rs.next()) {
            	
            	int idMlijeka = rs.getInt("IdMlijeka");
            	MlijekoDataAccess mlijekoDAO = new MlijekoDataAccessImpl();
            	Mlijeko mlijeko = mlijekoDAO.mlijeko(idMlijeka);
            	
            	String jib = rs.getString("JIB");
                DomacinstvoDataAccess domacinstvoDAO = new DomacinstvoDataAccessImpl();
                Domacinstvo domacinstvo = domacinstvoDAO.domacinstvo(jib);
                
                int idOsobe = rs.getInt("IdOsobe");
                ZaposleniDataAccess zaposleniDAO = new ZaposleniDataAccessImpl();
                Zaposleni zaposleni = zaposleniDAO.zaposleni(idOsobe);
                
                retVal = new PreradaMlijeka(
                		idPrerade,
                		mlijeko,
                		domacinstvo,
                		zaposleni,
                		rs.getDate("DatumPrerade").toLocalDate(),
                		rs.getDouble("KolicinaMlijekaZaPreradu"),
                		JedinicaMjere.fromId(rs.getInt("IdMjere"))
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
	public List<PreradaMlijeka> svaPreradaMlijeka() {
		List<PreradaMlijeka> retVal = new ArrayList<>();
        String query = "SELECT p.IdPrerade, m.IdMlijeka, d.JIB, z.IdOsobe, o.Ime, o.Prezime, p.DatumPrerade, p.KolicinaMlijekaZaPreradu, " +
						"p.IdMjere, mj.Jedinica " +
						"FROM prerada_mlijeka p " +
						"JOIN mlijeko m ON m.IdMlijeka = p.IdMlijeka " +
						"JOIN domacinstvo d ON d.JIB = p.JIB " +
						"JOIN zaposleni z ON z.IdOsobe = p.IdOsobe " +
						"JOIN osoba o ON o.IdOsobe = p.IdOsobe " +
						"JOIN jedinica_mjere mj ON mj.IdMjere = p.IdMjere";

        
        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

        	while (rs.next()) {

        		int idMlijeka = rs.getInt("IdMlijeka");
            	MlijekoDataAccess mlijekoDAO = new MlijekoDataAccessImpl();
            	Mlijeko mlijeko = mlijekoDAO.mlijeko(idMlijeka);
            	
            	String jib = rs.getString("JIB");
                DomacinstvoDataAccess domacinstvoDAO = new DomacinstvoDataAccessImpl();
                Domacinstvo domacinstvo = domacinstvoDAO.domacinstvo(jib);
                
                int idOsobe = rs.getInt("IdOsobe");
                ZaposleniDataAccess zaposleniDAO = new ZaposleniDataAccessImpl();
                Zaposleni zaposleni = zaposleniDAO.zaposleni(idOsobe);
                
                PreradaMlijeka prerada = new PreradaMlijeka(
                		rs.getInt("IdPrerade"),
                		mlijeko,
                		domacinstvo,
                		zaposleni,
                		rs.getDate("DatumPrerade").toLocalDate(),
                		rs.getDouble("KolicinaMlijekaZaPreradu"),
                		JedinicaMjere.fromId(rs.getInt("IdMjere"))
                		);
                
                retVal.add(prerada);
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return retVal;
	}

	@Override
	public boolean dodajPreraduMlijeka(PreradaMlijeka prerada) {
		String query = "INSERT INTO prerada_mlijeka (IdMlijeka, JIB, IdOsobe, DatumPrerade, KolicinaMlijekaZaPreradu, IdMjere) " +
						"VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, prerada.getMlijeko().getIdMlijeka());
            ps.setString(2, prerada.getDomacinstvo().getJib());
            ps.setInt(3, prerada.getZaposleni().getOsoba().getIdOsobe());
            ps.setDate(4, java.sql.Date.valueOf(prerada.getDatumPrerade()));
            ps.setDouble(5, prerada.getKolicinaZaPreradu());
            ps.setInt(6, prerada.getMjera().getId());
            
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
	}

	@Override
	public boolean azurirajPreraduMlijeka(PreradaMlijeka prerada) {
		String query = "UPDATE prerada_mlijeka SET IdMlijeka = ?, JIB = ?, IdOsobe = ?, DatumPrerade = ?, " +
						"KolicinaMlijekaZaPreradu = ?, IdMjere = ? WHERE IdPrerade = ?";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

        	ps.setInt(1, prerada.getMlijeko().getIdMlijeka());
            ps.setString(2, prerada.getDomacinstvo().getJib());
            ps.setInt(3, prerada.getZaposleni().getOsoba().getIdOsobe());
            ps.setDate(4, java.sql.Date.valueOf(prerada.getDatumPrerade()));
            ps.setDouble(5, prerada.getKolicinaZaPreradu());
            ps.setInt(6, prerada.getMjera().getId());
            
            ps.setInt(7, prerada.getIdPrerade());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
	}

	@Override
	public boolean obrisiPreraduMlijeka(int idPrerade) {
		String query = "DELETE FROM prerada_mlijeka WHERE IdPrerade = ?";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idPrerade);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return false;
	}

}
