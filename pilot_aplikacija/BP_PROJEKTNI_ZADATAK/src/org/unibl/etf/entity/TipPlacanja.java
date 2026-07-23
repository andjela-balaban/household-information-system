package org.unibl.etf.entity;

public enum TipPlacanja {
    GOTOVINA(1),
    KARTICA(2); 

	private final int id;

	TipPlacanja(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static TipPlacanja fromId(int id) {
        for (TipPlacanja tip : values()) {
            if (tip.getId() == id) 
            	return tip;
        }
        throw new IllegalArgumentException("Nepoznat ID: " + id);
    }
	
    @Override
    public String toString() {
        switch(this) {
            case GOTOVINA: return "gotovina";
            case KARTICA: return "kartica";
            default: return super.toString();
        }
    }
}