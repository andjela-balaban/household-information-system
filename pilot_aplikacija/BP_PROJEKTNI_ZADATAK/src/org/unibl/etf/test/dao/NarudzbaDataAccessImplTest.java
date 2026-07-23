package org.unibl.etf.test.dao;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.*;
import java.util.*;
import org.unibl.etf.dao.mysql.*;
import org.unibl.etf.entity.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NarudzbaDataAccessImplTest {

    private static NarudzbaDataAccessImpl narudzbaDAO;
    private static KupacDataAccessImpl kupacDAO;
    private static PlacanjeDataAccessImpl placanjeDAO;
    private static IsporukaDataAccessImpl isporukaDAO;
    private static final int TEST_KUPAC_ID = 13;
    private static final int TEST_PLACANJE_ID = 7;
    private static final int TEST_ISPORUKA_ID = 8;
    private static final int TEST_STATUS_NARUDZBE_ID = 1;
    private static int testNarudzbaId;

    @BeforeAll
    static void setUp() {
        narudzbaDAO = new NarudzbaDataAccessImpl();
        kupacDAO = new KupacDataAccessImpl();
        placanjeDAO = new PlacanjeDataAccessImpl();
        isporukaDAO = new IsporukaDataAccessImpl();
    }

    @Test
    @Order(1)
    void testDodajNarudzbu() {
        Kupac kupac = kupacDAO.kupac(TEST_KUPAC_ID);
        assertNotNull(kupac, "Kupac mora postojati");

        Placanje placanje = placanjeDAO.placanje(TEST_PLACANJE_ID);
        assertNotNull(placanje, "Placanje mora postojati");

        Isporuka isporuka = isporukaDAO.isporuka(TEST_ISPORUKA_ID);
        assertNotNull(isporuka, "Isporuka mora postojati");

        Narudzba nova = new Narudzba(
            0, 
            StatusNarudzbe.fromId(TEST_STATUS_NARUDZBE_ID), 
            LocalDate.now(),
            kupac,
            placanje,
            isporuka
        );

        boolean dodato = narudzbaDAO.dodajNarudzbu(nova);
        assertTrue(dodato, "Narudzba treba da bude uspjesno dodata");

        List<Narudzba> sve = narudzbaDAO.narudzbe();
        Narudzba nadjena = sve.stream()
            .filter(n -> n.getDatumNarudzbe().equals(nova.getDatumNarudzbe())
                    && n.getKupac().getOsoba().getIdOsobe() == TEST_KUPAC_ID
                    && n.getPlacanje().getIdPlacanja() == TEST_PLACANJE_ID
                    && n.getIsporuka().getIdIsporuke() == TEST_ISPORUKA_ID)
            .findFirst()
            .orElse(null);

        assertNotNull(nadjena, "Dodana narudzba mora postojati u bazi");
        testNarudzbaId = nadjena.getIdNarudzbe();
    }

    @Test
    @Order(2)
    void testDohvatiNarudzbuPoId() {
        assertTrue(testNarudzbaId > 0);

        Narudzba narudzba = narudzbaDAO.narudzba(testNarudzbaId);
        assertNotNull(narudzba, "Narudzba se mora pronaci u bazi");
        assertEquals(testNarudzbaId, narudzba.getIdNarudzbe());
        assertEquals(TEST_KUPAC_ID, narudzba.getKupac().getOsoba().getIdOsobe());
        assertEquals(TEST_PLACANJE_ID, narudzba.getPlacanje().getIdPlacanja());
        assertEquals(TEST_ISPORUKA_ID, narudzba.getIsporuka().getIdIsporuke());
    }

    @Test
    @Order(3)
    void testAzurirajNarudzbu() {
        Narudzba narudzba = narudzbaDAO.narudzba(testNarudzbaId);
        assertNotNull(narudzba, "Narudzba za azuriranje mora postojati");

        LocalDate noviDatum = LocalDate.now().plusDays(1);
        narudzba.setDatumNarudzbe(noviDatum);
        narudzba.setStatus(StatusNarudzbe.NEPLACENA); 

        boolean azurirano = narudzbaDAO.azurirajNarudzbu(narudzba);
        assertTrue(azurirano, "Narudzba treba da bude uspesno azurirana");

        Narudzba izBaze = narudzbaDAO.narudzba(testNarudzbaId);
        assertEquals(noviDatum, izBaze.getDatumNarudzbe());
        assertEquals(StatusNarudzbe.NEPLACENA, izBaze.getStatus());
    }

    @Test
    @Order(4)
    void testSveNarudzbe() {
        List<Narudzba> lista = narudzbaDAO.narudzbe();
        assertFalse(lista.isEmpty(), "Lista narudzbi ne sme biti prazna");
        assertNotNull(lista);
    }

    @Test
    @Order(5)
    void testObrisiNarudzbu() {
        Narudzba narudzba = narudzbaDAO.narudzba(testNarudzbaId);
        assertNotNull(narudzba, "Narudzba za brisanje mora postojati");

        boolean obrisano = narudzbaDAO.obrisiNarudzbu(testNarudzbaId);
        assertTrue(obrisano, "Narudzba treba da bude uspesno obrisana");

        Narudzba izBaze = narudzbaDAO.narudzba(testNarudzbaId);
        assertNull(izBaze, "Narudzba ne sme postojati u bazi nakon brisanja");
    }
}
