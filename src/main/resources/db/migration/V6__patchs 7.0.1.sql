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

update lslogilablimsorderdetail set lstestmasterlocal_testcode = testcode where filetype != 0 and (select count(*) from lstestmasterlocal where testcode = testcode) > 0 and lstestmasterlocal_testcode is null;

	-- Table: public.materialtype

-- DROP TABLE IF EXISTS public.materialtype;


CREATE TABLE IF NOT EXISTS public.materialtype
(
    nmaterialtypecode integer NOT NULL,
    jsondata jsonb,
    ndefaultstatus smallint NOT NULL DEFAULT 4,
    nsitecode smallint NOT NULL DEFAULT '-1'::integer,
    nstatus smallint NOT NULL DEFAULT 1,
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

ALTER TABLE public.period
    OWNER to postgres;
	
CREATE TABLE IF NOT EXISTS public.section
(
    nsectioncode integer NOT NULL,
    ssectionname character varying(100) NOT NULL,
    sdescription character varying(255) ,
    ndefaultstatus integer NOT NULL DEFAULT 4,
    nsitecode integer NOT NULL DEFAULT '-1'::integer,
    nstatus integer NOT NULL DEFAULT 1,
    CONSTRAINT section_pkey PRIMARY KEY (nsectioncode)
)

TABLESPACE pg_default;

ALTER TABLE public.section
    OWNER to postgres;
	
CREATE TABLE IF NOT EXISTS public.unit
(
    nunitcode integer NOT NULL,
    sunitname character varying(100) NOT NULL,
    sdescription character varying(255) ,
    ndefaultstatus integer NOT NULL DEFAULT 4,
    nsitecode integer NOT NULL DEFAULT '-1'::integer,
    nstatus integer NOT NULL DEFAULT 1,
    CONSTRAINT unit_pkey PRIMARY KEY (nunitcode)
)

TABLESPACE pg_default;

ALTER TABLE public.unit
    OWNER to postgres;

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



