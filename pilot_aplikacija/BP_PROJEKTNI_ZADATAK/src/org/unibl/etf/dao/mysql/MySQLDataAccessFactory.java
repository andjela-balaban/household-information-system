package org.unibl.etf.dao.mysql;

import org.unibl.etf.dao.*;

public class MySQLDataAccessFactory extends DataAccessFactory {

	@Override
	public MjestoDataAccess getMjestoDataAccess() {
	    return new MjestoDataAccessImpl();
	}

	@Override
	public UlicaDataAccess getUlicaDataAccess() {
	    return new UlicaDataAccessImpl();
	}
	
	@Override
	public AdresaDataAccess getAdresaDataAccess() {
	    return new AdresaDataAccessImpl();
	}
	
	@Override
	public OsobaDataAccess getOsobaDataAccess() {
	    return new OsobaDataAccessImpl();
	}

	@Override
	public TelefonODataAccess getTelefonODataAccess() {
		return new TelefonODataAccessImpl();
	}

	@Override
	public DomacinstvoDataAccess getDomacinstvoDataAccess() {
		return new DomacinstvoDataAccessImpl();
	}

	@Override
	public TelefonDDataAccess getTelefonDDataAccess() {
		return new TelefonDDataAccessImpl();
	}

	@Override
	public KupacDataAccess getKupacDataAccess() {
		return new KupacDataAccessImpl();
	}

	@Override
	public ZaposleniDataAccess getZaposleniDataAccess() {
		return new ZaposleniDataAccessImpl();
	}

	@Override
	public ZivotinjaDataAccess getZivotinjaDataAccess() {
		return new ZivotinjaDataAccessImpl();
	}

	@Override
	public MlijekoDataAccess getMlijekoDataAccess() {
		return new MlijekoDataAccessImpl();
	}

	@Override
	public PreradaMlijekaDataAccess getPreradaMlijekaDataAccess() {
		return new PreradaMlijekaDataAccessImpl();
	}

	@Override
	public ProizvodDataAccess getProizvodDataAccess() {
		return new ProizvodDataAccessImpl();
	}

	@Override
	public PlacanjeDataAccess getPlacanjeDataAccess() {
		return new PlacanjeDataAccessImpl();
	}

	@Override
	public IsporukaDataAccess getIsporukaDataAccess() {
		return new IsporukaDataAccessImpl();
	}

	@Override
	public NarudzbaDataAccess getNarudzbaDataAccess() {
		return new NarudzbaDataAccessImpl();
	}

	@Override
	public SadrzajNarudzbeDataAccess getSadrzajNarudzbeDataAccess() {
		return new SadrzajNarudzbeDataAccessImpl();
	}

}