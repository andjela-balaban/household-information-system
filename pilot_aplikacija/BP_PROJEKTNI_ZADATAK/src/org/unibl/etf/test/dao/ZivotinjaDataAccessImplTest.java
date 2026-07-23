package org.unibl.etf.test.dao;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.unibl.etf.dao.mysql.*;
import org.unibl.etf.entity.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ZivotinjaDataAccessImplTest {

    private static ZivotinjaDataAccessImpl zivotinjaDAO;
    private static DomacinstvoDataAccessImpl domacinstvoDAO;
    private static final String JIB_TEST = "JIBTEST123";
    private static int testZivotinjaId;

    @BeforeAll
    static void setUp() {
        zivotinjaDAO = new ZivotinjaDataAccessImpl();
        domacinstvoDAO = new DomacinstvoDataAccessImpl();
    }

    @Test
    @Order(1)
    void testDodajZivotinju() {
        Zivotinja nova = new Zivotinja(0, "Test Zivotinja", TipZivotinje.fromId(1), domacinstvoDAO.domacinstvo(JIB_TEST));
        boolean dodano = zivotinjaDAO.dodajZivotinju(nova);
        assertTrue(dodano, "Zivotinja treba da bude uspesno dodata");

        List<Zivotinja> sve = zivotinjaDAO.sveZivotinje();
        Zivotinja nadjena = sve.stream()
                .filter(z -> z.getIme().equals("Test Zivotinja") && z.getDomacinstvo().getJib().equals(JIB_TEST))
                .findFirst()
                .orElse(null);

        assertNotNull(nadjena, "Dodana zivotinja mora postojati u bazi");
        testZivotinjaId = nadjena.getIdZivotinje();
    }
    
    @Test
    @Order(2)
    void testDohvatiZivotinjuPoId() {
        assertTrue(testZivotinjaId > 0);

        Zivotinja zivotinja = zivotinjaDAO.zivotinja(testZivotinjaId);
        assertNotNull(zivotinja, "Zivotinja se mora pronaci u bazi");
        assertEquals("Test Zivotinja", zivotinja.getIme());
        assertEquals(JIB_TEST, zivotinja.getDomacinstvo().getJib());
    }

    @Test
    @Order(3)
    void testAzurirajZivotinju() {
        Zivotinja nova = new Zivotinja(0, "Azur Test", TipZivotinje.fromId(1), domacinstvoDAO.domacinstvo(JIB_TEST));
        assertTrue(zivotinjaDAO.dodajZivotinju(nova));

        Zivotinja dodana = zivotinjaDAO.sveZivotinje().stream()
                .filter(z -> z.getIme().equals("Azur Test"))
                .findFirst()
                .orElseThrow();

        dodana.setIme("Azurirana Zivotinja");
        dodana.setTip(TipZivotinje.KOZA);
        assertTrue(zivotinjaDAO.azurirajZivotinju(dodana));

        Zivotinja izBaze = zivotinjaDAO.zivotinja(dodana.getIdZivotinje());
        assertEquals("Azurirana Zivotinja", izBaze.getIme());
    }
    
    @Test
    @Order(4)
    void testSveZivotinje() {
        List<Zivotinja> lista = zivotinjaDAO.sveZivotinje();
        assertFalse(lista.isEmpty(), "Lista zivotinja ne sme biti prazna");
    }


    @Test
    @Order(5)
    void testObrisiZivotinju() {
        Zivotinja nova = new Zivotinja(0, "Brisi Test", TipZivotinje.fromId(1), domacinstvoDAO.domacinstvo(JIB_TEST));
        assertTrue(zivotinjaDAO.dodajZivotinju(nova));

        Zivotinja dodana = zivotinjaDAO.sveZivotinje().stream()
                .filter(z -> z.getIme().equals("Brisi Test"))
                .findFirst()
                .orElseThrow();

        assertTrue(zivotinjaDAO.obrisiZivotinju(dodana.getIdZivotinje()));

        assertNull(zivotinjaDAO.zivotinja(dodana.getIdZivotinje()));
    }
}
