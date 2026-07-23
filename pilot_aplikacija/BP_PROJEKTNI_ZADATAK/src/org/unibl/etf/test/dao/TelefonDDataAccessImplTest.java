package org.unibl.etf.test.dao;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.unibl.etf.dao.mysql.*;
import org.unibl.etf.entity.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TelefonDDataAccessImplTest {

    private TelefonDDataAccessImpl telefonDDAO;

    @BeforeEach
    void setUp() {
        telefonDDAO = new TelefonDDataAccessImpl();
    }

    @Test
    @Order(1)
    void testDodajTelefon() {
        Domacinstvo domacinstvo = new Domacinstvo();
        domacinstvo.setJib("1234567890123");
        TelefonD telefon = new TelefonD(domacinstvo, "061123456");

        boolean result = telefonDDAO.dodajTelefon(telefon);
        assertTrue(result, "Telefon treba biti uspješno dodat");
    }

    @Test
    @Order(2)
    void testTelefonPoBrojuIJB() {
        String brojTelefona = "061123456";
        String jib = "1234567890123";

        TelefonD telefon = telefonDDAO.telefon(brojTelefona, jib);
        assertNotNull(telefon, "TelefonD ne smije biti null");
        assertEquals(brojTelefona, telefon.getBrojTelefona());
        assertEquals(jib, telefon.getDomacinstvo().getJib());
        assertNotNull(telefon.getDomacinstvo().getAdresa(), "Domacinstvo treba imati adresu");
        if (telefon.getDomacinstvo().getKontaktOsoba() != null) {
            assertNotNull(telefon.getDomacinstvo().getKontaktOsoba().getIme());
            assertNotNull(telefon.getDomacinstvo().getKontaktOsoba().getPrezime());
        }
    }

    @Test
    @Order(3)
    void testTelefoniZaDomacinstvo() {
        String jib = "1234567890123";
        
        List<TelefonD> telefoni = telefonDDAO.telefoniZaDomacinstvo(jib);
        assertNotNull(telefoni, "Lista telefona ne smije biti null");
        assertFalse(telefoni.isEmpty(), "Lista telefona ne smije biti prazna");

        for (TelefonD t : telefoni) {
            assertEquals(jib, t.getDomacinstvo().getJib());
            assertNotNull(t.getBrojTelefona());
        }
    }

    @Test
    @Order(4)
    void testAzurirajTelefon() {
        String jib = "1234567890123";
        TelefonD stariTelefon = telefonDDAO.telefon("061123456", jib);
        assertNotNull(stariTelefon, "Stari telefon mora postojati za test");

        TelefonD noviTelefon = new TelefonD(stariTelefon.getDomacinstvo(), "061654321");

        boolean result = telefonDDAO.azurirajTelefon(noviTelefon, stariTelefon.getBrojTelefona());
        assertTrue(result, "Telefon treba biti uspjesno azuriran");

        TelefonD azuriranTelefon = telefonDDAO.telefon(noviTelefon.getBrojTelefona(), jib);
        assertNotNull(azuriranTelefon, "Azurirani telefon ne smije biti null");
        assertEquals(noviTelefon.getBrojTelefona(), azuriranTelefon.getBrojTelefona());
    }

    @Test
    @Order(5)
    void testObrisiTelefon() {
        String jib = "1234567890123";
        String brojTelefona = "061654321";

        boolean result = telefonDDAO.obrisiTelefon(jib, brojTelefona);
        assertTrue(result, "Telefon treba biti uspjesno obrisan");

        TelefonD telefon = telefonDDAO.telefon(brojTelefona, jib);
        assertNull(telefon, "Telefon nakon brisanja treba biti null");
    }
}
