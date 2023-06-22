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

ALTER TABLE IF EXISTS public.materialtype OWNER to postgres;

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

ALTER TABLE public.materialconfig OWNER to postgres;

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
    smaterialtypename character varying(255),
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

ALTER TABLE IF Exists materialcategory ADD COLUMN IF NOT EXISTS smaterialtypename character varying(255);

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

update lsaudittrailconfigmaster set modulename='IDS_MDL_ORDERS' where modulename in ('Register Orders & Execute','Protocol Order And Register');
update lsaudittrailconfigmaster set modulename='IDS_MDL_TEMPLATES' where modulename in ('Protocols','Sheet Creation','Sheet Setting');
update lsaudittrailconfigmaster set modulename='IDS_MDL_MASTERS' where modulename ='Base Master';
update lsaudittrailconfigmaster set modulename='IDS_MDL_SETUP' where modulename ='User Management';
update lsaudittrailconfigmaster set modulename='IDS_MDL_AUDITTRAIL' where modulename ='Audit Trail';
update lsaudittrailconfigmaster set modulename='IDS_MDL_REPORTS' where modulename ='Reports';


update lsusergrouprights set modulename='IDS_MDL_SETUP' where modulename in ('UserManagement','User Master');
update lsusergrouprights set modulename='IDS_MDL_ORDERS' where modulename ='Protocol Order And Register';
update lsusergrouprights set modulename='IDS_MDL_PARSER' where modulename='Parser';
update lsusergrouprights set modulename='IDS_MDL_REPORTS' where modulename='Reports';
update lsusergrouprightsmaster set modulename='IDS_MDL_DASHBOARD' where modulename='Dash Board';
update lsusergrouprightsmaster set modulename='IDS_MDL_ORDERS' where modulename='Protocol Order And Register';
update lsusergrouprightsmaster set modulename='IDS_MDL_TEMPLATES' where modulename='Templates';
update lsusergrouprightsmaster set modulename='IDS_MDL_AUDITTRAIL' where modulename='AuditTrail History';
update lsusergrouprightsmaster set modulename='IDS_MDL_PARSER' where modulename='Parser';
update lsusergrouprights set modulename='IDS_MDL_MASTERS' where modulename in ('Base Master','Inventory');
update lsusergrouprightsmaster set modulename='IDS_MDL_REPORTS' where modulename='Reports';
update lsusergrouprightsmaster set modulename='IDS_MDL_MASTERS' where modulename in ('Base Master','Inventory');



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
update lsusergrouprightsmaster set displaytopic ='IDS_SCN_INSTRUMENTCATEGORY' where orderno=68;
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

update lsaudittrailconfigmaster set taskname='IDS_TSK_TASKSAVE' where serialno=4;
update lsaudittrailconfigmaster set taskname='IDS_TSK_PROJECTSAVE' where serialno=7;
update lsaudittrailconfigmaster set taskname='IDS_TSK_SAMPLESAVE' where serialno=9;
update lsaudittrailconfiguration set taskname='IDS_TSK_TASKSAVE' where screenname='IDS_SCN_TASKMASTER' and taskname='IDS_TSK_SAVE';
update lsaudittrailconfiguration set taskname='IDS_TSK_PROJECTSAVE' where screenname='IDS_SCN_PROJECTMASTER' and taskname='IDS_TSK_SAVE';
update lsaudittrailconfiguration set taskname='IDS_TSK_SAMPLESAVE' where screenname='IDS_SCN_SAMPLEMASTER' and taskname='IDS_TSK_SAVE';

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


update lsusergrouprightsmaster set status='1,0,0' where orderno=66;
update lsusergrouprightsmaster set sallow='0',screate='NA',sdelete='NA',sedit='NA' where orderno=22;
update lsusergrouprightsmaster set status='0,0,0' where orderno in (85,61,62,91);
update lsusergrouprights set modulename='IDS_MDL_ORDERS' where screenname='IDS_SCN_SHEETORDERS';

update lsusergrouprightsmaster set displaytopic='IDS_SCN_TEMPLATEWORKFLOW' where displaytopic='IDS_SCN_SHEETWORKFLOW';
update lsusergrouprights set displaytopic='IDS_SCN_TEMPLATEWORKFLOW' where displaytopic='IDS_SCN_SHEETWORKFLOW';
update lsusergrouprights set displaytopic='IDS_SCN_ACTIVEUSER' where displaytopic='IDS_TSK_ACTIVEUSER';
update lsusergrouprights set displaytopic='IDS_SCN_DOMAIN' where displaytopic='IDS_TSK_DOMAIN';


update lsusergrouprights set displaytopic ='IDS_TSK_CREATEARCHIVE' where displaytopic='Create Archive';
update lsusergrouprights set displaytopic='IDS_SCN_AUDITTRAILHIS' where displaytopic='IDS_SCN_AUDITTRAILHISTORY';
update lsusergrouprightsmaster set displaytopic='IDS_SCN_AUDITTRAILHIS' where displaytopic='IDS_SCN_AUDITTRAILHISTORY';

update lsusergrouprightsmaster set displaytopic='IDS_TSK_TEMPLATEOVERVIEW' where displaytopic='IDS_TSK_PARAMETEROVERVIEW';
update lsusergrouprights set displaytopic='IDS_TSK_TEMPLATEOVERVIEW' where displaytopic='IDS_TSK_PARAMETEROVERVIEW';
update lsusergrouprights set displaytopic='IDS_SCN_DASHBOARD' where displaytopic='IDS_TSK_PARAMETERUSAGE';

delete from lsusergrouprightsmaster where orderno=4;
delete from lsusergrouprightsmaster where orderno=42;
delete from lsusergrouprightsmaster where orderno=44;

update lsaudittrailconfigmaster set taskname='IDS_TSK_DELETED' where serialno in (21,56,57,34,85,88,91,94,97,28);

update lsaudittrailconfiguration set taskname='IDS_TSK_DELETED' where taskname='IDS_TSK_DELETE' and screenname in ('IDS_SCN_PROJECTTEAM','IDS_SCN_CFRSETTINGS','IDS_SCN_ORDERWORKFLOW','IDS_SCN_TEMPLATEWORKFOW');
update lsaudittrailconfiguration set taskname='IDS_TSK_DELETED' where taskname='IDS_TSK_DELETE' and modulename ='IDS_MDL_PARSER';

INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (85, 'IDS_TSK_FOLDERCREATION', 'IDS_MDL_ORDERS', '0', '0', '0', '0', '1,0,0',2,'IDS_SCN_SHEETORDERS') ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (86, 'IDS_TSK_FOLDERCREATIONPROTOCOL', 'IDS_MDL_ORDERS', '0', '0', '0', '0', '1,0,0',3,'IDS_SCN_PROTOCOLORDERS') ON CONFLICT(orderno)DO NOTHING;

update lsaudittrailconfigmaster set screenname='IDS_SCN_TEMPLATEWORKFLOW'  where screenname='IDS_SCN_SHEETWORKFLOW';
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (87, 'IDS_TSK_DASHBOARDINVENTORY', 'IDS_MDL_DASHBOARD', '0', 'NA', 'NA', 'NA', '0,0,0',4,'IDS_SCN_DASHBOARD') ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (88, 'IDS_TSK_SHEETTEMPEXPORT', 'IDS_MDL_TEMPLATES', '0', 'NA', 'NA', 'NA', '0,0,0',22,'IDS_SCN_SHEETTEMPLATE') ON CONFLICT(orderno)DO NOTHING;

update lsusergrouprightsmaster set sdelete='NA' where displaytopic in ('IDS_SCN_PROTOCOLTEMPLATE','IDS_SCN_INVENTORY');
update lsusergrouprightsmaster set status='1,1,0' where displaytopic='IDS_SCN_PROTOCOLTEMPLATE';
update lsusergrouprights set sdelete='NA' where displaytopic in ('IDS_SCN_PROTOCOLTEMPLATE','IDS_SCN_INVENTORY');


update lsusergrouprightsmaster set screate='NA' where orderno in (66,67);
update lsusergrouprights set screate='NA', sedit='NA' where displaytopic in ('IDS_TSK_ACTDEACTUSERMASTER','IDS_TSK_ORDEROVERVIEW','IDS_TSK_TEMPLATEOVERVIEW','IDS_TSK_ACTIVITIES','IDS_SCN_AUDITTRAILHIS');
update lsusergrouprights set screate ='NA' where displaytopic in ('IDS_SCN_ORDERWORKLOW','IDS_SCN_AUDITTRAILCONFIG','IDS_TSK_IMPORTADS','IDS_SCN_TEMPLATEWORKFLOW','IDS_SCN_PASSWORDPOLICY','IDS_TSK_ADDREPO','IDS_TSK_EDITREPO','IDS_TSK_NEWSTEP','IDS_TSK_EXPORTPDF','IDS_SCN_USERRIGHTS','IDS_TSK_NEWDOCUMENT','IDS_TSK_NEWTEMP','IDS_TSK_GENERATEREPORT');
update lsusergrouprightsmaster set screate='NA' where orderno IN (20,27,40,41,43,50,57,58,45,35,5,6,46,47);
update lsusergrouprightsmaster set sdelete='NA' where orderno in (19,26);
update lsusergrouprights set sdelete='NA' where screenname in ('IDS_SCN_CFRSETTINGS','IDS_SCN_PROJECTTEAM');

INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (89, 'IDS_TSK_IMPORTDOCX', 'IDS_MDL_REPORTS', '0', 'NA', 'NA', 'NA', '1,0,0',65,'IDS_SCN_REPORTS') ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (90, 'IDS_TSK_OPENREPORT', 'IDS_MDL_REPORTS', '0', 'NA', 'NA', 'NA', '1,0,0',66,'IDS_SCN_REPORTS') ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (91, 'IDS_TSK_MOVEORDERS', 'IDS_MDL_ORDERS', '0', 'NA', 'NA', 'NA', '1,0,0',14,'IDS_SCN_SHEETORDERS') ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (92, 'IDS_TSK_MOVEORDERSPROTOCOL', 'IDS_MDL_ORDERS', '0', 'NA', 'NA', 'NA', '1,0,0',21,'IDS_SCN_PROTOCOLORDERS') ON CONFLICT(orderno)DO NOTHING;

update lsusergrouprightsmaster set screate='0' where displaytopic in ('IDS_SCN_USERRIGHTS');
update lsusergrouprights set modulename='IDS_MDL_TEMPLATES' where displaytopic in ('IDS_TSK_SHEET','IDS_TSK_PROTOCOL','IDS_TSK_ELNANDRESEARCH','IDS_TSK_LIMSTESTORDER');
update lsusergrouprights set modulename='IDS_MDL_ORDERS' where displaytopic='IDS_TSK_MOVEORDERSPROTOCOL';
update lsusergrouprightsmaster set modulename='IDS_MDL_TEMPLATES' where orderno in (14,15,71,72);
update lsusergrouprightsmaster set modulename='IDS_MDL_ORDERS' where orderno in (46,47) and displaytopic='IDS_TSK_MOVEORDERSPROTOCOL';
update lsusergrouprights set modulename='IDS_MDL_ORDERS' where displaytopic in ('IDS_TSK_PENDINGWORKPROTOCOL','IDS_TSK_COMPLETEDWORKPROTOCOL');
update lsusergrouprightsmaster set modulename='IDS_MDL_SETUP' where orderno in (16,17,18,20,21,23,22,24,52,40,43,19,41);


update lsusergrouprightsmaster set status='0,0,0' where orderno in (45,58,35,89,90);

update lsaudittrailconfigmaster set screenname='IDS_SCN_REPORTS' where modulename='IDS_MDL_REPORTS';
update lsaudittrailconfiguration set screenname='IDS_SCN_REPORTS' where modulename='IDS_MDL_REPORTS';
update lsaudittrailconfigmaster set taskname='IDS_TSK_ADD' where modulename='IDS_MDL_PARSER' and taskname='IDS_TSK_SAVE';
update lsaudittrailconfiguration set taskname='IDS_TSK_ADD' where modulename='IDS_MDL_PARSER' and taskname='IDS_TSK_SAVE';

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
update lsusergrouprights set modulename='IDS_MDL_DASHBOARD' where displaytopic in ('IDS_TSK_TEMPLATEOVERVIEW','IDS_SCN_DASHBOARD','IDS_TSK_ACTIVITIES','IDS_TSK_ORDEROVERVIEW','IDS_TSK_DASHBOARDINVENTORY');
update lsusergrouprights set modulename='IDS_MDL_REPORTS' where displaytopic IN ('IDS_TSK_NEWTEMP','IDS_TSK_NEWDOCUMENT');
update lsusergrouprights set modulename='IDS_MDL_PARSER' where displaytopic in ('IDS_SCN_INSTRUMENTMASTER','IDS_SCN_PARSER','IDS_SCN_METHODDELIMITER','IDS_SCN_DELIMITER');
update lsusergrouprights set modulename='IDS_MDL_TEMPLATES' where displaytopic in ('IDS_TSK_TEMPLATESHAREDTOME','IDS_TSK_TEMPLATESHAREDBYME','IDS_TSK_EXPORTPDF','IDS_TSK_NEWSTEP','IDS_SCN_SHEETTEMPLATE','IDS_SCN_PROTOCOLTEMPLATE');
update lsusergrouprights set modulename='IDS_MDL_AUDITTRAIL' where displaytopic in('IDS_TSK_REVIEW','IDS_TSK_EXPORT','IDS_TSK_OPENARCHIVE','IDS_SCN_AUDITHISTORY','IDS_SCN_AUDITTRAILCONFIG');

INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (93, 'IDS_SCN_SHEETORDERS', 'IDS_MDL_ORDERS', '0', 'NA', 'NA', 'NA', '0,0,0',5,'IDS_SCN_SHEETORDERS') ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (94, 'IDS_SCN_PROTOCOLORDERS', 'IDS_MDL_ORDERS', '0', 'NA', 'NA', 'NA', '0,0,0',16,'IDS_SCN_PROTOCOLORDERS') ON CONFLICT(orderno)DO NOTHING;

INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (96, 'IDS_TSK_SHEETORDEREXPORT', 'IDS_MDL_ORDERS', '0', 'NA', 'NA', 'NA', '0,0,0',15,'IDS_SCN_SHEETORDERS') ON CONFLICT(orderno)DO NOTHING;

update lsusergrouprights set modulename='IDS_MDL_MASTERS' where displaytopic in ('IDS_TSK_ADDREPO','IDS_TSK_EDITREPO','IDS_SCN_UNLOCKORDERS','IDS_SCN_TASKMASTER','IDS_SCN_SAMPLEMASTER','IDS_SCN_PROJECTMASTER','IDS_SCN_INVENTORY');

update lsusergrouprightsmaster set screate='1' where orderno in (71,72);
update lsusergrouprights set screate='1' where displaytopic in ('IDS_TSK_SHEET','IDS_TSK_PROTOCOL') and screate='NA';
update lsusergrouprightsmaster set status='1,0,0' where orderno in (71,72);

delete from lsusergrouprightsmaster where orderno in (15,82,95,97,98);
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (97, 'IDS_TSK_ADDLOGBOOK', 'IDS_MDL_LOGBOOK', '0', 'NA', 'NA', 'NA', '0,0,0',44,'IDS_SCN_LOGBOOK') ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (98, 'IDS_TSK_EDITLOGBOOK', 'IDS_MDL_LOGBOOK', '0', 'NA', 'NA', 'NA', '0,0,0',44,'IDS_SCN_LOGBOOK') ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (95, 'IDS_SCN_LOGBOOK', 'IDS_MDL_LOGBOOK', '0', 'NA', 'NA', 'NA', '0,0,0',44,'IDS_SCN_LOGBOOK') ON CONFLICT(orderno)DO NOTHING;
update lsusergrouprightsmaster set screate='0',sedit='0',sdelete='0' where screenname='IDS_SCN_LOGBOOK';
update lsusergrouprightsmaster set status='1,1,1' where displaytopic='IDS_SCN_LOGBOOK';
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




INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) VALUES ('IDS_TSK_TEMPLATEOVERVIEW', 'IDS_MDL_DASHBOARD', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_DASHBOARD'); 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_DASHBOARDINVENTORY', 'IDS_MDL_DASHBOARD', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_DASHBOARD'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_DASHBOARDINVENTORY' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_NEWFOLDER', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_SHEETORDERS'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_NEWFOLDER' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_MOVEORDERS', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_SHEETORDERS'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_MOVEORDERS' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_SHEETORDEREXPORT', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_SHEETORDERS'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_SHEETORDEREXPORT' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_FOLDERCREATIONPROTOCOL', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLORDERS'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_FOLDERCREATIONPROTOCOL' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_PENDINGWORKPROTOCOL', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLORDERS'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_PENDINGWORKPROTOCOL' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_COMPLETEDWORKPROTOCOL', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLORDERS'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_COMPLETEDWORKPROTOCOL' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_MOVEORDERSPROTOCOL', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLORDERS'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_MOVEORDERSPROTOCOL' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_TEMPLATESHAREDBYME', 'IDS_MDL_TEMPLATES', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_SHEETTEMPLATE'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_TEMPLATESHAREDBYME' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) VALUES ('IDS_SCN_SHEETTEMPLATE', 'IDS_MDL_TEMPLATES', 'administrator', '1', '1', 'NA', '1', 1,1,'IDS_SCN_SHEETTEMPLATE');
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_TEMPLATESHAREDTOME', 'IDS_MDL_TEMPLATES', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_SHEETTEMPLATE'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_TEMPLATESHAREDTOME' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_SHEETTEMPEXPORT', 'IDS_MDL_TEMPLATES', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_SHEETTEMPLATE'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_SHEETTEMPEXPORT' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_SCN_PROTOCOLTEMPLATE', 'IDS_MDL_TEMPLATES', 'administrator', '1', '1', 'NA', '1', 1,1,'IDS_SCN_PROTOCOLTEMPLATE'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_SCN_PROTOCOLTEMPLATE' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) VALUES ('IDS_TSK_EXPORTPDF', 'IDS_MDL_TEMPLATES', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLTEMPLATE');
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
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) VALUES ('IDS_TSK_NEWDOCUMENT', 'IDS_MDL_REPORTS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_REPORTS'); 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_NEWTEMP', 'IDS_MDL_REPORTS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_REPORTS'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_NEWTEMP' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_OPENREPORT', 'IDS_MDL_REPORTS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_REPORTS'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_OPENREPORT' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_IMPORTDOCX', 'IDS_MDL_REPORTS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_REPORTS'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_IMPORTDOCX' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) VALUES ('IDS_SCN_PARSER', 'IDS_MDL_PARSER', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PARSER'); 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_SCN_INSTRUMENTCATEGORY', 'IDS_MDL_PARSER', 'administrator', '1', '1', '1', '1', 1,1,'IDS_SCN_INSTRUMENTCATEGORY'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_SCN_INSTRUMENTCATEGORY' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) VALUES ('IDS_SCN_DELIMITER', 'IDS_MDL_PARSER', 'administrator', '1', '1', '1', '1', 1,1,'IDS_SCN_DELIMITER'); 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) VALUES ('IDS_SCN_METHODDELIMITER', 'IDS_MDL_PARSER', 'administrator', '1', '1', '1', '1', 1,1,'IDS_SCN_METHODDELIMITER'); 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) VALUES ('IDS_SCN_METHODMASTER', 'IDS_MDL_PARSER', 'administrator', '1', '1', '1', '1', 1,1,'IDS_SCN_METHODMASTER');
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_SCN_SHEETORDERS', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_SHEETORDERS'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_SCN_SHEETORDERS' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_SCN_PROTOCOLORDERS', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLORDERS'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_SCN_PROTOCOLORDERS' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_ORDERSHAREDBYMEPROTOCOL', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLORDERS'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_ORDERSHAREDBYMEPROTOCOL' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_ORDERSHAREDTOMEPROTOCOL', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLORDERS'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_ORDERSHAREDTOMEPROTOCOL' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_ACTDEACTUSERMASTER', 'IDS_MDL_SETUP', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_USERMASTER'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_ACTDEACTUSERMASTER' and usergroupid_usergroupcode = 1) ; 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_FOLDERCREATION', 'IDS_MDL_ORDERS', 'administrator', '1', '1', '1', '1', 1,1,'IDS_SCN_SHEETORDERS'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_FOLDERCREATION' and usergroupid_usergroupcode = 1) ; 

delete from lsusergrouprights where displaytopic IN ('IDS_SCN_TEMPLATEMAPPING','IDS_TSK_ELNANDRESEARCH');

update lsusergrouprights set modulename='IDS_MDL_ORDERS' where displaytopic in ('IDS_SCN_SHEETORDERS','IDS_TSK_FOLDERCREATION','IDS_TSK_ELNTASKORDER','IDS_TSK_RESEARCHACTIVITY','IDS_TSK_MANAGEEXCEL','IDS_TSK_SHEETEVALUATION','IDS_TSK_ORDERSHAREDBYME','IDS_TSK_ORDERSHAREDTOME','IDS_TSK_PENDINGWORK') and usergroupid_usergroupcode=1;
update lsusergrouprights set screenname='IDS_SCN_USERMASTER' where displaytopic in ('IDS_TSK_UNLOCK','IDS_SCN_USERMASTER','IDS_TSK_RESETPASSWORD','IDS_TSK_RETIRE','IDS_TSK_IMPORTADS');
update lsusergrouprights set sedit='1' where displaytopic='IDS_SCN_INSTRUMENTMASTER' and usergroupid_usergroupcode=1;

delete from lsusergrouprightsmaster where orderno in (5,6,46,47);
delete from lsusergrouprights where displaytopic in ('IDS_TSK_PENDINGWORKPROTOCOL','IDS_TSK_COMPLETEDWORKPROTOCOL','IDS_TSK_PENDINGWORK','IDS_TSK_COMPLETEDWORK');
update lsusergrouprightsmaster set sedit='NA' where orderno in (80,81);
update lsusergrouprightsmaster set sdelete='1' where orderno=26;
update lsusergrouprights set sdelete='1' where displaytopic='IDS_SCN_CFRSETTINGS' and sdelete='NA';
update lsusergrouprightsmaster set screate='0' where orderno=34;

update lsusergrouprightsmaster set screate='NA',sedit='NA',sdelete='NA' where orderno in (97,98,108,109);
update lsusergrouprights set screate='NA',sedit='NA',sdelete='NA' where displaytopic in ('IDS_TSK_ADDLOGBOOK','IDS_TSK_EDITLOGBOOK');
update lsusergrouprights set screate='1',sedit='NA',sdelete='NA' where displaytopic='IDS_SCN_LOGBOOK' and usergroupid_usergroupcode=1;
update lsusergrouprights set sedit='NA',sdelete='NA' where displaytopic='IDS_SCN_LOGBOOK';
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
ALTER TABLE IF Exists LSprojectmaster ADD COLUMN IF NOT EXISTS createdby character varying(255);
ALTER TABLE IF Exists LSprojectmaster ADD COLUMN IF NOT EXISTS createdon character varying(255);

    
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
    ntransactionstatus integer NOT NULL,
    nmaterialcatcode integer NOT NULL,
    nmaterialtypecode integer NOT NULL,
    nsectioncode integer NOT NULL,
    jsondata text NOT NULL,
    jsonuidata text NOT NULL,
    nstatus integer NOT NULL DEFAULT 1,
    CONSTRAINT materialinventory_pkey PRIMARY KEY (nmaterialinventorycode)
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
    smaterialgradename character varying(50),
    CONSTRAINT materialgrade_pkey PRIMARY KEY (nmaterialgradecode)
)

TABLESPACE pg_default;

ALTER TABLE public.materialgrade OWNER to postgres; 

ALTER TABLE IF Exists materialgrade ADD COLUMN IF NOT EXISTS smaterialgradename character varying(50);

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

ALTER TABLE IF Exists period ADD COLUMN IF NOT EXISTS speriodname character varying(50);

INSERT INTO period (nperiodcode, jsondata, ndefaultstatus, nsitecode, nstatus,speriodname) VALUES (-1, '{"ndata": -1, "nmaxlength": 0, "speriodname": {"en-US": "NA", "ru-RU": "нет данных", "tg-TG": "НА"}, "sdescription": "NA"}', 4, -1, 1,'NA') on conflict(nperiodcode)do nothing;
INSERT INTO period (nperiodcode, jsondata, ndefaultstatus, nsitecode, nstatus,speriodname) VALUES (1, '{"ndata": 1, "nmaxlength": 5, "speriodname": {"en-US": "Minutes", "ru-RU": "Минуты", "tg-TG": "Дакикахо"}, "sdescription": ""}', 4, -1, 1,'Minutes') on conflict(nperiodcode)do nothing;;
INSERT INTO period (nperiodcode, jsondata, ndefaultstatus, nsitecode, nstatus,speriodname) VALUES (2, '{"ndata": 60, "nmaxlength": 4, "speriodname": {"en-US": "Hours", "ru-RU": "Часы", "tg-TG": "Соатхо"}, "sdescription": ""}', 3, -1, 1,'Hours') on conflict(nperiodcode)do nothing;;
INSERT INTO period (nperiodcode, jsondata, ndefaultstatus, nsitecode, nstatus,speriodname) VALUES (3, '{"ndata": 10080, "nmaxlength": 0, "speriodname": {"en-US": "Weeks", "ru-RU": "Недели", "tg-TG": "Ҳафтаҳо"}, "sdescription": ""}', 4, -1, 1,'Weeks') on conflict(nperiodcode)do nothing;;
INSERT INTO period (nperiodcode, jsondata, ndefaultstatus, nsitecode, nstatus,speriodname) VALUES (4, '{"ndata": 1440, "nmaxlength": 3, "speriodname": {"en-US": "Days", "ru-RU": "Дни", "tg-TG": "Рузхо"}, "sdescription": ""}', 4, -1, 1,'Days') on conflict(nperiodcode)do nothing;;
INSERT INTO period (nperiodcode, jsondata, ndefaultstatus, nsitecode, nstatus,speriodname) VALUES (5, '{"ndata": 43200, "nmaxlength": 2, "speriodname": {"en-US": "Month", "ru-RU": "Месяц", "tg-TG": "Мох"}, "sdescription": ""}', 4, -1, 1,'Month') on conflict(nperiodcode)do nothing;;
INSERT INTO period (nperiodcode, jsondata, ndefaultstatus, nsitecode, nstatus,speriodname) VALUES (6, '{"ndata": 525600, "nmaxlength": 1, "speriodname": {"en-US": "Years", "ru-RU": "Годы", "tg-TG": "Солхо"}, "sdescription": ""}', 4, -1, 1,'Years') on conflict(nperiodcode)do nothing;;
INSERT INTO period (nperiodcode, jsondata, ndefaultstatus, nsitecode, nstatus,speriodname) VALUES (7, '{"ndata": 0, "nmaxlength": 0, "speriodname": {"en-US": "Day Without Hours", "ru-RU": "День без часов", "tg-TG": "Рӯзи бе соат"}, "sdescription": ""}', 4, -1, 1,'Day Without Hours') on conflict(nperiodcode)do nothing;;
INSERT INTO period (nperiodcode, jsondata, ndefaultstatus, nsitecode, nstatus,speriodname) VALUES (8, '{"ndata": -1, "nmaxlength": 0, "speriodname": {"en-US": "Never", "ru-RU": "Никогда", "tg-TG": "Ҳеҷ гоҳ"}, "sdescription": "Never"}', 4, -1, 1,'Never') on conflict(nperiodcode)do nothing;;

update period set speriodname = 'NA' where nperiodcode = -1;
update period set speriodname = 'Minutes' where nperiodcode = 1;

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

update lsusergrouprightsmaster set modulename='IDS_MDL_DASHBOARD' where orderno in (1,3,87);
update lsusergrouprightsmaster set modulename='IDS_MDL_ORDERS' where orderno in (7,8,9,37,38,61,62,93,85);
update lsusergrouprightsmaster set modulename='IDS_MDL_MASTERS' where orderno=78;
update lsusergrouprights set modulename='IDS_MDL_ORDERS' where displaytopic='IDS_TSK_MOVEORDERSPROTOCOL';
update lsusergrouprightsmaster set screate='0' where orderno in (71,72);
update lsusergrouprightsmaster set sedit='0' where orderno in (11,12,13);
update lsusergrouprightsmaster set sdelete='0' where orderno=26;
update lsusergrouprightsmaster set status='1,1,1' where orderno in (39,11,12,13);

delete from lsaudittrailconfigmaster where serialno in (81,82,83);
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(81,0,'IDS_MDL_LOGBOOK',6,'IDS_SCN_LOGBOOK','IDS_TSK_SAVE') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(82,0,'IDS_MDL_LOGBOOK',6,'IDS_SCN_LOGBOOK','IDS_TSK_EDIT') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(83,0,'IDS_MDL_LOGBOOK',6,'IDS_SCN_LOGBOOK','IDS_TSK_DELETE') ON CONFLICT(serialno)DO NOTHING;

Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(84,0,'IDS_MDL_PARSER',12,'IDS_SCN_INSTRUMENTCATEGORY','IDS_TSK_SAVE') ON CONFLICT(serialno)DO NOTHING;

Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(147,0,'IDS_MDL_PARSER',12,'IDS_SCN_INSTRUMENTCATEGORY','IDS_TSK_EDIT') ON CONFLICT(serialno)DO NOTHING;

Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(85,0,'IDS_MDL_PARSER',12,'IDS_SCN_INSTRUMENTCATEGORY','IDS_TSK_DELETE') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(86,0,'IDS_MDL_PARSER',12,'IDS_SCN_INSTRUMENTMASTER','IDS_TSK_SAVE') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(87,0,'IDS_MDL_PARSER',12,'IDS_SCN_INSTRUMENTMASTER','IDS_TSK_EDIT') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(88,0,'IDS_MDL_PARSER',12,'IDS_SCN_INSTRUMENTMASTER','IDS_TSK_DELETE') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(89,0,'IDS_MDL_PARSER',12,'IDS_SCN_DELIMITER','IDS_TSK_SAVE') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(90,0,'IDS_MDL_PARSER',12,'IDS_SCN_DELIMITER','IDS_TSK_EDIT') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(91,0,'IDS_MDL_PARSER',12,'IDS_SCN_DELIMITER','IDS_TSK_DELETE') ON CONFLICT(serialno)DO NOTHING;
Insert into LSaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(92,0,'IDS_MDL_PARSER',12,'IDS_SCN_METHODDELIMITER','IDS_TSK_SAVE') ON CONFLICT(serialno)DO NOTHING;
Insert into LSaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(93,0,'IDS_MDL_PARSER',12,'IDS_SCN_METHODDELIMITER','IDS_TSK_EDIT') ON CONFLICT(serialno)DO NOTHING;
Insert into LSaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(94,0,'IDS_MDL_PARSER',12,'IDS_SCN_METHODDELIMITER','IDS_TSK_DELETE') ON CONFLICT(serialno)DO NOTHING;
Insert into LSaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(95,0,'IDS_MDL_PARSER',12,'IDS_SCN_METHODMASTER','IDS_TSK_SAVE') ON CONFLICT(serialno)DO NOTHING;
Insert into LSaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(96,0,'IDS_MDL_PARSER',12,'IDS_SCN_METHODMASTER','IDS_TSK_EDIT') ON CONFLICT(serialno)DO NOTHING;
Insert into LSaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(97,0,'IDS_MDL_PARSER',12,'IDS_SCN_METHODMASTER','IDS_TSK_DELETE') ON CONFLICT(serialno)DO NOTHING;

ALTER TABLE IF Exists lslogbooks ADD COLUMN IF NOT EXISTS retirestatus integer;
ALTER TABLE IF Exists lslogbooks ADD COLUMN IF NOT EXISTS userstatus character varying(10);

ALTER TABLE IF Exists materialinventory ALTER COLUMN jsondata TYPE text;
ALTER TABLE IF Exists materialinventory ALTER COLUMN jsonuidata TYPE text;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'materialinventorytransaction_sequence' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE materialinventorytransaction_sequence;
   ELSIF _kind = 'S' THEN  
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.materialinventorytransaction
(
    nmaterialinventtranscode integer NOT NULL DEFAULT nextval('materialinventorytransaction_sequence'::regclass), 
    nmaterialinventorycode integer NOT NULL,
    ninventorytranscode integer NOT NULL,
    ntransactiontype integer NOT NULL,
    nsectioncode integer NOT NULL,
    nresultusedmaterialcode integer NOT NULL,
    nqtyreceived numeric NOT NULL,
    nqtyissued numeric NOT NULL,
    jsondata text NOT NULL,
    jsonuidata text NOT NULL,
    nsitecode integer NOT NULL DEFAULT 1,
    nstatus integer NOT NULL DEFAULT 1,
    CONSTRAINT materialinventorytransaction_pkey PRIMARY KEY (nmaterialinventtranscode),
    CONSTRAINT fknok3att985drj90p3b41qn888 FOREIGN KEY (nmaterialinventorycode)
        REFERENCES public.materialinventory (nmaterialinventorycode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE public.materialinventorytransaction OWNER to postgres;

Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(98,0,'IDS_MDL_ORDERS',1,'IDS_SCN_SHEETORDERS','IDS_TSK_EXPORT') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(99,0,'IDS_MDL_ORDERS',1,'IDS_SCN_SHEETORDERS','IDS_TSK_EXPORTASJSON') ON CONFLICT(serialno)DO NOTHING;

ALTER TABLE IF Exists lspasswordpolicy ADD COLUMN IF NOT EXISTS idletime integer;

update LSpasswordpolicy set idletime=15 where idletime is null;

delete from lsusergrouprights where displaytopic='IDS_SCN_DASHBOARD';

update lsusergrouprights set modulename='IDS_MDL_ORDERS',screenname='IDS_SCN_PROTOCOLORDERS' where displaytopic in ('IDS_TSK_ELNPROTOCOL','IDS_TSK_DYNAMICPROTOCOL');
update lsusergrouprights set modulename='IDS_MDL_AUDITTRAIL' where displaytopic in ('IDS_SCN_CFRSETTINGS','IDS_TSK_CREATEARCHIVE','IDS_TSK_REVIEWHISTORY','IDS_SCN_AUDITTRAILHIS');
update lsusergrouprights set modulename='IDS_MDL_SETUP' where displaytopic  IN ('IDS_SCN_ACTIVEUSER','IDS_SCN_DOMAIN','IDS_TSK_IMPORTADS','IDS_TSK_RETIRE');
update lsusergrouprights set modulename='IDS_MDL_ORDERS' where displaytopic in ('IDS_TSK_ORDERSHAREDBYME','IDS_TSK_ORDERSHAREDTOME','IDS_TSK_SHEETEVALUATION','IDS_TSK_ELNTASKORDER','IDS_TSK_RESEARCHACTIVITY','IDS_TSK_MANAGEEXCEL');

update lsusergrouprights set screate='NA' where displaytopic in ('IDS_TSK_RESETPASSWORD','IDS_SCN_AUDITTRAILHIS');
update lsusergrouprights set screate='NA',sedit='NA',sdelete='NA' where displaytopic in ('IDS_TSK_REVIEW','IDS_TSK_CREATEARCHIVE','IDS_TSK_ORDERSHAREDBYME','IDS_TSK_ORDERSHAREDTOME');
delete from lsusergrouprights where displaytopic in ('IDS_SCN_ACTIVEUSER','IDS_SCN_DOMAIN') and usergroupid_usergroupcode!=1;

update lsusergrouprights set screenname='IDS_SCN_DASHBOARD' where displaytopic='IDS_SCN_DASHBOARD';
update lsusergrouprights set screenname='IDS_SCN_DASHBOARD' where displaytopic in ('IDS_TSK_ORDEROVERVIEW','IDS_TSK_ACTIVITIES');
update lsusergrouprights set screenname='IDS_SCN_SHEETORDERS' where displaytopic  in ('IDS_TSK_COMPLETEDWORK','IDS_TSK_ELNTASKORDER','IDS_TSK_RESEARCHACTIVITY','IDS_TSK_MANAGEEXCEL','IDS_TSK_SHEETEVALUATION','IDS_TSK_ORDERSHAREDBYME','IDS_TSK_ORDERSHAREDTOME','IDS_TSK_PENDINGWORK');
update lsusergrouprights set screenname='IDS_SCN_PROTOCOLORDERS' where displaytopic in ('IDS_TSK_ELNPROTOCOL','IDS_TSK_DYNAMICPROTOCOL');
update lsusergrouprights set screenname='IDS_SCN_SHEETTEMPLATE' where displaytopic='IDS_SCN_SHEETTEMPLATE' ;
update lsusergrouprights set screenname='IDS_SCN_PROTOCOLTEMPLATE' where displaytopic='IDS_TSK_NEWSTEP' ;
update lsusergrouprights set screenname='IDS_SCN_TEMPLATEMAPPING' where displaytopic='IDS_TSK_LIMSTESTORDER' ;
update lsusergrouprights set screenname='IDS_SCN_TASKMASTER' where displaytopic='IDS_SCN_TASKMASTER' ;
update lsusergrouprights set screenname='IDS_SCN_PROJECTMASTER' where displaytopic='IDS_SCN_PROJECTMASTER' ;
update lsusergrouprights set screenname='IDS_SCN_SAMPLEMASTER' where displaytopic='IDS_SCN_SAMPLEMASTER' ;
update lsusergrouprights set screenname='IDS_SCN_UNLOCKORDERS' where displaytopic='IDS_SCN_UNLOCKORDERS' ;
update lsusergrouprights set screenname='IDS_SCN_USERGROUP' where displaytopic='IDS_SCN_USERGROUP' ;
update lsusergrouprights set screenname='IDS_SCN_USERGROUP' where displaytopic='IDS_TSK_ACTDEACT' ;
update lsusergrouprights set screenname='IDS_SCN_USERMASTER' where displaytopic  in ('IDS_TSK_ACTDEACTUSERMASTER','IDS_TSK_RETIRE','IDS_TSK_IMPORTADS') ;
update lsusergrouprights set screenname='IDS_SCN_USERRIGHTS' where displaytopic='IDS_SCN_USERRIGHTS' ;
update lsusergrouprights set screenname='IDS_SCN_PROJECTTEAM' where displaytopic='IDS_SCN_PROJECTTEAM' ;
update lsusergrouprights set screenname='IDS_SCN_ORDERWORKLOW' where displaytopic='IDS_SCN_ORDERWORKLOW' ;
update lsusergrouprights set screenname='IDS_SCN_TEMPLATEWORKFLOW' where displaytopic='IDS_SCN_TEMPLATEWORKFLOW' ;
update lsusergrouprights set screenname='IDS_SCN_PASSWORDPOLICY' where displaytopic='IDS_SCN_PASSWORDPOLICY' ;
update lsusergrouprights set screenname='IDS_SCN_AUDITTRAILHIS' where displaytopic in ('IDS_SCN_AUDITTRAILHIS','IDS_TSK_REVIEWHISTORY','IDS_TSK_REVIEW','IDS_TSK_CREATEARCHIVE','IDS_TSK_OPENARCHIVE','IDS_TSK_EXPORT') ;
update lsusergrouprights set screenname='IDS_SCN_AUDITTRAILCONFIG' where displaytopic='IDS_SCN_AUDITTRAILCONFIG' ;
update lsusergrouprights set screenname='IDS_SCN_CFRSETTINGS' where displaytopic='IDS_SCN_CFRSETTINGS' ;
update lsusergrouprights set screenname='IDS_SCN_REPORTS' where displaytopic in ('IDS_SCN_REPORTS','IDS_TSK_GENERATEREPORT') ;
update lsusergrouprights set screenname='IDS_SCN_INSTRUMENTMASTER' where displaytopic='IDS_SCN_INSTRUMENTMASTER' ;
update lsusergrouprights set screenname='IDS_SCN_TEMPLATEMAPPING' where displaytopic='IDS_TSK_LIMSTASKORDER' ;
update lsusergrouprights set screenname='IDS_SCN_SITEMASTER' where displaytopic='IDS_SCN_SITEMASTER' ;
update lsusergrouprights set screenname='IDS_SCN_ACTIVEUSER' where displaytopic='IDS_SCN_ACTIVEUSER' ;
update lsusergrouprights set screenname='IDS_SCN_DOMAIN' where displaytopic='IDS_SCN_DOMAIN' ;
delete from lsusergrouprights where displaytopic='IDS_TSK_TEMPLATEDESIGN';

ALTER TABLE IF Exists lspasswordpolicy ADD COLUMN IF NOT EXISTS idletimeshowcheck integer;

update lspasswordpolicy set idletimeshowcheck=0 where idletimeshowcheck is null;


DO
$do$
DECLARE
    counter integer := 0;
BEGIN
  select count(*) into counter from datatype where datatypename = 'Number';

   IF counter=0 THEN       -- name is free
insert into datatype (datatypekey,datatypename)SELECT 3,'Number'
WHERE NOT EXISTS (select * from datatype where datatypename = 'Number'); 
   END IF;
END
$do$;
 
delete from datatype where datatype.datatypename='Integer';

update lsusergrouprights set modulename='IDS_MDL_LOGBOOK' where screenname='IDS_SCN_LOGBOOK';

INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (99, 'IDS_SCN_UNITMASTER', 'IDS_MDL_INVENTORY', '0', '0', '0', '0', '1,1,1',78,'IDS_SCN_UNITMASTER') ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (100, 'IDS_TSK_DOWNLOADPDFEXCEL', 'IDS_MDL_INVENTORY', '0', 'NA', 'NA', 'NA', '0,0,0',79,'IDS_SCN_UNITMASTER') ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (101, 'IDS_SCN_SECTIONMASTER', 'IDS_MDL_INVENTORY', '0', '0', '0', '0', '1,1,1',80,'IDS_SCN_SECTIONMASTER') ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (102, 'IDS_TSK_DOWNLOADPDFEXCELSECTION', 'IDS_MDL_INVENTORY', '0', 'NA', 'NA', 'NA', '0,0,0',81,'IDS_SCN_SECTIONMASTER') ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (103, 'IDS_SCN_STORAGELOCATION', 'IDS_MDL_INVENTORY', '0', '0', '0', '0', '1,1,1',82,'IDS_SCN_STORAGELOCATION') ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (104, 'IDS_SCN_MATERIALCATEGORY', 'IDS_MDL_INVENTORY', '0', '0', '0', '0', '1,1,1',83,'IDS_SCN_MATERIALCATEGORY') ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (105, 'IDS_TSK_DOWNLOADMATERILACATEGORY', 'IDS_MDL_INVENTORY', '0', 'NA', 'NA', 'NA', '0,0,0',84,'IDS_SCN_MATERIALCATEGORY') ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (106, 'IDS_SCN_MATERIAL', 'IDS_MDL_INVENTORY', '0', '0', 'NA', 'NA', '1,0,0',85,'IDS_SCN_MATERIAL') ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (107, 'IDS_SCN_MATERIALINVENTORY', 'IDS_MDL_INVENTORY', '0', '0', 'NA', 'NA', '1,0,0',86,'IDS_SCN_MATERIALINVENTORY') ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (108, 'IDS_TSK_RETIRELOGBOOK', 'IDS_MDL_LOGBOOK', '0', 'NA', 'NA', 'NA', '0,0,0',87,'IDS_SCN_LOGBOOK') ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (109, 'IDS_TSK_REVIEWLOGBOOK', 'IDS_MDL_LOGBOOK', '0', 'NA', 'NA', 'NA', '0,0,0',88,'IDS_SCN_LOGBOOK') ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (110, 'IDS_TSK_UPLOADSHEETORDER', 'IDS_MDL_ORDERS', '0', 'NA', 'NA', 'NA', '0,0,0',88,'IDS_SCN_SHEETORDERS') ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (111, 'IDS_TSK_UPLOADPROTOCOLORDER', 'IDS_MDL_ORDERS', '0', 'NA', 'NA', 'NA', '0,0,0',88,'IDS_SCN_PROTOCOLORDERS') ON CONFLICT(orderno)DO NOTHING;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_SCN_UNITMASTER', 'IDS_MDL_INVENTORY', 'administrator', '1', '1', '1', '1', 1,1,'IDS_SCN_UNITMASTER'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_SCN_UNITMASTER' and usergroupid_usergroupcode = 1); 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_DOWNLOADPDFEXCEL', 'IDS_MDL_INVENTORY', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_UNITMASTER'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_DOWNLOADPDFEXCEL' and usergroupid_usergroupcode = 1); 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_SCN_SECTIONMASTER', 'IDS_MDL_INVENTORY', 'administrator', '1', '1', '1', '1', 1,1,'IDS_SCN_SECTIONMASTER'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_SCN_SECTIONMASTER' and usergroupid_usergroupcode = 1); 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_DOWNLOADPDFEXCELSECTION', 'IDS_MDL_INVENTORY', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_SECTIONMASTER'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_DOWNLOADPDFEXCELSECTION' and usergroupid_usergroupcode = 1); 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_SCN_STORAGELOCATION', 'IDS_MDL_INVENTORY', 'administrator', '1', '1', '1', '1', 1,1,'IDS_SCN_STORAGELOCATION'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_SCN_STORAGELOCATION' and usergroupid_usergroupcode = 1); 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_SCN_MATERIALCATEGORY', 'IDS_MDL_INVENTORY', 'administrator', '1', '1', '1', '1', 1,1,'IDS_SCN_MATERIALCATEGORY'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_SCN_MATERIALCATEGORY' and usergroupid_usergroupcode = 1);
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_DOWNLOADMATERILACATEGORY', 'IDS_MDL_INVENTORY', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_MATERIALCATEGORY'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_DOWNLOADMATERILACATEGORY' and usergroupid_usergroupcode = 1);  
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_SCN_MATERIAL', 'IDS_MDL_INVENTORY', 'administrator', '1', '1', 'NA', 'NA', 1,1,'IDS_SCN_MATERIAL'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_SCN_MATERIAL' and usergroupid_usergroupcode = 1);
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_SCN_MATERIALINVENTORY', 'IDS_MDL_INVENTORY', 'administrator', '1', '1', 'NA', 'NA', 1,1,'IDS_SCN_MATERIALINVENTORY'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_SCN_MATERIALINVENTORY' and usergroupid_usergroupcode = 1);
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_RETIRELOGBOOK', 'IDS_MDL_LOGBOOK', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_LOGBOOK'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_RETIRELOGBOOK' and usergroupid_usergroupcode = 1);  
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_REVIEWLOGBOOK', 'IDS_MDL_LOGBOOK', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_LOGBOOK'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_REVIEWLOGBOOK' and usergroupid_usergroupcode = 1);    
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_UPLOADSHEETORDER', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_SHEETORDERS'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_UPLOADSHEETORDER' and usergroupid_usergroupcode = 1);    
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_UPLOADPROTOCOLORDER', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLORDERS'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_UPLOADPROTOCOLORDER' and usergroupid_usergroupcode = 1);    

update lsusergrouprightsmaster set sequenceorder=1 where orderno =2;
update lsusergrouprightsmaster set sequenceorder=2 where orderno =1;
update lsusergrouprightsmaster set sequenceorder=3 where orderno =3;
update lsusergrouprightsmaster set sequenceorder=4 where orderno =87;
update lsusergrouprightsmaster set sequenceorder=5 where orderno =7;
update lsusergrouprightsmaster set sequenceorder=6 where orderno =93;
update lsusergrouprightsmaster set sequenceorder=7 where orderno =85;
update lsusergrouprightsmaster set sequenceorder=8 where orderno =8;
update lsusergrouprightsmaster set sequenceorder=9 where orderno =9;
update lsusergrouprightsmaster set sequenceorder=10 where orderno =37;
update lsusergrouprightsmaster set sequenceorder=11 where orderno =38;
update lsusergrouprightsmaster set sequenceorder=12 where orderno =110;
update lsusergrouprightsmaster set sequenceorder=13 where orderno =61;
update lsusergrouprightsmaster set sequenceorder=14 where orderno =62;
update lsusergrouprightsmaster set sequenceorder=15 where orderno =96;
update lsusergrouprightsmaster set sequenceorder=16 where orderno =91;
update lsusergrouprightsmaster set sequenceorder=17 where orderno =86;
update lsusergrouprightsmaster set sequenceorder=18 where orderno =94;
update lsusergrouprightsmaster set sequenceorder=19 where orderno =48;
update lsusergrouprightsmaster set sequenceorder=20 where orderno =49;
update lsusergrouprightsmaster set sequenceorder=21 where orderno =111;
update lsusergrouprightsmaster set sequenceorder=22 where orderno =80;
update lsusergrouprightsmaster set sequenceorder=23 where orderno =81;
update lsusergrouprightsmaster set sequenceorder=24 where orderno =92;
update lsusergrouprightsmaster set sequenceorder=25 where orderno =10;
update lsusergrouprightsmaster set sequenceorder=26 where orderno =69;
update lsusergrouprightsmaster set sequenceorder=27 where orderno =70;
update lsusergrouprightsmaster set sequenceorder=28 where orderno =88;
update lsusergrouprightsmaster set sequenceorder=29 where orderno =51;
update lsusergrouprightsmaster set sequenceorder=30 where orderno =50;
update lsusergrouprightsmaster set sequenceorder=31 where orderno =57;
update lsusergrouprightsmaster set sequenceorder=32 where orderno =84;
update lsusergrouprightsmaster set sequenceorder=33 where orderno =83;
update lsusergrouprightsmaster set sequenceorder=34 where orderno =71;
update lsusergrouprightsmaster set sequenceorder=35 where orderno =72;
update lsusergrouprightsmaster set sequenceorder=36 where orderno =14;
update lsusergrouprightsmaster set sequenceorder=37 where orderno =11;
update lsusergrouprightsmaster set sequenceorder=38 where orderno =12;
update lsusergrouprightsmaster set sequenceorder=39 where orderno =13;
update lsusergrouprightsmaster set sequenceorder=40 where orderno =65;
update lsusergrouprightsmaster set sequenceorder=41 where orderno =66;
update lsusergrouprightsmaster set sequenceorder=42 where orderno =67;
update lsusergrouprightsmaster set sequenceorder=43 where orderno =78;
update lsusergrouprightsmaster set sequenceorder=44 where orderno =17;
update lsusergrouprightsmaster set sequenceorder=45 where orderno =21;
update lsusergrouprightsmaster set sequenceorder=46 where orderno =16;
update lsusergrouprightsmaster set sequenceorder=47 where orderno =23;
update lsusergrouprightsmaster set sequenceorder=48 where orderno =22;
update lsusergrouprightsmaster set sequenceorder=49 where orderno =24;
update lsusergrouprightsmaster set sequenceorder=50 where orderno =52;
update lsusergrouprightsmaster set sequenceorder=51 where orderno =40;
update lsusergrouprightsmaster set sequenceorder=52 where orderno =18;
update lsusergrouprightsmaster set sequenceorder=53 where orderno =19;
update lsusergrouprightsmaster set sequenceorder=54 where orderno =20;
update lsusergrouprightsmaster set sequenceorder=55 where orderno =41;
update lsusergrouprightsmaster set sequenceorder=56 where orderno =43;
update lsusergrouprightsmaster set sequenceorder=57 where orderno =25;
update lsusergrouprightsmaster set sequenceorder=58 where orderno =28;
update lsusergrouprightsmaster set sequenceorder=59 where orderno =29;
update lsusergrouprightsmaster set sequenceorder=60 where orderno =30;
update lsusergrouprightsmaster set sequenceorder=61 where orderno =31;
update lsusergrouprightsmaster set sequenceorder=62 where orderno =32;
update lsusergrouprightsmaster set sequenceorder=63 where orderno =26;
update lsusergrouprightsmaster set sequenceorder=64 where orderno =27;
update lsusergrouprightsmaster set sequenceorder=65 where orderno =34;
update lsusergrouprightsmaster set sequenceorder=66 where orderno =58;
update lsusergrouprightsmaster set sequenceorder=67 where orderno =45;
update lsusergrouprightsmaster set sequenceorder=68 where orderno =35;
update lsusergrouprightsmaster set sequenceorder=69 where orderno =90;
update lsusergrouprightsmaster set sequenceorder=70 where orderno =89;
update lsusergrouprightsmaster set sequenceorder=71 where orderno =53;
update lsusergrouprightsmaster set sequenceorder=72 where orderno =68;
update lsusergrouprightsmaster set sequenceorder=73 where orderno =39;
update lsusergrouprightsmaster set sequenceorder=74 where orderno =54;
update lsusergrouprightsmaster set sequenceorder=75 where orderno =55;
update lsusergrouprightsmaster set sequenceorder=76 where orderno =56;
update lsusergrouprightsmaster set sequenceorder=77 where orderno =99;
update lsusergrouprightsmaster set sequenceorder=78 where orderno =100;
update lsusergrouprightsmaster set sequenceorder=79 where orderno =101;
update lsusergrouprightsmaster set sequenceorder=80 where orderno =102;
update lsusergrouprightsmaster set sequenceorder=81 where orderno =103;
update lsusergrouprightsmaster set sequenceorder=82 where orderno =104;
update lsusergrouprightsmaster set sequenceorder=83 where orderno =105;
update lsusergrouprightsmaster set sequenceorder=84 where orderno =106;
update lsusergrouprightsmaster set sequenceorder=85 where orderno =107;
update lsusergrouprightsmaster set sequenceorder=86 where orderno =95;
update lsusergrouprightsmaster set sequenceorder=87 where orderno =95;
update lsusergrouprightsmaster set sequenceorder=88 where orderno =97;
update lsusergrouprightsmaster set sequenceorder=89 where orderno =98;
update lsusergrouprightsmaster set sequenceorder=90 where orderno =108;
update lsusergrouprightsmaster set sequenceorder=91 where orderno =109;


update lsusergrouprightsmaster set sedit='0',sdelete='0' where orderno=106;
update lsusergrouprightsmaster set sedit='0',sdelete='0' where orderno=107;
update lsusergrouprightsmaster set status='1,1,1' where orderno=106;
update lsusergrouprightsmaster set status='1,1,1' where orderno=107;
update lsusergrouprights set screate='NA',sedit='NA',  sdelete='NA' where displaytopic='IDS_TSK_RETIRELOGBOOK';
update lsusergrouprights set screate='NA',sedit='NA', sdelete='NA' where displaytopic='IDS_TSK_REVIEWLOGBOOK';
update lsusergrouprights set sedit='1', sdelete='1' where displaytopic='IDS_SCN_MATERIAL' and sedit='NA' and sdelete='NA';
update lsusergrouprights set sedit='1', sdelete='1' where displaytopic='IDS_SCN_MATERIALINVENTORY' and sedit='NA' and sdelete='NA';

delete from lsusergrouprights where displaytopic ='IDS_TSK_LIMSTASKORDER';
delete from lsusergrouprightsmaster where displaytopic='IDS_TSK_LIMSTESTORDER';

update lsfields set fieldorderno=1 where level04name='Notification' and level01code='G1';
update lsfields set fieldorderno=2 where level04name='ComboBox' and level01code='G1';
update lsfields set fieldorderno=3 where level04name='Current Date' and level01code='G1';
update lsfields set fieldorderno=4 where level04name='Dynamic ComboBox' and level01code='G1';
update lsfields set fieldorderno=5 where level04name='Loged User' and level01code='G1';
update lsfields set fieldorderno=6 where level04name='Login Time' and level01code='G1';
update lsfields set fieldorderno=7 where level04name='Mandatory Field' and level01code='G1';
update lsfields set fieldorderno=8 where level04name='Manual Date' and level01code='G1';
update lsfields set fieldorderno=9 where level04name='Manual Date & Time' and level01code='G1';
update lsfields set fieldorderno=10 where level04name='Manual Field' and level01code='G1';
update lsfields set fieldorderno=11 where level04name='Manual Time' and level01code='G1';
update lsfields set fieldorderno=12 where level04name='Numeric Field' and level01code='G1';
update lsfields set fieldorderno=13 where level04name='Order No' and level01code='G1';
update lsfields set fieldorderno=14 where level04name='Project Master' and level01code='G1';
update lsfields set fieldorderno=15 where level04name='Resource Detail' and level01code='G1';
update lsfields set fieldorderno=16 where level04name='Sample Name' and level01code='G1';
update lsfields set fieldorderno=17 where level04name='Signature' and level01code='G1';
update lsfields set fieldorderno=18 where level04name='Test Name' and level01code='G1';


INSERT INTO public.materialtype (nmaterialtypecode, jsondata, ndefaultstatus, nsitecode, nstatus) VALUES (-1, '{"sdescription": "NA", "smaterialtypename": {"en-US": "NA", "ru-RU": "нет данных", "tg-TG": "НА"}}', 4, -1, 1)on conflict (nmaterialtypecode) do nothing; 

INSERT INTO public.materialtype (nmaterialtypecode, jsondata, ndefaultstatus, nsitecode, nstatus) VALUES (2, '{"prefix": "V", "sdescription": "Volumetric Type", "needSectionwise": 3, "smaterialtypename": {"en-US": "Volumetric Type", "ru-RU": "Объемный тип", "tg-TG": "Навъи ҳаҷмӣ"}, "ismaterialSectionneed": 4}', 4, -1, 1)on conflict (nmaterialtypecode) do nothing; 

INSERT INTO public.materialtype (nmaterialtypecode, jsondata, ndefaultstatus, nsitecode, nstatus) VALUES (3, '{"prefix": "M", "sdescription": "Material Inventory Type", "needSectionwise": 3, "smaterialtypename": {"en-US": "Material Inventory Type", "ru-RU": "Тип инвентаризации материалов", "tg-TG": "Навъи инвентаризатсияи мавод"}, "ismaterialSectionneed": 3}', 4, -1, 1)on conflict (nmaterialtypecode) do nothing; 

INSERT INTO public.materialtype (nmaterialtypecode, jsondata, ndefaultstatus, nsitecode, nstatus) VALUES (1, '{"prefix": "S", "sdescription": "Standared Type", "needSectionwise": 3, "smaterialtypename": {"en-US": "Standard Type", "ru-RU": "Стандартный тип", "tg-TG": "Навъи стандартӣ"}, "ismaterialSectionneed": 3}', 3, -1, 1)on conflict (nmaterialtypecode) do nothing; 

INSERT INTO public.materialtype (nmaterialtypecode, jsondata, ndefaultstatus, nsitecode, nstatus) VALUES (4, '{"prefix": "I", "sdescription": "IQC Standard Material Type", "needSectionwise": 3, "smaterialtypename": {"en-US": "IQC Standard Material Type", "ru-RU": "Стандартный тип материала IQC", "tg-TG": "Стандарти намуди маводи IQC"}, "ismaterialSectionneed": 4}', 4, -1, 1)on conflict (nmaterialtypecode) do nothing; 

DO
$do$
DECLARE
    counter integer := 0;
BEGIN
  select count(*) into counter from methoddelimiter where delimiterkey = 2 and parsermethodkey =1; --resultwithoutspace for datablock

   IF counter=0 THEN       -- name is free
INSERT into methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue)
SELECT 1,1,2,1,1
WHERE NOT EXISTS (select * from methoddelimiter where delimiterkey = 2 and parsermethodkey =1); 
   END IF;
END
$do$;

DO
$do$
DECLARE
    counter integer := 0;
BEGIN
  select count(*) into counter from methoddelimiter where delimiterkey = 3 and parsermethodkey =1;--resultwithspace for datablock

   IF counter=0 THEN       -- name is free
INSERT into methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue)
SELECT 1,1,3,1,1
WHERE NOT EXISTS (select * from methoddelimiter where delimiterkey = 3 and parsermethodkey =1); 
   END IF;
END
$do$;

DO
$do$
DECLARE
    counter integer := 0;
BEGIN
  select count(*) into counter from methoddelimiter where delimiterkey = 5 and parsermethodkey =1;--comma for datablock

   IF counter=0 THEN       -- name is free
INSERT into methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue)
SELECT 1,1,5,1,1
WHERE NOT EXISTS (select * from methoddelimiter where delimiterkey = 5 and parsermethodkey =1); 
   END IF;
END
$do$;

--for split
DO
$do$
DECLARE
    counter integer := 0;
BEGIN
  select count(*) into counter from methoddelimiter where delimiterkey = 2 and parsermethodkey =7;--resultwithoutspace for split

   IF counter=0 THEN       -- name is free
INSERT into methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue)
SELECT 1,1,2,7,1
WHERE NOT EXISTS (select * from methoddelimiter where delimiterkey = 2 and parsermethodkey =7); 
   END IF;
END
$do$;

DO
$do$
DECLARE
    counter integer := 0;
BEGIN
  select count(*) into counter from methoddelimiter where delimiterkey = 3 and parsermethodkey =7;--resultwithspace for split

   IF counter=0 THEN       -- name is free
INSERT into methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue)
SELECT 1,1,3,7,1
WHERE NOT EXISTS (select * from methoddelimiter where delimiterkey = 3 and parsermethodkey =7); 
   END IF;
END
$do$;

DO
$do$
DECLARE
    counter integer := 0;
BEGIN
  select count(*) into counter from methoddelimiter where delimiterkey = 4 and parsermethodkey =7;--colon for split

   IF counter=0 THEN       -- name is free
INSERT into methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue)
SELECT 1,1,4,7,1
WHERE NOT EXISTS (select * from methoddelimiter where delimiterkey = 4 and parsermethodkey =7); 
   END IF;
END
$do$;

DO
$do$
DECLARE
    counter integer := 0;
BEGIN
  select count(*) into counter from methoddelimiter where delimiterkey = 5 and parsermethodkey =7;--comma for split

   IF counter=0 THEN       -- name is free
INSERT into methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue)
SELECT 1,1,5,7,1
WHERE NOT EXISTS (select * from methoddelimiter where delimiterkey = 5 and parsermethodkey =7); 
   END IF;
END
$do$;

DO
$do$
DECLARE
    counter integer := 0;
BEGIN
  select count(*) into counter from methoddelimiter where delimiterkey = 6 and parsermethodkey =7;--space for split

   IF counter=0 THEN       -- name is free
INSERT into methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue)
SELECT 1,1,6,7,1
WHERE NOT EXISTS (select * from methoddelimiter where delimiterkey = 6 and parsermethodkey =7); 
   END IF;
END
$do$;

DO
$do$
DECLARE
    counter integer := 0;
BEGIN
  select count(*) into counter from methoddelimiter where delimiterkey = 7 and parsermethodkey =7;--splitdot for split

   IF counter=0 THEN       -- name is free
INSERT into methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue)
SELECT 1,1,7,7,1
WHERE NOT EXISTS (select * from methoddelimiter where delimiterkey = 7 and parsermethodkey =7); 
   END IF;
END
$do$;

DO
$do$
DECLARE
    counter integer := 0;
BEGIN
  select count(*) into counter from methoddelimiter where delimiterkey = 8 and parsermethodkey =7;--mergedot for split

   IF counter=0 THEN       -- name is free
INSERT into methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue)
SELECT 1,1,8,7,1
WHERE NOT EXISTS (select * from methoddelimiter where delimiterkey = 8 and parsermethodkey =7); 
   END IF;
END
$do$;

DO
$do$
DECLARE
    counter integer := 0;
BEGIN
  select count(*) into counter from methoddelimiter where delimiterkey = 9 and parsermethodkey =7;--slash for split

   IF counter=0 THEN       -- name is free
INSERT into methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue)
SELECT 1,1,9,7,1
WHERE NOT EXISTS (select * from methoddelimiter where delimiterkey = 9 and parsermethodkey =7); 
   END IF;
END
$do$;

---merge

DO
$do$
DECLARE
    counter integer := 0;
BEGIN
  select count(*) into counter from methoddelimiter where delimiterkey = 8 and parsermethodkey =6;--mergedot for merge

   IF counter=0 THEN       -- name is free
INSERT into methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue)
SELECT 1,1,8,6,1
WHERE NOT EXISTS (select * from methoddelimiter where delimiterkey = 8 and parsermethodkey =6); 
   END IF;
END
$do$;

DO
$do$
DECLARE
    counter integer := 0;
BEGIN
  select count(*) into counter from methoddelimiter where delimiterkey = 9 and parsermethodkey =6;--slash for merge

   IF counter=0 THEN       -- name is free
INSERT into methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue)
SELECT 1,1,9,6,1
WHERE NOT EXISTS (select * from methoddelimiter where delimiterkey = 9 and parsermethodkey =6); 
   END IF;
END
$do$;

DO
$do$
DECLARE
    counter integer := 0;
BEGIN
  select count(*) into counter from methoddelimiter where delimiterkey = 4 and parsermethodkey =6;--colon for merge

   IF counter=0 THEN       -- name is free
INSERT into methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue)
SELECT 1,1,4,6,1
WHERE NOT EXISTS (select * from methoddelimiter where delimiterkey = 4 and parsermethodkey =6); 
   END IF;
END
$do$;

DO
$do$
DECLARE
    counter integer := 0;
BEGIN
  select count(*) into counter from methoddelimiter where delimiterkey = 5 and parsermethodkey =6;--comma for merge

   IF counter=0 THEN       -- name is free
INSERT into methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue)
SELECT 1,1,5,6,1
WHERE NOT EXISTS (select * from methoddelimiter where delimiterkey = 5 and parsermethodkey =6); 
   END IF;
END
$do$;

DO
$do$
DECLARE
    counter integer := 0;
BEGIN
  select count(*) into counter from methoddelimiter where delimiterkey = 6 and parsermethodkey =6;--space for merge

   IF counter=0 THEN       -- name is free
INSERT into methoddelimiter (status, usercode, delimiterkey, parsermethodkey,defaultvalue)
SELECT 1,1,6,6,1
WHERE NOT EXISTS (select * from methoddelimiter where delimiterkey = 6 and parsermethodkey =6); 
   END IF;
END
$do$;

update methoddelimiter set defaultvalue = 1 where defaultvalue is Null; 

INSERT INTO delimiter (delimitername,actualdelimiter,status,usercode,defaultvalue) SELECT 'Tab', '\t', 1, 1,1 WHERE NOT EXISTS (SELECT delimitername FROM delimiter WHERE delimitername = 'Tab');

CREATE TABLE IF NOT EXISTS public.lslogilablimsorder
(
    orderid numeric(17,0) NOT NULL,
    batchid character varying(250) COLLATE pg_catalog."default",
    completedtimestamp timestamp without time zone,
    createdtimestamp timestamp without time zone,
    instrumentcode character varying(100) COLLATE pg_catalog."default",
    instrumentname character varying(100) COLLATE pg_catalog."default",
    methodcode character varying(100) COLLATE pg_catalog."default",
    orderflag character(10) COLLATE pg_catalog."default",
    parserflag character(10) COLLATE pg_catalog."default",
    replicateid character varying(100) COLLATE pg_catalog."default",
    sampleid character varying(250) COLLATE pg_catalog."default",
    testcode character varying(100) COLLATE pg_catalog."default",
    CONSTRAINT lslogilablimsorder_pkey PRIMARY KEY (orderid)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.lslogilablimsorder
    OWNER to postgres;
    
 INSERT INTO parserignorechars(ignorechars)SELECT 'â†' WHERE NOT EXISTS (SELECT ignorechars FROM parserignorechars WHERE ignorechars = 'â†');
 INSERT INTO parserignorechars(ignorechars)SELECT 'â' WHERE NOT EXISTS (SELECT ignorechars FROM parserignorechars WHERE ignorechars = 'â');
 update lspasswordpolicy set idletimeshowcheck=1 where idletimeshowcheck=0;
 
 delete from lsaudittrailconfigmaster where serialno in (132,133,134,135);
 Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(100,0,'IDS_MDL_ORDERS',1,'IDS_SCN_SHEETORDERS','IDS_TSK_NEWFOLDER') ON CONFLICT(serialno)DO NOTHING;
 Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(101,0,'IDS_MDL_ORDERS',1,'IDS_SCN_SHEETORDERS','IDS_TSK_MOVEORDER') ON CONFLICT(serialno)DO NOTHING;
 Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(102,0,'IDS_MDL_ORDERS',1,'IDS_SCN_SHEETORDERS','IDS_TSK_UPLOAD') ON CONFLICT(serialno)DO NOTHING;
 Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(105,0,'IDS_MDL_ORDERS',1,'IDS_SCN_SHEETORDERS','IDS_TSK_DELETE') ON CONFLICT(serialno)DO NOTHING;
 Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(107,0,'IDS_MDL_ORDERS',2,'IDS_SCN_PROTOCOLORDERS','IDS_TSK_OPEN') ON CONFLICT(serialno)DO NOTHING;
 Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(108,0,'IDS_MDL_ORDERS',2,'IDS_SCN_PROTOCOLORDERS','IDS_TSK_DELETE') ON CONFLICT(serialno)DO NOTHING;
 Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(109,0,'IDS_MDL_ORDERS',2,'IDS_SCN_PROTOCOLORDERS','IDS_TSK_NEWFOLDER') ON CONFLICT(serialno)DO NOTHING;
 Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(111,0,'IDS_MDL_ORDERS',2,'IDS_SCN_PROTOCOLORDERS','IDS_TSK_UPLOAD') ON CONFLICT(serialno)DO NOTHING;
 Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(112,0,'IDS_MDL_ORDERS',2,'IDS_SCN_PROTOCOLORDERS','IDS_TSK_EXPORT') ON CONFLICT(serialno)DO NOTHING;
 Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(113,0,'IDS_MDL_TEMPLATES',3,'IDS_SCN_SHEETTEMPLATES','IDS_TSK_NEWTEMPLATE') ON CONFLICT(serialno)DO NOTHING;
 Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(114,0,'IDS_MDL_TEMPLATES',3,'IDS_SCN_SHEETTEMPLATES','IDS_TSK_EXPORT') ON CONFLICT(serialno)DO NOTHING;
 Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(115,0,'IDS_MDL_MASTERS',6,'IDS_SCN_UNLOCKORDERS','IDS_TSK_UNLOCK') ON CONFLICT(serialno)DO NOTHING;
 Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(116,0,'IDS_MDL_SETUP',7,'IDS_SCN_USERMASTER','IDS_TSK_RETIRE') ON CONFLICT(serialno)DO NOTHING;
 Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(117,0,'IDS_MDL_AUDITTRAIL',10,'IDS_SCN_AUDITTRAILHIS','IDS_TSK_REVIEW') ON CONFLICT(serialno)DO NOTHING;
 Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(118,0,'IDS_MDL_INVENTORY',13,'IDS_SCN_UNITMASTER','IDS_TSK_SAVE') ON CONFLICT(serialno)DO NOTHING;
 Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(119,0,'IDS_MDL_INVENTORY',13,'IDS_SCN_UNITMASTER','IDS_TSK_DELETE') ON CONFLICT(serialno)DO NOTHING;
 Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(120,0,'IDS_MDL_INVENTORY',13,'IDS_SCN_SECTIONMASTER','IDS_TSK_SAVE') ON CONFLICT(serialno)DO NOTHING;
 Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(121,0,'IDS_MDL_INVENTORY',13,'IDS_SCN_SECTIONMASTER','IDS_TSK_DELETE') ON CONFLICT(serialno)DO NOTHING;
 Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(122,0,'IDS_MDL_INVENTORY',13,'IDS_SCN_STORAGELOCATION','IDS_TSK_SAVE') ON CONFLICT(serialno)DO NOTHING;
 Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(123,0,'IDS_MDL_INVENTORY',13,'IDS_SCN_STORAGELOCATION','IDS_TSK_DELETE') ON CONFLICT(serialno)DO NOTHING;
 Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(124,0,'IDS_MDL_INVENTORY',13,'IDS_SCN_MATERIALCATEGORY','IDS_TSK_SAVE') ON CONFLICT(serialno)DO NOTHING;
 Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(125,0,'IDS_MDL_INVENTORY',13,'IDS_SCN_MATERIALCATEGORY','IDS_TSK_DELETE') ON CONFLICT(serialno)DO NOTHING;
 Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(126,0,'IDS_MDL_INVENTORY',13,'IDS_SCN_MATERIAL','IDS_TSK_SAVE') ON CONFLICT(serialno)DO NOTHING;
 Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(127,0,'IDS_MDL_INVENTORY',13,'IDS_SCN_MATERIAL','IDS_TSK_DELETE') ON CONFLICT(serialno)DO NOTHING;
 Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(128,0,'IDS_MDL_INVENTORY',13,'IDS_SCN_MATERIALINVENTORY','IDS_TSK_SAVE') ON CONFLICT(serialno)DO NOTHING;
 Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(129,0,'IDS_MDL_INVENTORY',13,'IDS_SCN_MATERIALINVENTORY','IDS_TSK_DELETE') ON CONFLICT(serialno)DO NOTHING;
 Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(130,0,'IDS_MDL_ORDERS',2,'IDS_SCN_PROTOCOLORDERS','IDS_TSK_MOVEORDER') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(131,0,'IDS_MDL_ORDERS',2,'IDS_SCN_SHEETORDERS','IDS_TSK_OPENORDER') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(132,0,'IDS_MDL_LOGBOOK',10,'IDS_SCN_LOGBOOK','IDS_TSK_ADDLOGBOOK') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(133,0,'IDS_MDL_LOGBOOK',10,'IDS_SCN_LOGBOOK','IDS_TSK_EDITLOGBOOK') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(134,0,'IDS_MDL_LOGBOOK',10,'IDS_SCN_LOGBOOK','IDS_TSK_REVIEWLOGBOOK') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(135,0,'IDS_MDL_LOGBOOK',10,'IDS_SCN_LOGBOOK','IDS_TSK_RETIRELOGBOOK') ON CONFLICT(serialno)DO NOTHING;

 delete from lsaudittrailconfigmaster where serialno in (30,31,36,52,53);
 
 update lsaudittrailconfigmaster set  modulename='IDS_MDL_ORDERS' where serialno in (102,38,40,76,98,99,131);
 update lsaudittrailconfigmaster set modulename='IDS_MDL_ORDERS',screenname='IDS_SCN_SHEETORDERS' where serialno=101;
 update lsaudittrailconfigmaster set screenname='IDS_SCN_INVENTORY' where screenname='IDS_SCN_REPOSITORY';
 update lsaudittrailconfigmaster set taskname='IDS_TSK_ADDREPO' where serialno=71;
 update lsaudittrailconfigmaster set taskname='IDS_TSK_EDITREPO' where serialno=72;
 update lsaudittrailconfigmaster set taskname='IDS_TSK_EXPORTPDF' where serialno=61;
 
update lsaudittrailconfiguration set taskname='IDS_TSK_EXPORTPDF' where screenname='IDS_SCN_PROTOCOLTEMP' and taskname='IDS_TSK_DELETE';
update lsaudittrailconfiguration set ordersequnce=9 where taskname in ('IDS_TSK_ADDREPO','IDS_TSK_EDITREPO');
update lsaudittrailconfiguration set ordersequnce=2 where taskname='IDS_TSK_MOVEORDER' and screenname='IDS_SCN_PROTOCOLORDERS';

update lsaudittrailconfiguration set ordersequnce=1 where taskname='IDS_TSK_NEWFOLDER' and screenname='IDS_SCN_SHEETORDERS' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=2 where taskname='IDS_TSK_REGISTER' and screenname='IDS_SCN_SHEETORDERS'  and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=3 where taskname='IDS_TSK_UPLOAD' and screenname='IDS_SCN_SHEETORDERS'  and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=4 where taskname='IDS_TSK_SAVE' and screenname='IDS_SCN_SHEETORDERS'  and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=5 where taskname='IDS_TSK_COMPLETETASK' and screenname='IDS_SCN_SHEETORDERS'  and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=6 where taskname='IDS_TSK_EXPORT' and screenname='IDS_SCN_SHEETORDERS'  and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=7 where taskname='IDS_TSK_EXPORTASJSON' and screenname='IDS_SCN_SHEETORDERS'  and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=8 where taskname='IDS_TSK_PROCESSORDER' and screenname='IDS_SCN_SHEETORDERS'  and lsusermaster_usercode=1 ;
 update lsaudittrailconfiguration set ordersequnce=9 where taskname='IDS_TSK_OPENORDER' and screenname='IDS_SCN_SHEETORDERS'  and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=10 where taskname='IDS_TSK_MOVEORDER' and screenname='IDS_SCN_SHEETORDERS'  and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=11 where taskname='IDS_TSK_DELETE' and screenname='IDS_SCN_SHEETORDERS' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=12 where taskname='IDS_TSK_PARSEDATA' and screenname='IDS_SCN_SHEETORDERS'  and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=13 where taskname='IDS_TSK_SENDTOLIMS' and screenname='IDS_SCN_SHEETORDERS'  and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=14 where taskname='IDS_TSK_NEWFOLDER' and screenname='IDS_SCN_PROTOCOLORDERS' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=15 where taskname='IDS_TSK_REGPROTOCOL'and screenname='IDS_SCN_PROTOCOLORDERS' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=16 where taskname='IDS_TSK_UPLOAD'and screenname='IDS_SCN_PROTOCOLORDERS' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=17 where taskname='IDS_TSK_SAVE'and screenname='IDS_SCN_PROTOCOLORDERS' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=18 where taskname='IDS_TSK_COMPLETETASK'and screenname='IDS_SCN_PROTOCOLORDERS' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=19 where taskname='IDS_TSK_EXPORT'and screenname='IDS_SCN_PROTOCOLORDERS' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=20 where taskname='IDS_TSK_PROCESSPROTOCOL'and screenname='IDS_SCN_PROTOCOLORDERS' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=21 where taskname='IDS_TSK_OPEN' and screenname='IDS_SCN_PROTOCOLORDERS' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=22 where taskname='IDS_TSK_MOVEORDER'and screenname='IDS_SCN_PROTOCOLORDERS' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=23 where taskname='IDS_TSK_DELETE'and screenname='IDS_SCN_PROTOCOLORDERS' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=24 where taskname='IDS_TSK_NEWTEMPLATE'and screenname='IDS_SCN_SHEETTEMPLATES' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=25 where taskname='IDS_TSK_SAVE'and screenname='IDS_SCN_SHEETTEMPLATES' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=26 where taskname='IDS_TSK_SAVEAS'and screenname='IDS_SCN_SHEETTEMPLATES' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=27 where taskname='IDS_TSK_EXPORT'and screenname='IDS_SCN_SHEETTEMPLATES' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=28 where taskname='IDS_TSK_ADDPROTOCOL'and screenname='IDS_SCN_PROTOCOLTEMP' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=29 where taskname='IDS_TSK_NEWSTEP'and screenname='IDS_SCN_PROTOCOLTEMP' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=30 where taskname='IDS_TSK_DELETE'and screenname='IDS_SCN_PROTOCOLTEMP' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=31 where taskname='IDS_TSK_SHAREWITHTEAM'and screenname='IDS_SCN_PROTOCOLTEMP' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=32 where taskname='IDS_TSK_SAVE' and screenname='IDS_SCN_TEMPLATEMAPPING' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=33 where taskname='IDS_TSK_SAVE' and screenname='IDS_SCN_TASKMASTER' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=34 where taskname='IDS_TSK_DELETE' and screenname='IDS_SCN_TASKMASTER' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=35 where taskname='IDS_TSK_SAVE' and screenname='IDS_SCN_PROJECTMASTER' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=36 where taskname='IDS_TSK_DELETE' and screenname='IDS_SCN_PROJECTMASTER' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=37 where taskname='IDS_TSK_SAVE' and screenname='IDS_SCN_SAMPLEMASTER' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=38 where taskname='IDS_TSK_DELETE'and screenname='IDS_SCN_SAMPLEMASTER' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=39 where taskname='IDS_TSK_ADDREPO'and screenname='IDS_SCN_INVENTORY' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=40 where taskname='IDS_TSK_EDITREPO'and screenname='IDS_SCN_INVENTORY' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=41 where taskname='IDS_TSK_SAVE' and screenname='IDS_SCN_INVENTORY' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=42 where taskname='IDS_TSK_EDIT' and screenname='IDS_SCN_INVENTORY' and lsusermaster_usercode=1; 
 update lsaudittrailconfiguration set ordersequnce=43 where taskname='IDS_TSK_DELETE' and screenname='IDS_SCN_INVENTORY' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=44 where taskname='IDS_TSK_UNLOCK' and screenname='IDS_SCN_UNLOCKORDERS' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=45 where taskname='IDS_TSK_SAVE' and screenname='IDS_SCN_SITEMASTER' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=46 where taskname='IDS_TSK_ACTDEACT' and screenname='IDS_SCN_SITEMASTER' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=47 where taskname='IDS_TSK_ADDNEWGROUP' and screenname='IDS_SCN_USERGROUP' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=48 where taskname='IDS_TSK_EDIT' and screenname='IDS_SCN_USERGROUP' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=49 where taskname='IDS_TSK_ACTDEACT' and screenname='IDS_SCN_USERGROUP' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=50 where taskname='IDS_TSK_ADDUSER' and screenname='IDS_SCN_USERMASTER' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=51 where taskname='IDS_TSK_EDIT' and screenname='IDS_SCN_USERMASTER' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=52 where taskname='IDS_TSK_UNLOCK' and screenname='IDS_SCN_USERMASTER' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=53 where taskname='IDS_TSK_ACTDEACT' and screenname='IDS_SCN_USERMASTER' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=54 where taskname='IDS_TSK_RESETPASSWORD' and screenname='IDS_SCN_USERMASTER' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=55 where taskname='IDS_TSK_RETIRE' and screenname='IDS_SCN_USERMASTER' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=56 where taskname='IDS_TSK_IMPORTADS' and screenname='IDS_SCN_USERMASTER' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=57 where taskname='IDS_TSK_CONNECT' and screenname='IDS_SCN_USERMASTER' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=58 where taskname='IDS_TSK_SAVE' and screenname='IDS_SCN_USERRIGHTS' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=59 where taskname='IDS_TSK_SAVE' and screenname='IDS_SCN_PROJECTTEAM' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=60 where taskname='IDS_TSK_DELETE' and screenname='IDS_SCN_PROJECTTEAM' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=61 where taskname='IDS_TSK_SAVE' and screenname='IDS_SCN_ORDERWORKFLOW' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=62 where taskname='IDS_TSK_DELETE' and screenname='IDS_SCN_ORDERWORKFLOW' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=63 where taskname='IDS_TSK_SAVE' and screenname='IDS_SCN_TEMPLATEWORKFLOW' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=64 where taskname='IDS_TSK_DELETE' and screenname='IDS_SCN_TEMPLATEWORKFLOW' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=65 where taskname='IDS_TSK_SAVE' and screenname='IDS_SCN_DOMAIN' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=66 where taskname='IDS_TSK_DELETE' and screenname='IDS_SCN_DOMAIN' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=67 where taskname='IDS_TSK_SAVE' and screenname='IDS_SCN_PASSWORDPOLICY' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=68 where taskname='IDS_TSK_REVIEWHIS' and screenname='IDS_SCN_AUDITTRAILHIS' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=69 where taskname='IDS_TSK_REVIEW' and screenname='IDS_SCN_AUDITTRAILHIS' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=70 where taskname='IDS_TSK_CREATEARCHIVE' and screenname='IDS_SCN_AUDITTRAILHIS' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=71 where taskname='IDS_TSK_OPENARCHIVE' and screenname='IDS_SCN_AUDITTRAILHIS' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=72 where taskname='IDS_TSK_EXPORT' and screenname='IDS_SCN_AUDITTRAILHIS' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=73 where taskname='IDS_TSK_SAVE' and screenname='IDS_SCN_CFRSETTINGS' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=74 where taskname='IDS_TSK_DELETE' and screenname='IDS_SCN_CFRSETTINGS' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=75 where taskname='IDS_TSK_SAVE' and screenname='IDS_SCN_AUDITTRAILCONFIG' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=76 where taskname='IDS_TSK_CONFIGURE' and screenname='IDS_SCN_REPORTS' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=77 where taskname='IDS_TSK_DIRECTORYSAVE' and screenname='IDS_SCN_REPORTS' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=78 where taskname='IDS_TSK_NEWTEMP' and screenname='IDS_SCN_REPORTS' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=79 where taskname='IDS_TSK_SAVE' and screenname='IDS_SCN_REPORTS' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=80 where taskname='IDS_TSK_SAVEAS' and screenname='IDS_SCN_REPORTS' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=81 where taskname='IDS_TSK_GENERATEREPORT' and screenname='IDS_SCN_REPORTS' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=82 where taskname='IDS_TSK_SAVE' and screenname='IDS_SCN_INSTRUMENTCATEGORY' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=83 where taskname='IDS_TSK_DELETE' and screenname='IDS_SCN_INSTRUMENTCATEGORY' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=84 where taskname='IDS_TSK_SAVE' and screenname='IDS_SCN_INSTRUMENTMASTER' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=85 where taskname='IDS_TSK_EDIT' and screenname='IDS_SCN_INSTRUMENTMASTER' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=86 where taskname='IDS_TSK_DELETE' and screenname='IDS_SCN_INSTRUMENTMASTER' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=87 where taskname='IDS_TSK_SAVE' and screenname='IDS_SCN_DELIMITER' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=88 where taskname='IDS_TSK_EDIT' and screenname='IDS_SCN_DELIMITER'  and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=89 where taskname='IDS_TSK_DELETE' and screenname='IDS_SCN_DELIMITER' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=90 where taskname='IDS_TSK_SAVE' and screenname='IDS_SCN_METHODDELIMITER' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=91 where taskname='IDS_TSK_EDIT' and screenname='IDS_SCN_METHODDELIMITER' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=92 where taskname='IDS_TSK_DELETE' and screenname='IDS_SCN_METHODDELIMITER' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=93 where taskname='IDS_TSK_SAVE' and screenname='IDS_SCN_METHODMASTER' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=94 where taskname='IDS_TSK_EDIT' and screenname='IDS_SCN_METHODMASTER' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=95 where taskname='IDS_TSK_DELETE' and screenname='IDS_SCN_METHODMASTER' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=96 where taskname='IDS_TSK_SAVE' and screenname='IDS_SCN_UNITMASTER' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=97 where taskname='IDS_TSK_DELETE' and screenname='IDS_SCN_UNITMASTER' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=98 where taskname='IDS_TSK_SAVE' and screenname='IDS_SCN_SECTIONMASTER' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=99 where taskname='IDS_TSK_DELETE' and screenname='IDS_SCN_SECTIONMASTER' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=100 where taskname='IDS_TSK_SAVE' and screenname='IDS_SCN_STORAGELOCATION' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=101 where taskname='IDS_TSK_DELETE' and screenname='IDS_SCN_STORAGELOCATION' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=102 where taskname='IDS_TSK_SAVE' and screenname='IDS_SCN_MATERIALCATEGORY' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=103 where taskname='IDS_TSK_DELETE' and screenname='IDS_SCN_MATERIALCATEGORY' and lsusermaster_usercode=1 ;
 update lsaudittrailconfiguration set ordersequnce=104 where taskname='IDS_TSK_SAVE' and screenname='IDS_SCN_MATERIAL' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=105 where taskname='IDS_TSK_DELETE' and screenname='IDS_SCN_MATERIAL' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=106 where taskname='IDS_TSK_SAVE' and screenname='IDS_SCN_MATERIALINVENTORY' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=107 where taskname='IDS_TSK_DELETE' and screenname='IDS_SCN_MATERIALINVENTORY' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=108 where taskname='IDS_TSK_ADDLOGBOOK' and screenname='IDS_SCN_LOGBOOK' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=109 where taskname='IDS_TSK_EDITLOGBOOK' and screenname='IDS_SCN_LOGBOOK' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=110 where taskname='IDS_TSK_RETIRELOGBOOK' and screenname='IDS_SCN_LOGBOOK' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=111 where taskname='IDS_TSK_REVIEWLOGBOOK' and screenname='IDS_SCN_LOGBOOK' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=112 where taskname='IDS_TSK_SAVE' and screenname='IDS_SCN_LOGBOOK' and lsusermaster_usercode=1;
 update lsaudittrailconfiguration set ordersequnce=113 where taskname='IDS_TSK_EDIT' and screenname='IDS_SCN_LOGBOOK' and lsusermaster_usercode=1 ;
 update lsaudittrailconfiguration set ordersequnce=114 where taskname='IDS_TSK_DELETE' and screenname='IDS_SCN_LOGBOOK' and lsusermaster_usercode=1;


update lsaudittrailconfiguration set modulename='IDS_MDL_ORDERS' where taskname in ('IDS_TSK_PARSEDATA','IDS_TSK_EXPORTASJSON','IDS_TSK_EXPORT','IDS_TSK_COMPLETETASK','IDS_TSK_SAVE','IDS_TSK_UPLOAD','IDS_TSK_OPEN') and screenname='IDS_SCN_SHEETORDERS';
update lsaudittrailconfigmaster set taskname='IDS_TSK_SHEETSHARE' where serialno=99;
update lsaudittrailconfigmaster set taskname='IDS_TSK_CHANGEPASSWORD',modulename='IDS_MDL_SETUP',screenname='IDS_SCN_USERMASTER' where taskname='IDS_TSK_OPENORDER';
update lsaudittrailconfigmaster set taskname='IDS_TSK_PROTOCOLSHARE' where serialno=107;
update lsaudittrailconfigmaster set taskname='IDS_TSK_SHEETTEMPSHARE',screenname='IDS_SCN_SHEETTEMPLATES',modulename='IDS_MDL_TEMPLATES' where serialno=108;
update lsaudittrailconfiguration set modulename='IDS_MDL_SETUP' where taskname='IDS_TSK_CHANGEPASSWORD';
update lsaudittrailconfigmaster set modulename='IDS_MDL_SETUP' where taskname='IDS_TSK_CHANGEPASSWORD';

update lsaudittrailconfigmaster set modulename='IDS_MDL_INVENTORY' where serialno in (71,72,73,74,75);
update lsaudittrailconfiguration set modulename='IDS_MDL_INVENTORY' where taskname in ('IDS_TSK_ADDREPO','IDS_TSK_EDITREPO','IDS_TSK_SAVE','IDS_TSK_DELETE','IDS_TSK_EDIT');

Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(136,0,'IDS_MDL_TEMPLATES',6,'IDS_SCN_SHEETTEMPLATES','IDS_TSK_EDITSHEET') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(137,0,'IDS_MDL_TEMPLATES',6,'IDS_SCN_PROTOCOLTEMP','IDS_TSK_EDITPROTOCOL') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(138,0,'IDS_MDL_MASTERS',6,'IDS_SCN_TASKMASTER','IDS_TSK_EDITTASK') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(139,0,'IDS_MDL_MASTERS',6,'IDS_SCN_PROJECTMASTER','IDS_TSK_EDITPROJECT') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(140,0,'IDS_MDL_MASTERS',6,'IDS_SCN_SAMPLEMASTER','IDS_TSK_EDITSAMPLE') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(141,0,'IDS_MDL_INVENTORY',6,'IDS_SCN_UNITMASTER','IDS_TSK_EDIT') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(142,0,'IDS_MDL_INVENTORY',6,'IDS_SCN_SECTIONMASTER','IDS_TSK_EDIT') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(143,0,'IDS_MDL_INVENTORY',6,'IDS_SCN_STORAGELOCATION','IDS_TSK_EDIT') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(144,0,'IDS_MDL_INVENTORY',6,'IDS_SCN_MATERIALCATEGORY','IDS_TSK_EDIT') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(145,0,'IDS_MDL_INVENTORY',6,'IDS_SCN_MATERIAL','IDS_TSK_EDIT') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(146,0,'IDS_MDL_INVENTORY',6,'IDS_SCN_MATERIALINVENTORY','IDS_TSK_EDIT') ON CONFLICT(serialno)DO NOTHING;

 update lsaudittrailconfigmaster set ordersequnce=1 where serialno=100;
 update lsaudittrailconfigmaster set ordersequnce=2 where serialno=2;
 update lsaudittrailconfigmaster set ordersequnce=3 where serialno=102;
 update lsaudittrailconfigmaster set ordersequnce=4 where serialno=38;
 update lsaudittrailconfigmaster set ordersequnce=5 where serialno=40;
 update lsaudittrailconfigmaster set ordersequnce=6 where serialno=98;
 update lsaudittrailconfigmaster set ordersequnce=7 where serialno=99;
 update lsaudittrailconfigmaster set ordersequnce=8 where serialno=1;
 update lsaudittrailconfigmaster set ordersequnce=9 where serialno=101;
 update lsaudittrailconfigmaster set ordersequnce=10 where serialno=105;
 update lsaudittrailconfigmaster set ordersequnce=11 where serialno=76;
 update lsaudittrailconfigmaster set ordersequnce=12 where serialno=39;
 update lsaudittrailconfigmaster set ordersequnce=13 where serialno=109;
 update lsaudittrailconfigmaster set ordersequnce=14 where serialno=77;
 update lsaudittrailconfigmaster set ordersequnce=15 where serialno=111;
 update lsaudittrailconfigmaster set ordersequnce=16 where serialno=79;
 update lsaudittrailconfigmaster set ordersequnce=17 where serialno=80;
 update lsaudittrailconfigmaster set ordersequnce=18 where serialno=112;
 update lsaudittrailconfigmaster set ordersequnce=19 where serialno=78;
 update lsaudittrailconfigmaster set ordersequnce=20 where serialno=130;
 update lsaudittrailconfigmaster set ordersequnce=21 where serialno=107;
 update lsaudittrailconfigmaster set ordersequnce=24 where serialno=113;
 update lsaudittrailconfigmaster set ordersequnce=25 where serialno=136;
 update lsaudittrailconfigmaster set ordersequnce=26 where serialno=3;
 update lsaudittrailconfigmaster set ordersequnce=27 where serialno=41;
 update lsaudittrailconfigmaster set ordersequnce=28 where serialno=114;
 update lsaudittrailconfigmaster set ordersequnce=29 where serialno=108;
 update lsaudittrailconfigmaster set ordersequnce=30 where serialno=58;
 update lsaudittrailconfigmaster set ordersequnce=31 where serialno=137;
 update lsaudittrailconfigmaster set ordersequnce=32 where serialno=59;
 update lsaudittrailconfigmaster set ordersequnce=33 where serialno=61;
 update lsaudittrailconfigmaster set ordersequnce=34 where serialno=60;
 update lsaudittrailconfigmaster set ordersequnce=35 where serialno=10;
 update lsaudittrailconfigmaster set ordersequnce=36 where serialno=4;
 update lsaudittrailconfigmaster set ordersequnce=37 where serialno=138;
 update lsaudittrailconfigmaster set ordersequnce=38 where serialno=5;
 update lsaudittrailconfigmaster set ordersequnce=39 where serialno=7;
 update lsaudittrailconfigmaster set ordersequnce=40 where serialno=139;
 update lsaudittrailconfigmaster set ordersequnce=41 where serialno=6;
 update lsaudittrailconfigmaster set ordersequnce=42 where serialno=9;
 update lsaudittrailconfigmaster set ordersequnce=43 where serialno=140;
 update lsaudittrailconfigmaster set ordersequnce=44 where serialno=8;
 update lsaudittrailconfigmaster set ordersequnce=45 where serialno=30;
 update lsaudittrailconfigmaster set ordersequnce=46 where serialno=115;
 update lsaudittrailconfigmaster set ordersequnce=47 where serialno=50;
 update lsaudittrailconfigmaster set ordersequnce=48 where serialno=51;
 update lsaudittrailconfigmaster set ordersequnce=49 where serialno=11;
 update lsaudittrailconfigmaster set ordersequnce=50 where serialno=12;
 update lsaudittrailconfigmaster set ordersequnce=51 where serialno=13;
 update lsaudittrailconfigmaster set ordersequnce=52 where serialno=14;
 update lsaudittrailconfigmaster set ordersequnce=53 where serialno=15;
 update lsaudittrailconfigmaster set ordersequnce=54 where serialno=16;
 update lsaudittrailconfigmaster set ordersequnce=55 where serialno=17;
 update lsaudittrailconfigmaster set ordersequnce=56 where serialno=18;
 update lsaudittrailconfigmaster set ordersequnce=57 where serialno=116;
 update lsaudittrailconfigmaster set ordersequnce=58 where serialno=54;
 update lsaudittrailconfigmaster set ordersequnce=59 where serialno=55;
 update lsaudittrailconfigmaster set ordersequnce=60 where serialno=131;
 update lsaudittrailconfigmaster set ordersequnce=61 where serialno=19;
 update lsaudittrailconfigmaster set ordersequnce=62 where serialno=20;
 update lsaudittrailconfigmaster set ordersequnce=63 where serialno=21;
 update lsaudittrailconfigmaster set ordersequnce=64 where serialno=22;
 update lsaudittrailconfigmaster set ordersequnce=65 where serialno=56;
 update lsaudittrailconfigmaster set ordersequnce=66 where serialno=32;
 update lsaudittrailconfigmaster set ordersequnce=67 where serialno=57;
 update lsaudittrailconfigmaster set ordersequnce=68 where serialno=33;
 update lsaudittrailconfigmaster set ordersequnce=69 where serialno=34;
 update lsaudittrailconfigmaster set ordersequnce=70 where serialno=35;
 update lsaudittrailconfigmaster set ordersequnce=71 where serialno=84;
 update lsaudittrailconfigmaster set ordersequnce=72 where serialno=147;
 update lsaudittrailconfigmaster set ordersequnce=73 where serialno=85;
 update lsaudittrailconfigmaster set ordersequnce=74 where serialno=86;
 update lsaudittrailconfigmaster set ordersequnce=75 where serialno=87;
 update lsaudittrailconfigmaster set ordersequnce=76 where serialno=88;
 update lsaudittrailconfigmaster set ordersequnce=77 where serialno=89;
 update lsaudittrailconfigmaster set ordersequnce=78 where serialno=90;
 update lsaudittrailconfigmaster set ordersequnce=79 where serialno=91;
 update lsaudittrailconfigmaster set ordersequnce=80 where serialno=92;
 update lsaudittrailconfigmaster set ordersequnce=81 where serialno=93;
 update lsaudittrailconfigmaster set ordersequnce=82 where serialno=94;
 update lsaudittrailconfigmaster set ordersequnce=83 where serialno=95;
 update lsaudittrailconfigmaster set ordersequnce=84 where serialno=96;
 update lsaudittrailconfigmaster set ordersequnce=85 where serialno=97;
 update lsaudittrailconfigmaster set ordersequnce=86 where serialno=23;
 update lsaudittrailconfigmaster set ordersequnce=87 where serialno=117;
 update lsaudittrailconfigmaster set ordersequnce=88 where serialno=24;
 update lsaudittrailconfigmaster set ordersequnce=89 where serialno=25;
 update lsaudittrailconfigmaster set ordersequnce=90 where serialno=26;
 update lsaudittrailconfigmaster set ordersequnce=91 where serialno=27;
 update lsaudittrailconfigmaster set ordersequnce=92 where serialno=28;
 update lsaudittrailconfigmaster set ordersequnce=93 where serialno=42;
 update lsaudittrailconfigmaster set ordersequnce=94 where serialno=47;
 update lsaudittrailconfigmaster set ordersequnce=95 where serialno=49;
 update lsaudittrailconfigmaster set ordersequnce=96 where serialno=45;
 update lsaudittrailconfigmaster set ordersequnce=97 where serialno=48;
 update lsaudittrailconfigmaster set ordersequnce=98 where serialno=43;
 update lsaudittrailconfigmaster set ordersequnce=99 where serialno=46;
 update lsaudittrailconfigmaster set ordersequnce=100 where serialno=118;
 update lsaudittrailconfigmaster set ordersequnce=101 where serialno=141;
 update lsaudittrailconfigmaster set ordersequnce=102 where serialno=119;
 update lsaudittrailconfigmaster set ordersequnce=109 where serialno=120;
 update lsaudittrailconfigmaster set ordersequnce=110 where serialno=142;
 update lsaudittrailconfigmaster set ordersequnce=111 where serialno=121;
 update lsaudittrailconfigmaster set ordersequnce=112 where serialno=122;
 update lsaudittrailconfigmaster set ordersequnce=113 where serialno=143;
 update lsaudittrailconfigmaster set ordersequnce=114 where serialno=123;
 update lsaudittrailconfigmaster set ordersequnce=115 where serialno=124;
 update lsaudittrailconfigmaster set ordersequnce=116 where serialno=144;
 update lsaudittrailconfigmaster set ordersequnce=117 where serialno=125;
 update lsaudittrailconfigmaster set ordersequnce=118 where serialno=126;
 update lsaudittrailconfigmaster set ordersequnce=119 where serialno=145;
 update lsaudittrailconfigmaster set ordersequnce=120 where serialno=127;
 update lsaudittrailconfigmaster set ordersequnce=121 where serialno=128;
 update lsaudittrailconfigmaster set ordersequnce=122 where serialno=146;
 update lsaudittrailconfigmaster set ordersequnce=123 where serialno=129;
update lsaudittrailconfigmaster set ordersequnce=124 where serialno=71;
update lsaudittrailconfigmaster set ordersequnce=125 where serialno=72;
update lsaudittrailconfigmaster set ordersequnce=126 where serialno=73;
update lsaudittrailconfigmaster set ordersequnce=127 where serialno=74;
update lsaudittrailconfigmaster set ordersequnce=128 where serialno=75;
update lsaudittrailconfigmaster set ordersequnce=129 where serialno=132;
update lsaudittrailconfigmaster set ordersequnce=130 where serialno=133;
update lsaudittrailconfigmaster set ordersequnce=131 where serialno=134;
update lsaudittrailconfigmaster set ordersequnce=132 where serialno=135;
update lsaudittrailconfigmaster set ordersequnce=133 where serialno=81;
update lsaudittrailconfigmaster set ordersequnce=134 where serialno=82;
update lsaudittrailconfigmaster set ordersequnce=135 where serialno=83;

update lsusergrouprights set modulename='IDS_MDL_LOGBOOK' where screenname='IDS_SCN_LOGBBOK';
update lsaudittrailconfiguration set modulename='IDS_MDL_LOGBOOK' where screenname='IDS_SCN_LOGBBOK';
update lsaudittrailconfiguration set taskname='IDS_TSK_RETIRELOG' where screenname='IDS_SCN_LOGBOOK' and taskname='IDS_TSK_DELETE';
update lsaudittrailconfigmaster set taskname='IDS_TSK_RETIRELOG' where serialno=83;
delete from lsaudittrailconfigmaster where serialno=105;


update lsusergrouprights set sequenceorder=1 where displaytopic ='IDS_TSK_ORDEROVERVIEW' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=2 where displaytopic ='IDS_TSK_TEMPLATEOVERVIEW' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=3 where displaytopic ='IDS_TSK_ACTIVITIES' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=4 where displaytopic ='IDS_TSK_DASHBOARDINVENTORY' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=5 where displaytopic ='IDS_SCN_SHEETORDERS' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=6 where displaytopic ='IDS_TSK_FOLDERCREATION' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=7 where displaytopic ='IDS_TSK_ELNTASKORDER' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=8 where displaytopic ='IDS_TSK_RESEARCHACTIVITY' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=9 where displaytopic ='IDS_TSK_MANAGEEXCEL' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=10 where displaytopic ='IDS_TSK_SHEETEVALUATION' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=11 where displaytopic ='IDS_TSK_UPLOADSHEETORDER' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=12 where displaytopic ='IDS_TSK_ORDERSHAREDBYME' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=13 where displaytopic ='IDS_TSK_ORDERSHAREDTOME' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=14 where displaytopic ='IDS_TSK_MOVEORDERS' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=15 where displaytopic ='IDS_TSK_SHEETORDEREXPORT' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=16 where displaytopic ='IDS_SCN_PROTOCOLORDERS' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=17 where displaytopic ='IDS_TSK_FOLDERCREATIONPROTOCOL' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=18 where displaytopic ='IDS_TSK_ELNPROTOCOL' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=19 where displaytopic ='IDS_TSK_DYNAMICPROTOCOL' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=20 where displaytopic ='IDS_TSK_UPLOADPROTOCOLORDER' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=21 where displaytopic ='IDS_TSK_ORDERSHAREDBYMEPROTOCOL' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=22 where displaytopic ='IDS_TSK_ORDERSHAREDTOMEPROTOCOL' and usergroupid_usergroupcode=1;
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
update lsusergrouprights set sequenceorder=33 where displaytopic ='IDS_TSK_SHEET' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=34 where displaytopic ='IDS_TSK_PROTOCOL' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=35 where displaytopic ='IDS_TSK_LIMSTESTORDER' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=36 where displaytopic ='IDS_SCN_TASKMASTER' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=37 where displaytopic ='IDS_SCN_PROJECTMASTER' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=38 where displaytopic ='IDS_SCN_SAMPLEMASTER' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=39 where displaytopic ='IDS_SCN_INVENTORY' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=40 where displaytopic ='IDS_TSK_ADDREPO' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=41 where displaytopic ='IDS_TSK_EDITREPO' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=42 where displaytopic ='IDS_SCN_UNLOCKORDERS' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=43 where displaytopic ='IDS_SCN_USERGROUP' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=44 where displaytopic ='IDS_TSK_ACTDEACT' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=45 where displaytopic ='IDS_SCN_USERMASTER' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=46 where displaytopic ='IDS_TSK_UNLOCK' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=47 where displaytopic ='IDS_TSK_ACTDEACTUSERMASTER' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=48 where displaytopic ='IDS_TSK_RESETPASSWORD' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=49 where displaytopic ='IDS_TSK_RETIRE' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=50 where displaytopic ='IDS_TSK_IMPORTADS' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=51 where displaytopic ='IDS_SCN_USERRIGHTS' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=52 where displaytopic ='IDS_SCN_PROJECTTEAM' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=53 where displaytopic ='IDS_SCN_ORDERWORKLOW' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=54 where displaytopic ='IDS_SCN_TEMPLATEWORKFLOW' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=55 where displaytopic ='IDS_SCN_PASSWORDPOLICY' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=56 where displaytopic ='IDS_SCN_AUDITTRAILHIS' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=57 where displaytopic ='IDS_TSK_REVIEWHISTORY' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=58 where displaytopic ='IDS_TSK_REVIEW' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=59 where displaytopic ='IDS_TSK_CREATEARCHIVE' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=60 where displaytopic ='IDS_TSK_OPENARCHIVE' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=61 where displaytopic ='IDS_TSK_EXPORT' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=62 where displaytopic ='IDS_SCN_CFRSETTINGS' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=63 where displaytopic ='IDS_SCN_AUDITTRAILCONFIG' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=64 where displaytopic ='IDS_SCN_REPORTS' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=65 where displaytopic ='IDS_TSK_NEWDOCUMENT' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=66 where displaytopic ='IDS_TSK_NEWTEMP' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=67 where displaytopic ='IDS_TSK_GENERATEREPORT' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=68 where displaytopic ='IDS_TSK_OPENREPORT' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=69 where displaytopic ='IDS_TSK_IMPORTDOCX' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=70 where displaytopic ='IDS_SCN_PARSER' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=71 where displaytopic ='IDS_SCN_INSTRUMENTCATEGORY' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=72 where displaytopic ='IDS_SCN_INSTRUMENTMASTER' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=73 where displaytopic ='IDS_SCN_DELIMITER' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=74 where displaytopic ='IDS_SCN_METHODDELIMITER' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=75 where displaytopic ='IDS_SCN_METHODMASTER' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=76 where displaytopic ='IDS_SCN_UNITMASTER' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=77 where displaytopic ='IDS_TSK_DOWNLOADPDFEXCEL' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=78 where displaytopic ='IDS_SCN_SECTIONMASTER' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=79 where displaytopic ='IDS_TSK_DOWNLOADPDFEXCELSECTION' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=80 where displaytopic ='IDS_SCN_STORAGELOCATION' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=81 where displaytopic ='IDS_SCN_MATERIALCATEGORY' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=82 where displaytopic ='IDS_TSK_DOWNLOADMATERILACATEGORY' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=83 where displaytopic ='IDS_SCN_MATERIAL' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=84 where displaytopic ='IDS_SCN_MATERIALINVENTORY' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=85 where displaytopic ='IDS_SCN_LOGBOOK' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=86 where displaytopic ='IDS_TSK_ADDLOGBOOK' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=87 where displaytopic ='IDS_TSK_EDITLOGBOOK' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=88 where displaytopic ='IDS_TSK_RETIRELOGBOOK' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=89 where displaytopic ='IDS_TSK_REVIEWLOGBOOK' and usergroupid_usergroupcode=1;


 DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'lsprotocolfolderfiles_folderfilecode_seq' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE lsprotocolfolderfiles_folderfilecode_seq;
   ELSIF _kind = 'S' THEN  
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;


CREATE TABLE IF NOT EXISTS public.lsprotocolfolderfiles
(
    folderfilecode integer NOT NULL DEFAULT nextval('lsprotocolfolderfiles_folderfilecode_seq'::regclass),
    createdtimestamp timestamp without time zone,
    directorycode numeric(17,0),
    filefor character varying(255) COLLATE pg_catalog."default",
    filename character varying(255) COLLATE pg_catalog."default",
    filesize bigint,
    fileviewfor integer,
    uuid character varying(255) COLLATE pg_catalog."default",
    createby_usercode integer,
    lssitemaster_sitecode integer,
    CONSTRAINT lsprotocolfolderfiles_pkey PRIMARY KEY (folderfilecode),
    CONSTRAINT fk8kdlmvt6b6psoy9qwfkpdrsdq FOREIGN KEY (createby_usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fke9wk0li0eyx8n2j35serlvp6w FOREIGN KEY (lssitemaster_sitecode)
        REFERENCES public.lssitemaster (sitecode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.lsprotocolfolderfiles
    OWNER to postgres;
    
ALTER TABLE IF Exists lslogbooks ADD COLUMN IF NOT EXISTS logbookcategory character varying(255);
update lslogbooks set logbookcategory='' where logbookcategory is null;

ALTER TABLE IF Exists lslogbooks ADD COLUMN IF NOT EXISTS reviewstatus character varying(255);
update lslogbooks set reviewstatus='' where reviewstatus is null;

ALTER TABLE IF Exists lslogbooksdata ADD COLUMN IF NOT EXISTS logitemstatus character varying(255); 
update lslogbooksdata set logitemstatus='A' where logitemstatus is null;

update parserfield set datatypekey = 1 where datatypekey is Null;
ALTER TABLE IF Exists lsprotocolmaster ADD COLUMN IF NOT EXISTS viewoption integer;

update lsprotocolmaster set viewoption = 1 where viewoption is null;

ALTER TABLE IF Exists lsprotocolmaster ADD COLUMN IF NOT EXISTS category character varying(255);

ALTER TABLE IF Exists LSfile ADD COLUMN IF NOT EXISTS category character varying(255);

ALTER TABLE IF Exists lslogilabprotocoldetail ADD COLUMN IF NOT EXISTS ordercancell integer;

ALTER TABLE IF Exists lslogilablimsorderdetail ADD COLUMN IF NOT EXISTS ordercancell integer;


 DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'lsprotocolorderstephistory_protocolorderstephistorycode_seq' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE lsprotocolorderstephistory_protocolorderstephistorycode_seq;
   ELSIF _kind = 'S' THEN  
     
   ELSE                  
    
   END IF;
END
$do$;

    
CREATE TABLE IF NOT EXISTS public.lsprotocolorderstephistory
(
    protocolorderstephistorycode integer NOT NULL DEFAULT nextval('lsprotocolorderstephistory_protocolorderstephistorycode_seq'::regclass),
    action character varying(250) COLLATE pg_catalog."default",
    comment character varying(250) COLLATE pg_catalog."default",
    protocolordercode bigint,
    protocolorderstepcode integer,
    stependdate timestamp without time zone,
    stepno integer,
    stepskipeddate timestamp without time zone,
    stepstartdate timestamp without time zone,
    viewoption integer,
    createby_usercode integer,
    modifiedby_usercode integer,
    CONSTRAINT lsprotocolorderstephistory_pkey PRIMARY KEY (protocolorderstephistorycode),
    CONSTRAINT fkcw20pabmliwvfrn9on2qethrb FOREIGN KEY (modifiedby_usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkeo4a00cj4658t2q59p5hmba2w FOREIGN KEY (createby_usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.lsprotocolorderstephistory
    OWNER to postgres;
    
    
ALTER TABLE IF Exists lssamplemaster ADD COLUMN IF NOT EXISTS samplecategory character varying(255);

ALTER TABLE IF Exists lssamplemaster ADD COLUMN IF NOT EXISTS createdate timestamp without time zone;

ALTER TABLE IF Exists lssamplemaster ADD COLUMN IF NOT EXISTS  createby_usercode integer;

DO
$do$
declare
  resultvalues integer :=0;
begin

SELECT count(*) into resultvalues FROM
information_schema.table_constraints WHERE constraint_name='fk88q928l57ed9lx1yxo3opl4a'
AND table_name='lssamplemaster';
 IF resultvalues =0 THEN
    ALTER TABLE ONLY lssamplemaster ADD CONSTRAINT fk88q928l57ed9lx1yxo3opl4a FOREIGN KEY (createby_usercode) REFERENCES lsusermaster (usercode);
   END IF;
END
$do$; 

ALTER TABLE IF Exists lstestmasterlocal ADD COLUMN IF NOT EXISTS taskcategory character varying(255);

ALTER TABLE IF Exists lstestmasterlocal ADD COLUMN IF NOT EXISTS createdate timestamp without time zone;

ALTER TABLE IF Exists lstestmasterlocal ADD COLUMN IF NOT EXISTS  createby_usercode integer;

DO
$do$
declare
  resultvalues integer :=0;
begin

SELECT count(*) into resultvalues FROM
information_schema.table_constraints WHERE constraint_name='fkjpbxy60g5p0bx5jj2dgrtn23u'
AND table_name='lstestmasterlocal';
 IF resultvalues =0 THEN
    ALTER TABLE ONLY lstestmasterlocal ADD CONSTRAINT fkjpbxy60g5p0bx5jj2dgrtn23u FOREIGN KEY (createby_usercode) REFERENCES lsusermaster (usercode);
   END IF;
END
$do$;

INSERT into LSfields (fieldcode, createby, createdate, fieldorderno, fieldtypecode, isactive, level01code, level01name, level02code, level02name, level03code, level03name, level04code, level04name, siteID) VALUES (59, NULL, NULL, 19, 3, 1, 'G1', 'ID_GENERAL', '20', 'ID_GENERAL', 20, 'ID_GENERAL', 'G20', 'multiselect combobox', 1) on conflict (fieldcode) do nothing;

update lsfields set isactive=1  where level04code='G14';

ALTER TABLE IF Exists lsprotocolorderStructure ADD COLUMN IF NOT EXISTS  teamcode integer;

ALTER TABLE IF Exists lslogilabprotocoldetail ADD COLUMN IF NOT EXISTS  teamcode integer;

ALTER TABLE IF Exists lssheetOrderStructure ADD COLUMN IF NOT EXISTS  teamcode integer;

ALTER TABLE IF Exists lslogilablimsorderdetail ADD COLUMN IF NOT EXISTS  teamcode integer;

INSERT INTO transactionstatus (ntranscode, stransstatus, jsondata, nstatus) VALUES (47, 'Receive', '{"salertdisplaystatus": {"en-US": "Received", "ru-RU": "Получила", "tg-TG": "Гирифтанд"}, "stransdisplaystatus": {"en-US": "Received", "ru-RU": "Получила", "tg-TG": "Гирифтанд"}, "sactiondisplaystatus": {"en-US": "Receive", "ru-RU": "Получать", "tg-TG": "Гирифтан"}}', 1) on conflict(ntranscode) do nothing;

INSERT INTO transactionstatus (ntranscode, stransstatus, jsondata, nstatus) VALUES (48, 'Issue', '{"salertdisplaystatus": {"en-US": "Issued", "ru-RU": "Изданный", "tg-TG": "Нашр шуда"}, "stransdisplaystatus": {"en-US": "Issued", "ru-RU": "Изданный", "tg-TG": "Нашр шуда"}, "sactiondisplaystatus": {"en-US": "Issue", "ru-RU": "Проблема", "tg-TG": "Чоп"}}', 1)on conflict(ntranscode) do nothing;

INSERT INTO materialinventorytype (ninventorytypecode, jsondata, ndefaultstatus, nstatus) VALUES (1, '{"sinventorytypename": {"en-US": "Inhouse", "ru-RU": "Внутренний", "tg-TG": "Дар хона"}}', 3, 1)on conflict(ninventorytypecode) do nothing;

INSERT INTO materialinventorytype (ninventorytypecode, jsondata, ndefaultstatus, nstatus) VALUES (2, '{"sinventorytypename": {"en-US": "Outside", "ru-RU": "За пределами", "tg-TG": "Дар берун"}}', 4, 1)on conflict(ninventorytypecode) do nothing;

INSERT INTO materialgrade (nmaterialgradecode, jsondata, ndefaultstatus, nsitecode, nstatus, smaterialgradename) VALUES (1, '{"sdescription": "", "smaterialgradename": "A"}', 4, -1, 1, 'A')on conflict(nmaterialgradecode) do nothing;

INSERT INTO materialgrade (nmaterialgradecode, jsondata, ndefaultstatus, nsitecode, nstatus, smaterialgradename) VALUES (2, '{"sdescription": "", "smaterialgradename": "B"}', 4, -1, 1, 'B')on conflict(nmaterialgradecode) do nothing;

update  lsaudittrailconfiguration set manualaudittrail=1  where taskname='IDS_TSK_REVIEWLOGBOOK';

ALTER TABLE IF Exists cloudparserfile ADD COLUMN IF NOT EXISTS version integer default 1;

update cloudparserfile set version = 1 where version is Null;

ALTER TABLE IF Exists method ADD COLUMN IF NOT EXISTS version integer default 1;

update method set version = 1 where version is Null;

ALTER TABLE IF Exists lsprotocolorderstephistory ADD COLUMN IF NOT EXISTS  batchcode bigint;

ALTER TABLE IF Exists lstestmasterlocal ADD COLUMN IF NOT EXISTS description  character varying(255);

ALTER TABLE IF Exists material ADD COLUMN IF NOT EXISTS sprefix character varying(50);


CREATE TABLE IF NOT EXISTS public.lslogbooksampleupdates
(
    logbooksamplecode integer NOT NULL,
    consumefieldkey character varying(255) COLLATE pg_catalog."default",
    createdbyusername character varying(255) COLLATE pg_catalog."default",
    createddate timestamp without time zone,
    indexof integer,
    logbookcode integer,
    logbooksample character varying(255) COLLATE pg_catalog."default",
    logbooksampletype character varying(255) COLLATE pg_catalog."default",
    logbooksampleuseddetail character varying(255) COLLATE pg_catalog."default",
    repositorydatacode integer,
    status integer,
    unit character varying(255) COLLATE pg_catalog."default",
    usedquantity integer,
    usercode integer,
    CONSTRAINT lslogbooksampleupdates_pkey PRIMARY KEY (logbooksamplecode)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.lslogbooksampleupdates
    OWNER to postgres;
    
     Do
 $do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'methodversion_mvno_seq' 
   INTO  _kind;

   IF NOT FOUND THEN       
      CREATE SEQUENCE methodversion_mvno_seq;
   ELSIF _kind = 'S' THEN  
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.methodversion
(
    mvno integer NOT NULL DEFAULT nextval('methodversion_mvno_seq'::regclass),
    blobid character varying(255) COLLATE pg_catalog."default",
    createddate timestamp without time zone,
    filename character varying(255) COLLATE pg_catalog."default",
    instrawdataurl character varying(255) COLLATE pg_catalog."default",
    methodkey integer,
    status integer,
    version integer,
    CONSTRAINT methodversion_pkey PRIMARY KEY (mvno),
    CONSTRAINT fk8iusen0109oloyuaarcepkb94 FOREIGN KEY (methodkey)
        REFERENCES public.method (methodkey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT methodversion_status_check CHECK (status >= '-1'::integer AND status <= 1)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.methodversion
    OWNER to postgres;
    
INSERT INTO parserignorechars(ignorechars)SELECT 'µ' WHERE NOT EXISTS (SELECT ignorechars FROM parserignorechars WHERE ignorechars = 'µ');
INSERT INTO parserignorechars(ignorechars)SELECT 'µµ' WHERE NOT EXISTS (SELECT ignorechars FROM parserignorechars WHERE ignorechars = 'µµ');
INSERT INTO parserignorechars(ignorechars)SELECT 'µµµ' WHERE NOT EXISTS (SELECT ignorechars FROM parserignorechars WHERE ignorechars = 'µµµ');
INSERT INTO parserignorechars(ignorechars)SELECT 'µµµµ' WHERE NOT EXISTS (SELECT ignorechars FROM parserignorechars WHERE ignorechars = 'µµµµµ');
INSERT INTO parserignorechars(ignorechars)SELECT 'µµµµµ' WHERE NOT EXISTS (SELECT ignorechars FROM parserignorechars WHERE ignorechars = 'µµµµµµ');

 INSERT INTO parserignorechars(ignorechars)SELECT 'â†â†' WHERE NOT EXISTS (SELECT ignorechars FROM parserignorechars WHERE ignorechars = 'â†â†');
 INSERT INTO parserignorechars(ignorechars)SELECT 'â†â' WHERE NOT EXISTS (SELECT ignorechars FROM parserignorechars WHERE ignorechars = 'â†â');
 INSERT INTO parserignorechars(ignorechars)SELECT 'ââ' WHERE NOT EXISTS (SELECT ignorechars FROM parserignorechars WHERE ignorechars = 'ââ');
 INSERT INTO parserignorechars(ignorechars)SELECT 'âââ' WHERE NOT EXISTS (SELECT ignorechars FROM parserignorechars WHERE ignorechars = 'âââ');
   
 INSERT INTO parserignorechars(ignorechars)SELECT 'âââ' WHERE NOT EXISTS (SELECT ignorechars FROM parserignorechars WHERE ignorechars = 'â†µ');
  
CREATE TABLE IF NOT EXISTS public.lslogilablimsorder
(
    orderid numeric(17,0) NOT NULL,
    batchid character varying(250) COLLATE pg_catalog."default",
    completedtimestamp timestamp without time zone,
    createdtimestamp timestamp without time zone,
    instrumentcode character varying(100) COLLATE pg_catalog."default",
    instrumentname character varying(100) COLLATE pg_catalog."default",
    methodcode character varying(100) COLLATE pg_catalog."default",
    orderflag character(10) COLLATE pg_catalog."default",
    parserflag character(10) COLLATE pg_catalog."default",
    replicateid character varying(100) COLLATE pg_catalog."default",
    sampleid character varying(250) COLLATE pg_catalog."default",
    testcode character varying(100) COLLATE pg_catalog."default",
    CONSTRAINT lslogilablimsorder_pkey PRIMARY KEY (orderid)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.lslogilablimsorder
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.lsinstruments
(
    t06_strinstid character varying(50) COLLATE pg_catalog."default" NOT NULL,
    t06_bytecaliber smallint,
    t06_byte_clientbound smallint,
    t06_bytedatasource smallint,
    t06_bytemultisid smallint,
    t06_byte_peakdetection smallint,
    t06_bytesplitsid smallint,
    t06_byteselected smallint,
    t06_bytesid smallint,
    t06_bytestationid smallint NOT NULL,
    t06_calibduedate date,
    t06_caliblastmodifiedon date,
    t06_caliblastsetby character varying(50) COLLATE pg_catalog."default",
    t06_calibstatus character varying(50) COLLATE pg_catalog."default",
    t06_calibtime character varying(6) COLLATE pg_catalog."default",
    t06_int_usedby smallint NOT NULL,
    t06_lastcalibon date,
    t06_strcalibtest character varying(50) COLLATE pg_catalog."default",
    t06_strsched1 character varying(10) COLLATE pg_catalog."default",
    t06_strsched2 character varying(15) COLLATE pg_catalog."default",
    t06_strinstname character varying(50) COLLATE pg_catalog."default",
    t06_striopnumber character varying(20) COLLATE pg_catalog."default",
    t06_strelectrodeno character varying(50) COLLATE pg_catalog."default",
    t06_strmake character varying(15) COLLATE pg_catalog."default",
    t06_strmodel character varying(15) COLLATE pg_catalog."default",
    CONSTRAINT lsinstruments_pkey PRIMARY KEY (t06_strinstid)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.lsinstruments
    OWNER to postgres;
    
CREATE TABLE IF NOT EXISTS public.lsmethodfields
(
    t23_strfieldkey character varying(12) COLLATE pg_catalog."default" NOT NULL,
    t23_bytealign smallint,
    t23_bytecoltype smallint,
    t23_bytedeletable smallint,
    t23_bytedimension smallint,
    t23_byteselected smallint,
    t23_byteselloginrpt smallint,
    t23_bytesequence smallint,
    t23_bytevalue smallint,
    t23_strdatatype character varying(12) COLLATE pg_catalog."default",
    t23_strelnfieldname character varying(30) COLLATE pg_catalog."default",
    t23_strfieldname character varying(30) COLLATE pg_catalog."default",
    t23_strformat character varying(25) COLLATE pg_catalog."default",
    t23_strinstrumentid character varying(50) COLLATE pg_catalog."default" NOT NULL,
    t23_strlimsfieldname character varying(30) COLLATE pg_catalog."default",
    t23_strmethodname character varying(50) COLLATE pg_catalog."default" NOT NULL,
    t23_strparsername character varying(30) COLLATE pg_catalog."default" NOT NULL,
    t23_intsortsequence smallint,
    CONSTRAINT lsmethodfields_pkey PRIMARY KEY (t23_strfieldkey)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.lsmethodfields
    OWNER to postgres;
    
    
ALTER TABLE IF Exists lsrepositoriesdata ADD COLUMN IF NOT EXISTS expirydate date;
update LSprojectmaster set createdby = 'Administrator' where createdby is Null;
ALTER TABLE IF Exists lsrepositoriesdata ADD COLUMN IF NOT EXISTS inventoryid varchar(255);
ALTER TABLE IF Exists LSlogilabprotocoldetail ADD COLUMN IF NOT EXISTS approvelstatus integer;
ALTER TABLE IF Exists LsOrderSampleUpdate ADD COLUMN IF NOT EXISTS inventoryused integer;
update lsusergroup set createdby='Administrator' where usergroupcode=1;

ALTER TABLE IF Exists lsfile ADD COLUMN IF NOT EXISTS viewoption integer;

update lsfile set viewoption = 1 where viewoption is null;
update lsusergrouprightsmaster set sequenceorder=86 where orderno =65;
update lsusergrouprightsmaster set sequenceorder=87 where orderno =66;
update lsusergrouprightsmaster set sequenceorder=88 where orderno =67;
update lsusergrouprightsmaster set sequenceorder=89 where orderno =95;
update lsusergrouprightsmaster set sequenceorder=90 where orderno =97;
update lsusergrouprightsmaster set sequenceorder=91 where orderno =98;
update lsusergrouprightsmaster set sequenceorder=92 where orderno =108;
update lsusergrouprightsmaster set sequenceorder=93 where orderno =109;

update lsusergrouprights set sequenceorder=85 where displaytopic='IDS_SCN_INVENTORY' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=86 where displaytopic='IDS_TSK_ADDREPO' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=87 where displaytopic='IDS_TSK_EDITREPO' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=88 where displaytopic='IDS_SCN_LOGBOOK' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=89 where displaytopic='IDS_TSK_ADDLOGBOOK' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=90 where displaytopic='IDS_TSK_EDITLOGBOOK' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=91 where displaytopic='IDS_TSK_RETIRELOGBOOK' and usergroupid_usergroupcode=1;
update lsusergrouprights set sequenceorder=92 where displaytopic='IDS_TSK_REVIEWLOGBOOK' and usergroupid_usergroupcode=1;
update lsusergrouprightsmaster set modulename='IDS_MDL_INVENTORY' where screenname='IDS_SCN_INVENTORY';
update lsusergrouprights set modulename='IDS_MDL_INVENTORY' where screenname='IDS_SCN_INVENTORY';

update lsaudittrailconfiguration set ordersequnce=1 where screenname='IDS_SCN_SHEETORDERS';
update lsaudittrailconfiguration set ordersequnce=2 where screenname='IDS_SCN_PROTOCOLORDERS';
update lsaudittrailconfiguration set ordersequnce=3 where screenname='IDS_SCN_SHEETTEMPLATES';
update lsaudittrailconfiguration set ordersequnce=4 where screenname='IDS_SCN_PROTOCOLTEMP';
update lsaudittrailconfiguration set ordersequnce=5 where screenname='IDS_SCN_TEMPLATEMAPPING';
update lsaudittrailconfiguration set ordersequnce=6 where screenname='IDS_SCN_TASKMASTER';
update lsaudittrailconfiguration set ordersequnce=7 where screenname='IDS_SCN_PROJECTMASTER';
update lsaudittrailconfiguration set ordersequnce=8 where screenname='IDS_SCN_SAMPLEMASTER';
update lsaudittrailconfiguration set ordersequnce=9 where screenname='IDS_SCN_UNLOCKORDERS';
update lsaudittrailconfiguration set ordersequnce=10 where screenname='IDS_SCN_SITEMASTER';
update lsaudittrailconfiguration set ordersequnce=11 where screenname='IDS_SCN_USERGROUP';
update lsaudittrailconfiguration set ordersequnce=12 where screenname='IDS_SCN_USERMASTER';
update lsaudittrailconfiguration set ordersequnce=14 where screenname='IDS_SCN_USERRIGHTS';
update lsaudittrailconfiguration set ordersequnce=15 where screenname='IDS_SCN_PROJECTTEAM';
update lsaudittrailconfiguration set ordersequnce=16 where screenname='IDS_SCN_ORDERWORKFLOW';
update lsaudittrailconfiguration set ordersequnce=17 where screenname='IDS_SCN_TEMPLATEWORKFLOW';
update lsaudittrailconfiguration set ordersequnce=18 where screenname='IDS_SCN_DOMAIN';
update lsaudittrailconfiguration set ordersequnce=19 where screenname='IDS_SCN_PASSWORDPOLICY';
update lsaudittrailconfiguration set ordersequnce=20 where screenname='IDS_SCN_INSTRUMENTCATEGORY';
update lsaudittrailconfiguration set ordersequnce=21 where screenname='IDS_SCN_INSTRUMENTMASTER';
update lsaudittrailconfiguration set ordersequnce=22 where screenname='IDS_SCN_DELIMITER';
update lsaudittrailconfiguration set ordersequnce=23 where screenname='IDS_SCN_METHODDELIMITER';
update lsaudittrailconfiguration set ordersequnce=24 where screenname='IDS_SCN_METHODMASTER';
update lsaudittrailconfiguration set ordersequnce=25 where screenname='IDS_SCN_AUDITTRAILHIS';
update lsaudittrailconfiguration set ordersequnce=26 where screenname='IDS_SCN_CFRSETTINGS';
update lsaudittrailconfiguration set ordersequnce=27 where screenname='IDS_SCN_AUDITTRAILCONFIG';
update lsaudittrailconfiguration set ordersequnce=28 where screenname='IDS_SCN_REPORTS';
update lsaudittrailconfiguration set ordersequnce=29 where screenname='IDS_SCN_UNITMASTER';
update lsaudittrailconfiguration set ordersequnce=33 where screenname='IDS_SCN_SECTIONMASTER';
update lsaudittrailconfiguration set ordersequnce=34 where screenname='IDS_SCN_STORAGELOCATION';
update lsaudittrailconfiguration set ordersequnce=35 where screenname='IDS_SCN_MATERIALCATEGORY';
update lsaudittrailconfiguration set ordersequnce=36 where screenname='IDS_SCN_MATERIAL';
update lsaudittrailconfiguration set ordersequnce=37 where screenname='IDS_SCN_MATERIALINVENTORY';
update lsaudittrailconfiguration set ordersequnce=38 where screenname='IDS_SCN_INVENTORY';
update lsaudittrailconfiguration set ordersequnce=39 where screenname='IDS_SCN_LOGBOOK';

update lsaudittrailconfiguration set ordersequnce=3 where taskname='IDS_TSK_EDITSHEET';
update lsaudittrailconfiguration set ordersequnce=4 where taskname='IDS_TSK_EDITPROTOCOL';
update lsaudittrailconfiguration set ordersequnce=6 where taskname='IDS_TSK_EDITTASK';
update lsaudittrailconfiguration set ordersequnce=7 where taskname='IDS_TSK_EDITPROJECT';
update lsaudittrailconfiguration set ordersequnce=8 where taskname='IDS_TSK_EDITSAMPLE';
delete from lsaudittrailconfiguration where taskname='IDS_TSK_DELETED' and modulename='IDS_MDL_PARSER';
update lsaudittrailconfigmaster set taskname='IDS_TSK_DELETEPARSER' where serialno in (85,88,91,94,97);
update lsaudittrailconfiguration set taskname='IDS_TSK_DELETEPARSER' where taskname='IDS_TSK_DELETED' and modulename='IDS_MDL_PARSER';


update lsaudittrailconfiguration set modulename='IDS_MDL_ORDERS' where screenname IN ('IDS_SCN_PROTOCOLORDERS','IDS_SCN_SHEETORDERS');
update lsaudittrailconfiguration set modulename='IDS_MDL_TEMPLATES' where screenname IN ('IDS_SCN_SHEETTEMPLATES','IDS_SCN_TEMPLATEMAPPING','IDS_SCN_PROTOCOLTEMPLATES');
update lsaudittrailconfiguration set modulename='IDS_MDL_MASTERS' where screenname IN ('IDS_SCN_TASKMASTER','IDS_SCN_PROJECTMASTER','IDS_SCN_SAMPLEMASTER','IDS_SCN_UNLOCKORDERS');
update lsaudittrailconfiguration set modulename='IDS_MDL_SETUP' where screenname IN ('IDS_SCN_USERGROUP','IDS_SCN_SITEMASTER','IDS_SCN_USERMASTER','IDS_SCN_PROJECTTEAM','IDS_SCN_USERRIGHTS','IDS_SCN_ORDERWORKFLOW','IDS_SCN_TEMPLATEWORKFLOW','IDS_SCN_DOMAIN','IDS_SCN_PASSWORDPOLICY');
update lsaudittrailconfiguration set modulename='IDS_MDL_AUDITTRAIL' where screenname IN ('IDS_SCN_CFRSETTINGS','IDS_SCN_AUDITTRAILCONFIG');
update lsaudittrailconfiguration set modulename='IDS_MDL_REPORTS' where screenname ='IDS_SCN_REPORTS';
update lsaudittrailconfiguration set modulename='IDS_MDL_PARSER' where screenname IN ('IDS_SCN_INSTRUMENTCATEGORY','IDS_SCN_INSTRUMENTMASTER','IDS_SCN_DELIMITER','IDS_SCN_METHODDELIMITER','IDS_SCN_METHODMASTER');
update lsaudittrailconfiguration set modulename='IDS_MDL_INVENTORY' where screenname IN ('IDS_SCN_UNITMASTER','IDS_SCN_SECTIONMASTER','IDS_SCN_STORAGELOCATION','IDS_SCN_MATERIALCATEGORY','IDS_SCN_MATERIAL','IDS_SCN_MATERIALINVENTORY');
update lsaudittrailconfiguration set modulename='IDS_MDL_LOGBOOK' where screenname ='IDS_SCN_LOGBOOK';

ALTER TABLE IF Exists lslogilablimsorderdetail ADD COLUMN IF NOT EXISTS material_nmaterialcode integer;
DO
$do$
declare
  multiusergroupcount integer :=0;
begin

SELECT count(*) into multiusergroupcount FROM information_schema.table_constraints WHERE constraint_name='fkawjfm6e00pklunwofd3ebfi5v'
AND table_name='lslogilablimsorderdetail';
 IF multiusergroupcount =0 THEN
    ALTER TABLE ONLY lslogilablimsorderdetail ADD CONSTRAINT fkawjfm6e00pklunwofd3ebfi5v FOREIGN KEY (material_nmaterialcode) REFERENCES material(nmaterialcode);
   END IF;
END
$do$;

ALTER TABLE IF Exists lslogilablimsorderdetail ADD COLUMN IF NOT EXISTS materialinventory_nmaterialinventorycode integer;
DO
$do$
declare
  multiusergroupcount integer :=0;
begin

SELECT count(*) into multiusergroupcount FROM information_schema.table_constraints WHERE constraint_name='fkhp9i9obfk74cs1gjvj0isuk4x'
AND table_name='lslogilablimsorderdetail';
 IF multiusergroupcount =0 THEN
    ALTER TABLE ONLY lslogilablimsorderdetail ADD CONSTRAINT fkhp9i9obfk74cs1gjvj0isuk4x FOREIGN KEY (materialinventory_nmaterialinventorycode) REFERENCES materialinventory(nmaterialinventorycode);
   END IF;
END
$do$;

ALTER TABLE IF Exists LSlogilabprotocoldetail ADD COLUMN IF NOT EXISTS material_nmaterialcode integer;
DO
$do$
declare
  multiusergroupcount integer :=0;
begin

SELECT count(*) into multiusergroupcount FROM information_schema.table_constraints WHERE constraint_name='fkstcm67npdiupengejgrnayedf' 
AND table_name='LSlogilabprotocoldetail';

IF multiusergroupcount =0 THEN
    Alter TABLE ONLY LSlogilabprotocoldetail DROP CONSTRAINT if EXISTS fkstcm67npdiupengejgrnayedf;
    ALTER TABLE ONLY LSlogilabprotocoldetail ADD CONSTRAINT fkstcm67npdiupengejgrnayedf FOREIGN KEY (material_nmaterialcode) REFERENCES material(nmaterialcode);
   END IF;
END
$do$;

ALTER TABLE IF Exists LSlogilabprotocoldetail ADD COLUMN IF NOT EXISTS materialinventory_nmaterialinventorycode integer;
DO
$do$
declare  
    multiusergroupcount integer :=0;
begin

SELECT count(*) into multiusergroupcount FROM information_schema.table_constraints WHERE constraint_name='fkt457hqlkbgfelrx793hg67nqb'
AND table_name='LSlogilabprotocoldetail';
 IF multiusergroupcount =0 THEN
    Alter TABLE ONLY LSlogilabprotocoldetail DROP CONSTRAINT if EXISTS fkt457hqlkbgfelrx793hg67nqb;
    ALTER TABLE ONLY LSlogilabprotocoldetail ADD CONSTRAINT fkt457hqlkbgfelrx793hg67nqb FOREIGN KEY (materialinventory_nmaterialinventorycode) REFERENCES materialinventory(nmaterialinventorycode);
   END IF;
END
$do$;
             
ALTER TABLE IF Exists instrumentcategory ADD COLUMN IF NOT EXISTS lssitemaster_sitecode integer;
 
DO
$do$
declare
  resultvalues integer :=0;
begin

SELECT count(*) into resultvalues FROM
information_schema.table_constraints WHERE constraint_name='fkkl0697aigp5fwx9aoxl7b5fk'
AND table_name='instrumentcategory';
 IF resultvalues =0 THEN
    ALTER TABLE ONLY instrumentcategory ADD CONSTRAINT fkkl0697aigp5fwx9aoxl7b5fk FOREIGN KEY (lssitemaster_sitecode) REFERENCES lssitemaster (sitecode);
   END IF;
END
$do$;  

update instrumentcategory set lssitemaster_sitecode=1 where lssitemaster_sitecode is Null;

ALTER TABLE IF Exists delimiter ADD COLUMN IF NOT EXISTS lssitemaster_sitecode integer;
 
DO
$do$
declare
  resultvalues integer :=0;
begin

SELECT count(*) into resultvalues FROM
information_schema.table_constraints WHERE constraint_name='fke7mdkv29a179e89p7dyljka57'
AND table_name='delimiter';
 IF resultvalues =0 THEN
    ALTER TABLE ONLY delimiter ADD CONSTRAINT fke7mdkv29a179e89p7dyljka57 FOREIGN KEY (lssitemaster_sitecode) REFERENCES lssitemaster (sitecode);
   END IF;
END
$do$;  

update delimiter set lssitemaster_sitecode=1 where lssitemaster_sitecode is Null;

ALTER TABLE IF Exists methoddelimiter ADD COLUMN IF NOT EXISTS lssitemaster_sitecode integer;
 
DO
$do$
declare
  resultvalues integer :=0;
begin

SELECT count(*) into resultvalues FROM
information_schema.table_constraints WHERE constraint_name='fk39k48l4vkd0dausrjxscoelw3'
AND table_name='methoddelimiter';
 IF resultvalues =0 
    THEN
    ALTER TABLE ONLY methoddelimiter ADD CONSTRAINT fk39k48l4vkd0dausrjxscoelw3 FOREIGN KEY (lssitemaster_sitecode) REFERENCES lssitemaster (sitecode);
   END IF;
END
$do$;  

update methoddelimiter set lssitemaster_sitecode=1 where lssitemaster_sitecode is Null;

ALTER TABLE IF Exists lstestmasterlocal ADD COLUMN IF NOT EXISTS teststatus varchar(20);

update LStestmasterlocal set teststatus = 'A' where status = 1;
update LStestmasterlocal set teststatus = 'D' where status = -1;

ALTER TABLE IF Exists lsprojectmaster ADD COLUMN IF NOT EXISTS projectstatus varchar(20);

update lsprojectmaster set projectstatus = 'A' where status = 1;
update lsprojectmaster set projectstatus = 'D' where status = -1;

ALTER TABLE IF Exists lssamplemaster ADD COLUMN IF NOT EXISTS samplestatus varchar(20);

update lssamplemaster set samplestatus = 'A' where status = 1;
update lssamplemaster set samplestatus = 'D' where status = -1;

ALTER TABLE IF Exists lsactiveuser ADD COLUMN IF NOT EXISTS lastactivetime timestamp without time zone;

ALTER TABLE IF Exists delimiter ADD COLUMN IF NOT EXISTS delimiterstatus varchar(20) ;
ALTER TABLE IF Exists methoddelimiter ADD COLUMN IF NOT EXISTS methoddelimiterstatus varchar(20) ;
ALTER TABLE IF Exists method ADD COLUMN IF NOT EXISTS methodstatus varchar(20) ;
ALTER TABLE IF Exists instrumentmaster ADD COLUMN IF NOT EXISTS inststatus varchar(20) ;
ALTER TABLE IF Exists instrumentcategory ADD COLUMN IF NOT EXISTS instcategorystatus varchar(20) ;

update delimiter set delimiterstatus='A' where status = 1;
update delimiter set delimiterstatus='D' where status = -1;

update instrumentmaster set inststatus='A' where status = 1;
update instrumentmaster set inststatus='D' where status = -1;

update methoddelimiter set methoddelimiterstatus='A' where status = 1;
update methoddelimiter set methoddelimiterstatus='D' where status = -1;

update method set methodstatus='A' where status = 1;
update method set methodstatus='D' where status = -1;

update instrumentcategory set instcategorystatus='A' where status = 1;
update instrumentcategory set instcategorystatus='D' where status = -1;

ALTER TABLE IF Exists lscfttransaction ADD COLUMN IF NOT EXISTS auditkey varchar(100);

 ALTER TABLE IF Exists subparserfield ADD COLUMN IF NOT EXISTS datatypekey integer;
 
 update subparserfield set datatypekey=1 where datatypekey is Null;
 
ALTER TABLE IF Exists material ADD COLUMN IF NOT EXISTS nsitecode INTEGER;
ALTER TABLE IF Exists materialinventory ADD COLUMN IF NOT EXISTS nsitecode INTEGER;

--update samplestoragelocation set sitekey = 1 where sitekey = -1;
--update materialcategory set nsitecode = 1 where materialcategory.nsitecode = -1;
update material set nsitecode = 1 where nsitecode ISNULL;
update materialinventory set nsitecode = 1 where nsitecode ISNULL;
ALTER TABLE IF Exists lslogbooks ALTER COLUMN logbookcode TYPE numeric(17,0) USING logbookcode::numeric;

ALTER TABLE IF Exists lslogbooks ADD COLUMN IF NOT EXISTS logbookid character varying(255);
ALTER TABLE IF Exists lslogbooks ADD COLUMN IF NOT EXISTS reviewedby character varying(255);
ALTER TABLE IF Exists lslogbooks ADD COLUMN IF NOT EXISTS revieweddate timestamp without time zone;

INSERT INTO parserignorechars(ignorechars)SELECT 'â†µâ†µâ' WHERE NOT EXISTS (SELECT ignorechars FROM parserignorechars WHERE ignorechars = 'â†µâ†µâ');
INSERT INTO parserignorechars(ignorechars)SELECT 'â†µ' WHERE NOT EXISTS (SELECT ignorechars FROM parserignorechars WHERE ignorechars = 'â†µ');

INSERT INTO parserignorechars(ignorechars)SELECT 'â†µâ†µâ†µâ†µ' WHERE NOT EXISTS (SELECT ignorechars FROM parserignorechars WHERE ignorechars = 'â†µâ†µâ†µâ†µ');
INSERT INTO parserignorechars(ignorechars)SELECT 'â†µâ' WHERE NOT EXISTS (SELECT ignorechars FROM parserignorechars WHERE ignorechars = 'â†µâ');

delete from lsusergrouprights where displaytopic='IDS_TSK_LIMSTESTORDER';
delete from lsusergrouprights where displaytopic='IDS_TSK_DASHBOARDINVENTORY';
delete from lsusergrouprightsmaster where displaytopic='IDS_TSK_DASHBOARDINVENTORY';
update lsusergrouprightsmaster set modulename='IDS_MDL_SETUP' where modulename='IDS_MDL_PARSER';
update lsusergrouprights set modulename='IDS_MDL_SETUP' where modulename='IDS_MDL_PARSER';
update lsusergrouprights set sedit='NA' where sedit ='1'and  displaytopic='IDS_TSK_ORDERSHAREDBYMEPROTOCOL';
update lsusergrouprights set sedit='NA' where sedit ='1'and  displaytopic='IDS_TSK_ORDERSHAREDTOMEPROTOCOL';
delete from lsusergrouprightsmaster where displaytopic ='IDS_TSK_DOWNLOADPDFEXCEL';
delete from lsusergrouprights where displaytopic='IDS_TSK_DOWNLOADPDFEXCEL';
delete from lsusergrouprightsmaster where displaytopic='IDS_TSK_DOWNLOADPDFEXCELSECTION';
delete from lsusergrouprights where displaytopic='IDS_TSK_DOWNLOADPDFEXCELSECTION';
delete from lsusergrouprightsmaster where displaytopic='IDS_TSK_DOWNLOADMATERILACATEGORY';
delete from lsusergrouprights where displaytopic='IDS_TSK_DOWNLOADMATERILACATEGORY';
update lsusergrouprights set sedit='NA' where sedit ='1'and  displaytopic='IDS_SCN_UNLOCKORDERS';
update lsusergrouprightsmaster set sedit='NA' where sedit ='0'and  displaytopic='IDS_SCN_UNLOCKORDERS';
update lsusergrouprights set screate='0' where screate ='NA' and  displaytopic='IDS_SCN_UNLOCKORDERS';
update lsusergrouprightsmaster set screate='0' where screate='NA' and  displaytopic='IDS_SCN_UNLOCKORDERS';
update lsusergrouprights set screate='1' where displaytopic='IDS_SCN_UNLOCKORDERS' and usergroupid_usergroupcode=1;
update lsusergrouprightsmaster set sdelete='NA' where displaytopic='IDS_SCN_LOGBOOK';
update lsusergrouprightsmaster set sedit='NA' where displaytopic='IDS_SCN_LOGBOOK';
update lsusergrouprightsmaster set screate='0' where displaytopic='IDS_SCN_AUDITTRAILCONFIG';
update lsusergrouprights set screate='1' where displaytopic='IDS_SCN_AUDITTRAILCONFIG' and usergroupid_usergroupcode=1;
update lsusergrouprightsmaster set screate='0' where displaytopic='IDS_SCN_ORDERWORKLOW';
update lsusergrouprightsmaster set screate='0' where displaytopic='IDS_SCN_TEMPLATEWORKFLOW';
update lsusergrouprightsmaster set screate='0' where displaytopic='IDS_SCN_PASSWORDPOLICY';
update lsusergrouprights set screate='1' where displaytopic='IDS_SCN_USERRIGHTS' and usergroupid_usergroupcode=1;
update lsusergrouprights set screate='1' where displaytopic='IDS_SCN_ORDERWORKLOW' and usergroupid_usergroupcode=1;
update lsusergrouprights set screate='1' where displaytopic='IDS_SCN_TEMPLATEWORKFLOW' and usergroupid_usergroupcode=1;
update lsusergrouprights set screate='1' where displaytopic='IDS_SCN_PASSWORDPOLICY' and usergroupid_usergroupcode=1;
update lsusergrouprightsmaster set sdelete='0' where displaytopic='IDS_SCN_PROJECTTEAM';
update lsusergrouprightsmaster set status='1,0,1' where displaytopic='IDS_SCN_PROJECTTEAM';
update lsusergrouprights set sdelete='1' where displaytopic='IDS_SCN_PROJECTTEAM' and usergroupid_usergroupcode=1;
delete from lsusergrouprightsmaster where displaytopic='IDS_TSK_IMPORTADS';
delete from lsusergrouprights where displaytopic='IDS_TSK_IMPORTADS';
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (120, 'IDS_SCN_MATERIALTYPE', 'IDS_MDL_INVENTORY', '0', 'NA', 'NA', 'NA', '0,0,0',97,'IDS_SCN_MATERIALTYPE') ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_SCN_MATERIALTYPE', 'IDS_MDL_INVENTORY', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_MATERIALTYPE'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_SCN_MATERIALTYPE' and usergroupid_usergroupcode = 1);
update lsaudittrailconfigmaster set modulename='IDS_MDL_SETUP' where lsaudittrailconfigmaster.modulename='IDS_MDL_PARSER';
delete from lsusergrouprightsmaster where displaytopic='IDS_TSK_LIMSTASKORDER';
delete from lsusergrouprights where displaytopic='IDS_SCN_SITEMASTER' and usergroupid_usergroupcode=1;
delete from lsusergrouprights where displaytopic='IDS_SCN_DOMAIN' and usergroupid_usergroupcode=1;
delete from lsusergrouprights where displaytopic='IDS_SCN_ACTIVEUSER' and usergroupid_usergroupcode=1;
INSERT INTO materialgrade (nmaterialgradecode, jsondata, ndefaultstatus, nsitecode, nstatus, smaterialgradename) VALUES (3, '{"sdescription": "", "smaterialgradename": "C"}', 4, -1, 1, 'C')on conflict(nmaterialgradecode) do nothing;

INSERT INTO materialgrade (nmaterialgradecode, jsondata, ndefaultstatus, nsitecode, nstatus, smaterialgradename) VALUES (4, '{"sdescription": "", "smaterialgradename": "D"}', 4, -1, 1, 'D')on conflict(nmaterialgradecode) do nothing;

update lsusergrouprightsmaster set sequenceorder=1 where displaytopic ='IDS_TSK_ORDEROVERVIEW' ;
update lsusergrouprightsmaster set sequenceorder=2 where displaytopic ='IDS_TSK_TEMPLATEOVERVIEW' ;
update lsusergrouprightsmaster set sequenceorder=3 where displaytopic ='IDS_TSK_ACTIVITIES' ;
update lsusergrouprightsmaster set sequenceorder=4 where displaytopic ='IDS_SCN_SHEETORDERS' ;
update lsusergrouprightsmaster set sequenceorder=5 where displaytopic ='IDS_TSK_FOLDERCREATION' ;
update lsusergrouprightsmaster set sequenceorder=6 where displaytopic ='IDS_TSK_ELNTASKORDER' ;
update lsusergrouprightsmaster set sequenceorder=7 where displaytopic ='IDS_TSK_RESEARCHACTIVITY' ;
update lsusergrouprightsmaster set sequenceorder=8 where displaytopic ='IDS_TSK_MANAGEEXCEL' ;
update lsusergrouprightsmaster set sequenceorder=9 where displaytopic ='IDS_TSK_SHEETEVALUATION' ;
update lsusergrouprightsmaster set sequenceorder=10 where displaytopic ='IDS_TSK_UPLOADSHEETORDER' ;
update lsusergrouprightsmaster set sequenceorder=11 where displaytopic ='IDS_TSK_ORDERSHAREDBYME' ;
update lsusergrouprightsmaster set sequenceorder=12 where displaytopic ='IDS_TSK_ORDERSHAREDTOME' ;
update lsusergrouprightsmaster set sequenceorder=13 where displaytopic ='IDS_TSK_SHEETORDEREXPORT' ;
update lsusergrouprightsmaster set sequenceorder=14 where displaytopic ='IDS_TSK_MOVEORDERS' ;
update lsusergrouprightsmaster set sequenceorder=15 where displaytopic ='IDS_SCN_PROTOCOLORDERS' ;
update lsusergrouprightsmaster set sequenceorder=16 where displaytopic ='IDS_TSK_FOLDERCREATIONPROTOCOL' ;
update lsusergrouprightsmaster set sequenceorder=17 where displaytopic ='IDS_TSK_ELNPROTOCOL' ;
update lsusergrouprightsmaster set sequenceorder=18 where displaytopic ='IDS_TSK_DYNAMICPROTOCOL' ;
update lsusergrouprightsmaster set sequenceorder=19 where displaytopic ='IDS_TSK_UPLOADPROTOCOLORDER' ;
update lsusergrouprightsmaster set sequenceorder=20 where displaytopic ='IDS_TSK_ORDERSHAREDBYMEPROTOCOL' ;
update lsusergrouprightsmaster set sequenceorder=21 where displaytopic ='IDS_TSK_ORDERSHAREDTOMEPROTOCOL' ;
update lsusergrouprightsmaster set sequenceorder=22 where displaytopic ='IDS_TSK_MOVEORDERSPROTOCOL' ;
update lsusergrouprightsmaster set sequenceorder=23 where displaytopic ='IDS_SCN_SHEETTEMPLATE' ;
update lsusergrouprightsmaster set sequenceorder=24 where displaytopic ='IDS_TSK_TEMPLATESHAREDBYME' ;
update lsusergrouprightsmaster set sequenceorder=25 where displaytopic ='IDS_TSK_TEMPLATESHAREDTOME' ;
update lsusergrouprightsmaster set sequenceorder=26 where displaytopic ='IDS_TSK_SHEETTEMPEXPORT' ;
update lsusergrouprightsmaster set sequenceorder=27 where displaytopic ='IDS_SCN_PROTOCOLTEMPLATE' ;
update lsusergrouprightsmaster set sequenceorder=28 where displaytopic ='IDS_TSK_NEWSTEP' ;
update lsusergrouprightsmaster set sequenceorder=29 where displaytopic ='IDS_TSK_PROTOCOLTEMPSHAREBYME' ;
update lsusergrouprightsmaster set sequenceorder=30 where displaytopic ='IDS_TSK_PROTOCOLTEMPSHARETOME' ;
update lsusergrouprightsmaster set sequenceorder=31 where displaytopic ='IDS_TSK_EXPORTPDF' ;
update lsusergrouprightsmaster set sequenceorder=32 where displaytopic ='IDS_TSK_SHEET' ;
update lsusergrouprightsmaster set sequenceorder=33 where displaytopic ='IDS_TSK_PROTOCOL' ;
update lsusergrouprightsmaster set sequenceorder=34 where displaytopic ='IDS_SCN_TASKMASTER' ;
update lsusergrouprightsmaster set sequenceorder=35 where displaytopic ='IDS_SCN_PROJECTMASTER' ;
update lsusergrouprightsmaster set sequenceorder=36 where displaytopic ='IDS_SCN_SAMPLEMASTER' ;
update lsusergrouprightsmaster set sequenceorder=37 where displaytopic ='IDS_SCN_UNLOCKORDERS' ;
update lsusergrouprightsmaster set sequenceorder=38 where displaytopic ='IDS_SCN_USERGROUP' ;
update lsusergrouprightsmaster set sequenceorder=39 where displaytopic ='IDS_TSK_ACTDEACT' ;
update lsusergrouprightsmaster set sequenceorder=40 where displaytopic ='IDS_SCN_USERMASTER' ;
update lsusergrouprightsmaster set sequenceorder=41 where displaytopic ='IDS_TSK_UNLOCK' ;
update lsusergrouprightsmaster set sequenceorder=42 where displaytopic ='IDS_TSK_ACTDEACTUSERMASTER' ;
update lsusergrouprightsmaster set sequenceorder=43 where displaytopic ='IDS_TSK_RESETPASSWORD' ;
update lsusergrouprightsmaster set sequenceorder=44 where displaytopic ='IDS_TSK_RETIRE' ;
update lsusergrouprightsmaster set sequenceorder=45 where displaytopic ='IDS_SCN_USERRIGHTS' ;
update lsusergrouprightsmaster set sequenceorder=46 where displaytopic ='IDS_SCN_PROJECTTEAM' ;
update lsusergrouprightsmaster set sequenceorder=47 where displaytopic ='IDS_SCN_ORDERWORKLOW' ;
update lsusergrouprightsmaster set sequenceorder=48 where displaytopic ='IDS_SCN_TEMPLATEWORKFLOW' ;
update lsusergrouprightsmaster set sequenceorder=49 where displaytopic ='IDS_SCN_PASSWORDPOLICY' ;
update lsusergrouprightsmaster set sequenceorder=50 where displaytopic ='IDS_SCN_PARSER' ;
update lsusergrouprightsmaster set sequenceorder=51 where displaytopic ='IDS_SCN_INSTRUMENTCATEGORY' ;
update lsusergrouprightsmaster set sequenceorder=52 where displaytopic ='IDS_SCN_INSTRUMENTMASTER' ;
update lsusergrouprightsmaster set sequenceorder=53 where displaytopic ='IDS_SCN_DELIMITER' ;
update lsusergrouprightsmaster set sequenceorder=54 where displaytopic ='IDS_SCN_METHODDELIMITER' ;
update lsusergrouprightsmaster set sequenceorder=55 where displaytopic ='IDS_SCN_METHODMASTER' ;
update lsusergrouprightsmaster set sequenceorder=56 where displaytopic ='IDS_SCN_AUDITTRAILHIS' ;
update lsusergrouprightsmaster set sequenceorder=57 where displaytopic ='IDS_TSK_REVIEWHISTORY' ;
update lsusergrouprightsmaster set sequenceorder=58 where displaytopic ='IDS_TSK_REVIEW' ;
update lsusergrouprightsmaster set sequenceorder=59 where displaytopic ='IDS_TSK_CREATEARCHIVE' ;
update lsusergrouprightsmaster set sequenceorder=60 where displaytopic ='IDS_TSK_OPENARCHIVE' ;
update lsusergrouprightsmaster set sequenceorder=61 where displaytopic ='IDS_TSK_EXPORT' ;
update lsusergrouprightsmaster set sequenceorder=62 where displaytopic ='IDS_SCN_CFRSETTINGS' ;
update lsusergrouprightsmaster set sequenceorder=63 where displaytopic ='IDS_SCN_AUDITTRAILCONFIG' ;
update lsusergrouprightsmaster set sequenceorder=64 where displaytopic ='IDS_SCN_REPORTS' ;
update lsusergrouprightsmaster set sequenceorder=65 where displaytopic ='IDS_TSK_NEWDOCUMENT' ;
update lsusergrouprightsmaster set sequenceorder=66 where displaytopic ='IDS_TSK_NEWTEMP' ;
update lsusergrouprightsmaster set sequenceorder=67 where displaytopic ='IDS_TSK_GENERATEREPORT' ;
update lsusergrouprightsmaster set sequenceorder=68 where displaytopic ='IDS_TSK_OPENREPORT' ;
update lsusergrouprightsmaster set sequenceorder=69 where displaytopic ='IDS_TSK_IMPORTDOCX' ;
update lsusergrouprightsmaster set sequenceorder=70 where displaytopic ='IDS_SCN_MATERIALTYPE' ;
update lsusergrouprightsmaster set sequenceorder=71 where displaytopic ='IDS_SCN_UNITMASTER' ;
update lsusergrouprightsmaster set sequenceorder=72 where displaytopic ='IDS_SCN_INVENTORY' ;
update lsusergrouprightsmaster set sequenceorder=73 where displaytopic ='IDS_TSK_ADDREPO' ;
update lsusergrouprightsmaster set sequenceorder=74 where displaytopic ='IDS_TSK_EDITREPO' ;
update lsusergrouprightsmaster set sequenceorder=75 where displaytopic ='IDS_SCN_SECTIONMASTER' ;
update lsusergrouprightsmaster set sequenceorder=76 where displaytopic ='IDS_SCN_STORAGELOCATION' ;
update lsusergrouprightsmaster set sequenceorder=77 where displaytopic ='IDS_SCN_MATERIALCATEGORY' ;
update lsusergrouprightsmaster set sequenceorder=78 where displaytopic ='IDS_SCN_MATERIAL' ;
update lsusergrouprightsmaster set sequenceorder=79 where displaytopic ='IDS_SCN_MATERIALINVENTORY' ;
update lsusergrouprightsmaster set sequenceorder=83 where displaytopic ='IDS_SCN_LOGBOOK' ;
update lsusergrouprightsmaster set sequenceorder=84 where displaytopic ='IDS_TSK_ADDLOGBOOK' ;
update lsusergrouprightsmaster set sequenceorder=85 where displaytopic ='IDS_TSK_EDITLOGBOOK' ;
update lsusergrouprightsmaster set sequenceorder=86 where displaytopic ='IDS_TSK_RETIRELOGBOOK' ;
update lsusergrouprightsmaster set sequenceorder=87 where displaytopic ='IDS_TSK_REVIEWLOGBOOK' ;




update lsusergrouprights set sequenceorder=1 where displaytopic ='IDS_TSK_ORDEROVERVIEW' ;
update lsusergrouprights set sequenceorder=2 where displaytopic ='IDS_TSK_TEMPLATEOVERVIEW' ;
update lsusergrouprights set sequenceorder=3 where displaytopic ='IDS_TSK_ACTIVITIES' ;
update lsusergrouprights set sequenceorder=4 where displaytopic ='IDS_SCN_SHEETORDERS' ;
update lsusergrouprights set sequenceorder=5 where displaytopic ='IDS_TSK_FOLDERCREATION' ;
update lsusergrouprights set sequenceorder=6 where displaytopic ='IDS_TSK_ELNTASKORDER' ;
update lsusergrouprights set sequenceorder=7 where displaytopic ='IDS_TSK_RESEARCHACTIVITY' ;
update lsusergrouprights set sequenceorder=8 where displaytopic ='IDS_TSK_MANAGEEXCEL' ;
update lsusergrouprights set sequenceorder=9 where displaytopic ='IDS_TSK_SHEETEVALUATION' ;
update lsusergrouprights set sequenceorder=10 where displaytopic ='IDS_TSK_UPLOADSHEETORDER' ;
update lsusergrouprights set sequenceorder=11 where displaytopic ='IDS_TSK_ORDERSHAREDBYME' ;
update lsusergrouprights set sequenceorder=12 where displaytopic ='IDS_TSK_ORDERSHAREDTOME' ;
update lsusergrouprights set sequenceorder=13 where displaytopic ='IDS_TSK_SHEETORDEREXPORT' ;
update lsusergrouprights set sequenceorder=14 where displaytopic ='IDS_TSK_MOVEORDERS' ;
update lsusergrouprights set sequenceorder=15 where displaytopic ='IDS_SCN_PROTOCOLORDERS' ;
update lsusergrouprights set sequenceorder=16 where displaytopic ='IDS_TSK_FOLDERCREATIONPROTOCOL' ;
update lsusergrouprights set sequenceorder=17 where displaytopic ='IDS_TSK_ELNPROTOCOL' ;
update lsusergrouprights set sequenceorder=18 where displaytopic ='IDS_TSK_DYNAMICPROTOCOL' ;
update lsusergrouprights set sequenceorder=19 where displaytopic ='IDS_TSK_UPLOADPROTOCOLORDER' ;
update lsusergrouprights set sequenceorder=20 where displaytopic ='IDS_TSK_ORDERSHAREDBYMEPROTOCOL' ;
update lsusergrouprights set sequenceorder=21 where displaytopic ='IDS_TSK_ORDERSHAREDTOMEPROTOCOL' ;
update lsusergrouprights set sequenceorder=22 where displaytopic ='IDS_TSK_MOVEORDERSPROTOCOL' ;
update lsusergrouprights set sequenceorder=23 where displaytopic ='IDS_SCN_SHEETTEMPLATE' ;
update lsusergrouprights set sequenceorder=24 where displaytopic ='IDS_TSK_TEMPLATESHAREDBYME' ;
update lsusergrouprights set sequenceorder=25 where displaytopic ='IDS_TSK_TEMPLATESHAREDTOME' ;
update lsusergrouprights set sequenceorder=26 where displaytopic ='IDS_TSK_SHEETTEMPEXPORT' ;
update lsusergrouprights set sequenceorder=27 where displaytopic ='IDS_SCN_PROTOCOLTEMPLATE' ;
update lsusergrouprights set sequenceorder=28 where displaytopic ='IDS_TSK_NEWSTEP' ;
update lsusergrouprights set sequenceorder=29 where displaytopic ='IDS_TSK_PROTOCOLTEMPSHAREBYME' ;
update lsusergrouprights set sequenceorder=30 where displaytopic ='IDS_TSK_PROTOCOLTEMPSHARETOME' ;
update lsusergrouprights set sequenceorder=31 where displaytopic ='IDS_TSK_EXPORTPDF' ;
update lsusergrouprights set sequenceorder=32 where displaytopic ='IDS_TSK_SHEET' ;
update lsusergrouprights set sequenceorder=33 where displaytopic ='IDS_TSK_PROTOCOL' ;
update lsusergrouprights set sequenceorder=34 where displaytopic ='IDS_SCN_TASKMASTER' ;
update lsusergrouprights set sequenceorder=35 where displaytopic ='IDS_SCN_PROJECTMASTER' ;
update lsusergrouprights set sequenceorder=36 where displaytopic ='IDS_SCN_SAMPLEMASTER' ;
update lsusergrouprights set sequenceorder=37 where displaytopic ='IDS_SCN_UNLOCKORDERS' ;
update lsusergrouprights set sequenceorder=38 where displaytopic ='IDS_SCN_USERGROUP' ;
update lsusergrouprights set sequenceorder=39 where displaytopic ='IDS_TSK_ACTDEACT' ;
update lsusergrouprights set sequenceorder=40 where displaytopic ='IDS_SCN_USERMASTER' ;
update lsusergrouprights set sequenceorder=41 where displaytopic ='IDS_TSK_UNLOCK' ;
update lsusergrouprights set sequenceorder=42 where displaytopic ='IDS_TSK_ACTDEACTUSERMASTER' ;
update lsusergrouprights set sequenceorder=43 where displaytopic ='IDS_TSK_RESETPASSWORD' ;
update lsusergrouprights set sequenceorder=44 where displaytopic ='IDS_TSK_RETIRE' ;
update lsusergrouprights set sequenceorder=45 where displaytopic ='IDS_SCN_USERRIGHTS' ;
update lsusergrouprights set sequenceorder=46 where displaytopic ='IDS_SCN_PROJECTTEAM' ;
update lsusergrouprights set sequenceorder=47 where displaytopic ='IDS_SCN_ORDERWORKLOW' ;
update lsusergrouprights set sequenceorder=48 where displaytopic ='IDS_SCN_TEMPLATEWORKFLOW' ;
update lsusergrouprights set sequenceorder=49 where displaytopic ='IDS_SCN_PASSWORDPOLICY' ;
update lsusergrouprights set sequenceorder=50 where displaytopic ='IDS_SCN_PARSER' ;
update lsusergrouprights set sequenceorder=51 where displaytopic ='IDS_SCN_INSTRUMENTCATEGORY' ;
update lsusergrouprights set sequenceorder=52 where displaytopic ='IDS_SCN_INSTRUMENTMASTER' ;
update lsusergrouprights set sequenceorder=53 where displaytopic ='IDS_SCN_DELIMITER' ;
update lsusergrouprights set sequenceorder=54 where displaytopic ='IDS_SCN_METHODDELIMITER' ;
update lsusergrouprights set sequenceorder=55 where displaytopic ='IDS_SCN_METHODMASTER' ;
update lsusergrouprights set sequenceorder=56 where displaytopic ='IDS_SCN_AUDITTRAILHIS' ;
update lsusergrouprights set sequenceorder=57 where displaytopic ='IDS_TSK_REVIEWHISTORY' ;
update lsusergrouprights set sequenceorder=58 where displaytopic ='IDS_TSK_REVIEW' ;
update lsusergrouprights set sequenceorder=59 where displaytopic ='IDS_TSK_CREATEARCHIVE' ;
update lsusergrouprights set sequenceorder=60 where displaytopic ='IDS_TSK_OPENARCHIVE' ;
update lsusergrouprights set sequenceorder=61 where displaytopic ='IDS_TSK_EXPORT' ;
update lsusergrouprights set sequenceorder=62 where displaytopic ='IDS_SCN_CFRSETTINGS' ;
update lsusergrouprights set sequenceorder=63 where displaytopic ='IDS_SCN_AUDITTRAILCONFIG' ;
update lsusergrouprights set sequenceorder=64 where displaytopic ='IDS_SCN_REPORTS' ;
update lsusergrouprights set sequenceorder=65 where displaytopic ='IDS_TSK_NEWDOCUMENT' ;
update lsusergrouprights set sequenceorder=66 where displaytopic ='IDS_TSK_NEWTEMP' ;
update lsusergrouprights set sequenceorder=67 where displaytopic ='IDS_TSK_GENERATEREPORT' ;
update lsusergrouprights set sequenceorder=68 where displaytopic ='IDS_TSK_OPENREPORT' ;


update	lsusergrouprights set	screenname='IDS_SCN_AUDITTRAILCONFIG'	where displaytopic ='IDS_SCN_AUDITTRAILCONFIG';
update	lsusergrouprights set	screenname='IDS_SCN_AUDITTRAILHIS'	where displaytopic ='IDS_SCN_AUDITTRAILHIS';
update	lsusergrouprights set	screenname='IDS_SCN_CFRSETTINGS'	where displaytopic ='IDS_SCN_CFRSETTINGS';
update	lsusergrouprights set	screenname='IDS_SCN_DELIMITER'	where displaytopic ='IDS_SCN_DELIMITER';
update	lsusergrouprights set	screenname='IDS_SCN_INSTRUMENTCATEGORY'	where displaytopic ='IDS_SCN_INSTRUMENTCATEGORY';
update	lsusergrouprights set	screenname='IDS_SCN_INSTRUMENTMASTER'	where displaytopic ='IDS_SCN_INSTRUMENTMASTER';
update	lsusergrouprights set	screenname='IDS_SCN_INVENTORY'	where displaytopic ='IDS_SCN_INVENTORY';
update	lsusergrouprights set	screenname='IDS_SCN_LOGBOOK'	where displaytopic ='IDS_SCN_LOGBOOK';
update	lsusergrouprights set	screenname='IDS_SCN_MATERIAL'	where displaytopic ='IDS_SCN_MATERIAL';
update	lsusergrouprights set	screenname='IDS_SCN_MATERIALCATEGORY'	where displaytopic ='IDS_SCN_MATERIALCATEGORY';
update	lsusergrouprights set	screenname='IDS_SCN_MATERIALINVENTORY'	where displaytopic ='IDS_SCN_MATERIALINVENTORY';
update	lsusergrouprights set	screenname='IDS_SCN_MATERIALTYPE'	where displaytopic ='IDS_SCN_MATERIALTYPE';
update	lsusergrouprights set	screenname='IDS_SCN_METHODDELIMITER'	where displaytopic ='IDS_SCN_METHODDELIMITER';
update	lsusergrouprights set	screenname='IDS_SCN_METHODMASTER'	where displaytopic ='IDS_SCN_METHODMASTER';
update	lsusergrouprights set	screenname='IDS_SCN_ORDERWORKLOW'	where displaytopic ='IDS_SCN_ORDERWORKLOW';
update	lsusergrouprights set	screenname='IDS_SCN_PARSER'	where displaytopic ='IDS_SCN_PARSER';
update	lsusergrouprights set	screenname='IDS_SCN_PASSWORDPOLICY'	where displaytopic ='IDS_SCN_PASSWORDPOLICY';
update	lsusergrouprights set	screenname='IDS_SCN_PROJECTMASTER'	where displaytopic ='IDS_SCN_PROJECTMASTER';
update	lsusergrouprights set	screenname='IDS_SCN_PROJECTTEAM'	where displaytopic ='IDS_SCN_PROJECTTEAM';
update	lsusergrouprights set	screenname='IDS_SCN_PROTOCOLORDERS'	where displaytopic ='IDS_SCN_PROTOCOLORDERS';
update	lsusergrouprights set	screenname='IDS_SCN_PROTOCOLTEMPLATE'	where displaytopic ='IDS_SCN_PROTOCOLTEMPLATE';
update	lsusergrouprights set	screenname='IDS_SCN_REPORTS'	where displaytopic ='IDS_SCN_REPORTS';
update	lsusergrouprights set	screenname='IDS_SCN_SAMPLEMASTER'	where displaytopic ='IDS_SCN_SAMPLEMASTER';
update	lsusergrouprights set	screenname='IDS_SCN_SECTIONMASTER'	where displaytopic ='IDS_SCN_SECTIONMASTER';
update	lsusergrouprights set	screenname='IDS_SCN_SHEETORDERS'	where displaytopic ='IDS_SCN_SHEETORDERS';
update	lsusergrouprights set	screenname='IDS_SCN_SHEETTEMPLATE'	where displaytopic ='IDS_SCN_SHEETTEMPLATE';
update	lsusergrouprights set	screenname='IDS_SCN_STORAGELOCATION'	where displaytopic ='IDS_SCN_STORAGELOCATION';
update	lsusergrouprights set	screenname='IDS_SCN_TASKMASTER'	where displaytopic ='IDS_SCN_TASKMASTER';
update	lsusergrouprights set	screenname='IDS_SCN_TEMPLATEWORKFLOW'	where displaytopic ='IDS_SCN_TEMPLATEWORKFLOW';
update	lsusergrouprights set	screenname='IDS_SCN_UNITMASTER'	where displaytopic ='IDS_SCN_UNITMASTER';
update	lsusergrouprights set	screenname='IDS_SCN_UNLOCKORDERS'	where displaytopic ='IDS_SCN_UNLOCKORDERS';
update	lsusergrouprights set	screenname='IDS_SCN_USERGROUP'	where displaytopic ='IDS_SCN_USERGROUP';
update	lsusergrouprights set	screenname='IDS_SCN_USERMASTER'	where displaytopic ='IDS_SCN_USERMASTER';
update	lsusergrouprights set	screenname='IDS_SCN_USERRIGHTS'	where displaytopic ='IDS_SCN_USERRIGHTS';
update	lsusergrouprights set	screenname='IDS_SCN_USERGROUP'	where displaytopic ='IDS_TSK_ACTDEACT';
update	lsusergrouprights set	screenname='IDS_SCN_USERMASTER'	where displaytopic ='IDS_TSK_ACTDEACTUSERMASTER';
update	lsusergrouprights set	screenname='IDS_SCN_DASHBOARD'	where displaytopic ='IDS_TSK_ACTIVITIES';
update	lsusergrouprights set	screenname='IDS_SCN_LOGBOOK'	where displaytopic ='IDS_TSK_ADDLOGBOOK';
update	lsusergrouprights set	screenname='IDS_SCN_INVENTORY'	where displaytopic ='IDS_TSK_ADDREPO';
update	lsusergrouprights set	screenname='IDS_SCN_AUDITTRAILHIS'	where displaytopic ='IDS_TSK_CREATEARCHIVE';
update	lsusergrouprights set	screenname='IDS_SCN_PROTOCOLORDERS'	where displaytopic ='IDS_TSK_DYNAMICPROTOCOL';
update	lsusergrouprights set	screenname='IDS_SCN_LOGBOOK' where displaytopic ='IDS_TSK_EDITLOGBOOK';
update	lsusergrouprights set	screenname='IDS_SCN_INVENTORY'	where displaytopic ='IDS_TSK_EDITREPO';
update	lsusergrouprights set	screenname='IDS_SCN_PROTOCOLORDERS'	where displaytopic ='IDS_TSK_ELNPROTOCOL';
update	lsusergrouprights set	screenname='IDS_SCN_SHEETORDERS'	where displaytopic ='IDS_TSK_ELNTASKORDER';
update	lsusergrouprights set	screenname='IDS_SCN_AUDITTRAILHIS'	where displaytopic ='IDS_TSK_EXPORT';
update	lsusergrouprights set	screenname='IDS_SCN_PROTOCOLTEMPLATE'	where displaytopic ='IDS_TSK_EXPORTPDF';
update	lsusergrouprights set	screenname='IDS_SCN_SHEETORDERS'	where displaytopic ='IDS_TSK_FOLDERCREATION';
update	lsusergrouprights set	screenname='IDS_SCN_PROTOCOLORDERS'	where displaytopic ='IDS_TSK_FOLDERCREATIONPROTOCOL';
update	lsusergrouprights set	screenname='IDS_SCN_REPORTS'	where displaytopic ='IDS_TSK_GENERATEREPORT';
update	lsusergrouprights set	screenname='IDS_SCN_REPORTS'	where displaytopic ='IDS_TSK_IMPORTDOCX';
update	lsusergrouprights set	screenname='IDS_SCN_SHEETORDERS'	where displaytopic ='IDS_TSK_MANAGEEXCEL';
update	lsusergrouprights set	screenname='IDS_SCN_SHEETORDERS'	where displaytopic ='IDS_TSK_MOVEORDERS';
update	lsusergrouprights set	screenname='IDS_SCN_PROTOCOLORDERS'	where displaytopic ='IDS_TSK_MOVEORDERSPROTOCOL';
update	lsusergrouprights set	screenname='IDS_SCN_REPORTS'	where displaytopic ='IDS_TSK_NEWDOCUMENT';
update	lsusergrouprights set	screenname='IDS_SCN_REPORTS'	where displaytopic ='IDS_TSK_NEWDOCUMENT';
update	lsusergrouprights set	screenname='IDS_SCN_PROTOCOLTEMPLATE'	where displaytopic ='IDS_TSK_NEWSTEP';
update	lsusergrouprights set	screenname='IDS_SCN_REPORTS'	where displaytopic ='IDS_TSK_NEWTEMP';
update	lsusergrouprights set	screenname='IDS_SCN_AUDITTRAILHIS'	where displaytopic ='IDS_TSK_OPENARCHIVE';
update	lsusergrouprights set	screenname='IDS_SCN_REPORTS'	where displaytopic ='IDS_TSK_OPENREPORT';
update	lsusergrouprights set	screenname='IDS_SCN_DASHBOARD'	where displaytopic ='IDS_TSK_ORDEROVERVIEW';
update	lsusergrouprights set	screenname='IDS_SCN_SHEETORDERS'	where displaytopic ='IDS_TSK_ORDERSHAREDBYME';
update	lsusergrouprights set	screenname='IDS_SCN_PROTOCOLORDERS'	where displaytopic ='IDS_TSK_ORDERSHAREDBYMEPROTOCOL';
update	lsusergrouprights set	screenname='IDS_SCN_SHEETORDERS'	where displaytopic ='IDS_TSK_ORDERSHAREDTOME';
update	lsusergrouprights set	screenname='IDS_SCN_PROTOCOLORDERS'	where displaytopic ='IDS_TSK_ORDERSHAREDTOMEPROTOCOL';
update	lsusergrouprights set	screenname='IDS_SCN_TEMPLATEMAPPING'	where displaytopic ='IDS_TSK_PROTOCOL';
update	lsusergrouprights set	screenname='IDS_SCN_PROTOCOLTEMPLATE'	where displaytopic =' IDS_TSK_PROTOCOLTEMPSHAREBYME';
update	lsusergrouprights set	screenname='IDS_SCN_PROTOCOLTEMPLATE'	where displaytopic ='IDS_TSK_PROTOCOLTEMPSHARETOME';
update	lsusergrouprights set	screenname='IDS_SCN_SHEETORDERS'	where displaytopic ='IDS_TSK_RESEARCHACTIVITY';
update	lsusergrouprights set	screenname='IDS_SCN_USERMASTER'	where displaytopic ='IDS_TSK_RESETPASSWORD';
update	lsusergrouprights set	screenname='IDS_SCN_USERMASTER'	where displaytopic ='IDS_TSK_RETIRE';
update	lsusergrouprights set	screenname='IDS_SCN_LOGBOOK'	where displaytopic ='IDS_TSK_RETIRELOGBOOK';
update	lsusergrouprights set	screenname='IDS_SCN_AUDITTRAILHIS'	where displaytopic ='IDS_TSK_REVIEW';
update	lsusergrouprights set	screenname='IDS_SCN_AUDITTRAILHIS'	where displaytopic ='IDS_TSK_REVIEWHISTORY';
update	lsusergrouprights set	screenname='IDS_SCN_LOGBOOK'	where displaytopic ='IDS_TSK_REVIEWLOGBOOK';
update	lsusergrouprights set	screenname='IDS_SCN_TEMPLATEMAPPING'	where displaytopic ='IDS_TSK_SHEET';
update	lsusergrouprights set	screenname='IDS_SCN_SHEETORDERS' where displaytopic ='IDS_TSK_SHEETEVALUATION';
update	lsusergrouprights set	screenname='IDS_SCN_SHEETORDERS'	where displaytopic ='IDS_TSK_SHEETORDEREXPORT';
update	lsusergrouprights set	screenname='IDS_SCN_SHEETTEMPLATE'	where displaytopic ='IDS_TSK_SHEETTEMPEXPORT';
update	lsusergrouprights set	screenname='IDS_SCN_DASHBOARD'	where displaytopic ='IDS_TSK_TEMPLATEOVERVIEW';
update	lsusergrouprights set	screenname='IDS_SCN_SHEETTEMPLATE'	where displaytopic ='IDS_TSK_TEMPLATESHAREDBYME';
update	lsusergrouprights set	screenname='IDS_SCN_SHEETTEMPLATE'	where displaytopic ='IDS_TSK_TEMPLATESHAREDTOME';
update	lsusergrouprights set	screenname='IDS_SCN_USERMASTER'	where displaytopic ='IDS_TSK_UNLOCK';
update	lsusergrouprights set	screenname='IDS_SCN_PROTOCOLORDERS'	where displaytopic ='IDS_TSK_UPLOADPROTOCOLORDER';
update	lsusergrouprights set	screenname='IDS_SCN_SHEETORDERS'	where displaytopic ='IDS_TSK_UPLOADSHEETORDER';

update lsusergrouprights set screate='1' where displaytopic='IDS_SCN_USERRIGHTS' and screate='NA' and usergroupid_usergroupcode !=1;  
update lsusergrouprights set screate='1' where displaytopic='IDS_SCN_TEMPLATEWORKFLOW'and screate='NA' and usergroupid_usergroupcode !=1; 
update lsusergrouprights set screate='1' where displaytopic='IDS_SCN_PASSWORDPOLICY' and screate='NA'  and usergroupid_usergroupcode !=1; 
update lsusergrouprights set screate='1' where displaytopic='IDS_SCN_ORDERWORKLOW' and screate='NA'  and usergroupid_usergroupcode !=1; 
update lsusergrouprights set screate='1' where displaytopic='IDS_SCN_AUDITTRAILCONFIG' and screate='NA'  and usergroupid_usergroupcode !=1;
update lsusergrouprights set sdelete='1' where displaytopic='IDS_SCN_PROJECTTEAM' and sdelete='NA'  and usergroupid_usergroupcode !=1; 

ALTER TABLE IF Exists material ADD COLUMN IF NOT EXISTS createddate date default CURRENT_DATE;

update material set createddate = CURRENT_DATE where createddate ISNULL;

ALTER TABLE IF Exists lsusersteam ADD COLUMN IF NOT EXISTS projectteamstatus varchar(20);
update lsusersteam set projectteamstatus='A' where status=1;
update lsusersteam set projectteamstatus='D' where status=-1;

INSERT into LSusergrouprights(displaytopic,modulename,createdby, createdon, sallow, screate, sdelete, sedit, lssitemaster_sitecode, usergroupid_usergroupcode,screenname) values ( 'IDS_SCN_SITEMASTER','IDS_MDL_SETUP','administrator', CAST('2022-01-21 00:00:00.000' AS date),'1','1','1','1',1,1,'IDS_SCN_SITEMASTER') ON CONFLICT(orderno)DO NOTHING;

INSERT into LSusergrouprights(displaytopic,modulename,createdby, createdon, sallow, screate, sdelete, sedit, lssitemaster_sitecode, usergroupid_usergroupcode,screenname) values ( 'IDS_SCN_DOMAIN','IDS_MDL_SETUP','administrator', CAST('2022-01-21 00:00:00.000' AS date),'1','1','1','1',1,1,'IDS_SCN_DOMAIN')ON CONFLICT(orderno)DO NOTHING;

INSERT into LSusergrouprights(displaytopic,modulename,createdby, createdon, sallow, screate, sdelete, sedit, lssitemaster_sitecode, usergroupid_usergroupcode,screenname) values ( 'IDS_SCN_ACTIVEUSER','IDS_MDL_SETUP','administrator', CAST('2022-01-21 00:00:00.000' AS date),'1','1','1','1',1,1,'IDS_SCN_ACTIVEUSER')ON CONFLICT(orderno)DO NOTHING;

ALTER TABLE IF Exists material ALTER COLUMN createddate TYPE timestamp without time zone;

update lsaudittrailconfiguration set modulename='IDS_MDL_SETUP' where modulename='IDS_MDL_PARSER';
delete from lsusergrouprights where screenname='IDS_SCN_PARSER';
delete from lsusergrouprightsmaster where screenname='IDS_SCN_PARSER';

update lsusergrouprights set sedit ='1' where displaytopic='IDS_TSK_FOLDERCREATION' and sedit='NA' and usergroupid_usergroupcode =1;
update lsusergrouprights set sedit ='0' where displaytopic='IDS_TSK_FOLDERCREATION' and sedit='NA' and usergroupid_usergroupcode !=1;
update lsusergrouprights set sdelete ='1' where displaytopic='IDS_TSK_FOLDERCREATION' and sdelete='NA' and usergroupid_usergroupcode =1;
update lsusergrouprights set sdelete ='0' where displaytopic='IDS_TSK_FOLDERCREATION' and sdelete='NA' and usergroupid_usergroupcode !=1;
update lsusergrouprights set sedit ='1' where displaytopic='IDS_TSK_FOLDERCREATIONPROTOCOL' and sedit='NA' and usergroupid_usergroupcode =1;
update lsusergrouprights set sedit ='0' where displaytopic='IDS_TSK_FOLDERCREATIONPROTOCOL' and sedit='NA' and usergroupid_usergroupcode !=1;
update lsusergrouprights set sdelete ='1' where displaytopic='IDS_TSK_FOLDERCREATIONPROTOCOL' and sdelete='NA' and usergroupid_usergroupcode =1;
update lsusergrouprights set sdelete ='0' where displaytopic='IDS_TSK_FOLDERCREATIONPROTOCOL' and sdelete='NA' and usergroupid_usergroupcode !=1;
update lsusergrouprightsmaster set sedit ='0',sdelete='0' where displaytopic='IDS_TSK_FOLDERCREATION' and sedit='NA' and sdelete='NA';
update lsusergrouprightsmaster set sedit ='0',sdelete='0' where displaytopic='IDS_TSK_FOLDERCREATIONPROTOCOL' and sedit='NA' and sdelete='NA';

CREATE TABLE IF NOT EXISTS public.lsmappedinstruments
(
    sinstrumentid character varying(50) COLLATE pg_catalog."default" NOT NULL,
    ncommunicationtype integer,
    ninstrumentstatus integer,
    ninterfacestatus integer,
    nparsertype integer,
    sinstrumentaliasname character varying(50) COLLATE pg_catalog."default",
    sinstrumentmake character varying(50) COLLATE pg_catalog."default",
    sinstrumentmodel character varying(50) COLLATE pg_catalog."default",
    sinstrumentname character varying(50) COLLATE pg_catalog."default",
    slocktype character varying(50) COLLATE pg_catalog."default",
    CONSTRAINT lsmappedinstruments_pkey PRIMARY KEY (sinstrumentid)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.lsmappedinstruments
    OWNER to postgres;
    
CREATE TABLE IF NOT EXISTS public.lsmappedfields
(
    sfieldkey character varying(12) COLLATE pg_catalog."default" NOT NULL,
    sdatatype character varying(12) COLLATE pg_catalog."default",
    selnfieldname character varying(30) COLLATE pg_catalog."default",
    sfieldname character varying(30) COLLATE pg_catalog."default",
    sfieldtype character varying(30) COLLATE pg_catalog."default",
    sformat character varying(25) COLLATE pg_catalog."default",
    sinstrumentid character varying(50) COLLATE pg_catalog."default" NOT NULL,
    slimsfieldname character varying(30) COLLATE pg_catalog."default",
    smethodname character varying(50) COLLATE pg_catalog."default" NOT NULL,
    sparsername character varying(30) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT lsmappedfields_pkey PRIMARY KEY (sfieldkey)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.lsmappedfields
    OWNER to postgres;
    
update delimiter set lssitemaster_sitecode = NULL where defaultvalue=1;
update methoddelimiter set lssitemaster_sitecode = NULL where defaultvalue=1;
  
ALTER TABLE IF Exists lscfttransaction DROP COLUMN IF EXISTS auditkey;

CREATE TABLE IF NOT EXISTS public.Lscfrtransactiononorder
(
    serialno integer NOT NULL,
    actions character varying(250) COLLATE pg_catalog."default",
    affectedclientid character varying(100) COLLATE pg_catalog."default",
    comments character varying(250) COLLATE pg_catalog."default",
    instrumentid character varying(100) COLLATE pg_catalog."default",
    lssitemaster_sitecode integer,
    lsusermaster_usercode integer,
    manipulatetype character varying(100) COLLATE pg_catalog."default",
    modifieddata character varying(100) COLLATE pg_catalog."default",
    modulename character varying(250) COLLATE pg_catalog."default",
    reason character varying(100) COLLATE pg_catalog."default",
    requestedclientid character varying(100) COLLATE pg_catalog."default",
    reviewedstatus character varying(100) COLLATE pg_catalog."default",
    systemcoments character varying(100) COLLATE pg_catalog."default",
    tablename character varying(100) COLLATE pg_catalog."default",
    transactiondate timestamp without time zone,
    batchcode character varying(100) COLLATE pg_catalog."default",
    CONSTRAINT Lscfrtransactiononorder_pkey PRIMARY KEY (serialno)
)
WITH (OIDS = FALSE) TABLESPACE pg_default;

ALTER TABLE public.Lscfrtransactiononorder OWNER to postgres;

ALTER TABLE IF Exists LSSheetCreationfiles ADD COLUMN IF NOT EXISTS fileuid varchar(100);
ALTER TABLE IF Exists LSSheetCreationfiles ADD COLUMN IF NOT EXISTS fileuri varchar(250);
ALTER TABLE IF Exists LSSheetCreationfiles ADD COLUMN IF NOT EXISTS containerstored integer default 0;

ALTER TABLE IF Exists LSSheetVersionfiles ADD COLUMN IF NOT EXISTS fileuid varchar(100);
ALTER TABLE IF Exists LSSheetVersionfiles ADD COLUMN IF NOT EXISTS fileuri varchar(250);
ALTER TABLE IF Exists LSSheetVersionfiles ADD COLUMN IF NOT EXISTS containerstored integer default 0;

ALTER TABLE IF Exists LSOrderCreationfiles ADD COLUMN IF NOT EXISTS fileuid varchar(100);
ALTER TABLE IF Exists LSOrderCreationfiles ADD COLUMN IF NOT EXISTS fileuri varchar(250);
ALTER TABLE IF Exists LSOrderCreationfiles ADD COLUMN IF NOT EXISTS containerstored integer default 0;

ALTER TABLE IF Exists LSOrderVersionfiles ADD COLUMN IF NOT EXISTS fileuid varchar(100);
ALTER TABLE IF Exists LSOrderVersionfiles ADD COLUMN IF NOT EXISTS fileuri varchar(250);
ALTER TABLE IF Exists LSOrderVersionfiles ADD COLUMN IF NOT EXISTS containerstored integer default 0;

ALTER TABLE IF Exists lssheetworkflow ADD COLUMN IF NOT EXISTS status integer;
update lssheetworkflow set status=1 where status is null;

CREATE TABLE IF NOT EXISTS public.lslogilablimsordergroup
(
    groupid character varying(250) COLLATE pg_catalog."default" NOT NULL,
    batchid character varying(250) COLLATE pg_catalog."default",
    limsprimarycode numeric(20,0),
    testcode integer,
    ntransactiontestcode integer,
    arno character varying(100) COLLATE pg_catalog."default",
    samplearno character varying(100) COLLATE pg_catalog."default",
    testname character varying(100) COLLATE pg_catalog."default",
    CONSTRAINT lslogilablimsordergroup_pkey PRIMARY KEY (groupid)
)
WITH ( OIDS = FALSE ) TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.lslogilablimsordergroup OWNER to postgres;

ALTER TABLE IF Exists DataSourceConfig ADD COLUMN IF NOT EXISTS isenableparser integer default 1;
ALTER TABLE IF Exists DataSourceConfig ADD COLUMN IF NOT EXISTS isenablereport integer default 1;

INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (121, 'IDS_SCN_GRADEMASTER', 'IDS_MDL_INVENTORY','0', '0', '0', '0', '1,1,1',80,'IDS_SCN_GRADEMASTER') ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_SCN_GRADEMASTER', 'IDS_MDL_INVENTORY', 'administrator', '1', '1', '1', '1', 1,1,'IDS_SCN_GRADEMASTER'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_SCN_GRADEMASTER' and usergroupid_usergroupcode = 1);
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (122, 'IDS_SCN_SUPPLIER', 'IDS_MDL_INVENTORY','0', '0', '0', '0', '1,1,1',81,'IDS_SCN_SUPPLIER') ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_SCN_SUPPLIER', 'IDS_MDL_INVENTORY', 'administrator', '1', '1', '1', '1', 1,1,'IDS_SCN_SUPPLIER'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_SCN_SUPPLIER' and usergroupid_usergroupcode = 1);
INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder,screenname) VALUES (123, 'IDS_SCN_MANUFACTURER', 'IDS_MDL_INVENTORY','0', '0', '0', '0', '1,1,1',82,'IDS_SCN_MANUFACTURER') ON CONFLICT(orderno)DO NOTHING;
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_SCN_MANUFACTURER', 'IDS_MDL_INVENTORY', 'administrator', '1', '1', '1', '1', 1,1,'IDS_SCN_MANUFACTURER'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_SCN_MANUFACTURER' and usergroupid_usergroupcode = 1);
ALTER TABLE IF Exists materialgrade ADD COLUMN IF NOT EXISTS sdescription character varying(100);

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'supplier_sequence' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE supplier_sequence;
   ELSIF _kind = 'S' THEN  
      -- do nothing?
   ELSE             
      -- do something!
   END IF;
END
$do$;   

CREATE TABLE IF NOT EXISTS supplier
(
    nsuppliercode integer Not NULL DEFAULT nextval('supplier_sequence'::regclass),
    scountryname character varying(100) NOT NULL,
    ssuppliername character varying(100) NOT NULL,
    saddress1 character varying(255) NOT NULL,
    saddress2 character varying(255),
    saddress3 character varying(255),
    sphoneno character varying(50),
    smobileno character varying(50) NOT NULL,
    sfaxno character varying(50),
    semail character varying(50) NOT NULL,
    ntransactionstatus integer NOT NULL DEFAULT 8,
    nsitecode integer NOT NULL DEFAULT '-1'::integer,
    nstatus integer NOT NULL DEFAULT 1,
    CONSTRAINT supplier_pkey PRIMARY KEY (nsuppliercode)
)TABLESPACE pg_default;

ALTER TABLE public.supplier OWNER to postgres;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'manufacturer_sequence' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE manufacturer_sequence;
   ELSIF _kind = 'S' THEN  
      -- do nothing?
   ELSE             
      -- do something!
   END IF;
END
$do$; 

CREATE TABLE IF NOT EXISTS manufacturer
(
    nmanufcode integer NOT NULL DEFAULT nextval('manufacturer_sequence'::regclass),
    smanufname character varying(100) NOT NULL,
    sdescription character varying(255),
    ntransactionstatus integer NOT NULL DEFAULT 1,
    nsitecode integer NOT NULL DEFAULT '-1'::integer,
    nstatus integer NOT NULL DEFAULT 1,
    CONSTRAINT manufacturer_pkey PRIMARY KEY (nmanufcode)
)TABLESPACE pg_default;

ALTER TABLE public.manufacturer OWNER to postgres;

ALTER TABLE IF Exists LSparsedparameters ADD COLUMN IF NOT EXISTS resultsvalue jsonb;

ALTER TABLE IF Exists materialinventorytransaction ADD COLUMN IF NOT EXISTS createddate timestamp without time zone;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind FROM   pg_class WHERE  relname = 'resultusedmaterial_sequence'  INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE resultusedmaterial_sequence;
   ELSIF _kind = 'S' THEN  
      -- do nothing?
   ELSE             
      -- do something!
   END IF;
END
$do$; 

CREATE TABLE IF NOT EXISTS resultusedmaterial
(
    nresultusedmaterialcode integer NOT NULL DEFAULT nextval('resultusedmaterial_sequence'::regclass),
    ordercode numeric(17,0) NOT NULL,
    transactionscreen integer NOT NULL,
    templatecode integer NOT NULL,
    nmaterialtypecode integer NOT NULL,
    nmaterialcategorycode integer NOT NULL,
    nmaterialcode integer NOT NULL,
    ninventorycode integer NOT NULL,
    jsondata character varying(255) NOT NULL,
    nstatus integer NOT NULL DEFAULT 1,
    createdbyusercode integer NOT NULL,
    createddate timestamp without time zone,
    CONSTRAINT resultusedmaterial_pkey PRIMARY KEY (nresultusedmaterialcode)
)TABLESPACE pg_default;

ALTER TABLE public.resultusedmaterial OWNER to postgres;

ALTER TABLE IF Exists resultusedmaterial ADD COLUMN IF NOT EXISTS batchid character varying(100);
ALTER TABLE IF EXISTS resultusedmaterial ADD COLUMN IF NOT EXISTS nqtyissued double precision;
ALTER TABLE IF EXISTS resultusedmaterial ADD COLUMN IF NOT EXISTS nqtyused double precision;
ALTER TABLE IF EXISTS resultusedmaterial ADD COLUMN IF NOT EXISTS nqtyleft double precision;
ALTER TABLE IF EXISTS materialinventory ADD COLUMN IF NOT EXISTS nqtynotification double precision;
ALTER TABLE IF Exists lsorderattachments ADD COLUMN IF NOT EXISTS nmaterialcode integer;

ALTER TABLE IF Exists materialinventorytransaction ADD COLUMN IF NOT EXISTS issuedbyusercode_usercode integer;
ALTER TABLE IF Exists materialinventorytransaction ADD COLUMN IF NOT EXISTS createdbyusercode_usercode integer;

ALTER TABLE IF Exists resultusedmaterial ADD COLUMN IF NOT EXISTS testcode_testcode integer;
ALTER TABLE IF Exists resultusedmaterial ADD COLUMN IF NOT EXISTS createdbyusercode_usercode integer;

ALTER TABLE resultusedmaterial DROP COLUMN IF EXISTS testcode;
ALTER TABLE resultusedmaterial DROP COLUMN IF EXISTS createdbyusercode;
ALTER TABLE materialinventorytransaction DROP COLUMN IF EXISTS createdbyusercode;
ALTER TABLE materialinventorytransaction DROP COLUMN IF EXISTS issuedbyusercode; 

DO
$do$
declare
  resultcount integer :=0;
begin
SELECT count(*) into resultcount FROM information_schema.table_constraints WHERE constraint_name='fk5wxdhe1234wr5m0ejfbh6e47' 
AND table_name='resultusedmaterial';
 IF resultcount =0 THEN
    ALTER TABLE ONLY resultusedmaterial ADD CONSTRAINT fk5wxdhe1234wr5m0ejfbh6e47 FOREIGN KEY (createdbyusercode_usercode) REFERENCES lsusermaster(usercode);
    ALTER TABLE ONLY resultusedmaterial ADD CONSTRAINT fk5wxdhe1256wr5m0ejfbh6e47 FOREIGN KEY (testcode_testcode) REFERENCES LStestmasterlocal(testcode);
  END IF;
END
$do$;

DO
$do$
declare
  inventorycount integer :=0;
begin
SELECT count(*) into inventorycount FROM information_schema.table_constraints WHERE constraint_name='fk5wxdhe678ewr5m0ejfbabc47' 
AND table_name='materialinventorytransaction';
 IF inventorycount =0 THEN
    ALTER TABLE ONLY materialinventorytransaction ADD CONSTRAINT fk5wxdhe678ewr5m0ejfbabc47 FOREIGN KEY (createdbyusercode_usercode) REFERENCES lsusermaster(usercode);
    ALTER TABLE ONLY materialinventorytransaction ADD CONSTRAINT fk5wxdhe690ewr5m0ejfbabc47 FOREIGN KEY (issuedbyusercode_usercode) REFERENCES lsusermaster(usercode);
  END IF;
END
$do$;

DO
$do$
declare
  multiusergroupcount integer :=0;
begin
SELECT count(*) into multiusergroupcount FROM information_schema.table_constraints WHERE constraint_name='fk701k777d2da33pkkl6lnamavi'
AND table_name='lsorderattachments';
 IF multiusergroupcount =0 THEN
    ALTER TABLE ONLY lsorderattachments ADD CONSTRAINT fk701k777d2da33pkkl6lnamavi FOREIGN KEY (nmaterialcode) REFERENCES material(nmaterialcode);
   END IF;
END
$do$;
update delimiter set createddate='2020-05-15 00:00:00.0000000' where createddate is Null;

update methoddelimiter set createddate='2020-05-15 00:00:00.0000000' where createddate is Null;

ALTER TABLE IF Exists lsorderattachments ADD COLUMN IF NOT EXISTS nmaterialinventorycode integer;

DO
$do$
declare
  multiusergroupcount integer :=0;
begin
SELECT count(*) into multiusergroupcount FROM information_schema.table_constraints WHERE constraint_name='fkbvkyp7mg7pxs4oxsg45bmnj6l'
AND table_name='lsorderattachments';
 IF multiusergroupcount =0 THEN
    ALTER TABLE ONLY lsorderattachments ADD CONSTRAINT fkbvkyp7mg7pxs4oxsg45bmnj6l FOREIGN KEY (nmaterialinventorycode) REFERENCES materialinventory(nmaterialinventorycode);
   END IF;
END
$do$;

ALTER TABLE IF Exists DataSourceConfig ADD COLUMN IF NOT EXISTS licencetype integer default 1;

update lsusergrouprightsmaster set screate='0' where displaytopic='IDS_TSK_FOLDERCREATION' and screate='NA';
update lsusergrouprightsmaster set screate='0' where displaytopic='IDS_TSK_FOLDERCREATIONPROTOCOL' and screate='NA';
update lsusergrouprights set screate ='1' where displaytopic='IDS_TSK_FOLDERCREATION' and screate='NA';
update lsusergrouprights set screate ='1' where displaytopic='IDS_TSK_FOLDERCREATIONPROTOCOL' and screate='NA';

Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(150,0,'IDS_MDL_INVENTORY',103,'IDS_SCN_GRADEMASTER','IDS_TSK_SAVEGRADE') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(151,0,'IDS_MDL_INVENTORY',104,'IDS_SCN_GRADEMASTER','IDS_TSK_DELETEGRADE') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(152,0,'IDS_MDL_INVENTORY',105,'IDS_SCN_SUPPLIER','IDS_TSK_SAVESUPPLIER') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(153,0,'IDS_MDL_INVENTORY',106,'IDS_SCN_SUPPLIER','IDS_TSK_DELETESUPPLIER') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(154,0,'IDS_MDL_INVENTORY',107,'IDS_SCN_MANUFACTURER','IDS_TSK_SAVEMANUFACTURER') ON CONFLICT(serialno)DO NOTHING;
Insert into lsaudittrailconfigmaster (serialno,manualaudittrail,modulename,ordersequnce,screenname,taskname) values(155,0,'IDS_MDL_INVENTORY',108,'IDS_SCN_MANUFACTURER','IDS_TSK_DELETEMANUFACTURER') ON CONFLICT(serialno)DO NOTHING;

update lsusergrouprightsmaster set sequenceorder=80 where displaytopic ='IDS_SCN_GRADEMASTER' ;
update lsusergrouprightsmaster set sequenceorder=81 where displaytopic ='IDS_SCN_SUPPLIER' ;
update lsusergrouprightsmaster set sequenceorder=82 where displaytopic ='IDS_SCN_MANUFACTURER' ;

update lsaudittrailconfigmaster set ordersequnce=103 where taskname ='IDS_TSK_SAVEGRADE' ;
update lsaudittrailconfigmaster set ordersequnce=104 where taskname ='IDS_TSK_DELETEGRADE' ;
update lsaudittrailconfigmaster set ordersequnce=105 where taskname ='IDS_TSK_SAVESUPPLIER' ;
update lsaudittrailconfigmaster set ordersequnce=106 where taskname ='IDS_TSK_DELETESUPPLIER' ;
update lsaudittrailconfigmaster set ordersequnce=107 where taskname ='IDS_TSK_SAVEMANUFACTURER' ;
update lsaudittrailconfigmaster set ordersequnce=108 where taskname ='IDS_TSK_DELETEMANUFACTURER' ;

update lsaudittrailconfiguration set ordersequnce=30 where screenname ='IDS_SCN_GRADEMASTER' ;
update lsaudittrailconfiguration set ordersequnce=31 where screenname ='IDS_SCN_SUPPLIER' ;
update lsaudittrailconfiguration set ordersequnce=32 where screenname ='IDS_SCN_MANUFACTURER' ;

delete from datatype where datatypename='Integer';

ALTER TABLE IF EXISTS lsbatchdetails ADD COLUMN IF NOT EXISTS limsprimarycode numeric(17,0);

update lsusergrouprightsmaster set sdelete='0' where sdelete='NA' and displaytopic='IDS_SCN_ORDERWORKLOW';
update lsusergrouprights set sdelete='1' where displaytopic='IDS_SCN_ORDERWORKLOW' and usergroupid_usergroupcode=1;
update lsusergrouprights set sdelete='0' where sdelete='NA' and displaytopic='IDS_SCN_ORDERWORKLOW' and usergroupid_usergroupcode!=1;
update lsusergrouprightsmaster set sdelete='0' where sdelete='NA' and displaytopic='IDS_SCN_TEMPLATEWORKFLOW';
update lsusergrouprights set sdelete='1' where displaytopic='IDS_SCN_TEMPLATEWORKFLOW' and usergroupid_usergroupcode=1;
update lsusergrouprights set sdelete='0' where sdelete='NA' and displaytopic='IDS_SCN_TEMPLATEWORKFLOW' and usergroupid_usergroupcode!=1;
update lsusergrouprightsmaster set screate='0' where screate='NA' and displaytopic='IDS_SCN_MATERIALTYPE';
update lsusergrouprights set screate='1' where displaytopic='IDS_SCN_MATERIALTYPE' and usergroupid_usergroupcode=1;
update lsusergrouprights set screate='0' where screate='NA' and displaytopic='IDS_SCN_MATERIALTYPE' and usergroupid_usergroupcode!=1;

delete from lsaudittrailconfigmaster where modulename = 'IDS_MDL_INVENTORY';
delete from lsaudittrailconfiguration where modulename = 'IDS_MDL_INVENTORY';

update lsusergrouprights set sedit = 1 where sedit = 'NA' and displaytopic = 'IDS_SCN_PROJECTMASTER' and modulename ='IDS_MDL_MASTERS';
update lsusergrouprights set sedit = 1 where sedit = 'NA' and displaytopic = 'IDS_SCN_SAMPLEMASTER' and modulename ='IDS_MDL_MASTERS';
update lsusergrouprights set sedit = 1 where sedit = 'NA' and displaytopic = 'IDS_SCN_TASKMASTER' and modulename ='IDS_MDL_MASTERS';

ALTER TABLE IF Exists lsprotocolorderstructure ADD COLUMN IF NOT EXISTS floatvalues bigint;
ALTER TABLE IF Exists lssheetorderstructure ADD COLUMN IF NOT EXISTS floatvalues bigint;

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

ALTER TABLE IF Exists LSusersteam ADD Column IF NOT EXISTS createdate DATE;
update LSusersteam set createdate=modifieddate where createdate is Null;