package org.unibl.etf.dao;

import java.util.*;
import org.unibl.etf.entity.*;

public interface PreradaMlijekaDataAccess {
	PreradaMlijeka preradaMlijeka(int idPrerade);
    List<PreradaMlijeka> svaPreradaMlijeka();
    boolean dodajPreraduMlijeka(PreradaMlijeka prerada);
    boolean azurirajPreraduMlijeka(PreradaMlijeka prerada);
    boolean obrisiPreraduMlijeka(int idPrerade);
}