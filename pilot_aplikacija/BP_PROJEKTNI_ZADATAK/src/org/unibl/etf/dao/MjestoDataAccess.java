package org.unibl.etf.dao;

import java.util.*;
import org.unibl.etf.entity.*;

public interface MjestoDataAccess {
	Mjesto mjesto(int postanskiBroj);
    List<Mjesto> mjesta();
    boolean dodajMjesto(Mjesto mjesto);
    boolean azurirajMjesto(Mjesto mjesto);
    boolean obrisiMjesto(int postanskiBroj);
}