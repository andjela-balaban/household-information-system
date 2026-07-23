package org.unibl.etf.dao;

import java.util.*;
import org.unibl.etf.entity.*;

public interface PlacanjeDataAccess {
	Placanje placanje(int idPlacanja);
    List<Placanje> svaPlacanja();
    boolean dodajPlacanje(Placanje placanje);
    boolean azurirajPlacanje(Placanje placanje);
    boolean obrisiPlacanje(int idPlacanja);
}