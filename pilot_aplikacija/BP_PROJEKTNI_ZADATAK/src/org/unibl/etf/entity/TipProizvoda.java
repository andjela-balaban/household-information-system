package org.unibl.etf.entity;

public enum TipProizvoda {
    SIR(1),       
    KAJMAK(2),    
    JOGURT(3),
    MLIJEKO(4),
    PAVLAKA(5); 
	
	private final int id;

	TipProizvoda(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static TipProizvoda fromId(int id) {
        for (TipProizvoda tip : values()) {
            if (tip.getId() == id) 
            	return tip;
        }
        throw new IllegalArgumentException("Nepoznat ID: " + id);
    }

    @Override
    public String toString() {
        switch(this) {
            case SIR: return "sir";
            case KAJMAK: return "kajmak";
            case JOGURT: return "jogurt";
            case MLIJEKO: return "mlijeko";
            case PAVLAKA: return "pavlaka";
            default: return super.toString();
        }
    }
}