package org.unibl.etf.entity;

import java.time.LocalDate;

public class Mlijeko {
	private int idMlijeka;
	private LocalDate datumMuze;
	private double kolicina;
	private JedinicaMjere mjera;
	private int kvalitet;
	private Zivotinja zivotinja;
	
	public Mlijeko() {
		
	}
	
	public Mlijeko(int idMlijeka, LocalDate datumMuze, double kolicina, JedinicaMjere mjera, int kvalitet, Zivotinja zivotinja) {
		this.idMlijeka = idMlijeka;
		this.datumMuze = datumMuze;
		this.kolicina = kolicina;
		this.mjera = mjera;
		this.kvalitet = kvalitet;
		this.zivotinja = zivotinja;
	}
	
	public Mlijeko(int idMlijeka, LocalDate datumMuze, double kolicina, int kvalitet, Zivotinja zivotinja) {
		this.idMlijeka = idMlijeka;
		this.datumMuze = datumMuze;
		this.kolicina = kolicina;
		this.mjera = JedinicaMjere.LITAR;
		this.kvalitet = kvalitet;
		this.zivotinja = zivotinja;
	}
	
	public Mlijeko(LocalDate datumMuze, double kolicina, JedinicaMjere mjera, int kvalitet, Zivotinja zivotinja) {
		this.datumMuze = datumMuze;
		this.kolicina = kolicina;
		this.mjera = mjera;
		this.kvalitet = kvalitet;
		this.zivotinja = zivotinja;
	}
	
	public Mlijeko(LocalDate datumMuze, double kolicina, int kvalitet, Zivotinja zivotinja) {
		this.datumMuze = datumMuze;
		this.kolicina = kolicina;
		this.mjera = JedinicaMjere.LITAR;
		this.kvalitet = kvalitet;
		this.zivotinja = zivotinja;
	}

	public int getIdMlijeka() {
		return idMlijeka;
	}

	public void setIdMlijeka(int idMlijeka) {
		this.idMlijeka = idMlijeka;
	}

	public LocalDate getDatumMuze() {
		return datumMuze;
	}

	public void setDatumMuze(LocalDate datumMuze) {
		this.datumMuze = datumMuze;
	}

	public double getKolicina() {
		return kolicina;
	}

	public void setKolicina(double kolicina) {
		this.kolicina = kolicina;
	}

	public JedinicaMjere getMjera() {
		return mjera;
	}

	public void setMjera(JedinicaMjere mjera) {
		this.mjera = mjera;
	}

	public int getKvalitet() {
		return kvalitet;
	}

	public void setKvalitet(int kvalitet) {
		this.kvalitet = kvalitet;
	}

	public Zivotinja getZivotinja() {
		return zivotinja;
	}

	public void setZivotinja(Zivotinja zivotinja) {
		this.zivotinja = zivotinja;
	}
	
	@Override
    public int hashCode() {
        return Integer.hashCode(idMlijeka);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) 
        	return true;
        if (!(obj instanceof Mlijeko)) 
        	return false;
        Mlijeko mlijeko = (Mlijeko) obj;
        return idMlijeka == mlijeko.idMlijeka;
    }
	
	@Override
	public String toString() {
		return "Mlijeko ID: " + idMlijeka + 
				"\n - Datum muže: " + datumMuze +
				"\n - Kolicina: " + kolicina + " " + mjera.toString() + 
				"\n - Kvalitet: " + kvalitet + 
				"\n - " + zivotinja.toString();
	}
}
