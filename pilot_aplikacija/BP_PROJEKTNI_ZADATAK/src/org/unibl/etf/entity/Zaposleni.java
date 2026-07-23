package org.unibl.etf.entity;

public class Zaposleni {
    private Osoba osoba; 
    private Domacinstvo domacinstvo;
    private ZaposleniFunkcija funkcija;
    private double plata;

    public Zaposleni() {
    	
    }

    public Zaposleni(Osoba osoba, Domacinstvo domacinstvo, ZaposleniFunkcija funkcija, double plata) {
        this.osoba = osoba;
        this.domacinstvo = domacinstvo;
        this.funkcija = funkcija;
        this.plata = plata;
    }

    public Osoba getOsoba() {
        return osoba;
    }

    public void setOsoba(Osoba osoba) {
        this.osoba = osoba;
    }
    
    public Domacinstvo getDomacinstvo() {
    	return domacinstvo;
    }
    
    public void setDomacinstvo(Domacinstvo domacinstvo) {
    	this.domacinstvo = domacinstvo;
    }
    
    public ZaposleniFunkcija getFunkcija() {
    	return this.funkcija;
    }
    
    public void setFunkcija(ZaposleniFunkcija funkcija) {
    	this.funkcija = funkcija;
    }

    public double getPlata() {
    	return plata;
    }
    
    public void setPlata(double plata) {
    	this.plata = plata;
    }
    
    @Override
    public int hashCode() {
        return osoba != null ? osoba.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) 
        	return true;
        if (obj == null || getClass() != obj.getClass()) 
        	return false;
        Zaposleni zaposleni = (Zaposleni) obj;
        return osoba != null && osoba.equals(zaposleni.osoba);
    }

    @Override
    public String toString() {
        return "ZAPOSLENI - " + funkcija + "\n" + osoba.toString() +
        		"\n- Plata: " + plata + "KM";
    }
}
