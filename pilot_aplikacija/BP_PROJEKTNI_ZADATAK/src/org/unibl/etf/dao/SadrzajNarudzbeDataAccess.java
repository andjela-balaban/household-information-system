package org.unibl.etf.dao;

import java.util.List;
import java.util.Map;
import org.unibl.etf.entity.*;

public interface SadrzajNarudzbeDataAccess {
	SadrzajNarudzbe sadrzajNarudzbe(int idNarudzbe, int idProizvoda);
    boolean dodajSadrzajNarudzbe(SadrzajNarudzbe sadrzajNarudzbe);
    boolean azurirajSadrzajNarudzbe(SadrzajNarudzbe sadrzajNarudzbe);
    boolean obrisiSadrzajNarudzbe(int idNarudzbe, int idProizvoda);
    Map<Proizvod, Integer> proizvodiIzNarudzbe(int idNarudzbe);
	List<Proizvod> slobodniProizvodiPoTipu(TipProizvoda tip);
}