package org.unibl.etf.test.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import org.unibl.etf.entity.*;

public class ProizvodTest {
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
        
        Proizvod proizvod = new Proizvod(1, prerada, TipProizvoda.KAJMAK, 10.0);
        
        assertEquals(1, proizvod.getIdProizvoda());
        assertEquals(prerada, proizvod.getPrerada());
        assertEquals(TipProizvoda.KAJMAK, proizvod.getTip());
        assertEquals(10.0, proizvod.getCijena());
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
        LocalDate datum = LocalDate.of(2025, 8, 1);
        PreradaMlijeka prerada = new PreradaMlijeka(1, mlijeko, d, z, datum, 11.0, JedinicaMjere.LITAR);
        
        Proizvod proizvod = new Proizvod();
        
        proizvod.setIdProizvoda(1);
        proizvod.setPrerada(prerada);
        proizvod.setTip(TipProizvoda.SIR);
        proizvod.setCijena(20.0);
        
        assertEquals(1, proizvod.getIdProizvoda());
        assertEquals(prerada, proizvod.getPrerada());
        assertEquals(TipProizvoda.SIR, proizvod.getTip());
        assertEquals(20.0, proizvod.getCijena());
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
        PreradaMlijeka prerada = new PreradaMlijeka(1, mlijeko, d, z, datum, 11.0, JedinicaMjere.LITAR);
        
        Proizvod p1 = new Proizvod(1, prerada, TipProizvoda.KAJMAK, 10.0);
        Proizvod p2 = new Proizvod(1, prerada, TipProizvoda.KAJMAK, 10.0);
        
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }
}
