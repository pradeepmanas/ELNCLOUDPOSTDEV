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

ALTER TABLE IF Exists LSprotocolmaster ADD COLUMN IF NOT EXISTS fileuid varchar(100);
ALTER TABLE IF Exists LSprotocolmaster ADD COLUMN IF NOT EXISTS fileuri varchar(250);
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

ALTER TABLE IF Exists LSlogilabprotocoldetail ADD COLUMN IF NOT EXISTS fileuid varchar(100);
ALTER TABLE IF Exists LSlogilabprotocoldetail ADD COLUMN IF NOT EXISTS fileuri varchar(250);
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
