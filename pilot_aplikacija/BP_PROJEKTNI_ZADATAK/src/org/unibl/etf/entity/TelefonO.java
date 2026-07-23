package org.unibl.etf.entity;

public class TelefonO {
    private Osoba osoba;
    private String brojTelefona;

    public TelefonO() {
    }

    public TelefonO(Osoba osoba, String brojTelefona) {
        this.osoba = osoba;
        this.brojTelefona = brojTelefona;
    }

    public Osoba getOsoba() {
        return osoba;
    }

    public void setOsoba(Osoba osoba) {
        this.osoba = osoba;
    }

    public String getBrojTelefona() {
        return brojTelefona;
    }

    public void setBrojTelefona(String brojTelefona) {
        this.brojTelefona = brojTelefona;
    }

    @Override
    public int hashCode() {
        int result = osoba.hashCode();
        result = 31 * result + (brojTelefona != null ? brojTelefona.hashCode() : 0);
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) 
        	return true;
        if (!(obj instanceof TelefonO)) 
        	return false;
        TelefonO telefon = (TelefonO) obj;
        if (!osoba.equals(telefon.osoba)) 
        	return false;
        return brojTelefona.equals(telefon.brojTelefona);
    }

    @Override
    public String toString() {
        return "Telefon: " + (brojTelefona != null ? brojTelefona : "Nema") + (brojTelefona != null ? ", osoba: " + osoba.toString() : "");
    }
}