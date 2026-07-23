package org.unibl.etf.dao;

import java.util.*;
import org.unibl.etf.entity.*;

public interface UlicaDataAccess {
	Ulica ulica(int postanskiBroj, String nazivUlice);
	List<Ulica> uliceUMjestu(int postanskiBroj); // sve ulice za dato mjesto
	boolean dodajUlicu(Ulica ulica);
	boolean azurirajUlicu(Ulica ulica);
	boolean obrisiUlicu(int postanskiBroj, String nazivUlice);
}