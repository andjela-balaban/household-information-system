package org.unibl.etf.test.dao;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.unibl.etf.dao.mysql.*;
import org.unibl.etf.entity.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TelefonODataAccessImplTest {

    private TelefonODataAccessImpl telefonODAO;

    @BeforeEach
    void setUp() {
        telefonODAO = new TelefonODataAccessImpl();
    }

    @Test
    @Order(1)
    void testDodajTelefon() {
        Osoba osoba = new Osoba(1, "TestIme", "TestPrezime");
        TelefonO telefon = new TelefonO(osoba, "061123456");
        
        boolean result = telefonODAO.dodajTelefon(telefon);
        assertTrue(result, "Telefon treba da bude uspjesno dodat");
    }

    @Test
    @Order(2)
    void testTelefonPoBrojuIOsobi() {
        String brojTelefona = "061123456"; 
        int idOsobe = 1; 
        
        TelefonO telefon = telefonODAO.telefon(brojTelefona, idOsobe);
        assertNotNull(telefon, "TelefonO ne smije biti null");
        assertEquals(brojTelefona, telefon.getBrojTelefona());
        assertEquals(idOsobe, telefon.getOsoba().getIdOsobe());
        assertNotNull(telefon.getOsoba().getIme());
        assertNotNull(telefon.getOsoba().getPrezime());
    }

    @Test
    @Order(3)
    void testTelefoniZaOsobu() {
        int idOsobe = 1;
        
        List<TelefonO> telefoni = telefonODAO.telefoniZaOsobu(idOsobe);
        assertNotNull(telefoni, "Lista telefona ne smije biti null");
        assertFalse(telefoni.isEmpty(), "Lista telefona ne smije biti prazna");

        for (TelefonO t : telefoni) {
            assertEquals(idOsobe, t.getOsoba().getIdOsobe());
            assertNotNull(t.getBrojTelefona());
        }
    }

    @Test
    @Order(4)
    void testAzurirajTelefon() {
        Osoba osoba = new Osoba(1, "TestIme", "TestPrezime");
        TelefonO stariTelefon = new TelefonO(osoba, "061123456");
        TelefonO noviTelefon = new TelefonO(osoba, "061654321");
        
        boolean result = telefonODAO.azurirajTelefon(noviTelefon, stariTelefon.getBrojTelefona());
        assertTrue(result, "Telefon treba biti uspjesno azuriran");
        
        TelefonO azuriranTelefon = telefonODAO.telefon(noviTelefon.getBrojTelefona(), osoba.getIdOsobe());
        assertNotNull(azuriranTelefon);
        assertEquals(noviTelefon.getBrojTelefona(), azuriranTelefon.getBrojTelefona());
    }

    @Test
    @Order(5)
    void testObrisiTelefon() {
        int idOsobe = 1;
        String brojTelefona = "061654321"; 

        boolean result = telefonODAO.obrisiTelefon(idOsobe, brojTelefona);
        assertTrue(result, "Telefon treba biti uspješno obrisan");

        TelefonO telefon = telefonODAO.telefon(brojTelefona, idOsobe);
        assertNull(telefon, "Telefon nakon brisanja treba biti null");
    }
}
