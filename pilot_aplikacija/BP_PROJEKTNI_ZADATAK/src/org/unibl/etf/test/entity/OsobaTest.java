package org.unibl.etf.test.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.unibl.etf.entity.*;

public class OsobaTest {

    @Test
    public void testConstructorAndGetters() {
    	Adresa adresa = new Adresa(1, new Ulica("Kralja Petra I", new Mjesto(78000, "Banja Luka")), 21);
        Osoba o = new Osoba(1, "Marko", "Markovic", adresa);
        assertEquals(1, o.getIdOsobe());
        assertEquals("Marko", o.getIme());
        assertEquals("Markovic", o.getPrezime());
        assertEquals(new Adresa(1, new Ulica("Kralja Petra I", new Mjesto(78000, "Banja Luka")), 21), o.getAdresa());
    }

    @Test
    public void testSetters() {
        Osoba o = new Osoba();
        Adresa adresa2 = new Adresa(1, new Ulica("Milosa Obilica", new Mjesto(89101, "Trebinje")), 33);
        o.setIdOsobe(2);
        o.setIme("Petar");
        o.setPrezime("Petrovic");
        o.setAdresa(adresa2);

        assertEquals(2, o.getIdOsobe());
        assertEquals("Petar", o.getIme());
        assertEquals("Petrovic", o.getPrezime());
        assertEquals(adresa2, o.getAdresa());
    }

    @Test
    public void testEqualsAndHashCode() {
    	Adresa adresa = new Adresa(1, new Ulica("Kralja Petra I", new Mjesto(78000, "Banja Luka")), 21);
        Osoba o1 = new Osoba(1, "Marko", "Markovic", adresa);
        Osoba o2 = new Osoba(1, "Marko", "Markovic", adresa);
        
        assertEquals(o1, o2);
        assertEquals(o1.hashCode(), o2.hashCode());
    }
}
