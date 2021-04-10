select 1

select * from LSSitemaster

ALTER TABLE if exists email alter column  mailcontent Type varchar(5000)

UPDATE  lsusermaster SET userretirestatus=0 WHERE userretirestatus is null

--update LSusergrouprightsmaster set sallow='0',screate='0',sdelete='0',sedit='0' where sallow='1' or screate='1' or sdelete='1' or sedit='1';

update LSusergrouprightsmaster set sallow='0' where sallow='1';
update LSusergrouprightsmaster set screate='0' where  screate='1';
update LSusergrouprightsmaster set sdelete='0' where sdelete='1';
update LSusergrouprightsmaster set sedit='0' where sedit='1';

update LSfileversion set modifiedby_usercode= (select  modifiedby_usercode from LSfileversion where modifiedby_usercode is not null and modifieddate is not null order by modifieddate asc LIMIT  1 ), modifieddate= (select createdate from LSfileversion where modifiedby_usercode is not null and modifieddate is not null order by modifieddate asc LIMIT  1 ) where modifiedby_usercode is null or modifieddate is  null and versionname='version_1';