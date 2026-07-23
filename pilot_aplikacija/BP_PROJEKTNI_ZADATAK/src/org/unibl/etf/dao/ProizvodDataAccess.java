package org.unibl.etf.dao;

import java.util.*;
import org.unibl.etf.entity.*;

public interface ProizvodDataAccess {
	Proizvod proizvod(int idProizvoda);
    List<Proizvod> sviProizvodi();
    boolean dodajProizvod(Proizvod proizvod);
    boolean azurirajProizvod(Proizvod proizvod);
    boolean obrisiProizvod(int idProizvoda);
}