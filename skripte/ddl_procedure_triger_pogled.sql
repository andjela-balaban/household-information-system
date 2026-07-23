use inf_sis_atom;


-- view
create view kolicina_mlijeka_po_zivotinji as
	select z.IdZivotinje, z.Ime, z.IdTipaZivotinje, tz.NazivTipaZivotinje, SUM(m.Kolicina) AS UkupnaKolicinaMlijeka
	from zivotinja z
	join tip_zivotinje tz on z.IdTipaZivotinje = tz.IdTipaZivotinje
	join mlijeko m on z.IdZivotinje = m.IdZivotinje
	group by z.IdZivotinje;



-- procedura broj 1
DELIMITER $$
create procedure ukupna_dobit_domacinstva (
	in pIdStatusa int,
    in p_jib char(13),          
    out p_dobit double)
BEGIN
    declare v_idNarudzbe int;
    declare v_ukupno double default 0;
    declare done int default false;
    
    declare narudzbe_cursor cursor for
        select distinct n.IdNarudzbe
        from narudzba n
        join status_narudzbe s  on n.IdStatusaNarudzbe = s.IdStatusaNarudzbe
        join sadrzaj_narudzbe sn on n.IdNarudzbe = sn.IdNarudzbe
        join proizvod p on sn.IdProizvoda = p.IdProizvoda
        join prerada_mlijeka pm on p.IdPrerade = pm.IdPrerade  
        where s.IdStatusaNarudzbe = pIdStatusa and pm.JIB = p_jib;

    declare continue handler for not found set done = true;
    
    open narudzbe_cursor;
    
    read_loop: loop
        fetch narudzbe_cursor into v_idNarudzbe;
        if done then
            leave read_loop;
        end if;
        -- sumiramo stadrzaj narudzbe za svaku narudzbu
        select IFNULL(SUM(p.Cijena * sn.Kolicina), 0) into p_dobit
        from sadrzaj_narudzbe sn
        join proizvod p on sn.IdProizvoda = p.IdProizvoda
        join prerada_mlijeka pm on p.IdPrerade = pm.IdPrerade
        where sn.IdNarudzbe = v_idNarudzbe and pm.JIB = p_jib; 
        
        set v_ukupno = v_ukupno + p_dobit;
    end loop;
    close narudzbe_cursor;
    
    set p_dobit = v_ukupno;
END $$
DELIMITER ;

-- call ukupna_dobit_domacinstva_kursor_jib(1, '1234567890123', @rez);
-- select @rez as DobitPlacene;



-- procedura broj 2
DELIMITER $$
create procedure top_proizvodi_domacinstva (
    in p_jib char(13),
    in p_limit int)
BEGIN
    select tp.NazivTipaProizvoda as Proizvod, SUM(sn.Kolicina) as UkupnoKomada, SUM(sn.Kolicina * pr.Cijena) as UkupnaZarada
    from narudzba n
	join sadrzaj_narudzbe sn on n.IdNarudzbe = sn.IdNarudzbe
	join proizvod pr on sn.IdProizvoda = pr.IdProizvoda
	join prerada_mlijeka pm on pr.IdPrerade = pm.IdPrerade
	join tip_proizvoda tp on pr.IdTipaProizvoda = tp.IdTipaProizvoda
    where n.IdStatusaNarudzbe = 1 and pm.JIB = p_jib  -- gleda u odnosu na placene narudzbe
    group by tp.NazivTipaProizvoda
    order by SUM(sn.Kolicina) desc
    limit p_limit;
END $$
DELIMITER ;

-- call top_proizvodi_domacinstva('1234567890123', 5);  



-- trigger koji status tek kreirane narudzbe postavi na neplacena ukoliko nije postavljen status
DELIMITER $$
create trigger narudzba_default_status_trigger
before insert on narudzba
for each row
BEGIN
    if new.IdStatusaNarudzbe is null then
        set new.IdStatusaNarudzbe = 2;
    END IF;
END $$
DELIMITER ;