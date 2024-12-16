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


INSERT into sequencetable(sequencecode, screenname, resetperiod,sequenceview,sequenceformat,applicationsequence,sequenceday,sequencemonth,sequenceyear) VALUES (1,'IDS_SCN_SHEETORDERS',1,1,'{"t":{"t":"ELN"},"s":{"l":"6"}}',-1,0,0,0) ON CONFLICT (sequencecode) DO NOTHING;; 
INSERT into sequencetable(sequencecode, screenname, resetperiod,sequenceview,sequenceformat,applicationsequence,sequenceday,sequencemonth,sequenceyear) VALUES (2,'IDS_SCN_PROTOCOLORDERS',1,1,'{"t":{"t":"ELN"},"s":{"l":"6"}}',-1,0,0,0) ON CONFLICT (sequencecode) DO NOTHING;; 

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

ALTER TABLE IF EXISTS elnmaterial ADD COLUMN IF NOT EXISTS assignedtasks TEXT;