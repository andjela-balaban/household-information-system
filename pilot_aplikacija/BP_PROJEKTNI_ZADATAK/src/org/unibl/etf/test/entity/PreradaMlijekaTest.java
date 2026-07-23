package org.unibl.etf.test.entity;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import org.junit.jupiter.api.*;
import org.unibl.etf.entity.*;

public class PreradaMlijekaTest {
	@Test
    public void testConstructorAndGetters() {
        Osoba o = new Osoba(1, "Marko", "Markovic");
        Osoba kontakt = new Osoba(1, "Pero", "Peric");
        Adresa adresa = new Adresa(1, new Ulica("Zaglavicka", new Mjesto(12320, "Zaglavica")), 21); 
        Domacinstvo d = new Domacinstvo("1234567890123", "Domaćinstvo ATOM", adresa, kontakt);
        Zaposleni z = new Zaposleni(o, d, ZaposleniFunkcija.PRERADA, 1500.0);
        Zivotinja zivotinja = new Zivotinja(1, "Milka", TipZivotinje.KRAVA, d);
        Mlijeko mlijeko = new Mlijeko(10, LocalDate.of(2025, 8, 1), 12.5, JedinicaMjere.LITAR, 8, zivotinja);
        LocalDate datum = LocalDate.of(2025, 8, 1);

        PreradaMlijeka prerada = new PreradaMlijeka(1, mlijeko, d, z, datum, 11.0, JedinicaMjere.LITAR);
        
        assertEquals(1, prerada.getIdPrerade());
        assertEquals(mlijeko, prerada.getMlijeko());
        assertEquals(d, prerada.getDomacinstvo());
        assertEquals(z, prerada.getZaposleni());
        assertEquals(11.0, prerada.getKolicinaZaPreradu());
        assertEquals(JedinicaMjere.LITAR, prerada.getMjera());
    }

    @Test
    public void testSetters() {
    	Osoba o = new Osoba(1, "Marko", "Markovic");
        Osoba kontakt = new Osoba(1, "Pero", "Peric");
        Adresa adresa = new Adresa(1, new Ulica("Zaglavicka", new Mjesto(12320, "Zaglavica")), 21); 
        Domacinstvo d = new Domacinstvo("1234567890123", "Domaćinstvo ATOM", adresa, kontakt);
        Zaposleni z = new Zaposleni(o, d, ZaposleniFunkcija.PRERADA, 1500.0);
        Zivotinja zivotinja = new Zivotinja(1, "Milka", TipZivotinje.KRAVA, d);
        Mlijeko mlijeko = new Mlijeko(10, LocalDate.of(2025, 8, 1), 12.5, JedinicaMjere.LITAR, 8, zivotinja);

        PreradaMlijeka prerada = new PreradaMlijeka();
        
        prerada.setIdPrerade(1);
        prerada.setMlijeko(mlijeko);
        prerada.setDomacinstvo(d);
        prerada.setZaposleni(z);
        prerada.setKolicinaZaPreradu(20.0);
        prerada.setMjera(JedinicaMjere.LITAR);
        
        assertEquals(1, prerada.getIdPrerade());
        assertEquals(mlijeko, prerada.getMlijeko());
        assertEquals(d, prerada.getDomacinstvo());
        assertEquals(z, prerada.getZaposleni());
        assertEquals(20.0, prerada.getKolicinaZaPreradu());
        assertEquals(JedinicaMjere.LITAR, prerada.getMjera());
    }

    @Test
    public void testEqualsAndHashCode() {
    	Osoba o = new Osoba(1, "Marko", "Markovic");
        Osoba kontakt = new Osoba(1, "Pero", "Peric");
        Adresa adresa = new Adresa(1, new Ulica("Zaglavicka", new Mjesto(12320, "Zaglavica")), 21); 
        Domacinstvo d = new Domacinstvo("1234567890123", "Domaćinstvo ATOM", adresa, kontakt);
        Zaposleni z = new Zaposleni(o, d, ZaposleniFunkcija.PRERADA, 1500.0);
        Zivotinja zivotinja = new Zivotinja(1, "Milka", TipZivotinje.KRAVA, d);
        Mlijeko mlijeko = new Mlijeko(10, LocalDate.of(2025, 8, 1), 12.5, JedinicaMjere.LITAR, 8, zivotinja);
        LocalDate datum = LocalDate.of(2025, 8, 1);

        PreradaMlijeka p1 = new PreradaMlijeka(1, mlijeko, d, z, datum, 11.0, JedinicaMjere.LITAR);
        PreradaMlijeka p2 = new PreradaMlijeka(1, mlijeko, d, z, datum, 11.0, JedinicaMjere.LITAR);
        
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }
}
