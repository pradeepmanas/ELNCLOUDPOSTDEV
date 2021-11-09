select 1;

select * from LSSitemaster;

ALTER TABLE if exists email alter column  mailcontent Type varchar(5000);

UPDATE  lsusermaster SET userretirestatus=0 WHERE userretirestatus is null;

--update LSusergrouprightsmaster set sallow='0',screate='0',sdelete='0',sedit='0' where sallow='1' or screate='1' or sdelete='1' or sedit='1';

update LSusergrouprightsmaster set sallow='0' where sallow='1';
update LSusergrouprightsmaster set screate='0' where  screate='1';
update LSusergrouprightsmaster set sdelete='0' where sdelete='1';
update LSusergrouprightsmaster set sedit='0' where sedit='1';

update LSfileversion set modifiedby_usercode= (select  modifiedby_usercode from LSfileversion where modifiedby_usercode is not null and modifieddate is not null order by modifieddate asc LIMIT  1 ), modifieddate= (select createdate from LSfileversion where modifiedby_usercode is not null and modifieddate is not null order by modifieddate asc LIMIT  1 ) where modifiedby_usercode is null or modifieddate is  null and versionname='version_1';

--UPDATE LSprotocolmaster SET createby_usercode = createdby where createby_usercode is null;
ALTER TABLE IF Exists lsprotocolmaster DROP COLUMN IF Exists createby_usercode;

UPDATE  DataSourceConfig SET isadministrator_verify=false WHERE isadministrator_verify is null;

update LSaudittrailconfigmaster set screenname='Protocols',taskname='Add Protocol' where serialno=58;
update LSaudittrailconfigmaster set screenname='Protocols',taskname='New Step' where serialno=59;
update LSaudittrailconfigmaster set screenname='Protocols',taskname='Share with Team' where serialno=60;
update LSaudittrailconfigmaster set screenname='Protocols',taskname='Delete' where serialno=61;

update LSaudittrailconfigmaster set ordersequnce= 63 where serialno=64;
update LSaudittrailconfigmaster set ordersequnce= 64 where serialno=65;

--DO
--$do$
--declare
--  multiusergroupcount integer :=0;
--begin
-- select count(*) 
--   into multiusergroupcount
--    from lsmultiusergroup;
---- 	select * from multiusergroupcount
-- IF multiusergroupcount =0 THEN

-- insert into lsmultiusergroup (usercode,lsusergroup_usergroupcode) 
--(
--	select usercode,lsusergroup.usergroupcode from lsusermaster,lsusergroup 
--where lsusermaster.lsusergroup_usergroupcode = lsusergroup.usergroupcode
--);
--     ELSE               
--      -- do something!
--   END IF;
--END
--$do$;

ALTER TABLE IF Exists Helptittle ADD COLUMN IF NOT EXISTS page varying(255);

