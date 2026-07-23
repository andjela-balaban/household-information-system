package org.unibl.etf.dao;

import java.util.*;
import org.unibl.etf.entity.*;

public interface AdresaDataAccess {
	Adresa adresa(int idAdrese);
	List<Adresa> adrese();
	boolean dodajAdresu(Adresa adresa);
	boolean azurirajAdresu(Adresa adresa);
	boolean obrisiAdresu(int idAdrese);
}
