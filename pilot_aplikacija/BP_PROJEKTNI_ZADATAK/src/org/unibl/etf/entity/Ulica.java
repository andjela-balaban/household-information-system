package org.unibl.etf.entity;

public class Ulica {
	private String nazivUlice;
	private Mjesto mjesto;

	public Ulica() {
	}

	public Ulica(String nazivUlice, Mjesto mjesto) {
		this.nazivUlice = nazivUlice;
		this.mjesto = mjesto;
	}

	public String getNazivUlice() {
		return nazivUlice;
	}

	public void setNazivUlice(String nazivUlice) {
		this.nazivUlice = nazivUlice;
	}

	public Mjesto getMjesto() {
		return mjesto;
	}

	public void setMjesto(Mjesto mjesto) {
		this.mjesto = mjesto;
	}

	@Override
    public int hashCode() {
        return mjesto.hashCode() + nazivUlice.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) 
        	return true;
        if (obj == null || getClass() != obj.getClass()) 
        	return false;
        Ulica ulica = (Ulica) obj;
        return this.nazivUlice.equals(ulica.nazivUlice) && this.mjesto.equals(ulica.mjesto);
    }

    @Override
    public String toString() {
        return mjesto.toString() + ", " + nazivUlice;
    }
}