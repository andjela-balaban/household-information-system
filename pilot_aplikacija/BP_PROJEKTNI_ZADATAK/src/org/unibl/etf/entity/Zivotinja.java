package org.unibl.etf.entity;

public class Zivotinja {
	private int idZivotinje;
	private String ime;
	private TipZivotinje tip;
	private Domacinstvo domacinstvo;
	
	public Zivotinja() {
		
	}
	
	public Zivotinja(int idZivotinje, String ime, TipZivotinje tip, Domacinstvo domacinstvo) {
		this.idZivotinje = idZivotinje;
		this.ime = ime;
		this.tip = tip;
		this.domacinstvo = domacinstvo;
	}
	
	public Zivotinja(String ime, TipZivotinje tip, Domacinstvo domacinstvo) {
		this.ime = ime;
		this.tip = tip;
		this.domacinstvo = domacinstvo;
	}

	public int getIdZivotinje() {
		return idZivotinje;
	}

	public void setIdZivotinje(int idZivotinje) {
		this.idZivotinje = idZivotinje;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public TipZivotinje getTip() {
		return tip;
	}

	public void setTip(TipZivotinje tip) {
		this.tip = tip;
	}

	public Domacinstvo getDomacinstvo() {
		return domacinstvo;
	}

	public void setDomacinstvo(Domacinstvo domacinstvo) {
		this.domacinstvo = domacinstvo;
	}

	@Override
    public int hashCode() {
        return Integer.hashCode(idZivotinje);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) 
        	return true;
        if (!(obj instanceof Zivotinja)) 
        	return false;
        Zivotinja zivotinja = (Zivotinja) obj;
        return idZivotinje == zivotinja.idZivotinje;
    }
	
	@Override
	public String toString() {
		return tip.toString() + " " + ime + ", iz domacinstva " + domacinstvo.getNaziv();
	}
}
