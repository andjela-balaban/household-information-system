package org.unibl.etf.test.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.unibl.etf.entity.*;

public class ZivotinjaTest {

	@Test
    public void testConstructorAndGetters() {
        Osoba kontakt = new Osoba(1, "Pero", "Peric");
        Adresa adresa = new Adresa(1, new Ulica("Zaglavicka", new Mjesto(12320, "Zaglavica")), 21); 
        Domacinstvo d = new Domacinstvo("1234567890123", "Domaćinstvo ATOM", adresa, kontakt);
        Zivotinja zivotinja = new Zivotinja(1, "Sejla", TipZivotinje.KRAVA, d);
        assertEquals(1, zivotinja.getIdZivotinje());
        assertEquals("Sejla", zivotinja.getIme());
        assertEquals(TipZivotinje.KRAVA, zivotinja.getTip());
        assertEquals(d, zivotinja.getDomacinstvo());
    }

    @Test
    public void testSetters() {
        Zivotinja z = new Zivotinja();
        Osoba kontakt = new Osoba(1, "Pero", "Peric");
        Adresa adresa = new Adresa(1, new Ulica("Zaglavicka", new Mjesto(12320, "Zaglavica")), 21); 
        Domacinstvo d = new Domacinstvo("1234567890123", "Domaćinstvo ATOM", adresa, kontakt);
        z.setIdZivotinje(2);
        z.setIme("Belka");
        z.setTip(TipZivotinje.KOZA);
        z.setDomacinstvo(d);
        
        assertEquals(2, z.getIdZivotinje());
        assertEquals("Belka", z.getIme());
        assertEquals(TipZivotinje.KOZA, z.getTip());
        assertEquals(d, z.getDomacinstvo());
    }

    @Test
    public void testEqualsAndHashCode() {
        Osoba kontakt = new Osoba(1, "Pero", "Peric");
        Adresa adresa = new Adresa(1, new Ulica("Zaglavicka", new Mjesto(12320, "Zaglavica")), 21); 
        Domacinstvo d = new Domacinstvo("1234567890123", "Domaćinstvo ATOM", adresa, kontakt);
       
        Zivotinja z1 = new Zivotinja(1, "Milka", TipZivotinje.KRAVA, d);
        Zivotinja z2 = new Zivotinja(1, "Milka", TipZivotinje.KRAVA, d);
        
        assertEquals(z1, z2);
        assertEquals(z1.hashCode(), z2.hashCode());
    }
}
