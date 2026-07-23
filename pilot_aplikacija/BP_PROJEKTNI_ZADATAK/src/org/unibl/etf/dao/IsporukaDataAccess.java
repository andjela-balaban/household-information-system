package org.unibl.etf.dao;

import java.util.*;
import org.unibl.etf.entity.*;

public interface IsporukaDataAccess {
	Isporuka isporuka(int idIsporuke);
    List<Isporuka> isporuke();
    boolean dodajIsporuku(Isporuka isporuka);
    boolean azurirajIsporuku(Isporuka isporuka);
    boolean obrisiIsporuku(int idIsporuke);
}