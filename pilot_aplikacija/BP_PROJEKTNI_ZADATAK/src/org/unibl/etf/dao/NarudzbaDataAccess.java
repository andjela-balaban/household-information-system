package org.unibl.etf.dao;

import java.util.*;
import org.unibl.etf.entity.*;

public interface NarudzbaDataAccess {
	Narudzba narudzba(int idNarudzbe);
    List<Narudzba> narudzbe();
    boolean dodajNarudzbu(Narudzba narudzba);
    boolean azurirajNarudzbu(Narudzba narudzba);
    boolean obrisiNarudzbu(int idNarudzbe);
}
