package org.unibl.etf.dao.mysql;

import java.sql.*;
import java.util.*;
import org.unibl.etf.dao.*;
import org.unibl.etf.entity.*;

public class DomacinstvoDataAccessImpl implements DomacinstvoDataAccess {

	@Override
	public Domacinstvo domacinstvo(String jib) {
	    Domacinstvo retVal = null;
	    Connection conn = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    String query = "SELECT d.JIB, d.Naziv, d.IdAdrese, a.KucniBroj, u.NazivUlice, m.PostanskiBroj, " +
	                   "m.NazivMjesta, d.IdKontaktOsobe, o.Ime, o.Prezime, t.BrojTelefona " +
	                   "FROM domacinstvo d " +
	                   "JOIN adresa a ON d.IdAdrese = a.IdAdrese " +
	                   "JOIN ulica u ON a.NazivUlice = u.NazivUlice AND a.PostanskiBroj = u.PostanskiBroj " +
	                   "JOIN mjesto m ON u.PostanskiBroj = m.PostanskiBroj " +
	                   "LEFT JOIN telefon_d t ON t.JIB = d.JIB " +
	                   "JOIN osoba o ON d.IdKontaktOsobe = o.IdOsobe " +
	                   "WHERE d.JIB = ?";

	    try {
	        conn = ConnectionPool.getInstance().checkOut();
	        ps = conn.prepareStatement(query);
	        ps.setString(1, jib);
	        rs = ps.executeQuery();

	        while (rs.next()) {
	            if (retVal == null) {
	                Mjesto mjesto = new Mjesto(rs.getInt("PostanskiBroj"), rs.getString("NazivMjesta"));
	                Ulica ulica = new Ulica(rs.getString("NazivUlice"), mjesto);
	                Adresa adresa = new Adresa(rs.getInt("IdAdrese"), ulica, rs.getInt("KucniBroj"));
	                
	                OsobaDataAccess osobaDAO = new OsobaDataAccessImpl();
	                int kontaktId = rs.getInt("IdKontaktOsobe");
	                Osoba kontakt = null;
	                if (!rs.wasNull()) {
	                    kontakt = osobaDAO.osoba(kontaktId);
	                }

	                retVal = new Domacinstvo(
	                    rs.getString("JIB"),
	                    rs.getString("Naziv"),
	                    adresa,
	                    kontakt
	                );
	            }
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
	public List<Domacinstvo> svaDomacinstva() {
	    Map<String, Domacinstvo> domacinstvaMap = new LinkedHashMap<>();

	    String query = "SELECT d.JIB, d.Naziv, d.IdAdrese, a.KucniBroj, u.NazivUlice, m.PostanskiBroj, " +
	                   "m.NazivMjesta, d.IdKontaktOsobe, o.Ime, o.Prezime, t.BrojTelefona " +
	                   "FROM domacinstvo d " +
	                   "JOIN adresa a ON d.IdAdrese = a.IdAdrese " +
	                   "JOIN ulica u ON a.NazivUlice = u.NazivUlice AND a.PostanskiBroj = u.PostanskiBroj " +
	                   "JOIN mjesto m ON u.PostanskiBroj = m.PostanskiBroj " +
	                   "LEFT JOIN telefon_d t ON t.JIB = d.JIB " +
	                   "JOIN osoba o ON d.IdKontaktOsobe = o.IdOsobe ";

	    try (Connection conn = ConnectionPool.getInstance().checkOut();
	         PreparedStatement ps = conn.prepareStatement(query);
	         ResultSet rs = ps.executeQuery()) {

	        while (rs.next()) {
	            String jib = rs.getString("JIB");
	            Domacinstvo d = domacinstvaMap.get(jib);
	            if (d == null) {
	                Mjesto mjesto = new Mjesto(rs.getInt("PostanskiBroj"), rs.getString("NazivMjesta"));
	                Ulica ulica = new Ulica(rs.getString("NazivUlice"), mjesto);
	                Adresa adresa = new Adresa(rs.getInt("IdAdrese"), ulica, rs.getInt("KucniBroj"));
	                
	                OsobaDataAccess osobaDAO = new OsobaDataAccessImpl();
	                int kontaktId = rs.getInt("IdKontaktOsobe");
	                Osoba kontakt = null;
	                if (!rs.wasNull()) {
	                    kontakt = osobaDAO.osoba(kontaktId);
	                }

	                d = new Domacinstvo(
	                    jib,
	                    rs.getString("Naziv"),
	                    adresa,
	                    kontakt
	                );

	                domacinstvaMap.put(jib, d);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return new ArrayList<>(domacinstvaMap.values());
	}


    @Override
    public boolean dodajDomacinstvo(Domacinstvo d) {
        String query = "INSERT INTO domacinstvo (JIB, Naziv, IdAdrese, IdKontaktOsobe) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, d.getJib());
            ps.setString(2, d.getNaziv());
            ps.setInt(3, d.getAdresa().getIdAdrese());

            if (d.getKontaktOsoba() != null)
                ps.setInt(4, d.getKontaktOsoba().getIdOsobe());
            else
                ps.setNull(4, Types.INTEGER);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean azurirajDomacinstvo(Domacinstvo d) {
        String query = "UPDATE domacinstvo SET Naziv = ?, IdAdrese = ?, IdKontaktOsobe = ? WHERE JIB = ?";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, d.getNaziv());
            ps.setInt(2, d.getAdresa().getIdAdrese());

            if (d.getKontaktOsoba() != null)
                ps.setInt(3, d.getKontaktOsoba().getIdOsobe());
            else
                ps.setNull(3, Types.INTEGER);

            ps.setString(4, d.getJib());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean obrisiDomacinstvo(String jib) {
        String query = "DELETE FROM domacinstvo WHERE JIB = ?";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, jib);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
        	e.printStackTrace();
        }

        return false;
    }
    
    @Override
    public double izracunajDobit(int idStatusaNarudzbe, String jib) {
        double dobit = 0;
        String query = "{call ukupna_dobit_domacinstva(?, ?, ?)}";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             CallableStatement cs = conn.prepareCall(query)) {

            cs.setInt(1, idStatusaNarudzbe); 
            cs.setString(2, jib);            
            cs.registerOutParameter(3, Types.DOUBLE);

            cs.execute();

            dobit = cs.getDouble(3);     

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dobit;
    }
    
    @Override
    public List<TopProizvodDTO> topProizvodi(String jib, int limit) {
        List<TopProizvodDTO> lista = new ArrayList<>();

        String call = "{call top_proizvodi_domacinstva(?, ?)}";

        try (Connection conn = ConnectionPool.getInstance().checkOut();
             CallableStatement cs = conn.prepareCall(call)) {

            cs.setString(1, jib);
            cs.setInt(2, limit);

            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                String naziv = rs.getString("Proizvod");
                int ukupno = rs.getInt("UkupnoKomada");
                double zarada = rs.getDouble("UkupnaZarada");

                lista.add(new TopProizvodDTO(naziv, ukupno, zarada));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }


}
