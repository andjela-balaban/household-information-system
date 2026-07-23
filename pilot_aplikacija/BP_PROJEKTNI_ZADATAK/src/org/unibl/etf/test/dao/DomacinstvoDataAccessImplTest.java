package org.unibl.etf.test.dao;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.unibl.etf.dao.mysql.*;
import org.unibl.etf.entity.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DomacinstvoDataAccessImplTest {

    private static MjestoDataAccessImpl mjestoDAO;
    private static UlicaDataAccessImpl ulicaDAO;
    private static AdresaDataAccessImpl adresaDAO;
    private static OsobaDataAccessImpl osobaDAO;
    private static DomacinstvoDataAccessImpl domacinstvoDAO;

    private static final int POSTANSKI_1 = 2222;
    private static final int POSTANSKI_2 = 66666;

    private static int idAdresa1;
    private static int idAdresa2;
    private static int idOsobaKontakt;
    private static final String JIB_TEST = "JIBTEST123";

    @BeforeAll
    static void setUpAll() {
        mjestoDAO = new MjestoDataAccessImpl();
        ulicaDAO = new UlicaDataAccessImpl();
        adresaDAO = new AdresaDataAccessImpl();
        osobaDAO = new OsobaDataAccessImpl();
        domacinstvoDAO = new DomacinstvoDataAccessImpl();

        // Kreiramo mjesto 1 i ulicu 1
        mjestoDAO.dodajMjesto(new Mjesto(POSTANSKI_1, "TestGrad1"));
        ulicaDAO.dodajUlicu(new Ulica("TestUlica1", new Mjesto(POSTANSKI_1, "TestGrad1")));
        adresaDAO.dodajAdresu(new Adresa(0, new Ulica("TestUlica1", new Mjesto(POSTANSKI_1, "TestGrad1")), 10));
        idAdresa1 = adresaDAO.adrese().stream()
                .filter(a -> a.getUlica().getNazivUlice().equals("TestUlica1") && a.getKucniBroj() == 10)
                .findFirst().get().getIdAdrese();

        // Kreiramo mjesto 2 i ulicu 2 (za update)
        mjestoDAO.dodajMjesto(new Mjesto(POSTANSKI_2, "TestGrad2"));
        ulicaDAO.dodajUlicu(new Ulica("TestUlica2", new Mjesto(POSTANSKI_2, "TestGrad2")));
        adresaDAO.dodajAdresu(new Adresa(0, new Ulica("TestUlica2", new Mjesto(POSTANSKI_2, "TestGrad2")), 20));
        idAdresa2 = adresaDAO.adrese().stream()
                .filter(a -> a.getUlica().getNazivUlice().equals("TestUlica2") && a.getKucniBroj() == 20)
                .findFirst().get().getIdAdrese();

        // Kreiramo kontakt osobu
        osobaDAO.dodajOsobu(new Osoba(0, "KontaktIme", "KontaktPrezime"));
        idOsobaKontakt = osobaDAO.osobe().stream()
                .filter(o -> o.getIme().equals("KontaktIme") && o.getPrezime().equals("KontaktPrezime"))
                .findFirst().get().getIdOsobe();
    }

    @Test
    @Order(1)
    void testDodajDomacinstvo() {
        Domacinstvo d = new Domacinstvo(
                JIB_TEST,
                "TestDomacinstvo",
                new Adresa(idAdresa1, new Ulica("TestUlica1", new Mjesto(POSTANSKI_1, "TestGrad1")), 10),
                new Osoba(idOsobaKontakt, "KontaktIme", "KontaktPrezime")
        );
        boolean result = domacinstvoDAO.dodajDomacinstvo(d);
        assertTrue(result);
    }

    @Test
    @Order(2)
    void testDomacinstvoPoJIB() {
        Domacinstvo d = domacinstvoDAO.domacinstvo(JIB_TEST);
        assertNotNull(d);
        assertEquals("TestDomacinstvo", d.getNaziv());
        assertEquals(POSTANSKI_1, d.getAdresa().getUlica().getMjesto().getPostanskiBroj());
    }

    @Test
    @Order(3)
    void testSvaDomacinstva() {
        List<Domacinstvo> lista = domacinstvoDAO.svaDomacinstva();
        assertNotNull(lista);
        assertTrue(lista.stream().anyMatch(d -> JIB_TEST.equals(d.getJib())));
    }

    @Test
    @Order(4)
    void testAzurirajDomacinstvo() {
        Domacinstvo d = new Domacinstvo(
                JIB_TEST,
                "NovoImeDomacinstva",
                new Adresa(idAdresa2, new Ulica("TestUlica2", new Mjesto(POSTANSKI_2, "TestGrad2")), 20),
                osobaDAO.osoba(idOsobaKontakt)
        );
        boolean result = domacinstvoDAO.azurirajDomacinstvo(d);
        assertTrue(result);

        Domacinstvo provjera = domacinstvoDAO.domacinstvo(JIB_TEST);
        assertEquals("NovoImeDomacinstva", provjera.getNaziv());
        assertEquals(POSTANSKI_2, provjera.getAdresa().getUlica().getMjesto().getPostanskiBroj());
        assertNotNull(provjera.getKontaktOsoba());
    }

    @Test
    @Order(5)
    void testObrisiDomacinstvo() {
        boolean result = domacinstvoDAO.obrisiDomacinstvo(JIB_TEST);
        assertTrue(result);
        assertNull(domacinstvoDAO.domacinstvo(JIB_TEST));
    }
}
