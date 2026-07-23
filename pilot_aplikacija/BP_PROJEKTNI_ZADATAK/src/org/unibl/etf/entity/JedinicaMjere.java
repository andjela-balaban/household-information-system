package org.unibl.etf.entity;

public enum JedinicaMjere {
	LITAR(1),
	KILOGRAM(2),
	KOMAD(3);
	
	private final int id;

	JedinicaMjere(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static JedinicaMjere fromId(int id) {
        for (JedinicaMjere tip : values()) {
            if (tip.getId() == id) 
            	return tip;
        }
        throw new IllegalArgumentException("Nepoznat ID: " + id);
    }
	
	@Override
    public String toString() {
        switch(this) {
            case LITAR: return "l";
            case KILOGRAM: return "kg";
            case KOMAD: return "kom";
            default: return super.toString();
        }
    }
}