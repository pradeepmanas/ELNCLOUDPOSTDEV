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

ALTER TABLE IF Exists Helptittle ADD COLUMN IF NOT EXISTS page character varying(100);

delete from lsusergrouprightsmaster where modulename = 'Register Task Orders & Execute' and displaytopic in ('Assigned Orders','My Orders');

delete from lsusergrouprights where modulename = 'Register Task Orders & Execute' and displaytopic in ('Assigned Orders','My Orders');

INSERT INTO lsmultisites (usercode, lssitemaster_sitecode, defaultsiteMaster) SELECT lsusermaster.usercode, lssitemaster.sitecode, 1 FROM lsusermaster JOIN lssitemaster ON lsusermaster.lssitemaster_sitecode = lssitemaster.sitecode WHERE (SELECT COUNT(*) FROM lsmultisites) = 0;


-- INSERT INTO Elnprotocolworkflow SELECT * FROM LSworkflow WHERE (SELECT COUNT(*) FROM Elnprotocolworkflow) = 0 AND (SELECT COUNT(*) FROM LSworkflow) != 0;

-- INSERT INTO elnprotocolworkflowgroupmap SELECT * FROM LSworkflowgroupmapping WHERE (SELECT COUNT(*) FROM elnprotocolworkflowgroupmap) = 0  AND (SELECT COUNT(*) FROM LSworkflowgroupmapping) != 0 AND workflowcode IS NOT NULL AND lsusergroup_usergroupcode IS NOT NULL;

UPDATE lslogilabprotocoldetail SET elnprotocolworkflow_workflowcode = lsworkflow_workflowcode WHERE (SELECT COUNT(*) FROM lslogilabprotocoldetail WHERE elnprotocolworkflow_workflowcode IS NOT NULL) = 0;

-- INSERT INTO ElnprotocolTemplateworkflow (workflowcode, status, workflowname, lssitemaster_sitecode)SELECT workflowcode, status, workflowname, lssitemaster_sitecode FROM LSsheetworkflow WHERE (SELECT COUNT(*) FROM ElnprotocolTemplateworkflow) = 0AND (SELECT COUNT(*)FROM LSsheetworkflow) != 0;

-- INSERT INTO ElnprotocolTemplateworkflowgroupmap SELECT * FROM LSsheetworkflowgroupmap WHERE (SELECT COUNT(*) FROM ElnprotocolTemplateworkflowgroupmap) = 0 AND (SELECT COUNT(*) FROM LSsheetworkflowgroupmap WHERE workflowcode IS NOT NULL AND lsusergroup_usergroupcode IS NOT NULL ) != 0;

UPDATE LSprotocolorderworkflowhistory SET elnprotocolworkflow_workflowcode = lsworkflow_workflowcode WHERE elnprotocolworkflow_workflowcode IS NULL OR elnprotocolworkflow_workflowcode = 0;

-- INSERT INTO ElnprotocolTemplateworkflow (workflowcode, status, workflowname, lssitemaster_sitecode) SELECT workflowcode, status, workflowname, lssitemaster_sitecode FROM LSsheetworkflow WHERE (SELECT COUNT(*) FROM ElnprotocolTemplateworkflow) = 0 AND (SELECT COUNT(*) FROM LSsheetworkflow) != 0;

-- INSERT INTO ElnprotocolTemplateworkflowgroupmap SELECT * FROM LSsheetworkflowgroupmap WHERE workflowcode IS NOT NULL AND lsusergroup_usergroupcode IS NOT NULL AND (SELECT COUNT(*) FROM ElnprotocolTemplateworkflowgroupmap) = 0 AND (SELECT COUNT(*) FROM LSsheetworkflowgroupmap) != 0 AND (SELECT COUNT(*) FROM ElnprotocolTemplateworkflow) != 0;

-- UPDATE lsprotocolworkflowhistory SET elnprotocoltemplateworkflow_workflowcode = lssheetworkflow_workflowcode WHERE ( SELECT COUNT(*) FROM lsprotocolworkflowhistory WHERE elnprotocoltemplateworkflow_workflowcode IS NOT NULL) = 0;

-- UPDATE lsprotocolmaster SET elnprotocoltemplateworkflow_workflowcode = lssheetworkflow_workflowcode WHERE ( SELECT COUNT(*) FROM lsprotocolmaster WHERE elnprotocoltemplateworkflow_workflowcode IS NOT NULL ) = 0;

update lsusergrouprights set screate = '1', sedit = '1' where modulename = 'IDS_MDL_INVENTORY' and screenname = 'IDS_SCN_MATERIALTYPEPARAMS' and displaytopic = 'IDS_SCN_MATERIALTYPEPARAMS' and screate='NA' and sedit='NA';