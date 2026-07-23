package org.unibl.etf.test.dao;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.unibl.etf.dao.mysql.*;
import org.unibl.etf.entity.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdresaDataAccessImplTest {

    private static MjestoDataAccessImpl mjestoDAO;
    private static UlicaDataAccessImpl ulicaDAO;
    private static AdresaDataAccessImpl adresaDAO;
    private static final int POSTANSKI_1 = 99999;
    private static final int POSTANSKI_2 = 88888;
    private static int idAdreseTest; 

    @BeforeAll
    static void setUpAll() {
        mjestoDAO = new MjestoDataAccessImpl();
        ulicaDAO = new UlicaDataAccessImpl();
        adresaDAO = new AdresaDataAccessImpl();

        mjestoDAO.dodajMjesto(new Mjesto(POSTANSKI_1, "TestGrad"));
        ulicaDAO.dodajUlicu(new Ulica("TestUlica", new Mjesto(POSTANSKI_1, "TestGrad")));
    }

    @Test
    @Order(1)
    void testDodajAdresu() {
        Adresa novaAdresa = new Adresa(0,
                new Ulica("TestUlica", new Mjesto(POSTANSKI_1, "TestGrad")),
                123);

        boolean result = adresaDAO.dodajAdresu(novaAdresa);
        assertTrue(result, "Adresa treba da bude uspjesno dodata");

        List<Adresa> sveAdrese = adresaDAO.adrese();
        Adresa dodata = sveAdrese.stream()
                .filter(a -> a.getUlica().getNazivUlice().equals("TestUlica") &&
                             a.getKucniBroj() == 123)
                .findFirst()
                .orElse(null);
        assertNotNull(dodata);
        idAdreseTest = dodata.getIdAdrese();
    }

    @Test
    @Order(2)
    void testAdresaPoId() {
        Adresa a = adresaDAO.adresa(idAdreseTest);
        assertNotNull(a, "Adresa sa datim ID-em treba da postoji");
        assertEquals("TestUlica", a.getUlica().getNazivUlice());
        assertEquals(POSTANSKI_1, a.getUlica().getMjesto().getPostanskiBroj());
        assertEquals(123, a.getKucniBroj());
    }

    @Test
    @Order(3)
    void testSveAdrese() {
        List<Adresa> lista = adresaDAO.adrese();
        assertNotNull(lista);
        assertFalse(lista.isEmpty(), "Lista adresa ne smije biti prazna");
        assertTrue(lista.stream().anyMatch(a -> a.getIdAdrese() == idAdreseTest));
    }

    @Test
    @Order(4)
    void testAzurirajAdresu() {
        mjestoDAO.dodajMjesto(new Mjesto(POSTANSKI_2, "TestGrad2"));
        ulicaDAO.dodajUlicu(new Ulica("TestUlica2", new Mjesto(POSTANSKI_2, "TestGrad2")));

        Adresa azurirana = new Adresa(idAdreseTest,
                new Ulica("TestUlica2", new Mjesto(POSTANSKI_2, "TestGrad2")),
                456);

        boolean result = adresaDAO.azurirajAdresu(azurirana);
        assertTrue(result, "Adresa treba da bude uspjesno azurirana");

        Adresa provjera = adresaDAO.adresa(idAdreseTest);
        assertNotNull(provjera);
        assertEquals("TestUlica2", provjera.getUlica().getNazivUlice());
        assertEquals(POSTANSKI_2, provjera.getUlica().getMjesto().getPostanskiBroj());
        assertEquals(456, provjera.getKucniBroj());
    }

    @Test
    @Order(5)
    void testObrisiAdresu() {
        boolean result = adresaDAO.obrisiAdresu(idAdreseTest);
        assertTrue(result, "Adresa treba da bude uspjesno obrisana");

        Adresa obrisana = adresaDAO.adresa(idAdreseTest);
        assertNull(obrisana, "Obrisana adresa ne smije da postoji u bazi");
    }
}
