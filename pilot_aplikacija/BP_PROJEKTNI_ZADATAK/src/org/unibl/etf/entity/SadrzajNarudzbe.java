package org.unibl.etf.entity;

public class SadrzajNarudzbe {
	private Narudzba narudzba;
	private Proizvod proizvod;
	private int kolicina;
	
	public SadrzajNarudzbe() {
		
	}
	
	public SadrzajNarudzbe(Narudzba narudzba, Proizvod proizvod, int kolicina) {
		this.narudzba = narudzba;
		this.proizvod = proizvod;
		this.kolicina = kolicina;
	}

	public Narudzba getNarudzba() {
		return narudzba;
	}

	public void setNarudzba(Narudzba narudzba) {
		this.narudzba = narudzba;
	}

	public Proizvod getProizvod() {
		return proizvod;
	}

	public void setProizvod(Proizvod proizvod) {
		this.proizvod = proizvod;
	}
	
	public int getKolicina() {
		return kolicina;
	}
	
	public void setKolicina(int kolicina) {
		this.kolicina = kolicina;
	}

	@Override
    public int hashCode() {
        return narudzba.hashCode() + proizvod.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) 
        	return true;
        if (!(obj instanceof SadrzajNarudzbe)) 
        	return false;
        SadrzajNarudzbe sadrzajNarudzbe = (SadrzajNarudzbe) obj;
        return narudzba.getIdNarudzbe() == sadrzajNarudzbe.getNarudzba().getIdNarudzbe() && 
        		proizvod.getIdProizvoda() == sadrzajNarudzbe.getProizvod().getIdProizvoda();
    }
	
//	@Override
//	public String toString() {
//		return  "SADRZAJ NARUDZBE" +
//				"\n - Narudzba ID: " + narudzba.getIdNarudzbe() + 
//				"\n - Proizvod ID: " + proizvod.getIdProizvoda() +
//				"\n - Proizvod: " + proizvod.getTip() +
//				"\n - Kolicina: " + kolicina;
//	}
    
    @Override
	public String toString() {
		return  " - Proizvod ID: " + proizvod.getIdProizvoda() +
				"\n - Proizvod: " + proizvod.getTip() +
				"\n - Kolicina: " + kolicina + "\n";
	}
}
