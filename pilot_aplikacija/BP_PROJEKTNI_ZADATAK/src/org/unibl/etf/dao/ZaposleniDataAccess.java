package org.unibl.etf.dao;

import java.util.*;
import org.unibl.etf.entity.*;

public interface ZaposleniDataAccess {
	Zaposleni zaposleni(int idOsobe);
	List<Zaposleni> sviZaposleni();
	boolean dodajZaposlenog(Zaposleni zaposleni);
	boolean azurirajZaposlenog(Zaposleni zaposleni);
	boolean obrisiZaposlenog(int idOsobe);
}
