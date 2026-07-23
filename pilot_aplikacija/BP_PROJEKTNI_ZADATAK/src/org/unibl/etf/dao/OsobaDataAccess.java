package org.unibl.etf.dao;

import java.util.*;
import org.unibl.etf.entity.*;

public interface OsobaDataAccess {
	Osoba osoba(int idOsobe);
	List<Osoba> osobe();
	boolean dodajOsobu(Osoba osoba);
	boolean azurirajOsobu(Osoba osoba);
	boolean obrisiOsobu(int idOsobe);
}
