package org.unibl.etf.main;

import org.unibl.etf.dao.*;
import org.unibl.etf.entity.*;
import java.sql.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws SQLException {
    	
//    	try {
//    		MjestoDataAccessImpl mjesto = new MjestoDataAccessImpl();
//    		mjesto.mjesta();
//    		for(Mjesto m : mjesto.mjesta())
//    			System.out.println(m);
//    			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

    	DataAccessFactory mysql = DataAccessFactory.getFactory(DataAccessFactoryType.MySQL);
    	
    	OsobaDataAccess osobaDAO = mysql.getOsobaDataAccess();
    	List<Osoba> osobe = osobaDAO.osobe();
    	for(Osoba o : osobe) {
    		System.out.println(o);
    		TelefonODataAccess telefonODAO = mysql.getTelefonODataAccess();
        	List<TelefonO> telefon = telefonODAO.telefoniZaOsobu(o.getIdOsobe());
        	for(TelefonO tel : telefon)
        		System.out.println(tel);
    	}
    		
    	
		System.out.println("\n------------------------------------------------------------------------\n");
    	
    	MjestoDataAccess mjestoDAO = mysql.getMjestoDataAccess();
    	List<Mjesto> mjesta = mjestoDAO.mjesta();
    	for(Mjesto m : mjesta) {
    		System.out.println(m);
    		UlicaDataAccess ulicaDAO = mysql.getUlicaDataAccess();
        	List<Ulica> ulica = ulicaDAO.uliceUMjestu(m.getPostanskiBroj());
        	for(Ulica u : ulica) {
        		System.out.println(u);
        	}
    	}
    	
    	System.out.println("\n------------------------------------------------------------------------\n");
    		
    	AdresaDataAccess adresaDAO = mysql.getAdresaDataAccess();
    	List<Adresa> adresa = adresaDAO.adrese();
    	for(Adresa a : adresa) {
    		System.out.println(a);
    	}
    	
    	System.out.println("\n------------------------------------------------------------------------\n");
//    	
    	DomacinstvoDataAccess domacinstvoDAO = mysql.getDomacinstvoDataAccess();
    	List<Domacinstvo> domacinstvo = domacinstvoDAO.svaDomacinstva();
    	for(Domacinstvo d : domacinstvo) {
    		System.out.println(d);
    		TelefonDDataAccess telefonDDAO = mysql.getTelefonDDataAccess();
        	List<TelefonD> telefon = telefonDDAO.telefoniZaDomacinstvo(d.getJib());
        	for(TelefonD tel : telefon)
        		System.out.println(tel.getBrojTelefona());
    	}
    	
		
    	System.out.println("\n------------------------------------------------------------------------\n");
    	
    	KupacDataAccess kupacDAO = mysql.getKupacDataAccess();
    	List<Kupac> kupac = kupacDAO.kupci();
    	for(Kupac k : kupac)
    		System.out.println(k);
    	
    	System.out.println("\n------------------------------------------------------------------------\n");

    	ZaposleniDataAccess zaposleniDAO = mysql.getZaposleniDataAccess();
    	List<Zaposleni> zaposleni = zaposleniDAO.sviZaposleni();
    	for(Zaposleni z : zaposleni)
    		System.out.println(z);
    	
    	System.out.println("\n------------------------------------------------------------------------\n");

    	ZivotinjaDataAccess zivotinjaDAO = mysql.getZivotinjaDataAccess();
    	List<Zivotinja> zivotinje = zivotinjaDAO.sveZivotinje();
    	for(Zivotinja z : zivotinje)
    		System.out.println(z);
//    		System.out.println(z.getIme() + " " + zivotinjaDAO.kolicinaMlijeka(z.getIdZivotinje()));

    	System.out.println("\n------------------------------------------------------------------------\n");

    	MlijekoDataAccess mlijekoDAO = mysql.getMlijekoDataAccess();
    	List<Mlijeko> mlijeko = mlijekoDAO.mlijeko();
    	for(Mlijeko m : mlijeko)
    		System.out.println(m);
    	
    	System.out.println("\n------------------------------------------------------------------------\n");

    	PreradaMlijekaDataAccess preradaDAO = mysql.getPreradaMlijekaDataAccess();
    	List<PreradaMlijeka> prerada = preradaDAO.svaPreradaMlijeka();
    	for(PreradaMlijeka pr : prerada)
    		System.out.println(pr);

    	System.out.println("\n------------------------------------------------------------------------\n");

    	ProizvodDataAccess proizvodDAO = mysql.getProizvodDataAccess();
    	List<Proizvod> proizvodi = proizvodDAO.sviProizvodi();
    	for(Proizvod p : proizvodi)
    		System.out.println(p);
    	
    	System.out.println("\n------------------------------------------------------------------------\n");

    	PlacanjeDataAccess placanjeDAO = mysql.getPlacanjeDataAccess();
    	List<Placanje> placanje = placanjeDAO.svaPlacanja();
    	for(Placanje p : placanje)
    		System.out.println(p);
    	

    	System.out.println("\n------------------------------------------------------------------------\n");

    	IsporukaDataAccess isporukaDAO = mysql.getIsporukaDataAccess();
    	List<Isporuka> isporuke = isporukaDAO.isporuke();
    	for(Isporuka i : isporuke)
    		System.out.println(i);
    	
    	System.out.println("\n------------------------------------------------------------------------\n");

    	NarudzbaDataAccess narudzbaDAO = mysql.getNarudzbaDataAccess();
    	List<Narudzba> narudzbe = narudzbaDAO.narudzbe();
    	for(Narudzba n : narudzbe)
    		System.out.println(n);

    	System.out.println("\n------------------------------------------------------------------------\n");

    	
    	SadrzajNarudzbeDataAccess sadrzajDAO = mysql.getSadrzajNarudzbeDataAccess();
    	Map<Proizvod, Integer> proizvodiIzNarudzbe = sadrzajDAO.proizvodiIzNarudzbe(7);
    	for (Map.Entry<Proizvod, Integer> entry : proizvodiIzNarudzbe.entrySet()) {
    	    System.out.println(entry.getKey().getIdProizvoda() + " -> " + entry.getValue());
    	}

    }
}
