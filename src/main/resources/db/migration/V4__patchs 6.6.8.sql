
DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'lsprojectarchieve_projectarchievecode_seq'  
   -- sequence name, optionally schema-qualified
   INTO  _kind;

   IF NOT FOUND THEN       -- name is free
      CREATE SEQUENCE lsprojectarchieve_projectarchievecode_seq;
   ELSIF _kind = 'S' THEN  -- sequence exists
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.lsprojectarchieve
(
    projectarchievecode integer NOT NULL DEFAULT nextval('lsprojectarchieve_projectarchievecode_seq'::regclass),
    filenameuuid character varying(100) COLLATE pg_catalog."default",
    modifieddate timestamp without time zone,
    projectname character varying(100) COLLATE pg_catalog."default",
    archieveby_usercode integer,
    lssitemaster_sitecode integer,
    CONSTRAINT lsprojectarchieve_pkey PRIMARY KEY (projectarchievecode),
    CONSTRAINT fk99ywhbq3f2trkoli6fg6777e6 FOREIGN KEY (lssitemaster_sitecode)
        REFERENCES public.lssitemaster (sitecode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkk2v7dy4b1qd0v24yv3yqe3frk FOREIGN KEY (archieveby_usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.lsprojectarchieve
    OWNER to postgres;


DO
$do$
DECLARE
   counter integer := 0;
BEGIN
  select count(*) into counter from lsusergrouprightsmaster where sequenceorder =33 and modulename ='User Master';

   IF counter=0 THEN       -- name is free
INSERT into LSusergrouprightsmaster (orderno, displaytopic, modulename, sallow, screate, sdelete,sedit, status,sequenceorder)
VALUES (52, 'Retire', 'User Master', '0', 'NA', 'NA', 'NA', '0,0,0',33);
   END IF;
END
$do$;

do $$
declare 
   counter1 integer := 0;
     orderno1 integer :=0;
  sequenceorder1 integer :=0;
  checkv integer :=0;
  i integer :=0;
  samecode integer :=0;
  sampleorderno integer :=0;
begin

 select count(*) into samecode from lsusergrouprightsmaster where sequenceorder =33;
 select count(*)into checkv from lsusergrouprightsmaster where sequenceorder >= 33 and modulename !='User Master';
--       raise notice 'Counter %', samecode;
-- 	    raise notice 'Counter %', checkv;
   IF samecode =2 THEN		
 while i < checkv loop
 select orderno into orderno1 from lsusergrouprightsmaster where sequenceorder >=33 and modulename !='User Master' order by sequenceorder desc offset i  limit 1; 
 select sequenceorder into sequenceorder1 from lsusergrouprightsmaster where sequenceorder >=33 and modulename !='User Master' order by sequenceorder desc offset i  limit 1; 
   raise notice 'orderno1 %', orderno1;

raise notice 'Counter %', sequenceorder1;
 sequenceorder1 := sequenceorder1+1;
     update lsusergrouprightsmaster set sequenceorder=sequenceorder1 where orderno=orderno1;
raise notice 'Counter %', sequenceorder1;
 i := i + 1;
--   IF samecode =2 and sequenceorder1 =33 THEN	
--    select count(*) into samecode from lsusergrouprightsmaster where sequenceorder =33;
--   END IF;
  end loop;
    END IF;

end$$;


CREATE TABLE IF NOT EXISTS public.lsprotocolstepinformation
(
    id integer NOT NULL,
    lsprotocolstepinfo jsonb,
    CONSTRAINT lsprotocolstepinformation_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.lsprotocolstepinformation
    OWNER to postgres;
    
CREATE TABLE IF NOT EXISTS public.lsprotocolfiles
(
    protocolstepfilecode integer NOT NULL,
    extension character varying(255) COLLATE pg_catalog."default",
    fileid character varying(255) COLLATE pg_catalog."default",
    filename character varying(255) COLLATE pg_catalog."default",
    protocolmastercode integer,
    protocolstepcode integer,
    protocolstepname character varying(255) COLLATE pg_catalog."default",
    stepno integer,
    CONSTRAINT lsprotocolfiles_pkey PRIMARY KEY (protocolstepfilecode)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.lsprotocolfiles
    OWNER to postgres;
    
    
 CREATE TABLE IF NOT EXISTS public.lsprotocolimages
(
    protocolstepimagecode integer NOT NULL,
    extension character varying(255) COLLATE pg_catalog."default",
    fileid character varying(255) COLLATE pg_catalog."default",
    protocolmastercode integer,
    protocolstepcode integer,
    protocolstepname character varying(255) COLLATE pg_catalog."default",
    stepno integer,
    filename character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT lsprotocolimages_pkey PRIMARY KEY (protocolstepimagecode)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.lsprotocolimages
    OWNER to postgres;

ALTER TABLE IF Exists LSprotocolsampleupdates ADD COLUMN IF NOT EXISTS indexof integer;  

ALTER TABLE IF Exists LSprotocolsampleupdates ADD COLUMN IF NOT EXISTS usedquantity integer;    

ALTER TABLE IF Exists LSprotocolsampleupdates ADD COLUMN IF NOT EXISTS repositorydatacode integer;  

ALTER TABLE IF Exists LSprotocolsampleupdates ADD COLUMN IF NOT EXISTS status integer;  
    
ALTER TABLE IF Exists LSprotocolsampleupdates ADD COLUMN IF NOT EXISTS consumefieldkey varchar(250);

ALTER TABLE IF Exists LSlogilabprotocoldetail ADD COLUMN IF NOT EXISTS lsprotocolworkflow_workflowcode integer;

ALTER TABLE IF Exists LSlogilabprotocoldetail ADD COLUMN IF NOT EXISTS approved integer;

ALTER TABLE IF Exists LSlogilabprotocoldetail ADD COLUMN IF NOT EXISTS rejected integer;

CREATE TABLE IF NOT EXISTS public.lsusersignature
(
    id integer NOT NULL,
    image bytea,
    name character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT lsusersignature_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF Exists LSlogilabprotocoldetail ADD COLUMN IF NOT EXISTS sitecode integer;

ALTER TABLE IF Exists LSlogilabprotocoldetail ADD COLUMN IF NOT EXISTS assignedto_usercode integer;



CREATE TABLE IF NOT EXISTS public.lsprotocolorderimages
(
    protocolorderstepimagecode integer NOT NULL,
    extension character varying(255) COLLATE pg_catalog."default",
    fileid character varying(255) COLLATE pg_catalog."default",
    filename character varying(255) COLLATE pg_catalog."default",
  protocolordercode bigint,
    protocolorderstepcode integer,
    protocolstepname character varying(255) COLLATE pg_catalog."default",
    stepno integer,
    oldfileid character varying(255) COLLATE pg_catalog."default",
    src jsonb,
     oldsrc jsonb,
    CONSTRAINT lsprotocolorderimages_pkey PRIMARY KEY (protocolorderstepimagecode)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.lsprotocolorderimages
    OWNER to postgres;
    
    
    ALTER TABLE IF Exists LSprotocolimages ADD COLUMN IF NOT EXISTS  src jsonb;
	
	
	CREATE TABLE IF NOT EXISTS public.lsprotocolorderfiles
(
    protocolorderstepfilecode integer NOT NULL,
    extension character varying(255) COLLATE pg_catalog."default",
    fileid character varying(255) COLLATE pg_catalog."default",
    filename character varying(255) COLLATE pg_catalog."default",
    protocolordercode bigint,
    protocolorderstepcode integer,
    protocolstepname character varying(255) COLLATE pg_catalog."default",
    stepno integer,
    CONSTRAINT lsprotocolorderfiles_pkey PRIMARY KEY (protocolorderstepfilecode)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.lsprotocolorderfiles
    OWNER to postgres;
    
    ALTER TABLE IF Exists LSlogilabprotocoldetail ADD COLUMN IF NOT EXISTS assignedto_usercode integer;
    
    
    CREATE TABLE IF NOT EXISTS public.lsprotocolordershareto
(
    sharetoprotocolordercode bigint NOT NULL,
    protocoltype integer,
    sharebyunifiedid character varying(250) COLLATE pg_catalog."default",
    sharebyusername character varying(250) COLLATE pg_catalog."default",
    sharedon timestamp without time zone,
    shareitemdetails jsonb,
    shareprotoclordername character varying(250) COLLATE pg_catalog."default",
    shareprotocolordercode bigint,
    sharerights integer NOT NULL,
    sharestatus integer NOT NULL,
    sharetounifiedid character varying(250) COLLATE pg_catalog."default",
    sharetousername character varying(250) COLLATE pg_catalog."default",
    unsharedon timestamp without time zone,
    CONSTRAINT lsprotocolordershareto_pkey PRIMARY KEY (sharetoprotocolordercode)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.lsprotocolordershareto
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.lsprotocolordersharedby
(
    sharedbytoprotocolordercode bigint NOT NULL,
    protocoltype integer,
    sharebyunifiedid character varying(250) COLLATE pg_catalog."default",
    sharebyusername character varying(250) COLLATE pg_catalog."default",
    sharedon timestamp without time zone,
    shareitemdetails jsonb,
    sharemodifiedon timestamp without time zone,
    shareprotoclordername character varying(250) COLLATE pg_catalog."default",
    shareprotocolordercode bigint,
    sharerights integer NOT NULL,
    sharestatus integer NOT NULL,
    sharetoprotocolordercode bigint,
    sharetounifiedid character varying(250) COLLATE pg_catalog."default",
    sharetousername character varying(250) COLLATE pg_catalog."default",
    unsharedon timestamp without time zone,
    CONSTRAINT lsprotocolordersharedby_pkey PRIMARY KEY (sharedbytoprotocolordercode)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.lsprotocolordersharedby
    OWNER to postgres;

ALTER TABLE IF Exists lsprotocolstep ADD COLUMN IF NOT EXISTS modifiedusername varchar(250);

ALTER TABLE IF Exists LSlogilabprotocolsteps ADD COLUMN IF NOT EXISTS modifiedusername varchar(250);

 CREATE TABLE IF NOT EXISTS public.lsprotocolshareto
(
    sharetoprotocolcode bigint NOT NULL,
    sharebyunifiedid character varying(250) COLLATE pg_catalog."default",
    sharebyusername character varying(250) COLLATE pg_catalog."default",
    sharedon timestamp without time zone,
    shareitemdetails jsonb,
    shareprotocolname character varying(250) COLLATE pg_catalog."default",
    shareprotocolcode bigint,
    sharerights integer NOT NULL,
    sharestatus integer NOT NULL,
    sharetounifiedid character varying(250) COLLATE pg_catalog."default",
    sharetousername character varying(250) COLLATE pg_catalog."default",
    unsharedon timestamp without time zone,
    CONSTRAINT lsprotocolshareto_pkey PRIMARY KEY (sharetoprotocolcode)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.Lsprotocolshareto
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.lsprotocolsharedby
(
    sharedbytoprotocolcode bigint NOT NULL,
    sharebyunifiedid character varying(250) COLLATE pg_catalog."default",
    sharebyusername character varying(250) COLLATE pg_catalog."default",
    sharedon timestamp without time zone,
    shareitemdetails jsonb,
    sharemodifiedon timestamp without time zone,
    shareprotocolname character varying(250) COLLATE pg_catalog."default",
    shareprotocolcode bigint,
    sharerights integer NOT NULL,
    sharestatus integer NOT NULL,
    sharetoprotocolcode bigint,
    sharetounifiedid character varying(250) COLLATE pg_catalog."default",
    sharetousername character varying(250) COLLATE pg_catalog."default",
    unsharedon timestamp without time zone,
    CONSTRAINT lsprotocolsharedby_pkey PRIMARY KEY (sharedbytoprotocolcode)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.lsprotocolsharedby
    OWNER to postgres;

 CREATE TABLE IF NOT EXISTS public.lsfileshareto
(
    sharetofilecode bigint NOT NULL,
    sharebyunifiedid character varying(250) COLLATE pg_catalog."default",
    sharebyusername character varying(250) COLLATE pg_catalog."default",
    sharedon timestamp without time zone,
    shareitemdetails jsonb,
    sharefilename character varying(250) COLLATE pg_catalog."default",
    sharefilecode bigint,
    sharerights integer NOT NULL,
    sharestatus integer NOT NULL,
    sharetounifiedid character varying(250) COLLATE pg_catalog."default",
    sharetousername character varying(250) COLLATE pg_catalog."default",
    unsharedon timestamp without time zone,
    CONSTRAINT lsfileshareto_pkey PRIMARY KEY (sharetofilecode)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.lsfileshareto
    OWNER to postgres;
	
CREATE TABLE IF NOT EXISTS public.lsfilesharedby
(
    sharedbytofilecode bigint NOT NULL,
    sharebyunifiedid character varying(250) COLLATE pg_catalog."default",
    sharebyusername character varying(250) COLLATE pg_catalog."default",
    sharedon timestamp without time zone,
    shareitemdetails jsonb,
    sharemodifiedon timestamp without time zone,
    sharefilename character varying(250) COLLATE pg_catalog."default",
    sharefilecode bigint,
    sharerights integer NOT NULL,
    sharestatus integer NOT NULL,
    sharetofilecode bigint,
    sharetounifiedid character varying(250) COLLATE pg_catalog."default",
    sharetousername character varying(250) COLLATE pg_catalog."default",
    unsharedon timestamp without time zone,
    CONSTRAINT lsfilesharedby_pkey PRIMARY KEY (sharedbytofilecode)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.lsfilesharedby
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.LSprotocolmastertest
(
    protocoltestcode integer NOT NULL,
    protocolmastercode integer,
    testcode integer,
    testtype integer,
    CONSTRAINT LSprotocolmastertest_pkey PRIMARY KEY (protocoltestcode),
	CONSTRAINT fkpms1rp8lpcg8ol5obwqurjw35 FOREIGN KEY (protocolmastercode)
        REFERENCES public.LSprotocolmaster (protocolmastercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.LSprotocolmastertest
    OWNER to postgres;
    
    
    
ALTER TABLE IF Exists LSprotocolorderfiles ADD COLUMN IF NOT EXISTS oldfileid character varying(255);
    
ALTER TABLE IF Exists LSprotocolordersampleupdates ADD COLUMN IF NOT EXISTS indexof Integer;
     
ALTER TABLE IF Exists LSprotocolordersampleupdates ADD COLUMN IF NOT EXISTS status Integer;
      
ALTER TABLE IF Exists LSprotocolordersampleupdates ADD COLUMN IF NOT EXISTS consumefieldkey varchar(250);

ALTER TABLE IF Exists LSprotocolordersampleupdates ADD COLUMN IF NOT EXISTS usedquantity Integer;

ALTER TABLE IF Exists LSprotocolordersampleupdates ADD COLUMN IF NOT EXISTS repositorydatacode Integer;

ALTER TABLE IF Exists LSlogilabprotocoldetail ADD COLUMN IF NOT EXISTS createby Integer;

CREATE TABLE IF NOT EXISTS public.lsprotocolorderworkflowhistory
(
    historycode integer NOT NULL,
    action character varying(250) COLLATE pg_catalog."default",
    approvelstatus integer,
    comment character varying(250) COLLATE pg_catalog."default",
    createdate timestamp without time zone,
    protocolordercode integer,
    createby_usercode integer,
    currentworkflow_workflowcode integer,
    CONSTRAINT lsprotocolorderworkflowhistory_pkey PRIMARY KEY (historycode),
    CONSTRAINT fk5ypng9qblkh4kupixpjujvqqs FOREIGN KEY (createby_usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk900cvb1r5g22s8lq8jenwcmgy FOREIGN KEY (currentworkflow_workflowcode)
        REFERENCES public.lsprotocolworkflow (workflowcode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.lsprotocolorderworkflowhistory
    OWNER to postgres;
    
INSERT into LSusergrouprights (orderno,createdby, createdon, displaytopic, modifiedby, modifiedon, modulename, sallow, screate, sdelete, sedit, lssitemaster_sitecode, usergroupid_usergroupcode) VALUES (52,N'administrator', CAST(N'2020-02-21T14:50:55.727' AS date), 'Retire', NULL, NULL, 'User Master', '1', '1', 'NA', 'NA', 1, 1) ON CONFLICT(orderno)DO NOTHING;

CREATE TABLE IF NOT EXISTS public.lsprotocolvideos
(
    protocolstepvideoscode integer NOT NULL,
    extension character varying(255) COLLATE pg_catalog."default",
    fileid character varying(255) COLLATE pg_catalog."default",
    filename character varying(255) COLLATE pg_catalog."default",
    protocolmastercode integer,
    protocolstepcode integer,
    protocolstepname character varying(255) COLLATE pg_catalog."default",
    stepno integer,
    CONSTRAINT lsprotocolvideos_pkey PRIMARY KEY (protocolstepvideoscode)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.lsprotocolvideos
    OWNER to postgres;  
    
    
CREATE TABLE IF NOT EXISTS  public.lsprotocolordervideos
(
    protocolorderstepvideoscode integer NOT NULL,
    extension character varying(255) COLLATE pg_catalog."default",
    fileid character varying(255) COLLATE pg_catalog."default",
    filename character varying(255) COLLATE pg_catalog."default",
    protocolordercode bigint,
    protocolorderstepcode integer,
    protocolstepname character varying(255) COLLATE pg_catalog."default",
    stepno integer,
    CONSTRAINT lsprotocolordervideos_pkey PRIMARY KEY (protocolorderstepvideoscode)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.lsprotocolordervideos
    OWNER to postgres;  

	CREATE TABLE IF NOT EXISTS public.notification
(
    notificationid bigint NOT NULL,
    addedby character varying(255) COLLATE pg_catalog."default",
    addedon timestamp without time zone,
    cautiondate timestamp without time zone,
    currentdate timestamp without time zone,
    description character varying(255) COLLATE pg_catalog."default",
    duedate timestamp without time zone,
    intervals character varying(255) COLLATE pg_catalog."default",
    orderid character varying(255) COLLATE pg_catalog."default",
    status integer,
    usercode integer,
    CONSTRAINT notification_pkey PRIMARY KEY (notificationid)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.notification
    OWNER to postgres;

INSERT into LSfields (fieldcode, createby, createdate, fieldorderno, fieldtypecode, isactive, level01code, level01name, level02code, level02name, level03code, level03name, level04code, level04name, siteID) VALUES (58, NULL, NULL, 19, 3, 1, 'G1', 'ID_GENERAL', '19', 'ID_GENERAL', 19, 'ID_GENERAL', 'G19', 'Notification', 1) on conflict (fieldcode) do nothing;

 ALTER TABLE IF Exists notification alter column  orderid Type numeric(17,0)USING orderid::numeric(17,0);

ALTER TABLE IF Exists LSlogilabprotocoldetail ADD COLUMN IF NOT EXISTS versionno Integer;

CREATE TABLE IF NOT EXISTS public.lsprotocolorderstepversion
(
    protocolorderstepversioncode integer NOT NULL,
    approved integer,
    createdate timestamp without time zone,
    createdbyusername character varying(120) COLLATE pg_catalog."default",
    protocolmastercode integer,
    protocolordercode bigint,
    protocolorderstepcode integer,
    protocolstepname character varying(255) COLLATE pg_catalog."default",
    rejected integer,
    sharewithteam integer,
    status integer,
    stepno integer,
    versionname character varying(255) COLLATE pg_catalog."default",
    versionno integer,
    CONSTRAINT lsprotocolorderstepversion_pkey PRIMARY KEY (protocolorderstepversioncode)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.lsprotocolorderstepversion
    OWNER to postgres;
		
CREATE TABLE IF NOT EXISTS public.cloudlsprotocolorderversionstep
(
    idversioncode integer NOT NULL,
    lsprotocolstepinfo jsonb,
    protocolordercode bigint,
    protocolorderstepversioncode integer,
    status integer,
    versionname character varying(100) COLLATE pg_catalog."default",
    versionno integer,
    CONSTRAINT cloudlsprotocolorderversionstep_pkey PRIMARY KEY (idversioncode)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.cloudlsprotocolorderversionstep
    OWNER to postgres;
	

CREATE TABLE IF NOT EXISTS public.lsprotocolorderversion
(
    protocolorderversioncode integer NOT NULL,
    createdate timestamp without time zone,
    createdbyusername character varying(255) COLLATE pg_catalog."default",
    protocolordercode bigint,
    status integer,
    versionname character varying(255) COLLATE pg_catalog."default",
    versionno integer,
    CONSTRAINT lsprotocolorderversion_pkey PRIMARY KEY (protocolorderversioncode)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.lsprotocolorderversion
    OWNER to postgres;
    
  ALTER TABLE IF Exists lslogilabprotocolsteps ADD COLUMN IF NOT EXISTS skipdata Integer;  

  
DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'lsusergroupedcolumns_usergroupedcolcode_seq'  
   INTO  _kind;
   IF NOT FOUND THEN  
      CREATE SEQUENCE lsusergroupedcolumns_usergroupedcolcode_seq;
   ELSIF _kind = 'S' THEN 
   ELSE                  
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.lsusergroupedcolumns
(
    usergroupedcolcode integer NOT NULL DEFAULT nextval('lsusergroupedcolumns_usergroupedcolcode_seq'::regclass),
    gridcolumns text COLLATE pg_catalog."default",
    gridname character varying(100) COLLATE pg_catalog."default",
    sitecode integer,
    usercode integer,
    CONSTRAINT lsusergroupedcolumns_pkey PRIMARY KEY (usergroupedcolcode)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.lsusergroupedcolumns
    OWNER to postgres;
    
update lsusergrouprightsmaster set displaytopic ='Task Master' where displaytopic ='Test Master' and modulename = 'Base Master';

update lsusergrouprights set displaytopic ='Task Master' where displaytopic = 'Test Master' and modulename = 'Base Master';

ALTER TABLE IF Exists Lsrepositories ADD COLUMN IF NOT EXISTS unit varchar(250);
 
INSERT into LSusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder) VALUES (53, 'Parser', 'Parser', '0', 'NA', 'NA', 'NA', '1,0,0',55) ON CONFLICT(orderno)DO NOTHING;

INSERT into LSusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder) VALUES (54, 'Delimiters', 'Parser', '0', '0', '0', '0', '1,1,1',56) ON CONFLICT(orderno)DO NOTHING;
INSERT into LSusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder) VALUES (55, 'Method Delimiters', 'Parser', '0', '0', '0', '0', '1,1,1',57) ON CONFLICT(orderno)DO NOTHING;
INSERT into LSusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder) VALUES (56, 'Method Master', 'Parser', '0', '0', '0', '0', '1,1,1',58) ON CONFLICT(orderno)DO NOTHING;

update lsusergrouprightsmaster set displaytopic ='New Step' where orderno=50;
update lsusergrouprightsmaster set sallow='0',screate='0',sdelete='0',sedit='0',status='1,1,1' where orderno=51;

update lsusergrouprights set displaytopic ='New Step' where modulename='Protocol Templates' and displaytopic ='New';
update lsusergrouprights set sallow='0',screate='0',sdelete='0',sedit='0' where modulename='Protocol Templates' and displaytopic ='Protocol Templates';

INSERT into LSusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder) VALUES (57, 'Export to pdf', 'Protocol Templates', '0', '0', 'NA', 'NA', '1,0,0',53) ON CONFLICT(orderno)DO NOTHING;

update lsusergrouprightsmaster set displaytopic ='New Template' where displaytopic='Template Designing' and modulename='Reports';
update lsusergrouprightsmaster set displaytopic ='New Template' where displaytopic='Template Designing' and modulename='Reports';

INSERT into LSusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder) VALUES (58, 'New Document', 'Reports', '0', '0', 'NA', 'NA', '1,0,0',45) ON CONFLICT(orderno)DO NOTHING;

INSERT INTO parserignorechars(ignorechars)SELECT '--' WHERE NOT EXISTS (SELECT ignorechars FROM parserignorechars WHERE ignorechars = '--');

INSERT INTO parserignorechars(ignorechars)SELECT '↵↵' WHERE NOT EXISTS (SELECT ignorechars FROM parserignorechars WHERE ignorechars = '↵↵');

INSERT INTO parserignorechars(ignorechars)SELECT '♀' WHERE NOT EXISTS (SELECT ignorechars FROM parserignorechars WHERE ignorechars = '♀');

INSERT INTO parserignorechars(ignorechars)SELECT '↵' WHERE NOT EXISTS (SELECT ignorechars FROM parserignorechars WHERE ignorechars = '↵');

INSERT INTO delimiter (delimitername,actualdelimiter,status,usercode) SELECT 'None', 'None', 1, 1 WHERE NOT EXISTS (SELECT delimitername FROM delimiter WHERE delimitername = 'None');

INSERT INTO delimiter (delimitername,actualdelimiter,status,usercode) SELECT 'Result without space', '[\s]+', 1, 1 WHERE NOT EXISTS (SELECT delimitername FROM delimiter WHERE delimitername = 'Result without space');

INSERT INTO delimiter (delimitername,actualdelimiter,status,usercode) SELECT 'Result with space', '\s\s+', 1, 1 WHERE NOT EXISTS (SELECT delimitername FROM delimiter WHERE delimitername = 'Result with space');

INSERT INTO delimiter (delimitername,actualdelimiter,status,usercode) SELECT 'Colon', ':', 1, 1 WHERE NOT EXISTS (SELECT delimitername FROM delimiter WHERE delimitername = 'Colon');

INSERT INTO delimiter (delimitername,actualdelimiter,status,usercode) SELECT 'Comma', ',', 1, 1 WHERE NOT EXISTS (SELECT delimitername FROM delimiter WHERE delimitername = 'Comma');

INSERT INTO delimiter (delimitername,actualdelimiter,status,usercode) SELECT 'Space', ' ', 1, 1 WHERE NOT EXISTS (SELECT delimitername FROM delimiter WHERE delimitername = 'Space');

INSERT INTO delimiter (delimitername,actualdelimiter,status,usercode) SELECT 'Split Dot', '[.]', 1, 1 WHERE NOT EXISTS (SELECT delimitername FROM delimiter WHERE delimitername = 'Split Dot');

INSERT INTO delimiter (delimitername,actualdelimiter,status,usercode) SELECT 'Merge Dot', '.', 1, 1 WHERE NOT EXISTS (SELECT delimitername FROM delimiter WHERE delimitername = 'Merge Dot');

INSERT INTO delimiter (delimitername,actualdelimiter,status,usercode) SELECT 'Slash', '/', 1, 1 WHERE NOT EXISTS (SELECT delimitername FROM delimiter WHERE delimitername = 'Slash');

update LSusergrouprightsmaster set status='0,0,0' where orderno=53;

update LSusergrouprightsmaster set sedit='NA' where orderno in (54,55,56);

INSERT into LSusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder)VALUES (59, 'Assigned Orders', 'Register Task Orders & Execute', '0', 'NA', 'NA', 'NA', '1,0,0',62) ON CONFLICT(orderno)DO NOTHING;

INSERT into LSusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder) VALUES (60, 'My Orders', 'Register Task Orders & Execute', '0', 'NA', 'NA', 'NA', '1,0,0',63) ON CONFLICT(orderno)DO NOTHING;

INSERT into LSusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder) VALUES (61, 'Orders Shared By Me', 'Register Task Orders & Execute', '0', 'NA', 'NA', 'NA', '1,0,0',64) ON CONFLICT(orderno)DO NOTHING;

INSERT into LSusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder) VALUES (62, 'Orders Shared To Me', 'Register Task Orders & Execute', '0', 'NA', 'NA', 'NA', '1,0,0',65)ON CONFLICT(orderno)DO NOTHING;

INSERT into LSusergrouprights(displaytopic,modulename,createdby, createdon, sallow, screate, sdelete, sedit, lssitemaster_sitecode, usergroupid_usergroupcode) SELECT 'Assigned Orders', 'Register Task Orders & Execute', 'administrator', CAST('2020-02-21T14:50:55.727' AS date), '1', 'NA', 'NA', 'NA', 1,1 WHERE NOT EXISTS (SELECT displaytopic FROM LSusergrouprights WHERE displaytopic = 'Assigned Orders' and modulename = 'Register Task Orders & Execute' and usergroupid_usergroupcode = 1);

INSERT into LSusergrouprights(displaytopic,modulename,createdby, createdon, sallow, screate, sdelete, sedit, lssitemaster_sitecode, usergroupid_usergroupcode) SELECT 'My Orders', 'Register Task Orders & Execute', 'administrator', CAST('2020-02-21T14:50:55.727' AS date), '1', 'NA', 'NA', 'NA', 1,1 WHERE NOT EXISTS (SELECT displaytopic FROM LSusergrouprights WHERE displaytopic = 'My Orders' and modulename = 'Register Task Orders & Execute' and usergroupid_usergroupcode = 1);

INSERT into LSusergrouprights(displaytopic,modulename,createdby, createdon, sallow, screate, sdelete, sedit, lssitemaster_sitecode, usergroupid_usergroupcode) SELECT 'Orders Shared By Me', 'Register Task Orders & Execute', 'administrator', CAST('2020-02-21T14:50:55.727' AS date), '1', 'NA', 'NA', 'NA', 1,1 WHERE NOT EXISTS (SELECT displaytopic FROM LSusergrouprights WHERE displaytopic = 'Orders Shared By Me' and modulename = 'Register Task Orders & Execute' and usergroupid_usergroupcode = 1);

INSERT into LSusergrouprights(displaytopic,modulename,createdby, createdon, sallow, screate, sdelete, sedit, lssitemaster_sitecode, usergroupid_usergroupcode) SELECT 'Orders Shared To Me', 'Register Task Orders & Execute', 'administrator', CAST('2020-02-21T14:50:55.727' AS date), '1', 'NA', 'NA', 'NA', 1,1 WHERE NOT EXISTS (SELECT displaytopic FROM LSusergrouprights WHERE displaytopic = 'Orders Shared To Me' and modulename = 'Register Task Orders & Execute' and usergroupid_usergroupcode = 1);

INSERT into LSusergrouprights(displaytopic,modulename,createdby, createdon, sallow, screate, sdelete, sedit, lssitemaster_sitecode, usergroupid_usergroupcode) select 'Pending Work', 'Register Task Orders & Execute', 'administrator', CAST('2020-02-21T14:50:55.727' AS date), '1', '1', 'NA', 'NA', 1,1 WHERE NOT EXISTS (SELECT displaytopic FROM LSusergrouprights WHERE displaytopic = 'Pending Work' and modulename = 'Register Task Orders & Execute' and usergroupid_usergroupcode = 1);

INSERT into LSusergrouprights(displaytopic,modulename,createdby, createdon, sallow, screate, sdelete, sedit, lssitemaster_sitecode, usergroupid_usergroupcode) select 'Completed Work', 'Register Task Orders & Execute', 'administrator', CAST('2020-02-21T14:50:55.727' AS date), '1', '1', 'NA', 'NA', 1,1 WHERE NOT EXISTS (SELECT displaytopic FROM LSusergrouprights WHERE displaytopic = 'Completed Work' and modulename = 'Register Task Orders & Execute' and usergroupid_usergroupcode = 1);

INSERT into LSusergrouprights(displaytopic,modulename,createdby, createdon, sallow, screate, sdelete, sedit, lssitemaster_sitecode, usergroupid_usergroupcode) select 'Sheet Evaluation', 'Register Task Orders & Execute', 'administrator', CAST('2020-02-21T14:50:55.727' AS date), '1', '1', 'NA', 'NA', 1,1 WHERE NOT EXISTS (SELECT displaytopic FROM LSusergrouprights WHERE displaytopic = 'Sheet Evaluation' and modulename = 'Register Task Orders & Execute' and usergroupid_usergroupcode = 1);

INSERT into LSusergrouprights(displaytopic,modulename,createdby, createdon, sallow, screate, sdelete, sedit, lssitemaster_sitecode, usergroupid_usergroupcode) select 'ELN Task Order', 'Register Task Orders & Execute', 'administrator', CAST('2020-02-21T14:50:55.727' AS date), '1', '1', 'NA', 'NA', 1,1 WHERE NOT EXISTS (SELECT displaytopic FROM LSusergrouprights WHERE displaytopic = 'ELN Task Order' and modulename = 'Register Task Orders & Execute' and usergroupid_usergroupcode = 1);

INSERT into LSusergrouprights(displaytopic,modulename,createdby, createdon, sallow, screate, sdelete, sedit, lssitemaster_sitecode, usergroupid_usergroupcode) select 'Research Activity Order', 'Register Task Orders & Execute', 'administrator', CAST('2020-02-21T14:50:55.727' AS date), '1', '1', 'NA', 'NA', 1,1 WHERE NOT EXISTS (SELECT displaytopic FROM LSusergrouprights WHERE displaytopic = 'Research Activity Order' and modulename = 'Register Task Orders & Execute' and usergroupid_usergroupcode = 1);

INSERT into LSusergrouprights(displaytopic,modulename,createdby, createdon, sallow, screate, sdelete, sedit, lssitemaster_sitecode, usergroupid_usergroupcode) SELECT 'Site Master','UserManagement','administrator', CAST('2022-01-21 00:00:00.000' AS date),'1','1','1','1',1,1 WHERE NOT EXISTS (SELECT displaytopic FROM LSusergrouprights WHERE displaytopic = 'Site Master' and modulename = 'UserManagement' and usergroupid_usergroupcode = 1);