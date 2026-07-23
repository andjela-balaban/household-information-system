package org.unibl.etf.entity;

import java.time.LocalDate;

public class PreradaMlijeka {
	private int idPrerade;
	private Mlijeko mlijeko;
	private Domacinstvo domacinstvo;
	private Zaposleni zaposleni;
	private LocalDate datumPrerade;
	private double kolicinaZaPreradu;
	private JedinicaMjere mjera;
	
	public PreradaMlijeka() {
		
	}
	
	public PreradaMlijeka(int idPrerade, Mlijeko mlijeko, Domacinstvo domacinstvo, Zaposleni zaposleni, LocalDate datumPrerade, double kolicinaZaPreradu, JedinicaMjere mjera) {
		this.idPrerade = idPrerade;
		this.mlijeko = mlijeko;
		this.domacinstvo = domacinstvo;
		this.zaposleni = zaposleni;
		this.datumPrerade = datumPrerade;
		this.kolicinaZaPreradu = kolicinaZaPreradu;
		this.mjera = mjera;
	}
	
	public PreradaMlijeka(int idPrerade, Mlijeko mlijeko, Domacinstvo domacinstvo, Zaposleni zaposleni, LocalDate datumPrerade, double kolicinaZaPreradu) {
		this.idPrerade = idPrerade;
		this.mlijeko = mlijeko;
		this.domacinstvo = domacinstvo;
		this.zaposleni = zaposleni;
		this.datumPrerade = datumPrerade;
		this.kolicinaZaPreradu = kolicinaZaPreradu;
		this.mjera = JedinicaMjere.LITAR;
	}
	
	public PreradaMlijeka(Mlijeko mlijeko, Domacinstvo domacinstvo, Zaposleni zaposleni, LocalDate datumPrerade, double kolicinaZaPreradu, JedinicaMjere mjera) {
		this.mlijeko = mlijeko;
		this.domacinstvo = domacinstvo;
		this.zaposleni = zaposleni;
		this.datumPrerade = datumPrerade;
		this.kolicinaZaPreradu = kolicinaZaPreradu;
		this.mjera = mjera;
	}
	
	public PreradaMlijeka(Mlijeko mlijeko, Domacinstvo domacinstvo, Zaposleni zaposleni, LocalDate datumPrerade, double kolicinaZaPreradu) {
		this.mlijeko = mlijeko;
		this.domacinstvo = domacinstvo;
		this.zaposleni = zaposleni;
		this.datumPrerade = datumPrerade;
		this.kolicinaZaPreradu = kolicinaZaPreradu;
		this.mjera = JedinicaMjere.LITAR;
	}

	public int getIdPrerade() {
		return idPrerade;
	}

	public void setIdPrerade(int idPrerade) {
		this.idPrerade = idPrerade;
	}

	public Mlijeko getMlijeko() {
		return mlijeko;
	}

	public void setMlijeko(Mlijeko mlijeko) {
		this.mlijeko = mlijeko;
	}

	public Domacinstvo getDomacinstvo() {
		return domacinstvo;
	}

	public void setDomacinstvo(Domacinstvo domacinstvo) {
		this.domacinstvo = domacinstvo;
	}

	public Zaposleni getZaposleni() {
		return zaposleni;
	}

	public void setZaposleni(Zaposleni zaposleni) {
		this.zaposleni = zaposleni;
	}

	public LocalDate getDatumPrerade() {
		return datumPrerade;
	}

	public void setDatumPrerade(LocalDate datumPrerade) {
		this.datumPrerade = datumPrerade;
	}

	public double getKolicinaZaPreradu() {
		return kolicinaZaPreradu;
	}

	public void setKolicinaZaPreradu(double kolicinaZaPreradu) {
		this.kolicinaZaPreradu = kolicinaZaPreradu;
	}

	public JedinicaMjere getMjera() {
		return mjera;
	}

	public void setMjera(JedinicaMjere mjera) {
		this.mjera = mjera;
	}
	
	@Override
    public int hashCode() {
        return Integer.hashCode(idPrerade);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) 
        	return true;
        if (!(obj instanceof PreradaMlijeka)) 
        	return false;
        PreradaMlijeka prerada = (PreradaMlijeka) obj;
        return idPrerade == prerada.idPrerade;
    }
	
	@Override
	public String toString() {
		return "Prerada ID: " + idPrerade + 
				"\n - Mlijeko id: " + mlijeko.getIdMlijeka() +
				"\n - Kolicina mlijeka za preradu: " + kolicinaZaPreradu + " " + mjera.toString() + 
				"\n - Domacinstvo: " + domacinstvo.getNaziv();
	}
}
