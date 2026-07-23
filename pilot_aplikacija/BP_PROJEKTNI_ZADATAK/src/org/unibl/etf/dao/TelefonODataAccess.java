package org.unibl.etf.dao;

import java.util.*;
import org.unibl.etf.entity.*;

public interface TelefonODataAccess {
	TelefonO telefon(String brojTelefona, int idOsobe);
	List<TelefonO> telefoniZaOsobu(int idOsobe);
	boolean dodajTelefon(TelefonO telefon);
	boolean azurirajTelefon(TelefonO telefon, String stariBrojTelefona);
	boolean obrisiTelefon(int idOsobe, String brojTelefona);
	List<TelefonO> sviTelefoni();
}
