package org.unibl.etf.test.dao;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.unibl.etf.dao.DomacinstvoDataAccess;
import org.unibl.etf.dao.mysql.*;
import org.unibl.etf.entity.*;

public class ZaposleniDataAccessImplTest {

    private ZaposleniDataAccessImpl zaposleniDAO;
    private static int testZaposelniId;

    @BeforeEach
    void setUp() {
        zaposleniDAO = new ZaposleniDataAccessImpl();
    }

    @Test
    void testDodajZaposlenog() {
        Osoba osoba = new Osoba("ZaposleniIme", "ZaposleniPrezime", null);
        DomacinstvoDataAccess domacinstvoDAO = new DomacinstvoDataAccessImpl();
        Domacinstvo domacinstvo = domacinstvoDAO.domacinstvo("1234567890123");
        ZaposleniFunkcija funkcija = ZaposleniFunkcija.fromId(1);  
        Zaposleni noviZaposleni = new Zaposleni(osoba, domacinstvo, funkcija, 1500.0);
        boolean result = zaposleniDAO.dodajZaposlenog(noviZaposleni);
        assertTrue(result, "Zaposleni treba da bude uspjesno dodat");
        

        List<Zaposleni> zaposleni = zaposleniDAO.sviZaposleni();
        Zaposleni nadjeni = zaposleni.stream()
               .filter(n -> n.getOsoba().equals(noviZaposleni.getOsoba())
                        && n.getDomacinstvo().equals(noviZaposleni.getDomacinstvo()) 
                        && n.getPlata() == noviZaposleni.getPlata()
                        && n.getFunkcija() == noviZaposleni.getFunkcija())
               .findFirst()
               .orElse(null);

          assertNotNull(nadjeni, "Dodati zaposelni mora postojati u bazi");
          testZaposelniId = nadjeni.getOsoba().getIdOsobe();
    }

    @Test
    void testZaposleniPoId() {
        Zaposleni z = zaposleniDAO.zaposleni(2);
        assertNotNull(z, "Zaposleni sa id=2 treba da postoji");
        assertEquals(2, z.getOsoba().getIdOsobe());
        assertNotNull(z.getOsoba(), "Zaposleni treba da ima povezanu Osobu");
        assertNotNull(z.getDomacinstvo(), "Zaposleni treba da ima povezano Domacinstvo");
        assertNotNull(z.getFunkcija(), "Zaposleni treba da ima funkciju");
    }

    @Test
    void testSviZaposleni() {
        List<Zaposleni> lista = zaposleniDAO.sviZaposleni();
        assertNotNull(lista);
        assertFalse(lista.isEmpty(), "Lista zaposlenih ne smije biti prazna");
    }
    
    @Test
    void testAzurirajZaposlenog() {
    	Zaposleni zaposleni = zaposleniDAO.zaposleni(3);
        zaposleni.getOsoba().setIme("NovoImeZaposlenog");
        zaposleni.setPlata(zaposleni.getPlata() + 100);
        zaposleni.setFunkcija(ZaposleniFunkcija.ISPORUKA);
        boolean result = zaposleniDAO.azurirajZaposlenog(zaposleni);
        assertTrue(result);
        Zaposleni azuriran = zaposleniDAO.zaposleni(3);
        assertEquals("NovoImeZaposlenog", azuriran.getOsoba().getIme());
        assertEquals(zaposleni.getPlata(), azuriran.getPlata());
        assertEquals(ZaposleniFunkcija.ISPORUKA, azuriran.getFunkcija());
    }

    @Test
    void testObrisiZaposlenog() {
        boolean result = zaposleniDAO.obrisiZaposlenog(testZaposelniId);
        assertTrue(result);

        Zaposleni z = zaposleniDAO.zaposleni(testZaposelniId);
        assertNull(z, "Zaposleni sa id=testZaposelniId treba biti obrisan");
    }
}
