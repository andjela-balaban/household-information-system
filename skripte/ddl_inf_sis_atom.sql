drop schema if exists inf_sis_atom; 

create schema inf_sis_atom;

use inf_sis_atom;

create table mjesto (
	PostanskiBroj int primary key,
	NazivMjesta varchar(50) not null
);

create table ulica (
	PostanskiBroj int,
	NazivUlice varchar(50),
	primary key (PostanskiBroj, NazivUlice),
	constraint fk_ulica_mjesto
	foreign key (PostanskiBroj)
	references mjesto(PostanskiBroj)
	on update cascade
	on delete cascade
);

create table adresa (
	IdAdrese int auto_increment primary key,
	PostanskiBroj int,
	NazivUlice varchar(50),
	KucniBroj int,
	constraint fk_adresa_ulica
	foreign key (PostanskiBroj, NazivUlice)
	references ulica(PostanskiBroj, NazivUlice)
	on update restrict
	on delete restrict
);

create table osoba (
	IdOsobe int auto_increment primary key,
	Ime varchar(20) not null,
	Prezime varchar(30) not null,
	IdAdrese int,
    foreign key (IdAdrese)
	references adresa(IdAdrese)
	on update restrict
	on delete restrict
);

create table telefon_o (
	IdOsobe int,
	BrojTelefona varchar(15),
	primary key (IdOsobe, BrojTelefona),
	constraint fk_telefon_o_osoba
	foreign key (IdOsobe)
	references osoba(IdOsobe)
	on update cascade	
    on delete cascade
);

create table domacinstvo (
	JIB char(13) primary key,
	Naziv varchar(50) not null,
    IdAdrese int not null,
    IdKontaktOsobe int not null,
    constraint fk_domacinstvo_adresa
	foreign key (IdAdrese)
	references adresa(IdAdrese)
	on update restrict
	on delete restrict,
	constraint fk_domacinstvo_osoba
	foreign key (IdKontaktOsobe)
	references osoba(IdOsobe)
	on update restrict
	on delete restrict
);

create table telefon_d (
	JIB char(13),
	BrojTelefona varchar(15),
	primary key (JIB, BrojTelefona),
	constraint fk_telefon_d_domacinstvo
	foreign key (JIB)
	references domacinstvo(JIB)
	on update cascade
	on delete cascade
);

create table kupac (
	IdOsobe int primary key,
    constraint fk_kupac_osoba
	foreign key (IdOsobe)
	references osoba(IdOsobe)
	on update cascade
	on delete cascade
);

create table zaposleni_funkcija (
    IdFunkcije int primary key,
    NazivFunkcije varchar(50) not null
);

-- insert into zaposleni_funkcija (IdFunkcije, NazivFunkcije) values (1, 'muža'), (2, 'prerada'), (3, 'isporuka');

create table zaposleni (
	IdOsobe int primary key,
    JIB char(13) not null,
    IdFunkcije int not null,
    Plata double not null,
    constraint fk_zaposleni_osoba
	foreign key (IdOsobe)
	references osoba(IdOsobe)
    on update cascade
	on delete cascade,
    constraint fk_zaposleni_domacinstvo
    foreign key (JIB)
    references domacinstvo(JIB)
    on update cascade
	on delete cascade,
    constraint fk_zaposleni_funkcija
    foreign key (IdFunkcije)
    references zaposleni_funkcija(IdFunkcije)
    on update cascade
	on delete cascade
);

create table tip_zivotinje (
    IdTipaZivotinje INT PRIMARY KEY,
    NazivTipaZivotinje VARCHAR(50) NOT NULL
);

-- insert into tip_zivotinje (IdTipaZivotinje, NazivTipaZivotinje) values (1, 'krava'), (2, 'koza');

create table zivotinja (
	IdZivotinje int auto_increment primary key,
    Ime varchar(50),
    IdTipaZivotinje int not null,
    JIB char(13) not null,
    constraint fk_zivotinja_tip_zivotinje
    foreign key (IdTipaZivotinje)
    references tip_zivotinje(IdTipaZivotinje)
    on update cascade
	on delete cascade,
    constraint fk_zivotinja_domacinstvo
    foreign key (JIB)
    references domacinstvo(JIB)
    on update cascade
	on delete cascade
);

create table jedinica_mjere (
	IdMjere int primary key,
    Jedinica varchar(15) not null
);

-- insert into jedinica_mjere (IdMjere, Jedinica) values (1, 'litar'), (2, 'kilogram'), (3, 'komad');

create table mlijeko (
	IdMlijeka int auto_increment primary key,
	DatumMuze date not null,
    Kolicina double not null,
    IdMjere int not null,
    Kvalitet int not null,
    IdZivotinje int not null,
    constraint fk_mlijeko_jed_mjere
    foreign key (IdMjere)
    references jedinica_mjere(IdMjere)
    on update cascade
	on delete cascade,
    constraint fk_mlijeko_zivotinja
    foreign key (IdZivotinje)
    references zivotinja(IdZivotinje)
    on update cascade
	on delete cascade
);

create table prerada_mlijeka (
	IdPrerade int auto_increment primary key,
    IdMlijeka int not null,
    JIB char(13) not null,
    IdOsobe int not null,
    DatumPrerade date not null,
    KolicinaMlijekaZaPreradu double not null,
	IdMjere int not null,
	 constraint fk_prerada_mlijeka_mlijeko
    foreign key (IdMlijeka)
    references mlijeko(IdMlijeka)
    on update cascade
	on delete cascade,
    constraint fk_prerada_mlijeka_domacinstvo
    foreign key (JIB)
    references domacinstvo(JIB)
    on update cascade
	on delete cascade,
    constraint fk_prerada_mlijeka_zaposleni
    foreign key (IdOsobe)
    references zaposleni(IdOsobe)
    on update cascade
	on delete cascade,
    constraint fk_prerada_mlijeka_jed_mjere
    foreign key (IdMjere)
    references jedinica_mjere(IdMjere)
    on update cascade
	on delete cascade
);

create table tip_proizvoda (
	IdTipaProizvoda int primary key,
    NazivTipaProizvoda varchar(50) not null
);

-- insert into tip_proizvoda (IdTipaProizvoda, NazivTipaProizvoda) values (1, 'sir'), (2, 'kajmak'), (3, 'jogurt'), (4, 'mlijeko'), (5, 'pavlaka');

create table proizvod (
	IdProizvoda int auto_increment primary key,
    IdPrerade int not null,
    IdTipaProizvoda int not null,
    Cijena double not null,
    constraint fk_proizvod_prerada
    foreign key (IdPrerade)
    references prerada_mlijeka(IdPrerade)
    on update cascade 
    on delete cascade,
	constraint fk_proizvod_tip_proizvoda
    foreign key (IdTipaProizvoda)
    references tip_proizvoda(IdTipaProizvoda)
    on update cascade 
    on delete cascade
);

create table tip_placanja (
	IdTipaPlacanja int primary key,
    TipPlacanja varchar(50) not null
);

-- insert into tip_placanja (IdTipaPlacanja, TipPlacanja) values (1, 'gotovina'), (2, 'kartica');

create table placanje (
	IdPlacanja int auto_increment primary key,
    IdOsobe int not null,
    Iznos double not null,
    IdTipaPlacanja int not null,
    constraint fk_placanje_kupac
    foreign key (IdOsobe)
    references kupac(IdOsobe)
    on update cascade
	on delete cascade,
    constraint fk_placanje_tip_placanja
    foreign key (IdTipaPlacanja)
    references tip_placanja(IdTipaPlacanja)
    on update cascade
	on delete cascade
);

create table isporuka (
	IdIsporuke int auto_increment primary key,
    DatumIsporuke date not null,
    IdAdrese int not null,
    IdKupca int not null,
    IdIsporucioca int not null,
    foreign key (IdAdrese)
	references adresa(IdAdrese)
    on update restrict
	on delete restrict,
    constraint fk_isporuka_kupac
	foreign key (IdKupca)
	references kupac(IdOsobe)
	on update restrict
	on delete restrict,
    constraint fk_isporuka_zaposleni
	foreign key (IdIsporucioca)
	references zaposleni(IdOsobe)
	on update restrict
	on delete restrict
);

create table status_narudzbe (
	IdStatusaNarudzbe int primary key,
    StatusNarudzbe varchar(50) not null
);

-- insert into status_narudzbe (IdStatusaNarudzbe, StatusNarudzbe) values (1, 'plaćena'), (2, 'neplaćena');

create table narudzba (
	IdNarudzbe int auto_increment primary key,
	IdStatusaNarudzbe int not null,
    DatumNarudzbe date not null,
    IdKupca int not null,
    IdPlacanja int not null,
    IdIsporuke int,
    constraint fk_narudzba_status_narudzbe
	foreign key (IdStatusaNarudzbe)
	references status_narudzbe(IdStatusaNarudzbe)
	on update restrict
	on delete restrict,
    constraint fk_narudzba_kupac
	foreign key (IdKupca)
	references kupac(IdOsobe)
	on update restrict
	on delete restrict,
    constraint fk_narudzba_placanje
	foreign key (IdPlacanja)
	references placanje(IdPlacanja)
	on update restrict
	on delete cascade,
    constraint fk_narudzba_isporuka
	foreign key (IdIsporuke)
	references isporuka(IdIsporuke)
	on update restrict
	on delete restrict
);

create table sadrzaj_narudzbe (
	IdNarudzbe int,
    IdProizvoda int,
	Kolicina int not null,
    primary key (IdNarudzbe, IdProizvoda),
    constraint fk_sadrzaj_narudzba
	foreign key (IdNarudzbe)
	references narudzba(IdNarudzbe)
	on update restrict
	on delete cascade,
    constraint fk_sadrzaj_proizvod
	foreign key (IdProizvoda)
	references proizvod(IdProizvoda)
	on update restrict
	on delete restrict
);