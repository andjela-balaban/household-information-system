package org.unibl.etf.test.dao;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.unibl.etf.dao.mysql.*;
import org.unibl.etf.entity.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OsobaDataAccessImplTest {

    private static OsobaDataAccessImpl osobaDAO;
    private static int testOsobaId;

    @BeforeAll
    static void setUp() {
        osobaDAO = new OsobaDataAccessImpl();
    }

    @Test
    @Order(1)
    void testDodajOsobu() {
        Osoba novaOsoba = new Osoba("TestIme", "TestPrezime", null);
        boolean result = osobaDAO.dodajOsobu(novaOsoba);
        assertTrue(result, "Osoba treba da bude uspjesno dodata");

        List<Osoba> osobe = osobaDAO.osobe();
        Osoba nadjena = osobe.stream()
                .filter(o -> "TestIme".equals(o.getIme()) &&
                             "TestPrezime".equals(o.getPrezime()))
                .findFirst()
                .orElse(null);

        assertNotNull(nadjena, "Dodana osoba mora postojati u bazi");
        testOsobaId = nadjena.getIdOsobe();
    }

    @Test
    @Order(2)
    void testOsobaPoId() {
        assertTrue(testOsobaId > 0, "Test ID osobe mora biti postavljen - auto_increment");

        Osoba o = osobaDAO.osoba(testOsobaId);
        assertNotNull(o, "Osoba mora postojati");
        assertEquals(testOsobaId, o.getIdOsobe());
    }

    @Test
    @Order(3)
    void testOsobeLista() {
        List<Osoba> lista = osobaDAO.osobe();
        assertNotNull(lista);
        assertFalse(lista.isEmpty(), "Lista osoba ne smije da bude prazna");
    }

    @Test
    @Order(4)
    void testAzurirajOsobu() {
        Osoba o = osobaDAO.osoba(testOsobaId);
        o.setIme("NovoTestIme");

        boolean result = osobaDAO.azurirajOsobu(o);
        assertTrue(result, "Osoba treba da bude azurirana");

        Osoba azurirana = osobaDAO.osoba(testOsobaId);
        assertEquals("NovoTestIme", azurirana.getIme());
    }

    @Test
    @Order(5)
    void testObrisiOsobu() {
        boolean result = osobaDAO.obrisiOsobu(testOsobaId);
        assertTrue(result, "Osoba treba da bude obrisana");

        Osoba o = osobaDAO.osoba(testOsobaId);
        assertNull(o, "Osoba ne sme postojati nakon brisanja");
    }
}
