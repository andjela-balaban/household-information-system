package org.unibl.etf.entity;

public enum TipZivotinje {
	KRAVA(1),
	KOZA(2);
	
	private final int id;

	TipZivotinje(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static TipZivotinje fromId(int id) {
        for (TipZivotinje tip : values()) {
            if (tip.getId() == id) 
            	return tip;
        }
        throw new IllegalArgumentException("Nepoznat ID: " + id);
    }
	
	@Override
    public String toString() {
        switch(this) {
            case KRAVA: return "krava";
            case KOZA: return "koza";
            default: return super.toString();
        }
    }
}
