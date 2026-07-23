package org.unibl.etf.entity;

public class Kupac {
    private Osoba osoba; 

    public Kupac() {
    	
    }

    public Kupac(Osoba osoba) {
        this.osoba = osoba;
    }

    public Osoba getOsoba() {
        return osoba;
    }

    public void setOsoba(Osoba osoba) {
        this.osoba = osoba;
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
        Kupac kupac = (Kupac) obj;
        return osoba != null && osoba.equals(kupac.osoba);
    }

    @Override
    public String toString() {
        return "KUPAC \n" + osoba.toString();
    }
}
