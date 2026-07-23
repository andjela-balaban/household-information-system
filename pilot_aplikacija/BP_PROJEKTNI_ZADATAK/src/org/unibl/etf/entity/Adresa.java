package org.unibl.etf.entity;

public class Adresa {
    private int idAdrese;
    private Ulica ulica; 
    private int kucniBroj;

    public Adresa() {
    	
    }

    public Adresa(int idAdrese, Ulica ulica, int kucniBroj) {
        this.idAdrese = idAdrese;
        this.ulica = ulica;
        this.kucniBroj = kucniBroj;
    }
    
    public Adresa(Ulica ulica, int kucniBroj) {
        this.ulica = ulica;
        this.kucniBroj = kucniBroj;
    }

    public int getIdAdrese() {
        return idAdrese;
    }

    public void setIdAdrese(int idAdrese) {
        this.idAdrese = idAdrese;
    }

    public Ulica getUlica() {
        return ulica;
    }

    public void setUlica(Ulica ulica) {
        this.ulica = ulica;
    }

    public int getKucniBroj() {
        return kucniBroj;
    }

    public void setKucniBroj(int kucniBroj) {
        this.kucniBroj = kucniBroj;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idAdrese);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) 
        	return true;
        if (obj == null || getClass() != obj.getClass()) 
        	return false;
        Adresa adresa = (Adresa) obj;
        return idAdrese == adresa.idAdrese;
    }

    @Override
    public String toString() {
        return ulica.toString() + " " + kucniBroj;
    }
}
