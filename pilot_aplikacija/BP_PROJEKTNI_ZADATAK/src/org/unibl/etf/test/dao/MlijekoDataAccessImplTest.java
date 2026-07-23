package org.unibl.etf.test.dao;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.*;
import java.util.*;
import org.unibl.etf.dao.mysql.*;
import org.unibl.etf.entity.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MlijekoDataAccessImplTest {

    private static MlijekoDataAccessImpl mlijekoDAO;
    private static ZivotinjaDataAccessImpl zivotinjaDAO;
    private static DomacinstvoDataAccessImpl domacinstvoDAO;
    private static MjestoDataAccessImpl mjestoDAO;
    private static UlicaDataAccessImpl ulicaDAO;
    private static AdresaDataAccessImpl adresaDAO;
    private static final String JIB_TEST = "JIBTEST123";
    private static final int TEST_ID_ZIVOTINJE = 1;
    private static final int POSTANSKI_1 = 121212;
    private static int idAdreseTest;
    private static int idOsobaKontakt;
    private static int idMlijekaTest;

    @BeforeAll
    static void setUpAll() {
        mlijekoDAO = new MlijekoDataAccessImpl();
        zivotinjaDAO = new ZivotinjaDataAccessImpl();
        domacinstvoDAO = new DomacinstvoDataAccessImpl();
        mjestoDAO = new MjestoDataAccessImpl();
        ulicaDAO = new UlicaDataAccessImpl();
        adresaDAO = new AdresaDataAccessImpl();
        
        mjestoDAO.dodajMjesto(new Mjesto(POSTANSKI_1, "TestGrad"));
        ulicaDAO.dodajUlicu(new Ulica("TestUlica", new Mjesto(POSTANSKI_1, "TestGrad")));
        Adresa novaAdresa = new Adresa(0,
                new Ulica("TestUlica", new Mjesto(POSTANSKI_1, "TestGrad")),
                123);
        adresaDAO.dodajAdresu(novaAdresa);
        List<Adresa> sveAdrese = adresaDAO.adrese();
        Adresa dodata = sveAdrese.stream()
                .filter(a -> a.getUlica().getNazivUlice().equals("TestUlica") &&
                             a.getKucniBroj() == 123)
                .findFirst()
                .orElse(null);
        assertNotNull(dodata);
        idAdreseTest = dodata.getIdAdrese();
        
        if (domacinstvoDAO.domacinstvo(JIB_TEST) == null) {
        	Domacinstvo d = new Domacinstvo(
                    JIB_TEST,
                    "TestDomacinstvo",
                    new Adresa(idAdreseTest, new Ulica("TestUlica1", new Mjesto(POSTANSKI_1, "TestGrad1")), 10),
                    new Osoba(idOsobaKontakt, "KontaktIme", "KontaktPrezime")
            );
            boolean dodano = domacinstvoDAO.dodajDomacinstvo(d);
            assertTrue(dodano, "Domacinstvo za test mora biti kreirano");
        }
    }

    @Test
    @Order(1)
    void testDodajMlijeko() {
        Zivotinja zivotinja = zivotinjaDAO.zivotinja(TEST_ID_ZIVOTINJE);
        assertNotNull(zivotinja);

        Mlijeko mlijeko = new Mlijeko(
                0,
                LocalDate.now(),
                10.5,
                JedinicaMjere.LITAR,
                1,
                zivotinja
        );

        boolean result = mlijekoDAO.dodajMlijeko(mlijeko);
        assertTrue(result, "Mlijeko treba uspijesno da se doda u bazu");

        List<Mlijeko> lista = mlijekoDAO.mlijeko();
        Mlijeko zadnji = lista.stream()
            .filter(m -> m.getDatumMuze().equals(mlijeko.getDatumMuze()) &&
                         m.getZivotinja().getIdZivotinje() == TEST_ID_ZIVOTINJE &&
                         m.getKolicina() == 10.5)
            .findFirst().orElse(null);

        assertNotNull(zadnji, "Treba da bude pronadjeno zadnje ubaceno mlijeko");
        idMlijekaTest = zadnji.getIdMlijeka();
    }

    @Test
    @Order(2)
    void testDohvatiMlijekoPoId() {
        assertTrue(idMlijekaTest > 0);

        Mlijeko mlijeko = mlijekoDAO.mlijeko(idMlijekaTest);
        assertNotNull(mlijeko, "Mlijeko mora da postoji u bazi");
        assertEquals(idMlijekaTest, mlijeko.getIdMlijeka());
        assertEquals(TEST_ID_ZIVOTINJE, mlijeko.getZivotinja().getIdZivotinje());
    }

    @Test
    @Order(3)
    void testSvaMlijeka() {
        List<Mlijeko> lista = mlijekoDAO.mlijeko();
        assertNotNull(lista);
        assertFalse(lista.isEmpty(), "Lista svih mlijeka ne smije biti prazna");
    }

    @Test
    @Order(4)
    void testAzurirajMlijeko() {
        Mlijeko mlijeko = mlijekoDAO.mlijeko(idMlijekaTest);
        assertNotNull(mlijeko);

        mlijeko.setKolicina(20.0);
        mlijeko.setKvalitet(5);

        Zivotinja novaZivotinja = new Zivotinja(0, "Test MILKAA", TipZivotinje.fromId(1), domacinstvoDAO.domacinstvo(JIB_TEST));
        boolean dodano = zivotinjaDAO.dodajZivotinju(novaZivotinja);
        assertTrue(dodano);

        List<Zivotinja> lista = zivotinjaDAO.sveZivotinje();
        novaZivotinja = lista.stream()
                             .filter(z -> "Test MILKAA".equals(z.getIme()))
                             .findFirst()
                             .orElseThrow(() -> new RuntimeException("Nova zivotinja nije pronađena nakon unosa"));

        mlijeko.setZivotinja(novaZivotinja);

        boolean result = mlijekoDAO.azurirajMlijeko(mlijeko);
        assertTrue(result);

        Mlijeko azurirano = mlijekoDAO.mlijeko(idMlijekaTest);
        assertNotNull(azurirano);
        assertEquals(20.0, azurirano.getKolicina());
        assertEquals(5, azurirano.getKvalitet());
        assertEquals(novaZivotinja.getIdZivotinje(), azurirano.getZivotinja().getIdZivotinje());
    }


    @Test
    @Order(5)
    void testObrisiMlijeko() {
        boolean result = mlijekoDAO.obrisiMlijeko(idMlijekaTest);
        assertTrue(result);

        Mlijeko mlijeko = mlijekoDAO.mlijeko(idMlijekaTest);
        assertNull(mlijeko);
    }
}
