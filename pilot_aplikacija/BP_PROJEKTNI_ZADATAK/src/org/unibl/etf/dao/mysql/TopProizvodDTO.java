package org.unibl.etf.dao.mysql;

public class TopProizvodDTO {
    private String naziv;
    private int ukupnoKomada;
    private double zarada;

    public TopProizvodDTO(String naziv, int ukupnoKomada, double zarada) {
        this.naziv = naziv;
        this.ukupnoKomada = ukupnoKomada;
        this.zarada = zarada;
    }
    public String getNaziv() { 
    	return naziv; 
    }
    
    public int getUkupnoKomada() {
    	return ukupnoKomada; 
    }
    
    public double getZarada() { 
    	return zarada; 
    }
}
