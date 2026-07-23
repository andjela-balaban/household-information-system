package org.unibl.etf.dao.mysql;

import java.sql.*;
import java.util.*;
import org.unibl.etf.dao.*;
import org.unibl.etf.entity.*;

public class ProizvodDataAccessImpl implements ProizvodDataAccess {

	@Override
	public Proizvod proizvod(int idProizvoda) {
		Proizvod retVal = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT p.IdProizvoda, pr.IdPrerade, tp.IdTipaProizvoda, tp.NazivTipaProizvoda, p.Cijena " +
        			   "FROM proizvod p " +
        			   "JOIN prerada_mlijeka pr ON pr.IdPrerade = p.IdPrerade " +
        			   "JOIN tip_proizvoda tp ON tp.IdTipaProizvoda = p.IdTipaProizvoda " +
        			   "WHERE p.IdProizvoda = ? ";
        
        
        try {
            conn = ConnectionPool.getInstance().checkOut();
            ps = conn.prepareStatement(query);
            ps.setInt(1, idProizvoda);
            rs = ps.executeQuery();

            if (rs.next()) {
            	
            	int idPrerade = rs.getInt("IdPrerade");
            	PreradaMlijekaDataAccess preradaMlijekaDAO = new PreradaMlijekaDataAccessImpl();
            	PreradaMlijeka preradaMlijeka = preradaMlijekaDAO.preradaMlijeka(idPrerade);
                retVal = new Proizvod(
                		idProizvoda,
                		preradaMlijeka,
                		TipProizvoda.fromId(rs.getInt("IdTipaProizvoda")),
                		rs.getDouble("Cijena")
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
	public List<Proizvod> sviProizvodi() {
		List<Proizvod> retVal = new ArrayList<>();
        String query = "SELECT p.IdProizvoda, pr.IdPrerade, tp.IdTipaProizvoda, tp.NazivTipaProizvoda, p.Cijena " +
 			   			"FROM proizvod p " +
 			   			"JOIN prerada_mlijeka pr ON pr.IdPrerade = p.IdPrerade " +
 			   			"JOIN tip_proizvoda tp ON tp.IdTipaProizvoda = p.IdTipaProizvoda";

        
        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

        	while (rs.next()) {

        		int idPrerade = rs.getInt("IdPrerade");
            	PreradaMlijekaDataAccess preradaMlijekaDAO = new PreradaMlijekaDataAccessImpl();
            	PreradaMlijeka preradaMlijeka = preradaMlijekaDAO.preradaMlijeka(idPrerade);
            	Proizvod proizvod = new Proizvod(
            			rs.getInt("IdProizvoda"),
                		preradaMlijeka,
                		TipProizvoda.fromId(rs.getInt("IdTipaProizvoda")),
                		rs.getDouble("Cijena")
                		);

                retVal.add(proizvod);
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return retVal;
	}
	
	@Override
	public boolean dodajProizvod(Proizvod proizvod) {
		String query = "INSERT INTO proizvod (IdPrerade, IdTipaProizvoda, Cijena) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

        	ps.setInt(1, proizvod.getPrerada().getIdPrerade());
        	ps.setInt(2, proizvod.getTip().getId()); 
            ps.setDouble(3, proizvod.getCijena());
            
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
	}

	@Override
	public boolean azurirajProizvod(Proizvod proizvod) {
		String query = "UPDATE proizvod SET IdPrerade = ?, IdTipaProizvoda = ?, Cijena = ? WHERE IdProizvoda = ?";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

        	ps.setInt(1, proizvod.getPrerada().getIdPrerade());
        	ps.setInt(2, proizvod.getTip().getId()); 
            ps.setDouble(3, proizvod.getCijena());
            
            ps.setInt(4, proizvod.getIdProizvoda());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
	}

	@Override
	public boolean obrisiProizvod(int idProizvoda) {
		String query = "DELETE FROM proizvod WHERE IdProizvoda = ?";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idProizvoda);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return false;
	}
}
