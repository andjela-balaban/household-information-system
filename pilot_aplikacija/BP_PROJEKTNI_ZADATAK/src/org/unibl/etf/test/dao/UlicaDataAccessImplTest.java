package org.unibl.etf.test.dao;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.unibl.etf.dao.mysql.*;
import org.unibl.etf.entity.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UlicaDataAccessImplTest {

	private static MjestoDataAccessImpl mjestoDAO;
    private static UlicaDataAccessImpl ulicaDAO;

    @BeforeEach
    void setUp() {
    	mjestoDAO = new MjestoDataAccessImpl();
        ulicaDAO = new UlicaDataAccessImpl();
    }

    @Test
    @Order(1)
    void testDodajUlicu() {
    	Mjesto mjesto = new Mjesto(99999, "TestGrad");
    	mjestoDAO.dodajMjesto(mjesto);
        Ulica novaUlica = new Ulica("TestUlica", mjesto);
        boolean result = ulicaDAO.dodajUlicu(novaUlica);
        assertTrue(result, "Ulica treba da bude uspjesno dodata");
    }

    @Test
    @Order(2)
    void testUlicaPoNazivuIPostanskomBroju() {
        Ulica u = ulicaDAO.ulica(99999, "TestUlica");
        assertNotNull(u, "Ulica sa datim postanskim brojem i nazivom treba da postoji u bazi");
        assertEquals("TestUlica", u.getNazivUlice());
        assertEquals(99999, u.getMjesto().getPostanskiBroj());
    }

    @Test
    @Order(3)
    void testUliceUMjestu() {
        List<Ulica> lista = ulicaDAO.uliceUMjestu(99999);
        assertNotNull(lista);
        assertFalse(lista.isEmpty(), "Lista ulica u mjestu ne smije biti prazna");
        assertTrue(lista.stream().anyMatch(u -> u.getNazivUlice().equals("TestUlica")));
    }

    @Test
    @Order(4)
    void testAzurirajUlicu() {
        Mjesto novoMjesto = new Mjesto(88888, "TestGrad2"); 
        mjestoDAO.dodajMjesto(novoMjesto);
        Ulica u = new Ulica("TestUlica", novoMjesto);

        boolean result = ulicaDAO.azurirajUlicu(u);
        assertTrue(result, "Ulica treba da bude uspjesno azurirana");

        Ulica azurirana = ulicaDAO.ulica(88888, "TestUlica");
        assertNotNull(azurirana, "Azurirana ulica treba da postoji");
        assertEquals(88888, azurirana.getMjesto().getPostanskiBroj());
    }

    @Test
    @Order(5)
    void testObrisiUlicu() {
        boolean result = ulicaDAO.obrisiUlicu(88888, "TestUlica");
        assertTrue(result, "Ulica treba da bude uspjesno obrisana");

        Ulica obrisana = ulicaDAO.ulica(88888, "TestUlica");
        assertNull(obrisana, "Obrisana ulica ne smije da postoji u bazi");
    }
}
