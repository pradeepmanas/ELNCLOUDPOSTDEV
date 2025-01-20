ALTER TABLE IF Exists elnmaterialinventory ADD Column IF NOT EXISTS inventoryname character varying(255);

ALTER TABLE IF EXISTS RCTCPResultDetails ADD COLUMN IF NOT EXISTS valueloaded INTEGER DEFAULT 0;

ALTER TABLE IF Exists rctcpresultdetails ADD COLUMN IF NOT EXISTS filecode Integer;
    
DO
$do$
declare
  multiusergroupcount integer :=0;
begin
SELECT count(*) into multiusergroupcount FROM
information_schema.table_constraints WHERE constraint_name='fk4lfwyaj6ouryj2dk87wn0ij37'
AND table_name='rctcpresultdetails';
 IF multiusergroupcount =0 THEN
    ALTER TABLE ONLY rctcpresultdetails ADD CONSTRAINT fk4lfwyaj6ouryj2dk87wn0ij37 FOREIGN KEY (filecode) REFERENCES rctcpfiledetails(filecode);
   END IF;
END
$do$; 

ALTER TABLE IF Exists lsprojectmaster ADD Column IF NOT EXISTS duedate character varying(255);
ALTER TABLE IF Exists lsprojectmaster ADD Column IF NOT EXISTS startdate timestamp without time zone;
ALTER TABLE IF Exists lsprojectmaster ADD Column IF NOT EXISTS enddate timestamp without time zone;

DELETE FROM datatype WHERE datatypekey = 2 AND EXISTS ( SELECT 1 FROM datatype WHERE datatypekey = 2);

DO
$do$
BEGIN
    IF NOT EXISTS (
        SELECT 1 
        FROM datatype 
        WHERE datatypekey = 1 AND datatypename = 'string'
    ) THEN
        UPDATE datatype 
        SET datatypename = 'string' 
        WHERE datatypekey = 1;
    END IF;
END
$do$;

DO
$do$
BEGIN
    -- Check if datatypekey = 3 exists
    IF EXISTS (
        SELECT 1 
        FROM datatype 
        WHERE datatypekey = 3
    ) THEN
        -- Check if datatypename is NOT 'Number'
        IF EXISTS (
            SELECT 1 
            FROM datatype 
            WHERE datatypekey = 3 AND datatypename <> 'Number'
        ) THEN
            -- Update datatypename to 'Number'
            UPDATE datatype 
            SET datatypename = 'Number' 
            WHERE datatypekey = 3;
        END IF;
    ELSE
        -- Insert a new record with datatypekey = 3 and datatypename = 'Number'
        INSERT INTO datatype (datatypekey, datatypename)
        VALUES (3, 'Number');
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
   WHERE  relname = 'elnmaterialchemdiagref_diagramcode_seq' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE elnmaterialchemdiagref_diagramcode_seq;
   ELSIF _kind = 'S' THEN  
     
   ELSE                  
    
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.elnmaterialchemdiagref
(
    diagramcode bigint NOT NULL DEFAULT nextval('elnmaterialchemdiagref_diagramcode_seq'::regclass),
    createdate timestamp without time zone,
    fileid character varying(250) COLLATE pg_catalog."default",
    createby_usercode integer,
    nmaterialcode integer,
    smiles TEXT,
    moljson TEXT,
    CONSTRAINT elnmaterialchemdiagref_pkey PRIMARY KEY (diagramcode),
    CONSTRAINT fk701k777d2da33pkkl6lsathis FOREIGN KEY (nmaterialcode)
        REFERENCES public.elnmaterial (nmaterialcode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk9m36tjv4e767440yabcsup6ab FOREIGN KEY (createby_usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (OIDS = FALSE)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.elnmaterialchemdiagref OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.materialattachments
(
    nmaterialattachcode integer NOT NULL,
    createddate timestamp without time zone,
    fileextension character varying(10) COLLATE pg_catalog."default",
    fileid character varying(250) COLLATE pg_catalog."default",
    filename character varying(250) COLLATE pg_catalog."default",
    nmaterialcatcode integer,
    nmaterialcode integer,
    nmaterialtypecode integer,
    nsitecode integer,
    nstatus integer,
    createby_usercode integer,
    CONSTRAINT materialattachments_pkey PRIMARY KEY (nmaterialattachcode),
    CONSTRAINT fk133yuho3utecv6hl5rkkqrnlt FOREIGN KEY (nmaterialcode)
        REFERENCES public.elnmaterial (nmaterialcode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkmctfht2luiya3aem7u8lp0xo9 FOREIGN KEY (createby_usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.materialattachments
    OWNER to postgres;

ALTER TABLE IF EXISTS public.elnmaterialchemdiagref OWNER to postgres;
ALTER TABLE IF EXISTS public.elnmaterialchemdiagref OWNER to postgres;

ALTER TABLE IF EXISTS elnmaterialchemdiagref ADD COLUMN IF NOT EXISTS smiles TEXT;
ALTER TABLE IF EXISTS elnmaterialchemdiagref ADD COLUMN IF NOT EXISTS moljson TEXT;

ALTER TABLE IF Exists materialtype ADD COLUMN IF NOT EXISTS usageoption Integer;

update materialtype set usageoption = 1 where usageoption IS NULL;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'sequencetable_sequencecode_seq' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE sequencetable_sequencecode_seq;
   ELSIF _kind = 'S' THEN  
     
   ELSE                  
    
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.sequencetable
(
    sequencecode integer NOT NULL DEFAULT nextval('sequencetable_sequencecode_seq'::regclass),
    applicationsequence bigint,
    resetperiod integer,
    screenname character varying(255) COLLATE pg_catalog."default",
    sequenceday integer,
    sequenceformat character varying(255) COLLATE pg_catalog."default",
    sequencemonth integer,
    sequenceview integer,
    sequenceyear integer,
    CONSTRAINT sequencetable_pkey PRIMARY KEY (sequencecode)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.sequencetable
    OWNER to postgres;


INSERT into sequencetable(sequencecode, screenname, resetperiod,sequenceview,sequenceformat,applicationsequence,sequenceday,sequencemonth,sequenceyear) VALUES (1,'IDS_SCN_SHEETORDERS',1,1,'{"t":{"t":"ELN"},"s":{"l":"6"}}',-1,0,0,0) ON CONFLICT (sequencecode) DO NOTHING; 
INSERT into sequencetable(sequencecode, screenname, resetperiod,sequenceview,sequenceformat,applicationsequence,sequenceday,sequencemonth,sequenceyear) VALUES (2,'IDS_SCN_PROTOCOLORDERS',1,1,'{"t":{"t":"ELN"},"s":{"l":"6"}}',-1,0,0,0) ON CONFLICT (sequencecode) DO NOTHING; 

ALTER TABLE IF EXISTS lslogilablimsorderdetail ADD COLUMN IF NOT EXISTS applicationsequence bigint;
ALTER TABLE IF EXISTS lslogilablimsorderdetail ADD COLUMN IF NOT EXISTS projectsequence bigint;
ALTER TABLE IF EXISTS lslogilablimsorderdetail ADD COLUMN IF NOT EXISTS sitesequence bigint;
ALTER TABLE IF EXISTS lslogilablimsorderdetail ADD COLUMN IF NOT EXISTS tasksequence bigint;
ALTER TABLE IF EXISTS lslogilablimsorderdetail ADD COLUMN IF NOT EXISTS ordertypesequence bigint;

ALTER TABLE IF EXISTS lslogilabprotocoldetail ADD COLUMN IF NOT EXISTS applicationsequence bigint;
ALTER TABLE IF EXISTS lslogilabprotocoldetail ADD COLUMN IF NOT EXISTS projectsequence bigint;
ALTER TABLE IF EXISTS lslogilabprotocoldetail ADD COLUMN IF NOT EXISTS sitesequence bigint;
ALTER TABLE IF EXISTS lslogilabprotocoldetail ADD COLUMN IF NOT EXISTS tasksequence bigint;
ALTER TABLE IF EXISTS lslogilabprotocoldetail ADD COLUMN IF NOT EXISTS ordertypesequence bigint;


DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'sequencetablesite_sequencecodesite_seq' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE sequencetablesite_sequencecodesite_seq;
   ELSIF _kind = 'S' THEN  
     
   ELSE                  
    
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.sequencetablesite
(
    sequencecodesite integer NOT NULL DEFAULT nextval('sequencetablesite_sequencecodesite_seq'::regclass),
    sequencecode integer,
    sitesequence bigint,
	sitecode integer,
    CONSTRAINT sequencetablesite_pkey PRIMARY KEY (sequencecodesite),
    CONSTRAINT fksguwcp89bljj2buemq8jedqjm FOREIGN KEY (sequencecode)
        REFERENCES public.sequencetable (sequencecode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.sequencetablesite
    OWNER to postgres;

	DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'sequencetableproject_sequencecodeproject_seq' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE sequencetableproject_sequencecodeproject_seq;
   ELSIF _kind = 'S' THEN  
     
   ELSE                  
    
   END IF;
END
$do$;

	CREATE TABLE IF NOT EXISTS public.sequencetableproject
(
    sequencecodeproject integer NOT NULL DEFAULT nextval('sequencetableproject_sequencecodeproject_seq'::regclass),
    projectsequence bigint,
    sequencecode integer,
	projectcode integer,
    CONSTRAINT sequencetableproject_pkey PRIMARY KEY (sequencecodeproject),
    CONSTRAINT fkhkeunrng30mjqhoeuiwal9qm3 FOREIGN KEY (sequencecode)
        REFERENCES public.sequencetable (sequencecode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.sequencetableproject
    OWNER to postgres;

		DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'sequencetabletask_sequencecodetask_seq' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE sequencetabletask_sequencecodetask_seq;
   ELSIF _kind = 'S' THEN  
     
   ELSE                  
    
   END IF;
END
$do$;

	CREATE TABLE IF NOT EXISTS public.sequencetabletask
(
    sequencecodetask integer NOT NULL DEFAULT nextval('sequencetabletask_sequencecodetask_seq'::regclass),
    sequencecode integer,
    tasksequence bigint,
	testcode integer,
    CONSTRAINT sequencetabletask_pkey PRIMARY KEY (sequencecodetask),
    CONSTRAINT fkic732g9sb7cp8let7ehg0nb68 FOREIGN KEY (sequencecode)
        REFERENCES public.sequencetable (sequencecode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.sequencetabletask
    OWNER to postgres;


		DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'sequencetableordertype_sequencecodeordertype_seq' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE sequencetableordertype_sequencecodeordertype_seq;
   ELSIF _kind = 'S' THEN  
     
   ELSE                  
    
   END IF;
END
$do$;

	
CREATE TABLE IF NOT EXISTS public.sequencetableordertype
(
    sequencecodeordertype integer NOT NULL DEFAULT nextval('sequencetableordertype_sequencecodeordertype_seq'::regclass),
    ordertype integer,
    ordertypesequence bigint,
    sequencecode integer,
    CONSTRAINT sequencetableordertype_pkey PRIMARY KEY (sequencecodeordertype),
    CONSTRAINT fkev1183angphftoup3tfo0825y FOREIGN KEY (sequencecode)
        REFERENCES public.sequencetable (sequencecode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.sequencetableordertype OWNER to postgres;

ALTER TABLE IF EXISTS elnmaterial ADD COLUMN IF NOT EXISTS assignedproject TEXT;

ALTER TABLE IF EXISTS sequencetable ADD COLUMN IF NOT EXISTS seperator character varying(10);

update sequencetable set seperator='_' where seperator Is Null;

ALTER TABLE IF EXISTS lslogilablimsorderdetail ADD COLUMN IF NOT EXISTS sequenceid character varying(255);

ALTER TABLE IF EXISTS lslogilabprotocoldetail ADD COLUMN IF NOT EXISTS sequenceid character varying(255);

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'sequencetableprojectlevel_sequencecodeprojectlevel_seq' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE sequencetableprojectlevel_sequencecodeprojectlevel_seq;
   ELSIF _kind = 'S' THEN  
     
   ELSE                  
    
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.sequencetableprojectlevel
(
    sequencecodeprojectlevel integer NOT NULL DEFAULT nextval('sequencetableprojectlevel_sequencecodeprojectlevel_seq'::regclass),
    projectcode integer,
    projectsequence bigint,
    CONSTRAINT sequencetableprojectlevel_pkey PRIMARY KEY (sequencecodeprojectlevel)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.sequencetableprojectlevel
    OWNER to postgres;

	DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'sequencetabletasklevel_sequencecodetasklevel_seq' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE sequencetabletasklevel_sequencecodetasklevel_seq;
   ELSIF _kind = 'S' THEN  
     
   ELSE                  
    
   END IF;
END
$do$;


	CREATE TABLE IF NOT EXISTS public.sequencetabletasklevel
(
    sequencecodetasklevel integer NOT NULL DEFAULT nextval('sequencetabletasklevel_sequencecodetasklevel_seq'::regclass),
    tasksequence bigint,
    testcode integer,
    CONSTRAINT sequencetabletasklevel_pkey PRIMARY KEY (sequencecodetasklevel)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.sequencetabletasklevel
    OWNER to postgres;


	ALTER TABLE IF EXISTS lslogilablimsorderdetail ADD COLUMN IF NOT EXISTS projectlevelsequence bigint;
ALTER TABLE IF EXISTS lslogilablimsorderdetail ADD COLUMN IF NOT EXISTS tasklevelsequence bigint;

ALTER TABLE IF EXISTS lslogilabprotocoldetail ADD COLUMN IF NOT EXISTS projectlevelsequence bigint;
ALTER TABLE IF EXISTS lslogilabprotocoldetail ADD COLUMN IF NOT EXISTS tasklevelsequence bigint;


INSERT into sequencetable(sequencecode, screenname, resetperiod,sequenceview,sequenceformat,applicationsequence,sequenceday,sequencemonth,sequenceyear,seperator) VALUES (3,'IDS_SCN_MATERIALMGNT',1,1,'{"t":{"t":"ELN"},"s":{"l":"6"}}',-1,0,0,0,'_') ON CONFLICT (sequencecode) DO NOTHING;
INSERT into sequencetable(sequencecode, screenname, resetperiod,sequenceview,sequenceformat,applicationsequence,sequenceday,sequencemonth,sequenceyear,seperator) VALUES (4,'IDS_SCN_EQUIPMENTMGNT',1,1,'{"t":{"t":"ELN"},"s":{"l":"6"}}',-1,0,0,0,'_') ON CONFLICT (sequencecode) DO NOTHING;
INSERT into sequencetable(sequencecode, screenname, resetperiod,sequenceview,sequenceformat,applicationsequence,sequenceday,sequencemonth,sequenceyear,seperator) VALUES (5,'IDS_SCN_SAMPLEMGNT',1,1,'{"t":{"t":"ELN"},"s":{"l":"6"}}',-1,0,0,0,'_') ON CONFLICT (sequencecode) DO NOTHING;
INSERT into sequencetable(sequencecode, screenname, resetperiod,sequenceview,sequenceformat,applicationsequence,sequenceday,sequencemonth,sequenceyear,seperator) VALUES (5,'IDS_SCN_SAMPLEMGNT',1,1,'{"t":{"t":"ELN"},"s":{"l":"6"}}',-1,0,0,0,'_') ON CONFLICT (sequencecode) DO NOTHING;
INSERT into sequencetable(sequencecode, screenname, resetperiod,sequenceview,sequenceformat,applicationsequence,sequenceday,sequencemonth,sequenceyear,seperator) VALUES (5,'IDS_SCN_SAMPLEMGNT',1,1,'{"t":{"t":"ELN"},"s":{"l":"6"}}',-1,0,0,0,'_') ON CONFLICT (sequencecode) DO NOTHING;

ALTER TABLE IF Exists Elnmaterial ADD Column IF NOT EXISTS sequenceid character varying(255);

ALTER TABLE IF Exists Elnmaterial ADD COLUMN IF NOT EXISTS applicationsequence bigint;

ALTER TABLE IF Exists Equipment ADD Column IF NOT EXISTS sequenceid character varying(255);

ALTER TABLE IF Exists Equipment ADD COLUMN IF NOT EXISTS applicationsequence bigint;

ALTER TABLE IF Exists Equipment ADD COLUMN IF NOT EXISTS sitesequence bigint;

CREATE TABLE IF NOT EXISTS public.materiallinks
(
    materiallinkcode integer NOT NULL,
    createddate timestamp without time zone,
    link character varying(500) COLLATE pg_catalog."default",
    nmaterialcatcode integer,
    nmaterialcode integer,
    nmaterialtypecode integer,
    nsitecode integer,
    nstatus integer,
    createby_usercode integer,
    CONSTRAINT materiallinks_pkey PRIMARY KEY (materiallinkcode),
    CONSTRAINT fklhqk864c6gtk703ef80ee2hw2 FOREIGN KEY (createby_usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.materiallinks
    OWNER to postgres;


CREATE TABLE IF NOT EXISTS public.lsorderlinks
(
    norderlinkcode integer NOT NULL,
    batchcode bigint,
    batchid character varying(255) COLLATE pg_catalog."default",
    createddate timestamp without time zone,
    link character varying(500) COLLATE pg_catalog."default",
    nsitecode integer,
    nstatus integer,
    createby_usercode integer,
    CONSTRAINT lsorderlinks_pkey PRIMARY KEY (norderlinkcode),
    CONSTRAINT fkfnq0rw4nowgbkfh0b9cxhpjux FOREIGN KEY (createby_usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.lsorderlinks
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.materialinventorylinks
(
    nmaterialinventorylinkcode integer NOT NULL,
    createddate timestamp without time zone,
    link character varying(500) COLLATE pg_catalog."default",
    nmaterialcatcode integer,
    nmaterialcode integer,
    nmaterialinventorycode integer,
    nmaterialtypecode integer,
    nsitecode integer,
    nstatus integer,
    createby_usercode integer,
    CONSTRAINT materialinventorylinks_pkey PRIMARY KEY (nmaterialinventorylinkcode),
    CONSTRAINT fk96f6pgb6768omxenjtashaxci FOREIGN KEY (createby_usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.materialinventorylinks
    OWNER to postgres;
    
ALTER TABLE tbladssettings ALTER COLUMN groupname TYPE character varying(255); 
INSERT INTO tblADSSettings (ldaplocationid,createddate,groupname,lastsyncdate,ldaplocation,ldapserverdomainname,ldapstatus) VALUES ('L0001', CURRENT_DATE,'Domain Guests',NULL ,'LDAP://192.168.0.250/OU=Users,DC=CORPAGARAM,DC=COM','corpagaram.com', 1)ON CONFLICT (ldaplocationid) DO NOTHING;

CREATE SEQUENCE IF NOT EXISTS sampletype_nsampletypecode_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS public.sampletype
(
    nsampletypecode integer NOT NULL DEFAULT nextval('sampletype_nsampletypecode_seq'),
    createdate timestamp without time zone,
    expvalidation boolean,
    jsondata jsonb,
    ndefaultstatus integer NOT NULL DEFAULT 4,
    nsitecode integer NOT NULL DEFAULT '-1'::integer,
    nstatus integer NOT NULL DEFAULT 1,
    quarvalidation boolean,
    sampletype integer,
    ssampletypename character varying(255) COLLATE pg_catalog."default",
    usageoption integer NOT NULL DEFAULT 1,
    barcode_barcodeno integer,
    createby_usercode integer,
    CONSTRAINT sampletype_pkey PRIMARY KEY (nsampletypecode),
    CONSTRAINT fk9y68k5g1e0b9s47dljd8usu4m FOREIGN KEY (barcode_barcodeno)
        REFERENCES public.barcodemaster (barcodeno) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fke6kija80tcdq005g1nvnw7ldp FOREIGN KEY (createby_usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.sampletype
    OWNER to postgres;

CREATE SEQUENCE IF NOT EXISTS samplecategory_nsamplecatcode_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS public.samplecategory
(
    nsamplecatcode integer NOT NULL DEFAULT nextval('samplecategory_nsamplecatcode_seq'::regclass),
    createdate timestamp without time zone,
    nactivestatus integer NOT NULL DEFAULT 1,
    nbarcode integer NOT NULL DEFAULT '-1'::integer,
    ncategorybasedflow integer NOT NULL DEFAULT 4,
    ndefaultstatus integer NOT NULL DEFAULT 4,
    needsectionwise integer,
    nsampletypecode integer NOT NULL,
    nsitecode integer NOT NULL DEFAULT '-1'::integer,
    nstatus integer NOT NULL DEFAULT 1,
    nuserrolecode integer NOT NULL,
    sdescription character varying(255) COLLATE pg_catalog."default",
    ssamplecatname character varying(100) COLLATE pg_catalog."default" NOT NULL,
    ssampletypename character varying(255) COLLATE pg_catalog."default",
    createby_usercode integer,
    CONSTRAINT samplecategory_pkey PRIMARY KEY (nsamplecatcode),
    CONSTRAINT fkl3j7bnkcsuonjp9tjx5v4uxpw FOREIGN KEY (createby_usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.samplecategory
    OWNER to postgres;


ALTER TABLE IF Exists materialcategory ADD COLUMN IF NOT EXISTS modifieddate timestamp without time zone;

ALTER TABLE IF Exists materialcategory ADD COLUMN IF NOT EXISTS modifiedby character varying(255);

ALTER TABLE IF Exists materialtype ADD COLUMN IF NOT EXISTS modifieddate timestamp without time zone;

ALTER TABLE IF Exists materialtype ADD COLUMN IF NOT EXISTS modifiedby character varying(255);

ALTER TABLE IF Exists sampletype ADD COLUMN IF NOT EXISTS modifieddate timestamp without time zone;

ALTER TABLE IF Exists sampletype ADD COLUMN IF NOT EXISTS modifiedby character varying(255);

ALTER TABLE IF Exists samplecategory ADD COLUMN IF NOT EXISTS modifieddate timestamp without time zone;

ALTER TABLE IF Exists samplecategory ADD COLUMN IF NOT EXISTS modifiedby character varying(255);

ALTER TABLE IF Exists samplestoragelocation ADD COLUMN IF NOT EXISTS modifieddate timestamp without time zone;

ALTER TABLE IF Exists samplestoragelocation ADD COLUMN IF NOT EXISTS modifiedby character varying(255);

ALTER TABLE IF Exists unit ADD COLUMN IF NOT EXISTS modifieddate timestamp without time zone;

ALTER TABLE IF Exists unit ADD COLUMN IF NOT EXISTS modifiedby character varying(255);

ALTER TABLE IF Exists section ADD COLUMN IF NOT EXISTS modifieddate timestamp without time zone;

ALTER TABLE IF Exists section ADD COLUMN IF NOT EXISTS modifiedby character varying(255);

ALTER TABLE IF Exists equipment ADD COLUMN IF NOT EXISTS modifieddate timestamp without time zone;

ALTER TABLE IF Exists equipment ADD COLUMN IF NOT EXISTS modifiedby character varying(255);

ALTER TABLE IF Exists equipmenttype ADD COLUMN IF NOT EXISTS modifieddate timestamp without time zone;

ALTER TABLE IF Exists equipmenttype ADD COLUMN IF NOT EXISTS modifiedby character varying(255);

ALTER TABLE IF Exists equipmentcategory ADD COLUMN IF NOT EXISTS modifieddate timestamp without time zone;

ALTER TABLE IF Exists equipmentcategory ADD COLUMN IF NOT EXISTS modifiedby character varying(255);

ALTER TABLE IF Exists elnmaterial ADD COLUMN IF NOT EXISTS modifieddate timestamp without time zone;

ALTER TABLE IF Exists elnmaterial ADD COLUMN IF NOT EXISTS modifiedby character varying(255);

ALTER TABLE IF Exists Lslogbooks ADD COLUMN IF NOT EXISTS modifieddate timestamp without time zone;

ALTER TABLE IF Exists Lslogbooks ADD COLUMN IF NOT EXISTS modifiedby character varying(255);

UPDATE tbladssettings SET groupname='Domain Guests' WHERE groupname='1234';

CREATE SEQUENCE IF NOT EXISTS sample_samplecode_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS public.sample
(
    samplecode integer NOT NULL DEFAULT nextval('sample_samplecode_seq'::regclass),
    applicationsequence bigint,
    createddate timestamp without time zone,
    jsondata jsonb,
    modifiedby character varying(255) COLLATE pg_catalog."default",
    modifieddate timestamp without time zone,
    nsitecode integer,
    nstatus integer,
    samplename character varying(255) COLLATE pg_catalog."default",
    sequenceid character varying(255) COLLATE pg_catalog."default",
    createby_usercode integer,
    samplecategory_nsamplecatcode integer,
    sampletype_nsampletypecode integer,
    unit_nunitcode integer,
	derivedtype integer,
    CONSTRAINT sample_pkey PRIMARY KEY (samplecode),
    CONSTRAINT fkcfgmmgiwg8v8kpdm3ju6jgxg FOREIGN KEY (sampletype_nsampletypecode)
        REFERENCES public.sampletype (nsampletypecode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkf8c3tn1hb0xdf15x5tk05tq4x FOREIGN KEY (unit_nunitcode)
        REFERENCES public.unit (nunitcode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkgoforoe9w5cm5qq1r0nh3t8a4 FOREIGN KEY (createby_usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkorw1e693bx13px5914rk15cq6 FOREIGN KEY (samplecategory_nsamplecatcode)
        REFERENCES public.samplecategory (nsamplecatcode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.sample
    OWNER to postgres;

	CREATE SEQUENCE IF NOT EXISTS derivedsamples_derivedsamplecode_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


	CREATE TABLE IF NOT EXISTS public.derivedsamples
(
    derivedsamplecode integer NOT NULL DEFAULT nextval('derivedsamples_derivedsamplecode_seq'::regclass),
    samplecode integer,
    parentsample_samplecode integer,
    CONSTRAINT derivedsamples_pkey PRIMARY KEY (derivedsamplecode),
    CONSTRAINT fkgn96b35q32kx09p3u93akpykh FOREIGN KEY (parentsample_samplecode)
        REFERENCES public.sample (samplecode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkm8p2x7s87gs4txs7x7b8y9ig9 FOREIGN KEY (samplecode)
        REFERENCES public.sample (samplecode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.derivedsamples
    OWNER to postgres;




