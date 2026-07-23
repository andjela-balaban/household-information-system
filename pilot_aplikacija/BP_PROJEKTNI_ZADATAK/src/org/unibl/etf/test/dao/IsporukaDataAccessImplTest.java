package org.unibl.etf.test.dao;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.*;
import java.util.*;
import org.unibl.etf.dao.mysql.*;
import org.unibl.etf.entity.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IsporukaDataAccessImplTest {

    private static IsporukaDataAccessImpl isporukaDAO;
    private static KupacDataAccessImpl kupacDAO;
    private static ZaposleniDataAccessImpl zaposleniDAO;
    private static final int TEST_KUPAC_ID = 12;
    private static final int TEST_ZAPOSLENI_ID = 4;
    private static int testIsporukaId;

    @BeforeAll
    static void setUp() {
        isporukaDAO = new IsporukaDataAccessImpl();
        kupacDAO = new KupacDataAccessImpl();
        zaposleniDAO = new ZaposleniDataAccessImpl();
    }

    @Test
    @Order(1)
    void testDodajIsporuku() {
        Kupac kupac = kupacDAO.kupac(TEST_KUPAC_ID);
        assertNotNull(kupac, "Kupac mora postojati");

        Zaposleni zaposleni = zaposleniDAO.zaposleni(TEST_ZAPOSLENI_ID);
        assertNotNull(zaposleni, "Zaposleni mora postojati");


        Adresa adresa = kupac.getOsoba().getAdresa(); 

        Isporuka nova = new Isporuka(
            0,
            LocalDate.now(),
            adresa,
            kupac,
            zaposleni
        );

        boolean dodano = isporukaDAO.dodajIsporuku(nova);
        assertTrue(dodano, "Isporuka treba da bude uspesno dodata");

        List<Isporuka> sve = isporukaDAO.isporuke();
        Isporuka nadjena = sve.stream()
            .filter(i -> i.getDatumIsporuke().equals(nova.getDatumIsporuke()) 
                      && i.getKupac().getOsoba().getIdOsobe() == TEST_KUPAC_ID
                      && i.getZaposleniIsporucilac().getOsoba().getIdOsobe() == TEST_ZAPOSLENI_ID)
            .findFirst()
            .orElse(null);

        assertNotNull(nadjena, "Dodata isporuka mora postojati u bazi");
        testIsporukaId = nadjena.getIdIsporuke();
    }

    @Test
    @Order(2)
    void testDohvatiIsporukuPoId() {
        assertTrue(testIsporukaId > 0);

        Isporuka isporuka = isporukaDAO.isporuka(testIsporukaId);
        assertNotNull(isporuka, "Isporuka se mora pronaci u bazi");
        assertEquals(testIsporukaId, isporuka.getIdIsporuke());
        assertEquals(TEST_KUPAC_ID, isporuka.getKupac().getOsoba().getIdOsobe());
        assertEquals(TEST_ZAPOSLENI_ID, isporuka.getZaposleniIsporucilac().getOsoba().getIdOsobe());
    }

    @Test
    @Order(3)
    void testAzurirajIsporuku() {
        Isporuka isporuka = isporukaDAO.isporuka(testIsporukaId);
        assertNotNull(isporuka, "Isporuka za ažuriranje mora postojati");

        LocalDate noviDatum = LocalDate.now().plusDays(1);
        isporuka.setDatumIsporuke(noviDatum);

        boolean azurirano = isporukaDAO.azurirajIsporuku(isporuka);
        assertTrue(azurirano, "Isporuka treba da bude uspesno azurirana");

        Isporuka isporukaAzurirana = isporukaDAO.isporuka(testIsporukaId);
        assertEquals(noviDatum, isporukaAzurirana.getDatumIsporuke());
    }

    @Test
    @Order(4)
    void testSveIsporuke() {
        List<Isporuka> lista = isporukaDAO.isporuke();
        assertFalse(lista.isEmpty(), "Lista isporuka ne sme biti prazna");
        assertNotNull(lista); 
    }

    @Test
    @Order(5)
    void testObrisiIsporuku() {
        Isporuka isporuka = isporukaDAO.isporuka(testIsporukaId);
        assertNotNull(isporuka, "Isporuka za brisanje mora postojati");

        boolean obrisano = isporukaDAO.obrisiIsporuku(testIsporukaId);
        assertTrue(obrisano, "Isporuka treba da bude uspesno obrisana");

        Isporuka izBaze = isporukaDAO.isporuka(testIsporukaId);
        assertNull(izBaze, "Isporuka ne sme postojati u bazi nakon brisanja");
    }
}

