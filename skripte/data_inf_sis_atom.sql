use inf_sis_atom;

-- Fukcije zaposlenog u domacinstvu
insert into zaposleni_funkcija (IdFunkcije, NazivFunkcije) values
	(1, 'muža'),
	(2, 'prerada'),
	(3, 'isporuka');
    
-- Tipovi zivotinja
insert into tip_zivotinje (IdTipaZivotinje, NazivTipaZivotinje) values
	(1, 'krava'),
	(2, 'koza');

-- Jedinice mjere
insert into jedinica_mjere (IdMjere, Jedinica) values
	(1, 'litar'),
	(2, 'kilogram'),
    (3, 'komad');
    
-- Tipovi proizvoda
insert into tip_proizvoda (IdTipaProizvoda, NazivTipaProizvoda) values
	(1, 'sir'),
	(2, 'kajmak'),
    (3, 'jogurt'),
    (4, 'mlijeko'),
    (5, 'pavlaka');

-- Tipovi placanja
insert into tip_placanja (IdTipaPlacanja, TipPlacanja) values
	(1, 'gotovina'),
	(2, 'kartica');
    
-- Statusi narudzbe
insert into status_narudzbe (IdStatusaNarudzbe, StatusNarudzbe) values
	(1, 'plaćena'),
	(2, 'neplaćena');

-- Mjesta
INSERT INTO mjesto VALUES 
	(80260, 'Drvar'), 
	(78000, 'Banja Luka'), 
	(12320, 'Zaglavica'), 
	(89101, 'Trebinje'),
	(71000, 'Sarajevo');

-- Ulice
INSERT INTO ulica VALUES 
	(80260, 'Titova ulica'), 
	(80260, 'Beogradska'), 
	(80260, 'Grmečka'), 
	(12320, 'Zaglavička'), 
	(12320, 'Petrovačka'),
	(78000, 'Kralja Petra I'), 
	(78000, 'Đure Đakovića'), 
	(78000, 'Jevrejska'),
	(89101, 'Njegoševa'), 
	(89101, 'Dositejeva'), 
	(89101, 'Miloša Obilića'),
	(71000, 'Maršala Tita'),
	(71000, 'Put života');

-- Adrese
INSERT INTO adresa (PostanskiBroj, NazivUlice, KucniBroj) VALUES 
	(80260, 'Titova ulica', 12), 	-- 1
	(80260, 'Beogradska', 5), 	 	-- 2
	(80260, 'Grmečka', 4), 			-- 3
	(12320, 'Zaglavička', 21), 		-- 4
	(12320, 'Petrovačka', 18),		-- 5
	(78000, 'Kralja Petra I', 10), 	-- 6
	(78000, 'Đure Đakovića', 4), 	-- 7
	(78000, 'Jevrejska', 42),		-- 8
	(89101, 'Njegoševa', 3), 		-- 9
	(89101, 'Dositejeva', 51), 		-- 10
	(89101, 'Miloša Obilića', 22),	-- 11
	(71000, 'Maršala Tita', 12),	-- 12
	(71000, 'Put života', 72);		-- 13

-- Osobe
INSERT INTO osoba (Ime, Prezime, IdAdrese) VALUES 
	('Gordana', 'Balaban', 1),			-- 1
	('Mladen', 'Balaban', 7),			-- 2
	('Milan', 'Balaban', 8),			-- 3
	('Žega', 'Popović', 5),				-- 4
	('Šobre', 'Krivonosi', 3),			-- 5
	('Maira', 'Pećanac', 2),			-- 6
	('Marko', 'Marković', 6),			-- 7
	('Marija', 'Petrović', 10),			-- 8
	('Simo', 'Simić', 10),				-- 9
	('Ana', 'Babić', 9),				-- 10
	('Miroslav', 'Spasojević', 4),		-- 11
	('Stefan', 'Kovač', 11),			-- 12
	('Saša', 'Trnić', 12),				-- 13
	('Branko', 'Hadžić', 13);			-- 14

-- Telefoni osoba
INSERT INTO telefon_o VALUES 
	(1, '066111111'), 
	(2, '065222222'),
	(5, '065412432'),
	(9, '066123321'),
	(14, '066444444');

-- Domaćinstvo
INSERT INTO domacinstvo VALUES ('1234567890123', 'Domaćinstvo ATOM', 4, 1);

-- Telefoni domaćinstva
INSERT INTO telefon_d VALUES ('1234567890123', '051555555');

-- Kupci
INSERT INTO kupac VALUES 
	(6),
	(7),
	(8),
	(9),
	(10),
	(11),
	(12),
	(13),
	(14);

-- Zaposleni
INSERT INTO zaposleni VALUES 
	(1, '1234567890123', 1, 1980.00),
	(2, '1234567890123', 2, 1350.00),
	(3, '1234567890123', 2, 1350.00),
	(4, '1234567890123', 3, 950.00),
	(5, '1234567890123', 3, 950.00);

-- Životinje
INSERT INTO zivotinja (Ime, IdTipaZivotinje, JIB) VALUES 
	('Krava Ružova', 1, '1234567890123'),		-- 1
	('Krava Milenka', 1, '1234567890123'),		-- 2
	('Krava Kiti', 1, '1234567890123'),			-- 3
	('Krava Keti', 1, '1234567890123'),			-- 4
	('Koza Bela', 2, '1234567890123'),			-- 5
	('Koza Smiljka', 2, '1234567890123');		-- 6

-- Mlijeko
INSERT INTO mlijeko (DatumMuze, Kolicina, IdMjere, Kvalitet, IdZivotinje) VALUES 
	('2025-07-20', 10.0, 1, 90, 1),
	('2025-07-20', 9.0, 1, 92, 2),
	('2025-07-20', 8.5, 1, 89, 3),
	('2025-07-20', 7.0, 1, 82, 4),
	('2025-07-19', 3.0, 1, 90, 5),
	('2025-07-18', 5.0, 1, 85, 6);

-- Prerada mlijeka
INSERT INTO prerada_mlijeka (IdMlijeka, JIB, IdOsobe, DatumPrerade, KolicinaMlijekaZaPreradu, IdMjere) VALUES
	(1, '1234567890123', 3, '2025-07-21', 8.0, 1),		-- 1
	(2, '1234567890123', 2, '2025-07-20', 4.5, 1),		-- 2
    (3, '1234567890123', 3, '2025-07-19', 9.0, 1),		-- 3
    (4, '1234567890123', 2, '2025-07-22', 10.0, 1),		-- 4
    (5, '1234567890123', 3, '2025-07-23', 7.5, 1),		-- 5
    (6, '1234567890123', 3, '2025-07-18', 8.0, 1);		-- 6

-- Proizvodi
INSERT INTO proizvod (IdPrerade, IdTipaProizvoda, Cijena) VALUES
	(1, 1, 20.0), -- sir		-- 1
	(2, 4, 2.5),  -- mlijeko	-- 2
    (3, 5, 12.0),  -- pavlaka	-- 3
    (4, 3, 11.5),  -- jogurt	-- 4
    (5, 2, 15.0),  -- kajmak	-- 5
    (6, 1, 20.0),  -- sir		-- 6
    (1, 2, 15.0), -- kajmak		-- 7
	(2, 1, 20.0),  -- sir		-- 8
    (3, 2, 15.0),  -- kajmak	-- 9
    (4, 4, 2.5),  -- pavlaka	-- 10
    (5, 1, 20.0),  -- sir		-- 11
    (6, 3, 11.5),  -- jogurt	-- 12
    (4, 1, 20.0),  -- sir		-- 13
    (1, 1, 20.0),  -- sir		-- 14
    (6, 1, 20.0),  -- sir		-- 15
    (3, 1, 20.0);  -- sir		-- 16

-- Placanje
INSERT INTO placanje (IdOsobe, Iznos, IdTipaPlacanja) VALUES
	(7, 22.5, 1), -- gotovina		-- 1
    (8, 11.5, 1), -- gotovina		-- 2
    (9, 12.0, 1), -- gotovina		-- 3
    (10, 20.0, 1), -- gotovina		-- 4
    (11, 35.0, 2), -- kartica		-- 5
    (12, 20.0, 1), -- gotovina		-- 6
    (13, 37.5, 2), -- kartica		-- 7
    (14, 40.0, 2), -- kartica		-- 8
    (14, 55.0, 1); -- gotovina		-- 9

-- Isporuka
INSERT INTO isporuka (Datumisporuke, IdAdrese, IdKupca, IdIsporucioca) VALUES
	('2025-05-30', 12, 13, 4),		-- 1
	('2025-07-22', 6, 7, 4),		-- 2
	('2025-08-01', 10, 8, 5),		-- 3
    ('2025-07-15', 9, 10, 4),		-- 4
    ('2025-07-23', 4, 11, 4),		-- 5
    ('2025-07-11', 13, 14, 5),		-- 6
    ('2025-06-29', 10, 9, 5),		-- 7
    ('2025-08-12', 11, 12, 5);		-- 8

-- Narudzba
INSERT INTO narudzba (IdStatusaNarudzbe, DatumNarudzbe, IdKupca, IdPlacanja, IdIsporuke) VALUES
	(1, '2025-05-29', 7, 1, 1), 		-- 1			-- placena narudzba
    (1, '2025-07-21', 8, 2, 2), 		-- 2			-- placena narudzba
    (2, '2025-07-31', 9, 3, 3), 		-- 3			-- neplacena narudzba
    (2, '2025-07-13', 10, 4, 4), 		-- 4			-- neplacena narudzba
    (1, '2025-07-22', 11, 5, 5), 		-- 5			-- placena narudzba
    (1, '2025-07-10', 12, 6, 6), 		-- 6			-- placena narudzba
    (2, '2025-06-28', 13, 7, 7), 		-- 7			-- neplacena narudzba
    (1, '2025-08-11', 14, 8, 8), 		-- 8			-- placena narudzba
    (1, '2025-08-15', 14, 9, null); 	-- 9			-- placena narudzba

-- Sadrzaj narudzbe
INSERT INTO sadrzaj_narudzbe (IdNarudzbe, IdProizvoda, Kolicina) VALUES
	(1, 1, 1),	-- sir
    (1, 2, 1), 	-- mlijeko
    (2, 4, 1), -- jogurt
    (3, 3, 1), -- pavlaka
    (4, 6, 1), -- sir
    (5, 8, 1), -- sir
    (5, 9, 1), -- kajmak
    (6, 11, 1), -- sir
    (7, 13, 1), -- sir
    (7, 7, 1), -- kajmak
    (7, 10, 1), -- mlijeko
    (8, 14, 2), -- sir x2
    (9, 15, 2), -- sir x2
	(9, 5, 1);	-- kajmak
