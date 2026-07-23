package org.unibl.etf.test.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.unibl.etf.entity.*;

public class DomacinstvoTest {
    @Test
    public void testConstructorAndGetters() {
        Osoba kontakt = new Osoba(1, "Pero", "Peric");
        Adresa adresaD = new Adresa(1, new Ulica("Zaglavicka", new Mjesto(12320, "Zaglavica")), 21); 
        Domacinstvo domacinstvo = new Domacinstvo("1234567890123", "Domaćinstvo ATOM", adresaD, kontakt);
        
        assertEquals("1234567890123", domacinstvo.getJib());
        assertEquals("Domaćinstvo ATOM", domacinstvo.getNaziv());
        assertEquals(adresaD, domacinstvo.getAdresa());
        assertEquals(kontakt, domacinstvo.getKontaktOsoba());
    }

    @Test
    public void testSetters() {
    	Osoba kontakt = new Osoba(1, "Pero", "Peric");
        Adresa adresaD = new Adresa(1, new Ulica("Zaglavicka", new Mjesto(12320, "Zaglavica")), 21); 
        Domacinstvo domacinstvo = new Domacinstvo("1234567890123", "Domaćinstvo ATOM", adresaD, kontakt);
        
        domacinstvo.setJib("1234567890123");
        domacinstvo.setNaziv("Domaćinstvo ATOM");
        domacinstvo.setAdresa(adresaD);
        domacinstvo.setKontaktOsoba(kontakt);
        
        assertEquals("1234567890123", domacinstvo.getJib());
        assertEquals("Domaćinstvo ATOM", domacinstvo.getNaziv());
        assertEquals(adresaD, domacinstvo.getAdresa());
        assertEquals(kontakt, domacinstvo.getKontaktOsoba());
    }

    @Test
    public void testEqualsAndHashCode() {
    	Osoba kontakt = new Osoba(1, "Pero", "Peric");
        Adresa adresaD = new Adresa(1, new Ulica("Zaglavicka", new Mjesto(12320, "Zaglavica")), 21); 

        Domacinstvo d1 = new Domacinstvo("1234567890123", "Domaćinstvo ATOM", adresaD, kontakt);
        Domacinstvo d2 = new Domacinstvo("1234567890123", "Domaćinstvo ATOM", adresaD, kontakt);
        
        assertEquals(d1, d2);
        assertEquals(d1.hashCode(), d2.hashCode());
    }
}
