package org.unibl.etf.test.dao;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.unibl.etf.dao.mysql.*;
import org.unibl.etf.entity.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProizvodDataAccessImplTest {

    private static ProizvodDataAccessImpl proizvodDAO;
    private static PreradaMlijekaDataAccessImpl preradaDAO;
    private static final double CIJENA_TEST = 23.45;
    private static int testProizvodId;
    private static int testPreradaId;

    @BeforeAll
    static void setUp() {
        proizvodDAO = new ProizvodDataAccessImpl();
        preradaDAO = new PreradaMlijekaDataAccessImpl();

        List<PreradaMlijeka> svePrerade = preradaDAO.svaPreradaMlijeka();
        testPreradaId = svePrerade.get(0).getIdPrerade();
    }

    @Test
    @Order(1)
    void testDodajProizvod() {
        PreradaMlijeka prerada = preradaDAO.preradaMlijeka(testPreradaId);
        assertNotNull(prerada, "Prerada mlijeka mora postojati za dodavanje proizvoda");

        Proizvod novi = new Proizvod(0, prerada, TipProizvoda.SIR, CIJENA_TEST);  
        boolean dodano = proizvodDAO.dodajProizvod(novi);
        assertTrue(dodano, "Proizvod treba biti uspjesno dodan");

        List<Proizvod> svi = proizvodDAO.sviProizvodi();
        Proizvod pronadjeni = svi.stream()
            .filter(p -> p.getCijena() == CIJENA_TEST
                      && p.getPrerada().getIdPrerade() == testPreradaId
                      && p.getTip() == TipProizvoda.SIR)
            .findFirst()
            .orElse(null);

        assertNotNull(pronadjeni, "Dodani proizvod mora postojati u bazi");
        testProizvodId = pronadjeni.getIdProizvoda();
    }

    @Test
    @Order(2)
    void testDohvatiProizvod() {
        assertTrue(testProizvodId > 0);

        Proizvod proizvod = proizvodDAO.proizvod(testProizvodId);
        assertNotNull(proizvod, "Proizvod se mora pronaci u bazi");
        assertEquals(testProizvodId, proizvod.getIdProizvoda());
        assertEquals(testPreradaId, proizvod.getPrerada().getIdPrerade());
        assertEquals(CIJENA_TEST, proizvod.getCijena());
    }

    @Test
    @Order(3)
    void testAzurirajProizvod() {
        Proizvod proizvod = proizvodDAO.proizvod(testProizvodId);
        assertNotNull(proizvod);

        double novaCijena = CIJENA_TEST + 50.0;
        proizvod.setCijena(novaCijena);
        proizvod.setTip(TipProizvoda.KAJMAK);

        boolean azurirano = proizvodDAO.azurirajProizvod(proizvod);
        assertTrue(azurirano, "Proizvod treba biti uspjesno azuriran");

        Proizvod proizvodAzuriran = proizvodDAO.proizvod(testProizvodId);
        assertEquals(novaCijena, proizvodAzuriran.getCijena());
        assertEquals(TipProizvoda.KAJMAK, proizvodAzuriran.getTip());
    }

    @Test
    @Order(4)
    void testSviProizvodi() {
        List<Proizvod> lista = proizvodDAO.sviProizvodi();
        assertFalse(lista.isEmpty(), "Lista proizvoda ne smije biti prazna");
        assertTrue(lista.stream().anyMatch(p -> p.getIdProizvoda() == testProizvodId));
        assertNotNull(lista);
    }

    @Test
    @Order(5)
    void testObrisiProizvod() {
        boolean obrisano = proizvodDAO.obrisiProizvod(testProizvodId);
        assertTrue(obrisano, "Proizvod treba biti uspjesno obrisan");

        Proizvod proizvod = proizvodDAO.proizvod(testProizvodId);
        assertNull(proizvod, "Obrisani proizvod ne smije postojati u bazi");
    }
}
