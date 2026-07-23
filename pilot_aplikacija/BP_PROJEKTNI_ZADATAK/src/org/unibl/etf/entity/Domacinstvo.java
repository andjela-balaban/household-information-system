package org.unibl.etf.entity;

public class Domacinstvo {
    private String jib;          
    private String naziv;
    private Adresa adresa;       
    private Osoba kontaktOsoba;

    public Domacinstvo() {
    }

    public Domacinstvo(String jib, String naziv, Adresa adresa, Osoba kontaktOsoba) {
        this.jib = jib;
        this.naziv = naziv;
        this.adresa = adresa;
        this.kontaktOsoba = kontaktOsoba;
    }

    public String getJib() {
        return jib;
    }

    public void setJib(String jib) {
        this.jib = jib;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Adresa getAdresa() {
        return adresa;
    }

    public void setAdresa(Adresa adresa) {
        this.adresa = adresa;
    }

    public Osoba getKontaktOsoba() {
        return kontaktOsoba;
    }

    public void setKontaktOsoba(Osoba kontaktOsoba) {
        this.kontaktOsoba = kontaktOsoba;
    }

	@Override
    public int hashCode() {
        return jib != null ? jib.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) 
        	return true;
        if (obj == null || getClass() != obj.getClass()) 
        	return false;
        Domacinstvo other = (Domacinstvo) obj;
        return jib != null && jib.equals(other.jib);
    }
    
    @Override
    public String toString() {
        return "Domacinstvo: " + naziv + ", JIB: " + jib + ", Adresa: " + (adresa != null ? adresa.toString() : "Nema") +
               ", Kontakt osoba: " + (kontaktOsoba != null ? kontaktOsoba.toString() : "Nema");
    }
}
