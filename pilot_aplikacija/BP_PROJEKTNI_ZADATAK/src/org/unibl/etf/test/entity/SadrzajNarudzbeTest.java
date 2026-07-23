package org.unibl.etf.test.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import org.unibl.etf.entity.*;

public class SadrzajNarudzbeTest {
	@Test
    public void testConstructorAndGetters() {
		Adresa adresaD = new Adresa(1, new Ulica("Zaglavicka", new Mjesto(12320, "Zaglavica")), 21); 
	    Adresa adresa = new Adresa(1, new Ulica("Zaglavicka", new Mjesto(12320, "Zaglavica")), 21); 
        Osoba kontakt = new Osoba(1, "Pero", "Peric");
        Osoba osoba = new Osoba(1, "Marko", "Markovic", adresa);
        Kupac kupac = new Kupac(osoba);
        Osoba isporucilacOsoba = new Osoba(2, "Ivan", "Ivic");
        Domacinstvo d = new Domacinstvo("1234567890123", "Domaćinstvo ATOM", adresaD, kontakt);
        Zaposleni z = new Zaposleni(osoba, d, ZaposleniFunkcija.PRERADA, 1500.0);
        Zivotinja zivotinja = new Zivotinja(1, "Milka", TipZivotinje.KRAVA, d);
        Mlijeko mlijeko = new Mlijeko(10, LocalDate.of(2025, 8, 1), 12.5, JedinicaMjere.LITAR, 8, zivotinja);
        LocalDate datumPrerade = LocalDate.of(2025, 8, 1);
        PreradaMlijeka prerada = new PreradaMlijeka(1, mlijeko, d, z, datumPrerade, 11.0, JedinicaMjere.LITAR);
        Zaposleni isporucilac = new Zaposleni(isporucilacOsoba, d, ZaposleniFunkcija.ISPORUKA, 1700.0);
        Placanje placanje = new Placanje(100, kupac, 120.0, TipPlacanja.GOTOVINA);
        Isporuka isporuka = new Isporuka(101, LocalDate.of(2025, 8, 2), kupac.getOsoba().getAdresa(), kupac, isporucilac);
        LocalDate datum = LocalDate.of(2025, 8, 1);
        Proizvod proizvod = new Proizvod(1, prerada, TipProizvoda.KAJMAK, 10.0);
        Narudzba narudzba = new Narudzba(10, StatusNarudzbe.NEPLACENA, datum, kupac, placanje, isporuka);

        SadrzajNarudzbe sadrzaj = new SadrzajNarudzbe(narudzba, proizvod, 3);
        
        assertEquals(narudzba, sadrzaj.getNarudzba());
        assertEquals(proizvod, sadrzaj.getProizvod());
        assertEquals(3, sadrzaj.getKolicina());
    }

    @Test
    public void testSetters() {
    	Adresa adresaD = new Adresa(1, new Ulica("Zaglavicka", new Mjesto(12320, "Zaglavica")), 21); 
	    Adresa adresa = new Adresa(1, new Ulica("Zaglavicka", new Mjesto(12320, "Zaglavica")), 21); 
        Osoba kontakt = new Osoba(1, "Pero", "Peric");
        Osoba osoba = new Osoba(1, "Marko", "Markovic", adresa);
        Kupac kupac = new Kupac(osoba);
        Osoba isporucilacOsoba = new Osoba(2, "Ivan", "Ivic");
        Domacinstvo d = new Domacinstvo("1234567890123", "Domaćinstvo ATOM", adresaD, kontakt);
        Zaposleni z = new Zaposleni(osoba, d, ZaposleniFunkcija.PRERADA, 1500.0);
        Zivotinja zivotinja = new Zivotinja(1, "Milka", TipZivotinje.KRAVA, d);
        Mlijeko mlijeko = new Mlijeko(10, LocalDate.of(2025, 8, 1), 12.5, JedinicaMjere.LITAR, 8, zivotinja);
        LocalDate datumPrerade = LocalDate.of(2025, 8, 1);
        PreradaMlijeka prerada = new PreradaMlijeka(1, mlijeko, d, z, datumPrerade, 11.0, JedinicaMjere.LITAR);
        Zaposleni isporucilac = new Zaposleni(isporucilacOsoba, d, ZaposleniFunkcija.ISPORUKA, 1700.0);
        Placanje placanje = new Placanje(100, kupac, 120.0, TipPlacanja.GOTOVINA);
        Isporuka isporuka = new Isporuka(101, LocalDate.of(2025, 8, 2), kupac.getOsoba().getAdresa(), kupac, isporucilac);
        LocalDate datum = LocalDate.of(2025, 8, 1);
        Proizvod proizvod = new Proizvod(1, prerada, TipProizvoda.KAJMAK, 10.0);
        Narudzba narudzba = new Narudzba(10, StatusNarudzbe.NEPLACENA, datum, kupac, placanje, isporuka);

        SadrzajNarudzbe sadrzaj = new SadrzajNarudzbe();
        
        sadrzaj.setNarudzba(narudzba);
        sadrzaj.setProizvod(proizvod);
        sadrzaj.setKolicina(2);
        
        assertEquals(narudzba, sadrzaj.getNarudzba());
        assertEquals(proizvod, sadrzaj.getProizvod());
        assertEquals(2, sadrzaj.getKolicina());
    }

    @Test
    public void testEqualsAndHashCode() {
    	Adresa adresaD = new Adresa(1, new Ulica("Zaglavicka", new Mjesto(12320, "Zaglavica")), 21); 
	    Adresa adresa = new Adresa(1, new Ulica("Zaglavicka", new Mjesto(12320, "Zaglavica")), 21); 
        Osoba kontakt = new Osoba(1, "Pero", "Peric");
        Osoba osoba = new Osoba(1, "Marko", "Markovic", adresa);
        Kupac kupac = new Kupac(osoba);
        Osoba isporucilacOsoba = new Osoba(2, "Ivan", "Ivic");
        Domacinstvo d = new Domacinstvo("1234567890123", "Domaćinstvo ATOM", adresaD, kontakt);
        Zaposleni z = new Zaposleni(osoba, d, ZaposleniFunkcija.PRERADA, 1500.0);
        Zivotinja zivotinja = new Zivotinja(1, "Milka", TipZivotinje.KRAVA, d);
        Mlijeko mlijeko = new Mlijeko(10, LocalDate.of(2025, 8, 1), 12.5, JedinicaMjere.LITAR, 8, zivotinja);
        LocalDate datumPrerade = LocalDate.of(2025, 8, 1);
        PreradaMlijeka prerada = new PreradaMlijeka(1, mlijeko, d, z, datumPrerade, 11.0, JedinicaMjere.LITAR);
        Zaposleni isporucilac = new Zaposleni(isporucilacOsoba, d, ZaposleniFunkcija.ISPORUKA, 1700.0);
        Placanje placanje = new Placanje(100, kupac, 120.0, TipPlacanja.GOTOVINA);
        Isporuka isporuka = new Isporuka(101, LocalDate.of(2025, 8, 2), kupac.getOsoba().getAdresa(), kupac, isporucilac);
        LocalDate datum = LocalDate.of(2025, 8, 1);
        Proizvod proizvod = new Proizvod(1, prerada, TipProizvoda.KAJMAK, 10.0);
        Narudzba narudzba = new Narudzba(10, StatusNarudzbe.NEPLACENA, datum, kupac, placanje, isporuka);

        SadrzajNarudzbe s1 = new SadrzajNarudzbe(narudzba, proizvod, 3);
        SadrzajNarudzbe s2 = new SadrzajNarudzbe(narudzba, proizvod, 3);
        
        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }
}