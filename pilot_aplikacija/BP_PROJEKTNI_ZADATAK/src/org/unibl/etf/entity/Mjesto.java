package org.unibl.etf.entity;

public class Mjesto {
	private int postanskiBroj;
	private String naziv;

	public Mjesto() {
	}

	public Mjesto(int postanskiBroj, String naziv) {
		super();
		this.postanskiBroj = postanskiBroj;
		this.naziv = naziv;
	}

	public int getPostanskiBroj() {
		return postanskiBroj;
	}

	public void setPostanskiBroj(int postanskiBroj) {
		this.postanskiBroj = postanskiBroj;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + postanskiBroj;
	    return result;
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj) 
	    	return true;
        if (obj == null || getClass() != obj.getClass()) 
        	return false;
        Mjesto mjesto = (Mjesto) obj;
        return postanskiBroj == mjesto.postanskiBroj;
	}


	@Override
	public String toString() {
		return postanskiBroj + " " + naziv;
	}
}