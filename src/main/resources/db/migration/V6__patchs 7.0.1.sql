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
update lsusergrouprightsmaster set displaytopic='IDS_SCN_INSTRUMENTCATEGORY' where displaytopic='IDS_TSK_INSTRUMENTCATEGORY';
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
update lsusergrouprights set displaytopic = 'IDS_SCN_PROTOCOLTEMPLATE' where displaytopic='Protocol Templates';
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

INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder) VALUES (82, 'IDS_SCN_TEMPLATEMAPPING', 'IDS_MDL_TEMPLATES', '0', 'NA', 'NA', 'NA', '1,0,0',4) WHERE NOT EXISTS(select * from lsusergrouprightsmaster where orderno=82) ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder) VALUES (83, 'IDS_TSK_PROTOCOLTEMPSHARETOME', 'IDS_MDL_TEMPLATES', '0', 'NA', 'NA', 'NA', '0,0,1',4)  WHERE NOT EXISTS(select * from lsusergrouprightsmaster where orderno=83) ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder) VALUES (84, 'IDS_TSK_PROTOCOLTEMPSHAREBYME', 'IDS_MDL_TEMPLATES', '0', 'NA', 'NA', 'NA', '0,0,1',4) WHERE NOT EXISTS(select * from lsusergrouprightsmaster where orderno=84) ON CONFLICT(orderno)DO NOTHING;

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
update lsusergrouprights set displaytopic = 'IDS_TSK_ACTDEACTUSERMASTER' where screenname='IDS_SCN_USERMASTER';