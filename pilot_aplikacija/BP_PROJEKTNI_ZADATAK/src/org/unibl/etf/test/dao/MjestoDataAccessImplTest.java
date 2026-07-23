package org.unibl.etf.test.dao;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.unibl.etf.dao.mysql.*;
import org.unibl.etf.entity.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MjestoDataAccessImplTest {

    private static MjestoDataAccessImpl mjestoDAO;
    private static int testPostanskiBroj;

    @BeforeAll
    static void setUp() {
        mjestoDAO = new MjestoDataAccessImpl();
    }

    @Test
    @Order(1)
    void testDodajMjesto() {
        testPostanskiBroj = (int) (Math.random() * 90000) + 10000; 
        Mjesto novoMjesto = new Mjesto(testPostanskiBroj, "TestGrad");

        boolean result = mjestoDAO.dodajMjesto(novoMjesto);
        assertTrue(result, "Mjesto treba da bude uspješno dodato");

        Mjesto izBaze = mjestoDAO.mjesto(testPostanskiBroj);
        assertNotNull(izBaze, "Dodato mjesto mora postojati u bazi");
    }

    @Test
    @Order(2)
    void testMjestoPoPostanskomBroju() {
        Mjesto m = mjestoDAO.mjesto(testPostanskiBroj);
        assertNotNull(m, "Mjesto mora postojati");
        assertEquals(testPostanskiBroj, m.getPostanskiBroj());
        assertNotNull(m.getNaziv(), "Naziv mjesta ne smije biti null");
    }

    @Test
    @Order(3)
    void testMjestaLista() {
        List<Mjesto> lista = mjestoDAO.mjesta();
        assertNotNull(lista, "Lista mjesta ne smije biti null");
        assertFalse(lista.isEmpty(), "Lista mjesta ne smije biti prazna");
    }

    @Test
    @Order(4)
    void testAzurirajMjesto() {
        Mjesto m = mjestoDAO.mjesto(testPostanskiBroj);
        assertNotNull(m, "Mjesto za ažuriranje mora postojati");

        m.setNaziv("TestMjestoAzurirano");
        boolean result = mjestoDAO.azurirajMjesto(m);
        assertTrue(result, "Mjesto treba da bude uspjesno azurirano");

        Mjesto azurirano = mjestoDAO.mjesto(testPostanskiBroj);
        assertEquals("TestMjestoAzurirano", azurirano.getNaziv());
    }

    @Test
    @Order(5)
    void testObrisiMjesto() {
        boolean result = mjestoDAO.obrisiMjesto(testPostanskiBroj);
        assertTrue(result, "Mjesto treba da bude uspjesno obrisano");

        Mjesto obrisano = mjestoDAO.mjesto(testPostanskiBroj);
        assertNull(obrisano, "Mjesto ne smije postojati nakon brisanja");
    }
}
