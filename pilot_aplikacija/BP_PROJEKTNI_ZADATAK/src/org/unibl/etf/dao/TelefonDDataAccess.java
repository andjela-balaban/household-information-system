package org.unibl.etf.dao;

import java.util.*;
import org.unibl.etf.entity.*;

public interface TelefonDDataAccess {
	TelefonD telefon(String brojTelefona, String jib);
	List<TelefonD> telefoniZaDomacinstvo(String jib);
	boolean dodajTelefon(TelefonD telefon);
	boolean azurirajTelefon(TelefonD telefon, String stariBrojTelefona);
	boolean obrisiTelefon(String jib, String brojTelefona);
}