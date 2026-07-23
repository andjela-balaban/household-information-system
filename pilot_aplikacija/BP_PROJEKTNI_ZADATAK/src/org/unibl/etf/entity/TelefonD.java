package org.unibl.etf.entity;

public class TelefonD {
    private Domacinstvo domacinstvo;
    private String brojTelefona;

    public TelefonD() {
    }

    public TelefonD(Domacinstvo domacinstvo, String brojTelefona) {
        this.domacinstvo = domacinstvo;
        this.brojTelefona = brojTelefona;
    }

    public Domacinstvo getDomacinstvo() {
        return domacinstvo;
    }

    public void setDomacinstvo(Domacinstvo domacinstvo) {
        this.domacinstvo = domacinstvo;
    }

    public String getBrojTelefona() {
        return brojTelefona;
    }

    public void setBrojTelefona(String brojTelefona) {
        this.brojTelefona = brojTelefona;
    }

    @Override
    public int hashCode() {
        int result = domacinstvo != null ? domacinstvo.hashCode() : 0;
        result = 31 * result + (brojTelefona != null ? brojTelefona.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) 
        	return true;
        if(obj == null || getClass() != obj.getClass()) 
        	return false;
        TelefonD telefon = (TelefonD) obj;
        if(domacinstvo == null || telefon.domacinstvo == null) 
        	return false;
        if(!domacinstvo.equals(telefon.domacinstvo)) 
        	return false;
        return brojTelefona != null && brojTelefona.equals(telefon.brojTelefona);
    }
    
    @Override
    public String toString() {
        return "Telefon: " + (brojTelefona != null ? brojTelefona : "-") +
               ", domacinstvo: " + (domacinstvo != null ? domacinstvo.getNaziv() : "Nema");
    }

}