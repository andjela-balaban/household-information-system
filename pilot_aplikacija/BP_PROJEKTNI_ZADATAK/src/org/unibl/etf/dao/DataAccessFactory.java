package org.unibl.etf.dao;

import org.unibl.etf.dao.mysql.MySQLDataAccessFactory;

public abstract class DataAccessFactory {
	
	public abstract MjestoDataAccess getMjestoDataAccess();
	public abstract UlicaDataAccess getUlicaDataAccess();
	public abstract AdresaDataAccess getAdresaDataAccess();
	public abstract OsobaDataAccess getOsobaDataAccess();
	public abstract TelefonODataAccess getTelefonODataAccess();
	public abstract DomacinstvoDataAccess getDomacinstvoDataAccess();
	public abstract TelefonDDataAccess getTelefonDDataAccess();
	public abstract KupacDataAccess getKupacDataAccess();
	public abstract ZaposleniDataAccess getZaposleniDataAccess();
	public abstract ZivotinjaDataAccess getZivotinjaDataAccess();
	public abstract MlijekoDataAccess getMlijekoDataAccess();
	public abstract PreradaMlijekaDataAccess getPreradaMlijekaDataAccess();
	public abstract ProizvodDataAccess getProizvodDataAccess();
	public abstract PlacanjeDataAccess getPlacanjeDataAccess();
	public abstract IsporukaDataAccess getIsporukaDataAccess();
	public abstract NarudzbaDataAccess getNarudzbaDataAccess();
	public abstract SadrzajNarudzbeDataAccess getSadrzajNarudzbeDataAccess();
	
	public static DataAccessFactory getFactory(DataAccessFactoryType type) {
		if (DataAccessFactoryType.MySQL.equals(type)) {
			return new MySQLDataAccessFactory();
		}
		throw new IllegalArgumentException();
	}
	
}