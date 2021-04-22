ALTER TABLE if exists email alter column  mailcontent Type varchar(5000);
ALTER TABLE IF Exists lsusermaster ADD COLUMN IF NOT EXISTS passwordstatus Integer;
ALTER TABLE IF Exists lsusermaster ADD COLUMN IF NOT EXISTS userretirestatus Integer;

INSERT into LSusergrouprightsmaster (orderno, displaytopic, modulename, sallow, screate, sdelete,sedit, status,sequenceorder)VALUES (46, 'Pending', 'Protocol Order And Register', '0', '0', 'NA', 'NA', '1,0,0',46) ON CONFLICT(orderno)DO NOTHING;
INSERT into LSusergrouprightsmaster (orderno, displaytopic, modulename, sallow, screate, sdelete, sedit, status,sequenceorder)VALUES (47, 'Completed', 'Protocol Order And Register', '0', '0', 'NA', 'NA', '1,0,0',47) ON CONFLICT(orderno)DO NOTHING;
INSERT into LSusergrouprightsmaster (orderno, displaytopic, modulename, sallow, screate, sdelete, sedit, status,sequenceorder)VALUES (48, 'ELN Protocol Order', 'Protocol Order And Register', '0', '0', 'NA', 'NA', '1,0,0',48) ON CONFLICT(orderno)DO NOTHING;
INSERT into LSusergrouprightsmaster (orderno, displaytopic, modulename, sallow, screate, sdelete, sedit, status,sequenceorder)VALUES (49, 'Dynamic Protocol Order', 'Protocol Order And Register', '0', '0', 'NA', 'NA', '1,0,0',49) ON CONFLICT(orderno)DO NOTHING;
INSERT into LSusergrouprightsmaster (orderno, displaytopic, modulename, sallow, screate, sdelete, sedit, status,sequenceorder)VALUES (50, 'New', 'Protocols', '0', '0', 'NA', 'NA', '1,0,0',50) ON CONFLICT(orderno)DO NOTHING;

INSERT into LSusergrouprights (createdby, createdon, displaytopic, modifiedby, modifiedon, modulename, sallow, screate, sdelete, sedit, lssitemaster_sitecode, usergroupid_usergroupcode) VALUES (N'administrator', CAST(N'2020-02-21T14:50:55.727' AS date), 'Pending', NULL, NULL, 'Protocol Order And Register', '1', '1', 'NA', 'NA', 1, 1) ON CONFLICT(orderno)DO NOTHING;
INSERT into LSusergrouprights (createdby, createdon, displaytopic, modifiedby, modifiedon, modulename, sallow, screate, sdelete, sedit, lssitemaster_sitecode, usergroupid_usergroupcode) VALUES (N'administrator', CAST(N'2020-02-21T14:50:55.727' AS date), 'Completed', NULL, NULL, 'Protocol Order And Register', '1','1', 'NA', 'NA', 1, 1) ON CONFLICT(orderno)DO NOTHING;
INSERT into LSusergrouprights (createdby, createdon, displaytopic, modifiedby, modifiedon, modulename, sallow, screate, sdelete, sedit, lssitemaster_sitecode, usergroupid_usergroupcode) VALUES (N'administrator', CAST(N'2020-02-21T14:50:55.727' AS date), 'ELN Protocol Order', NULL, NULL, 'Protocol Order And Register', '1', '1','NA', 'NA', 1, 1) ON CONFLICT(orderno)DO NOTHING;
INSERT into LSusergrouprights (createdby, createdon, displaytopic, modifiedby, modifiedon, modulename, sallow, screate, sdelete, sedit, lssitemaster_sitecode, usergroupid_usergroupcode) VALUES (N'administrator', CAST(N'2020-02-21T14:50:55.727' AS date), 'Dynamic Protocol Order', NULL, NULL, 'Protocol Order And Register', '1', '1', 'NA', 'NA', 1, 1) ON CONFLICT(orderno)DO NOTHING;
INSERT into LSusergrouprights (createdby, createdon, displaytopic, modifiedby, modifiedon, modulename, sallow, screate, sdelete, sedit, lssitemaster_sitecode, usergroupid_usergroupcode) VALUES (N'administrator', CAST(N'2020-02-21T14:50:55.727' AS date), 'New', NULL, NULL, 'Protocols', '1', '1', 'NA', 'NA', 1, 1) ON CONFLICT(orderno)DO NOTHING;

ALTER TABLE IF Exists lsusermaster ADD COLUMN IF NOT EXISTS unifieduserid varchar(500);

CREATE TABLE IF NOT EXISTS public.lscentralisedusers
(
    centralisedusercode integer NOT NULL ,
    id bigint,
    sitecode integer,
    sitename character varying(255) COLLATE pg_catalog."default",
    tenantid character varying(255) COLLATE pg_catalog."default",
    tenantname character varying(255) COLLATE pg_catalog."default",
    unifieduserid character varying(255) COLLATE pg_catalog."default",
    usercode integer,
    username character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT lscentralisedusers_pkey PRIMARY KEY (centralisedusercode)
);

ALTER TABLE if exists lsusergrouprights alter column  createdby Type varchar(250);
ALTER TABLE if exists lsusergrouprights alter column  modifiedby Type varchar(250);

ALTER TABLE IF Exists LSprotocolstep ADD COLUMN IF NOT EXISTS sitecode int;
ALTER TABLE IF Exists lslogilabprotocolsteps ADD COLUMN IF NOT EXISTS sitecode int;

CREATE TABLE IF NOT EXISTS public.lsordershareto
(
    sharetocode bigint NOT NULL,
    sharebatchcode bigint,
    sharebatchid character varying(250) COLLATE pg_catalog."default",
    sharebyunifiedid character varying(250) COLLATE pg_catalog."default",
    shareitemdetails jsonb,
    sharerights integer NOT NULL,
    sharestatus integer NOT NULL,
    sharetounifiedid character varying(250) COLLATE pg_catalog."default",
    CONSTRAINT lsordershareto_pkey PRIMARY KEY (sharetocode)
);

CREATE TABLE IF NOT EXISTS public.lsordersharedby
(
    sharedbycode bigint NOT NULL,
    sharebatchcode bigint,
    sharebatchid character varying(250) COLLATE pg_catalog."default",
    sharebyunifiedid character varying(250) COLLATE pg_catalog."default",
    shareitemdetails jsonb,
    sharerights integer NOT NULL,
    sharestatus integer NOT NULL,
    sharetocode bigint,
    sharetounifiedid character varying(250) COLLATE pg_catalog."default",
    CONSTRAINT lsordersharedby_pkey PRIMARY KEY (sharedbycode)
);

ALTER TABLE IF Exists lsordershareto ADD COLUMN IF NOT EXISTS ordertype int;

ALTER TABLE IF Exists lsordersharedby ADD COLUMN IF NOT EXISTS ordertype int;

UPDATE  lsusermaster SET passwordexpirydate=NOW() + INTERVAL '90 DAY' WHERE passwordexpirydate IS  NULL;

ALTER TABLE IF Exists lsordershareto ADD COLUMN IF NOT EXISTS sharebyusername varchar(250);

ALTER TABLE IF Exists lsordershareto ADD COLUMN IF NOT EXISTS sharetousername varchar(250);

ALTER TABLE IF Exists lsordersharedby ADD COLUMN IF NOT EXISTS sharebyusername varchar(250);

ALTER TABLE IF Exists lsordersharedby ADD COLUMN IF NOT EXISTS sharetousername varchar(250);

ALTER TABLE IF Exists lsordershareto ADD COLUMN IF NOT EXISTS sharedon timestamp;

ALTER TABLE IF Exists lsordershareto ADD COLUMN IF NOT EXISTS sharemodifiedon timestamp;

ALTER TABLE IF Exists lsordershareto ADD COLUMN IF NOT EXISTS unsharedon timestamp;

ALTER TABLE IF Exists lsordersharedby ADD COLUMN IF NOT EXISTS sharedon timestamp;

ALTER TABLE IF Exists lsordersharedby ADD COLUMN IF NOT EXISTS sharemodifiedon timestamp;

ALTER TABLE IF Exists lsordersharedby ADD COLUMN IF NOT EXISTS unsharedon timestamp;

update lsusergrouprights set sedit=1 where lsusergrouprights.displaytopic = 'User Group' and lsusergrouprights.sedit='NA';

update lsusergrouprightsmaster set sedit=1 where lsusergrouprightsmaster.orderno=17 and lsusergrouprightsmaster.sedit='NA';


ALTER TABLE IF Exists lslogilabprotocolsteps ADD COLUMN IF NOT EXISTS orderstepflag varchar(2);

update lslogilabprotocolsteps set orderstepflag = 'N';

ALTER TABLE IF Exists lslogilabprotocoldetail ADD COLUMN IF NOT EXISTS orderflag varchar(2);

update lslogilabprotocoldetail set orderflag = 'N';

update lssheetworkflow set lssitemaster_sitecode =1 where lssheetworkflow.workflowname='New' and lssheetworkflow.lssitemaster_sitecode is null;

UPDATE lsworkflow set lssitemaster_sitecode=1 where lsworkflow.workflowname = 'New' and lsworkflow.lssitemaster_sitecode is null;

UPDATE  lsusermaster SET userretirestatus=0 WHERE userretirestatus is null;

ALTER TABLE IF Exists lsrepositories ADD COLUMN IF NOT EXISTS fieldcount int;

UPDATE  lsrepositories SET fieldcount=0 WHERE fieldcount is null;

ALTER TABLE IF Exists lsrepositoriesdata ADD COLUMN IF NOT EXISTS itemstatus int;

UPDATE  lsrepositoriesdata SET itemstatus=1 WHERE itemstatus is null;

CREATE TABLE IF NOT EXISTS public.LsLogilabprotocolstepInfoCloud
(
    id integer NOT NULL,
    lsprotocolstepinfo jsonb,
    CONSTRAINT LsLogilabprotocolstepInfoCloud_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.helpdocument
(
    id integer NOT NULL,
    documentname character varying(255) COLLATE pg_catalog."default",
    lshelpdocumentcontent jsonb,
    CONSTRAINT helpdocument_pkey PRIMARY KEY (id)
);

update LSusergrouprightsmaster set sallow='0' where sallow='1';
update LSusergrouprightsmaster set screate='0' where  screate='1';
update LSusergrouprightsmaster set sdelete='0' where sdelete='1';
update LSusergrouprightsmaster set sedit='0' where sedit='1';

update LSfileversion set modifiedby_usercode= (select  modifiedby_usercode from LSfileversion where modifiedby_usercode is not null and modifieddate is not null order by modifieddate asc LIMIT  1 ), modifieddate= (select createdate from LSfileversion where modifiedby_usercode is not null and modifieddate is not null order by modifieddate asc LIMIT  1 ) where modifiedby_usercode is null or modifieddate is  null and versionname='version_1';

create table IF NOT EXISTS LSprotocolupdates(
	protocolcode integer NOT NULL,
	protocolcomment character varying(250),
	protocolmastercode integer,
	protocolmodifieddate timestamp without time zone,
	modifiedby_usercode integer
	);
create table IF NOT EXISTS  LSprotocolworkflowhistory(
	historycode integer NOT NULL,
	action character varying(250),
	approvelstatus integer,
	comment character varying(250),
	createdate timestamp without time zone,
	protocolmastercode integer,
	createby_usercode integer,
	currentworkflow_workflowcode integer
	);
	--ALTER TABLE IF Exists lsprotocolmaster ADD COLUMN IF NOT EXISTS createby_usercode integer;
	--UPDATE LSprotocolmaster SET createby_usercode = createdby where createby_usercode is null;
	ALTER TABLE IF Exists lsprotocolmaster DROP COLUMN IF Exists createby_usercode;

	ALTER TABLE IF Exists lsrepositories ADD COLUMN IF NOT EXISTS isconsumable boolean;

	UPDATE  lsrepositories SET isconsumable=false WHERE isconsumable is null;

	ALTER TABLE IF Exists lsrepositories ADD COLUMN IF NOT EXISTS consumefield varchar(250);


