package org.unibl.etf.dao;

import java.util.*;
import org.unibl.etf.entity.*;

public interface ZivotinjaDataAccess {
	Zivotinja zivotinja(int idZivotinje);
	List<Zivotinja> sveZivotinje();
	boolean dodajZivotinju(Zivotinja zivotinja);
	boolean azurirajZivotinju(Zivotinja zivotinja);
	boolean obrisiZivotinju(int idZivotinje);
	double kolicinaMlijeka(int idZivotinje);
}
