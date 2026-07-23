package org.unibl.etf.entity;

public enum StatusNarudzbe {
	PLACENA(1),
	NEPLACENA(2);
	
	private final int id;

	StatusNarudzbe(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static StatusNarudzbe fromId(int id) {
        for (StatusNarudzbe status : values()) {
            if (status.getId() == id) 
            	return status;
        }
        throw new IllegalArgumentException("Nepoznat ID: " + id);
    }
	
	@Override
    public String toString() {
        switch(this) {
            case PLACENA: return "plaćena";
            case NEPLACENA: return "neplaćena";
            default: return super.toString();
        }
    }
}
