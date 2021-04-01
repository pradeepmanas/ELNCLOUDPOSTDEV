select 1

select * from LSSitemaster

ALTER TABLE if exists email alter column  mailcontent Type varchar(5000)

UPDATE  lsusermaster SET userretirestatus=0 WHERE userretirestatus is null
