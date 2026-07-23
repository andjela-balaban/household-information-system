package org.unibl.etf.entity;

import java.time.LocalDate;

public class Narudzba {
	private int idNarudzbe;
	private StatusNarudzbe status;
	private LocalDate datumNarudzbe;
	private Kupac kupac;
	private Placanje placanje;
	private Isporuka isporuka;
	
	public Narudzba() {
		
	}
	
	public Narudzba(int idNarudzbe, StatusNarudzbe status, LocalDate datumNarudzbe, Kupac kupac, Placanje placanje, Isporuka isporuka) {
		this.idNarudzbe = idNarudzbe;
		this.status = status;
		this.datumNarudzbe = datumNarudzbe;
		this.kupac = kupac;
		this.placanje = placanje;
		this.isporuka = isporuka;
	}
	
	public Narudzba(StatusNarudzbe status, LocalDate datumNarudzbe, Kupac kupac, Placanje placanje, Isporuka isporuka) {
		this.status = status;
		this.datumNarudzbe = datumNarudzbe;
		this.kupac = kupac;
		this.placanje = placanje;
		this.isporuka = isporuka;
	}

	public int getIdNarudzbe() {
		return idNarudzbe;
	}

	public void setIdNarudzbe(int idNarudzbe) {
		this.idNarudzbe = idNarudzbe;
	}

	public StatusNarudzbe getStatus() {
		return status;
	}

	public void setStatus(StatusNarudzbe status) {
		this.status = status;
	}

	public LocalDate getDatumNarudzbe() {
		return datumNarudzbe;
	}

	public void setDatumNarudzbe(LocalDate datumNarudzbe) {
		this.datumNarudzbe = datumNarudzbe;
	}

	public Kupac getKupac() {
		return kupac;
	}

	public void setKupac(Kupac kupac) {
		this.kupac = kupac;
	}

	public Placanje getPlacanje() {
		return placanje;
	}

	public void setPlacanje(Placanje placanje) {
		this.placanje = placanje;
	}

	public Isporuka getIsporuka() {
		return isporuka;
	}

	public void setIsporuka(Isporuka isporuka) {
		this.isporuka = isporuka;
	}
	
	@Override
    public int hashCode() {
        return Integer.hashCode(idNarudzbe);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) 
        	return true;
        if (!(obj instanceof Narudzba)) 
        	return false;
        Narudzba narudzba = (Narudzba) obj;
        return idNarudzbe == narudzba.idNarudzbe;
    }
	
	@Override
	public String toString() {
		return "Narudzba ID: " + idNarudzbe + 
				"\n - Status narudzbe: " + status +
				"\n - Datum narudzbe: " + datumNarudzbe.toString() +
				"\n - Kupac: " + kupac.getOsoba().getIme() + " " + kupac.getOsoba().getPrezime() +
				"\n - Placanje ID: " + placanje.getIdPlacanja() + ", iznos: " + placanje.getIznos() + "KM" +
				"\n - Isporuka ID: " + (isporuka == null ? "nema isporuke" : isporuka.getIdIsporuke());
	}
}
