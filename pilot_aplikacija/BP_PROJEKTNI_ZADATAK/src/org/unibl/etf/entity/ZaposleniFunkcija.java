package org.unibl.etf.entity;


public enum ZaposleniFunkcija {
    MUZA(1),       
    PRERADA(2),    
    ISPORUKA(3);
	
	private final int id;

	ZaposleniFunkcija(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static ZaposleniFunkcija fromId(int id) {
        for (ZaposleniFunkcija funkcija : values()) {
            if (funkcija.getId() == id) 
            	return funkcija;
        }
        throw new IllegalArgumentException("Nepoznat ID: " + id);
    }

    @Override
    public String toString() {
        switch(this) {
            case MUZA: return "muža";
            case PRERADA: return "prerada";
            case ISPORUKA: return "isporuka";
            default: return super.toString();
        }
    }
}
