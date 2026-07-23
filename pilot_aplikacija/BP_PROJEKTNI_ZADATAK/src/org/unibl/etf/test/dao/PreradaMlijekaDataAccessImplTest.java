package org.unibl.etf.test.dao;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.*;
import java.util.*;
import org.unibl.etf.dao.mysql.*;
import org.unibl.etf.entity.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PreradaMlijekaDataAccessImplTest {

    private static PreradaMlijekaDataAccessImpl preradaDAO;
    private static MlijekoDataAccessImpl mlijekoDAO;
    private static DomacinstvoDataAccessImpl domacinstvoDAO;
    private static ZaposleniDataAccessImpl zaposleniDAO;

    private static final String JIB_TEST = "JIBTEST123";
    private static int testPreradaId;
    private static int testMlijekoId;
    private static int testZaposleniId;

    @BeforeAll
    static void setUp() {
        preradaDAO = new PreradaMlijekaDataAccessImpl();
        mlijekoDAO = new MlijekoDataAccessImpl();
        domacinstvoDAO = new DomacinstvoDataAccessImpl();
        zaposleniDAO = new ZaposleniDataAccessImpl();
        List<Zaposleni> zaposleniLista = zaposleniDAO.sviZaposleni();
        testZaposleniId = zaposleniLista.get(0).getOsoba().getIdOsobe();
        List<Mlijeko> mlijekoLista = mlijekoDAO.mlijeko();
        testMlijekoId = mlijekoLista.get(0).getIdMlijeka();
    }

    @Test
    @Order(1)
    void testDodajPreraduMlijeka() {
        Mlijeko mlijeko = mlijekoDAO.mlijeko(testMlijekoId);
        Domacinstvo domacinstvo = domacinstvoDAO.domacinstvo(JIB_TEST);
        Zaposleni zaposleni = zaposleniDAO.zaposleni(testZaposleniId);

        PreradaMlijeka novaPrerada = new PreradaMlijeka(
            0,
            mlijeko,
            domacinstvo,
            zaposleni,
            LocalDate.now(),
            15.5,
            JedinicaMjere.KILOGRAM
        );

        boolean dodano = preradaDAO.dodajPreraduMlijeka(novaPrerada);
        assertTrue(dodano, "Prerada mlijeka treba biti uspjesno dodana");

        List<PreradaMlijeka> sve = preradaDAO.svaPreradaMlijeka();
        PreradaMlijeka pronadjena = sve.stream()
            .filter(p -> p.getDatumPrerade().equals(novaPrerada.getDatumPrerade()) &&
                         p.getKolicinaZaPreradu() == novaPrerada.getKolicinaZaPreradu() &&
                         p.getDomacinstvo().getJib().equals(JIB_TEST))
            .findFirst()
            .orElse(null);

        assertNotNull(pronadjena, "Dodana prerada mlijeka mora postojati u bazi");
        testPreradaId = pronadjena.getIdPrerade();
    }

    @Test
    @Order(2)
    void testDohvatiPreraduMlijeka() {
        PreradaMlijeka prerada = preradaDAO.preradaMlijeka(testPreradaId);
        assertNotNull(prerada, "Prerada mlijeka se mora pronaci u bazi");
        assertEquals(testPreradaId, prerada.getIdPrerade());
        assertEquals(JIB_TEST, prerada.getDomacinstvo().getJib());
    }

    @Test
    @Order(3)
    void testAzurirajPreraduMlijeka() {
        PreradaMlijeka prerada = preradaDAO.preradaMlijeka(testPreradaId);
        assertNotNull(prerada);

        prerada.setKolicinaZaPreradu(25.0);
        prerada.setDatumPrerade(LocalDate.now().minusDays(1));

        boolean azurirano = preradaDAO.azurirajPreraduMlijeka(prerada);
        assertTrue(azurirano, "Prerada mlijeka treba biti uspješno ažurirana");

        PreradaMlijeka preradaAzurirana = preradaDAO.preradaMlijeka(testPreradaId);
        assertEquals(25.0, preradaAzurirana.getKolicinaZaPreradu());
        assertEquals(LocalDate.now().minusDays(1), preradaAzurirana.getDatumPrerade());
    }

    @Test
    @Order(4)
    void testSvaPreradaMlijeka() {
        List<PreradaMlijeka> lista = preradaDAO.svaPreradaMlijeka();
        assertFalse(lista.isEmpty(), "Lista prerada mlijeka ne smije biti prazna");
        assertTrue(lista.stream().anyMatch(p -> p.getIdPrerade() == testPreradaId));
        assertNotNull(lista);
    }

    @Test
    @Order(5)
    void testObrisiPreraduMlijeka() {
        boolean obrisano = preradaDAO.obrisiPreraduMlijeka(testPreradaId);
        assertTrue(obrisano, "Prerada mlijeka treba biti uspjesno obrisana");

        PreradaMlijeka prerada = preradaDAO.preradaMlijeka(testPreradaId);
        assertNull(prerada, "Obrisana prerada mlijeka ne smije postojati u bazi");
    }
}
