package org.unibl.etf.dao;

import java.util.*;
import org.unibl.etf.entity.*;

public interface MlijekoDataAccess {
	Mlijeko mlijeko(int idMlijeka);
    List<Mlijeko> mlijeko();
    boolean dodajMlijeko(Mlijeko mlijeko);
    boolean azurirajMlijeko(Mlijeko mlijeko);
    boolean obrisiMlijeko(int idMlijeka);
}