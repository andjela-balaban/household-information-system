package org.unibl.etf.test.dao;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.unibl.etf.dao.mysql.*;
import org.unibl.etf.entity.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class KupacDataAccessImplTest {

    private static KupacDataAccessImpl kupacDAO;
    private static int testKupacId;

    @BeforeAll
    static void setUp() {
        kupacDAO = new KupacDataAccessImpl();
    }

    @Test
    @Order(1)
    void testDodajKupca() {
        Osoba osoba = new Osoba("ImeKupcaTest", "PrezimeKupcaTest", null);
        Kupac noviKupac = new Kupac(osoba);

        boolean result = kupacDAO.dodajKupca(noviKupac);
        assertTrue(result, "Kupac treba da bude uspjesno dodat");

        List<Kupac> kupci = kupacDAO.kupci();
        Kupac nadjeni = kupci.stream()
                .filter(k -> k.getOsoba().getIme().equals("ImeKupcaTest") &&
                             k.getOsoba().getPrezime().equals("PrezimeKupcaTest"))
                .findFirst()
                .orElse(null);

        assertNotNull(nadjeni, "Dodani kupac mora postojati u bazi");
        testKupacId = nadjeni.getOsoba().getIdOsobe();
    }

    @Test
    @Order(2)
    void testKupacPoId() {
        assertTrue(testKupacId > 0, "Test ID kupca mora biti postavljen - auto_increment");

        Kupac k = kupacDAO.kupac(testKupacId);
        assertNotNull(k, "Kupac mora postojati");
        assertEquals(testKupacId, k.getOsoba().getIdOsobe());
        assertNotNull(k.getOsoba(), "Kupac treba da ima povezan objekat Osoba");
    }

    @Test
    @Order(3)
    void testKupciLista() {
        List<Kupac> lista = kupacDAO.kupci();
        assertNotNull(lista);
        assertFalse(lista.isEmpty(), "Lista kupaca ne smije biti prazna");
    }

    @Test
    @Order(4)
    void testAzurirajKupca() {
        Kupac k = kupacDAO.kupac(testKupacId);
        k.getOsoba().setIme("NovoImeKupcaTest");

        boolean result = kupacDAO.azurirajKupca(k);
        assertTrue(result, "Kupac treba da bude azuriran");

        Kupac azuriran = kupacDAO.kupac(testKupacId);
        assertEquals("NovoImeKupcaTest", azuriran.getOsoba().getIme());
    }

    @Test
    @Order(5)
    void testObrisiKupca() {
        boolean result = kupacDAO.obrisiKupca(testKupacId);
        assertTrue(result, "Kupac treba da bude obrisan");

        Kupac k = kupacDAO.kupac(testKupacId);
        assertNull(k, "Kupac ne sme postojati nakon brisanja");
    }
}
