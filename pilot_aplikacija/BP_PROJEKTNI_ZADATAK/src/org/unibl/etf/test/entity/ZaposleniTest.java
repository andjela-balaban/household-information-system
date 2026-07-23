package org.unibl.etf.test.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.unibl.etf.entity.*;

public class ZaposleniTest {
    @Test
    public void testConstructorAndGetters() {
        Osoba o = new Osoba(1, "Marko", "Markovic");
        Osoba kontakt = new Osoba(1, "Pero", "Peric");
        Adresa adresa = new Adresa(1, new Ulica("Zaglavicka", new Mjesto(12320, "Zaglavica")), 21); 
        Domacinstvo d = new Domacinstvo("1234567890123", "Domaćinstvo ATOM", adresa, kontakt);
        Zaposleni z = new Zaposleni(o, d, ZaposleniFunkcija.PRERADA, 1500.0);
        assertEquals(1, z.getOsoba().getIdOsobe());
        assertEquals("Marko", z.getOsoba().getIme());
        assertEquals("Markovic", z.getOsoba().getPrezime());
        assertEquals(d, z.getDomacinstvo());
        assertEquals(1500.0, z.getPlata());
        assertEquals(ZaposleniFunkcija.PRERADA, z.getFunkcija());
    }

    @Test
    public void testSetters() {
        Zaposleni z = new Zaposleni();
        Osoba kontakt = new Osoba(1, "Pero", "Peric");
        Adresa adresa = new Adresa(1, new Ulica("Zaglavicka", new Mjesto(12320, "Zaglavica")), 21); 
        Domacinstvo d = new Domacinstvo("1234567890123", "Domaćinstvo ATOM", adresa, kontakt);
        z.setOsoba(new Osoba(1, "Marko", "Markovic"));
        z.setDomacinstvo(d);
        z.setFunkcija(ZaposleniFunkcija.ISPORUKA);
        z.setPlata(2100.0);
        
        assertEquals(new Osoba(1, "Marko", "Markovic"), z.getOsoba());
        assertEquals(d, z.getDomacinstvo());
        assertEquals(ZaposleniFunkcija.ISPORUKA, z.getFunkcija());
        assertEquals(2100.0, z.getPlata());
    }

    @Test
    public void testEqualsAndHashCode() {
        Osoba o = new Osoba(1, "Marko", "Markovic");
        Osoba kontakt = new Osoba(1, "Pero", "Peric");
        Adresa adresa = new Adresa(1, new Ulica("Zaglavicka", new Mjesto(12320, "Zaglavica")), 21); 
        Domacinstvo d = new Domacinstvo("1234567890123", "Domaćinstvo ATOM", adresa, kontakt);
       
        Zaposleni z1 = new Zaposleni(o, d, ZaposleniFunkcija.PRERADA, 1500.0);
        Zaposleni z2 = new Zaposleni(o, d, ZaposleniFunkcija.PRERADA, 1500.0);
        
        assertEquals(z1, z2);
        assertEquals(z1.hashCode(), z2.hashCode());
    }
}
