update lslogilablimsorderdetail set directorycode = null where directorycode in (select directorycode from lssheetorderstructure where directoryname = 'my order');
delete from lssheetorderstructure where parentdircode in (select directorycode from lssheetorderstructure where directoryname = 'my order');
delete from lssheetorderstructure where directoryname = 'my order';

ALTER TABLE IF Exists lslogilablimsorderdetail ADD COLUMN IF NOT EXISTS orderdisplaytype integer;

update LSlogilablimsorderdetail set orderdisplaytype = 1 where orderdisplaytype is null;

CREATE TABLE IF NOT EXISTS public.samplestoragelocation
(
    samplestoragelocationkey integer NOT NULL,
    createdby text COLLATE pg_catalog."default" NOT NULL,
    createddate timestamp without time zone NOT NULL,
    samplestoragelocationname character varying COLLATE pg_catalog."default" NOT NULL,
    sitekey integer NOT NULL,
    status integer NOT NULL,
    CONSTRAINT samplestoragelocation_pkey PRIMARY KEY (samplestoragelocationkey)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.samplestoragelocation
    OWNER to postgres;
	
DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'samplestoragelocation_sequence' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE samplestoragelocation_sequence;
   ELSIF _kind = 'S' THEN  
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.samplestorageversion
(
    samplestorageversionkey integer NOT NULL,
    approvalstatus integer NOT NULL,
    createdby text COLLATE pg_catalog."default" NOT NULL,
    createddate timestamp without time zone NOT NULL,
    jsonbresult jsonb NOT NULL,
    versionno integer NOT NULL,
    samplestoragelocationkey integer NOT NULL,
    CONSTRAINT samplestorageversion_pkey PRIMARY KEY (samplestorageversionkey),
    CONSTRAINT fk5g5vdy68mma3qj12crvewwu8b FOREIGN KEY (samplestoragelocationkey)
        REFERENCES public.samplestoragelocation (samplestoragelocationkey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.samplestorageversion
    OWNER to postgres;
	
DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'samplestorageversion_sequence' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE samplestorageversion_sequence;
   ELSIF _kind = 'S' THEN  
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

ALTER TABLE IF Exists lslogilablimsorderdetail ADD COLUMN IF NOT EXISTS lstestmasterlocal_testcode integer;
	
DO
$do$
declare
  testcodelocal integer :=0;
begin

SELECT count(*) into testcodelocal FROM
information_schema.table_constraints WHERE constraint_name='fkoc0h7d2l44ol5jm8h12mghlf0'
AND table_name='lslogilablimsorderdetail';
 IF testcodelocal =0 THEN
 	ALTER TABLE ONLY lslogilablimsorderdetail ADD CONSTRAINT fkoc0h7d2l44ol5jm8h12mghlf0 FOREIGN KEY (lstestmasterlocal_testcode) REFERENCES lstestmasterlocal(testcode);
   END IF;
END
$do$;

update lslogilablimsorderdetail set lstestmasterlocal_testcode = testcode where filetype != 0 and (select count(*) from lstestmasterlocal where testcode = testcode) > 0 and testcode >0 and lstestmasterlocal_testcode is null;

	-- Table: public.materialtype

-- DROP TABLE IF EXISTS public.materialtype;


CREATE TABLE IF NOT EXISTS public.materialtype
(
    nmaterialtypecode integer NOT NULL,
    jsondata jsonb,
    ndefaultstatus integer NOT NULL DEFAULT 4,
    nsitecode integer NOT NULL DEFAULT '-1'::integer,
    nstatus integer NOT NULL DEFAULT 1,
    CONSTRAINT materialtype_pkey PRIMARY KEY (nmaterialtypecode)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.materialtype
    OWNER to postgres;
	

INSERT INTO public.materialtype(
	nmaterialtypecode, jsondata, ndefaultstatus, nsitecode, nstatus)
	VALUES (1,'{
  "prefix": "V",
  "sdescription": "Volumetric Type",
  "needSectionwise": 3,
  "smaterialtypename": {
    "en-US": "Volumetric Type",
    "ru-RU": "Объемный тип",
    "tg-TG": "Навъи ҳаҷмӣ"
  },
  "ismaterialSectionneed": 4
}' , 4, -1, 1) ON CONFLICT(nmaterialtypecode)DO NOTHING; 

INSERT INTO public.materialtype(
	nmaterialtypecode, jsondata, ndefaultstatus, nsitecode, nstatus)
	VALUES (2,'{
  "prefix": "M",
  "sdescription": "Material Inventory Type",
  "needSectionwise": 3,
  "smaterialtypename": {
    "en-US": "Material Inventory Type",
    "ru-RU": "Тип инвентаризации материалов",
    "tg-TG": "Навъи инвентаризатсияи мавод"
  },
  "ismaterialSectionneed": 3
}' , 4, -1, 1) ON CONFLICT(nmaterialtypecode)DO NOTHING; 

INSERT INTO public.materialtype(
	nmaterialtypecode, jsondata, ndefaultstatus, nsitecode, nstatus)
	VALUES (3,'{
  "prefix": "S",
  "sdescription": "Standared Type",
  "needSectionwise": 3,
  "smaterialtypename": {
    "en-US": "Standard Type",
    "ru-RU": "Стандартный тип",
    "tg-TG": "Навъи стандартӣ"
  },
  "ismaterialSectionneed": 3
}' , 4, -1, 1) ON CONFLICT(nmaterialtypecode)DO NOTHING;


	
ALTER TABLE IF Exists delimiter ADD COLUMN IF NOT EXISTS defaultvalue integer;

update delimiter set defaultvalue = 1 where delimitername='None'and defaultvalue is null;

update delimiter set defaultvalue = 1 where delimitername='Result without space'and defaultvalue is null;

update delimiter set defaultvalue = 1 where delimitername='Result with space'and defaultvalue is null;

update delimiter set defaultvalue = 1 where delimitername='Colon' and defaultvalue is null;

update delimiter set defaultvalue = 1 where delimitername='Comma'  and defaultvalue is null;

update delimiter set defaultvalue = 1 where delimitername='Space' and defaultvalue is null;

update delimiter set defaultvalue = 1 where delimitername='Split Dot' and defaultvalue is null;

update delimiter set defaultvalue = 1 where delimitername='Merge Dot' and defaultvalue is null;

update delimiter set defaultvalue = 1 where delimitername='Slash' and defaultvalue is null;

ALTER TABLE IF Exists method ADD COLUMN IF NOT EXISTS filename varchar(255);

update method set filename = instrawdataurl where filename is null;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'lsprotocolorderstructure_directorycode_seq' 
   INTO  _kind;

   IF NOT FOUND THEN       
      CREATE SEQUENCE lsprotocolorderstructure_directorycode_seq;
   ELSIF _kind = 'S' THEN  
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;


CREATE TABLE IF NOT EXISTS public.lsprotocolorderstructure
(
    directorycode bigint NOT NULL DEFAULT nextval('lsprotocolorderstructure_directorycode_seq'::regclass),
    datecreated timestamp without time zone,
    datemodified timestamp without time zone,
    directoryname character varying(255) COLLATE pg_catalog."default",
    icon character varying(255) COLLATE pg_catalog."default",
    length integer,
    parentdircode bigint,
    path character varying(255) COLLATE pg_catalog."default",
    size integer,
    CONSTRAINT lsprotocolorderstructure_pkey PRIMARY KEY (directorycode)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.lsprotocolorderstructure
    OWNER to postgres;
    
ALTER TABLE IF Exists lslogilabprotocoldetail ADD COLUMN IF NOT EXISTS orderdisplaytype integer;

update lslogilabprotocoldetail set orderdisplaytype = 1 where orderdisplaytype is null;

ALTER TABLE IF Exists lslogilabprotocoldetail ADD COLUMN IF NOT EXISTS directorycode bigint;

update lslogilabprotocoldetail set directorycode = null where directorycode in (select directorycode from lsprotocolorderstructure where directoryname = 'my order');

CREATE TABLE IF NOT EXISTS public.materialconfig
(
    nmaterialconfigcode integer NOT NULL,
    nmaterialtypecode integer NOT NULL,
    nformcode integer NOT NULL,
    jsondata jsonb NOT NULL,
    nstatus integer NOT NULL DEFAULT 1,
    CONSTRAINT materialconfig_pkey PRIMARY KEY (nmaterialconfigcode)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.materialconfig
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.period
(
    nperiodcode integer NOT NULL,
    jsondata jsonb NOT NULL,
    ndefaultstatus integer NOT NULL DEFAULT 4,
    nsitecode integer NOT NULL DEFAULT '-1'::integer,
    nstatus integer NOT NULL DEFAULT 1,
    CONSTRAINT period_pkey PRIMARY KEY (nperiodcode)
)

TABLESPACE pg_default;

ALTER TABLE public.period OWNER to postgres;
	
DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'section_sequence' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE section_sequence;
   ELSIF _kind = 'S' THEN  
      -- do nothing?
   ELSE             
      -- do something!
   END IF;
END
$do$;   
    
CREATE TABLE IF NOT EXISTS public.section
(
    nsectioncode integer  Not NULL DEFAULT nextval('section_sequence'::regclass),
    ssectionname character varying(100) NOT NULL,
    sdescription character varying(255) ,
    ndefaultstatus integer NOT NULL DEFAULT 4,
    nsitecode integer NOT NULL DEFAULT '-1'::integer,
    nstatus integer NOT NULL DEFAULT 1,
    CONSTRAINT section_pkey PRIMARY KEY (nsectioncode)
)

TABLESPACE pg_default;

ALTER TABLE public.section  OWNER to postgres;
    
DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'unit_sequence' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE unit_sequence;
   ELSIF _kind = 'S' THEN  
      -- do nothing?
   ELSE             
      -- do something!
   END IF;
END
$do$;   
    
CREATE TABLE IF NOT EXISTS public.unit
(
    nunitcode integer Not NULL DEFAULT nextval('unit_sequence'::regclass),
    sunitname character varying(100) NOT NULL,
    sdescription character varying(255) ,
    ndefaultstatus integer NOT NULL DEFAULT 4,
    nsitecode integer NOT NULL DEFAULT '-1'::integer,
    nstatus integer NOT NULL DEFAULT 1,
    CONSTRAINT unit_pkey PRIMARY KEY (nunitcode)
)

TABLESPACE pg_default;

ALTER TABLE public.unit OWNER to postgres;


DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'materialcategory_nmaterialcatcode_seq' 
   INTO  _kind;

   IF NOT FOUND THEN       
      CREATE SEQUENCE materialcategory_nmaterialcatcode_seq;
   ELSIF _kind = 'S' THEN  
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.materialcategory
(
    nmaterialcatcode integer NOT NULL DEFAULT nextval('materialcategory_nmaterialcatcode_seq'::regclass),
    nactivestatus integer NOT NULL DEFAULT 1,
    nbarcode integer NOT NULL DEFAULT '-1'::integer,
    ncategorybasedflow integer NOT NULL DEFAULT 4,
    ndefaultstatus integer NOT NULL DEFAULT 4,
    needsectionwise integer,
    nmaterialtypecode integer NOT NULL,
    nsitecode integer NOT NULL DEFAULT '-1'::integer,
    nstatus integer NOT NULL DEFAULT 1,
    nuserrolecode integer NOT NULL,
    sdescription character varying(255) COLLATE pg_catalog."default",
    smaterialcatname character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT materialcategory_pkey PRIMARY KEY (nmaterialcatcode)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.materialcategory
    OWNER to postgres;

ALTER TABLE IF Exists lsprotocolorderstructure ADD COLUMN IF NOT EXISTS modifiedby_usercode integer;

DO
$do$
declare
  resultvalues integer :=0;
begin

SELECT count(*) into resultvalues FROM
information_schema.table_constraints WHERE constraint_name='fkly66uu1eaxbbop72kclab53q6'
AND table_name='lsprotocolorderstructure';
 IF resultvalues =0 THEN
 	ALTER TABLE ONLY lsprotocolorderstructure ADD CONSTRAINT fkly66uu1eaxbbop72kclab53q6 FOREIGN KEY (modifiedby_usercode) REFERENCES lsusermaster(usercode);
   END IF;
END
$do$;  

ALTER TABLE IF Exists lsprotocolorderstructure ADD COLUMN IF NOT EXISTS createdby integer;
ALTER TABLE IF Exists lsprotocolorderstructure ADD COLUMN IF NOT EXISTS onlytome integer;
ALTER TABLE IF Exists lsprotocolorderstructure ADD COLUMN IF NOT EXISTS sitecode integer;
ALTER TABLE IF Exists lsprotocolorderstructure ADD COLUMN IF NOT EXISTS tothesite integer;
ALTER TABLE IF Exists lsprotocolorderstructure ADD COLUMN IF NOT EXISTS createdbyusername varchar(255);

update lsprotocolorderstructure set sitecode = 1 where sitecode is null;
update lsprotocolorderstructure set createdby = 1 where createdby is null;
update lsprotocolorderstructure set createdbyusername = 'Administrator' where createdbyusername is null;

ALTER TABLE IF Exists lssheetorderstructure ADD COLUMN IF NOT EXISTS modifiedby_usercode integer;

DO
$do$
declare
  resultvalues integer :=0;
begin

SELECT count(*) into resultvalues FROM
information_schema.table_constraints WHERE constraint_name='fkqdpmym80hts6xti2cj89sx315'
AND table_name='lssheetorderstructure';
 IF resultvalues =0 THEN
 	ALTER TABLE ONLY lssheetorderstructure ADD CONSTRAINT fkqdpmym80hts6xti2cj89sx315 FOREIGN KEY (modifiedby_usercode) REFERENCES lsusermaster(usercode);
   END IF;
END
$do$;  

ALTER TABLE IF Exists lssheetorderstructure ADD COLUMN IF NOT EXISTS createdby integer;
ALTER TABLE IF Exists lssheetorderstructure ADD COLUMN IF NOT EXISTS onlytome integer;
ALTER TABLE IF Exists lssheetorderstructure ADD COLUMN IF NOT EXISTS sitecode integer;
ALTER TABLE IF Exists lssheetorderstructure ADD COLUMN IF NOT EXISTS tothesite integer;
ALTER TABLE IF Exists lssheetorderstructure ADD COLUMN IF NOT EXISTS createdbyusername varchar(255);

update lssheetorderstructure set sitecode = 1 where sitecode is null;
update lssheetorderstructure set createdby = 1 where createdby is null;
update lssheetorderstructure set createdbyusername = 'Administrator' where createdbyusername is null;



update lsaudittrailconfiguration set modulename = 'IDS_MDL_ORDERS' where ordersequnce IN (2,1);
update lsaudittrailconfiguration set modulename = 'IDS_MDL_MASTERS' where ordersequnce=6;
update lsaudittrailconfiguration set modulename = 'IDS_MDL_SETUP' where ordersequnce=7;
update lsaudittrailconfiguration set modulename = 'IDS_MDL_AUDITTRAIL' where ordersequnce=10;
update lsaudittrailconfiguration set modulename = 'IDS_MDL_TEMPLATES' where ordersequnce IN(4,5,3);
update lsaudittrailconfiguration set modulename = 'IDS_MDL_REPORTS' where ordersequnce=11;
update lsaudittrailconfiguration set modulename = 'IDS_MDL_PARSER' where ordersequnce=12;

update lsaudittrailconfigmaster set modulename = 'IDS_MDL_ORDERS' where ordersequnce IN (2,1);
update lsaudittrailconfigmaster set modulename = 'IDS_MDL_MASTERS' where ordersequnce=6;
update lsaudittrailconfigmaster set modulename = 'IDS_MDL_SETUP' where ordersequnce=7;
update lsaudittrailconfigmaster set modulename = 'IDS_MDL_AUDITTRAIL' where ordersequnce=10;
update lsaudittrailconfigmaster set modulename = 'IDS_MDL_TEMPLATES' where ordersequnce IN(4,5,3);
update lsaudittrailconfigmaster set modulename = 'IDS_MDL_REPORTS' where ordersequnce=11;
update lsaudittrailconfigmaster set modulename = 'IDS_MDL_PARSER' where ordersequnce=12;

update lsaudittrailconfigmaster set screenname='IDS_SCN_SHEETORDERS' where screenname='Register Orders & Execute';
update lsaudittrailconfigmaster set screenname='IDS_SCN_SHEETTEMPLATES' where screenname='Sheet Creation';
update lsaudittrailconfigmaster set screenname='IDS_SCN_TASKMASTER' where screenname='Test Master';
update lsaudittrailconfigmaster set screenname='IDS_SCN_PROJECTMASTER' where screenname='Project Master';
update lsaudittrailconfigmaster set screenname='IDS_SCN_SAMPLEMASTER' where screenname='Sample Master';
update lsaudittrailconfigmaster set screenname='IDS_SCN_INSTRUMENTMASTER' where screenname='Instrument Master';
update lsaudittrailconfigmaster set screenname='IDS_SCN_USERGROUP' where screenname='User Group';
update lsaudittrailconfigmaster set screenname='IDS_SCN_USERMASTER' where screenname='User Master';
update lsaudittrailconfigmaster set screenname='IDS_SCN_USERRIGHTS' where screenname='User Rights';
update lsaudittrailconfigmaster set screenname='IDS_SCN_PROJECTTEAM' where screenname='Project Team';
update lsaudittrailconfigmaster set screenname='IDS_SCN_ORDERWORKFLOW' where screenname='Order Workflow';
update lsaudittrailconfigmaster set screenname='IDS_SCN_SHEETWORKFLOW' where screenname='Sheet Workflow';
update lsaudittrailconfigmaster set screenname='IDS_SCN_DOMAIN' where screenname='Domain';
update lsaudittrailconfigmaster set screenname='IDS_SCN_PASSWORDPOLICY' where screenname='Password Policy';
update lsaudittrailconfigmaster set screenname='IDS_SCN_CHANGEPASSWORD' where screenname='Change Password';
update lsaudittrailconfigmaster set screenname='IDS_SCN_AUDITTRAILHIS' where screenname='Audit Trail History';
update lsaudittrailconfigmaster set screenname='IDS_SCN_CFRSETTINGS' where screenname='CFR Settings';
update lsaudittrailconfigmaster set screenname='IDS_SCN_PROTOCOLORDERS' where screenname='Protocol Order And Register';
update lsaudittrailconfigmaster set screenname='IDS_SCN_PROTOCOLTEMP' where screenname='Protocols';
update lsaudittrailconfigmaster set screenname='IDS_SCN_TEMPLATEMAPPING' where screenname='Sheet Setting';
update lsaudittrailconfigmaster set screenname='IDS_SCN_REPOSITORY' where screenname='Repository';
update lsaudittrailconfigmaster set screenname='IDS_SCN_INVENTORY' where screenname='Inventory';
update lsaudittrailconfigmaster set screenname='IDS_SCN_SITEMASTER' where screenname='Site Master';
update lsaudittrailconfigmaster set screenname='IDS_SCN_AUDITTRAILCONFIG' where screenname='Audit Trial Configuration';
update lsaudittrailconfigmaster set screenname='IDS_SCN_DELIMITER' where screenname='Delimiter';

update lsaudittrailconfiguration set screenname='IDS_SCN_SHEETORDERS' WHERE screenname='Register Orders & Execute';
update lsaudittrailconfiguration set screenname='IDS_SCN_PROTOCOLORDERS' WHERE screenname='Protocol Order And Register';
update lsaudittrailconfiguration set screenname='IDS_SCN_INSTRUMENTMASTER' WHERE screenname='Instrument Master';
update lsaudittrailconfiguration set screenname='IDS_SCN_SAMPLEMASTER' WHERE screenname='Sample Master';
update lsaudittrailconfiguration set screenname='IDS_SCN_TASKMASTER' WHERE screenname='Test Master';
update lsaudittrailconfiguration set screenname='IDS_SCN_PROJECTMASTER' WHERE screenname='Project Master';
update lsaudittrailconfiguration set screenname='IDS_SCN_INVENTORY' WHERE screenname='Inventory';
update lsaudittrailconfiguration set screenname='IDS_SCN_REPOSITORY' WHERE screenname='Repository';
update lsaudittrailconfiguration set screenname='IDS_SCN_USERMASTER' WHERE screenname='User Master';
update lsaudittrailconfiguration set screenname='IDS_SCN_ORDERWORKFLOW' WHERE screenname='Order Workflow';
update lsaudittrailconfiguration set screenname='IDS_SCN_SITEMASTER' WHERE screenname='Site Master';
update lsaudittrailconfiguration set screenname='IDS_SCN_SHEETWORKFLOW' WHERE screenname='Sheet Workflow';
update lsaudittrailconfiguration set screenname='IDS_SCN_USERGROUP' WHERE screenname='User Group';
update lsaudittrailconfiguration set screenname='IDS_SCN_PROJECTTEAM' WHERE screenname='Project Team';
update lsaudittrailconfiguration set screenname='IDS_SCN_USERRIGHTS' WHERE screenname='User Rights';
update lsaudittrailconfiguration set screenname='IDS_SCN_DOMAIN' WHERE screenname='Domain';
update lsaudittrailconfiguration set screenname='IDS_SCN_PASSWORDPOLICY' WHERE screenname='Password Policy';
update lsaudittrailconfiguration set screenname='IDS_SCN_CHANGEPASSWORD' WHERE screenname='Change Password';
update lsaudittrailconfiguration set screenname='IDS_SCN_AUDITTRAILHIS' WHERE screenname='Audit Trail History';
update lsaudittrailconfiguration set screenname='IDS_SCN_CFRSETTINGS' WHERE screenname='CFR Settings';
update lsaudittrailconfiguration set screenname='IDS_SCN_AUDITTRAILCONFIG' WHERE screenname='Audit Trial Configuration';
update lsaudittrailconfiguration set screenname='IDS_SCN_SHEETTEMPLATES' WHERE screenname='Sheet Creation';
update lsaudittrailconfiguration set screenname='IDS_SCN_PROTOCOLTEMP' WHERE screenname='Protocols';
update lsaudittrailconfiguration set screenname='IDS_SCN_REPORTS' WHERE screenname='Reports';
update lsaudittrailconfiguration set screenname='IDS_SCN_TEMPLATEMAPPING' WHERE screenname='Sheet Setting';
update lsaudittrailconfiguration set screenname='IDS_SCN_DELIMITER' WHERE screenname='Delimiter';
update lsaudittrailconfiguration set screenname='IDS_SCN_METHODDELIMITER' WHERE screenname='MethodDelimiter';
update lsaudittrailconfiguration set screenname='IDS_SCN_METHODMASTER' WHERE screenname='MethodMaster';

update lsaudittrailconfigmaster set taskname = 'IDS_TSK_SAVE' where taskname='Save';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_SENDTOLIMS' where taskname= 'Send to Lims';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_PROCESSORDER' where taskname='Process Order';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_REGISTER' where taskname='Register';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_REGPROTOCOL' where taskname='Register Protocol';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_PROCESSPROTOCOL' where taskname='Process Protocol';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_COMPLETETASK' where taskname='Complete task';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_DELETE' where taskname ='Delete';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_ADDNEWGROUP' where taskname='ADD NEW GROUP';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_EDIT' where taskname='Edit';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_ACTDEACT' where taskname ='Active/Deactive';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_ADDUSER' where taskname='ADD USER';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_UNLOCK' where taskname ='Unlock';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_RESETPASSWORD' where taskname='Reset Password';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_SUBMIT' where taskname='Submit';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_REVIEWHIS' where taskname='Review History';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_CREATEARCHIVE' where taskname='Create Archive';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_OPENARCHIVE' where taskname='Open Archive';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_EXPORT' where taskname='Export';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_SAVEAS' where taskname='Save As';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_LOCK' where taskname='Lock';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_ADDPROTOCOL' where taskname='Add Protocol';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_NEWSTEP' where taskname='New Step';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_SHAREWITHTEAM' where taskname='Share with Team';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_PARSEDATA' where taskname='Parse Data';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_IMPORTADS' where taskname='Import ADS';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_CONNECT' where taskname='Connect';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_NEWTEMP' where taskname='New Template';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_GENERATEREPORT' where taskname='Generate Report';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_CONFIGURE' where taskname='Configure';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_EXPORTPDF' where taskname='Export to PDF';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_DIRECTORYSAVE' where taskname='Directory Save';

update lsaudittrailconfiguration set taskname = 'IDS_TSK_SAVE' where taskname='Save';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_SENDTOLIMS' where taskname='Send to Lims';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_PROCESSORDER' where taskname='Process Order';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_REGISTER' where taskname='Register';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_REGPROTOCOL' where taskname='Register Protocol';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_PROCESSPROTOCOL' where taskname='Process Protocol';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_COMPLETETASK' where taskname='Complete task';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_DELETE' where taskname ='Delete';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_ADDNEWGROUP' where taskname='ADD NEW GROUP';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_EDIT' where taskname='Edit';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_ACTDEACT' where taskname ='Active/Deactive';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_ADDUSER' where taskname='ADD USER';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_UNLOCK' where taskname ='Unlock';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_RESETPASSWORD' where taskname='Reset Password';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_SUBMIT' where taskname='Submit';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_REVIEWHIS' where taskname='Review History';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_CREATEARCHIVE' where taskname='Create Archive';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_OPENARCHIVE' where taskname='Open Archive';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_EXPORT' where taskname='Export';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_SAVEAS' where taskname='Save As';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_LOCK' where taskname='Lock';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_ADDPROTOCOL' where taskname='Add Protocol';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_NEWSTEP' where taskname='New Step';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_SHAREWITHTEAM' where taskname='Share with Team';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_PARSEDATA' where taskname='Parse Data';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_IMPORTADS' where taskname='Import ADS';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_CONNECT' where taskname='Connect';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_NEWTEMP' where taskname='New Template';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_GENERATEREPORT' where taskname='Generate Report';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_CONFIGURE' where taskname='Configure';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_DIRECTORYSAVE' where taskname='Directory Save';



ALTER TABLE IF Exists lsordersharedby ADD COLUMN IF NOT EXISTS order_batchcode numeric(17,0);

ALTER TABLE IF Exists lsordersharedby ADD COLUMN IF NOT EXISTS usersharedby_usercode integer;

ALTER TABLE IF Exists lsordersharedby ADD COLUMN IF NOT EXISTS usersharedon_usercode integer;

DO
$do$
declare
  sharebyconst integer :=0;
begin

SELECT count(*) into sharebyconst FROM
information_schema.table_constraints WHERE constraint_name='fkdl41qlwg7jfpm8diekvft2cvs'
AND table_name='lsordersharedby';
 IF sharebyconst =0 THEN
 	ALTER TABLE ONLY lsordersharedby ADD CONSTRAINT  fkdl41qlwg7jfpm8diekvft2cvs FOREIGN KEY (order_batchcode) REFERENCES lslogilablimsorderdetail (batchcode);
	ALTER TABLE ONLY lsordersharedby ADD CONSTRAINT fk7g9lq71nd94juwopfv9o88dun FOREIGN KEY (usersharedby_usercode) REFERENCES lsusermaster(usercode);
	ALTER TABLE ONLY lsordersharedby ADD CONSTRAINT fk4aii6cq1rj2j9bw814b88dacl FOREIGN KEY (usersharedon_usercode) REFERENCES lsusermaster(usercode);
   END IF;
END
$do$;  

UPDATE lsordersharedby set order_batchcode = sharebatchcode, usersharedby_usercode = (select usercode from lsusermaster where username = sharebyusername fetch first 1 rows only), usersharedon_usercode = (select usercode from lsusermaster where username = sharetousername fetch first 1 rows only) where order_batchcode is null and usersharedby_usercode is null and  usersharedby_usercode is null;

ALTER TABLE IF Exists lsordershareto ADD COLUMN IF NOT EXISTS order_batchcode numeric(17,0);

ALTER TABLE IF Exists lsordershareto ADD COLUMN IF NOT EXISTS usersharedby_usercode integer;

ALTER TABLE IF Exists lsordershareto ADD COLUMN IF NOT EXISTS usersharedon_usercode integer;

DO
$do$
declare
  sharetoconst integer :=0;
begin

SELECT count(*) into sharetoconst FROM
information_schema.table_constraints WHERE constraint_name='fkib60igm624s073vdsv2aghwah'
AND table_name='lsordershareto';
 IF sharetoconst =0 THEN
 	ALTER TABLE ONLY lsordershareto ADD CONSTRAINT fkib60igm624s073vdsv2aghwah FOREIGN KEY (order_batchcode) REFERENCES lslogilablimsorderdetail (batchcode);
	ALTER TABLE ONLY lsordershareto ADD CONSTRAINT fkirjadtam9sj7xh4es0th8ojen FOREIGN KEY (usersharedby_usercode) REFERENCES lsusermaster(usercode);
	ALTER TABLE ONLY lsordershareto ADD CONSTRAINT fk53672xvcvim49wg6rp474im2s FOREIGN KEY (usersharedon_usercode) REFERENCES lsusermaster(usercode);
   END IF;
END
$do$;  

UPDATE lsordershareto set order_batchcode = sharebatchcode, usersharedby_usercode = (select usercode from lsusermaster where username = sharebyusername fetch first 1 rows only), usersharedon_usercode = (select usercode from lsusermaster where username = sharetousername fetch first 1 rows only) where order_batchcode is null and usersharedby_usercode is null and  usersharedby_usercode is null;



ALTER TABLE IF Exists lslogilablimsorderdetail ADD COLUMN IF NOT EXISTS viewoption integer;

update lslogilablimsorderdetail set viewoption = 1 where viewoption is null;

ALTER TABLE IF Exists lssheetorderstructure Drop COLUMN IF EXISTS createdby;
ALTER TABLE IF Exists lssheetorderstructure Drop COLUMN IF EXISTS onlytome;
ALTER TABLE IF Exists lssheetorderstructure Drop COLUMN IF EXISTS sitecode;
ALTER TABLE IF Exists lssheetorderstructure Drop COLUMN IF EXISTS tothesite;
ALTER TABLE IF Exists lssheetorderstructure Drop COLUMN IF EXISTS createdbyusername;

ALTER TABLE IF Exists lssheetorderstructure ADD COLUMN IF NOT EXISTS createdby_usercode integer;
ALTER TABLE IF Exists lssheetorderstructure ADD COLUMN IF NOT EXISTS sitemaster_sitecode integer;
ALTER TABLE IF Exists lssheetorderstructure ADD COLUMN IF NOT EXISTS viewoption integer;

DO
$do$
declare
  sheetfoldercreate integer :=0;
begin

SELECT count(*) into sheetfoldercreate FROM
information_schema.table_constraints WHERE constraint_name='fk5wxdhhqis9cwr5m0ejfbh6e47'
AND table_name='lssheetorderstructure';
 IF sheetfoldercreate =0 THEN
 	ALTER TABLE ONLY lssheetorderstructure ADD CONSTRAINT fk5wxdhhqis9cwr5m0ejfbh6e47 FOREIGN KEY (createdby_usercode) REFERENCES lsusermaster (usercode);
	ALTER TABLE ONLY lssheetorderstructure ADD CONSTRAINT fk9bglp2u8m8ng4ckbeebshmkoc FOREIGN KEY (sitemaster_sitecode) REFERENCES lssitemaster(sitecode);
  END IF;
END
$do$;  

update lssheetorderstructure set createdby_usercode = 1 where createdby_usercode is null;
update lssheetorderstructure set sitemaster_sitecode = 1 where sitemaster_sitecode is null;
update lssheetorderstructure set viewoption = 2 where viewoption is null;


ALTER TABLE IF Exists lslogilabprotocoldetail ADD COLUMN IF NOT EXISTS lsworkflow_workflowcode integer;

DO
$do$
declare
  resultvalues integer :=0;
begin

SELECT count(*) into resultvalues FROM
information_schema.table_constraints WHERE constraint_name='fkg9fa19muc4tvdk8lg6cf9l4b6'
AND table_name='lslogilabprotocoldetail';
 IF resultvalues =0 THEN
 	ALTER TABLE ONLY lslogilabprotocoldetail ADD CONSTRAINT fkg9fa19muc4tvdk8lg6cf9l4b6 FOREIGN KEY (lsworkflow_workflowcode) REFERENCES lsworkflow(workflowcode);
   END IF;
END
$do$; 


ALTER TABLE IF Exists lsprotocolorderworkflowhistory ADD COLUMN IF NOT EXISTS lsworkflow_workflowcode integer;


DO
$do$
declare
  resultvalues integer :=0;
begin

SELECT count(*) into resultvalues FROM
information_schema.table_constraints WHERE constraint_name='fkie1ot3vihwhddieq4rog8vlqk'
AND table_name='lsprotocolorderworkflowhistory';
 IF resultvalues =0 THEN
 	ALTER TABLE ONLY lsprotocolorderworkflowhistory ADD CONSTRAINT fkie1ot3vihwhddieq4rog8vlqk FOREIGN KEY (lsworkflow_workflowcode) REFERENCES lsworkflow(workflowcode);
   END IF;
END
$do$; 


ALTER TABLE IF Exists lsprotocolmaster ADD COLUMN IF NOT EXISTS lssheetworkflow_workflowcode integer;

DO
$do$
declare
  resultvalues integer :=0;
begin

SELECT count(*) into resultvalues FROM
information_schema.table_constraints WHERE constraint_name='fkg6acg5khtmyb897d7rtlfqr9x'
AND table_name='lsprotocolmaster';
 IF resultvalues =0 THEN
 	ALTER TABLE ONLY lsprotocolmaster ADD CONSTRAINT fkg6acg5khtmyb897d7rtlfqr9x FOREIGN KEY (lssheetworkflow_workflowcode) REFERENCES lssheetworkflow(workflowcode);
   END IF;
END
$do$; 


ALTER TABLE IF Exists lsprotocolworkflowhistory ADD COLUMN IF NOT EXISTS lssheetworkflow_workflowcode integer;

DO
$do$
declare
  resultvalues integer :=0;
begin

SELECT count(*) into resultvalues FROM
information_schema.table_constraints WHERE constraint_name='fkn5ab7q19c60m37vkyux34igny'
AND table_name='lsprotocolworkflowhistory';
 IF resultvalues =0 THEN
 	ALTER TABLE ONLY lsprotocolworkflowhistory ADD CONSTRAINT fkn5ab7q19c60m37vkyux34igny FOREIGN KEY (lssheetworkflow_workflowcode) REFERENCES lssheetworkflow(workflowcode);
   END IF;
END
$do$; 

ALTER TABLE IF Exists lsprotocolorderstructure Drop COLUMN IF EXISTS createdby;
ALTER TABLE IF Exists lsprotocolorderstructure Drop COLUMN IF EXISTS onlytome;
ALTER TABLE IF Exists lsprotocolorderstructure Drop COLUMN IF EXISTS sitecode;
ALTER TABLE IF Exists lsprotocolorderstructure Drop COLUMN IF EXISTS tothesite;
ALTER TABLE IF Exists lsprotocolorderstructure Drop COLUMN IF EXISTS createdbyusername;

ALTER TABLE IF Exists lsprotocolorderstructure ADD COLUMN IF NOT EXISTS createdby_usercode integer;
ALTER TABLE IF Exists lsprotocolorderstructure ADD COLUMN IF NOT EXISTS sitemaster_sitecode integer;
ALTER TABLE IF Exists lsprotocolorderstructure ADD COLUMN IF NOT EXISTS viewoption integer;

DO
$do$
declare
  sheetfoldercreate integer :=0;
  protocolfoldercreate integer :=0;
begin

SELECT count(*) into sheetfoldercreate FROM
information_schema.table_constraints WHERE constraint_name='fk1iyo88a9vor9jyr0wcg5ejo6j'
AND table_name='lsprotocolorderstructure';
SELECT count(*) into protocolfoldercreate FROM
information_schema.table_constraints WHERE constraint_name='fkr15dsl9yqwg7ra49pos62g1rb'
AND table_name='lsprotocolorderstructure';
 IF sheetfoldercreate =0 THEN
 	ALTER TABLE ONLY lsprotocolorderstructure ADD CONSTRAINT fk1iyo88a9vor9jyr0wcg5ejo6j FOREIGN KEY (createdby_usercode) REFERENCES lsusermaster (usercode);
  END IF;
   IF protocolfoldercreate =0 THEN
	ALTER TABLE ONLY lsprotocolorderstructure ADD CONSTRAINT fkr15dsl9yqwg7ra49pos62g1rb FOREIGN KEY (sitemaster_sitecode) REFERENCES lssitemaster(sitecode);
  END IF;
END
$do$;  

update lsprotocolorderstructure set createdby_usercode = 1 where createdby_usercode is null;
update lsprotocolorderstructure set sitemaster_sitecode = 1 where sitemaster_sitecode is null;
update lsprotocolorderstructure set viewoption = 2 where viewoption is null;

ALTER TABLE IF Exists lslogilabprotocoldetail ADD COLUMN IF NOT EXISTS viewoption integer;

update lslogilabprotocoldetail set viewoption = 1 where viewoption is null;

update lsusergrouprightsmaster set modulename = 'IDS_MDL_DASHBOARD' where sequenceorder = 1;
update lsusergrouprightsmaster set modulename = 'IDS_MDL_ORDERS' where sequenceorder IN(2,3);
update lsusergrouprightsmaster set modulename = 'IDS_MDL_TEMPLATES' where sequenceorder IN(4,5);
update lsusergrouprightsmaster set modulename = 'IDS_MDL_SETUP' where sequenceorder IN(7,9,8);
update lsusergrouprightsmaster set modulename = 'IDS_MDL_AUDITTRAIL' where sequenceorder=10;
update lsusergrouprightsmaster set modulename = 'IDS_MDL_MASTERS' where sequenceorder IN(6,13);
update lsusergrouprightsmaster set modulename = 'IDS_MDL_REPORTS' where sequenceorder=11;
update lsusergrouprightsmaster set modulename = 'IDS_MDL_PARSER' where sequenceorder=12;

update lsusergrouprights set modulename = 'IDS_MDL_DASHBOARD' where sequenceorder = 1;
update lsusergrouprights set modulename = 'IDS_MDL_ORDERS' where sequenceorder IN(2,3);
update lsusergrouprights set modulename = 'IDS_MDL_TEMPLATES' where sequenceorder IN(4,5);
update lsusergrouprights set modulename = 'IDS_MDL_SETUP' where sequenceorder IN(7,9,8);
update lsusergrouprights set modulename = 'IDS_MDL_AUDITTRAIL' where sequenceorder=10;
update lsusergrouprights set modulename = 'IDS_MDL_MASTERS' where sequenceorder IN(6,13);
update lsusergrouprights set modulename = 'IDS_MDL_REPORTS' where sequenceorder=11;
update lsusergrouprights set modulename = 'IDS_MDL_PARSER' where sequenceorder=12;

update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_PARAMETEROVERVIEW' where displaytopic='Parameter OverView';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_ORDEROVERVIEW' where displaytopic='Order Overview';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_ACTIVITIES' where displaytopic='Activities';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_PARAMETERUSAGE' where displaytopic='Parameter Usage';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_PENDINGWORK' where displaytopic='Pending Work';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_COMPLETEDWORK' where displaytopic='Completed Work';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_LIMSTASKORDER' where displaytopic='LIMS Task Order';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_ELNTASKORDER' where displaytopic='ELN Task Order';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_RESEARCHACTIVITY' where displaytopic='Research Activity Order';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_MANAGEEXCEL' where displaytopic='Manage Excel';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_SHEETEVALUATION' where displaytopic='Sheet Evaluation';
update lsusergrouprightsmaster set displaytopic = 'IDS_SCN_SHEETTEMPLATE' where displaytopic='Sheet Templates';
update lsusergrouprightsmaster set displaytopic = 'IDS_SCN_TASKMASTER' where displaytopic='Task Master';
update lsusergrouprightsmaster set displaytopic = 'IDS_SCN_PROJECTMASTER' where displaytopic='Project Master';
update lsusergrouprightsmaster set displaytopic = 'IDS_SCN_SAMPLEMASTER' where displaytopic='Sample Master';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_INSTRUMENTMASTER' where displaytopic='Instrument Master';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_LIMSTESTORDER' where displaytopic='LIMS Test  Order';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_ELNANDRESEARCH' where displaytopic='ELN & Research Activity Test Order';
update lsusergrouprightsmaster set displaytopic = 'IDS_SCN_USERMASTER' where displaytopic='User Master';
update lsusergrouprightsmaster set displaytopic = 'IDS_SCN_USERGROUP' where displaytopic='User Group';
update lsusergrouprightsmaster set displaytopic = 'IDS_SCN_USERRIGHTS' where displaytopic='User Rights';
update lsusergrouprightsmaster set displaytopic = 'IDS_SCN_PROJECTTEAM' where displaytopic='Project Team';
update lsusergrouprightsmaster set displaytopic = 'IDS_SCN_ORDERWORKLOW' where displaytopic='Order Workflow';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_ACTIVEUSER' where displaytopic='Active User';
update lsusergrouprightsmaster set displaytopic = 'IDS_SCN_SHEETWORKFLOW' where displaytopic='Sheet Workflow';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_DOMAIN' where displaytopic='Domain';
update lsusergrouprightsmaster set displaytopic = 'IDS_SCN_PASSWORDPOLICY' where displaytopic='Password Policy';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_ACTDEACT' where displaytopic='ACTIVATE/DEACTIVATE';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_UNLOCK' where displaytopic='Unlock';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_RESETPASSWORD' where displaytopic='Reset Password';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_IMPORTADS' where displaytopic='Import ADS';
update lsusergrouprightsmaster set displaytopic = 'IDS_SCN_AUDITTRAILHISTORY' where displaytopic='Audit trail history';
update lsusergrouprightsmaster set displaytopic = 'IDS_SCN_CFRSETTINGS' where displaytopic='CFR Settings';
update lsusergrouprightsmaster set displaytopic = 'IDS_SCN_AUDITTRAILCONFIG' where displaytopic='Audit trail configuration';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_REVIEWHISTORY' where displaytopic='Review History';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_REVIEW' where displaytopic='Review';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_CREATEARCHIVE' where displaytopic='Create Archive';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_OPENARCHIVE' where displaytopic='Open Archive';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_EXPORT' where displaytopic='Export';
update lsusergrouprightsmaster set displaytopic = 'IDS_SCN_REPORT' where displaytopic='Reports';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_GENERATEREPORT' where displaytopic='Generate Reports';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_NEWTEMP' where displaytopic='New Template';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_ELNPROTOCOL' where displaytopic='ELN Protocol Order';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_DYNAMICPROTOCOL' where displaytopic='Dynamic Protocol Order';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_NEWSTEP' where displaytopic='New Step';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_UNLOCKORDERS' where displaytopic='Unlock Orders';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_ORDERSHAREDBYME' where displaytopic='Orders Shared By Me';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_ORDERSHAREDTOME' where displaytopic='Orders Shared To Me';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_TEMPLATESHAREDBYME' where displaytopic='Templates Shared By Me';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_TEMPLATESHAREDTOME' where displaytopic='Templates Shared To Me';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_EXPORTPDF' where displaytopic='Export to pdf';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_SHEET' where displaytopic='Sheet';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_PROTOCOL' where displaytopic='Protocol';
update lsusergrouprightsmaster set displaytopic = 'IDS_SCN_TEMPLATEMAPPING' where displaytopic='Sheet Setting';
update lsusergrouprightsmaster set displaytopic = 'IDS_SCN_INVENTORY' where displaytopic='Inventory';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_RETIRE' where displaytopic='Retire';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_NEWDOCUMENT' where displaytopic='New Document';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_INSTRUMENTCATEGORY' where displaytopic='Instrument Category';
update lsusergrouprightsmaster set displaytopic = 'IDS_SCN_PARSER' where displaytopic='Parser';
update lsusergrouprightsmaster set displaytopic = 'IDS_SCN_DELIMITER' where displaytopic='Delimiters';
update lsusergrouprightsmaster set displaytopic = 'IDS_SCN_METHODDELIMITER' where displaytopic='Method Delimiters';
update lsusergrouprightsmaster set displaytopic = 'IDS_SCN_METHODMASTER' where displaytopic='Method Master';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_ADDREPO' where displaytopic='Add repository';
update lsusergrouprightsmaster set displaytopic = 'IDS_TSK_EDITREPO' where displaytopic='Edit repository';
update lsusergrouprightsmaster set displaytopic = 'IDS_SCN_PROTOCOLTEMPLATE' where displaytopic='Protocol Templates';
update lsusergrouprightsmaster set displaytopic='IDS_TSK_ASSIGNEDORDERS' where displaytopic='Assigned Orders';
update lsusergrouprightsmaster set displaytopic='IDS_TSK_MYORDERS' where displaytopic='My Orders';
update lsusergrouprightsmaster set modulename='IDS_MDL_ORDERS' where modulename='Register Task Orders & Execute';
update lsusergrouprightsmaster set displaytopic='IDS_TSK_ORDERSHAREDBYMEPROTOCOL' where orderno=80;
update lsusergrouprightsmaster set displaytopic='IDS_TSK_ORDERSHAREDTOMEPROTOCOL' where orderno=81;
update lsusergrouprightsmaster set displaytopic='IDS_TSK_PENDINGWORKPROTOCOL' where orderno=46;
update lsusergrouprightsmaster set displaytopic='IDS_TSK_COMPLETEDWORK' where orderno=47;
update lsusergrouprightsmaster set displaytopic='IDS_TSK_ACTDEACTUSERMASTER' where orderno=22;
update lsusergrouprightsmaster set displaytopic='IDS_SCN_PROTOCOLTEMPLATE' where orderno=51;
update lsusergrouprightsmaster set displaytopic='IDS_SCN_INSTRUMENTMASTER' where displaytopic='IDS_TSK_INSTRUMENTMASTER';

update lsusergrouprightsmaster set displaytopic = 'IDS_SCN_REPORTS' where displaytopic='IDS_SCN_REPORT';
update lsusergrouprightsmaster set displaytopic='IDS_TSK_COMPLETEDWORKPROTOCOL' where orderno=47;

update lsusergrouprights set displaytopic = 'IDS_TSK_UNLOCKORDERS' where displaytopic='Unlock Orders';
update lsusergrouprights set displaytopic = 'IDS_TSK_PARAMETERUSAGE' where displaytopic='Parameter OverView';
update lsusergrouprights set displaytopic = 'IDS_TSK_ORDEROVERVIEW' where displaytopic='Order Overview';
update lsusergrouprights set displaytopic = 'IDS_TSK_ACTIVITIES' where displaytopic='Activities';
update lsusergrouprights set displaytopic = 'IDS_TSK_PARAMETERUSAGE' where displaytopic='Parameter Usage';
update lsusergrouprights set displaytopic = 'IDS_TSK_LIMSTASKORDER' where displaytopic='LIMS Task Order';
update lsusergrouprights set displaytopic = 'IDS_TSK_MANAGEEXCEL' where displaytopic='Manage Excel';
update lsusergrouprights set displaytopic = 'IDS_TSK_ORDERSHAREDBYME' where displaytopic='Orders Shared By Me';
update lsusergrouprights set displaytopic = 'IDS_TSK_ORDERSHAREDTOME' where displaytopic='Orders Shared To Me';
update lsusergrouprights set displaytopic = 'IDS_TSK_PENDINGWORK' where displaytopic='Pending Work';
update lsusergrouprights set displaytopic = 'IDS_TSK_COMPLETEDWORK' where displaytopic='Completed Work';
update lsusergrouprights set displaytopic = 'IDS_TSK_SHEETEVALUATION' where displaytopic='Sheet Evaluation';
update lsusergrouprights set displaytopic = 'IDS_TSK_ELNTASKORDER' where displaytopic='ELN Task Order';
update lsusergrouprights set displaytopic = 'IDS_TSK_RESEARCHACTIVITY' where displaytopic='Research Activity Order';
update lsusergrouprights set displaytopic = 'IDS_TSK_ELNPROTOCOL' where displaytopic='ELN Protocol Order';
update lsusergrouprights set displaytopic = 'IDS_TSK_DYNAMICPROTOCOL' where displaytopic='Dynamic Protocol Order';
update lsusergrouprights set displaytopic = 'IDS_TSK_NEWSTEP' where displaytopic='New Step';
update lsusergrouprights set displaytopic = 'IDS_SCN_SHEETTEMPLATE' where displaytopic='Sheet Templates';
update lsusergrouprights set displaytopic = 'IDS_TSK_ELNANDRESEARCH' where displaytopic='ELN & Research Activity Test Order';
update lsusergrouprights set displaytopic = 'IDS_TSK_LIMSTESTORDER' where displaytopic='LIMS Test  Order';
update lsusergrouprights set displaytopic = 'IDS_SCN_PROJECTMASTER' where displaytopic='Project Master';
update lsusergrouprights set displaytopic = 'IDS_SCN_SAMPLEMASTER' where displaytopic='Sample Master';
update lsusergrouprights set displaytopic = 'IDS_SCN_TASKMASTER' where displaytopic='Task Master';
update lsusergrouprights set displaytopic = 'IDS_SCN_USERMASTER' where displaytopic='User Master';
update lsusergrouprights set displaytopic = 'IDS_SCN_USERRIGHTS' where displaytopic='User Rights';
update lsusergrouprights set displaytopic = 'IDS_SCN_PROJECTTEAM' where displaytopic='Project Team';
update lsusergrouprights set displaytopic = 'IDS_SCN_ORDERWORKLOW' where displaytopic='Order Workflow';
update lsusergrouprights set displaytopic = 'IDS_TSK_ACTIVEUSER' where displaytopic='Active User';
update lsusergrouprights set displaytopic = 'IDS_SCN_SHEETWORKFLOW' where displaytopic='Sheet Workflow';
update lsusergrouprights set displaytopic = 'IDS_TSK_DOMAIN' where displaytopic='Domain';
update lsusergrouprights set displaytopic = 'IDS_SCN_PASSWORDPOLICY' where displaytopic='Password Policy';
update lsusergrouprights set displaytopic = 'IDS_SCN_USERGROUP' where displaytopic='User Group';
update lsusergrouprights set displaytopic = 'IDS_SCN_SITEMASTER' where displaytopic='Site Master';
update lsusergrouprights set displaytopic = 'IDS_TSK_ACTDEACT' where displaytopic='ACTIVATE/DEACTIVATE';
update lsusergrouprights set displaytopic = 'IDS_TSK_UNLOCK' where displaytopic='Unlock';
update lsusergrouprights set displaytopic = 'IDS_TSK_RESETPASSWORD' where displaytopic='Reset Password';
update lsusergrouprights set displaytopic = 'IDS_TSK_IMPORTADS' where displaytopic='Import ADS';
update lsusergrouprights set displaytopic = 'IDS_TSK_RETIRE' where displaytopic='Retire';
update lsusergrouprights set displaytopic = 'IDS_SCN_CFRSETTINGS' where displaytopic='CFR Settings';
update lsusergrouprights set displaytopic = 'IDS_TSK_REVIEWHISTORY' where displaytopic='Review History';
update lsusergrouprights set displaytopic = 'IDS_TSK_OPENARCHIVE' where displaytopic='Open Archive';
update lsusergrouprights set displaytopic = 'IDS_TSK_EXPORT' where displaytopic='Export';
update lsusergrouprights set displaytopic = 'IDS_SCN_AUDITTRAILCONFIG' where displaytopic='Audit trail configuration';
update lsusergrouprights set displaytopic = 'IDS_SCN_AUDITTRAILHISTORY' where displaytopic='Audit trail history';
update lsusergrouprights set displaytopic = 'IDS_TSK_REVIEW' where displaytopic='Review';
update lsusergrouprights set displaytopic = 'IDS_TSK_GENERATEREPORT' where displaytopic='Generate Reports';
update lsusergrouprights set displaytopic = 'IDS_TSK_TEMPLATEDESIGN' where displaytopic='Template Designing';
update lsusergrouprights set displaytopic = 'IDS_TSK_LIMSTASKORDER' where displaytopic='LIMS Task Order';
update lsusergrouprights set displaytopic = 'IDS_SCN_SHEETTEMPLATE' where displaytopic='Sheet Templates';
update lsusergrouprights set displaytopic = 'IDS_TSK_EXPORTPDF' where displaytopic='Export to pdf';
update lsusergrouprights set displaytopic = 'IDS_TSK_PROTOCOL' where displaytopic='Protocol';
update lsusergrouprights set displaytopic = 'IDS_SCN_REPORTS' where displaytopic='Reports';
update lsusergrouprights set displaytopic = 'IDS_TSK_TEMPLATESHAREDBYME' where displaytopic='Templates Shared By Me';
update lsusergrouprights set displaytopic = 'IDS_TSK_TEMPLATESHAREDTOME' where displaytopic='Templates Shared To Me';
update lsusergrouprights set displaytopic = 'IDS_TSK_SHEET' where displaytopic='Sheet';
update lsusergrouprights set displaytopic = 'IDS_SCN_METHODDELIMITER' where displaytopic='Method Delimiters';
update lsusergrouprights set displaytopic = 'IDS_SCN_METHODMASTER' where displaytopic='Method Master';
update lsusergrouprights set displaytopic = 'IDS_TSK_INSTRUMENTCATEGORY' where displaytopic='Instrument Category';
update lsusergrouprights set displaytopic = 'IDS_SCN_DELIMITER' where displaytopic='Delimiters';
update lsusergrouprights set displaytopic = 'IDS_SCN_PARSER' where displaytopic='Parser';
update lsusergrouprights set displaytopic = 'IDS_TSK_INSTRUMENTMASTER' where displaytopic='Instrument Master';
update lsusergrouprights set displaytopic = 'IDS_TSK_ADDREPO' where displaytopic='Add repository';
update lsusergrouprights set displaytopic = 'IDS_TSK_EDITREPO' where displaytopic='Edit repository';
update lsusergrouprights set displaytopic = 'IDS_SCN_TEMPLATEMAPPING' where displaytopic='Sheet Setting';
update lsusergrouprights set displaytopic='IDS_SCN_INSTRUMENTMASTER' where displaytopic='IDS_TSK_INSTRUMENTMASTER';
update lsusergrouprights set displaytopic='IDS_SCN_INSTRUMENTCATEGORY' where displaytopic='IDS_TSK_INSTRUMENTCATEGORY';
update lsusergrouprights set displaytopic = 'IDS_TSK_NEWDOCUMENT' where displaytopic='New Document';
update lsusergrouprights set displaytopic = 'IDS_SCN_REPORTS' where displaytopic='IDS_SCN_REPORT';


update lsaudittrailconfigmaster set screenname='IDS_SCN_SHEETORDERS' where screenname='Register Orders & Execute';
update lsaudittrailconfigmaster set screenname='IDS_SCN_SHEETTEMPLATES' where screenname='Sheet Creation';
update lsaudittrailconfigmaster set screenname='IDS_SCN_TASKMASTER' where screenname='Test Master';
update lsaudittrailconfigmaster set screenname='IDS_SCN_PROJECTMASTER' where screenname='Project Master';
update lsaudittrailconfigmaster set screenname='IDS_SCN_SAMPLEMASTER' where screenname='Sample Master';
update lsaudittrailconfigmaster set screenname='IDS_SCN_INSTRUMENTMASTER' where screenname='Instrument Master';
update lsaudittrailconfigmaster set screenname='IDS_SCN_USERGROUP' where screenname='User Group';
update lsaudittrailconfigmaster set screenname='IDS_SCN_USERMASTER' where screenname='User Master';
update lsaudittrailconfigmaster set screenname='IDS_SCN_USERRIGHTS' where screenname='User Rights';
update lsaudittrailconfigmaster set screenname='IDS_SCN_PROJECTTEAM' where screenname='Project Team';
update lsaudittrailconfigmaster set screenname='IDS_SCN_ORDERWORKFLOW' where screenname='Order Workflow';
update lsaudittrailconfigmaster set screenname='IDS_SCN_SHEETWORKFLOW' where screenname='Sheet Workflow';
update lsaudittrailconfigmaster set screenname='IDS_SCN_DOMAIN' where screenname='Domain';
update lsaudittrailconfigmaster set screenname='IDS_SCN_PASSWORDPOLICY' where screenname='Password Policy';
update lsaudittrailconfigmaster set screenname='IDS_SCN_CHANGEPASSWORD' where screenname='Change Password';
update lsaudittrailconfigmaster set screenname='IDS_SCN_AUDITTRAILHIS' where screenname='Audit Trail History';
update lsaudittrailconfigmaster set screenname='IDS_SCN_CFRSETTINGS' where screenname='CFR Settings';
update lsaudittrailconfigmaster set screenname='IDS_SCN_PROTOCOLORDERS' where screenname='Protocol Order And Register';
update lsaudittrailconfigmaster set screenname='IDS_SCN_PROTOCOLTEMP' where screenname='Protocols';
update lsaudittrailconfigmaster set screenname='IDS_SCN_TEMPLATEMAPPING' where screenname='Sheet Setting';
update lsaudittrailconfigmaster set screenname='IDS_SCN_REPOSITORY' where screenname='Repository';
update lsaudittrailconfigmaster set screenname='IDS_SCN_INVENTORY' where screenname='Inventory';
update lsaudittrailconfigmaster set screenname='IDS_SCN_SITEMASTER' where screenname='Site Master';
update lsaudittrailconfigmaster set screenname='IDS_SCN_AUDITTRAILCONFIG' where screenname='Audit Trial Configuration';
update lsaudittrailconfigmaster set screenname='IDS_SCN_DELIMITER' where screenname='Delimiter';

update lsaudittrailconfiguration set screenname='IDS_SCN_SHEETORDERS' WHERE screenname='Register Orders & Execute';
update lsaudittrailconfiguration set screenname='IDS_SCN_PROTOCOLORDERS' WHERE screenname='Protocol Order And Register';
update lsaudittrailconfiguration set screenname='IDS_SCN_INSTRUMENTMASTER' WHERE screenname='Instrument Master';
update lsaudittrailconfiguration set screenname='IDS_SCN_SAMPLEMASTER' WHERE screenname='Sample Master';
update lsaudittrailconfiguration set screenname='IDS_SCN_TASKMASTER' WHERE screenname='Test Master';
update lsaudittrailconfiguration set screenname='IDS_SCN_PROJECTMASTER' WHERE screenname='Project Master';
update lsaudittrailconfiguration set screenname='IDS_SCN_INVENTORY' WHERE screenname='Inventory';
update lsaudittrailconfiguration set screenname='IDS_SCN_REPOSITORY' WHERE screenname='Repository';
update lsaudittrailconfiguration set screenname='IDS_SCN_USERMASTER' WHERE screenname='User Master';
update lsaudittrailconfiguration set screenname='IDS_SCN_ORDERWORKFLOW' WHERE screenname='Order Workflow';
update lsaudittrailconfiguration set screenname='IDS_SCN_SITEMASTER' WHERE screenname='Site Master';
update lsaudittrailconfiguration set screenname='IDS_SCN_SHEETWORKFLOW' WHERE screenname='Sheet Workflow';
update lsaudittrailconfiguration set screenname='IDS_SCN_USERGROUP' WHERE screenname='User Group';
update lsaudittrailconfiguration set screenname='IDS_SCN_PROJECTTEAM' WHERE screenname='Project Team';
update lsaudittrailconfiguration set screenname='IDS_SCN_USERRIGHTS' WHERE screenname='User Rights';
update lsaudittrailconfiguration set screenname='IDS_SCN_DOMAIN' WHERE screenname='Domain';
update lsaudittrailconfiguration set screenname='IDS_SCN_PASSWORDPOLICY' WHERE screenname='Password Policy';
update lsaudittrailconfiguration set screenname='IDS_SCN_CHANGEPASSWORD' WHERE screenname='Change Password';
update lsaudittrailconfiguration set screenname='IDS_SCN_AUDITTRAILHIS' WHERE screenname='Audit Trail History';
update lsaudittrailconfiguration set screenname='IDS_SCN_CFRSETTINGS' WHERE screenname='CFR Settings';
update lsaudittrailconfiguration set screenname='IDS_SCN_AUDITTRAILCONFIG' WHERE screenname='Audit Trial Configuration';
update lsaudittrailconfiguration set screenname='IDS_SCN_SHEETTEMPLATES' WHERE screenname='Sheet Creation';
update lsaudittrailconfiguration set screenname='IDS_SCN_PROTOCOLTEMP' WHERE screenname='Protocols';
update lsaudittrailconfiguration set screenname='IDS_SCN_REPORTS' WHERE screenname='Reports';
update lsaudittrailconfiguration set screenname='IDS_SCN_TEMPLATEMAPPING' WHERE screenname='Sheet Settings';
update lsaudittrailconfiguration set screenname='IDS_SCN_DELIMITER' WHERE screenname='Delimiter';
update lsaudittrailconfiguration set screenname='IDS_SCN_METHODDELIMITER' WHERE screenname='MethodDelimiter';
update lsaudittrailconfiguration set screenname='IDS_SCN_METHODMASTER' WHERE screenname='MethodMaster';

update lsaudittrailconfigmaster set taskname = 'IDS_TSK_SAVE' where taskname='Save';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_SENDTOLIMS' where taskname= 'Send to Lims';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_PROCESSORDER' where taskname='Process Order';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_REGISTER' where taskname='Register';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_REGPROTOCOL' where taskname='Register Protocol';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_PROCESSPROTOCOL' where taskname='Process Protocol';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_COMPLETETASK' where taskname='Complete task';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_DELETE' where taskname ='Delete';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_ADDNEWGROUP' where taskname='ADD NEW GROUP';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_EDIT' where taskname='Edit';

update lsaudittrailconfigmaster set taskname = 'IDS_TSK_ACTDEACT' where taskname ='Active/Deactive';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_ADDUSER' where taskname='ADD USER';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_UNLOCK' where taskname ='Unlock';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_RESETPASSWORD' where taskname='Reset Password';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_SUBMIT' where taskname='Submit';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_REVIEWHIS' where taskname='Review History';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_CREATEARCHIVE' where taskname='Create Archive';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_OPENARCHIVE' where taskname='Open Archive';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_EXPORT' where taskname='Export';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_SAVEAS' where taskname='Save As';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_LOCK' where taskname='Lock';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_ADDPROTOCOL' where taskname='Add Protocol';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_NEWSTEP' where taskname='New Step';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_SHAREWITHTEAM' where taskname='Share with Team';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_PARSEDATA' where taskname='Parse Data';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_IMPORTADS' where taskname='Import ADS';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_CONNECT' where taskname='Connect';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_NEWTEMP' where taskname='New Template';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_GENERATEREPORT' where taskname='Generate Report';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_CONFIGURE' where taskname='Configure';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_EXPORTPDF' where taskname='Export to PDF';
update lsaudittrailconfigmaster set taskname = 'IDS_TSK_DIRECTORYSAVE' where taskname='Directory Save';

update lsaudittrailconfiguration set taskname = 'IDS_TSK_SAVE' where taskname='Save';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_SENDTOLIMS' where taskname='Send to Lims';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_PROCESSORDER' where taskname='Process Order';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_REGISTER' where taskname='Register';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_REGPROTOCOL' where taskname='Register Protocol';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_PROCESSPROTOCOL' where taskname='Process Protocol';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_COMPLETETASK' where taskname='Complete task';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_DELETE' where taskname ='Delete';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_ADDNEWGROUP' where taskname='ADD NEW GROUP';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_EDIT' where taskname='Edit';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_ACTDEACT' where taskname ='Active/Deactive';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_ADDUSER' where taskname='ADD USER';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_UNLOCK' where taskname ='Unlock';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_RESETPASSWORD' where taskname='Reset Password';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_SUBMIT' where taskname='Submit';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_REVIEWHIS' where taskname='Review History';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_CREATEARCHIVE' where taskname='Create Archive';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_OPENARCHIVE' where taskname='Open Archive';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_EXPORT' where taskname='Export';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_SAVEAS' where taskname='Save As';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_LOCK' where taskname='Lock';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_ADDPROTOCOL' where taskname='Add Protocol';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_NEWSTEP' where taskname='New Step';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_SHAREWITHTEAM' where taskname='Share with Team';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_PARSEDATA' where taskname='Parse Data';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_IMPORTADS' where taskname='Import ADS';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_CONNECT' where taskname='Connect';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_NEWTEMP' where taskname='New Template';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_GENERATEREPORT' where taskname='Generate Report';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_CONFIGURE' where taskname='Configure';
update lsaudittrailconfiguration set taskname = 'IDS_TSK_DIRECTORYSAVE' where taskname='Directory Save';

update lsaudittrailconfiguration set modulename = 'IDS_MDL_ORDERS' where ordersequnce IN (2,1);
update lsaudittrailconfiguration set modulename = 'IDS_MDL_MASTERS' where ordersequnce=6;
update lsaudittrailconfiguration set modulename = 'IDS_MDL_SETUP' where ordersequnce=7;
update lsaudittrailconfiguration set modulename = 'IDS_MDL_AUDITTRAIL' where ordersequnce=10;
update lsaudittrailconfiguration set modulename = 'IDS_MDL_TEMPLATES' where ordersequnce IN(4,5,3);
update lsaudittrailconfiguration set modulename = 'IDS_MDL_REPORTS' where ordersequnce=11;
update lsaudittrailconfiguration set modulename = 'IDS_MDL_PARSER' where ordersequnce=12;

update lsaudittrailconfigmaster set modulename = 'IDS_MDL_ORDERS' where ordersequnce IN (2,1);
update lsaudittrailconfigmaster set modulename = 'IDS_MDL_MASTERS' where ordersequnce=6;
update lsaudittrailconfigmaster set modulename = 'IDS_MDL_SETUP' where ordersequnce=7;
update lsaudittrailconfigmaster set modulename = 'IDS_MDL_AUDITTRAIL' where ordersequnce=10;
update lsaudittrailconfigmaster set modulename = 'IDS_MDL_TEMPLATES' where ordersequnce IN(4,5,3);
update lsaudittrailconfigmaster set modulename = 'IDS_MDL_REPORTS' where ordersequnce=11;
update lsaudittrailconfigmaster set modulename = 'IDS_MDL_PARSER' where ordersequnce=12;

update lsusergrouprights set displaytopic = 'IDS_SCN_UNLOCKORDERS' where displaytopic='IDS_TSK_UNLOCKORDERS';
update lsusergrouprightsmaster set displaytopic = 'IDS_SCN_UNLOCKORDERS' where displaytopic='IDS_TSK_UNLOCKORDERS';

INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder) VALUES (82, 'IDS_SCN_TEMPLATEMAPPING', 'IDS_MDL_TEMPLATES', '0', 'NA', 'NA', 'NA', '1,0,0',4) ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder) VALUES (83, 'IDS_TSK_PROTOCOLTEMPSHARETOME', 'IDS_MDL_TEMPLATES', '0', 'NA', 'NA', 'NA', '0,0,1',4) ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder) VALUES (84, 'IDS_TSK_PROTOCOLTEMPSHAREBYME', 'IDS_MDL_TEMPLATES', '0', 'NA', 'NA', 'NA', '0,0,1',4) ON CONFLICT(orderno)DO NOTHING;

ALTER TABLE IF Exists lsusergrouprightsmaster ADD COLUMN IF NOT EXISTS screenname character varying(255);
ALTER TABLE IF Exists lsusergrouprights ADD COLUMN IF NOT EXISTS screenname character varying(100);

update lsusergrouprightsmaster set screenname='IDS_SCN_DASHBOARD' where orderno in (1,2,3,4);
update lsusergrouprightsmaster set screenname='IDS_SCN_SHEETORDERS' where orderno in (5,6,7,8,9,37,38,61,62);
update lsusergrouprightsmaster set screenname='IDS_SCN_PROTOCOLORDERS' where orderno in (46,47,48,49,80,81);
update lsusergrouprightsmaster set screenname='IDS_SCN_SHEETTEMPLATE' where orderno in (10,69,70);
update lsusergrouprightsmaster set screenname='IDS_SCN_PROTOCOLTEMPLATE' where orderno in (50,51,57,83,84);
update lsusergrouprightsmaster set screenname='IDS_SCN_TEMPLATEMAPPING' where orderno in (14,15,71,72,82);
update lsusergrouprightsmaster set screenname='IDS_SCN_TASKMASTER' where orderno=11;
update lsusergrouprightsmaster set screenname='IDS_SCN_PROJECTMASTER' where orderno=12;
update lsusergrouprightsmaster set screenname='IDS_SCN_SAMPLEMASTER' where orderno=13;
update lsusergrouprightsmaster set screenname='IDS_SCN_INVENTORY' where orderno in (65,66,67);
update lsusergrouprightsmaster set screenname='IDS_SCN_UNLOCKORDERS' where orderno=78;
update lsusergrouprightsmaster set screenname='IDS_SCN_USERGROUP' where orderno in(17,21);
update lsusergrouprightsmaster set screenname='IDS_SCN_USERMASTER' where orderno in(16,52,23,24,40,22);
update lsusergrouprightsmaster set screenname='IDS_SCN_ACTIVEUSER' where orderno=44;
update lsusergrouprightsmaster set screenname='IDS_SCN_PASSWORDPOLICY' where orderno=43;
update lsusergrouprightsmaster set screenname='IDS_SCN_PROJECTTEAM' where orderno=19;
update lsusergrouprightsmaster set screenname='IDS_SCN_AUDITTRAILHIS' where orderno in(25,28,29,30,31,32);
update lsusergrouprightsmaster set screenname='IDS_SCN_DOMAIN' where orderno=42;
update lsusergrouprightsmaster set screenname='IDS_SCN_USERRIGHTS' where orderno=18;
update lsusergrouprightsmaster set screenname='IDS_SCN_CFRSETTINGS' where orderno=26;
update lsusergrouprightsmaster set screenname='IDS_SCN_AUDITTRAILCONFIG' where orderno=27;
update lsusergrouprightsmaster set screenname='IDS_SCN_REPORTS' where orderno in (34,35,45,58);
update lsusergrouprightsmaster set screenname='IDS_SCN_METHODMASTER' where orderno=56;
update lsusergrouprightsmaster set screenname='IDS_SCN_PARSER' where orderno=53;
update lsusergrouprightsmaster set screenname='IDS_SCN_DELIMITER' where orderno=54;
update lsusergrouprightsmaster set screenname='IDS_SCN_METHODDELIMITER' where orderno=55;
update lsusergrouprightsmaster set screenname='IDS_SCN_INSTRUMENTMASTER' where orderno=39;
update lsusergrouprightsmaster set screenname='IDS_SCN_INSTRUMENTCATEGORY' where orderno=68;
update lsusergrouprightsmaster set screenname='IDS_SCN_TEMPLATEWORKFLOW' where orderno=41;
update lsusergrouprightsmaster set screenname='IDS_SCN_ORDERWORKLOW' where orderno=20;
update lsusergrouprightsmaster set sequenceorder=6 where orderno in (66,67);

update lsusergrouprightsmaster set status='1,0,0' where orderno=66;
update lsusergrouprightsmaster set sallow='0',screate='NA',sdelete='NA',sedit='NA' where orderno=22;

update lsusergrouprightsmaster set displaytopic='IDS_SCN_TEMPLATEWORKFLOW' where displaytopic='IDS_SCN_SHEETWORKFLOW';
update lsusergrouprights set displaytopic='IDS_SCN_TEMPLATEWORKFLOW' where displaytopic='IDS_SCN_SHEETWORKFLOW';
update lsusergrouprightsmaster set displaytopic='IDS_SCN_ACTIVEUSER' where displaytopic='IDS_TSK_ACTIVEUSER';
update lsusergrouprights set displaytopic='IDS_SCN_ACTIVEUSER' where displaytopic='IDS_TSK_ACTIVEUSER';
update lsusergrouprightsmaster set displaytopic='IDS_SCN_DOMAIN' where displaytopic='IDS_TSK_DOMAIN';
update lsusergrouprights set displaytopic='IDS_SCN_DOMAIN' where displaytopic='IDS_TSK_DOMAIN';

update lsusergrouprightsmaster set sequenceorder=7 where modulename='IDS_MDL_SETUP';
update lsusergrouprights set sequenceorder=7 where modulename='IDS_MDL_SETUP';
update lsusergrouprights set displaytopic ='IDS_TSK_CREATEARCHIVE' where displaytopic='Create Archive';
update lsusergrouprights set displaytopic='IDS_SCN_AUDITTRAILHIS' where displaytopic='IDS_SCN_AUDITTRAILHISTORY';
update lsusergrouprightsmaster set displaytopic='IDS_SCN_AUDITTRAILHIS' where displaytopic='IDS_SCN_AUDITTRAILHISTORY';

update lsusergrouprightsmaster set displaytopic='IDS_TSK_TEMPLATEOVERVIEW' where displaytopic='IDS_TSK_PARAMETEROVERVIEW';
update lsusergrouprightsmaster set displaytopic='IDS_SCN_DASHBOARD' where displaytopic='IDS_TSK_PARAMETERUSAGE';
update lsusergrouprights set displaytopic='IDS_TSK_TEMPLATEOVERVIEW' where displaytopic='IDS_TSK_PARAMETEROVERVIEW';
update lsusergrouprights set displaytopic='IDS_SCN_DASHBOARD' where displaytopic='IDS_TSK_PARAMETERUSAGE';

delete from lsusergrouprightsmaster where orderno=4;
delete from lsusergrouprightsmaster where orderno=42;
delete from lsusergrouprightsmaster where orderno=44;


INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (85, 'IDS_TSK_FOLDERCREATION', 'IDS_MDL_ORDERS', '0', 'NA', 'NA', 'NA', '1,0,0',2,'IDS_SCN_SHEETORDERS') ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (86, 'IDS_TSK_FOLDERCREATIONPROTOCOL', 'IDS_MDL_ORDERS', '0', 'NA', 'NA', 'NA', '1,0,0',3,'IDS_SCN_PROTOCOLORDERS') ON CONFLICT(orderno)DO NOTHING;

update lsusergrouprightsmaster set sequenceorder=1 where orderno=2;
update lsusergrouprightsmaster set sequenceorder=2 where orderno=1;
update lsusergrouprightsmaster set sequenceorder=3 where orderno=3;
update lsusergrouprightsmaster set sequenceorder=4 where orderno=7;
update lsusergrouprightsmaster set sequenceorder=5 where orderno=85;
update lsusergrouprightsmaster set sequenceorder=6 where orderno=8;
update lsusergrouprightsmaster set sequenceorder=7 where orderno=9;
update lsusergrouprightsmaster set sequenceorder=8 where orderno=37;
update lsusergrouprightsmaster set sequenceorder=9 where orderno=38;
update lsusergrouprightsmaster set sequenceorder=10 where orderno=61;
update lsusergrouprightsmaster set sequenceorder=11 where orderno=62;
update lsusergrouprightsmaster set sequenceorder=12 where orderno=5;
update lsusergrouprightsmaster set sequenceorder=13 where orderno=6;
update lsusergrouprightsmaster set sequenceorder=14 where orderno=86;
update lsusergrouprightsmaster set sequenceorder=15 where orderno=48;
update lsusergrouprightsmaster set sequenceorder=16 where orderno=49;
update lsusergrouprightsmaster set sequenceorder=17 where orderno=80;
update lsusergrouprightsmaster set sequenceorder=18 where orderno=81;
update lsusergrouprightsmaster set sequenceorder=19 where orderno=46;
update lsusergrouprightsmaster set sequenceorder=20 where orderno=47;
update lsusergrouprightsmaster set sequenceorder=21 where orderno=10;
update lsusergrouprightsmaster set sequenceorder=22 where orderno=69;
update lsusergrouprightsmaster set sequenceorder=23 where orderno=70;

update lsusergrouprightsmaster set sequenceorder=24 where orderno=51;
update lsusergrouprightsmaster set sequenceorder=25 where orderno=50;
update lsusergrouprightsmaster set sequenceorder=26 where orderno=57;
update lsusergrouprightsmaster set sequenceorder=27 where orderno=84;
update lsusergrouprightsmaster set sequenceorder=28 where orderno=83;

update lsusergrouprightsmaster set sequenceorder=29 where orderno=82;
update lsusergrouprightsmaster set sequenceorder=30 where orderno=71;
update lsusergrouprightsmaster set sequenceorder=31 where orderno=72;
update lsusergrouprightsmaster set sequenceorder=32 where orderno=15;
update lsusergrouprightsmaster set sequenceorder=33 where orderno=14;

update lsusergrouprightsmaster set sequenceorder=34 where orderno=11;
update lsusergrouprightsmaster set sequenceorder=35 where orderno=12;
update lsusergrouprightsmaster set sequenceorder=36 where orderno=13;

update lsusergrouprightsmaster set sequenceorder=37 where orderno=65;
update lsusergrouprightsmaster set sequenceorder=38 where orderno=66;
update lsusergrouprightsmaster set sequenceorder=39 where orderno=67;

update lsusergrouprightsmaster set sequenceorder=40 where orderno=78;

update lsusergrouprightsmaster set sequenceorder=41 where orderno=17;
update lsusergrouprightsmaster set sequenceorder=42 where orderno=21;

update lsusergrouprightsmaster set sequenceorder=43 where orderno=16;
update lsusergrouprightsmaster set sequenceorder=44 where orderno=23;
update lsusergrouprightsmaster set sequenceorder=45 where orderno=22;
update lsusergrouprightsmaster set sequenceorder=46 where orderno=24;
update lsusergrouprightsmaster set sequenceorder=47 where orderno=52;
update lsusergrouprightsmaster set sequenceorder=48 where orderno=40;

update lsusergrouprightsmaster set sequenceorder=49 where orderno=18;

update lsusergrouprightsmaster set sequenceorder=50 where orderno=19;

update lsusergrouprightsmaster set sequenceorder=51 where orderno=20;

update lsusergrouprightsmaster set sequenceorder=52 where orderno=41;

update lsusergrouprightsmaster set sequenceorder=53 where orderno=43;

update lsusergrouprightsmaster set sequenceorder=54 where orderno=25;
update lsusergrouprightsmaster set sequenceorder=55 where orderno=28;
update lsusergrouprightsmaster set sequenceorder=56 where orderno=29;
update lsusergrouprightsmaster set sequenceorder=57 where orderno=30;
update lsusergrouprightsmaster set sequenceorder=58 where orderno=31;
update lsusergrouprightsmaster set sequenceorder=59 where orderno=32;

update lsusergrouprightsmaster set sequenceorder=60 where orderno=26;
update lsusergrouprightsmaster set sequenceorder=61 where orderno=27;

update lsusergrouprightsmaster set sequenceorder=62 where orderno=34;
update lsusergrouprightsmaster set sequenceorder=63 where orderno=58;
update lsusergrouprightsmaster set sequenceorder=64 where orderno=45;
update lsusergrouprightsmaster set sequenceorder=65 where orderno=35;

update lsusergrouprightsmaster set sequenceorder=66 where orderno=53;
update lsusergrouprightsmaster set sequenceorder=67 where orderno=68;
update lsusergrouprightsmaster set sequenceorder=68 where orderno=39;
update lsusergrouprightsmaster set sequenceorder=69 where orderno=54;
update lsusergrouprightsmaster set sequenceorder=70 where orderno=55;
update lsusergrouprightsmaster set sequenceorder=71 where orderno=56;

update lsusergrouprightsmaster set modulename = 'IDS_MDL_DASHBOARD' where sequenceorder IN (1,2,3);
update lsusergrouprightsmaster set modulename = 'IDS_MDL_ORDERS' where sequenceorder IN(4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20);
update lsusergrouprightsmaster set modulename = 'IDS_MDL_TEMPLATES' where sequenceorder IN(21,22,23,24,25,26,27,28,29,30,31,32,33);
update lsusergrouprightsmaster set modulename = 'IDS_MDL_MASTERS' where sequenceorder IN(34,35,36,37,38,39,40);

update lsaudittrailconfigmaster set screenname='IDS_SCN_TEMPLATEWORKFLOW'  where screenname='IDS_SCN_SHEETWORKFLOW';
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (87, 'IDS_TSK_DASHBOARDINVENTORY', 'IDS_MDL_DASHBOARD', '0', 'NA', 'NA', 'NA', '0,0,0',4,'IDS_SCN_DASHBOARD') ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (88, 'IDS_TSK_SHEETTEMPEXPORT', 'IDS_MDL_TEMPLATES', '0', 'NA', 'NA', 'NA', '0,0,0',22,'IDS_SCN_SHEETTEMPLATE') ON CONFLICT(orderno)DO NOTHING;

update lsusergrouprightsmaster set sdelete='NA' where displaytopic='IDS_SCN_PROTOCOLTEMPLATE';
update lsusergrouprightsmaster set status='1,1,0' where displaytopic='IDS_SCN_PROTOCOLTEMPLATE';
update lsusergrouprights set sdelete='NA' where displaytopic='IDS_SCN_PROTOCOLTEMPLATE';

update lsusergrouprightsmaster set sedit='1' where orderno in (11,12,13);
update lsusergrouprights set sedit='1' where displaytopic='IDS_SCN_TASKMASTER';
update lsusergrouprights set sedit='1' where displaytopic='IDS_SCN_PROJECTMASTER';
update lsusergrouprights set sedit='1' where displaytopic='IDS_SCN_SAMPLEMASTER';
update lsusergrouprightsmaster set sdelete='NA' where displaytopic='IDS_SCN_INVENTORY';
update lsusergrouprights set sdelete='NA' where displaytopic='IDS_SCN_INVENTORY';
update lsusergrouprightsmaster set screate='NA' where orderno in (66,67);
update lsusergrouprights set screate='NA' where displaytopic='IDS_TSK_ADDREPO';
update lsusergrouprights set screate='NA' where displaytopic='IDS_TSK_EDITREPO';
update lsusergrouprights set screate='NA', sedit='NA' where displaytopic='IDS_TSK_ACTDEACTUSERMASTER';
update lsusergrouprights set screate ='NA' where displaytopic in ('IDS_SCN_ORDERWORKLOW','IDS_SCN_AUDITTRAILCONFIG','IDS_TSK_IMPORTADS','IDS_SCN_TEMPLATEWORKFLOW','IDS_SCN_PASSWORDPOLICY');
update lsusergrouprightsmaster set screate='NA' where orderno IN (20,27,40,41,43);
update lsusergrouprightsmaster set sdelete='NA' where orderno in (19,26);
update lsusergrouprights set sdelete='NA' where screenname in ('IDS_SCN_CFRSETTINGS','IDS_SCN_PROJECTTEAM');

INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (89, 'IDS_TSK_IMPORTDOCX', 'IDS_MDL_REPORTS', '0', 'NA', 'NA', 'NA', '1,0,0',65,'IDS_SCN_REPORTS') ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (90, 'IDS_TSK_OPENREPORT', 'IDS_MDL_REPORTS', '0', 'NA', 'NA', 'NA', '1,0,0',66,'IDS_SCN_REPORTS') ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (91, 'IDS_TSK_MOVEORDERS', 'IDS_MDL_ORDERS', '0', 'NA', 'NA', 'NA', '1,0,0',14,'IDS_SCN_SHEETORDERS') ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (92, 'IDS_TSK_MOVEORDERSPROTOCOL', 'IDS_MDL_ORDERS', '0', 'NA', 'NA', 'NA', '1,0,0',21,'IDS_SCN_PROTOCOLORDERS') ON CONFLICT(orderno)DO NOTHING;

update lsusergrouprightsmaster set sequenceorder=5 where orderno=7;
update lsusergrouprightsmaster set sequenceorder=6 where orderno=85;
update lsusergrouprightsmaster set sequenceorder=7 where orderno=8;
update lsusergrouprightsmaster set sequenceorder=8 where orderno=9;
update lsusergrouprightsmaster set sequenceorder=9 where orderno=37;
update lsusergrouprightsmaster set sequenceorder=10 where orderno=38;
update lsusergrouprightsmaster set sequenceorder=11 where orderno=61;
update lsusergrouprightsmaster set sequenceorder=12 where orderno=62;
update lsusergrouprightsmaster set sequenceorder=13 where orderno=5;
update lsusergrouprightsmaster set sequenceorder=14 where orderno=6;
update lsusergrouprightsmaster set sequenceorder=15 where orderno=91;

update lsusergrouprightsmaster set sequenceorder=16 where orderno=86;
update lsusergrouprightsmaster set sequenceorder=17 where orderno=48;
update lsusergrouprightsmaster set sequenceorder=18 where orderno=49;
update lsusergrouprightsmaster set sequenceorder=19 where orderno=80;
update lsusergrouprightsmaster set sequenceorder=20 where orderno=81;
update lsusergrouprightsmaster set sequenceorder=21 where orderno=46;
update lsusergrouprightsmaster set sequenceorder=22 where orderno=47;
update lsusergrouprightsmaster set sequenceorder=23 where orderno=92;

update lsusergrouprightsmaster set sequenceorder=24 where orderno=10;
update lsusergrouprightsmaster set sequenceorder=25 where orderno=69;
update lsusergrouprightsmaster set sequenceorder=26 where orderno=70;
update lsusergrouprightsmaster set sequenceorder=27 where orderno=88;

update lsusergrouprightsmaster set sequenceorder=28 where orderno=51;
update lsusergrouprightsmaster set sequenceorder=29 where orderno=50;
update lsusergrouprightsmaster set sequenceorder=30 where orderno=57;
update lsusergrouprightsmaster set sequenceorder=31 where orderno=84;
update lsusergrouprightsmaster set sequenceorder=32 where orderno=83;

update lsusergrouprightsmaster set sequenceorder=33 where orderno=82;
update lsusergrouprightsmaster set sequenceorder=34 where orderno=71;
update lsusergrouprightsmaster set sequenceorder=35 where orderno=72;
update lsusergrouprightsmaster set sequenceorder=36 where orderno=15;
update lsusergrouprightsmaster set sequenceorder=37 where orderno=14;

update lsusergrouprightsmaster set sequenceorder=38 where orderno=11;
update lsusergrouprightsmaster set sequenceorder=39 where orderno=12;
update lsusergrouprightsmaster set sequenceorder=40 where orderno=13;

update lsusergrouprightsmaster set sequenceorder=41 where orderno=65;
update lsusergrouprightsmaster set sequenceorder=42 where orderno=66;
update lsusergrouprightsmaster set sequenceorder=43 where orderno=67;

update lsusergrouprightsmaster set sequenceorder=44 where orderno=78;

update lsusergrouprightsmaster set sequenceorder=45 where orderno=17;
update lsusergrouprightsmaster set sequenceorder=46 where orderno=21;

update lsusergrouprightsmaster set sequenceorder=47 where orderno=16;
update lsusergrouprightsmaster set sequenceorder=48 where orderno=23;
update lsusergrouprightsmaster set sequenceorder=49 where orderno=22;
update lsusergrouprightsmaster set sequenceorder=50 where orderno=24;
update lsusergrouprightsmaster set sequenceorder=51 where orderno=52;
update lsusergrouprightsmaster set sequenceorder=52 where orderno=40;

update lsusergrouprightsmaster set sequenceorder=53 where orderno=18;

update lsusergrouprightsmaster set sequenceorder=54 where orderno=19;

update lsusergrouprightsmaster set sequenceorder=55 where orderno=20;

update lsusergrouprightsmaster set sequenceorder=56 where orderno=41;

update lsusergrouprightsmaster set sequenceorder=57 where orderno=43;

update lsusergrouprightsmaster set sequenceorder=58 where orderno=25;
update lsusergrouprightsmaster set sequenceorder=59 where orderno=28;
update lsusergrouprightsmaster set sequenceorder=60 where orderno=29;
update lsusergrouprightsmaster set sequenceorder=61 where orderno=30;
update lsusergrouprightsmaster set sequenceorder=62 where orderno=31;
update lsusergrouprightsmaster set sequenceorder=63 where orderno=32;

update lsusergrouprightsmaster set sequenceorder=64 where orderno=26;
update lsusergrouprightsmaster set sequenceorder=65 where orderno=27;

update lsusergrouprightsmaster set sequenceorder=66 where orderno=34;
update lsusergrouprightsmaster set sequenceorder=67 where orderno=58;
update lsusergrouprightsmaster set sequenceorder=68 where orderno=45;
update lsusergrouprightsmaster set sequenceorder=69 where orderno=35;
update lsusergrouprightsmaster set sequenceorder=70 where orderno=90;
update lsusergrouprightsmaster set sequenceorder=71 where orderno=89;

update lsusergrouprightsmaster set sequenceorder=72 where orderno=53;
update lsusergrouprightsmaster set sequenceorder=73 where orderno=68;
update lsusergrouprightsmaster set sequenceorder=74 where orderno=39;
update lsusergrouprightsmaster set sequenceorder=75 where orderno=54;
update lsusergrouprightsmaster set sequenceorder=76 where orderno=55;
update lsusergrouprightsmaster set sequenceorder=77 where orderno=56;

update lsusergrouprightsmaster set modulename = 'IDS_MDL_DASHBOARD' where sequenceorder IN (1,2,3,4);
update lsusergrouprightsmaster set modulename = 'IDS_MDL_ORDERS' where sequenceorder IN(5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23);
update lsusergrouprightsmaster set modulename = 'IDS_MDL_TEMPLATES' where sequenceorder IN(24,25,26,27,28,29,30,31,32,33,34,35,36,37);
update lsusergrouprightsmaster set modulename = 'IDS_MDL_MASTERS' where sequenceorder IN(38,39,40,41,42,43,44);

update lsaudittrailconfigmaster set screenname='IDS_SCN_TEMPLATEWORKFLOW'  where screenname='IDS_SCN_SHEETWORKFLOW';
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (87, 'IDS_TSK_DASHBOARDINVENTORY', 'IDS_MDL_DASHBOARD', '0', 'NA', 'NA', 'NA', '0,0,0',4,'IDS_SCN_DASHBOARD') ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (88, 'IDS_TSK_SHEETTEMPEXPORT', 'IDS_MDL_TEMPLATES', '0', 'NA', 'NA', 'NA', '0,0,0',22,'IDS_SCN_SHEETTEMPLATE') ON CONFLICT(orderno)DO NOTHING;

update lsusergrouprightsmaster set sdelete='NA' where displaytopic='IDS_SCN_PROTOCOLTEMPLATE';
update lsusergrouprightsmaster set status='1,1,0' where displaytopic='IDS_SCN_PROTOCOLTEMPLATE';
update lsusergrouprights set sdelete='NA' where displaytopic='IDS_SCN_PROTOCOLTEMPLATE';

update lsusergrouprightsmaster set screate='NA' where orderno in (50,57);
update lsusergrouprights set screate='NA' where displaytopic in ('IDS_TSK_NEWSTEP','IDS_TSK_EXPORTPDF');

update lsusergrouprightsmaster set screate='NA' where displaytopic='IDS_SCN_USERRIGHTS';
update lsusergrouprights set screate='NA' where displaytopic='IDS_SCN_USERRIGHTS';
update lsusergrouprightsmaster set screate='NA' where displaytopic='IDS_SCN_REPORTS';
update lsusergrouprights set screate='NA' where displaytopic='IDS_SCN_REPORTS';
update lsusergrouprightsmaster set screate='NA' where orderno in (58,45,35);
update lsusergrouprights set screate='NA' where displaytopic in ('IDS_TSK_NEWDOCUMENT','IDS_TSK_NEWTEMP','IDS_TSK_GENERATEREPORT');
update lsusergrouprights set modulename='IDS_MDL_DASHBOARD' where displaytopic='IDS_TSK_DASHBOARDINVENTORY';
update lsusergrouprightsmaster set modulename='IDS_MDL_ORDERS' where displaytopic='IDS_TSK_MOVEORDERSPROTOCOL';
update lsusergrouprights set modulename='IDS_MDL_ORDERS' where displaytopic='IDS_TSK_MOVEORDERSPROTOCOL';
update lsusergrouprights set modulename='IDS_MDL_TEMPLATES' where displaytopic in ('IDS_TSK_SHEET','IDS_TSK_PROTOCOL','IDS_TSK_ELNANDRESEARCH','IDS_TSK_LIMSTESTORDER');
update lsusergrouprightsmaster set modulename='IDS_MDL_TEMPLATES' where orderno in (14,15,71,72);
update lsusergrouprightsmaster set modulename='IDS_MDL_ORDERS' where orderno in (46,47);
update lsusergrouprights set modulename='IDS_MDL_ORDERS' where displaytopic in ('IDS_TSK_PENDINGWORKPROTOCOL','IDS_TSK_COMPLETEDWORKPROTOCOL');
update lsusergrouprightsmaster set modulename='IDS_MDL_SETUP' where orderno in (16,17,18,20,21,23,22,24,52,40,43,19,41);

update lsusergrouprightsmaster set status='1,1,1' where orderno=39;

update lsaudittrailconfigmaster set screenname='IDS_SCN_REPORTS' where modulename='IDS_MDL_REPORTS';
update lsaudittrailconfiguration set screenname='IDS_SCN_REPORTS' where modulename='IDS_MDL_REPORTS';

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'lslogbooks_logbookcode_seq' 
   INTO  _kind;

   IF NOT FOUND THEN       
      CREATE SEQUENCE lslogbooks_logbookcode_seq;
   ELSIF _kind = 'S' THEN  
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.lslogbooks
(
    logbookcode integer NOT NULL DEFAULT nextval('lslogbooks_logbookcode_seq'::regclass),
    addedby character varying(255) COLLATE pg_catalog."default",
    addedon timestamp without time zone,
    fieldcount integer,
    logbookfields jsonb,
    logbookname character varying(255) COLLATE pg_catalog."default",
    sitecode integer,
    usercode integer,
    CONSTRAINT lslogbooks_pkey PRIMARY KEY (logbookcode)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.lslogbooks
    OWNER to postgres;

	DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'lslogbooksdata_logbookdatacode_seq' 
   INTO  _kind;

   IF NOT FOUND THEN       
      CREATE SEQUENCE lslogbooksdata_logbookdatacode_seq;
   ELSIF _kind = 'S' THEN  
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.lslogbooksdata
(
    logbookdatacode integer NOT NULL DEFAULT nextval('lslogbooksdata_logbookdatacode_seq'::regclass),
    addedby character varying(255) COLLATE pg_catalog."default",
    addedon timestamp without time zone,
    itemstatus integer,
    logbookcode integer,
    logbookdatafields jsonb,
    logbookitemname character varying(255) COLLATE pg_catalog."default",
    logbookuniqueid character varying(255) COLLATE pg_catalog."default",
    sitecode integer,
    usercode integer,
    CONSTRAINT lslogbooksdata_pkey PRIMARY KEY (logbookdatacode)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.lslogbooksdata
    OWNER to postgres;

ALTER TABLE IF Exists lsnotification ADD COLUMN IF NOT EXISTS notificationfor integer default 1;

update lsusermaster set createdby='Administrator' where usercode=1;

update lsusergrouprights set modulename='IDS_MDL_SETUP' where displaytopic in ('IDS_SCN_USERGROUP','IDS_TSK_ACTDEACT','IDS_SCN_ORDERWORKLOW','IDS_SCN_TEMPLATEWORKFLOW','IDS_SCN_PASSWORDPOLICY','IDS_SCN_PROJECTTEAM','IDS_SCN_USERRIGHTS');
update lsusergrouprights set modulename='IDS_MDL_DASHBOARD' where displaytopic in ('IDS_TSK_TEMPLATEOVERVIEW','IDS_SCN_DASHBOARD','IDS_TSK_ACTIVITIES','IDS_TSK_ORDEROVERVIEW');
update lsusergrouprights set modulename='IDS_MDL_REPORTS' where displaytopic IN ('IDS_TSK_NEWTEMP','IDS_TSK_NEWDOCUMENT');
update lsusergrouprights set modulename='IDS_MDL_PARSER' where displaytopic in ('IDS_SCN_INSTRUMENTMASTER','IDS_SCN_PARSER','IDS_SCN_METHODDELIMITER','IDS_SCN_DELIMITER');
update lsusergrouprights set modulename='IDS_MDL_TEMPLATES' where displaytopic in ('IDS_TSK_TEMPLATESHAREDTOME','IDS_TSK_TEMPLATESHAREDBYME','IDS_TSK_EXPORTPDF','IDS_TSK_NEWSTEP','IDS_SCN_SHEETTEMPLATE','IDS_SCN_PROTOCOLTEMPLATE');
update lsusergrouprights set modulename='IDS_MDL_AUDITTRAIL' where displaytopic in('IDS_TSK_REVIEW','IDS_TSK_EXPORT','IDS_TSK_OPENARCHIVE','IDS_SCN_AUDITHISTORY','IDS_SCN_AUDITTRAILCONFIG');

INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (93, 'IDS_SCN_SHEETORDERS', 'IDS_MDL_ORDERS', '0', 'NA', 'NA', 'NA', '0,0,0',5,'IDS_SCN_SHEETORDERS') ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (94, 'IDS_SCN_PROTOCOLORDERS', 'IDS_MDL_ORDERS', '0', 'NA', 'NA', 'NA', '0,0,0',16,'IDS_SCN_PROTOCOLORDERS') ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (95, 'IDS_SCN_LOGBOOK', 'IDS_MDL_MASTERS', '0', 'NA', 'NA', 'NA', '1,0,1',44,'IDS_SCN_LOGBOOK') ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (96, 'IDS_TSK_SHEETORDEREXPORT', 'IDS_MDL_ORDERS', '0', 'NA', 'NA', 'NA', '0,0,0',15,'IDS_SCN_SHEETORDERS') ON CONFLICT(orderno)DO NOTHING;

update lsusergrouprightsmaster set screate='NA' where orderno in (5,6,46,47);
update lsusergrouprights set modulename='IDS_MDL_MASTERS' where displaytopic in ('IDS_TSK_ADDREPO','IDS_TSK_EDITREPO','IDS_SCN_UNLOCKORDERS','IDS_SCN_TASKMASTER','IDS_SCN_SAMPLEMASTER','IDS_SCN_PROJECTMASTER','IDS_SCN_INVENTORY','IDS_SCN_LOGBOOK');

update lsusergrouprightsmaster set screate='0' where screenname='IDS_SCN_LOGBOOK';
update lsusergrouprights set screate='0' where screenname='IDS_SCN_LOGBOOK';
update lsusergrouprightsmaster set sedit='0'  where screenname='IDS_SCN_LOGBOOK';
update lsusergrouprights set sedit='0'  where screenname='IDS_SCN_LOGBOOK';
update lsusergrouprightsmaster set sdelete='0'  where screenname='IDS_SCN_LOGBOOK';
update lsusergrouprights set sdelete='0'  where screenname='IDS_SCN_LOGBOOK';

update lsusergrouprightsmaster set screate='1' where orderno in (71,72);
update lsusergrouprights set screate='1' where displaytopic in ('IDS_TSK_SHEET','IDS_TSK_PROTOCOL');
update lsusergrouprightsmaster set status='1,0,0' where orderno in (71,72);

delete from lsusergrouprightsmaster where orderno in (15,82);
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (97, 'IDS_TSK_ADDLOGBOOK', 'IDS_MDL_MASTERS', '0', 'NA', 'NA', 'NA', '1,0,0',44,'IDS_SCN_LOGBOOK') ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (98, 'IDS_TSK_EDITLOGBOOK', 'IDS_MDL_MASTERS', '0', 'NA', 'NA', 'NA', '1,0,0',44,'IDS_SCN_LOGBOOK') ON CONFLICT(orderno)DO NOTHING;

delete from lsusergrouprights where displaytopic IN ('IDS_SCN_TEMPLATEMAPPING','IDS_TSK_ELNANDRESEARCH');

ALTER TABLE IF Exists lsprotocolordersharedby ADD COLUMN IF NOT EXISTS protocolorders_protocolordercode numeric(17,0);

ALTER TABLE IF Exists lsprotocolordershareto ADD COLUMN IF NOT EXISTS protocolorders_protocolordercode numeric(17,0);

DO
$do$
declare
  sheetfoldercreate integer :=0;
  protocolfoldercreate integer :=0;
begin

SELECT count(*) into sheetfoldercreate FROM
information_schema.table_constraints WHERE constraint_name='fkghmbtxknscw2ha4cl83v2h85w'
AND table_name='lsprotocolordersharedby';
SELECT count(*) into protocolfoldercreate FROM
information_schema.table_constraints WHERE constraint_name='fkpth04gp8erxx77xnlafxc03nj'
AND table_name='lsprotocolordershareto';
 IF sheetfoldercreate =0 THEN
 	ALTER TABLE ONLY lsprotocolordersharedby ADD CONSTRAINT fkghmbtxknscw2ha4cl83v2h85w FOREIGN KEY (protocolorders_protocolordercode) REFERENCES lslogilabprotocoldetail(protocolordercode);
  END IF;
   IF protocolfoldercreate =0 THEN
	ALTER TABLE ONLY lsprotocolordershareto ADD CONSTRAINT fkpth04gp8erxx77xnlafxc03nj FOREIGN KEY (protocolorders_protocolordercode) REFERENCES lslogilabprotocoldetail(protocolordercode);
  END IF;
END
$do$;  

ALTER TABLE IF Exists lsprotocolordersharedby ADD COLUMN IF NOT EXISTS orderflag character varying(255);

ALTER TABLE IF Exists lsprotocolordershareto ADD COLUMN IF NOT EXISTS orderflag character varying(255);

update instrumenttype set status = -1 where instrumenttype.insttypename='RS232'  and status = 1;
update instrumenttype set status = -1 where instrumenttype.insttypename='TCP\IP' and status =1;

ALTER TABLE IF Exists methoddelimiter ADD COLUMN IF NOT EXISTS defaultvalue integer;

DO
$do$
DECLARE
    counter integer := 0;
BEGIN
  select count(*) into counter from methoddelimiter where delimiterkey = 1 and parsermethodkey =6;

   IF counter=0 THEN       -- name is free
INSERT into methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue)
SELECT 1,1,1,6,1
WHERE NOT EXISTS (select * from methoddelimiter where delimiterkey = 1 and parsermethodkey =6); 
   END IF;
END
$do$;

DO
$do$
DECLARE
    counter integer := 0;
BEGIN
  select count(*) into counter from methoddelimiter where delimiterkey = 1 and parsermethodkey =7;

   IF counter=0 THEN       -- name is free
INSERT into methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue)
SELECT 1,1,1,7,1
WHERE NOT EXISTS (select * from methoddelimiter where delimiterkey = 1 and parsermethodkey =7); 
   END IF;
END
$do$;

update methoddelimiter set defaultvalue =1 where delimiterkey = 1 and parsermethodkey =1;




INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_TEMPLATEOVERVIEW', 'IDS_MDL_DASHBOARD', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_DASHBOARD'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_TEMPLATEOVERVIEW' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_DASHBOARDINVENTORY', 'IDS_MDL_DASHBOARD', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_DASHBOARD'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_DASHBOARDINVENTORY' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_NEWFOLDER', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_SHEETORDERS'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_NEWFOLDER' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_MOVEORDERS', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_SHEETORDERS'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_MOVEORDER' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_SHEETORDEREXPORT', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_SHEETORDERS'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_SHEETORDEREXPORT' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_FOLDERCREATIONPROTOCOL', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLORDERS'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_FOLDERCREATIONPROTOCOL' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_PENDINGWORKPROTOCOL', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLORDERS'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_PENDINGWORKPROTOCOL' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_COMPLETEDWORKPROTOCOL', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLORDERS'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_COMPLETEDWORKPROTOCOL' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_MOVEORDERSPROTOCOL', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLORDERS'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_MOVEORDERSPROTOCOL' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_TEMPLATESHAREDBYME', 'IDS_MDL_TEMPLATES', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_SHEETTEMPLATE'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_TEMPLATESHAREDBYME' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_TEMPLATESHAREDTOME', 'IDS_MDL_TEMPLATES', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_SHEETTEMPLATE'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_TEMPLATESHAREDTOME' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_SHEETTEMPEXPORT', 'IDS_MDL_TEMPLATES', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_SHEETTEMPLATE'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_SHEETTEMPEXPORT' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_SCN_PROTOCOLTEMPLATE', 'IDS_MDL_TEMPLATES', 'administrator', '1', '1', 'NA', '1', 1,1,'IDS_SCN_PROTOCOLTEMPLATE'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_SCN_PROTOCOLTEMPLATE' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_EXPORTPDF', 'IDS_MDL_TEMPLATES', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLTEMPLATE'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_EXPORTPDF' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_PROTOCOLTEMPSHAREBYME', 'IDS_MDL_TEMPLATES', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLTEMPLATE'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_PROTOCOLTEMPSHAREBYME' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_PROTOCOLTEMPSHARETOME', 'IDS_MDL_TEMPLATES', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLTEMPLATE'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_PROTOCOLTEMPSHARETOME' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_SHEET', 'IDS_MDL_TEMPLATES', 'administrator', '1', '1', 'NA', 'NA', 1,1,'IDS_SCN_TEMPLATEMAPPING'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_SHEET' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_PROTOCOL', 'IDS_MDL_TEMPLATES', 'administrator', '1', '1', 'NA', 'NA', 1,1,'IDS_SCN_TEMPLATEMAPPING'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_PROTOCOL' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_SCN_INVENTORY', 'IDS_MDL_MASTERS', 'administrator', '1', '1', 'NA', '1', 1,1,'IDS_SCN_INVENTORY'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_SCN_INVENTORY' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_ADDREPO', 'IDS_MDL_MASTERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_INVENTORY'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_ADDREPO' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_EDITREPO', 'IDS_MDL_MASTERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_INVENTORY'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_EDITREPO' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_SCN_LOGBOOK', 'IDS_MDL_MASTERS', 'administrator', '1', '1', '1', '1', 1,1,'IDS_SCN_LOGBOOK'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_SCN_LOGBOOK' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_ADDLOGBOOK', 'IDS_MDL_MASTERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_LOGBOOK'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_ADDLOGBOOK' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_EDITLOGBOOK', 'IDS_MDL_MASTERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_LOGBOOK'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_EDITLOGBOOK' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_SCN_USERMASTER', 'IDS_MDL_SETUP', 'administrator', '1', '1', 'NA', '1', 1,1,'IDS_SCN_USERMASTER'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_SCN_USERMASTER' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_UNLOCK', 'IDS_MDL_SETUP', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_USERMASTER'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_UNLOCK' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_RESETPASSWORD', 'IDS_MDL_SETUP', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_USERMASTER'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_RESETPASSWORD' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_RETIRE', 'IDS_MDL_SETUP', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_USERMASTER'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_RETIRE' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_IMPORTADS', 'IDS_MDL_SETUP', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_USERMASTER'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_IMPORTADS' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_NEWDOCUMENT', 'IDS_MDL_REPORTS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_REPORTS'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_NEWDOCUMENT' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_NEWTEMP', 'IDS_MDL_REPORTS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_REPORTS'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_NEWTEMP' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_OPENREPORT', 'IDS_MDL_REPORTS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_REPORTS'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_OPENREPORT' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_IMPORTDOCX', 'IDS_MDL_REPORTS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_REPORTS'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_IMPORTDOCX' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_SCN_PARSER', 'IDS_MDL_PARSER', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PARSER'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_SCN_PARSER' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_SCN_INSTRUMENTCATEGORY', 'IDS_MDL_PARSER', 'administrator', '1', '1', '1', '1', 1,1,'IDS_SCN_INSTRUMENTCATEGORY'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_SCN_INSTRUMENTCATEGORY' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_SCN_DELIMITER', 'IDS_MDL_PARSER', 'administrator', '1', '1', '1', '1', 1,1,'IDS_SCN_DELIMITER'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_SCN_DELIMITER' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_SCN_METHODDELIMITER', 'IDS_MDL_PARSER', 'administrator', '1', '1', '1', '1', 1,1,'IDS_SCN_METHODDELIMITER'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_SCN_METHODDELIMITER' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_SCN_METHODMASTER', 'IDS_MDL_PARSER', 'administrator', '1', '1', '1', '1', 1,1,'IDS_SCN_METHODMASTER'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_SCN_METHODMASTER' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_SCN_SHEETORDERS', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_SHEETORDERS'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_SCN_SHEETORDERS' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_SCN_PROTOCOLORDERS', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLORDERS'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_SCN_PROTOCOLORDERS' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_ORDERSHAREDBYMEPROTOCOL', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLORDERS'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_ORDERSHAREDBYMEPROTOCOL' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_ORDERSHAREDTOMEPROTOCOL', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLORDERS'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_ORDERSHAREDTOMEPROTOCOL' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_ACTDEACTUSERMASTER', 'IDS_MDL_SETUP', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_USERMASTER'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_ACTDEACTUSERMASTER' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_FOLDERCREATION', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_SHEETORDERS'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_FOLDERCREATION' and usergroupid_usergroupcode = 1) ; 

update lsusergrouprights set screenname='IDS_SCN_DASHBOARD' where displaytopic='IDS_SCN_DASHBOARD' and usergroupid_usergroupcode=1;
update lsusergrouprights set screenname='IDS_SCN_DASHBOARD' where displaytopic in ('IDS_TSK_ORDEROVERVIEW','IDS_TSK_ACTIVITIES') and usergroupid_usergroupcode=1;
update lsusergrouprights set screenname='IDS_SCN_SHEETORDERS' where displaytopic  in ('IDS_TSK_COMPLETEDWORK','IDS_TSK_ELNTASKORDER','IDS_TSK_RESEARCHACTIVITY','IDS_TSK_MANAGEEXCEL','IDS_TSK_SHEETEVALUATION','IDS_TSK_ORDERSHAREDBYME','IDS_TSK_ORDERSHAREDTOME','IDS_TSK_PENDINGWORK') and usergroupid_usergroupcode=1;
update lsusergrouprights set screenname='IDS_SCN_PROTOCOLORDERS' where displaytopic in ('IDS_TSK_ELNPROTOCOL','IDS_TSK_DYNAMICPROTOCOL') and usergroupid_usergroupcode=1;
update lsusergrouprights set screenname='IDS_SCN_SHEETTEMPLATE' where displaytopic='IDS_SCN_SHEETTEMPLATE' and usergroupid_usergroupcode=1;
update lsusergrouprights set screenname='IDS_SCN_PROTOCOLTEMPLATE' where displaytopic='IDS_TSK_NEWSTEP' and usergroupid_usergroupcode=1;
update lsusergrouprights set screenname='IDS_SCN_TEMPLATEMAPPING' where displaytopic='IDS_TSK_LIMSTESTORDER' and usergroupid_usergroupcode=1;
update lsusergrouprights set screenname='IDS_SCN_TASKMASTER' where displaytopic='IDS_SCN_TASKMASTER' and usergroupid_usergroupcode=1;
update lsusergrouprights set screenname='IDS_SCN_PROJECTMASTER' where displaytopic='IDS_SCN_PROJECTMASTER' and usergroupid_usergroupcode=1;
update lsusergrouprights set screenname='IDS_SCN_SAMPLEMASTER' where displaytopic='IDS_SCN_SAMPLEMASTER' and usergroupid_usergroupcode=1;
update lsusergrouprights set screenname='IDS_SCN_UNLOCKORDERS' where displaytopic='IDS_SCN_UNLOCKORDERS' and usergroupid_usergroupcode=1;
update lsusergrouprights set screenname='IDS_SCN_USERGROUP' where displaytopic='IDS_SCN_USERGROUP' and usergroupid_usergroupcode=1;
update lsusergrouprights set screenname='IDS_SCN_USERGROUP' where displaytopic='IDS_TSK_ACTDEACT' and usergroupid_usergroupcode=1;
update lsusergrouprights set screenname='IDS_SCN_USERMASTER' where displaytopic='IDS_TSK_ACTDEACTUSERMASTER' and usergroupid_usergroupcode=1;
update lsusergrouprights set screenname='IDS_SCN_USERRIGHTS' where displaytopic='IDS_SCN_USERRIGHTS' and usergroupid_usergroupcode=1;
update lsusergrouprights set screenname='IDS_SCN_PROJECTTEAM' where displaytopic='IDS_SCN_PROJECTTEAM' and usergroupid_usergroupcode=1;
update lsusergrouprights set screenname='IDS_SCN_ORDERWORKLOW' where displaytopic='IDS_SCN_ORDERWORKLOW' and usergroupid_usergroupcode=1;
update lsusergrouprights set screenname='IDS_SCN_TEMPLATEWORKFLOW' where displaytopic='IDS_SCN_TEMPLATEWORKFLOW' and usergroupid_usergroupcode=1;
update lsusergrouprights set screenname='IDS_SCN_PASSWORDPOLICY' where displaytopic='IDS_SCN_PASSWORDPOLICY' and usergroupid_usergroupcode=1;
update lsusergrouprights set screenname='IDS_SCN_AUDITTRAILHIS' where displaytopic in ('IDS_SCN_AUDITTRAILHIS','IDS_TSK_REVIEWHISTORY','IDS_TSK_REVIEW','IDS_TSK_CREATEARCHIVE','IDS_TSK_OPENARCHIVE','IDS_TSK_EXPORT') and usergroupid_usergroupcode=1;
update lsusergrouprights set screenname='IDS_SCN_AUDITTRAILCONFIG' where displaytopic='IDS_SCN_AUDITTRAILCONFIG' and usergroupid_usergroupcode=1;
update lsusergrouprights set screenname='IDS_SCN_CFRSETTINGS' where displaytopic='IDS_SCN_CFRSETTINGS' and usergroupid_usergroupcode=1;
update lsusergrouprights set screenname='IDS_SCN_REPORTS' where displaytopic in ('IDS_SCN_REPORTS','IDS_TSK_GENERATEREPORT') and usergroupid_usergroupcode=1;
update lsusergrouprights set screenname='IDS_SCN_INSTRUMENTMASTER' where displaytopic='IDS_SCN_INSTRUMENTMASTER' and usergroupid_usergroupcode=1;
update lsusergrouprights set screenname='IDS_SCN_TEMPLATEMAPPING' where displaytopic='IDS_TSK_LIMSTASKORDER' and usergroupid_usergroupcode=1;
update lsusergrouprights set screenname='IDS_SCN_REPORTS' where displaytopic='IDS_TSK_TEMPLATEDESIGN' and usergroupid_usergroupcode=1;
update lsusergrouprights set screenname='IDS_SCN_SITEMASTER' where displaytopic='IDS_SCN_SITEMASTER' and usergroupid_usergroupcode=1;
update lsusergrouprights set screenname='IDS_SCN_ACTIVEUSER' where displaytopic='IDS_SCN_ACTIVEUSER' and usergroupid_usergroupcode=1;
update lsusergrouprights set screenname='IDS_SCN_DOMAIN' where displaytopic='IDS_SCN_DOMAIN' and usergroupid_usergroupcode=1;

update lsusergrouprights set sequenceorder=1 where displaytopic ='IDS_TSK_ORDEROVERVIEW' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=2 where displaytopic ='IDS_TSK_TEMPLATEOVERVIEW' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=3 where displaytopic ='IDS_TSK_ACTIVITIES' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=4 where displaytopic ='IDS_TSK_DASHBOARDINVENTORY' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=5 where displaytopic ='IDS_TSK_LIMSTASKORDER' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=5 where displaytopic ='IDS_SCN_SHEETORDERS' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=6 where displaytopic ='IDS_TSK_FOLDERCREATION' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=7 where displaytopic ='IDS_TSK_ELNTASKORDER' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=8 where displaytopic ='IDS_TSK_RESEARCHACTIVITY' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=9 where displaytopic ='IDS_TSK_MANAGEEXCEL' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=10 where displaytopic ='IDS_TSK_SHEETEVALUATION' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=11 where displaytopic ='IDS_TSK_ORDERSHAREDBYME' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=12 where displaytopic ='IDS_TSK_ORDERSHAREDTOME' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=13 where displaytopic ='IDS_TSK_PENDINGWORK' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=14 where displaytopic ='IDS_TSK_COMPLETEDWORK' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=15 where displaytopic ='IDS_TSK_MOVEORDERS' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=15 where displaytopic ='IDS_TSK_SHEETORDEREXPORT' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=16 where displaytopic ='IDS_TSK_FOLDERCREATIONPROTOCOL' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=16 where displaytopic ='IDS_SCN_PROTOCOLORDERS' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=17 where displaytopic ='IDS_TSK_ELNPROTOCOL' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=18 where displaytopic ='IDS_TSK_DYNAMICPROTOCOL' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=19 where displaytopic ='IDS_TSK_ORDERSHAREDBYMEPROTOCOL' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=20 where displaytopic ='IDS_TSK_ORDERSHAREDTOMEPROTOCOL' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=21 where displaytopic ='IDS_TSK_PENDINGWORKPROTOCOL' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=22 where displaytopic ='IDS_TSK_COMPLETEDWORKPROTOCOL' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=23 where displaytopic ='IDS_TSK_MOVEORDERSPROTOCOL' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=24 where displaytopic ='IDS_SCN_SHEETTEMPLATE' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=25 where displaytopic ='IDS_TSK_TEMPLATESHAREDBYME' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=26 where displaytopic ='IDS_TSK_TEMPLATESHAREDTOME' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=27 where displaytopic ='IDS_TSK_SHEETTEMPEXPORT' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=28 where displaytopic ='IDS_SCN_PROTOCOLTEMPLATE' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=29 where displaytopic ='IDS_TSK_NEWSTEP' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=30 where displaytopic ='IDS_TSK_EXPORTPDF' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=31 where displaytopic ='IDS_TSK_PROTOCOLTEMPSHAREBYME' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=32 where displaytopic ='IDS_TSK_PROTOCOLTEMPSHARETOME' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=34 where displaytopic ='IDS_TSK_SHEET' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=35 where displaytopic ='IDS_TSK_PROTOCOL' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=37 where displaytopic ='IDS_TSK_LIMSTESTORDER' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=38 where displaytopic ='IDS_SCN_TASKMASTER' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=39 where displaytopic ='IDS_SCN_PROJECTMASTER' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=40 where displaytopic ='IDS_SCN_SAMPLEMASTER' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=41 where displaytopic ='IDS_SCN_INVENTORY' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=42 where displaytopic ='IDS_TSK_ADDREPO' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=43 where displaytopic ='IDS_TSK_EDITREPO' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=44 where displaytopic ='IDS_SCN_LOGBOOK' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=45 where displaytopic ='IDS_TSK_ADDLOGBOOK' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=46 where displaytopic ='IDS_TSK_EDITLOGBOOK' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=47 where displaytopic ='IDS_SCN_UNLOCKORDERS' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=48 where displaytopic ='IDS_SCN_USERGROUP' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=49 where displaytopic ='IDS_TSK_ACTDEACT' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=50 where displaytopic ='IDS_SCN_USERMASTER' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=51 where displaytopic ='IDS_TSK_UNLOCK' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=52 where displaytopic ='IDS_TSK_ACTDEACTUSERMASTER' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=53 where displaytopic ='IDS_TSK_RESETPASSWORD' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=54 where displaytopic ='IDS_TSK_RETIRE' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=55 where displaytopic ='IDS_TSK_IMPORTADS' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=56 where displaytopic ='IDS_SCN_USERRIGHTS' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=57 where displaytopic ='IDS_SCN_PROJECTTEAM' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=58 where displaytopic ='IDS_SCN_ORDERWORKLOW' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=59 where displaytopic ='IDS_SCN_TEMPLATEWORKFLOW' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=60 where displaytopic ='IDS_SCN_PASSWORDPOLICY' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=61 where displaytopic ='IDS_SCN_AUDITTRAILHIS' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=62 where displaytopic ='IDS_TSK_REVIEWHISTORY' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=63 where displaytopic ='IDS_TSK_REVIEW' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=64 where displaytopic ='IDS_TSK_CREATEARCHIVE' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=65 where displaytopic ='IDS_TSK_OPENARCHIVE' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=66 where displaytopic ='IDS_TSK_EXPORT' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=67 where displaytopic ='IDS_SCN_CFRSETTINGS' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=68 where displaytopic ='IDS_SCN_AUDITTRAILCONFIG' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=69 where displaytopic ='IDS_SCN_REPORTS' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=70 where displaytopic ='IDS_TSK_NEWDOCUMENT' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=71 where displaytopic ='IDS_TSK_NEWTEMP' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=72 where displaytopic ='IDS_TSK_GENERATEREPORT' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=73 where displaytopic ='IDS_TSK_OPENREPORT' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=74 where displaytopic ='IDS_TSK_IMPORTDOCX' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=75 where displaytopic ='IDS_SCN_PARSER' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=76 where displaytopic ='IDS_TSK_INSTRUMENTCATEGORY' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=77 where displaytopic ='IDS_SCN_INSTRUMENTMASTER' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=78 where displaytopic ='IDS_SCN_DELIMITER' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=79 where displaytopic ='IDS_SCN_METHODDELIMITER' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=80 where displaytopic ='IDS_SCN_METHODMASTER' and usergroupid_usergroupcode=1;


delete from lsusergrouprights where displaytopic IN ('IDS_SCN_TEMPLATEMAPPING','IDS_TSK_ELNANDRESEARCH');

update lsusergrouprights set modulename='IDS_MDL_ORDERS' where displaytopic in ('IDS_SCN_SHEETORDERS','IDS_TSK_FOLDERCREATION','IDS_TSK_ELNTASKORDER','IDS_TSK_RESEARCHACTIVITY','IDS_TSK_MANAGEEXCEL','IDS_TSK_SHEETEVALUATION','IDS_TSK_ORDERSHAREDBYME','IDS_TSK_ORDERSHAREDTOME','IDS_TSK_PENDINGWORK') and usergroupid_usergroupcode=1;
update lsusergrouprights set screenname='IDS_SCN_USERMASTER' where displaytopic in ('IDS_TSK_UNLOCK','IDS_SCN_USERMASTER','IDS_TSK_RESETPASSWORD','IDS_TSK_RETIRE','IDS_TSK_IMPORTADS') and usergroupid_usergroupcode=1;
update lsusergrouprights set sedit='1' where displaytopic='IDS_SCN_INSTRUMENTMASTER' and usergroupid_usergroupcode=1;

delete from lsusergrouprightsmaster where orderno in (5,6,46,47);
delete from lsusergrouprights where displaytopic in ('IDS_TSK_PENDINGWORKPROTOCOL','IDS_TSK_COMPLETEDWORKPROTOCOL','IDS_TSK_PENDINGWORK','IDS_TSK_COMPLETEDWORK');
update lsusergrouprightsmaster set sedit='NA' where orderno in (80,81);
update lsusergrouprightsmaster set sdelete='1' where orderno=26;
update lsusergrouprights set sdelete='1' where displaytopic='IDS_SCN_CFRSETTINGS';
update lsusergrouprightsmaster set screate='1' where orderno=34;
update lsusergrouprights set screate='1' where displaytopic='IDS_SCN_REPORTS';
update lsusergrouprightsmaster set screate='NA',sedit='NA',sdelete='NA' where orderno in (97,98);
update lsusergrouprights set screate='NA',sedit='NA',sdelete='NA' where displaytopic in ('IDS_TSK_ADDLOGBOOK','IDS_TSK_EDITLOGBOOK');
update lsusergrouprights set screate='1',sedit='1',sdelete='1' where displaytopic='IDS_SCN_LOGBOOK' and usergroupid_usergroupcode=1;

DO $$                  
    BEGIN 
        IF  EXISTS
            (SELECT *
              FROM   lsusergrouprightsmaster
              WHERE  displaytopic = 'IDS_TSK_ELNPROTOCOLORDER')
        THEN
            UPDATE  lsusergrouprightsmaster
            SET displaytopic = 'IDS_TSK_ELNPROTOCOL'  where displaytopic='IDS_TSK_ELNPROTOCOLORDER';
        END IF ;
    END
   $$ ;
   
DO $$                  
    BEGIN 
        IF  EXISTS
            (SELECT *
              FROM   lsusergrouprights
              WHERE  displaytopic = 'IDS_TSK_ELNPROTOCOLORDER')
        THEN
            UPDATE  lsusergrouprights
            SET displaytopic = 'IDS_TSK_ELNPROTOCOL'  where displaytopic='IDS_TSK_ELNPROTOCOLORDER';
        END IF ;
    END
   $$ ;

   DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'lssheetfolderfiles_folderfilecode_seq' 
   INTO  _kind;

   IF NOT FOUND THEN       
      CREATE SEQUENCE lssheetfolderfiles_folderfilecode_seq;
   ELSIF _kind = 'S' THEN  
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.lssheetfolderfiles
(
    folderfilecode integer NOT NULL DEFAULT nextval('lssheetfolderfiles_folderfilecode_seq'::regclass),
    createdtimestamp timestamp without time zone,
    directorycode numeric(17,0),
    filefor character varying(255) COLLATE pg_catalog."default",
    filename character varying(255) COLLATE pg_catalog."default",
    filesize bigint,
    fileviewfor integer,
    uuid character varying(255) COLLATE pg_catalog."default",
    createby_usercode integer,
    lssitemaster_sitecode integer,
    CONSTRAINT lssheetfolderfiles_pkey PRIMARY KEY (folderfilecode),
    CONSTRAINT fkdd9svt9a5p3vot3nbn23oj39h FOREIGN KEY (lssitemaster_sitecode)
        REFERENCES public.lssitemaster (sitecode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkihbef285x9bfvfg5kd688c93v FOREIGN KEY (createby_usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.lssheetfolderfiles
    OWNER to postgres;
    

 ALTER TABLE IF Exists ParserField ADD COLUMN IF NOT EXISTS datatype varchar(50);
 
 Do
 $do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'datatype_datatypekey_seq' 
   INTO  _kind;

   IF NOT FOUND THEN       
      CREATE SEQUENCE datatype_datatypekey_seq;
   ELSIF _kind = 'S' THEN  
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

 CREATE TABLE IF NOT EXISTS public.datatype
(
    datatypekey integer NOT NULL DEFAULT nextval('datatype_datatypekey_seq'::regclass),
    datatypename character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT datatype_pkey PRIMARY KEY (datatypekey)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.datatype
    OWNER to postgres;
    
 ALTER TABLE IF Exists ParserField Drop COLUMN IF EXISTS datatype;
 
 ALTER TABLE IF Exists ParserField ADD COLUMN IF NOT EXISTS datatypekey integer;
 
DO
$do$
declare
  resultvalues integer :=0;
begin

SELECT count(*) into resultvalues FROM
information_schema.table_constraints WHERE constraint_name='fk6xm923bww44i2t95jw2b8keo4'
AND table_name='parserfield';
 IF resultvalues =0 THEN
 	ALTER TABLE ONLY parserfield ADD CONSTRAINT fk6xm923bww44i2t95jw2b8keo4 FOREIGN KEY (datatypekey) REFERENCES datatype (datatypekey);
   END IF;
END
$do$;  

    
DO
$do$
DECLARE
    counter integer := 0;
BEGIN
  select count(*) into counter from datatype where datatypename = 'string';

   IF counter=0 THEN       -- name is free
insert into datatype (datatypekey,datatypename)SELECT 1,'string'
WHERE NOT EXISTS (select * from datatype where datatypename = 'string'); 
   END IF;
END
$do$;

DO
$do$
DECLARE
    counter integer := 0;
BEGIN
  select count(*) into counter from datatype where datatypename = 'Integer';

   IF counter=0 THEN       -- name is free
insert into datatype (datatypekey,datatypename)SELECT 2,'Integer'
WHERE NOT EXISTS (select * from datatype where datatypename = 'Integer'); 
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
   WHERE  relname = 'materialinventory_sequence' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE materialinventory_sequence;
   ELSIF _kind = 'S' THEN  
      -- do nothing?
   ELSE             
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.materialinventory
(
    nmaterialinventorycode integer NOT NULL DEFAULT nextval('materialinventory_sequence'::regclass),
    nmaterialcode integer NOT NULL,
    nmaterialcatcode integer NOT NULL,
    nmaterialtypecode integer NOT NULL,
    ntransactionstatus integer NOT NULL,
    nsectioncode integer NOT NULL,
    jsondata jsonb NOT NULL,
    jsonuidata jsonb NOT NULL,
    nstatus integer NOT NULL DEFAULT 1,
    CONSTRAINT materialinventory_pkey PRIMARY KEY (nmaterialinventorycode)
    --CONSTRAINT material_nmaterialcode FOREIGN KEY (nmaterialcode)
    --REFERENCES public.material (nmaterialcode) MATCH SIMPLE
    --ON UPDATE NO ACTION
    --ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE public.materialinventory OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.transactionstatus
(
    ntranscode integer NOT NULL,
    stransstatus character varying(50) NOT NULL,
    jsondata jsonb NOT NULL,
    nstatus integer NOT NULL DEFAULT 1,
    CONSTRAINT transactionstatus_pkey PRIMARY KEY (ntranscode)
)

TABLESPACE pg_default;

ALTER TABLE public.transactionstatus OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.materialinventorytype
(
    ninventorytypecode Integer NOT NULL,
    jsondata jsonb NOT NULL,
    ndefaultstatus Integer NOT NULL DEFAULT 1,
    nstatus Integer NOT NULL DEFAULT 1,
    CONSTRAINT materialinventorytype_pkey PRIMARY KEY (ninventorytypecode)
)

TABLESPACE pg_default;

ALTER TABLE public.materialinventorytype OWNER to postgres;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'materialgrade_sequence' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE materialgrade_sequence;
   ELSIF _kind = 'S' THEN  
      -- do nothing?
   ELSE             
      -- do something!
   END IF;
END
$do$;
    
CREATE TABLE IF NOT EXISTS public.materialgrade
(
    nmaterialgradecode Integer NOT NULL DEFAULT nextval('materialgrade_sequence'::regclass),
    jsondata jsonb NOT NULL,
    ndefaultstatus Integer NOT NULL DEFAULT 4,
    nsitecode Integer NOT NULL DEFAULT '-1'::integer,
    nstatus Integer NOT NULL DEFAULT 1,
    CONSTRAINT materialgrade_pkey PRIMARY KEY (nmaterialgradecode)
)

TABLESPACE pg_default;

ALTER TABLE public.materialgrade OWNER to postgres; 

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'material_sequence' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE material_sequence;
   ELSIF _kind = 'S' THEN  
      -- do nothing?
   ELSE             
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.material
(
    nmaterialcode integer NOT NULL DEFAULT nextval('material_sequence'::regclass), 
    nmaterialcatcode integer NOT NULL,
    nmaterialtypecode integer NOT NULL,
    ntransactionstatus integer NOT NULL,
    smaterialname character varying(100) NOT NULL,
    jsondata jsonb NOT NULL,
    jsonuidata jsonb NOT NULL,
    nstatus integer NOT NULL DEFAULT 1,
    CONSTRAINT material_pkey PRIMARY KEY (nmaterialcode)
)

TABLESPACE pg_default;

ALTER TABLE public.material OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.mappedtemplatefieldpropsmaterial
(
    nmappedtemplatefieldpropmaterialcode integer NOT NULL,
    nmaterialconfigcode integer NOT NULL,
    jsondata jsonb NOT NULL,
    nstatus integer NOT NULL DEFAULT 1,
    CONSTRAINT mappedtemplatefieldpropsmaterial_pkey PRIMARY KEY (nmappedtemplatefieldpropmaterialcode)
)

TABLESPACE pg_default;

ALTER TABLE public.mappedtemplatefieldpropsmaterial OWNER to postgres;

INSERT INTO public.unit(
nunitcode, sunitname, sdescription, ndefaultstatus, nsitecode, nstatus)
VALUES (1, 'ml', '', 1, 1, 1)on conflict (nunitcode) do nothing;

INSERT INTO public.unit(
nunitcode, sunitname, sdescription, ndefaultstatus, nsitecode, nstatus)
VALUES (2, 'kg', '', 1, 1, 1)on conflict (nunitcode) do nothing;

INSERT INTO public.section(
nsectioncode, ssectionname, sdescription, ndefaultstatus, nsitecode, nstatus)
VALUES (1, 'A', '', 1, 1, 1)on conflict (nsectioncode) do nothing;

INSERT INTO public.section(
nsectioncode, ssectionname, sdescription, ndefaultstatus, nsitecode, nstatus)
VALUES (2, 'B', '', 1, 1, 1)on conflict (nsectioncode) do nothing;

insert into period values(-1,'{
    "ndata": -1,
    "nmaxlength": 0,
    "speriodname": {
        "en-US": "NA",
        "ru-RU": "нет данных",
        "tg-TG": "НА"
    },
    "sdescription": "NA"
}',4,-1,1)on conflict (nperiodcode) do nothing;

insert into period values(1,'{
    "ndata": 1,
    "nmaxlength": 5,
    "speriodname": {
        "en-US": "Minutes",
        "ru-RU": "Минуты",
        "tg-TG": "Дакикахо"
    },
    "sdescription": ""
}',4,-1,1)on conflict (nperiodcode) do nothing;

insert into materialgrade values(1,'{
    "sdescription": "",
    "smaterialgradename": "A"
}',4,-1,1)on conflict (nmaterialgradecode) do nothing;

insert into materialgrade values(2,'{
    "sdescription": "",
    "smaterialgradename": "B"
}',4,-1,1)on conflict (nmaterialgradecode) do nothing;

insert into materialconfig values(1,1,40,'[
    {
        "id": "pv1OWbsMYq",
        "type": "row",
        "children": [
            {
                "id": "Nybc4TT-jv",
                "type": "column",
                "children": [
                    {
                        "id": "wwi4eC9iw",
                        "type": "component",
                        "label": "Material Category",
                        "parent": "materialtype",
                        "source": "materialcategory",
                        "inputtype": "combo",
                        "mandatory": 3,
                        "displayname": {
                            "en-US": "Material Category",
                            "ru-RU": "Категория материала",
                            "tg-TG": "Категорияи мавод"
                        },
                        "valuemember": "nmaterialcatcode",
                        "componentcode": 3,
                        "componentname": "Combo Box",
                        "displaymember": "smaterialcatname"
                    },
                    {
                        "type": "component",
                        "label": "Material Name",
                        "inputtype": "textinput",
                        "mandatory": 3,
                        "displayname": {
                            "en-US": "Standard Name",
                            "ru-RU": "Стандартное имя",
                            "tg-TG": "Номи стандартӣ"
                        },
                        "sfieldlength": 100
                    },
                    {
                        "type": "component",
                        "label": "Basic Unit",
                        "source": "unit",
                        "inputtype": "combo",
                        "mandatory": 3,
                        "displayname": {
                            "en-US": "Basic Unit",
                            "ru-RU": "Базовая единица",
                            "tg-TG": "Воҳиди асосӣ"
                        },
                        "valuemember": "nunitcode",
                        "displaymember": "sunitname",
                        "nstandardtype": 3
                    },
                    {
                        "type": "component",
                        "label": "Prefix",
                        "inputtype": "textinput",
                        "mandatory": 3,
                        "displayname": {
                            "en-US": "Prefix",
                            "ru-RU": "Префикс",
                            "tg-TG": "Префикс"
                        },
                        "sdisplayname": "sprefix",
                        "sfieldlength": 10
                    },
                    {
                        "type": "component",
                        "label": "Quarantine",
                        "inputtype": "toggle",
                        "displayname": {
                            "en-US": "Quarantine",
                            "ru-RU": "Карантин",
                            "tg-TG": "Карантин"
                        },
                        "sdisplayname": "stransstatus",
                        "nstandardtype": 3
                    }
                ]
            },
            {
                "type": "column",
                "children": [
                    {
                        "label": "Section",
                        "source": "section",
                        "readonly": false,
                        "inputtype": "combo",
                        "mandatory": 3,
                        "displayname": {
                            "en-US": "Section",
                            "ru-RU": "Раздел",
                            "tg-TG": "Ҷудокунӣ"
                        },
                        "valuemember": "nsectioncode",
                        "displaymember": "ssectionname",
                        "nsqlquerycode": 2,
                        "nstandardtype": 3
                    },
                    {
                        "max": 99999999,
                        "min": 0,
                        "type": "component",
                        "label": "Reorder Level",
                        "readonly": false,
                        "inputtype": "Numeric",
                        "precision": 4,
                        "displayname": {
                            "en-US": "Reorder Level",
                            "ru-RU": "Уровень повторного заказа",
                            "tg-TG": "Сатҳи азнавсозӣ"
                        },
                        "sdisplayname": "nreorderlevel",
                        "sfieldlength": 8,
                        "nstandardtype": 3
                    },
                    {
                        "id": "-yskhHhld",
                        "type": "component",
                        "label": "Expiry Validations",
                        "inputtype": "radio",
                        "displayname": {
                            "en-US": "Expiry Validations",
                            "ru-RU": "Проверка истечения срока действия",
                            "tg-TG": "Санҷишҳои мӯҳлат"
                        },
                        "radioOptions": {
                            "tags": [
                                {
                                    "id": "No Expiry",
                                    "text": "No Expiry",
                                    "defaultchecked": "No Expiry"
                                },
                                {
                                    "id": "Expiry date",
                                    "text": "Expiry date"
                                },
                                {
                                    "id": "Expiry policy",
                                    "text": "Expiry policy"
                                }
                            ]
                        },
                        "componentcode": 7,
                        "componentname": "Multiple Choice"
                    },
                    {
                        "type": "componentrow",
                        "children": [
                            {
                                "max": 99999999,
                                "min": 0,
                                "type": "component",
                                "label": "Expiry Policy Days",
                                "readonly": false,
                                "inputtype": "Numeric",
                                "displayname": {
                                    "en-US": "Expiry Policy Days",
                                    "ru-RU": "Срок действия полиса дней",
                                    "tg-TG": "Рӯзҳои сиёсати мӯҳлат"
                                },
                                "sdisplayname": "speriodexpiry",
                                "sfieldlength": 8
                            },
                            {
                                "type": "component",
                                "label": "Expiry Policy Period",
                                "source": "period",
                                "inputtype": "combo",
                                "displayname": {
                                    "en-US": "Expiry Policy Period",
                                    "ru-RU": "Срок действия полиса",
                                    "tg-TG": "Мӯҳлати ба охир расидани сиёсати"
                                },
                                "valuemember": "nperiodcode",
                                "displaymember": "speriodname",
                                "nsqlquerycode": 1,
                                "isMultiLingual": true
                            }
                        ]
                    },
                    {
                        "type": "component",
                        "label": "Open Expiry Need",
                        "inputtype": "toggle",
                        "displayname": {
                            "en-US": "Open Expiry Need",
                            "ru-RU": "Открыть истечение срока действия",
                            "tg-TG": "Эҳтиёҷоти мӯҳлатро кушоед"
                        },
                        "sdisplayname": "stransstatus",
                        "nstandardtype": 4
                    },
                    {
                        "type": "componentrow",
                        "children": [
                            {
                                "max": 99999999,
                                "min": 0,
                                "type": "component",
                                "label": "Open Expiry",
                                "readonly": false,
                                "inputtype": "Numeric",
                                "displayname": {
                                    "en-US": "Open Expiry",
                                    "ru-RU": "Открытый срок действия",
                                    "tg-TG": "Муддатро кушоед"
                                },
                                "sdisplayname": "speriodopen",
                                "sfieldlength": 8
                            },
                            {
                                "type": "component",
                                "label": "Open Expiry Period",
                                "source": "period",
                                "inputtype": "combo",
                                "displayname": {
                                    "en-US": "Open Expiry Period",
                                    "ru-RU": "Открытый срок действия",
                                    "tg-TG": "Мӯҳлати кушодани мӯҳлат"
                                },
                                "valuemember": "nperiodcode",
                                "displaymember": "speriodname",
                                "nsqlquerycode": 1,
                                "isMultiLingual": true
                            }
                        ]
                    },
                    {
                        "type": "component",
                        "label": "Next Validation Need",
                        "inputtype": "toggle",
                        "displayname": {
                            "en-US": "Next Validation Need",
                            "ru-RU": "Следующая Потребность В Проверке",
                            "tg-TG": "Минбаъда Зарурати Ба Санҷиши"
                        },
                        "sdisplayname": "stransstatus",
                        "nstandardtype": 4,
                        "defaultchecked": 4
                    },
                    {
                        "type": "componentrow",
                        "children": [
                            {
                                "max": 99999999,
                                "min": 0,
                                "type": "component",
                                "label": "Next Validation",
                                "readonly": false,
                                "inputtype": "Numeric",
                                "displayname": {
                                    "en-US": "Next Validation",
                                    "ru-RU": "Следующая проверка",
                                    "tg-TG": "Оянда санҷиши"
                                },
                                "sdisplayname": "speriodopen",
                                "sfieldlength": 8
                            },
                            {
                                "type": "component",
                                "label": "Next Validation Period",
                                "source": "period",
                                "inputtype": "combo",
                                "displayname": {
                                    "en-US": "Next Validation Period",
                                    "ru-RU": "Следующий Период Проверки",
                                    "tg-TG": "Баъдӣ Давраи Санҷиши"
                                },
                                "valuemember": "nperiodcode",
                                "displaymember": "speriodname",
                                "nsqlquerycode": 1,
                                "isMultiLingual": true
                            }
                        ]
                    },
                    {
                        "type": "component",
                        "label": "Remarks",
                        "inputtype": "textarea",
                        "displayname": {
                            "en-US": "Remarks",
                            "ru-RU": "Примечания",
                            "tg-TG": "Мулохизахо"
                        },
                        "sdisplayname": "sremarks",
                        "sfieldlength": 255,
                        "nstandardtype": 3
                    }
                ]
            }
        ]
    }
]',1)on conflict(nmaterialconfigcode)do nothing;

   
insert into materialconfig values(2,1,138,'[
    {
        "id": "pv1OWbsMYq",
        "type": "row",
        "children": [
            {
                "id": "Nybc4TT-jv",
                "type": "column",
                "children": [
                    {
                        "label": "Internal Reference",
                        "readonly": false,
                        "inputtype": "textinput",
                        "mandatory": 3,
                        "displayname": {
                            "en-US": "Internal Reference",
                            "ru-RU": "Внутренняя ссылка",
                            "tg-TG": "Маълумоти дохилӣ"
                        },
                        "sfieldlength": 100
                    },
                    {
                        "label": "Batch No.",
                        "readonly": false,
                        "inputtype": "textinput",
                        "displayname": {
                            "en-US": "Batch No.",
                            "ru-RU": "Серия №.",
                            "tg-TG": "Рақами партия"
                        },
                        "sfieldlength": 100
                    },
                    {
                        "label": "Lot No.",
                        "readonly": false,
                        "inputtype": "textinput",
                        "displayname": {
                            "en-US": "Lot No.",
                            "ru-RU": "Много не.",
                            "tg-TG": "Лот №"
                        },
                        "sfieldlength": 100
                    },
                    {
                        "label": "Reference Number",
                        "readonly": false,
                        "inputtype": "textinput",
                        "displayname": {
                            "en-US": "Reference Number",
                            "ru-RU": "Справочный номер",
                            "tg-TG": "Рақами истинод"
                        },
                        "sfieldlength": 100
                    },
                    {
                        "label": "Container ID",
                        "readonly": false,
                        "inputtype": "textinput",
                        "displayname": {
                            "en-US": "Container ID",
                            "ru-RU": "Идентификатор контейнера",
                            "tg-TG": "ID контейнер"
                        },
                        "sfieldlength": 100
                    },
                    {
                        "label": "Storage Location",
                        "source": "storagelocation",
                        "readonly": false,
                        "inputtype": "combo",
                        "displayname": {
                            "en-US": "Storage Location",
                            "ru-RU": "Место хранения",
                            "tg-TG": "Ҷойгоҳи нигаҳдорӣ"
                        },
                        "valuemember": "nstoragelocationcode",
                        "displaymember": "sstoragelocationname",
                        "nstandardtype": 3
                    },
                    {
                        "label": "Supplier",
                        "source": "supplier",
                        "readonly": false,
                        "inputtype": "combo",
                        "displayname": {
                            "en-US": "Supplier",
                            "ru-RU": "Поставщик",
                            "tg-TG": "Таъминкунанда"
                        },
                        "valuemember": "nsuppliercode",
                        "displaymember": "ssuppliername",
                        "nstandardtype": 3
                    },
                    {
                        "label": "Manufacturer",
                        "source": "manufacturer",
                        "readonly": false,
                        "inputtype": "combo",
                        "displayname": {
                            "en-US": "Manufacturer",
                            "ru-RU": "Производитель",
                            "tg-TG": "Истеҳсолкунанда"
                        },
                        "valuemember": "nmanufcode",
                        "displaymember": "smanufname",
                        "nstandardtype": 3
                    }
                ]
            },
            {
                "type": "column",
                "children": [
                    {
                        "label": "Section",
                        "source": "section",
                        "readonly": false,
                        "inputtype": "combo",
                        "mandatory": 3,
                        "displayname": {
                            "en-US": "Section",
                            "ru-RU": "Раздел",
                            "tg-TG": "Ҷудокунӣ"
                        },
                        "valuemember": "nsectioncode",
                        "displaymember": "ssectionname",
                        "nsqlquerycode": 4,
                        "nstandardtype": 3
                    },
                    {
                        "type": "componentrow",
                        "children": [
                            {
                                "max": 99999999,
                                "min": 0,
                                "type": "component",
                                "label": "Received Quantity",
                                "readonly": false,
                                "inputtype": "Numeric",
                                "mandatory": 3,
                                "precision": 3,
                                "displayname": {
                                    "en-US": "Received Quantity",
                                    "ru-RU": "Полученное количество",
                                    "tg-TG": "Миқдори гирифташуда"
                                },
                                "sdisplayname": "nqtyreceived",
                                "sfieldlength": 8,
                                "nstandardtype": 3,
                                "fieldsForExpandedGrid": true
                            },
                            {
                                "type": "component",
                                "label": "Unit",
                                "parent": "unit",
                                "source": "unit",
                                "readonly": true,
                                "inputtype": "combo",
                                "displayname": {
                                    "en-US": "Unit",
                                    "ru-RU": "Ед. изм",
                                    "tg-TG": "Воҳиди"
                                },
                                "valuemember": "nunitcode",
                                "displaymember": "sunitname",
                                "nstandardtype": 3
                            }
                        ]
                    },
                    {
                        "label": "Grade",
                        "source": "materialgrade",
                        "readonly": false,
                        "inputtype": "combo",
                        "displayname": {
                            "en-US": "Grade",
                            "ru-RU": "Оценка",
                            "tg-TG": "Синф"
                        },
                        "valuemember": "nmaterialgradecode",
                        "displaymember": "smaterialgradename",
                        "nstandardtype": 3
                    },
                    {
                        "max": 99999999,
                        "min": 0,
                        "type": "component",
                        "label": "Cost",
                        "readonly": false,
                        "inputtype": "Numeric",
                        "displayname": {
                            "en-US": "Cost",
                            "ru-RU": "Расходы",
                            "tg-TG": "Арзиш"
                        },
                        "sfieldlength": 8
                    },
                    {
                        "label": "Date Of Manufacturer",
                        "inputtype": "date",
                        "displayname": {
                            "en-US": "Date Of Manufacturer",
                            "ru-RU": "Дата изготовления",
                            "tg-TG": "Санаи Истеҳсолкунанда"
                        }
                    },
                    {
                        "label": "Received Date & Time",
                        "inputtype": "date",
                        "displayname": {
                            "en-US": "Received Date & Time",
                            "ru-RU": "Дата и время получения",
                            "tg-TG": "Сана ва вақти қабулшуда"
                        }
                    },
                    {
                        "label": "Expiry Date & Time",
                        "inputtype": "date",
                        "displayname": {
                            "en-US": "Expiry Date & Time",
                            "ru-RU": "Дата и время истечения срока действия",
                            "tg-TG": "Санаи ба охир расидан ва вақт"
                        }
                    },
                    {
                        "label": "Remarks",
                        "readonly": false,
                        "inputtype": "textarea",
                        "displayname": {
                            "en-US": "Remarks",
                            "ru-RU": "Примечания",
                            "tg-TG": "Мулохизахо"
                        },
                        "sfieldlength": 255
                    }
                ]
            }
        ]
    }
]',1)on conflict(nmaterialconfigcode)do nothing;

insert into materialconfig values(3,-1,138,'[
    {
        "id": "pv1OWbsMYq",
        "type": "row",
        "children": [
            {
                "id": "Nybc4TT-jv",
                "type": "column",
                "children": [
                    {
                        "child": [
                            {
                                "label": "Transaction Type",
                                "childPath": "0-0-1-0",
                                "isMultiLingual": true,
                                "tablecolumnname": "ntranscode"
                            }
                        ],
                        "label": "Inventory Transaction Type",
                        "parent": true,
                        "source": "materialinventorytype",
                        "readonly": false,
                        "inputtype": "combo",
                        "mandatory": 3,
                        "displayname": {
                            "en-US": "Inventory Transaction Type",
                            "ru-RU": "Тип транзакции инвентаря",
                            "tg-TG": "Навъи муомилоти инвентаризатсия"
                        },
                        "valuemember": "ninventorytypecode",
                        "displaymember": "sinventorytypename",
                        "fieldsForGrid": true,
                        "nstandardtype": 3,
                        "isMultiLingual": true
                    },
                    {
                        "label": "Transaction Type",
                        "parent": false,
                        "source": "transactionstatus",
                        "readonly": false,
                        "inputtype": "combo",
                        "mandatory": 3,
                        "displayname": {
                            "en-US": "Transaction Type",
                            "ru-RU": "Тип операции",
                            "tg-TG": "Навъи транзаксия"
                        },
                        "valuemember": "ntranscode",
                        "defaultvalues": [
                            {
                                "1": "48,72"
                            },
                            {
                                "2": "47,33"
                            }
                        ],
                        "displaymember": "stransdisplaystatus",
                        "fieldsForGrid": true,
                        "nstandardtype": 3,
                        "isMultiLingual": true,
                        "parentprimarycode": "ninventorytypecode",
                        "Inventory Transaction Type": [
                            {
                                "label": "Transaction Type",
                                "source": "transactionstatus",
                                "valuemember": "ntranscode",
                                "defaultvalues": [
                                    {
                                        "1": "48,72"
                                    },
                                    {
                                        "2": "47,33"
                                    }
                                ],
                                "displaymember": "stransdisplaystatus",
                                "parentprimarycode": "ninventorytypecode"
                            }
                        ]
                    },
                    {
                        "label": "Available Quantity/Unit",
                        "readonly": true,
                        "inputtype": "textinput",
                        "displayname": {
                            "en-US": "Available Quantity/Unit",
                            "ru-RU": "Доступное количество/единица",
                            "tg-TG": "Миқдор/Воҳиди дастрас"
                        },
                        "sfieldlength": 100,
                        "needAccordian": true,
                        "fieldsForExpandedGrid": true
                    },
                    {
                        "max": 99999999,
                        "min": 0,
                        "type": "component",
                        "label": "Received Quantity",
                        "readonly": false,
                        "inputtype": "Numeric",
                        "mandatory": 3,
                        "precision": 3,
                        "displayname": {
                            "en-US": "Quantity",
                            "ru-RU": "Количество",
                            "tg-TG": "Миқдор"
                        },
                        "sdisplayname": "nqtyreceived",
                        "sfieldlength": 8,
                        "nstandardtype": 3,
                        "fieldsForExpandedGrid": true
                    },
                    {
                        "label": "Section",
                        "parent": true,
                        "source": "section",
                        "readonly": false,
                        "inputtype": "combo",
                        "mandatory": 3,
                        "displayname": {
                            "en-US": "Section",
                            "ru-RU": "Раздел",
                            "tg-TG": "Ҷудокунӣ"
                        },
                        "valuemember": "nsectioncode",
                        "displaymember": "ssectionname",
                        "nsqlquerycode": 3,
                        "nstandardtype": 3,
                        "fieldsForExpandedGrid": true
                    },
                    {
                        "label": "Comments",
                        "readonly": false,
                        "inputtype": "textarea",
                        "displayname": {
                            "en-US": "Comments",
                            "ru-RU": "Комментарии",
                            "tg-TG": "Шарҳҳо"
                        },
                        "sfieldlength": 255,
                        "fieldsForExpandedGrid": true
                    },
                    {
                        "nprecision": "11,3"
                    }
                ]
            }
        ]
    }
]',1)on conflict(nmaterialconfigcode)do nothing;

insert into mappedtemplatefieldpropsmaterial 
values(1,1,'{
        "ListMasterProps": [
            {
                "mainField": "Material Name",
                "firstField": "Reorder Level"
            }
        ],
        "searchFieldList": [
            {
                "searchFieldList": "Material Name"
            }
        ],
        "MaterialViewFields": [
            {
                "1": {
                    "en-US": "Material Category",
                    "ru-RU": "Категория материала",
                    "tg-TG": "Категорияи мавод"
                },
                "2": "Material Category"
            },
            {
                "1": {
                    "en-US": "Standard Name",
                    "ru-RU": "Стандартное имя",
                    "tg-TG": "Номи стандартӣ"
                },
                "2": "Material Name"
            },
            {
                "1": {
                    "en-US": "Basic Unit",
                    "ru-RU": "Базовая единица",
                    "tg-TG": "Воҳиди асосӣ"
                },
                "2": "Basic Unit"
            },
            {
                "1": {
                    "en-US": "Prefix",
                    "ru-RU": "Префикс",
                    "tg-TG": "Префикс"
                },
                "2": "Prefix"
            },
            {
                "1": {
                    "en-US": "Quarantine",
                    "ru-RU": "Карантин",
                    "tg-TG": "Карантин"
                },
                "2": "Quarantine"
            },
            {
                "1": {
                    "en-US": "Reorder Level",
                    "ru-RU": "Уровень повторного заказа",
                    "tg-TG": "Сатҳи азнавсозӣ"
                },
                "2": "Reorder Level"
            },
            {
                "1": {
                    "en-US": "Expiry Validations",
                    "ru-RU": "Проверка истечения срока действия",
                    "tg-TG": "Санҷишҳои мӯҳлат"
                },
                "2": "Expiry Validations"
            },
            {
                "1": {
                    "en-US": "Expiry Policy Days",
                    "ru-RU": "Срок действия полиса дней",
                    "tg-TG": "Рӯзҳои сиёсати мӯҳлат"
                },
                "2": "Expiry Policy Days"
            },
            {
                "1": {
                    "en-US": "Expiry Policy Period",
                    "ru-RU": "Срок действия полиса",
                    "tg-TG": "Мӯҳлати ба охир расидани сиёсати"
                },
                "2": "Expiry Policy Period"
            },
            {
                "1": {
                    "en-US": "Open Expiry Need",
                    "ru-RU": "Открыть истечение срока действия",
                    "tg-TG": "Эҳтиёҷоти мӯҳлатро кушоед"
                },
                "2": "Open Expiry Need"
            },
            {
                "1": {
                    "en-US": "Open Expiry",
                    "ru-RU": "Открытый срок действия",
                    "tg-TG": "Муддатро кушоед"
                },
                "2": "Open Expiry"
            },
            {
                "1": {
                    "en-US": "Open Expiry Period",
                    "ru-RU": "Открытый срок действия",
                    "tg-TG": "Мӯҳлати кушодани мӯҳлат"
                },
                "2": "Open Expiry Period"
            },
            {
                "1": {
                    "en-US": "Remarks",
                    "ru-RU": "Примечания",
                    "tg-TG": "Мулохизахо"
                },
                "2": "Remarks"
            },
            {
                "1": {
                    "en-US": "Next Validation Need",
                    "ru-RU": "Следующая Потребность В Проверке",
                    "tg-TG": "Минбаъда Зарурати Ба Санҷиши"
                },
                "2": "Next Validation Need"
            },
            {
                "1": {
                    "en-US": "Next Validation",
                    "ru-RU": "Следующая проверка",
                    "tg-TG": "Оянда санҷиши"
                },
                "2": "Next Validation"
            },
            {
                "1": {
                    "en-US": "Next Validation Period",
                    "ru-RU": "Следующий Период Проверки",
                    "tg-TG": "Баъдӣ Давраи Санҷиши"
                },
                "2": "Next Validation Period"
            }
        ],
        "MaterialStandarddatefields": [
            {
                "1": {
                    "en-US": "Expiry Date & Time",
                    "ru-RU": "Дата и время истечения срока действия",
                    "tg-TG": "Санаи ба охир расидан ва вақт"
                },
                "2": "Expiry Date & Time",
                "dateonly": true
            }
        ]
    }', 1)on conflict (nmappedtemplatefieldpropmaterialcode)do nothing;

insert into transactionstatus values(47,'Receive','{
    "salertdisplaystatus": {
        "en-US": "Received",
        "ru-RU": "Получила",
        "tg-TG": "Гирифтанд"
    },
    "stransdisplaystatus": {
        "en-US": "Received",
        "ru-RU": "Получила",
        "tg-TG": "Гирифтанд"
    },
    "sactiondisplaystatus": {
        "en-US": "Receive",
        "ru-RU": "Получать",
        "tg-TG": "Гирифтан"
    }
}',1)on conflict(ntranscode)do nothing;

insert into transactionstatus values(48,'Issue','{
    "salertdisplaystatus": {
        "en-US": "Issued",
        "ru-RU": "Изданный",
        "tg-TG": "Нашр шуда"
    },
    "stransdisplaystatus": {
        "en-US": "Issued",
        "ru-RU": "Изданный",
        "tg-TG": "Нашр шуда"
    },
    "sactiondisplaystatus": {
        "en-US": "Issue",
        "ru-RU": "Проблема",
        "tg-TG": "Чоп"
    }
}',1)on conflict(ntranscode)do nothing;

insert into materialinventorytype values(1,'{
    "sinventorytypename": {
        "en-US": "Inhouse",
        "ru-RU": "Внутренний",
        "tg-TG": "Дар хона"
    }
}',3,1)on conflict(ninventorytypecode)do nothing;

insert into materialinventorytype values(2,'{
    "sinventorytypename": {
        "en-US": "Outside",
        "ru-RU": "За пределами",
        "tg-TG": "Дар берун"
    }
}',4,1)on conflict(ninventorytypecode)do nothing;