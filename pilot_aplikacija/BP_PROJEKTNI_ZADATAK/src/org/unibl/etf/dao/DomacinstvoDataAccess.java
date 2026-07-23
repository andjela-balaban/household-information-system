package org.unibl.etf.dao;

import java.util.*;
import org.unibl.etf.dao.mysql.*;
import org.unibl.etf.entity.*;

public interface DomacinstvoDataAccess {
    Domacinstvo domacinstvo(String jib);
    List<Domacinstvo> svaDomacinstva();
    boolean dodajDomacinstvo(Domacinstvo domacinstvo);
    boolean azurirajDomacinstvo(Domacinstvo domacinstvo);
    boolean obrisiDomacinstvo(String jib);
    double izracunajDobit(int idStatusaNarudzbe, String jib);
    List<TopProizvodDTO> topProizvodi(String jib, int limit);
}