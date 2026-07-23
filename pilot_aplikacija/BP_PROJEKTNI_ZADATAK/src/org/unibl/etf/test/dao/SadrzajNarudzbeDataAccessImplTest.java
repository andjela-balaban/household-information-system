package org.unibl.etf.test.dao;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.unibl.etf.dao.mysql.*;
import org.unibl.etf.entity.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SadrzajNarudzbeDataAccessImplTest {

    private static SadrzajNarudzbeDataAccessImpl sadrzajDAO;
    private static NarudzbaDataAccessImpl narudzbaDAO;
    private static ProizvodDataAccessImpl proizvodDAO;
    private static final int TEST_NARUDZBA_ID = 9;
    private static final int TEST_PROIZVOD_ID = 15;
    private static int testKolicina = 2;

    @BeforeAll
    static void setUp() {
        sadrzajDAO = new SadrzajNarudzbeDataAccessImpl();
        narudzbaDAO = new NarudzbaDataAccessImpl();
        proizvodDAO = new ProizvodDataAccessImpl();
    }

    @Test
    @Order(1)
    void testDodajSadrzajNarudzbe() {
        Narudzba narudzba = narudzbaDAO.narudzba(TEST_NARUDZBA_ID);
        assertNotNull(narudzba, "Narudzba mora postojati");

        Proizvod proizvod = proizvodDAO.proizvod(TEST_PROIZVOD_ID);
        assertNotNull(proizvod, "Proizvod mora postojati");

        SadrzajNarudzbe noviSadrzaj = new SadrzajNarudzbe(narudzba, proizvod, testKolicina);

        boolean dodato = sadrzajDAO.dodajSadrzajNarudzbe(noviSadrzaj);
        assertTrue(dodato, "Sadrzaj narudzbe treba biti uspjesno dodat");

        SadrzajNarudzbe izBaze = sadrzajDAO.sadrzajNarudzbe(TEST_NARUDZBA_ID, TEST_PROIZVOD_ID);
        assertNotNull(izBaze, "Sadrzaj narudzbe mora postojati u bazi");
        assertEquals(testKolicina, izBaze.getKolicina());
    }

    @Test
    @Order(2)
    void testDohvatiSadrzajNarudzbe() {
        SadrzajNarudzbe sadrzaj = sadrzajDAO.sadrzajNarudzbe(TEST_NARUDZBA_ID, TEST_PROIZVOD_ID);
        assertNotNull(sadrzaj, "Sadrzaj narudzbe se mora pronaci u bazi");
        assertEquals(TEST_NARUDZBA_ID, sadrzaj.getNarudzba().getIdNarudzbe());
        assertEquals(TEST_PROIZVOD_ID, sadrzaj.getProizvod().getIdProizvoda());
    }

    @Test
    @Order(3)
    void testAzurirajSadrzajNarudzbe() {
        SadrzajNarudzbe sadrzaj = sadrzajDAO.sadrzajNarudzbe(TEST_NARUDZBA_ID, TEST_PROIZVOD_ID);
        assertNotNull(sadrzaj, "Sadrzaj narudzbe za azuriranje mora postojati");

        int novaKolicina = sadrzaj.getKolicina() + 3;
        sadrzaj.setKolicina(novaKolicina);

        boolean azurirano = sadrzajDAO.azurirajSadrzajNarudzbe(sadrzaj);
        assertTrue(azurirano, "Sadrzaj narudzbe treba biti uspjesno azuriran");

        SadrzajNarudzbe izBaze = sadrzajDAO.sadrzajNarudzbe(TEST_NARUDZBA_ID, TEST_PROIZVOD_ID);
        assertEquals(novaKolicina, izBaze.getKolicina());
    }

    @Test
    @Order(4)
    void testObrisiSadrzajNarudzbe() {
        boolean obrisano = sadrzajDAO.obrisiSadrzajNarudzbe(TEST_NARUDZBA_ID, TEST_PROIZVOD_ID);
        assertTrue(obrisano, "Sadrzaj narudzbe treba biti uspjesno obrisan");

        SadrzajNarudzbe izBaze = sadrzajDAO.sadrzajNarudzbe(TEST_NARUDZBA_ID, TEST_PROIZVOD_ID);
        assertNull(izBaze, "Sadrzaj narudzbe ne sme postojati u bazi nakon brisanja");
    }
}
