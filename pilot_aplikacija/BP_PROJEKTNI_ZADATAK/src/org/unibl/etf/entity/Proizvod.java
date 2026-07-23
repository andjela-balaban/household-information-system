package org.unibl.etf.entity;

public class Proizvod {
	private int idProizvoda;
	private PreradaMlijeka prerada;
	private TipProizvoda tip;
	private double cijena;
	
	public Proizvod() {
		
	}
	
	public Proizvod(int idProizvoda, PreradaMlijeka prerada, TipProizvoda tip, double cijena) {
		this.idProizvoda = idProizvoda;
		this.prerada = prerada;
		this.tip = tip;
		this.cijena = cijena;
	}
	
	public Proizvod(PreradaMlijeka prerada, TipProizvoda tip, double cijena) {
		this.prerada = prerada;
		this.tip = tip;
		this.cijena = cijena;
	}
	
	public int getIdProizvoda() {
		return idProizvoda;
	}

	public void setIdProizvoda(int idProizvoda) {
		this.idProizvoda = idProizvoda;
	}

	public PreradaMlijeka getPrerada() {
		return prerada;
	}

	public void setPrerada(PreradaMlijeka prerada) {
		this.prerada = prerada;
	}

	public TipProizvoda getTip() {
		return tip;
	}

	public void setTip(TipProizvoda tip) {
		this.tip = tip;
	}

	public double getCijena() {
		return cijena;
	}

	public void setCijena(double cijena) {
		this.cijena = cijena;
	}

	@Override
    public int hashCode() {
        return Integer.hashCode(idProizvoda);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) 
        	return true;
        if (!(obj instanceof Proizvod)) 
        	return false;
        Proizvod proizvod = (Proizvod) obj;
        return idProizvoda == proizvod.idProizvoda;
    }
	
	@Override
	public String toString() {
		return "Proizvod ID: " + idProizvoda + 
				"\n - Tip proizvoda: " + tip +
				"\n - Prerada: " + prerada.getIdPrerade() + 
				"\n - Cijena: " + cijena;
	}
}
