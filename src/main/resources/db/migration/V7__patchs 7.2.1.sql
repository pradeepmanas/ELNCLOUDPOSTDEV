DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'reportdesignerstructure_directorycode_seq' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE reportdesignerstructure_directorycode_seq;
   ELSIF _kind = 'S' THEN  
     
   ELSE                  
    
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.reportdesignerstructure
(
    directorycode bigint NOT NULL DEFAULT nextval('reportdesignerstructure_directorycode_seq'::regclass),
    datecreated timestamp without time zone,
    datemodified timestamp without time zone,
    directoryname character varying(255) COLLATE pg_catalog."default",
    icon character varying(255) COLLATE pg_catalog."default",
    length integer,
    parentdircode bigint,
    path character varying(255) COLLATE pg_catalog."default",
    size integer,
    viewoption integer,
    createdby_usercode integer,
    modifiedby_usercode integer,
    sitemaster_sitecode integer,
    CONSTRAINT reportdesignerstructure_pkey PRIMARY KEY (directorycode),
    CONSTRAINT fke9wnoku9dv39jpx9c3rk5510c FOREIGN KEY (modifiedby_usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fklf4hlolid6x8abwuptit9fs9l FOREIGN KEY (createdby_usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkt6th4pwhdwem2j79pbukw8e8g FOREIGN KEY (sitemaster_sitecode)
        REFERENCES public.lssitemaster (sitecode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.reportdesignerstructure
    OWNER to postgres;
  
 DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'reporttemplate_templatecode_seq' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE reporttemplate_templatecode_seq;
   ELSIF _kind = 'S' THEN  
     
   ELSE                  
    
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.reporttemplate
(
    templatecode bigint NOT NULL DEFAULT nextval('reporttemplate_templatecode_seq'::regclass),
    datecreated timestamp without time zone,
    datemodified timestamp without time zone,
    templatename character varying(255) COLLATE pg_catalog."default",
    templatetype integer,
    viewoption integer,
    createdby_usercode integer,
    modifiedby_usercode integer,
    reportdesignstructure_directorycode bigint,
    sitemaster_sitecode integer,
    CONSTRAINT reporttemplate_pkey PRIMARY KEY (templatecode),
    CONSTRAINT fk27rmob6wbvkpee3lhd2xlwqbr FOREIGN KEY (modifiedby_usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk6emarsho8ib4q9ib96bil563m FOREIGN KEY (sitemaster_sitecode)
        REFERENCES public.lssitemaster (sitecode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkkrbh6f4dued476rm35xy2bkvb FOREIGN KEY (reportdesignstructure_directorycode)
        REFERENCES public.reportdesignerstructure (directorycode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkp85tnq9by6h93e2ca5w87wl1n FOREIGN KEY (createdby_usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;



ALTER TABLE public.reporttemplate
    OWNER to postgres;

	CREATE TABLE IF NOT EXISTS public.cloudreporttemplate
(
    templatecode bigint NOT NULL,
    templatecontent jsonb,
    CONSTRAINT cloudreporttemplate_pkey PRIMARY KEY (templatecode)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

 DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'reportviewerstructure_directorycode_seq' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE reportviewerstructure_directorycode_seq;
   ELSIF _kind = 'S' THEN  
     
   ELSE                  
    
   END IF;
END
$do$;

ALTER TABLE public.cloudreporttemplate
    OWNER to postgres;

	CREATE TABLE IF NOT EXISTS public.reportviewerstructure
(
    directorycode bigint NOT NULL DEFAULT nextval('reportviewerstructure_directorycode_seq'::regclass),
    datecreated timestamp without time zone,
    datemodified timestamp without time zone,
    directoryname character varying(255) COLLATE pg_catalog."default",
    icon character varying(255) COLLATE pg_catalog."default",
    length integer,
    parentdircode bigint,
    path character varying(255) COLLATE pg_catalog."default",
    size integer,
    viewoption integer,
    createdby_usercode integer,
    modifiedby_usercode integer,
    sitemaster_sitecode integer,
    CONSTRAINT reportviewerstructure_pkey PRIMARY KEY (directorycode),
    CONSTRAINT fk6kbsl7fxaig9iiu8hw8bhjfvv FOREIGN KEY (modifiedby_usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk6t549wv5t5vwvhep528oksx6s FOREIGN KEY (createdby_usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fklsjs44g3xaarwqul1jkfjbk2s FOREIGN KEY (sitemaster_sitecode)
        REFERENCES public.lssitemaster (sitecode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;


DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'reports_reportcode_seq' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE reports_reportcode_seq;
   ELSIF _kind = 'S' THEN  
     
   ELSE                  
    
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.reports
(
    reportcode bigint NOT NULL DEFAULT nextval('reports_reportcode_seq'::regclass),
    datecreated timestamp without time zone,
    datemodified timestamp without time zone,
    reportname character varying(255) COLLATE pg_catalog."default",
    templatetype integer,
    viewoption integer,
    createdby_usercode integer,
    modifiedby_usercode integer,
    reporttemplate_templatecode bigint,
    reportviewerstructure_directorycode bigint,
    sitemaster_sitecode integer,
	fromdate timestamp without time zone,
    todate timestamp without time zone,
    CONSTRAINT reports_pkey PRIMARY KEY (reportcode),
    CONSTRAINT fk2u40ksg99iyrnkpjcme51at10 FOREIGN KEY (modifiedby_usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkaoksfhiers4vo22143indihnr FOREIGN KEY (sitemaster_sitecode)
        REFERENCES public.lssitemaster (sitecode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkj72pcjlc2vm6ihqcxohsyjdpx FOREIGN KEY (createdby_usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkj9nrfncme504w6allgoewektw FOREIGN KEY (reportviewerstructure_directorycode)
        REFERENCES public.reportviewerstructure (directorycode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fksihswj6ob6wvf7lysreimq0n6 FOREIGN KEY (reporttemplate_templatecode)
        REFERENCES public.reporttemplate (templatecode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS public.cloudreports
(
    reportcode bigint NOT NULL,
    reporttemplatecontent jsonb,
    CONSTRAINT cloudreports_pkey PRIMARY KEY (reportcode)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;
delete from lsusergrouprightsmaster where displaytopic in('IDS_SCN_REPORTS','IDS_TSK_NEWDOCUMENT','IDS_TSK_NEWTEMP','IDS_TSK_GENERATEREPORT','IDS_TSK_OPENREPORT','IDS_TSK_IMPORTDOCX');
delete from lsusergrouprights where displaytopic in('IDS_SCN_REPORTS','IDS_TSK_NEWDOCUMENT','IDS_TSK_NEWTEMP','IDS_TSK_GENERATEREPORT','IDS_TSK_OPENREPORT','IDS_TSK_IMPORTDOCX');

INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (34, 'IDS_SCN_REPORTDESIGNERNEWFOLDER', 'IDS_MDL_REPORTS', '0', '0', 'NA', 'NA', '0,0,0',64,'IDS_SCN_REPORTDESIGNER') ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (58, 'IDS_SCN_REPORTDESIGNER', 'IDS_MDL_REPORTS', '0', '0', 'NA', 'NA', '0,0,0',65,'IDS_SCN_REPORTDESIGNER') ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (45, 'IDS_SCN_REPORTVIEWERNEWFOLDER', 'IDS_MDL_REPORTS', '0', '0', 'NA', 'NA', '0,0,0',66,'IDS_SCN_REPORTVIEWER') ON CONFLICT(orderno)DO NOTHING; 
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (35, 'IDS_SCN_REPORTVIEWER', 'IDS_MDL_REPORTS', '0', '0', 'NA', 'NA', '0,0,0',67,'IDS_SCN_REPORTVIEWER') ON CONFLICT(orderno)DO NOTHING; 
 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_SCN_REPORTDESIGNERNEWFOLDER', 'IDS_MDL_REPORTS', 'administrator', '1', '1', 'NA', 'NA', 1,1,'IDS_SCN_REPORTDESIGNER'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_SCN_REPORTDESIGNERNEWFOLDER' and usergroupid_usergroupcode = 1);
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_SCN_REPORTDESIGNER', 'IDS_MDL_REPORTS', 'administrator', '1', '1', 'NA', 'NA', 1,1,'IDS_SCN_REPORTDESIGNER'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_SCN_REPORTDESIGNER' and usergroupid_usergroupcode = 1);
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_SCN_REPORTVIEWERNEWFOLDER', 'IDS_MDL_REPORTS', 'administrator', '1', '1', 'NA', 'NA', 1,1,'IDS_SCN_REPORTVIEWER'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_SCN_REPORTVIEWERNEWFOLDER' and usergroupid_usergroupcode = 1);
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_SCN_REPORTVIEWER', 'IDS_MDL_REPORTS', 'administrator', '1', '1', 'NA', 'NA', 1,1,'IDS_SCN_REPORTVIEWER'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_SCN_REPORTVIEWER' and usergroupid_usergroupcode = 1); 

update lsusergrouprightsmaster set sdelete='0' where sdelete='NA' and displaytopic='IDS_SCN_ORDERWORKLOW';
update lsusergrouprights set sdelete='1' where displaytopic='IDS_SCN_ORDERWORKLOW' and usergroupid_usergroupcode=1;
update lsusergrouprights set sdelete='0' where sdelete='NA' and displaytopic='IDS_SCN_ORDERWORKLOW' and usergroupid_usergroupcode!=1;
update lsusergrouprightsmaster set sdelete='0' where sdelete='NA' and displaytopic='IDS_SCN_TEMPLATEWORKFLOW';
update lsusergrouprights set sdelete='1' where displaytopic='IDS_SCN_TEMPLATEWORKFLOW' and usergroupid_usergroupcode=1;
update lsusergrouprights set sdelete='0' where sdelete='NA' and displaytopic='IDS_SCN_TEMPLATEWORKFLOW' and usergroupid_usergroupcode!=1;
update lsusergrouprightsmaster set screate='0' where screate='NA' and displaytopic='IDS_SCN_MATERIALTYPE';
update lsusergrouprights set screate='1' where displaytopic='IDS_SCN_MATERIALTYPE' and usergroupid_usergroupcode=1;
update lsusergrouprights set screate='0' where screate='NA' and displaytopic='IDS_SCN_MATERIALTYPE' and usergroupid_usergroupcode!=1;
update lsusergrouprights set screate='0' where screate='NA' and displaytopic='IDS_SCN_MATERIALTYPE' and usergroupid_usergroupcode!=1;

ALTER TABLE IF Exists lsprotocolorderstructure ADD COLUMN IF NOT EXISTS floatvalues bigint;
ALTER TABLE IF Exists lssheetorderstructure ADD COLUMN IF NOT EXISTS floatvalues bigint;


delete from lsaudittrailconfigmaster where modulename='IDS_MDL_REPORTS' and taskname in('IDS_TSK_CONFIGURE','IDS_TSK_DIRECTORYSAVE','IDS_TSK_NEWTEMP','IDS_TSK_SAVE','IDS_TSK_SAVEAS','IDS_TSK_GENERATEREPORT');
delete from lsaudittrailconfiguration where modulename='IDS_MDL_REPORTS' and taskname in('IDS_TSK_CONFIGURE','IDS_TSK_DIRECTORYSAVE','IDS_TSK_NEWTEMP','IDS_TSK_SAVE','IDS_TSK_SAVEAS','IDS_TSK_GENERATEREPORT');

Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) 
values(47,0,'IDS_MDL_REPORTS',94,'IDS_SCN_REPORTDESIGNER','IDS_TSK_RDEWFOLDER') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) 
values(49,0,'IDS_MDL_REPORTS',95,'IDS_SCN_REPORTDESIGNER','IDS_TSK_RDOPEN') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) 
values(43,0,'IDS_MDL_REPORTS',96,'IDS_SCN_REPORTDESIGNER','IDS_TSK_SAVE') ON CONFLICT(serialno)DO NOTHING;

Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) 
values(29,0,'IDS_MDL_REPORTS',98,'IDS_SCN_REPORTVIEWER','IDS_TSK_RDEWFOLDER') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) 
values(52,0,'IDS_MDL_REPORTS',99,'IDS_SCN_REPORTVIEWER','IDS_TSK_RDOPEN') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) 
values(53,0,'IDS_MDL_REPORTS',100,'IDS_SCN_REPORTVIEWER','IDS_TSK_SAVE') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) 
values(30,0,'IDS_MDL_REPORTS',101,'IDS_SCN_REPORTVIEWER','IDS_TSK_CONTINUE') ON CONFLICT(serialno)DO NOTHING;

UPDATE lsaudittrailconfigmaster set screenname='IDS_SCN_REPORTDESIGNER' where screenname='IDS_SCN_REPORTS';
update lsaudittrailconfiguration set screenname='IDS_SCN_REPORTDESIGNER' WHERE screenname='IDS_SCN_REPORTS';

update lsaudittrailconfigmaster set ordersequnce=104 where serialno=150 ;
update lsaudittrailconfigmaster set ordersequnce=105 where serialno=151 ;
update lsaudittrailconfigmaster set ordersequnce=106 where serialno=119 ;
update lsaudittrailconfigmaster set ordersequnce=107 where serialno=120 ;
update lsaudittrailconfigmaster set ordersequnce=108 where serialno=152 ;
update lsaudittrailconfigmaster set ordersequnce=109 where serialno=153 ;
update lsaudittrailconfigmaster set ordersequnce=110 where serialno=142 ;
update lsaudittrailconfigmaster set ordersequnce=111 where serialno=154 ;
update lsaudittrailconfigmaster set ordersequnce=112 where serialno=121 ;
update lsaudittrailconfigmaster set ordersequnce=113 where serialno=155 ;
update lsaudittrailconfigmaster set ordersequnce=114 where serialno=122 ;
update lsaudittrailconfigmaster set ordersequnce=115 where serialno=143 ;
update lsaudittrailconfigmaster set ordersequnce=116 where serialno=123 ;
update lsaudittrailconfigmaster set ordersequnce=117 where serialno=124 ;
update lsaudittrailconfigmaster set ordersequnce=118 where serialno=144 ;
update lsaudittrailconfigmaster set ordersequnce=119 where serialno=125 ;
update lsaudittrailconfigmaster set ordersequnce=120 where serialno=126 ;
update lsaudittrailconfigmaster set ordersequnce=121 where serialno=145 ;
update lsaudittrailconfigmaster set ordersequnce=122 where serialno=127 ;
update lsaudittrailconfigmaster set ordersequnce=123 where serialno=128 ;
update lsaudittrailconfigmaster set ordersequnce=124 where serialno=146 ;
update lsaudittrailconfigmaster set ordersequnce=125 where serialno=129 ;
update lsaudittrailconfigmaster set ordersequnce=126 where serialno=71 ;
update lsaudittrailconfigmaster set ordersequnce=127 where serialno=72 ;
update lsaudittrailconfigmaster set ordersequnce=128 where serialno=73 ;
update lsaudittrailconfigmaster set ordersequnce=129 where serialno=74 ;
update lsaudittrailconfigmaster set ordersequnce=130 where serialno=75 ;
update lsaudittrailconfigmaster set ordersequnce=131 where serialno=132 ;
update lsaudittrailconfigmaster set ordersequnce=132 where serialno=133 ;
update lsaudittrailconfigmaster set ordersequnce=133 where serialno=134 ;
update lsaudittrailconfigmaster set ordersequnce=134 where serialno=135 ;
update lsaudittrailconfigmaster set ordersequnce=135 where serialno=81 ;
update lsaudittrailconfigmaster set ordersequnce=136 where serialno=82 ;
update lsaudittrailconfigmaster set ordersequnce=137 where serialno=83 ;
update lsaudittrailconfigmaster set ordersequnce=108 where serialno=142 ;
update lsaudittrailconfigmaster set ordersequnce=110 where serialno=152 ;
update lsaudittrailconfigmaster set ordersequnce=109 where serialno=121 ;
update lsaudittrailconfigmaster set ordersequnce=104 where serialno=150 ;
update lsaudittrailconfigmaster set ordersequnce=106 where serialno=119 ;
update lsaudittrailconfigmaster set screenname='IDS_SCN_UNITMASTER' where serialno=150 ;
update lsaudittrailconfigmaster set screenname='IDS_SCN_GRADEMASTER' where serialno=119 ;

ALTER TABLE IF Exists LSprotocolmaster ADD COLUMN IF NOT EXISTS fileuid varchar(250);
ALTER TABLE IF Exists LSprotocolmaster ADD COLUMN IF NOT EXISTS fileuri varchar(500);
ALTER TABLE IF Exists LSprotocolmaster ADD COLUMN IF NOT EXISTS containerstored integer default 0;

ALTER TABLE IF Exists materialinventory ADD Column IF NOT EXISTS isexpiryneed BOOLEAN;
ALTER TABLE IF Exists materialinventory ADD Column IF NOT EXISTS expirydate DATE;
ALTER TABLE IF Exists materialinventory ADD Column IF NOT EXISTS validationneed Boolean;
ALTER TABLE IF Exists materialinventory ADD Column IF NOT EXISTS validationdate TIMESTAMP;

ALTER TABLE IF Exists material ADD Column IF NOT EXISTS expirypolicy BOOLEAN;
ALTER TABLE IF Exists material ADD Column IF NOT EXISTS expirypolicyvalue character varying(50);
ALTER TABLE IF Exists material ADD Column IF NOT EXISTS expirypolicyperiod character varying(50);

ALTER TABLE IF Exists material ADD Column IF NOT EXISTS openexpiry BOOLEAN;
ALTER TABLE IF Exists material ADD Column IF NOT EXISTS openexpiryvalue character varying(50);
ALTER TABLE IF Exists material ADD Column IF NOT EXISTS openexpiryperiod character varying(50);

ALTER TABLE IF Exists material ADD Column IF NOT EXISTS nextvalidation BOOLEAN;
ALTER TABLE IF Exists material ADD Column IF NOT EXISTS nextvalidationvalue character varying(50);
ALTER TABLE IF Exists material ADD Column IF NOT EXISTS nextvalidationperiod character varying(50);

ALTER TABLE IF Exists LSusersteam ADD Column IF NOT EXISTS createdate DATE;
update LSusersteam set createdate=modifieddate where createdate is Null;

ALTER TABLE IF Exists LSusermaster ADD Column IF NOT EXISTS isadsuser integer;

update LSusermaster set isadsuser = 0 where isadsuser is null;

CREATE TABLE IF NOT EXISTS tbladssettings
(
    ldaplocationid character varying(255) COLLATE pg_catalog."default" NOT NULL,
    createddate timestamp without time zone,
    groupname integer,
    lastsyncdate timestamp without time zone,
    ldaplocation character varying(255) COLLATE pg_catalog."default",
    ldapserverdomainname character varying(255) COLLATE pg_catalog."default",
    ldapstatus integer,
    CONSTRAINT tbladssettings_pkey PRIMARY KEY (ldaplocationid)
)
WITH (OIDS = FALSE) TABLESPACE pg_default;

ALTER TABLE tbladssettings OWNER to postgres;

ALTER TABLE IF EXISTS materialinventory ALTER COLUMN expirydate TYPE timestamp without time zone;

ALTER TABLE IF Exists LSlogilabprotocoldetail ADD COLUMN IF NOT EXISTS fileuid varchar(250);
ALTER TABLE IF Exists LSlogilabprotocoldetail ADD COLUMN IF NOT EXISTS fileuri varchar(500);
ALTER TABLE IF Exists LSlogilabprotocoldetail ADD COLUMN IF NOT EXISTS containerstored integer default 0;

ALTER TABLE IF Exists method ADD COLUMN IF NOT EXISTS converterstatus integer;
update method set converterstatus=1 where converterstatus is Null;

--datablock


DO $$
DECLARE
  dvalue Integer;
BEGIN
  SELECT delimiterkey INTO dvalue FROM delimiter WHERE delimitername='None';
  
  IF NOT EXISTS (SELECT 1 FROM methoddelimiter WHERE delimiterkey = dvalue and parsermethodkey =1) THEN
  
  INSERT INTO methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue,methoddelimiterstatus) VALUES 
  (1,1,dvalue,1,1,'A');
  END IF;
END $$;


DO $$
DECLARE
  dvalue Integer;
BEGIN
 
  SELECT delimiterkey INTO dvalue FROM delimiter WHERE delimitername='Result without space';
 
  IF NOT EXISTS (SELECT 1 FROM methoddelimiter WHERE delimiterkey = dvalue and parsermethodkey =1) THEN

  INSERT INTO methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue,methoddelimiterstatus) VALUES 
  (1,1,dvalue,1,1,'A');
  END IF;
END $$;

DO $$
DECLARE
  dvalue Integer;
BEGIN
 
  SELECT delimiterkey INTO dvalue FROM delimiter WHERE delimitername='Result with space';
 
  IF NOT EXISTS (SELECT 1 FROM methoddelimiter WHERE delimiterkey = dvalue and parsermethodkey =1) THEN
 
  INSERT INTO methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue,methoddelimiterstatus) VALUES 
  (1,1,dvalue,1,1,'A');
  END IF;
END $$;

DO $$
DECLARE
  dvalue Integer;
BEGIN
  
  SELECT delimiterkey INTO dvalue FROM delimiter WHERE delimitername='Comma';

  IF NOT EXISTS (SELECT 1 FROM methoddelimiter WHERE delimiterkey = dvalue and parsermethodkey =1) THEN
  
  INSERT INTO methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue,methoddelimiterstatus) VALUES 
  (1,1,dvalue,1,1,'A');
  END IF;
END $$;

--for split

DO $$
DECLARE
  dvalue Integer;
BEGIN
  SELECT delimiterkey INTO dvalue FROM delimiter WHERE delimitername='Result without space';

  IF NOT EXISTS (SELECT 1 FROM methoddelimiter WHERE delimiterkey = dvalue and parsermethodkey =7) THEN

     INSERT INTO methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue,methoddelimiterstatus) VALUES 
  (1,1,dvalue,7,1,'A');
  END IF;
END $$;

DO $$
DECLARE
  dvalue Integer;
BEGIN

  SELECT delimiterkey INTO dvalue FROM delimiter WHERE delimitername='Result with space';


  IF NOT EXISTS (SELECT 1 FROM methoddelimiter WHERE delimiterkey = dvalue and parsermethodkey =7) THEN

     INSERT INTO methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue,methoddelimiterstatus) VALUES 
  (1,1,dvalue,7,1,'A');
  END IF;
END $$;

DO $$
DECLARE
  dvalue Integer;
BEGIN
  SELECT delimiterkey INTO dvalue FROM delimiter WHERE delimitername='Colon';

  IF NOT EXISTS (SELECT 1 FROM methoddelimiter WHERE delimiterkey = dvalue and parsermethodkey =7) THEN

  INSERT INTO methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue,methoddelimiterstatus) VALUES 
  (1,1,dvalue,7,1,'A');
  END IF;
END $$;

DO $$
DECLARE
  dvalue Integer;
BEGIN
  SELECT delimiterkey INTO dvalue FROM delimiter WHERE delimitername='Comma';

  IF NOT EXISTS (SELECT 1 FROM methoddelimiter WHERE delimiterkey = dvalue and parsermethodkey =7) THEN

  INSERT INTO methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue,methoddelimiterstatus) VALUES 
  (1,1,dvalue,7,1,'A');
  END IF;
END $$;

DO $$
DECLARE
  dvalue Integer;
BEGIN
  SELECT delimiterkey INTO dvalue FROM delimiter WHERE delimitername='Space';

  IF NOT EXISTS (SELECT 1 FROM methoddelimiter WHERE delimiterkey = dvalue and parsermethodkey =7) THEN

  INSERT INTO methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue,methoddelimiterstatus) VALUES 
  (1,1,dvalue,7,1,'A');
  END IF;
END $$;

DO $$
DECLARE
  dvalue Integer;
BEGIN
  SELECT delimiterkey INTO dvalue FROM delimiter WHERE delimitername='Split Dot';

  IF NOT EXISTS (SELECT 1 FROM methoddelimiter WHERE delimiterkey = dvalue and parsermethodkey =7) THEN

  INSERT INTO methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue,methoddelimiterstatus) VALUES 
  (1,1,dvalue,7,1,'A');
  END IF;
END $$;

DO $$
DECLARE
  dvalue Integer;
BEGIN
  SELECT delimiterkey INTO dvalue FROM delimiter WHERE delimitername='Merge Dot';

  IF NOT EXISTS (SELECT 1 FROM methoddelimiter WHERE delimiterkey = dvalue and parsermethodkey =7) THEN

  INSERT INTO methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue,methoddelimiterstatus) VALUES 
  (1,1,dvalue,7,1,'A');
  END IF;
END $$;

DO $$
DECLARE
  dvalue Integer;
BEGIN
  SELECT delimiterkey INTO dvalue FROM delimiter WHERE delimitername='Slash';

  IF NOT EXISTS (SELECT 1 FROM methoddelimiter WHERE delimiterkey = dvalue and parsermethodkey =7) THEN

  INSERT INTO methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue,methoddelimiterstatus) VALUES 
  (1,1,dvalue,7,1,'A');
  END IF;
END $$;

DO $$
DECLARE
  dvalue Integer;
BEGIN
  SELECT delimiterkey INTO dvalue FROM delimiter WHERE delimitername='None';

  IF NOT EXISTS (SELECT 1 FROM methoddelimiter WHERE delimiterkey = dvalue and parsermethodkey =7) THEN

  INSERT INTO methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue,methoddelimiterstatus) VALUES 
  (1,1,dvalue,7,1,'A');
  END IF;
END $$;

---merge

DO $$
DECLARE
  dvalue Integer;
BEGIN
  SELECT delimiterkey INTO dvalue FROM delimiter WHERE delimitername='Merge Dot';

  IF NOT EXISTS (SELECT 1 FROM methoddelimiter WHERE delimiterkey = dvalue and parsermethodkey =6) THEN

  INSERT INTO methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue,methoddelimiterstatus) VALUES 
  (1,1,dvalue,6,1,'A');
  END IF;
END $$;

DO $$
DECLARE
  dvalue Integer;
BEGIN
  SELECT delimiterkey INTO dvalue FROM delimiter WHERE delimitername='Slash';

  IF NOT EXISTS (SELECT 1 FROM methoddelimiter WHERE delimiterkey = dvalue and parsermethodkey =6) THEN

  INSERT INTO methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue,methoddelimiterstatus) VALUES 
  (1,1,dvalue,6,1,'A');
  END IF;
END $$;

DO $$
DECLARE
  dvalue Integer;
BEGIN
  SELECT delimiterkey INTO dvalue FROM delimiter WHERE delimitername='Colon';

  IF NOT EXISTS (SELECT 1 FROM methoddelimiter WHERE delimiterkey = dvalue and parsermethodkey =6) THEN

  INSERT INTO methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue,methoddelimiterstatus) VALUES 
  (1,1,dvalue,6,1,'A');
  END IF;
END $$;

DO $$
DECLARE
  dvalue Integer;
BEGIN
  SELECT delimiterkey INTO dvalue FROM delimiter WHERE delimitername='Comma';

  IF NOT EXISTS (SELECT 1 FROM methoddelimiter WHERE delimiterkey = dvalue and parsermethodkey =6) THEN

  INSERT INTO methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue,methoddelimiterstatus) VALUES 
  (1,1,dvalue,6,1,'A');
  END IF;
END $$;

DO $$
DECLARE
  dvalue Integer;
BEGIN
  SELECT delimiterkey INTO dvalue FROM delimiter WHERE delimitername='Space';

  IF NOT EXISTS (SELECT 1 FROM methoddelimiter WHERE delimiterkey = dvalue and parsermethodkey =6) THEN

  INSERT INTO methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue,methoddelimiterstatus) VALUES 
  (1,1,dvalue,6,1,'A');
  END IF;
END $$;

DO $$
DECLARE
  dvalue Integer;
BEGIN
  SELECT delimiterkey INTO dvalue FROM delimiter WHERE delimitername='None';

  IF NOT EXISTS (SELECT 1 FROM methoddelimiter WHERE delimiterkey = dvalue and parsermethodkey =6) THEN

  INSERT INTO methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue,methoddelimiterstatus) VALUES 
  (1,1,dvalue,6,1,'A');
  END IF;
END $$;

ALTER TABLE IF Exists lslogilablimsorderdetail ADD COLUMN IF NOT EXISTS activeuser integer;

ALTER TABLE IF Exists lsactiveuser ADD Column IF NOT EXISTS removeinititated BOOLEAN;

update LSactiveuser set removeinititated =false where removeinititated is null;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'selectedinventorymapped_sequence' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE selectedinventorymapped_sequence;
   ELSIF _kind = 'S' THEN  
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.selectedinventorymapped
(
    mappedid  integer NOT NULL DEFAULT nextval('selectedinventorymapped_sequence'::regclass),
    id character varying(150),
    nmaterialinventorycode integer,
    samplestoragelocationkey integer,
    CONSTRAINT selectedinventorymapped_pkey PRIMARY KEY (mappedid),
    CONSTRAINT fk8fernqbshr6gaugjiauyb9b1 FOREIGN KEY (nmaterialinventorycode)
        REFERENCES public.materialinventory (nmaterialinventorycode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk8fernqbshr6gaprjiauyb9b1 FOREIGN KEY (samplestoragelocationkey)
        REFERENCES public.samplestoragelocation (samplestoragelocationkey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH ( OIDS = FALSE ) TABLESPACE pg_default;

ALTER TABLE public.selectedinventorymapped OWNER to postgres;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'lsmultisites_multisitecode_seq' 
   INTO  _kind;

   IF NOT FOUND THEN       
      CREATE SEQUENCE lsmultisites_multisitecode_seq;
   ELSIF _kind = 'S' THEN  
   ELSE                   
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.lsmultisites
(
    multisitecode integer NOT NULL DEFAULT nextval('lsmultisites_multisitecode_seq'::regclass),
    defaultsitemaster integer,
    usercode integer,
    lssitemaster_sitecode integer,
    CONSTRAINT lsmultisites_pkey PRIMARY KEY (multisitecode),
    CONSTRAINT fk3mh1ps7ew0vhwxccr1ja6g94v FOREIGN KEY (lssitemaster_sitecode)
        REFERENCES public.lssitemaster (sitecode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk57k5skwcounawuube2lagetg5 FOREIGN KEY (usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.lsmultisites
    OWNER to postgres;


DO
$do$
DECLARE
  multisitecount integer := 0;
BEGIN
  SELECT COUNT(*) INTO multisitecount FROM lsmultisites;

  IF multisitecount = 0 THEN
    INSERT INTO lsmultisites (usercode, lssitemaster_sitecode, defaultsiteMaster)
    SELECT lsusermaster.usercode, lssitemaster.sitecode, 1
    FROM lsusermaster
    JOIN lssitemaster ON lsusermaster.lssitemaster_sitecode = lssitemaster.sitecode;
  END IF;
END
$do$;

ALTER TABLE IF Exists LSprotocolimages ADD Column IF NOT EXISTS islinkimage boolean;

ALTER TABLE IF Exists LSprotocolfiles ADD Column IF NOT EXISTS islinkfile boolean ;

ALTER TABLE IF Exists reporttemplate ADD COLUMN IF NOT EXISTS reporttype integer;
update reporttemplate set reporttype=1 where reporttype is Null;

ALTER TABLE IF Exists selectedinventorymapped ADD COLUMN IF NOT EXISTS storagepath character varying(255);

ALTER TABLE IF Exists materialinventory ADD Column IF NOT EXISTS createddate DATE;

-- Attempt to insert into samplestoragelocation, but only if a record with samplestoragelocationkey = -1 does not exist
INSERT INTO public.samplestoragelocation(
    samplestoragelocationkey, createdby, createddate, samplestoragelocationname, sitekey, status)
VALUES (-1, 'Administrator', now(), 'Default', 1, 1)
ON CONFLICT (samplestoragelocationkey) DO NOTHING;

-- Attempt to insert into samplestorageversion, but only if a record with samplestorageversionkey = -1 does not exist
INSERT INTO public.samplestorageversion(
    samplestorageversionkey, approvalstatus, createdby, createddate, jsonbresult, versionno, samplestoragelocationkey)
VALUES (-1, 1, 'Administrator', now(), '[{"text":"Default node","expanded":true,"editable":false,"root":true,"id":"e9052aac-b470-44e0-a2de-5652f7475b67"}]', 1, -1)
ON CONFLICT (samplestorageversionkey) DO NOTHING;

UPDATE materialtype SET jsondata = '{
    "sdescription": "ALL",
    "smaterialtypename": {
        "en-US": "ALL",
        "ru-RU": "нет данных",
        "tg-TG": "ALL"
    }
}'WHERE nmaterialtypecode = -1;

ALTER TABLE IF Exists unit ADD COLUMN IF NOT EXISTS createby_usercode integer default 1 ,ADD COLUMN IF NOT EXISTS createdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE IF Exists section ADD COLUMN IF NOT EXISTS createby_usercode integer default 1 ,ADD COLUMN IF NOT EXISTS createdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE IF Exists materialgrade ADD COLUMN IF NOT EXISTS createby_usercode integer default 1 ,ADD COLUMN IF NOT EXISTS createdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE IF Exists supplier ADD COLUMN IF NOT EXISTS createby_usercode integer default 1 ,ADD COLUMN IF NOT EXISTS createdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE IF Exists manufacturer ADD COLUMN IF NOT EXISTS createby_usercode integer default 1 ,ADD COLUMN IF NOT EXISTS createdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE IF Exists materialcategory ADD COLUMN IF NOT EXISTS createby_usercode integer default 1 ,ADD COLUMN IF NOT EXISTS createdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

DO $$ 
BEGIN
    IF NOT EXISTS (SELECT conname
                   FROM pg_constraint
                   WHERE conname = 'fknok3att985drj28p3b41qn888') THEN
        ALTER TABLE ResultUsedMaterial
        ADD CONSTRAINT fknok3att985drj28p3b41qn888
        FOREIGN KEY (ninventorycode)
        REFERENCES public.materialinventory (nmaterialinventorycode)
        MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION;
    END IF;
END $$;

ALTER TABLE IF Exists LSlogilabprotocoldetail ADD COLUMN IF NOT EXISTS orderstarted integer ,ADD COLUMN IF NOT EXISTS orderstartedby_usercode integer ,ADD COLUMN IF NOT EXISTS orderstartedon TIMESTAMP;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'elnmaterial_sequence' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE elnmaterial_sequence;
   ELSIF _kind = 'S' THEN  
      -- do nothing?
   ELSE             
      -- do something!
   END IF;
END
$do$;
CREATE TABLE IF NOT EXISTS public.elnmaterial
(
    nmaterialcode integer NOT NULL DEFAULT nextval('elnmaterial_sequence'::regclass),
    materialcategory_nmaterialcatcode integer NOT NULL,
    materialtype_nmaterialtypecode integer NOT NULL,
    unit_nunitcode integer,
    section_nsectioncode integer,
    smaterialname character varying(100) COLLATE pg_catalog."default" NOT NULL,
    jsondata jsonb,
    nstatus integer NOT NULL DEFAULT 1,
    sprefix character varying(50) COLLATE pg_catalog."default",
    nsitecode integer,
    createddate timestamp without time zone DEFAULT ('now'::text)::date,
    expirytype integer,
    remarks character varying(200) COLLATE pg_catalog."default",
    expirypolicyvalue character varying(50) COLLATE pg_catalog."default",
    expirypolicyperiod character varying(50) COLLATE pg_catalog."default",
    openexpiryvalue character varying(50) COLLATE pg_catalog."default",
    openexpiryperiod character varying(50) COLLATE pg_catalog."default",
    openexpiry boolean,
    quarantine boolean,
    reusable boolean,
    CONSTRAINT elnmaterial_pkey PRIMARY KEY (nmaterialcode)
)
WITH (OIDS = FALSE ) TABLESPACE pg_default;

ALTER TABLE public.elnmaterial OWNER to postgres;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'elnmaterialinventory_sequence' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE elnmaterialinventory_sequence;
   ELSIF _kind = 'S' THEN  
      -- do nothing?
   ELSE             
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.elnmaterialinventory
(
    nmaterialinventorycode integer NOT NULL DEFAULT nextval('elnmaterialinventory_sequence'::regclass),
    material_nmaterialcode integer NOT NULL,
    materialcategory_nmaterialcatcode integer NOT NULL,
    materialtype_nmaterialtypecode integer NOT NULL,
    section_nsectioncode integer,
    unit_nunitcode integer,
    materialgrade_nmaterialgradecode integer,
    supplier_nsuppliercode integer,
    manufacturer_nmanufcode integer,
    jsondata text COLLATE pg_catalog."default",
    ntransactionstatus integer NOT NULL,
    nstatus integer NOT NULL DEFAULT 1,
    nsitecode integer,
    nqtynotification double precision,
    isexpiry boolean,
    expirydate timestamp without time zone,
    createddate timestamp without time zone,
    manufacdate timestamp without time zone,
    receiveddate timestamp without time zone,
    remarks character varying(200) COLLATE pg_catalog."default",
    sinventoryid character varying(50) COLLATE pg_catalog."default",
    savailablequantity character varying(50) COLLATE pg_catalog."default",
    sreceivedquantity character varying(50) COLLATE pg_catalog."default",
    CONSTRAINT elnmaterialinventory_pkey PRIMARY KEY (nmaterialinventorycode)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.elnmaterialinventory OWNER to postgres;

DO $$ 
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.table_constraints
        WHERE constraint_name = 'fk701k777d2da33pkkl6lnamavi' 
        AND table_name = 'lsorderattachments'
    ) THEN
        ALTER TABLE public.lsorderattachments
        DROP CONSTRAINT fk701k777d2da33pkkl6lnamavi;

        ALTER TABLE public.lsorderattachments
        ADD CONSTRAINT fk701k777d2da33pkkl6lnamavi
        FOREIGN KEY (nmaterialcode)
        REFERENCES public.elnmaterial (nmaterialcode)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION;
    END IF;
END $$;

ALTER TABLE IF Exists lsprotocolmaster ADD Column IF NOT EXISTS lastmodified timestamp;

-- DO $$ 
-- BEGIN
--     IF NOT EXISTS (
--         SELECT 1
--         FROM information_schema.table_constraints
--         WHERE constraint_name = 'fkbvkyp7mg7pxs4oxsg45bmnj6l' 
--         AND table_name = 'lsorderattachments'
--     ) THEN
--         ALTER TABLE public.lsorderattachments
--         DROP CONSTRAINT fkbvkyp7mg7pxs4oxsg45bmnj6l;

--         ALTER TABLE public.lsorderattachments
--         ADD CONSTRAINT fkbvkyp7mg7pxs4oxsg45bmnj6l
--         FOREIGN KEY (nmaterialinventorycode)
--         REFERENCES public.elnmaterialinventory (nmaterialinventorycode)
--         ON UPDATE NO ACTION
--         ON DELETE NO ACTION;
--     END IF;
-- END $$;

DO $$ 
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.table_constraints
        WHERE constraint_name = 'fk8fernqbshr6gaugjiauyb9b1' 
        AND table_name = 'selectedinventorymapped'
    ) THEN
        ALTER TABLE public.selectedinventorymapped
        DROP CONSTRAINT fk8fernqbshr6gaugjiauyb9b1;

        ALTER TABLE public.selectedinventorymapped
        ADD CONSTRAINT fk8fernqbshr6gaugjiauyb9b1
        FOREIGN KEY (nmaterialinventorycode)
        REFERENCES public.elnmaterialinventory (nmaterialinventorycode)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION;
    END IF;
END $$;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'elnresultusedmaterial_sequence' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE elnresultusedmaterial_sequence;
   ELSIF _kind = 'S' THEN  
      -- do nothing?
   ELSE             
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.elnresultusedmaterial
(
    nresultusedmaterialcode integer NOT NULL DEFAULT nextval('elnresultusedmaterial_sequence'::regclass),
    ordercode numeric(17,0) NOT NULL,
    transactionscreen integer NOT NULL,
    templatecode integer NOT NULL,
    nmaterialtypecode integer NOT NULL,
    nmaterialcategorycode integer NOT NULL,
    nmaterialcode integer NOT NULL,
    ninventorycode integer NOT NULL,
    jsondata character varying(255) COLLATE pg_catalog."default" NOT NULL,
    nstatus integer NOT NULL DEFAULT 1,
    createddate timestamp without time zone,
    nqtyissued double precision,
    nqtyused double precision,
    nqtyleft double precision,
    batchid character varying(100) COLLATE pg_catalog."default",
    lstestmasterlocal_testcode integer,
    lsusermaster_createdbyusercode integer,
    testcode_testcode integer,
    createdbyusercode_usercode integer,
    CONSTRAINT elnresultusedmaterial_pkey PRIMARY KEY (nresultusedmaterialcode),
    CONSTRAINT fk5wxdhabcdewr5m0ejfbh6e47 FOREIGN KEY (lsusermaster_createdbyusercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk5wxdhe1234wr5m0ejfbh6e47 FOREIGN KEY (createdbyusercode_usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk5wxdhe1256wr5m0ejdyd6e47 FOREIGN KEY (testcode_testcode)
        REFERENCES public.lstestmasterlocal (testcode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk5wxdhghijkwr5m0ejabc6e47 FOREIGN KEY (lstestmasterlocal_testcode)
        REFERENCES public.lstestmasterlocal (testcode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fknok3att985drj28p3bwern888 FOREIGN KEY (ninventorycode)
        REFERENCES public.elnmaterialinventory (nmaterialinventorycode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.elnresultusedmaterial OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.lsreportfile
(
    id bigint NOT NULL,
    content jsonb,
    contentstored integer,
    CONSTRAINT lsreportfile_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.lsreportfile
    OWNER to postgres;

ALTER TABLE IF Exists elnmaterialinventory ADD COLUMN IF NOT EXISTS createdby_usercode INTEGER;

ALTER TABLE IF Exists elnmaterialinventory ADD COLUMN IF NOT EXISTS opendate timestamp without time zone;

ALTER TABLE IF Exists elnmaterial ADD COLUMN IF NOT EXISTS createby_usercode INTEGER;

ALTER TABLE IF EXISTS public.lsprotocolmaster ADD COLUMN IF NOT EXISTS protocoldatainfo jsonb;

ALTER TABLE IF EXISTS public.lslogilabprotocoldetail ADD COLUMN IF NOT EXISTS protocoldatainfo jsonb;

ALTER TABLE IF Exists materialtype ADD COLUMN IF NOT EXISTS smaterialtypename varchar(100);

update materialtype set smaterialtypename = 'NA' where nmaterialtypecode = -1;
update materialtype set smaterialtypename = 'Standard Type' where nmaterialtypecode = 1;
update materialtype set smaterialtypename = 'Volumetric Type' where nmaterialtypecode = 2;
update materialtype set smaterialtypename = 'Material Inventory Type' where nmaterialtypecode = 3;
update materialtype set smaterialtypename = 'IQC Material Standard Type' where nmaterialtypecode = 4;

INSERT into LSfields (fieldcode, createby, createdate, fieldorderno, fieldtypecode, isactive, level01code, level01name, level02code, level02name, level03code, level03name, level04code, level04name, siteID) VALUES (60, NULL, NULL, 20, 3, 1, 'G1', 'ID_GENERAL', '21', 'ID_GENERAL', 21, 'ID_GENERAL', 'G21', 'Barcode', 1) on conflict (fieldcode) do nothing;
INSERT into LSfields (fieldcode, createby, createdate, fieldorderno, fieldtypecode, isactive, level01code, level01name, level02code, level02name, level03code, level03name, level04code, level04name, siteID) VALUES (61, NULL, NULL, 21, 3, 1, 'G1', 'ID_GENERAL', '22', 'ID_GENERAL', 22, 'ID_GENERAL', 'G22', 'Formula Field', 1) on conflict (fieldcode) do nothing;

ALTER TABLE IF Exists LSprotocolversion ADD COLUMN IF NOT EXISTS fileuid varchar(250);
ALTER TABLE IF Exists LSprotocolversion ADD COLUMN IF NOT EXISTS fileuri varchar(500);

update elnmaterialInventory set createdby_usercode=(select lsusermaster.usercode from lsusermaster  where lsusermaster.username='Administrator') where createdby_usercode is null;

ALTER TABLE IF Exists materialtype ADD COLUMN IF NOT EXISTS createby_usercode integer default 1,ADD COLUMN IF NOT EXISTS createdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'materialtype_sequence' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE materialtype_sequence START WITH 50;
   ELSIF _kind = 'S' THEN  
      -- do nothing?
   ELSE             
      -- do something!
   END IF;
END
$do$;

ALTER TABLE materialtype ALTER COLUMN nmaterialtypecode SET DEFAULT nextval('materialtype_sequence');

ALTER TABLE IF Exists lslogilablimsorderdetail ADD COLUMN IF NOT EXISTS elnmaterial_nmaterialcode integer,ADD COLUMN IF NOT EXISTS elnmaterialinventory_nmaterialinventorycode integer;
ALTER TABLE IF Exists lslogilabprotocoldetail ADD COLUMN IF NOT EXISTS elnmaterial_nmaterialcode integer,ADD COLUMN IF NOT EXISTS elnmaterialinventory_nmaterialinventorycode integer;

ALTER TABLE IF Exists materialtype ADD Column IF NOT EXISTS expvalidation Boolean default false, ADD Column IF NOT EXISTS quarvalidation Boolean default false;
ALTER TABLE IF Exists elnmaterialinventory ADD Column IF NOT EXISTS sbatchno varchar(250) default '';

ALTER TABLE IF Exists elnmaterial ADD COLUMN IF NOT EXISTS samplecode integer;

ALTER TABLE IF Exists lslogilabprotocoldetail ADD COLUMN IF NOT EXISTS lockeduser INTEGER;
ALTER TABLE IF Exists lslogilabprotocoldetail ADD COLUMN IF NOT EXISTS lockedusername character varying(255);

ALTER TABLE IF Exists materialtype ADD Column IF NOT EXISTS sampletype INTEGER default 1;

INSERT INTO materialtype (smaterialtypename, sampletype, nstatus, ndefaultstatus, jsondata, createdate, createby_usercode, expvalidation, quarvalidation) SELECT 'Samples', 1, 1, 4, '{}', '2023-11-23 12:08:01.187496', 1, false, false WHERE NOT EXISTS (SELECT 1 FROM materialtype WHERE smaterialtypename = 'Samples');

INSERT INTO materialcategory (smaterialcatname, nuserrolecode, nstatus, nmaterialtypecode, nsitecode, ndefaultstatus, createdate, createby_usercode) SELECT 'Samples', -1, 1, mt.nmaterialtypecode, 1, 3, '2023-11-23 12:08:01.187496', 1 FROM materialtype mt WHERE mt.smaterialtypename = 'Samples' AND NOT EXISTS (SELECT 1 FROM materialcategory WHERE smaterialcatname = 'Samples' );

ALTER TABLE IF Exists LSprotocolorderversion ADD COLUMN IF NOT EXISTS fileuid varchar(250);
ALTER TABLE IF Exists LSprotocolorderversion ADD COLUMN IF NOT EXISTS fileuri varchar(500);

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'equipmenttype_sequence' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE equipmenttype_sequence;
   ELSIF _kind = 'S' THEN  
      -- do nothing?
   ELSE             
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.equipmenttype
(
    nequipmenttypecode integer DEFAULT nextval('equipmenttype_sequence'::regclass),
    jsondata jsonb,
    ndefaultstatus integer NOT NULL DEFAULT 4,
    nsitecode integer NOT NULL DEFAULT '-1'::integer,
    nstatus integer NOT NULL DEFAULT 1,
    sequipmenttypename character varying(100) COLLATE pg_catalog."default",
    createby_usercode integer DEFAULT 1,
    createdate timestamp without time zone DEFAULT now(),
    CONSTRAINT equipmenttype_pkey PRIMARY KEY (nequipmenttypecode)
)
WITH (OIDS = FALSE) TABLESPACE pg_default;

ALTER TABLE public.equipmenttype OWNER to postgres;

ALTER TABLE IF Exists equipmenttype ADD COLUMN IF NOT EXISTS sequipmenttypename character varying(100);

INSERT INTO equipmenttype (sequipmenttypename, nstatus, ndefaultstatus, jsondata, createdate, createby_usercode) SELECT 'Equipments', 1, 4, '{}', '2023-11-23 12:08:01.187496', 1 WHERE NOT EXISTS (SELECT 1 FROM equipmenttype WHERE sequipmenttypename = 'Equipments');

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'equipmentcategory_nequipmentcatcode_seq' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE equipmentcategory_nequipmentcatcode_seq;
   ELSIF _kind = 'S' THEN  
      -- do nothing?
   ELSE             
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.equipmentcategory
(
    nequipmentcatcode integer NOT NULL DEFAULT nextval('equipmentcategory_nequipmentcatcode_seq'::regclass),
    equipmenttype_nequipmenttypecode integer NOT NULL,
    ndefaultstatus integer NOT NULL DEFAULT 4,
    nsitecode integer NOT NULL DEFAULT '-1'::integer,
    nstatus integer NOT NULL DEFAULT 1,
    sdescription character varying(255) COLLATE pg_catalog."default",
    sequipmentcatname character varying(100) COLLATE pg_catalog."default" NOT NULL,
    createby_usercode integer DEFAULT 1,
    createdate timestamp without time zone DEFAULT now(),
    CONSTRAINT equipmentcategory_pkey PRIMARY KEY (nequipmentcatcode)
)
WITH (OIDS = FALSE) TABLESPACE pg_default;

ALTER TABLE public.equipmentcategory OWNER to postgres;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'equipment_sequence' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE equipment_sequence;
   ELSIF _kind = 'S' THEN  
      -- do nothing?
   ELSE             
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.equipment
(
    nequipmentcode integer NOT NULL DEFAULT nextval('equipment_sequence'::regclass),
    equipmentcategory_nequipmentcatcode integer NOT NULL,
    equipmenttype_nequipmenttypecode integer NOT NULL,
    ntransactionstatus integer NOT NULL,
    sequipmentname character varying(100) COLLATE pg_catalog."default" NOT NULL,
    jsondata jsonb NOT NULL,
    nstatus integer NOT NULL DEFAULT 1,
    nsitecode integer,
    createddate timestamp without time zone DEFAULT ('now'::text)::date,
    createby_usercode integer DEFAULT 1,
    CONSTRAINT equipment_pkey PRIMARY KEY (nequipmentcode)
)
WITH (OIDS = FALSE) TABLESPACE pg_default;

ALTER TABLE public.equipment OWNER to postgres;

ALTER TABLE equipment DROP COLUMN IF Exists jsonuidata;

ALTER TABLE IF Exists equipment ADD COLUMN IF NOT EXISTS manintanancedate timestamp without time zone,ADD COLUMN IF NOT EXISTS callibrationdate timestamp without time zone,ADD COLUMN IF NOT EXISTS sequipmentid character varying(100);

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'lsprotocolmethod_sequence' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE lsprotocolmethod_sequence;
   ELSIF _kind = 'S' THEN  
      -- do nothing?
   ELSE             
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.lsprotocolmethod
(
    protocolmethodcode integer NOT NULL,
    instrumentid character varying(120) COLLATE pg_catalog."default",
    methodid character varying(120) COLLATE pg_catalog."default",
    protocolmastercode integer,
    sectioncode integer,
    stepcode integer,
    CONSTRAINT lsprotocolmethod_pkey PRIMARY KEY (protocolmethodcode),
    CONSTRAINT fkc4nmo3njuakiypx2ohlt8xerk FOREIGN KEY (protocolmastercode)
        REFERENCES public.lsprotocolmaster (protocolmastercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.lsprotocolmethod OWNER to postgres;
    
delete from period where speriodname in ('Never','NA','Minutes','Day Without Hours');

CREATE TABLE IF NOT EXISTS public.elnprotocolworkflow
(
    workflowcode integer NOT NULL,
    workflowname character varying(120) COLLATE pg_catalog."default",
    lssitemaster_sitecode integer,
    CONSTRAINT elnprotocolworkflow_pkey PRIMARY KEY (workflowcode),
    CONSTRAINT fkod7wbg5h1ih4pevt3oxk6be1f FOREIGN KEY (lssitemaster_sitecode)
        REFERENCES public.lssitemaster (sitecode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.elnprotocolworkflow
    OWNER to postgres;

    CREATE TABLE IF NOT EXISTS public.elnprotocolworkflowgroupmap
(
    workflowmapid integer NOT NULL,
    workflowcode integer,
    lsusergroup_usergroupcode integer,
    CONSTRAINT elnprotocolworkflowgroupmap_pkey PRIMARY KEY (workflowmapid),
    CONSTRAINT fkanrjwn5d7j6ruu3v5ri6uxltn FOREIGN KEY (workflowcode)
        REFERENCES public.elnprotocolworkflow (workflowcode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fktbt930h7jlk9ojp6h6ygjhklh FOREIGN KEY (lsusergroup_usergroupcode)
        REFERENCES public.lsusergroup (usergroupcode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.elnprotocolworkflowgroupmap
    OWNER to postgres;
    
DO $$
DECLARE
  Elnprotocolworkflow INTEGER := 0;
  LSworkflow INTEGER := 0;
  hasElnprotocolworkflowWorkflowcode BOOLEAN := false;
BEGIN
  SELECT COUNT(*) INTO Elnprotocolworkflow FROM Elnprotocolworkflow;
  SELECT COUNT(*) INTO LSworkflow FROM LSworkflow;
  IF Elnprotocolworkflow = 0 AND LSworkflow != 0 THEN
    INSERT INTO Elnprotocolworkflow
    SELECT * FROM LSworkflow;
  END IF;
END
$$;


DO $$
DECLARE
  elnprotocolworkflowgroupmap INTEGER := 0;
  LSworkflowgroupmapping INTEGER := 0;
  Elnprotocolworkflow INTEGER := 0;
BEGIN
  SELECT COUNT(*) INTO elnprotocolworkflowgroupmap FROM elnprotocolworkflowgroupmap;
  SELECT COUNT(*) INTO LSworkflowgroupmapping FROM LSworkflowgroupmapping;
  SELECT COUNT(*) INTO Elnprotocolworkflow FROM Elnprotocolworkflow;

  IF elnprotocolworkflowgroupmap = 0 AND LSworkflowgroupmapping != 0 AND Elnprotocolworkflow != 0 THEN
    INSERT INTO elnprotocolworkflowgroupmap
    SELECT * FROM LSworkflowgroupmapping 
    WHERE workflowcode IS NOT NULL AND lsusergroup_usergroupcode IS NOT NULL;
  END IF;
END
$$;


ALTER TABLE IF Exists lslogilabprotocoldetail ADD COLUMN IF NOT EXISTS elnprotocolworkflow_workflowcode integer;

DO
$do$
declare
  multiusergroupcount integer :=0;
begin
SELECT count(*) into multiusergroupcount FROM
information_schema.table_constraints WHERE constraint_name='fk15axj242rehy7vaftbtr51fv2'
AND table_name='lslogilabprotocoldetail';
 IF multiusergroupcount =0 THEN
 	ALTER TABLE ONLY lslogilabprotocoldetail ADD CONSTRAINT fk15axj242rehy7vaftbtr51fv2 FOREIGN KEY (elnprotocolworkflow_workflowcode) REFERENCES elnprotocolworkflow(workflowcode);
   END IF;
END
$do$; 

DO $$
DECLARE
  Elnprotocolworkflow INTEGER := 0;
BEGIN
 SELECT COUNT(*) INTO Elnprotocolworkflow FROM lslogilabprotocoldetail where elnprotocolworkflow_workflowcode is not null;
  IF Elnprotocolworkflow = 0 THEN
  UPDATE lslogilabprotocoldetail
SET elnprotocolworkflow_workflowcode =lsworkflow_workflowcode ;
  END IF;
END
$$;

ALTER TABLE IF Exists lsprotocolorderworkflowhistory ADD COLUMN IF NOT EXISTS elnprotocolworkflow_workflowcode integer;

DO
$do$
declare
  multiusergroupcount integer :=0;
begin
SELECT count(*) into multiusergroupcount FROM
information_schema.table_constraints WHERE constraint_name='fk3sk2cb8s314107cojt1wmtjyn'
AND table_name='lsprotocolorderworkflowhistory';
 IF multiusergroupcount =0 THEN
 	ALTER TABLE ONLY lsprotocolorderworkflowhistory ADD CONSTRAINT fk3sk2cb8s314107cojt1wmtjyn FOREIGN KEY (elnprotocolworkflow_workflowcode) REFERENCES elnprotocolworkflow(workflowcode);
   END IF;
END
$do$; 

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'elnprotocoltemplateworkflow_workflowcode_seq' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE elnprotocoltemplateworkflow_workflowcode_seq;
   ELSIF _kind = 'S' THEN  
     
   ELSE                  
    
   END IF;
END
$do$;

DO $$
DECLARE
  Elnprotocolworkflow INTEGER := 0;
BEGIN
 SELECT COUNT(*) INTO Elnprotocolworkflow FROM LSprotocolorderworkflowhistory where elnprotocolworkflow_workflowcode is not null;
  IF Elnprotocolworkflow = 0 THEN
  UPDATE LSprotocolorderworkflowhistory
SET elnprotocolworkflow_workflowcode =lsworkflow_workflowcode ;
  END IF;
END
$$;

CREATE TABLE IF NOT EXISTS public.elnprotocoltemplateworkflow
(
    workflowcode integer NOT NULL DEFAULT nextval('elnprotocoltemplateworkflow_workflowcode_seq'::regclass),
    status integer,
    workflowname character varying(120) COLLATE pg_catalog."default",
    lssitemaster_sitecode integer,
    CONSTRAINT elnprotocoltemplateworkflow_pkey PRIMARY KEY (workflowcode),
    CONSTRAINT fk86xurw8smb7jbeba3bqrrmwcp FOREIGN KEY (lssitemaster_sitecode)
        REFERENCES public.lssitemaster (sitecode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.elnprotocoltemplateworkflow
    OWNER to postgres;



CREATE TABLE IF NOT EXISTS public.elnprotocoltemplateworkflowgroupmap
(
    workflowmapid integer NOT NULL,
    workflowcode integer,
    lsusergroup_usergroupcode integer,
    CONSTRAINT elnprotocoltemplateworkflowgroupmap_pkey PRIMARY KEY (workflowmapid),
    CONSTRAINT fk7sgh5qbb0ylgf9ahb51j9w5cp FOREIGN KEY (workflowcode)
        REFERENCES public.elnprotocoltemplateworkflow (workflowcode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkn421ud3sx9u0qf8ccpy8rjivd FOREIGN KEY (lsusergroup_usergroupcode)
        REFERENCES public.lsusergroup (usergroupcode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.elnprotocoltemplateworkflowgroupmap
    OWNER to postgres;
    
 
DO $$
DECLARE
  ElnprotocolTemplateworkflow INTEGER := 0;
  LSsheetworkflow INTEGER := 0;
BEGIN
  SELECT COUNT(*) INTO ElnprotocolTemplateworkflow FROM ElnprotocolTemplateworkflow;
  SELECT COUNT(*) INTO LSsheetworkflow FROM LSsheetworkflow;
  IF ElnprotocolTemplateworkflow = 0 AND LSsheetworkflow != 0 THEN
   INSERT INTO ElnprotocolTemplateworkflow
      SELECT workflowcode,status,workflowname,lssitemaster_sitecode FROM LSsheetworkflow;
  END IF;
END
$$;


DO $$
DECLARE
  ElnprotocolTemplateworkflowgroupmap INTEGER := 0;
  LSsheetworkflowgroupmap INTEGER := 0;
  ElnprotocolTemplateworkflow INTEGER := 0;
BEGIN
  SELECT COUNT(*) INTO ElnprotocolTemplateworkflowgroupmap FROM ElnprotocolTemplateworkflowgroupmap;
  SELECT COUNT(*) INTO LSsheetworkflowgroupmap FROM LSsheetworkflowgroupmap;
   SELECT COUNT(*) INTO ElnprotocolTemplateworkflow FROM ElnprotocolTemplateworkflow;
  IF ElnprotocolTemplateworkflowgroupmap = 0 AND LSsheetworkflowgroupmap != 0 And ElnprotocolTemplateworkflow !=0 THEN
   INSERT INTO ElnprotocolTemplateworkflowgroupmap
      SELECT * FROM LSsheetworkflowgroupmap  where workflowcode is not null and lsusergroup_usergroupcode is not null;
  END IF;
END
$$;

ALTER TABLE IF Exists lsprotocolworkflowhistory ADD COLUMN IF NOT EXISTS elnprotocoltemplateworkflow_workflowcode integer;

DO
$do$
declare
  multiusergroupcount integer :=0;
begin
SELECT count(*) into multiusergroupcount FROM
information_schema.table_constraints WHERE constraint_name='fk1o5n7rogxv9097hxylr6pi7ko'
AND table_name='lsprotocolworkflowhistory';
 IF multiusergroupcount =0 THEN
 	ALTER TABLE ONLY lsprotocolworkflowhistory ADD CONSTRAINT fk1o5n7rogxv9097hxylr6pi7ko FOREIGN KEY (elnprotocoltemplateworkflow_workflowcode) REFERENCES elnprotocoltemplateworkflow(workflowcode);
   END IF;
END
$do$; 


DO $$
DECLARE
  Elnprotocolworkflow INTEGER := 0;
BEGIN
 SELECT COUNT(*) INTO Elnprotocolworkflow FROM lsprotocolworkflowhistory where elnprotocoltemplateworkflow_workflowcode is not null;
  IF Elnprotocolworkflow = 0 THEN
  UPDATE lsprotocolworkflowhistory
SET elnprotocoltemplateworkflow_workflowcode =lssheetworkflow_workflowcode ;
  END IF;
END
$$;

ALTER TABLE IF Exists lsprotocolmaster ADD COLUMN IF NOT EXISTS elnprotocoltemplateworkflow_workflowcode integer;

DO
$do$
declare
  multiusergroupcount integer :=0;
begin
SELECT count(*) into multiusergroupcount FROM
information_schema.table_constraints WHERE constraint_name='fkamn7qwghoiisbp5ci4axbvwt6'
AND table_name='lsprotocolmaster';
 IF multiusergroupcount =0 THEN
 	ALTER TABLE ONLY lsprotocolmaster ADD CONSTRAINT fkamn7qwghoiisbp5ci4axbvwt6 FOREIGN KEY (elnprotocoltemplateworkflow_workflowcode) REFERENCES elnprotocoltemplateworkflow(workflowcode);
   END IF;
END
$do$; 

DO $$
DECLARE
  Elnprotocolworkflow INTEGER := 0;
BEGIN
 SELECT COUNT(*) INTO Elnprotocolworkflow FROM lsprotocolmaster where elnprotocoltemplateworkflow_workflowcode is not null;
  IF Elnprotocolworkflow = 0 THEN
  UPDATE lsprotocolmaster
SET elnprotocoltemplateworkflow_workflowcode =lssheetworkflow_workflowcode ;
  END IF;
END
$$;

update LSpreferences set valueencrypted ='FrPnPlV4QlIH23zy6DkkeA==' where valueencrypted is null and serialno in (3,2);

ALTER TABLE IF Exists equipment ADD COLUMN IF NOT EXISTS sequipmentmake character varying(100), ADD COLUMN IF NOT EXISTS sequipmentmodel character varying(100), ADD COLUMN IF NOT EXISTS sequipmentlotno character varying(100);

ALTER TABLE IF Exists equipment ADD COLUMN IF NOT EXISTS remarks character varying(250);

ALTER TABLE IF Exists equipment ADD COLUMN IF NOT EXISTS remarks character varying(250);
ALTER TABLE IF Exists equipment ADD COLUMN IF NOT EXISTS callibrationvalue character varying(100);
ALTER TABLE IF Exists equipment ADD COLUMN IF NOT EXISTS callibrationperiod character varying(100);
ALTER TABLE IF Exists equipment ADD COLUMN IF NOT EXISTS manintanancevalue character varying(100);
ALTER TABLE IF Exists equipment ADD COLUMN IF NOT EXISTS maintananceperiod character varying(100);
ALTER TABLE IF Exists equipment ADD COLUMN IF NOT EXISTS equipmentused boolean;
ALTER TABLE IF Exists equipment ADD Column IF NOT EXISTS lastcallibrated timestamp without time zone;
ALTER TABLE IF Exists equipment ADD Column IF NOT EXISTS lastmaintained timestamp without time zone;
update equipment set equipmentused = false where equipmentused is null;

INSERT into LSfields (fieldcode, createby, createdate, fieldorderno, fieldtypecode, isactive, level01code, level01name, level02code, level02name, level03code, level03name, level04code, level04name, siteID) VALUES (62, NULL, NULL, 22, 3, 1, 'G1', 'ID_GENERAL', '22', 'ID_GENERAL', 22, 'ID_GENERAL', 'G23', 'Add Equipment', 1) on conflict (fieldcode) do nothing;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'elnresultequipment_sequence' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE elnresultequipment_sequence;
   ELSIF _kind = 'S' THEN  
     
   ELSE                  
    
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.elnresultequipment
(
    nresultequipmentcode integer NOT NULL DEFAULT nextval('elnresultequipment_sequence'::regclass),
    ordercode numeric(17,0),
    transactionscreen integer,
    templatecode integer,
    nequipmenttypecode integer,
    nequipmentcatcode integer,
    nequipmentcode integer,
    nstatus integer NOT NULL DEFAULT 1,
    createddate timestamp without time zone,
    batchid character varying(100) COLLATE pg_catalog."default",
    lstestmasterlocal_testcode integer,
    createdby_usercode integer,
    CONSTRAINT elnresultequipment_pkey PRIMARY KEY (nresultequipmentcode),
    CONSTRAINT fk5wxdhabcdewr5m0ejfbh6e47 FOREIGN KEY (createdby_usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk5wxdhghijkwr5m0ejabc6e47 FOREIGN KEY (lstestmasterlocal_testcode)
        REFERENCES public.lstestmasterlocal (testcode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fknok3att985drj28p3bwern888 FOREIGN KEY (nequipmentcode)
        REFERENCES public.equipment (nequipmentcode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
) WITH ( OIDS = FALSE ) TABLESPACE pg_default;

ALTER TABLE public.elnresultequipment OWNER to postgres;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'equipmenthistory_sequence' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE equipmenthistory_sequence;
   ELSIF _kind = 'S' THEN  
     
   ELSE                  
    
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.equipmenthistory
(
    nequipmenthistorycode integer NOT NULL DEFAULT nextval('equipmenthistory_sequence'::regclass),
    historytype integer,
    nequipmentcode integer,
    nstatus integer NOT NULL DEFAULT 1,
    createddate timestamp without time zone,
    createdby_usercode integer,
    lastcallibrated timestamp without time zone,
    lastmaintained timestamp without time zone,
    manintanancedate timestamp without time zone,
    callibrationdate timestamp without time zone,
    CONSTRAINT equipmenthistory_pkey PRIMARY KEY (nequipmenthistorycode),
    CONSTRAINT fk5wxdhabcdewr5m0ejfbh6e47 FOREIGN KEY (createdby_usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fknok3att985drj28p3bwern888 FOREIGN KEY (nequipmentcode)
        REFERENCES public.equipment (nequipmentcode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)WITH ( OIDS = FALSE ) TABLESPACE pg_default;

ALTER TABLE public.equipmenthistory OWNER to postgres;

ALTER TABLE IF Exists lslogilabprotocoldetail ADD COLUMN IF NOT EXISTS activeuser integer;

insert into LSpreferences (serialno,tasksettings) values(5,'samplesync') on conflict(serialno) do nothing;

ALTER TABLE IF Exists LSprojectmaster ADD COLUMN IF NOT EXISTS createdon character varying(255);

ALTER TABLE IF Exists elnmaterial ADD COLUMN IF NOT EXISTS barcodetype integer;

update elnmaterial set barcodetype = 1 where barcodetype is null;

Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(150,0,'IDS_MDL_INVENTORY',99,'IDS_SCN_MATERIALTYPE','IDS_TSK_SAVE') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(151,0,'IDS_MDL_INVENTORY',100,'IDS_SCN_MATERIALTYPE','IDS_TSK_EDIT') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(152,0,'IDS_MDL_INVENTORY',101,'IDS_SCN_MATERIALCATEGORY','IDS_TSK_SAVE') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(153,0,'IDS_MDL_INVENTORY',102,'IDS_SCN_MATERIALCATEGORY','IDS_TSK_EDIT') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(154,0,'IDS_MDL_INVENTORY',103,'IDS_SCN_MATERIALCATEGORY','IDS_TSK_RETIRE') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(155,0,'IDS_MDL_INVENTORY',104,'IDS_SCN_STORAGELOCATION','IDS_TSK_SAVE') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(156,0,'IDS_MDL_INVENTORY',105,'IDS_SCN_STORAGELOCATION','IDS_TSK_EDIT') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(157,0,'IDS_MDL_INVENTORY',106,'IDS_SCN_STORAGELOCATION','IDS_TSK_RETIRE') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(158,0,'IDS_MDL_INVENTORY',107,'IDS_SCN_UNITMASTER','IDS_TSK_SAVE') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(159,0,'IDS_MDL_INVENTORY',108,'IDS_SCN_UNITMASTER','IDS_TSK_EDIT') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(160,0,'IDS_MDL_INVENTORY',109,'IDS_SCN_UNITMASTER','IDS_TSK_RETIRE') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(161,0,'IDS_MDL_INVENTORY',110,'IDS_SCN_GRADEMASTER','IDS_TSK_SAVE') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(162,0,'IDS_MDL_INVENTORY',111,'IDS_SCN_GRADEMASTER','IDS_TSK_EDIT') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(163,0,'IDS_MDL_INVENTORY',112,'IDS_SCN_GRADEMASTER','IDS_TSK_RETIRE') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(164,0,'IDS_MDL_INVENTORY',113,'IDS_SCN_SUPPLIER','IDS_TSK_SAVE') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(165,0,'IDS_MDL_INVENTORY',114,'IDS_SCN_SUPPLIER','IDS_TSK_EDIT') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(166,0,'IDS_MDL_INVENTORY',115,'IDS_SCN_SUPPLIER','IDS_TSK_RETIRE') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(167,0,'IDS_MDL_INVENTORY',116,'IDS_SCN_MANUFACTURER','IDS_TSK_SAVE') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(168,0,'IDS_MDL_INVENTORY',117,'IDS_SCN_MANUFACTURER','IDS_TSK_EDIT') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(169,0,'IDS_MDL_INVENTORY',118,'IDS_SCN_MANUFACTURER','IDS_TSK_RETIRE') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(170,0,'IDS_MDL_INVENTORY',119,'IDS_SCN_SECTIONMASTER','IDS_TSK_SAVE') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(171,0,'IDS_MDL_INVENTORY',120,'IDS_SCN_SECTIONMASTER','IDS_TSK_EDIT') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(172,0,'IDS_MDL_INVENTORY',121,'IDS_SCN_SECTIONMASTER','IDS_TSK_RETIRE') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(173,0,'IDS_MDL_INVENTORY',122,'IDS_SCN_MATERIAL','IDS_TSK_SAVE') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(174,0,'IDS_MDL_INVENTORY',123,'IDS_SCN_MATERIAL','IDS_TSK_EDIT') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(175,0,'IDS_MDL_INVENTORY',124,'IDS_SCN_MATERIAL','IDS_TSK_RETIRE') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(176,0,'IDS_MDL_INVENTORY',125,'IDS_SCN_MATERIALINVENTORY','IDS_TSK_SAVE') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(177,0,'IDS_MDL_INVENTORY',126,'IDS_SCN_MATERIALINVENTORY','IDS_TSK_RESTOCK') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(178,0,'IDS_MDL_INVENTORY',127,'IDS_SCN_MATERIALINVENTORY','IDS_TSK_DISPOSE') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(179,0,'IDS_MDL_INVENTORY',128,'IDS_SCN_MATERIALINVENTORY','IDS_TSK_RELEASE') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(180,0,'IDS_MDL_INVENTORY',129,'IDS_SCN_MATERIALINVENTORY','IDS_TSK_OPENDATE') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(181,0,'IDS_MDL_INVENTORY',130,'IDS_SCN_EQUIPMENTTYPE','IDS_TSK_SAVE') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(182,0,'IDS_MDL_INVENTORY',131,'IDS_SCN_EQUIPMENTTYPE','IDS_TSK_EDIT') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(183,0,'IDS_MDL_INVENTORY',132,'IDS_SCN_EQUIPMENTCATEGORY','IDS_TSK_SAVE') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(184,0,'IDS_MDL_INVENTORY',133,'IDS_SCN_EQUIPMENTCATEGORY','IDS_TSK_EDIT') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(185,0,'IDS_MDL_INVENTORY',134,'IDS_SCN_EQUIPMENTCATEGORY','IDS_TSK_RETIRE') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(186,0,'IDS_MDL_INVENTORY',135,'IDS_SCN_EQUIPMENT','IDS_TSK_SAVE') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(187,0,'IDS_MDL_INVENTORY',136,'IDS_SCN_EQUIPMENT','IDS_TSK_EDIT') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(188,0,'IDS_MDL_INVENTORY',137,'IDS_SCN_EQUIPMENT','IDS_TSK_RETIRE') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(189,0,'IDS_MDL_INVENTORY',138,'IDS_SCN_EQUIPMENT','IDS_TSK_ACT/DCT') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(190,0,'IDS_MDL_INVENTORY',139,'IDS_SCN_EQUIPMENT','IDS_TSK_CALIBRATE') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(191,0,'IDS_MDL_INVENTORY',140,'IDS_SCN_EQUIPMENT','IDS_TSK_MAINTAINE') ON CONFLICT(serialno)DO NOTHING;

update lsaudittrailconfigmaster set ordersequnce = 145 where ordersequnce = 137 and screenname = 'IDS_SCN_LOGBOOK';
update lsaudittrailconfigmaster set ordersequnce = 146 where ordersequnce = 136 and screenname = 'IDS_SCN_LOGBOOK';
update lsaudittrailconfigmaster set ordersequnce = 147 where ordersequnce = 135 and screenname = 'IDS_SCN_LOGBOOK';
update lsaudittrailconfigmaster set ordersequnce = 148 where ordersequnce = 134 and screenname = 'IDS_SCN_LOGBOOK';
update lsaudittrailconfigmaster set ordersequnce = 149 where ordersequnce = 133 and screenname = 'IDS_SCN_LOGBOOK';
update lsaudittrailconfigmaster set ordersequnce = 150 where ordersequnce = 132 and screenname = 'IDS_SCN_LOGBOOK';
update lsaudittrailconfigmaster set ordersequnce = 151 where ordersequnce = 131 and screenname = 'IDS_SCN_LOGBOOK';