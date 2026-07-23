package org.unibl.etf.dao;

import java.util.*;
import org.unibl.etf.entity.*;

public interface KupacDataAccess {
	Kupac kupac(int idOsobe);
    List<Kupac> kupci();
    boolean dodajKupca(Kupac kupac);
    boolean azurirajKupca(Kupac kupac);
    boolean obrisiKupca(int idOsobe);
	int zadnjiIdOsobe();
}
