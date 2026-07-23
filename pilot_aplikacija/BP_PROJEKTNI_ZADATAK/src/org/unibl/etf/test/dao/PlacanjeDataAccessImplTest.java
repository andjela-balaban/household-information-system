package org.unibl.etf.test.dao;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.unibl.etf.dao.mysql.*;
import org.unibl.etf.entity.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PlacanjeDataAccessImplTest {

    private static PlacanjeDataAccessImpl placanjeDAO;
    private static KupacDataAccessImpl kupacDAO;

    private static int testPlacanjeId;
    private static int testKupacId;

    @BeforeAll
    static void setUp() {
        placanjeDAO = new PlacanjeDataAccessImpl();
        kupacDAO = new KupacDataAccessImpl();

        List<Kupac> sviKupci = kupacDAO.kupci();
        testKupacId = sviKupci.get(0).getOsoba().getIdOsobe();
    }

    @Test
    @Order(1)
    void testDodajPlacanje() {
        Kupac kupac = kupacDAO.kupac(testKupacId);
        assertNotNull(kupac, "Kupac mora postojati za dodavanje placanja");

        Placanje novoPlacanje = new Placanje(0, kupac, 150.0, TipPlacanja.KARTICA);

        boolean dodano = placanjeDAO.dodajPlacanje(novoPlacanje);
        assertTrue(dodano, "Placanje treba biti uspesno dodato");

        List<Placanje> svaPlacanja = placanjeDAO.svaPlacanja();
        Placanje pronadjeno = svaPlacanja.stream()
            .filter(p -> p.getKupac().getOsoba().getIdOsobe() == testKupacId && p.getIznos() == 150.0)
            .findFirst()
            .orElse(null);

        assertNotNull(pronadjeno, "Dodato placanje mora postojati u bazi");
        testPlacanjeId = pronadjeno.getIdPlacanja();
    }

    @Test
    @Order(2)
    void testDohvatiPlacanje() {
        assertTrue(testPlacanjeId > 0);

        Placanje placanje = placanjeDAO.placanje(testPlacanjeId);
        assertNotNull(placanje, "Placanje se mora pronaci u bazi");
        assertEquals(testPlacanjeId, placanje.getIdPlacanja());
        assertEquals(testKupacId, placanje.getKupac().getOsoba().getIdOsobe());
        assertEquals(150.0, placanje.getIznos());
    }

    @Test
    @Order(3)
    void testAzurirajPlacanje() {
        Placanje placanje = placanjeDAO.placanje(testPlacanjeId);
        assertNotNull(placanje);

        double noviIznos = 200.0;
        placanje.setIznos(noviIznos);

        placanje.setTipPlacanja(TipPlacanja.GOTOVINA);

        boolean azurirano = placanjeDAO.azurirajPlacanje(placanje);
        assertTrue(azurirano, "Placanje treba biti uspesno azurirano");

        Placanje placanjeAzurirano = placanjeDAO.placanje(testPlacanjeId);
        assertEquals(noviIznos, placanjeAzurirano.getIznos());
        assertEquals(TipPlacanja.GOTOVINA, placanjeAzurirano.getTipPlacanja());
    }

    @Test
    @Order(4)
    void testSvaPlacanja() {
        List<Placanje> lista = placanjeDAO.svaPlacanja();
        assertFalse(lista.isEmpty(), "Lista placanja ne sme biti prazna");
        assertTrue(lista.stream().anyMatch(p -> p.getIdPlacanja() == testPlacanjeId));
    }

    @Test
    @Order(5)
    void testObrisiPlacanje() {
        boolean obrisano = placanjeDAO.obrisiPlacanje(testPlacanjeId);
        assertTrue(obrisano, "Placanje treba biti uspesno obrisano");

        Placanje placanje = placanjeDAO.placanje(testPlacanjeId);
        assertNull(placanje, "Obrisano placanje ne sme postojati u bazi");
    }
}
