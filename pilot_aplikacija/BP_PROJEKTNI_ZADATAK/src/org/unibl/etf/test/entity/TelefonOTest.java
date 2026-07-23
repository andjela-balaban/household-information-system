package org.unibl.etf.test.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.unibl.etf.entity.*;

public class TelefonOTest {
	@Test
    public void testConstructorAndGetters() {
        Osoba osoba = new Osoba(1, "Pero", "Peric");
        TelefonO telefon = new TelefonO(osoba, "066-411-201");
        
        assertEquals(osoba, telefon.getOsoba());
        assertEquals("066-411-201", telefon.getBrojTelefona());
    }

    @Test
    public void testSetters() {
    	Osoba osoba = new Osoba(1, "Pero", "Peric");
        TelefonO telefon = new TelefonO();
        
        telefon.setOsoba(osoba);
        telefon.setBrojTelefona("066-411-200");
        
        assertEquals(osoba, telefon.getOsoba());
        assertEquals("066-411-200", telefon.getBrojTelefona());
    }

    @Test
    public void testEqualsAndHashCode() {
    	Osoba osoba = new Osoba(1, "Pero", "Peric");
       
    	TelefonO t1 = new TelefonO(osoba, "066-411-201");
    	TelefonO t2 = new TelefonO(osoba, "066-411-201");
       
        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
    }
}
