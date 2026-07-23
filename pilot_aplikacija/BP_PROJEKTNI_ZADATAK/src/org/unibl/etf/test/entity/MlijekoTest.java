package org.unibl.etf.test.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import org.unibl.etf.entity.*;

public class MlijekoTest {
    @Test
    public void testConstructorAndGetters() {
    	Osoba kontakt = new Osoba(1, "Pero", "Peric");
        Adresa adresa = new Adresa(1, new Ulica("Zaglavicka", new Mjesto(12320, "Zaglavica")), 21); 
        Domacinstvo d = new Domacinstvo("1234567890123", "Domaćinstvo ATOM", adresa, kontakt);
        Zivotinja zivotinja = new Zivotinja(1, "Milka", TipZivotinje.KRAVA, d);
        LocalDate datum = LocalDate.of(2025, 8, 1);
        double kolicina = 12.5;
        int kvalitet = 8;

        Mlijeko mlijeko = new Mlijeko(10, datum, kolicina, JedinicaMjere.LITAR, kvalitet, zivotinja);

        assertEquals(10, mlijeko.getIdMlijeka());
        assertEquals(datum, mlijeko.getDatumMuze());
        assertEquals(kolicina, mlijeko.getKolicina());
        assertEquals(JedinicaMjere.LITAR, mlijeko.getMjera());
        assertEquals(kvalitet, mlijeko.getKvalitet());
        assertEquals(zivotinja, mlijeko.getZivotinja());
    }

    @Test
    public void testSetters() {
        Mlijeko mlijeko = new Mlijeko();
        LocalDate datum = LocalDate.now();
        Osoba kontakt = new Osoba(1, "Pero", "Peric");
        Adresa adresa = new Adresa(1, new Ulica("Zaglavicka", new Mjesto(12320, "Zaglavica")), 21); 
        Domacinstvo d = new Domacinstvo("1234567890123", "Domaćinstvo ATOM", adresa, kontakt);
        Zivotinja zivotinja = new Zivotinja(1, "Milka", TipZivotinje.KRAVA, d);

        mlijeko.setIdMlijeka(5);
        mlijeko.setDatumMuze(datum);
        mlijeko.setKolicina(7.8);
        mlijeko.setMjera(JedinicaMjere.LITAR);
        mlijeko.setKvalitet(9);
        mlijeko.setZivotinja(zivotinja);

        assertEquals(5, mlijeko.getIdMlijeka());
        assertEquals(datum, mlijeko.getDatumMuze());
        assertEquals(7.8, mlijeko.getKolicina());
        assertEquals(JedinicaMjere.LITAR, mlijeko.getMjera());
        assertEquals(9, mlijeko.getKvalitet());
        assertEquals(zivotinja, mlijeko.getZivotinja());
    }

    @Test
    public void testEqualsAndHashCode() {
        Zivotinja z = new Zivotinja();
        Mlijeko m1 = new Mlijeko(1, LocalDate.now(), 10, 5, z);
        Mlijeko m2 = new Mlijeko(1, LocalDate.now(), 12, 6, z);

        assertEquals(m1, m2);
        assertEquals(m1.hashCode(), m2.hashCode());
    }
}
