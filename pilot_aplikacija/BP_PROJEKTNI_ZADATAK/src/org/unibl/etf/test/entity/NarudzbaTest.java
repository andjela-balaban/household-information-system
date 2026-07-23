package org.unibl.etf.test.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import org.unibl.etf.entity.*;

public class NarudzbaTest {

    @Test
    public void testConstructorAndGetters() {
    	Adresa adresa = new Adresa(1, new Ulica("Kralja Petra I", new Mjesto(78000, "Banja Luka")), 21);
        Osoba osoba = new Osoba(1, "Marko", "Markovic", adresa);
        Kupac kupac = new Kupac(osoba);
        Osoba isporucilacOsoba = new Osoba(2, "Ivan", "Ivic");
        Osoba kontakt = new Osoba(1, "Pero", "Peric");
        Adresa adresaD = new Adresa(1, new Ulica("Zaglavicka", new Mjesto(12320, "Zaglavica")), 21); 
        Domacinstvo domacinstvo = new Domacinstvo("1234567890123", "Domaćinstvo ATOM", adresaD, kontakt);
        Zaposleni isporucilac = new Zaposleni(isporucilacOsoba, domacinstvo, ZaposleniFunkcija.ISPORUKA, 1700.0);
        Placanje placanje = new Placanje(100, kupac, 120.0, TipPlacanja.GOTOVINA);
        Isporuka isporuka = new Isporuka(101, LocalDate.of(2025, 8, 2), kupac.getOsoba().getAdresa(), kupac, isporucilac);
        LocalDate datum = LocalDate.of(2025, 8, 1);
        
        Narudzba narudzba = new Narudzba(10, StatusNarudzbe.NEPLACENA, datum, kupac, placanje, isporuka);

        assertEquals(10, narudzba.getIdNarudzbe());
        assertEquals(StatusNarudzbe.NEPLACENA, narudzba.getStatus());
        assertEquals(datum, narudzba.getDatumNarudzbe());
        assertEquals(kupac, narudzba.getKupac());
        assertEquals(placanje, narudzba.getPlacanje());
        assertEquals(isporuka, narudzba.getIsporuka());
    }

    @Test
    public void testSetters() {
        Adresa adresa = new Adresa(1, new Ulica("Kralja Petra I", new Mjesto(78000, "Banja Luka")), 21);
        Osoba osoba = new Osoba(1, "Marko", "Markovic", adresa);
        Kupac kupac = new Kupac(osoba);
        Osoba isporucilacOsoba = new Osoba(2, "Ivan", "Ivic");
        Osoba kontakt = new Osoba(1, "Pero", "Peric");
        Adresa adresaD = new Adresa(1, new Ulica("Zaglavicka", new Mjesto(12320, "Zaglavica")), 21); 
        Domacinstvo domacinstvo = new Domacinstvo("1234567890123", "Domaćinstvo ATOM", adresaD, kontakt);
        Zaposleni isporucilac = new Zaposleni(isporucilacOsoba, domacinstvo, ZaposleniFunkcija.ISPORUKA, 1700.0);
        Placanje placanje = new Placanje(100, kupac, 120.0, TipPlacanja.GOTOVINA);
        Isporuka isporuka = new Isporuka(101, LocalDate.of(2025, 8, 2), kupac.getOsoba().getAdresa(), kupac, isporucilac);
        LocalDate datum = LocalDate.of(2025, 8, 1);
        
        Narudzba narudzba = new Narudzba();

        narudzba.setIdNarudzbe(20);
        narudzba.setStatus(StatusNarudzbe.PLACENA);
        narudzba.setDatumNarudzbe(datum);
        narudzba.setKupac(kupac);
        narudzba.setPlacanje(placanje);
        narudzba.setIsporuka(isporuka);

        assertEquals(20, narudzba.getIdNarudzbe());
        assertEquals(StatusNarudzbe.PLACENA, narudzba.getStatus());
        assertEquals(datum, narudzba.getDatumNarudzbe());
        assertEquals(kupac, narudzba.getKupac());
        assertEquals(placanje, narudzba.getPlacanje());
        assertEquals(isporuka, narudzba.getIsporuka());
    }

    @Test
    public void testEqualsAndHashCode() {
    	Adresa adresa = new Adresa(1, new Ulica("Kralja Petra I", new Mjesto(78000, "Banja Luka")), 21);
        Osoba osoba = new Osoba(1, "Marko", "Markovic", adresa);
        Kupac kupac = new Kupac(osoba);
        Osoba isporucilacOsoba = new Osoba(2, "Ivan", "Ivic");
        Osoba kontakt = new Osoba(1, "Pero", "Peric");
        Adresa adresaD = new Adresa(1, new Ulica("Zaglavicka", new Mjesto(12320, "Zaglavica")), 21); 
        Domacinstvo domacinstvo = new Domacinstvo("1234567890123", "Domaćinstvo ATOM", adresaD, kontakt);
        Zaposleni isporucilac = new Zaposleni(isporucilacOsoba, domacinstvo, ZaposleniFunkcija.ISPORUKA, 1700.0);
        Placanje placanje = new Placanje(100, kupac, 120.0, TipPlacanja.GOTOVINA);
        Isporuka isporuka = new Isporuka(101, LocalDate.of(2025, 8, 2), kupac.getOsoba().getAdresa(), kupac, isporucilac);
        LocalDate datum = LocalDate.of(2025, 8, 1);

        Narudzba n1 = new Narudzba(10, StatusNarudzbe.NEPLACENA, datum, kupac, placanje, isporuka);
        Narudzba n2 = new Narudzba(10, StatusNarudzbe.NEPLACENA, datum, kupac, placanje, isporuka);

        assertEquals(n1, n2);
        assertEquals(n1.hashCode(), n2.hashCode());
    }
}
