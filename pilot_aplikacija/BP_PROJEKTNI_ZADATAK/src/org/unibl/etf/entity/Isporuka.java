package org.unibl.etf.entity;

import java.time.LocalDate;

public class Isporuka {
	private int idIsporuke;
	private LocalDate datumIsporuke;
	private Adresa adresa;
	private Kupac kupac;
	private Zaposleni isporucilac;
	
	public Isporuka() {
		
	}
	
	public Isporuka(int idIsporuke, LocalDate datumIsporuke, Adresa adresa, Kupac kupac, Zaposleni zaposleniIsporucilac) {
		this.idIsporuke = idIsporuke;
		this.datumIsporuke = datumIsporuke;
		this.adresa = adresa;
		this.kupac = kupac;
		this.isporucilac = zaposleniIsporucilac;
	}
	
	public Isporuka(LocalDate datumIsporuke, Adresa adresa, Kupac kupac, Zaposleni zaposleniIsporucilac) {
		this.datumIsporuke = datumIsporuke;
		this.adresa = adresa;
		this.kupac = kupac;
		this.isporucilac = zaposleniIsporucilac;
	}

	public int getIdIsporuke() {
		return idIsporuke;
	}

	public void setIdIsporuke(int idIsporuke) {
		this.idIsporuke = idIsporuke;
	}

	public LocalDate getDatumIsporuke() {
		return datumIsporuke;
	}

	public void setDatumIsporuke(LocalDate datumIsporuke) {
		this.datumIsporuke = datumIsporuke;
	}

	public Adresa getAdresa() {
		return adresa;
	}

	public void setAdresa(Adresa adresa) {
		this.adresa = adresa;
	}

	public Kupac getKupac() {
		return kupac;
	}

	public void setKupac(Kupac kupac) {
		this.kupac = kupac;
	}

	public Zaposleni getZaposleniIsporucilac() {
		return isporucilac;
	}

	public void setZaposleniIsporucilac(Zaposleni zaposleniIsporucilac) {
		this.isporucilac = zaposleniIsporucilac;
	}
	
	@Override
    public int hashCode() {
        return Integer.hashCode(idIsporuke);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) 
        	return true;
        if (!(obj instanceof Isporuka)) 
        	return false;
        Isporuka isporuka = (Isporuka) obj;
        return idIsporuke == isporuka.idIsporuke;
    }
	
	@Override
	public String toString() {
		return "Isporuka ID: " + idIsporuke + 
				"\n - Kupac: " + kupac.getOsoba().getIme() + " " + kupac.getOsoba().getPrezime() +
				"\n - Isporucilac: " + isporucilac.getOsoba().getIme() + " " + isporucilac.getOsoba().getPrezime() + 
				"\n - Adresa: " + adresa.toString();
	}
}
