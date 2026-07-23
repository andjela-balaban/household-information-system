package org.unibl.etf.entity;

public class Osoba {
    private int idOsobe;
    private String ime;
    private String prezime;
    private Adresa adresa;

    public Osoba() {
    
    }

    public Osoba(int idOsobe, String ime, String prezime, Adresa adresa) {
        this.idOsobe = idOsobe;
        this.ime = ime;
        this.prezime = prezime;
        this.adresa = adresa;
    }
    
    public Osoba(int idOsobe, String ime, String prezime) {
        this.idOsobe = idOsobe;
        this.ime = ime;
        this.prezime = prezime;
    }
    
    public Osoba(String ime, String prezime, Adresa adresa) {
        this.ime = ime;
        this.prezime = prezime;
        this.adresa = adresa;
    }
    
    public Osoba(String ime, String prezime) {
        this.ime = ime;
        this.prezime = prezime;
    }

    public int getIdOsobe() {
        return idOsobe;
    }

    public void setIdOsobe(int idOsobe) {
        this.idOsobe = idOsobe;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public Adresa getAdresa() {
        return adresa;
    }

    public void setAdresa(Adresa adresa) {
        this.adresa = adresa;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(idOsobe);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) 
        	return true;
        if (!(obj instanceof Osoba)) 
        	return false;
        Osoba osoba = (Osoba) obj;
        return idOsobe == osoba.idOsobe;
    }

    @Override
    public String toString() {
        return "idOsobe: " + idOsobe +
                "\nIme: " + ime +
                "\nPrezime: " + prezime +
                "\nAdresa: " + (adresa != null ? adresa.toString() : "null");
    }
}
