package org.unibl.etf.test.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.unibl.etf.entity.*;

public class TelefonDTest {
	@Test
    public void testConstructorAndGetters() {
        Osoba kontakt = new Osoba(1, "Pero", "Peric");
        Adresa adresaD = new Adresa(1, new Ulica("Zaglavicka", new Mjesto(12320, "Zaglavica")), 21); 
        Domacinstvo domacinstvo = new Domacinstvo("1234567890123", "Domaćinstvo ATOM", adresaD, kontakt);
        TelefonD telefon = new TelefonD(domacinstvo, "051-411-201");
        
        assertEquals(domacinstvo, telefon.getDomacinstvo());
        assertEquals("051-411-201", telefon.getBrojTelefona());
    }

    @Test
    public void testSetters() {
    	Osoba kontakt = new Osoba(1, "Pero", "Peric");
        Adresa adresaD = new Adresa(1, new Ulica("Zaglavicka", new Mjesto(12320, "Zaglavica")), 21); 
        Domacinstvo domacinstvo = new Domacinstvo("1234567890123", "Domaćinstvo ATOM", adresaD, kontakt);
        TelefonD telefon = new TelefonD();
        
        telefon.setDomacinstvo(domacinstvo);
        telefon.setBrojTelefona("051-500-501");
        
        assertEquals(domacinstvo, telefon.getDomacinstvo());
        assertEquals("051-500-501", telefon.getBrojTelefona());
    }

    @Test
    public void testEqualsAndHashCode() {
    	Osoba kontakt = new Osoba(1, "Pero", "Peric");
        Adresa adresaD = new Adresa(1, new Ulica("Zaglavicka", new Mjesto(12320, "Zaglavica")), 21); 
        Domacinstvo domacinstvo = new Domacinstvo("1234567890123", "Domaćinstvo ATOM", adresaD, kontakt);
        
        TelefonD t1 = new TelefonD(domacinstvo, "051-411-201");
        TelefonD t2 = new TelefonD(domacinstvo, "051-411-201");
       
        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
    }
}
