package org.unibl.etf.dao.mysql;

import java.sql.*;
import java.util.*;
import org.unibl.etf.dao.*;
import org.unibl.etf.entity.*;

public class SadrzajNarudzbeDataAccessImpl implements SadrzajNarudzbeDataAccess {

	@Override
	public SadrzajNarudzbe sadrzajNarudzbe(int idNarudzbe, int idProizvoda) {
		SadrzajNarudzbe retVal = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT n.IdNarudzbe, n.IdStatusaNarudzbe, stat.StatusNarudzbe, n.DatumNarudzbe, " +
        				"p.IdProizvoda, p.IdTipaProizvoda, tp.NazivTipaProizvoda, sn.Kolicina " +
        				"FROM sadrzaj_narudzbe sn " +
        				"JOIN narudzba n ON n.IdNarudzbe = sn.IdNarudzbe " +
        				"LEFT JOIN status_narudzbe stat ON stat.IdStatusaNarudzbe = n.IdStatusaNarudzbe " +
        				"JOIN proizvod p ON p.IdProizvoda = sn.IdProizvoda " +
        				"LEFT JOIN tip_proizvoda tp ON tp.IdTipaProizvoda = p.IdTipaProizvoda " +
        				"WHERE n.IdNarudzbe = ?  AND p.IdProizvoda = ?";
        
        try {
            conn = ConnectionPool.getInstance().checkOut();
            ps = conn.prepareStatement(query);
            ps.setInt(1, idNarudzbe);
            ps.setInt(2, idProizvoda);
            rs = ps.executeQuery();

            if (rs.next()) {
            	
                NarudzbaDataAccess narudzbaDAO = new NarudzbaDataAccessImpl();
                Narudzba narudzba = narudzbaDAO.narudzba(idNarudzbe);
                
                ProizvodDataAccess proizvodDAO = new ProizvodDataAccessImpl();
                Proizvod proizvod = proizvodDAO.proizvod(idProizvoda);
                
                retVal = new SadrzajNarudzbe(
                		narudzba,
                		proizvod,
                		rs.getInt("Kolicina")
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
	public Map<Proizvod, Integer> proizvodiIzNarudzbe(int idNarudzbe) {
	    Map<Proizvod, Integer> proizvodi = new LinkedHashMap<>(); 
	    String query = "SELECT p.IdProizvoda, p.IdPrerade, p.IdTipaProizvoda, p.Cijena, sn.Kolicina " + 
	    				"FROM sadrzaj_narudzbe sn " + 
	    				"JOIN proizvod p ON p.IdProizvoda = sn.IdProizvoda " +
	    				"JOIN tip_proizvoda tp ON tp.IdTipaProizvoda = p.IdTipaProizvoda " +
	    				"WHERE sn.IdNarudzbe = ? "; 

	    try (Connection conn = ConnectionPool.getInstance().checkOut();
	         PreparedStatement ps = conn.prepareStatement(query)) {

	        ps.setInt(1, idNarudzbe);

	        try (ResultSet rs = ps.executeQuery()) {
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
	                int kolicina = rs.getInt("Kolicina");
	                proizvodi.put(proizvod, kolicina);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return proizvodi;
	}


	@Override
	public boolean dodajSadrzajNarudzbe(SadrzajNarudzbe sadrzajNarudzbe) {
		String query = "INSERT INTO sadrzaj_narudzbe (IdNarudzbe, IdProizvoda, Kolicina) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

        	ps.setInt(1, sadrzajNarudzbe.getNarudzba().getIdNarudzbe());
        	ps.setInt(2, sadrzajNarudzbe.getProizvod().getIdProizvoda());
        	ps.setInt(3, sadrzajNarudzbe.getKolicina());
            
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
	}

	@Override
	public boolean azurirajSadrzajNarudzbe(SadrzajNarudzbe sadrzajNarudzbe) {
	    String query = "UPDATE sadrzaj_narudzbe SET Kolicina = ? WHERE IdNarudzbe = ? AND IdProizvoda = ?";

	    try (Connection conn = ConnectionPool.getInstance().checkOut();
	         PreparedStatement ps = conn.prepareStatement(query)) {

	        ps.setInt(1, sadrzajNarudzbe.getKolicina());
	        ps.setInt(2, sadrzajNarudzbe.getNarudzba().getIdNarudzbe());
	        ps.setInt(3, sadrzajNarudzbe.getProizvod().getIdProizvoda());

	        return ps.executeUpdate() == 1;

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}


	@Override
	public boolean obrisiSadrzajNarudzbe(int idNarudzbe, int idProizvoda) {
		String query = "DELETE FROM sadrzaj_narudzbe WHERE IdNarudzbe = ? AND IdProizvoda = ?";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idNarudzbe);
            ps.setInt(2, idProizvoda);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return false;
	}
	
	@Override
	public List<Proizvod> slobodniProizvodiPoTipu(TipProizvoda tip) {
	    List<Proizvod> retVal = new ArrayList<>();
	    String query = "SELECT p.IdProizvoda, p.IdPrerade, p.IdTipaProizvoda, p.Cijena " +
	                   "FROM proizvod p " +
	                   "LEFT JOIN sadrzaj_narudzbe sn ON p.IdProizvoda = sn.IdProizvoda " +
	                   "WHERE p.IdTipaProizvoda = ? AND sn.IdProizvoda IS NULL"; // da se osiguramo da nije ni u jednoj narudzbi

	    try (Connection conn = ConnectionPool.getInstance().checkOut();
	         PreparedStatement ps = conn.prepareStatement(query)) {

	        ps.setInt(1, tip.getId());
	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                int idPrerade = rs.getInt("IdPrerade");
	                PreradaMlijekaDataAccess preradaDAO = new PreradaMlijekaDataAccessImpl();
	                PreradaMlijeka prerada = preradaDAO.preradaMlijeka(idPrerade);

	                Proizvod p = new Proizvod(
	                    rs.getInt("IdProizvoda"),
	                    prerada,
	                    TipProizvoda.fromId(rs.getInt("IdTipaProizvoda")),
	                    rs.getDouble("Cijena")
	                );
	                retVal.add(p);
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return retVal;
	}


	
	
	public boolean proizvodJeSlobodan(int idProizvoda) {
	    String query = "SELECT 1 FROM sadrzaj_narudzbe WHERE IdProizvoda = ? LIMIT 1";

	    try (Connection conn = ConnectionPool.getInstance().checkOut();
	         PreparedStatement ps = conn.prepareStatement(query)) {

	        ps.setInt(1, idProizvoda);
	        try (ResultSet rs = ps.executeQuery()) {
	            return !rs.next(); // true ako se proizvod ne nalazi ni u jednoj narudzbi
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

}