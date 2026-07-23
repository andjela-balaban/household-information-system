package org.unibl.etf.test.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.unibl.etf.entity.*;

public class PlacanjeTest {

	@Test
    public void testConstructorAndGetters() {
    	Adresa adresa = new Adresa(1, new Ulica("Kralja Petra I", new Mjesto(78000, "Banja Luka")), 21);
        Osoba o = new Osoba(1, "Marko", "Markovic", adresa);
        Kupac kupac = new Kupac(o);
        Placanje placanje = new Placanje(101, kupac, 100.0, TipPlacanja.GOTOVINA);
        
        assertEquals(101, placanje.getIdPlacanja());
        assertEquals(kupac, placanje.getKupac());
        assertEquals(100.0, placanje.getIznos());
        assertEquals(TipPlacanja.GOTOVINA, placanje.getTipPlacanja());
    }

    @Test
    public void testSetters() {
    	Adresa adresa = new Adresa(1, new Ulica("Kralja Petra I", new Mjesto(78000, "Banja Luka")), 21);
        Osoba o = new Osoba(1, "Marko", "Markovic", adresa);
        Kupac kupac = new Kupac(o);
        Placanje p = new Placanje();
        
        p.setIdPlacanja(102);
        p.setKupac(kupac);
        p.setIznos(110.0);
        p.setTipPlacanja(TipPlacanja.KARTICA);

        assertEquals(102, p.getIdPlacanja());
        assertEquals(kupac, p.getKupac());
        assertEquals(110.0, p.getIznos());
        assertEquals(TipPlacanja.KARTICA, p.getTipPlacanja());
    }

    @Test
    public void testEqualsAndHashCode() {
    	Adresa adresa = new Adresa(1, new Ulica("Kralja Petra I", new Mjesto(78000, "Banja Luka")), 21);
        Kupac k = new Kupac(new Osoba(1, "Marko", "Markovic", adresa));
        Placanje p1 = new Placanje(101, k, 100.0, TipPlacanja.GOTOVINA);
        Placanje p2 = new Placanje(101, k, 100.0, TipPlacanja.GOTOVINA);
        
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }
}
