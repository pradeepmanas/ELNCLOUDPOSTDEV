DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'lsmultiusergroup_multiusergroupcode_seq'  
   -- sequence name, optionally schema-qualified
   INTO  _kind;

   IF NOT FOUND THEN       -- name is free
      CREATE SEQUENCE lsmultiusergroup_multiusergroupcode_seq;
   ELSIF _kind = 'S' THEN  -- sequence exists
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.lsmultiusergroup
(
    multiusergroupcode integer NOT NULL DEFAULT nextval('lsmultiusergroup_multiusergroupcode_seq'::regclass),
    defaultusergroup integer,
    usercode integer,
    lsusergroup_usergroupcode integer,
    CONSTRAINT lsmultiusergroup_pkey PRIMARY KEY (multiusergroupcode),
    CONSTRAINT fk65a4hddqb9gs2a9c50uq2ytvi FOREIGN KEY (lsusergroup_usergroupcode)
        REFERENCES public.lsusergroup (usergroupcode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fknok3att985drj90p3b41qn7n3 FOREIGN KEY (usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF Exists ONLY lsmultiusergroup
    OWNER to postgres;

ALTER TABLE IF Exists lsmultiusergroup ADD COLUMN IF NOT EXISTS defaultusergroup integer;


DO
$do$
declare
  multiusergroupcount integer :=0;
begin
 select count(*) 
   into multiusergroupcount
    from lsmultiusergroup;
-- 	select * from multiusergroupcount
 IF multiusergroupcount =0 THEN

 insert into lsmultiusergroup (usercode,lsusergroup_usergroupcode) 
(
	select usercode,lsusergroup.usergroupcode from lsusermaster,lsusergroup 
where lsusermaster.lsusergroup_usergroupcode = lsusergroup.usergroupcode
);
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
   WHERE  relname = 'instrumentcategory_instcategorykey_seq'  
   -- sequence name, optionally schema-qualified
   INTO  _kind;

   IF NOT FOUND THEN       -- name is free
      CREATE SEQUENCE instrumentcategory_instcategorykey_seq;
   ELSIF _kind = 'S' THEN  -- sequence exists
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.instrumentcategory
(
    instcategorykey integer NOT NULL DEFAULT nextval('instrumentcategory_instcategorykey_seq'::regclass),
    createddate timestamp without time zone,
    instcategoryname character varying(255) COLLATE pg_catalog."default",
    status integer,
    usercode integer NOT NULL,
    CONSTRAINT instrumentcategory_pkey PRIMARY KEY (instcategorykey),
    CONSTRAINT fkr9r74elc9958p2cwd7t0f46s2 FOREIGN KEY (usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF Exists ONLY instrumentcategory
    OWNER to postgres;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'instrumenttype_insttypekey_seq'  
   -- sequence name, optionally schema-qualified
   INTO  _kind;

   IF NOT FOUND THEN       -- name is free
      CREATE SEQUENCE instrumenttype_insttypekey_seq;
   ELSIF _kind = 'S' THEN  -- sequence exists
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.instrumenttype
(
    insttypekey integer NOT NULL DEFAULT nextval('instrumenttype_insttypekey_seq'::regclass),
    insttypename character varying(255) COLLATE pg_catalog."default",
    status integer,
    CONSTRAINT instrumenttype_pkey PRIMARY KEY (insttypekey)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF Exists ONLY instrumenttype
    OWNER to postgres;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'instrumentmaster_instmasterkey_seq'  
   -- sequence name, optionally schema-qualified
   INTO  _kind;

   IF NOT FOUND THEN       -- name is free
      CREATE SEQUENCE instrumentmaster_instmasterkey_seq;
   ELSIF _kind = 'S' THEN  -- sequence exists
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.instrumentmaster
(
    instmasterkey integer NOT NULL DEFAULT nextval('instrumentmaster_instmasterkey_seq'::regclass),
    createddate timestamp without time zone,
    electrodeno character varying(255) COLLATE pg_catalog."default",
    instiopno character varying(255) COLLATE pg_catalog."default",
    instmake character varying(255) COLLATE pg_catalog."default",
    instmodel character varying(255) COLLATE pg_catalog."default",
    instrumentcode character varying(255) COLLATE pg_catalog."default",
    instrumentname character varying(255) COLLATE pg_catalog."default",
    instused integer,
    status integer,
    usercode integer NOT NULL,
    instcategorykey integer NOT NULL,
    insttypekey integer NOT NULL,
    sitecode integer NOT NULL,
    CONSTRAINT instrumentmaster_pkey PRIMARY KEY (instmasterkey),
    CONSTRAINT fk6n46vqr0suwo7ihkpgoawfyaw FOREIGN KEY (usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkafcwcw77gqcgioxk1xbake8qf FOREIGN KEY (sitecode)
        REFERENCES public.lssitemaster (sitecode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkbtecnvm7ef7367gu74r47xtia FOREIGN KEY (insttypekey)
        REFERENCES public.instrumenttype (insttypekey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkm8ues9bjr9jn2ricv9wf86j9l FOREIGN KEY (instcategorykey)
        REFERENCES public.instrumentcategory (instcategorykey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF Exists ONLY instrumentmaster
    OWNER to postgres;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'instrumentrights_instrightskey_seq'  
   -- sequence name, optionally schema-qualified
   INTO  _kind;

   IF NOT FOUND THEN       -- name is free
      CREATE SEQUENCE instrumentrights_instrightskey_seq;
   ELSIF _kind = 'S' THEN  -- sequence exists
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.instrumentrights
(
    instrightskey integer NOT NULL DEFAULT nextval('instrumentrights_instrightskey_seq'::regclass),
    createddate timestamp without time zone,
    status integer,
    usercode integer NOT NULL,
    instmasterkey integer NOT NULL,
    sitecode integer NOT NULL,
    CONSTRAINT instrumentrights_pkey PRIMARY KEY (instrightskey),
    CONSTRAINT fkec7w9vpdblwpyp3cpu1pgtes FOREIGN KEY (instmasterkey)
        REFERENCES public.instrumentmaster (instmasterkey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fknxsy3y5riphptf39i9sk9mi1q FOREIGN KEY (sitecode)
        REFERENCES public.lssitemaster (sitecode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkvo3i0f8ty7w7mblo9eepa5cf FOREIGN KEY (usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF Exists ONLY instrumentrights
    OWNER to postgres;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'tcpsettings_tcpsettingskey_seq'  
   -- sequence name, optionally schema-qualified
   INTO  _kind;

   IF NOT FOUND THEN       -- name is free
      CREATE SEQUENCE tcpsettings_tcpsettingskey_seq;
   ELSIF _kind = 'S' THEN  -- sequence exists
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.tcpsettings
(
    tcpsettingskey integer NOT NULL DEFAULT nextval('tcpsettings_tcpsettingskey_seq'::regclass),
    connectmode integer,
    protocol integer,
    remotehostip character varying(255) COLLATE pg_catalog."default",
    remoteport integer,
    retrycount integer,
    serverport integer,
    timeout integer,
    instmasterkey integer NOT NULL,
    CONSTRAINT tcpsettings_pkey PRIMARY KEY (tcpsettingskey),
    CONSTRAINT fk5x7jf226eju1w38a0wcev5qpm FOREIGN KEY (instmasterkey)
        REFERENCES public.instrumentmaster (instmasterkey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF Exists ONLY tcpsettings
    OWNER to postgres;
	
DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'filesettings_filesettingskey_seq'  
   -- sequence name, optionally schema-qualified
   INTO  _kind;

   IF NOT FOUND THEN       -- name is free
      CREATE SEQUENCE filesettings_filesettingskey_seq;
   ELSIF _kind = 'S' THEN  -- sequence exists
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.filesettings
(
    filesettingskey integer NOT NULL DEFAULT nextval('filesettings_filesettingskey_seq'::regclass),
    fileinterval integer,
    filemode integer,
    instmasterkey integer NOT NULL,
    CONSTRAINT filesettings_pkey PRIMARY KEY (filesettingskey),
    CONSTRAINT fkoo4x52xyk1vovmrcrbi6b9wgt FOREIGN KEY (instmasterkey)
        REFERENCES public.instrumentmaster (instmasterkey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF Exists ONLY filesettings
    OWNER to postgres;
	
DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'method_methodkey_seq'  
   -- sequence name, optionally schema-qualified
   INTO  _kind;

   IF NOT FOUND THEN       -- name is free
      CREATE SEQUENCE method_methodkey_seq;
   ELSIF _kind = 'S' THEN  -- sequence exists
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.method
(
    methodkey integer NOT NULL DEFAULT nextval('method_methodkey_seq'::regclass),
    createddate timestamp without time zone,
    instrawdataurl character varying(255) COLLATE pg_catalog."default",
    methodname character varying(255) COLLATE pg_catalog."default",
    parser integer,
    samplesplit integer,
    status integer,
    usercode integer NOT NULL,
    instmasterkey integer NOT NULL,
    sitecode integer NOT NULL,
    CONSTRAINT method_pkey PRIMARY KEY (methodkey),
    CONSTRAINT fk8tcdgidomcac5m92yjy88rn42 FOREIGN KEY (sitecode)
        REFERENCES public.lssitemaster (sitecode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkh1ce6srr9t1rc4n6d8374e2s4 FOREIGN KEY (instmasterkey)
        REFERENCES public.instrumentmaster (instmasterkey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkoioijomm1txl18krswmdx61t7 FOREIGN KEY (usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT method_status_check CHECK (status <= 1 AND status >= '-1'::integer)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF Exists ONLY method
    OWNER to postgres;
	
DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'rs232settings_rs232settingskey_seq'  
   -- sequence name, optionally schema-qualified
   INTO  _kind;

   IF NOT FOUND THEN       -- name is free
      CREATE SEQUENCE rs232settings_rs232settingskey_seq;
   ELSIF _kind = 'S' THEN  -- sequence exists
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.rs232settings
(
    rs232settingskey integer NOT NULL DEFAULT nextval('rs232settings_rs232settingskey_seq'::regclass),
    baudrate character varying(255) COLLATE pg_catalog."default",
    comport integer,
    databits character varying(255) COLLATE pg_catalog."default",
    handshake integer,
    parity character varying(255) COLLATE pg_catalog."default",
    stopbits character varying(255) COLLATE pg_catalog."default",
    timeout integer,
    instmasterkey integer NOT NULL,
    CONSTRAINT rs232settings_pkey PRIMARY KEY (rs232settingskey),
    CONSTRAINT fkj4mt57blmip49mvsgrgqybx24 FOREIGN KEY (instmasterkey)
        REFERENCES public.instrumentmaster (instmasterkey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF Exists ONLY rs232settings
    OWNER to postgres;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'appmaster_appmasterkey_seq'  
   -- sequence name, optionally schema-qualified
   INTO  _kind;

   IF NOT FOUND THEN       -- name is free
      CREATE SEQUENCE appmaster_appmasterkey_seq;
   ELSIF _kind = 'S' THEN  -- sequence exists
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.appmaster
(
    appmasterkey integer NOT NULL DEFAULT nextval('appmaster_appmasterkey_seq'::regclass),
    appmastername character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT appmaster_pkey PRIMARY KEY (appmasterkey)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF Exists ONLY appmaster
    OWNER to postgres;
	
DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'controltype_controltypekey_seq'  
   -- sequence name, optionally schema-qualified
   INTO  _kind;

   IF NOT FOUND THEN       -- name is free
      CREATE SEQUENCE controltype_controltypekey_seq;
   ELSIF _kind = 'S' THEN  -- sequence exists
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.controltype
(
    controltypekey integer NOT NULL DEFAULT nextval('controltype_controltypekey_seq'::regclass),
    controltypename character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT controltype_pkey PRIMARY KEY (controltypekey)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF Exists ONLY controltype
    OWNER to postgres;
	
DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'customfield_customfieldkey_seq'  
   -- sequence name, optionally schema-qualified
   INTO  _kind;

   IF NOT FOUND THEN       -- name is free
      CREATE SEQUENCE customfield_customfieldkey_seq;
   ELSIF _kind = 'S' THEN  -- sequence exists
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.customfield
(
    customfieldkey integer NOT NULL DEFAULT nextval('customfield_customfieldkey_seq'::regclass),
    createddate timestamp without time zone,
    customfieldname character varying(255) COLLATE pg_catalog."default",
    fieldid character varying(255) COLLATE pg_catalog."default",
    status integer,
    controltypekey integer NOT NULL,
    usercode integer NOT NULL,
    methodkey integer NOT NULL,
    CONSTRAINT customfield_pkey PRIMARY KEY (customfieldkey),
    CONSTRAINT fk35epfuftf6ophfodlci3o9onv FOREIGN KEY (methodkey)
        REFERENCES public.method (methodkey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk5yc8alnlq7kaaomktpo6ii4j6 FOREIGN KEY (controltypekey)
        REFERENCES public.controltype (controltypekey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkigsshsax8gmobv2p3g3iv5wo4 FOREIGN KEY (usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT customfield_status_check CHECK (status <= 1 AND status >= '-1'::integer)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF Exists ONLY customfield
    OWNER to postgres;
	
DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'delimiter_delimiterkey_seq'  
   -- sequence name, optionally schema-qualified
   INTO  _kind;

   IF NOT FOUND THEN       -- name is free
      CREATE SEQUENCE delimiter_delimiterkey_seq;
   ELSIF _kind = 'S' THEN  -- sequence exists
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.delimiter
(
    delimiterkey integer NOT NULL DEFAULT nextval('delimiter_delimiterkey_seq'::regclass),
    actualdelimiter character varying(255) COLLATE pg_catalog."default",
    delimitername character varying(255) COLLATE pg_catalog."default",
    status integer,
    usercode integer NOT NULL,
    createddate timestamp without time zone,
    CONSTRAINT delimiter_pkey PRIMARY KEY (delimiterkey),
    CONSTRAINT fk7uvy9rxfaitaqu7q6xonhugna FOREIGN KEY (usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT delimiter_status_check CHECK (status <= 1 AND status >= '-1'::integer)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF Exists ONLY delimiter
    OWNER to postgres;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'generalfield_generalfieldkey_seq'  
   -- sequence name, optionally schema-qualified
   INTO  _kind;

   IF NOT FOUND THEN       -- name is free
      CREATE SEQUENCE generalfield_generalfieldkey_seq;
   ELSIF _kind = 'S' THEN  -- sequence exists
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.generalfield
(
    generalfieldkey integer NOT NULL DEFAULT nextval('generalfield_generalfieldkey_seq'::regclass),
    createddate timestamp without time zone,
    fieldid character varying(255) COLLATE pg_catalog."default",
    generalfieldname character varying(255) COLLATE pg_catalog."default",
    status integer,
    appmasterkey integer NOT NULL,
    usercode integer NOT NULL,
    sitecode integer NOT NULL,
    CONSTRAINT generalfield_pkey PRIMARY KEY (generalfieldkey),
    CONSTRAINT fkfsercxjq75kd35a8r9k6s0dlb FOREIGN KEY (usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkhhcjc8wuebyyh1297dwlb95pc FOREIGN KEY (appmasterkey)
        REFERENCES public.appmaster (appmasterkey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkm7guy0fwr7ohquogct5c44b1l FOREIGN KEY (sitecode)
        REFERENCES public.lssitemaster (sitecode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT generalfield_status_check CHECK (status <= 1 AND status >= '-1'::integer)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF Exists ONLY generalfield
    OWNER to postgres;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'parsermethod_parsermethodkey_seq'  
   -- sequence name, optionally schema-qualified
   INTO  _kind;

   IF NOT FOUND THEN       -- name is free
      CREATE SEQUENCE parsermethod_parsermethodkey_seq;
   ELSIF _kind = 'S' THEN  -- sequence exists
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.parsermethod
(
    parsermethodkey integer NOT NULL DEFAULT nextval('parsermethod_parsermethodkey_seq'::regclass),
    parsermethodname character varying(255) COLLATE pg_catalog."default",
    parsermethodtype integer,
    CONSTRAINT parsermethod_pkey PRIMARY KEY (parsermethodkey)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF Exists ONLY parsermethod
    OWNER to postgres;
	
DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'methoddelimiter_methoddelimiterkey_seq'  
   -- sequence name, optionally schema-qualified
   INTO  _kind;

   IF NOT FOUND THEN       -- name is free
      CREATE SEQUENCE methoddelimiter_methoddelimiterkey_seq;
   ELSIF _kind = 'S' THEN  -- sequence exists
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.methoddelimiter
(
    methoddelimiterkey integer NOT NULL DEFAULT nextval('methoddelimiter_methoddelimiterkey_seq'::regclass),
    createddate timestamp without time zone,
    status integer,
    usercode integer NOT NULL,
    delimiterkey integer NOT NULL,
    parsermethodkey integer,
    CONSTRAINT methoddelimiter_pkey PRIMARY KEY (methoddelimiterkey),
    CONSTRAINT fk6dw0y8clrxc6kvj2t1peibe6u FOREIGN KEY (delimiterkey)
        REFERENCES public.delimiter (delimiterkey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk82oy9sr2nkrr40bvdkal37q2j FOREIGN KEY (usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkpeyh9qo2nfgrurgwmypm3p7 FOREIGN KEY (parsermethodkey)
        REFERENCES public.parsermethod (parsermethodkey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT methoddelimiter_status_check CHECK (status <= 1 AND status >= '-1'::integer)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF Exists ONLY methoddelimiter
    OWNER to postgres;
	
DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'parserblock_parserblockkey_seq'  
   -- sequence name, optionally schema-qualified
   INTO  _kind;

   IF NOT FOUND THEN       -- name is free
      CREATE SEQUENCE parserblock_parserblockkey_seq;
   ELSIF _kind = 'S' THEN  -- sequence exists
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.parserblock
(
    parserblockkey integer NOT NULL DEFAULT nextval('parserblock_parserblockkey_seq'::regclass),
    createddate timestamp without time zone,
    parserblockname character varying(255) COLLATE pg_catalog."default",
    status integer,
    usercode integer NOT NULL,
    methodkey integer NOT NULL,
    CONSTRAINT parserblock_pkey PRIMARY KEY (parserblockkey),
    CONSTRAINT fk7s7xqqh4ftiivtsgq4v7i75i2 FOREIGN KEY (methodkey)
        REFERENCES public.method (methodkey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkkgks2dxamodg67agq3ypc5jod FOREIGN KEY (usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT parserblock_status_check CHECK (status <= 1 AND status >= '-1'::integer)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF Exists ONLY parserblock
    OWNER to postgres;
DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'parserfield_parserfieldkey_seq'  
   -- sequence name, optionally schema-qualified
   INTO  _kind;

   IF NOT FOUND THEN       -- name is free
      CREATE SEQUENCE parserfield_parserfieldkey_seq;
   ELSIF _kind = 'S' THEN  -- sequence exists
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.parserfield
(
    parserfieldkey integer NOT NULL DEFAULT nextval('parserfield_parserfieldkey_seq'::regclass),
    createddate timestamp without time zone,
    fieldid character varying(255) COLLATE pg_catalog."default",
    parserfieldname character varying(255) COLLATE pg_catalog."default",
    status integer,
    usercode integer NOT NULL,
    methoddelimiterkey integer NOT NULL,
    parserblockkey integer NOT NULL,
    CONSTRAINT parserfield_pkey PRIMARY KEY (parserfieldkey),
    CONSTRAINT fk6gw2e4yxxe8e7v0xbcxdv6v0k FOREIGN KEY (methoddelimiterkey)
        REFERENCES public.methoddelimiter (methoddelimiterkey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk7lub7rshwt1ursjloggphu2pn FOREIGN KEY (parserblockkey)
        REFERENCES public.parserblock (parserblockkey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fknp0gimfxbvo7hs810ovxdlf7n FOREIGN KEY (usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT parserfield_status_check CHECK (status <= 1 AND status >= '-1'::integer)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF Exists ONLY parserfield
    OWNER to postgres;
	
DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'parserignorechars_parserignorecharskey_seq'  
   -- sequence name, optionally schema-qualified
   INTO  _kind;

   IF NOT FOUND THEN       -- name is free
      CREATE SEQUENCE parserignorechars_parserignorecharskey_seq;
   ELSIF _kind = 'S' THEN  -- sequence exists
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.parserignorechars
(
    parserignorecharskey integer NOT NULL DEFAULT nextval('parserignorechars_parserignorecharskey_seq'::regclass),
    ignorechars character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT parserignorechars_pkey PRIMARY KEY (parserignorecharskey)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF Exists ONLY parserignorechars
    OWNER to postgres;
	
DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'parsertechnique_parsertechniquekey_seq'  
   -- sequence name, optionally schema-qualified
   INTO  _kind;

   IF NOT FOUND THEN       -- name is free
      CREATE SEQUENCE parsertechnique_parsertechniquekey_seq;
   ELSIF _kind = 'S' THEN  -- sequence exists
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.parsertechnique
(
    parsertechniquekey integer NOT NULL DEFAULT nextval('parsertechnique_parsertechniquekey_seq'::regclass),
    col integer,
    cols integer,
    createddate timestamp without time zone,
    identificationtext character varying(255) COLLATE pg_catalog."default",
    "row" integer,
    rows integer,
    status integer,
    usercode integer NOT NULL,
    parserfieldkey integer NOT NULL,
    parsermethodkey integer NOT NULL,
    CONSTRAINT parsertechnique_pkey PRIMARY KEY (parsertechniquekey),
    CONSTRAINT fk3fxpet79n35mkbt8sd6fsiot3 FOREIGN KEY (parserfieldkey)
        REFERENCES public.parserfield (parserfieldkey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk6qr7fexdv25mh7vx6g0cxnr7h FOREIGN KEY (usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fksyy7i17ddd7wg9e4ddgoaq7gp FOREIGN KEY (parsermethodkey)
        REFERENCES public.parsermethod (parsermethodkey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT parsertechnique_status_check CHECK (status <= 1 AND status >= '-1'::integer)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF Exists ONLY parsertechnique
    OWNER to postgres;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'sampletextsplit_sampletextsplitkey_seq'  
   -- sequence name, optionally schema-qualified
   INTO  _kind;

   IF NOT FOUND THEN       -- name is free
      CREATE SEQUENCE sampletextsplit_sampletextsplitkey_seq;
   ELSIF _kind = 'S' THEN  -- sequence exists
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.sampletextsplit
(
    sampletextsplitkey integer NOT NULL DEFAULT nextval('sampletextsplit_sampletextsplitkey_seq'::regclass),
    begintext character varying(255) COLLATE pg_catalog."default",
    begintextoccurrenceno integer,
    begintextrowindex integer,
    createddate timestamp without time zone,
    endtext character varying(255) COLLATE pg_catalog."default",
    endtextoccurrenceno integer,
    endtextrowindex integer,
    excludebegintext integer,
    excludeendtext integer,
    extractblock character varying(255) COLLATE pg_catalog."default",
    removeorextracttext integer,
    repeattext integer,
    status integer,
    usercode integer NOT NULL,
    methodkey integer NOT NULL,
    CONSTRAINT sampletextsplit_pkey PRIMARY KEY (sampletextsplitkey),
    CONSTRAINT fkdmgwpowkverduc9ha93vbv2me FOREIGN KEY (usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkjrmn58bmfiufwnwi6e9ah6pd9 FOREIGN KEY (methodkey)
        REFERENCES public.method (methodkey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT sampletextsplit_status_check CHECK (status <= 1 AND status >= '-1'::integer)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF Exists ONLY sampletextsplit
    OWNER to postgres;
	
DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'samplelinesplit_samplelinesplitkey_seq'  
   -- sequence name, optionally schema-qualified
   INTO  _kind;

   IF NOT FOUND THEN       -- name is free
      CREATE SEQUENCE samplelinesplit_samplelinesplitkey_seq;
   ELSIF _kind = 'S' THEN  -- sequence exists
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.samplelinesplit
(
    samplelinesplitkey integer NOT NULL DEFAULT nextval('samplelinesplit_samplelinesplitkey_seq'::regclass),
    bottomlines integer,
    centertext character varying(255) COLLATE pg_catalog."default",
    centertextoccurrenceno integer,
    centertextrowindex integer,
    createddate timestamp without time zone,
    excludecentertext integer,
    extractblock character varying(255) COLLATE pg_catalog."default",
    removeorextractlines integer,
    repeatlines integer,
    status integer,
    toplines integer,
    usercode integer NOT NULL,
    methodkey integer NOT NULL,
    CONSTRAINT samplelinesplit_pkey PRIMARY KEY (samplelinesplitkey),
    CONSTRAINT fkht7c1bbov6vkshqob2gmjwdr6 FOREIGN KEY (usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fktks2ue00gij24bxdiwd40o9ov FOREIGN KEY (methodkey)
        REFERENCES public.method (methodkey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT samplelinesplit_status_check CHECK (status <= 1 AND status >= '-1'::integer)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF Exists ONLY samplelinesplit
    OWNER to postgres;	
	
DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'sampleextract_sampleextractkey_seq'  
   -- sequence name, optionally schema-qualified
   INTO  _kind;

   IF NOT FOUND THEN       -- name is free
      CREATE SEQUENCE sampleextract_sampleextractkey_seq;
   ELSIF _kind = 'S' THEN  -- sequence exists
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.sampleextract
(
    sampleextractkey integer NOT NULL DEFAULT nextval('sampleextract_sampleextractkey_seq'::regclass),
    absolutelines integer,
    createddate timestamp without time zone,
    extracttextorlines integer,
    matchtext character varying(255) COLLATE pg_catalog."default",
    status integer,
    usercode integer NOT NULL,
    methodkey integer NOT NULL,
    samplelinesplitkey integer,
    sampletextsplitkey integer,
    CONSTRAINT sampleextract_pkey PRIMARY KEY (sampleextractkey),
    CONSTRAINT fkcsm5x0wa4lcvpuefr5jv2rkk6 FOREIGN KEY (sampletextsplitkey)
        REFERENCES public.sampletextsplit (sampletextsplitkey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkcyf7eorb4xhl49ncrn7vstbm2 FOREIGN KEY (samplelinesplitkey)
        REFERENCES public.samplelinesplit (samplelinesplitkey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkkovn0it6idxhnlfmwqlusl6t1 FOREIGN KEY (usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkseej6dxn5jttpsj80qybni4f3 FOREIGN KEY (methodkey)
        REFERENCES public.method (methodkey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT sampleextract_status_check CHECK (status <= 1 AND status >= '-1'::integer)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF Exists ONLY sampleextract
    OWNER to postgres;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'subparserfield_subparserfieldkey_seq'  
   -- sequence name, optionally schema-qualified
   INTO  _kind;

   IF NOT FOUND THEN       -- name is free
      CREATE SEQUENCE subparserfield_subparserfieldkey_seq;
   ELSIF _kind = 'S' THEN  -- sequence exists
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.subparserfield
(
    subparserfieldkey integer NOT NULL DEFAULT nextval('subparserfield_subparserfieldkey_seq'::regclass),
    createddate timestamp without time zone,
    fieldid character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    status integer,
    subparserfieldname character varying(255) COLLATE pg_catalog."default",
    subparserfieldposition character varying(255) COLLATE pg_catalog."default",
    subparserfieldtype character varying(255) COLLATE pg_catalog."default",
    usercode integer NOT NULL,
    parserfieldkey integer NOT NULL,
    CONSTRAINT subparserfield_pkey PRIMARY KEY (subparserfieldkey),
    CONSTRAINT fk70r9rkwtrsooib0hdxvvyo60y FOREIGN KEY (parserfieldkey)
        REFERENCES public.parserfield (parserfieldkey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk93f8d4ctwh78d01r3l2eq3jy0 FOREIGN KEY (usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT subparserfield_status_check CHECK (status <= 1 AND status >= '-1'::integer)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF Exists ONLY subparserfield
    OWNER to postgres;
	
DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'subparsertechnique_subparsertechniquekey_seq'  
   -- sequence name, optionally schema-qualified
   INTO  _kind;

   IF NOT FOUND THEN       -- name is free
      CREATE SEQUENCE subparsertechnique_subparsertechniquekey_seq;
   ELSIF _kind = 'S' THEN  -- sequence exists
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.subparsertechnique
(
    subparsertechniquekey integer NOT NULL DEFAULT nextval('subparsertechnique_subparsertechniquekey_seq'::regclass),
    createddate timestamp without time zone,
    inputfields character varying(255) COLLATE pg_catalog."default",
    outputfields character varying(255) COLLATE pg_catalog."default",
    status integer,
    usercode integer NOT NULL,
    methoddelimiterkey integer NOT NULL,
    parserfieldkey integer NOT NULL,
    CONSTRAINT subparsertechnique_pkey PRIMARY KEY (subparsertechniquekey),
    CONSTRAINT fk273duje3s795iftkfx01cs56 FOREIGN KEY (usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk4q399nwu0oum3xrnhpm1jsp0i FOREIGN KEY (parserfieldkey)
        REFERENCES public.parserfield (parserfieldkey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk52jrvj3jmsi0g74bv27f4yd3j FOREIGN KEY (methoddelimiterkey)
        REFERENCES public.methoddelimiter (methoddelimiterkey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT subparsertechnique_status_check CHECK (status <= 1 AND status >= '-1'::integer)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF Exists ONLY subparsertechnique
    OWNER to postgres;
    
INSERT into parsermethod (parsermethodkey, parsermethodname, parsermethodtype) VALUES (1, N'Datablock', 1) ON CONFLICT(parsermethodkey)DO NOTHING;
INSERT into parsermethod (parsermethodkey, parsermethodname, parsermethodtype) VALUES (2, N'Beginrowmatch', 1) ON CONFLICT(parsermethodkey)DO NOTHING;
INSERT into parsermethod (parsermethodkey, parsermethodname, parsermethodtype) VALUES (3, N'Endrowmatch', 1) ON CONFLICT(parsermethodkey)DO NOTHING;
INSERT into parsermethod (parsermethodkey, parsermethodname, parsermethodtype) VALUES (4, N'Endcolmatch', 1) ON CONFLICT(parsermethodkey)DO NOTHING;
INSERT into parsermethod (parsermethodkey, parsermethodname, parsermethodtype) VALUES (5, N'None', 2) ON CONFLICT(parsermethodkey)DO NOTHING;
INSERT into parsermethod (parsermethodkey, parsermethodname, parsermethodtype) VALUES (6, N'Merge', 2) ON CONFLICT(parsermethodkey)DO NOTHING;
INSERT into parsermethod (parsermethodkey, parsermethodname, parsermethodtype) VALUES (7, N'Split', 2) ON CONFLICT(parsermethodkey)DO NOTHING;
INSERT into parsermethod (parsermethodkey, parsermethodname, parsermethodtype) VALUES (8, N'Shift', 2) ON CONFLICT(parsermethodkey)DO NOTHING;

INSERT into instrumenttype(insttypekey,insttypename,status) VALUES(1,'File',1) ON CONFLICT(insttypekey)DO NOTHING;
INSERT into instrumenttype(insttypekey,insttypename,status) VALUES(2,'RS232',1) ON CONFLICT(insttypekey)DO NOTHING;
INSERT into instrumenttype(insttypekey,insttypename,status) VALUES(3,'TCP\IP',1) ON CONFLICT(insttypekey)DO NOTHING;

ALTER TABLE IF Exists DataSourceConfig ADD COLUMN IF NOT EXISTS customer_creator_id numeric(30,0);

ALTER TABLE IF Exists DataSourceConfig ADD COLUMN IF NOT EXISTS customer_crm_id numeric(30,0);

ALTER TABLE IF Exists DataSourceConfig ADD COLUMN IF NOT EXISTS customer_subscription_id numeric(30,0);

CREATE TABLE IF NOT EXISTS public.lsprotocolstepversion
(
    protocolstepversioncode integer NOT NULL,
    protocolmastercode integer,
    protocolstepcode integer,
    protocolstepname character varying(255) COLLATE pg_catalog."default",
    status integer,
    stepno integer,
    versionno integer,
    CONSTRAINT lsprotocolstepversion_pkey PRIMARY KEY (protocolstepversioncode)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.lsprotocolstepversion
    OWNER to postgres;
    
DO
$do$
declare
  multiusergroupcount integer :=0;
begin

 select count(*) into multiusergroupcount from information_schema.columns 
                  where table_name='cloudlsprotocolversionstep';
			
 IF multiusergroupcount <=6
 THEN
 	DROP TABLE IF EXISTS cloudlsprotocolversionstep;
   END IF;
END
$do$;    
    
CREATE TABLE IF NOT EXISTS public.cloudlsprotocolversionstep
(
    id integer NOT NULL,
    protocolmastercode integer,
    status integer,
    versionname character varying(100) COLLATE pg_catalog."default",
    versionno integer,
    idversioncode integer NOT NULL,
    lsprotocolstepinfo jsonb,
    CONSTRAINT cloudlsprotocolversionstep_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.cloudlsprotocolversionstep
    OWNER to postgres;
    
update lsaudittrailconfigmaster set manualaudittrail = 0;

ALTER TABLE IF Exists Lsrepositories ADD COLUMN IF NOT EXISTS isexpiredate boolean;

ALTER TABLE IF Exists lsrepositories ADD COLUMN IF NOT EXISTS isonexpireddatefield varchar(250);

ALTER TABLE IF Exists lsordersampleupdate ADD COLUMN IF NOT EXISTS createdbyusername character varying(255);

ALTER TABLE IF Exists lsordersampleupdate ADD COLUMN IF NOT EXISTS historydetails character varying(255);

ALTER TABLE IF Exists lsordersampleupdate ADD COLUMN IF NOT EXISTS screenmodule character varying(255);

ALTER TABLE IF Exists lsnotification ADD COLUMN IF NOT EXISTS repositorycode integer;

ALTER TABLE IF Exists lsnotification ADD COLUMN IF NOT EXISTS repositorydatacode integer;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'lsusersettings_userid_seq'  
   INTO  _kind;
   IF NOT FOUND THEN  
      CREATE SEQUENCE lsusersettings_userid_seq;
   ELSIF _kind = 'S' THEN 
   ELSE                  
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.lsusersettings
(
    userid integer NOT NULL DEFAULT nextval('lsusersettings_userid_seq'::regclass),
    dformat character varying(255) COLLATE pg_catalog."default",
    usercode integer,
    CONSTRAINT lsusersettings_pkey PRIMARY KEY (userid)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.lsusersettings
    OWNER to postgres;
    
    
ALTER TABLE IF Exists lslogilablimsorderdetail ADD COLUMN IF NOT EXISTS lsrepositoriesdata_repositorydatacode integer;

DO
$do$
declare
  multiusergroupcount integer :=0;
begin

SELECT count(*) into multiusergroupcount FROM
information_schema.table_constraints WHERE constraint_name='fkm083etdp4bi5xn7vxmupfkv3e'
AND table_name='lslogilablimsorderdetail';
 IF multiusergroupcount =0 THEN
 	ALTER TABLE ONLY lslogilablimsorderdetail ADD CONSTRAINT fkm083etdp4bi5xn7vxmupfkv3e FOREIGN KEY (lsrepositoriesdata_repositorydatacode) REFERENCES lsrepositoriesdata(repositorydatacode);
   END IF;
END
$do$;  

ALTER TABLE IF Exists lslogilablimsorderdetail ADD COLUMN IF NOT EXISTS lsrepositories_repositorycode integer;

DO
$do$
declare
  multiusergroupcount integer :=0;
begin

SELECT count(*) into multiusergroupcount FROM
information_schema.table_constraints WHERE constraint_name='fkjayf1kn6mm1gfpj451la8b88i'
AND table_name='lslogilablimsorderdetail';
 IF multiusergroupcount =0 THEN
 	ALTER TABLE ONLY lslogilablimsorderdetail ADD CONSTRAINT fkjayf1kn6mm1gfpj451la8b88i FOREIGN KEY (lsrepositories_repositorycode) REFERENCES lsrepositories(repositorycode);
   END IF;
END
$do$;

update Lsrepositories set isexpiredate = false where isexpiredate IS NULL;
update lsaudittrailconfigmaster set manualaudittrail = 1 where serialno = 1;
update lsaudittrailconfigmaster set manualaudittrail = 1 where serialno = 2;
update lsaudittrailconfigmaster set manualaudittrail = 1 where serialno = 10;
update lsaudittrailconfigmaster set manualaudittrail = 1 where serialno = 38;
update lsaudittrailconfigmaster set manualaudittrail = 1 where serialno = 40;

update lsusergrouprightsmaster set modulename='Sheet Templates' where orderno=10;
update lsusergrouprights set modulename='Sheet Templates'  where displaytopic='Sheet Creation';

update lsusergrouprightsmaster set modulename='Protocol Templates' where orderno=50;
update lsusergrouprights set modulename='Protocol Templates' where modulename='Protocols';

INSERT into LSusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder) VALUES (51, 'Protocol Templates', 'Protocol Templates', '0', 'NA', 'NA', 'NA', '0,0,0',51) ON CONFLICT(orderno)DO NOTHING;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'lsprotocolworkflow_workflowcode_seq'  
   
   INTO  _kind;

   IF NOT FOUND THEN  
      CREATE SEQUENCE lsprotocolworkflow_workflowcode_seq;
	  
	  ALTER SEQUENCE lsprotocolworkflow_workflowcode_seq OWNED BY lsprotocolworkflow.workflowcode;
   ELSIF _kind = 'S' THEN 
     
   ELSE                   
   END IF;
END
$do$;
