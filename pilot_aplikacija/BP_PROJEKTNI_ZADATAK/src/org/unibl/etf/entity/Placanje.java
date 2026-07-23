package org.unibl.etf.entity;

public class Placanje {
	private int idPlacanja;
	private Kupac kupac;
	private double iznos;
	private TipPlacanja tipPlacanja;
	
	public Placanje() {
		
	}
	
	public Placanje(int idPlacanja, Kupac kupac, double iznos, TipPlacanja tipPlacanja) {
		this.idPlacanja = idPlacanja;
		this.kupac = kupac;
		this.iznos = iznos;
		this.tipPlacanja = tipPlacanja;
	}
	
	public Placanje(Kupac kupac, double iznos, TipPlacanja tipPlacanja) {
		this.kupac = kupac;
		this.iznos = iznos;
		this.tipPlacanja = tipPlacanja;
	}

	public int getIdPlacanja() {
		return idPlacanja;
	}

	public void setIdPlacanja(int idPlacanja) {
		this.idPlacanja = idPlacanja;
	}

	public Kupac getKupac() {
		return kupac;
	}

	public void setKupac(Kupac kupac) {
		this.kupac = kupac;
	}

	public double getIznos() {
		return iznos;
	}

	public void setIznos(double iznos) {
		this.iznos = iznos;
	}

	public TipPlacanja getTipPlacanja() {
		return tipPlacanja;
	}

	public void setTipPlacanja(TipPlacanja tipPlacanja) {
		this.tipPlacanja = tipPlacanja;
	}
	
	@Override
    public int hashCode() {
        return Integer.hashCode(idPlacanja);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) 
        	return true;
        if (!(obj instanceof Placanje)) 
        	return false;
        Placanje placanje = (Placanje) obj;
        return idPlacanja == placanje.idPlacanja;
    }
	
	@Override
	public String toString() {
		return "Placanje ID: " + idPlacanja + 
				"\n - Kupac: " + kupac.getOsoba().getIme() + " " + kupac.getOsoba().getPrezime() +
				"\n - Iznos: " + iznos + 
				"\n - Tip placanja: " + tipPlacanja;
	}
	
}
