package org.unibl.etf.test.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.unibl.etf.entity.*;

public class KupacTest {

    @Test
    public void testConstructorAndGetters() {
    	Adresa adresa = new Adresa(1, new Ulica("Kralja Petra I", new Mjesto(78000, "Banja Luka")), 21);
        Osoba o = new Osoba(1, "Marko", "Markovic", adresa);
        Kupac kupac = new Kupac(o);
        assertEquals(1, kupac.getOsoba().getIdOsobe());
        assertEquals("Marko", kupac.getOsoba().getIme());
        assertEquals("Markovic", kupac.getOsoba().getPrezime());
        assertEquals(new Adresa(1, new Ulica("Kralja Petra I", new Mjesto(78000, "Banja Luka")), 21), o.getAdresa());
    }

    @Test
    public void testSetters() {
    	Adresa adresa = new Adresa(1, new Ulica("Kralja Petra I", new Mjesto(78000, "Banja Luka")), 21);
        Osoba o = new Osoba(1, "Marko", "Markovic", adresa);
        Kupac k = new Kupac();
        
        k.setOsoba(o);

        assertEquals(1, k.getOsoba().getIdOsobe());
        assertEquals("Marko", k.getOsoba().getIme());
        assertEquals("Markovic", k.getOsoba().getPrezime());
        assertEquals(new Adresa(1, new Ulica("Kralja Petra I", new Mjesto(78000, "Banja Luka")), 21), o.getAdresa());
    }

    @Test
    public void testEqualsAndHashCode() {
    	Adresa adresa = new Adresa(1, new Ulica("Kralja Petra I", new Mjesto(78000, "Banja Luka")), 21);
        Osoba o1 = new Osoba(1, "Marko", "Markovic", adresa);
        Osoba o2 = new Osoba(1, "Marko", "Markovic", adresa);
        Kupac k1 = new Kupac(o1);
        Kupac k2 = new Kupac(o2);
        
        assertEquals(k1, k2);
        assertEquals(k1.hashCode(), k2.hashCode());
    }
}
