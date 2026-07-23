package org.unibl.etf.test.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import org.unibl.etf.entity.*;

public class IsporukaTest {

    @Test
    public void testConstructorAndGetters() {
        Adresa adresa = new Adresa(1, new Ulica("Glavna", new Mjesto(78000, "Banja Luka")), 10);
        Osoba kupacOsoba = new Osoba(1, "Marko", "Markovic", adresa);
        Kupac kupac = new Kupac(kupacOsoba);
        Osoba isporucilacOsoba = new Osoba(2, "Ivan", "Ivic");
        Osoba kontakt = new Osoba(1, "Pero", "Peric");
        Adresa adresaD = new Adresa(1, new Ulica("Zaglavicka", new Mjesto(12320, "Zaglavica")), 21); 
        Domacinstvo domacinstvo = new Domacinstvo("1234567890123", "Domaćinstvo ATOM", adresaD, kontakt);
        Zaposleni isporucilac = new Zaposleni(isporucilacOsoba, domacinstvo, ZaposleniFunkcija.ISPORUKA, 1700.0);
        LocalDate datum = LocalDate.of(2025, 8, 1);

        Isporuka isporuka = new Isporuka(5, datum, adresa, kupac, isporucilac);

        assertEquals(5, isporuka.getIdIsporuke());
        assertEquals(datum, isporuka.getDatumIsporuke());
        assertEquals(adresa, isporuka.getAdresa());
        assertEquals(kupac, isporuka.getKupac());
        assertEquals(isporucilac, isporuka.getZaposleniIsporucilac());
    }

    @Test
    public void testSetters() {
        Isporuka isporuka = new Isporuka();
        Adresa adresa = new Adresa(2, new Ulica("Nova", new Mjesto(78000, "BL")), 33);
        Osoba kupacOsoba = new Osoba(3, "Ana", "Anic", adresa);
        Kupac kupac = new Kupac(kupacOsoba);
        Osoba isporucilacOsoba = new Osoba(4, "Zoran", "Zoric", adresa);
        Osoba kontakt = new Osoba(1, "Pero", "Peric");
        Adresa adresaD = new Adresa(1, new Ulica("Zaglavicka", new Mjesto(12320, "Zaglavica")), 21); 
        Domacinstvo domacinstvo = new Domacinstvo("1234567890123", "Domaćinstvo ATOM", adresaD, kontakt);
        Zaposleni isporucilac = new Zaposleni(isporucilacOsoba, domacinstvo, ZaposleniFunkcija.ISPORUKA, 1700.0);
        LocalDate datum = LocalDate.now();

        isporuka.setIdIsporuke(10);
        isporuka.setDatumIsporuke(datum);
        isporuka.setAdresa(adresa);
        isporuka.setKupac(kupac);
        isporuka.setZaposleniIsporucilac(isporucilac);

        assertEquals(10, isporuka.getIdIsporuke());
        assertEquals(datum, isporuka.getDatumIsporuke());
        assertEquals(adresa, isporuka.getAdresa());
        assertEquals(kupac, isporuka.getKupac());
        assertEquals(isporucilac, isporuka.getZaposleniIsporucilac());
    }

    @Test
    public void testEqualsAndHashCode() {
        Isporuka i1 = new Isporuka(1, LocalDate.now(), null, null, null);
        Isporuka i2 = new Isporuka(1, LocalDate.now(), null, null, null);
        Isporuka i3 = new Isporuka(2, LocalDate.now(), null, null, null);

        assertEquals(i1, i2);
        assertNotEquals(i1, i3);
        assertEquals(i1.hashCode(), i2.hashCode());
        assertNotEquals(i1.hashCode(), i3.hashCode());
    }

    @Test
    public void testToString() {
        Adresa adresa = new Adresa(1, new Ulica("Test ulica", new Mjesto(78000, "Banja Luka")), 42);
        Osoba kupacOsoba = new Osoba(10, "Test", "Kupac", adresa);
        Osoba zapOsoba = new Osoba(11, "Test", "Zaposleni", adresa);
        Kupac kupac = new Kupac(kupacOsoba);
        Osoba kontakt = new Osoba(1, "Pero", "Peric");
        Adresa adresaD = new Adresa(1, new Ulica("Zaglavicka", new Mjesto(12320, "Zaglavica")), 21); 
        Domacinstvo domacinstvo = new Domacinstvo("1234567890123", "Domaćinstvo ATOM", adresaD, kontakt);
        Zaposleni zap = new Zaposleni(zapOsoba, domacinstvo, ZaposleniFunkcija.ISPORUKA, 1700.0);
        Isporuka isporuka = new Isporuka(100, LocalDate.now(), adresa, kupac, zap);

        String s = isporuka.toString();
        assertTrue(s.contains("Isporuka ID: 100"));
        assertTrue(s.contains("Test Kupac"));
        assertTrue(s.contains("Test Zaposleni"));
        assertTrue(s.contains("Adresa:"));
    }
}
