package org.unibl.etf.test.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.unibl.etf.entity.*;

public class MjestoUlicaAdresaTest {

	@Test
    public void testConstructorAndGetters() {
		Mjesto mjesto = new Mjesto(78000, "Banja Luka");
		Ulica ulica = new Ulica("Kralja Petra I", mjesto);
    	Adresa adresa = new Adresa(1, ulica, 21);
        assertEquals(78000, mjesto.getPostanskiBroj());
        assertEquals("Banja Luka", mjesto.getNaziv());
        assertEquals("Kralja Petra I", ulica.getNazivUlice());
        assertEquals(mjesto, ulica.getMjesto());
        assertEquals(ulica, adresa.getUlica());
        assertEquals(21, adresa.getKucniBroj());
    }

    @Test
    public void testSetters() {
        Adresa adresa = new Adresa();
        Mjesto mjesto = new Mjesto(78000, "Banja Luka");
		Ulica ulica = new Ulica("Kralja Petra I", mjesto);
		adresa.setIdAdrese(2);
		adresa.setUlica(ulica);
		adresa.setKucniBroj(35);
		
        assertEquals(2, adresa.getIdAdrese());
        assertEquals(ulica, adresa.getUlica());
        assertEquals(35, adresa.getKucniBroj());
    }

    @Test
    public void testEqualsAndHashCode() {
    	Mjesto m1 = new Mjesto(78000, "Banja Luka");
    	Mjesto m2 = new Mjesto(78000, "Banja Luka");
    	Ulica u1 = new Ulica("Djure Djakovica", m1);
    	Ulica u2 = new Ulica("Djure Djakovica", m2);
    	Adresa a1 = new Adresa(1, u1, 21);
    	Adresa a2 = new Adresa(1, u2, 21);
        
        assertEquals(m1, m2);
        assertEquals(m1.hashCode(), m2.hashCode());
        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }
}
