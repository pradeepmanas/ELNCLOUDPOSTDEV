
update lsfile set filenameuser = 'Default Template' where filecode = 1;

update lsusergrouprights set sallow='1',screate='1',sdelete='1',sedit='1' where modulename='Protocol Templates' and displaytopic ='Protocol Templates';

update lsusergrouprights set sallow='1',screate='1' where modulename='Protocol Templates' and displaytopic ='New Step' and screate='NA';

update lsusergrouprightsmaster set sallow='0',screate='0',sdelete='0',sedit='0' where modulename = 'Protocol Templates' and displaytopic ='Protocol Templates';

ALTER TABLE IF Exists LSpreferences ADD COLUMN IF NOT EXISTS valueencrypted varchar(250);
delete from LSpreferences where tasksettings ='WebParser';
insert into LSpreferences (serialno,tasksettings,valuesettings) values(1,'WebParser','InActive');
delete from LSpreferences where tasksettings ='ConCurrentUser';
insert into LSpreferences (serialno,tasksettings,valuesettings) values(2,'ConCurrentUser','InActive');
delete from LSpreferences where tasksettings ='MainFormUser';
insert into LSpreferences (serialno,tasksettings,valuesettings) values(3,'MainFormUser','InActive');

ALTER TABLE IF Exists lslogilabprotocoldetail ADD COLUMN IF NOT EXISTS lsrepositoriesdata_repositorydatacode integer;

DO
$do$
declare
  multiusergroupcount integer :=0;
begin

SELECT count(*) into multiusergroupcount FROM
information_schema.table_constraints WHERE constraint_name='fkgpc2701111y358e3flbb287pf'
AND table_name='lslogilabprotocoldetail';
 IF multiusergroupcount =0 THEN
 	ALTER TABLE ONLY lslogilabprotocoldetail ADD CONSTRAINT fkgpc2701111y358e3flbb287pf FOREIGN KEY (lsrepositoriesdata_repositorydatacode) REFERENCES lsrepositoriesdata(repositorydatacode);
   END IF;
END
$do$;  

ALTER TABLE IF Exists lslogilabprotocoldetail ADD COLUMN IF NOT EXISTS lsrepositories_repositorycode integer;

DO
$do$
declare
  multiusergroupcount integer :=0;
begin

SELECT count(*) into multiusergroupcount FROM
information_schema.table_constraints WHERE constraint_name='fkcisd747ka6hp4c2makfdui7xs'
AND table_name='lslogilabprotocoldetail';
 IF multiusergroupcount =0 THEN
 	ALTER TABLE ONLY lslogilabprotocoldetail ADD CONSTRAINT fkcisd747ka6hp4c2makfdui7xs FOREIGN KEY (lsrepositories_repositorycode) REFERENCES lsrepositories(repositorycode);
   END IF;
END
$do$;

ALTER TABLE IF Exists lslogilablimsorderdetail ADD COLUMN IF NOT EXISTS lockedusername varchar(50);

ALTER TABLE IF Exists LSreviewdetails ADD COLUMN IF NOT EXISTS auditserialno integer;
ALTER TABLE IF Exists LSreviewdetails ADD COLUMN IF NOT EXISTS modulename varchar(250);
ALTER TABLE IF Exists LSreviewdetails ADD COLUMN IF NOT EXISTS reviewvercomments varchar(250);
ALTER TABLE IF Exists LSreviewdetails ADD COLUMN IF NOT EXISTS auditusername varchar(250);
ALTER TABLE IF Exists LSreviewdetails ADD COLUMN IF NOT EXISTS action varchar(250);

INSERT into LSusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder) VALUES (65, 'Inventory', 'Base Master', '0', '0', '0', '0', '1,1,1',70) ON CONFLICT(orderno)DO NOTHING;
INSERT into LSusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder) VALUES (66, 'Add repository', 'Inventory', '0', '0', 'NA', 'NA', '1,0,0',71) ON CONFLICT(orderno)DO NOTHING;
INSERT into LSusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder) VALUES (67, 'Edit repository', 'Inventory', '0', '0', 'NA', 'NA', '1,0,0',71) ON CONFLICT(orderno)DO NOTHING;
INSERT into LSusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder) VALUES (68, 'Instrument Category', 'Parser', '0', '0', '0', '0', '1,1,1',72) ON CONFLICT(orderno)DO NOTHING;

update lsusergrouprightsmaster set sedit = '0' where modulename = 'Parser' and displaytopic != 'Parser';
update lsusergrouprights set sedit = '0' where modulename = 'Parser' and displaytopic != 'Parser' and sedit='NA';

update lsusergrouprightsmaster set modulename = 'Templates' where modulename ='Protocol Templates';
update lsusergrouprightsmaster set modulename = 'Templates' where modulename ='Sheet Templates';
update lsusergrouprights set modulename = 'Templates' where modulename ='Protocol Templates';
update lsusergrouprights set modulename = 'Templates' where modulename ='Sheet Templates';

update lsusergrouprights set sallow = '0' where modulename= 'Sheet Settings' and displaytopic = 'LIMS Test  Order';
update lsusergrouprightsmaster set sallow = '0' where modulename= 'Sheet Settings' and displaytopic = 'LIMS Test  Order';

update lsusergrouprightsmaster set displaytopic = 'Sheet Templates' where displaytopic = 'Sheet Creation';
update lsusergrouprights set displaytopic = 'Sheet Templates' where displaytopic = 'Sheet Creation';

ALTER TABLE lsnotification ALTER COLUMN CreatedTimeStamp TYPE timestamp without time zone;

INSERT into LSusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder) VALUES (69, 'Templates Shared By Me', 'Templates', '0', 'NA', 'NA', 'NA', '0,0,0',73) ON CONFLICT(orderno)DO NOTHING;
INSERT into LSusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder) VALUES (70, 'Templates Shared To Me', 'Templates', '0', 'NA', 'NA', 'NA', '0,0,0',74) ON CONFLICT(orderno)DO NOTHING;

INSERT into LSusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder)VALUES (71, 'Sheet', 'Sheet Settings', '0', 'NA', 'NA', 'NA', '0,0,0',75) ON CONFLICT(orderno)DO NOTHING;
INSERT into LSusergrouprightsmaster(orderno, displaytopic, modulename, sallow, screate,sdelete, sedit, status,sequenceorder)VALUES (72, 'Protocol', 'Sheet Settings', '0', 'NA', 'NA', 'NA', '0,0,0',76) ON CONFLICT(orderno)DO NOTHING;

Insert into LSaudittrailconfigmaster values(62,0,'Parser',59,'Delimiter','Save') ON CONFLICT(serialno)DO NOTHING;
Insert into LSaudittrailconfigmaster values(63,0,'Parser',60,'Delimiter','Edit')ON CONFLICT(serialno)DO NOTHING;
Insert into LSaudittrailconfigmaster values(64,0,'Parser',61,'Delimiter','Delete')ON CONFLICT(serialno)DO NOTHING;
Insert into LSaudittrailconfigmaster values(65,0,'Parser',62,'MethodDelimiter','Save')ON CONFLICT(serialno)DO NOTHING;
Insert into LSaudittrailconfigmaster values(66,0,'Parser',63,'MethodDelimiter','Edit')ON CONFLICT(serialno)DO NOTHING;
Insert into LSaudittrailconfigmaster values(67,0,'Parser',64,'MethodDelimiter','Delete')ON CONFLICT(serialno)DO NOTHING;
Insert into LSaudittrailconfigmaster values(68,0,'Parser',65,'MethodMaster','Save')ON CONFLICT(serialno)DO NOTHING;
Insert into LSaudittrailconfigmaster values(69,0,'Parser',66,'MethodMaster','Edit')ON CONFLICT(serialno)DO NOTHING;
Insert into LSaudittrailconfigmaster values(70,0,'Parser',67,'MethodMaster','Delete')ON CONFLICT(serialno)DO NOTHING;

insert into lsaudittrailconfigmaster values(71,0,'Base Master',68,'Repository','Save')ON CONFLICT(serialno)DO NOTHING;
insert into lsaudittrailconfigmaster values(72,0,'Base Master',69,'Repository','Edit')ON CONFLICT(serialno)DO NOTHING;
insert into lsaudittrailconfigmaster values(73,0,'Base Master',70,'Inventory','Save')ON CONFLICT(serialno)DO NOTHING;
insert into lsaudittrailconfigmaster values(74,0,'Base Master',71,'Inventory','Edit')ON CONFLICT(serialno)DO NOTHING;
insert into lsaudittrailconfigmaster values(75,0,'Base Master',72,'Inventory','Delete')ON CONFLICT(serialno)DO NOTHING;

insert into lsaudittrailconfigmaster values(76,0,'Register Orders & Execute',8,'Register Orders & Execute','Parse Data')ON CONFLICT(serialno)DO NOTHING;
insert into lsaudittrailconfigmaster values(77,0,'Protocol Order And Register',73,'Protocol Order And Register','Register Protocol')ON CONFLICT(serialno)DO NOTHING;
insert into lsaudittrailconfigmaster values(78,0,'Protocol Order And Register',74,'Protocol Order And Register','Process Protocol')ON CONFLICT(serialno)DO NOTHING;
insert into lsaudittrailconfigmaster values(79,0,'Protocol Order And Register',75,'Protocol Order And Register','Save')ON CONFLICT(serialno)DO NOTHING;
insert into lsaudittrailconfigmaster values(80,0,'Protocol Order And Register',77,'Protocol Order And Register','Complete task')ON CONFLICT(serialno)DO NOTHING;

update LSusergrouprightsmaster set sallow = '0' where displaytopic = 'Add repository' and sallow= 'NA';
update LSusergrouprightsmaster set sallow = '0' where displaytopic = 'Edit repository' and sallow= 'NA';

update LSusergrouprights set sallow = '0' where displaytopic = 'Add repository' and sallow= 'NA';
update LSusergrouprights set sallow = '0' where displaytopic = 'Edit repository' and sallow= 'NA';

update lsusergrouprightsmaster set displaytopic = 'Audit trail configuration' where displaytopic = 'Audit Trail Configuration';
update lsusergrouprightsmaster set displaytopic = 'Audit trail history' where displaytopic = 'Audit Trail History';

update lsusergrouprights set displaytopic = 'Audit trail configuration' where displaytopic = 'Audit Trail Configuration';
update lsusergrouprights set displaytopic = 'Audit trail history' where displaytopic = 'Audit Trail History';

delete from lsusergrouprightsmaster where modulename = 'Register Task Orders & Execute' and displaytopic in ('Assigned Orders','My Orders');
delete from lsusergrouprights where modulename = 'Register Task Orders & Execute' and displaytopic in ('Assigned Orders','My Orders');

ALTER TABLE IF Exists lsprotocolstep ADD COLUMN IF NOT EXISTS   timer jsonb;

ALTER TABLE IF Exists lslogilabprotocolsteps ADD COLUMN IF NOT EXISTS   timer jsonb;


DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'lssheetorderstructure_directorycode_seq' 
   INTO  _kind;

   IF NOT FOUND THEN       
      CREATE SEQUENCE lssheetorderstructure_directorycode_seq;
   ELSIF _kind = 'S' THEN  
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.lssheetorderstructure
(
    directorycode bigint NOT NULL DEFAULT nextval('lssheetorderstructure_directorycode_seq'::regclass),
    datecreated timestamp without time zone,
    datemodified timestamp without time zone,
    icon character varying(255) COLLATE pg_catalog."default",
    length integer,
    parentdircode bigint,
    path character varying(255) COLLATE pg_catalog."default",
    size integer,
    CONSTRAINT lssheetorderstructure_pkey PRIMARY KEY (directorycode)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.lssheetorderstructure
    OWNER to postgres;

ALTER TABLE IF Exists lslogilablimsorderdetail ADD COLUMN IF NOT EXISTS directorycode bigint;
ALTER TABLE IF Exists lssheetorderstructure ADD COLUMN IF NOT EXISTS directoryname character varying(255);

ALTER TABLE IF Exists elnresultdetails ADD COLUMN IF NOT EXISTS parserfieldkey integer;

ALTER TABLE IF Exists lsprotocolorderversion ADD COLUMN IF NOT EXISTS createdby integer;
    
ALTER TABLE IF Exists LSusergrouprights ADD COLUMN IF NOT EXISTS sequenceorder integer;

 
update LSusergrouprightsmaster set sequenceorder = 1 where modulename = 'Dash Board';
update LSusergrouprightsmaster set sequenceorder = 2 where modulename = 'Register Task Orders & Execute';
update LSusergrouprightsmaster set sequenceorder = 3 where modulename = 'Protocol Order And Register';
update LSusergrouprightsmaster set sequenceorder = 4 where modulename = 'Templates';
update LSusergrouprightsmaster set sequenceorder = 5 where modulename = 'Sheet Settings';
update LSusergrouprightsmaster set sequenceorder = 6 where modulename = 'Base Master';
update LSusergrouprightsmaster set sequenceorder = 7 where modulename = 'UserManagement';
update LSusergrouprightsmaster set sequenceorder = 8 where modulename = 'User Group';
update LSusergrouprightsmaster set sequenceorder = 9 where modulename = 'User Master';
update LSusergrouprightsmaster set sequenceorder = 10 where modulename = 'AuditTrail History';
update LSusergrouprightsmaster set sequenceorder = 11 where modulename = 'Reports';
update LSusergrouprightsmaster set sequenceorder = 12 where modulename = 'Parser';
update LSusergrouprightsmaster set sequenceorder = 13 where modulename = 'Inventory';

update LSusergrouprights set sequenceorder = 1 where modulename = 'Dash Board';
update LSusergrouprights set sequenceorder = 2 where modulename = 'Register Task Orders & Execute';
update LSusergrouprights set sequenceorder = 3 where modulename = 'Protocol Order And Register';
update LSusergrouprights set sequenceorder = 4 where modulename = 'Templates';
update LSusergrouprights set sequenceorder = 5 where modulename = 'Sheet Settings';
update LSusergrouprights set sequenceorder = 6 where modulename = 'Base Master';
update LSusergrouprights set sequenceorder = 7 where modulename = 'UserManagement';
update LSusergrouprights set sequenceorder = 8 where modulename = 'User Group';
update LSusergrouprights set sequenceorder = 9 where modulename = 'User Master';
update LSusergrouprights set sequenceorder = 10 where modulename = 'AuditTrail History';
update LSusergrouprights set sequenceorder = 11 where modulename = 'Reports';
update LSusergrouprights set sequenceorder = 12 where modulename = 'Parser';
update LSusergrouprights set sequenceorder = 13 where modulename = 'Inventory';

ALTER TABLE IF Exists LSaudittrailconfiguration ADD COLUMN IF NOT EXISTS ordersequnce integer;

update LSaudittrailconfigmaster set ordersequnce = 1 where modulename = 'Register Orders & Execute';
update LSaudittrailconfigmaster set ordersequnce = 2 where modulename = 'Protocol Order And Register';
update LSaudittrailconfigmaster set ordersequnce = 3 where modulename = 'Sheet Creation';
update LSaudittrailconfigmaster set ordersequnce = 4 where modulename = 'Protocols';
update LSaudittrailconfigmaster set ordersequnce = 5 where modulename = 'Sheet Setting';
update LSaudittrailconfigmaster set ordersequnce = 6 where modulename = 'Base Master';
update LSaudittrailconfigmaster set ordersequnce = 7 where modulename = 'User Management';
update LSaudittrailconfigmaster set ordersequnce = 10 where modulename = 'Audit Trail';
update LSaudittrailconfigmaster set ordersequnce = 11 where modulename = 'Reports';
update LSaudittrailconfigmaster set ordersequnce = 12 where modulename = 'Parser';

update LSaudittrailconfiguration set ordersequnce = 1 where modulename = 'Register Orders & Execute';
update LSaudittrailconfiguration set ordersequnce = 2 where modulename = 'Protocol Order And Register';
update LSaudittrailconfiguration set ordersequnce = 3 where modulename = 'Sheet Creation';
update LSaudittrailconfiguration set ordersequnce = 4 where modulename = 'Protocols';
update LSaudittrailconfiguration set ordersequnce = 5 where modulename = 'Sheet Setting';
update LSaudittrailconfiguration set ordersequnce = 6 where modulename = 'Base Master';
update LSaudittrailconfiguration set ordersequnce = 7 where modulename = 'User Management';
update LSaudittrailconfiguration set ordersequnce = 10 where modulename = 'Audit Trail';
update LSaudittrailconfiguration set ordersequnce = 11 where modulename = 'Reports';
update LSaudittrailconfiguration set ordersequnce = 12 where modulename = 'Parser';
  
CREATE TABLE IF NOT EXISTS public.cloudparserfile
(
    parserfilecode integer NOT NULL,
    extension character varying(255) COLLATE pg_catalog."default",
    fileid character varying(255) COLLATE pg_catalog."default",
    filename character varying(255) COLLATE pg_catalog."default",
    originalfilename character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT cloudparserfile_pkey PRIMARY KEY (parserfilecode)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.cloudparserfile
    OWNER to postgres;


update LSusergrouprightsmaster set displaytopic = 'Pending Work' where displaytopic = 'Pending' and sequenceorder = 3;
update LSusergrouprightsmaster set displaytopic = 'Completed Work' where displaytopic = 'Completed' and sequenceorder = 3;
update LSusergrouprights set displaytopic = 'Pending Work' where displaytopic = 'Pending' and sequenceorder = 3;
update LSusergrouprights set displaytopic = 'Completed Work' where displaytopic = 'Completed' and sequenceorder = 3;

DO
$do$
DECLARE
    counter integer := 0;
BEGIN
  select count(*) into counter from methoddelimiter where delimiterkey = 1;

   IF counter=0 THEN       -- name is free
INSERT into methoddelimiter (status, usercode, delimiterkey, parsermethodkey)
SELECT 1,1,1,1
WHERE NOT EXISTS (select * from methoddelimiter where delimiterkey = 1); 
   END IF;
END
$do$;

DO
$do$
DECLARE
    counter integer := 0;
BEGIN
  select count(*) into counter from delimiter where delimitername='None';

   IF counter=0 THEN       -- name is free
INSERT into delimiter (delimitername,actualdelimiter,status,usercode) 
SELECT 'None', 'None', 1, 1 WHERE NOT EXISTS (SELECT delimitername FROM delimiter WHERE delimitername = 'None'); 
ELSE
update delimiter set actualdelimiter = 'None' where delimitername='None' and status=1;
END IF;
END
$do$;

INSERT into LSusergrouprightsmaster (orderno, displaytopic, modulename, sallow, screate, sdelete,sedit, status,sequenceorder) SELECT 78, 'Unlock Orders', 'Base Master', '0', 'NA', 'NA', '0', '0,0,1',73 WHERE NOT EXISTS (select * from LSusergrouprightsmaster where displaytopic = 'Unlock Orders'); 

INSERT into LSusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode) SELECT 'Unlock Orders', 'Base Master', 'administrator', '1', 'NA', 'NA', '1', 1,1  WHERE NOT EXISTS (select * from LSusergrouprights where displaytopic = 'Unlock Orders' and usergroupid_usergroupcode = 1); 

INSERT into LSusergrouprightsmaster (orderno, displaytopic, modulename, sallow, screate, sdelete,sedit, status,sequenceorder) SELECT 80, 'Orders Shared By Me', 'Protocol Order And Register', '0', 'NA', 'NA', '0', '0,0,1',3 WHERE NOT EXISTS (select * from LSusergrouprightsmaster where displaytopic = 'Orders Shared By Me' and modulename = 'Protocol Order And Register') ON CONFLICT(orderno)DO NOTHING; 

INSERT into LSusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,sequenceorder) SELECT 'Orders Shared By Me', 'Protocol Order And Register', 'administrator', '1', 'NA', 'NA', '1', 1,1,3  WHERE NOT EXISTS (select * from LSusergrouprights where displaytopic = 'Orders Shared By Me' and modulename = 'Protocol Order And Register' and usergroupid_usergroupcode = 1); 

INSERT into LSusergrouprightsmaster (orderno, displaytopic, modulename, sallow, screate, sdelete,sedit, status,sequenceorder) SELECT 81, 'Orders Shared To Me', 'Protocol Order And Register', '0', 'NA', 'NA', '0', '0,0,1',3 WHERE NOT EXISTS (select * from LSusergrouprightsmaster where displaytopic = 'Orders Shared To Me' and modulename = 'Protocol Order And Register')ON CONFLICT(orderno)DO NOTHING; 

INSERT into LSusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,sequenceorder) SELECT 'Orders Shared To Me', 'Protocol Order And Register', 'administrator', '1', 'NA', 'NA', '1', 1,1,3  WHERE NOT EXISTS (select * from LSusergrouprights where displaytopic = 'Orders Shared To Me' and modulename = 'Protocol Order And Register' and usergroupid_usergroupcode = 1);

delete from LSaudittrailconfigmaster where modulename = 'Parser' ; 

ALTER TABLE IF Exists lsprotocolordersampleupdates ADD COLUMN IF NOT EXISTS unit varchar(250);

ALTER TABLE IF Exists lsprotocolsampleupdates ADD COLUMN IF NOT EXISTS unit varchar(250);

update LSusergrouprightsmaster set sedit='0' where displaytopic = 'User Group' and modulename= 'UserManagement';

ALTER TABLE LSreviewdetails ALTER COLUMN reviewcomments TYPE varchar(250);

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'lsresultfieldvalues_sno_seq' 
   INTO  _kind;

   IF NOT FOUND THEN       
      CREATE SEQUENCE lsresultfieldvalues_sno_seq;
   ELSIF _kind = 'S' THEN  
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;


CREATE TABLE IF NOT EXISTS public.lsresultfieldvalues
(
    sno integer NOT NULL DEFAULT nextval('lsresultfieldvalues_sno_seq'::regclass),
    fieldname character varying(100) COLLATE pg_catalog."default",
    fieldvalue character varying(100) COLLATE pg_catalog."default",
    resseqno integer,
    resultid integer,
    CONSTRAINT lsresultfieldvalues_pkey PRIMARY KEY (sno)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.lsresultfieldvalues
    OWNER to postgres;
    
    -- Table: public.elnfileattachments

-- DROP TABLE IF EXISTS public.elnfileattachments;

CREATE TABLE IF NOT EXISTS public.elnfileattachments
(
    attachmentcode bigint NOT NULL,
    batchcode numeric(17,0),
    createdate timestamp without time zone,
    fileextension character varying(10) COLLATE pg_catalog."default",
    fileid character varying(250) COLLATE pg_catalog."default",
    filename character varying(250) COLLATE pg_catalog."default",
    islargefile integer,
    methodkey integer,
    modifieddate timestamp without time zone,
    createby_usercode integer,
    modifiedby_usercode integer,
    CONSTRAINT elnfileattachments_pkey PRIMARY KEY (attachmentcode),
    CONSTRAINT fk1jd5ua7hdqnjmyy9lkcffmaiq FOREIGN KEY (createby_usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fknejj0mrkl68yn315an2hk10mv FOREIGN KEY (modifiedby_usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.elnfileattachments
    OWNER to postgres;


ALTER TABLE IF Exists lsprotocolmaster ADD COLUMN IF NOT EXISTS defaulttemplate integer;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'elnresultdetails_resultid_seq' 
   INTO  _kind;

   IF NOT FOUND THEN       
      CREATE SEQUENCE elnresultdetails_resultid_seq;
   ELSIF _kind = 'S' THEN  
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;
    
CREATE TABLE IF NOT EXISTS public.elnresultdetails
(
    resultid integer NOT NULL DEFAULT nextval('elnresultdetails_resultid_seq'::regclass),
    batchcode numeric(17,0) NOT NULL,
    createddate timestamp without time zone,
    filerefname character varying(255) COLLATE pg_catalog."default",
    methodkey integer,
    paramname character varying(255) COLLATE pg_catalog."default",
    parserblockkey integer,
    results character varying(255) COLLATE pg_catalog."default",
    seqnumber integer,
    usercode integer NOT NULL,
    sitecode integer NOT NULL,
    status integer,
    parserfieldkey integer,
    CONSTRAINT elnresultdetails_pkey PRIMARY KEY (resultid),
    CONSTRAINT fk3e3wxpn9p7uaf7bi93lpl65kx FOREIGN KEY (sitecode)
        REFERENCES public.lssitemaster (sitecode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkb74gvmqkxraf9nd715upaiwvn FOREIGN KEY (usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT elnresultdetails_status_check CHECK (status <= 1 AND status >= '-1'::integer)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.elnresultdetails
    OWNER to postgres;    

delete from LSpreferences where tasksettings ='ELNparser';
insert into LSpreferences (serialno,tasksettings,valuesettings) values(4,'ELNparser','1');

DO
$do$
declare
  resultvalues integer :=0;
begin

SELECT count(*) into resultvalues FROM
information_schema.table_constraints WHERE constraint_name='fk6awbvwq8363r0pdi4dmsf6g58'
AND table_name='lsresultfieldvalues';
 IF resultvalues =0 THEN
 	ALTER TABLE ONLY lsresultfieldvalues ADD CONSTRAINT fk6awbvwq8363r0pdi4dmsf6g58 FOREIGN KEY (resultid) REFERENCES elnresultdetails(resultid);
   END IF;
END
$do$;  

update lsusergrouprightsmaster set modulename='Parser', sequenceorder=12 where displaytopic='Instrument Master'and modulename='Base Master';

update lsusergrouprights set modulename='Parser',sequenceorder=12 where displaytopic='Instrument Master'and modulename='Base Master';

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

update lsusergrouprightsmaster set modulename = 'IDS_MDL_DASHBOARD' where sequenceorder = 1;
update lsusergrouprightsmaster set modulename = 'IDS_MDL_ORDERS' where sequenceorder IN(2,3);
update lsusergrouprightsmaster set modulename = 'IDS_MDL_TEMPLATES' where sequenceorder IN(4,5);
update lsusergrouprightsmaster set modulename = 'IDS_MDL_SETUP' where sequenceorder IN(7,9,8);
update lsusergrouprightsmaster set modulename = 'IDS_MDL_AUDITTRAIL' where sequenceorder=10;
update lsusergrouprightsmaster set modulename = 'IDS_MDL_MASTERS' where sequenceorder IN(6,13);
update lsusergrouprightsmaster set modulename = 'IDS_MDL_REPORTS' where sequenceorder=11;
update lsusergrouprightsmaster set modulename = 'IDS_MDL_PARSER' where sequenceorder=12;

update lsusergrouprights set modulename = 'IDS_MDL_DASHBOARD' where sequenceorder = 1;
update lsusergrouprights set modulename = 'IDS_MDL_MASTERS' where sequenceorder IN(6,13);
update lsusergrouprights set modulename = 'IDS_MDL_ORDERS' where sequenceorder IN(2,3);
update lsusergrouprights  set modulename = 'IDS_MDL_TEMPLATES' where sequenceorder IN(4,5);
update lsusergrouprights set modulename = 'IDS_MDL_SETUP' where sequenceorder IN(7,9,8);
update lsusergrouprights set modulename = 'IDS_MDL_AUDITTRAIL' where sequenceorder=10;
update lsusergrouprights set modulename = 'IDS_MDL_MASTERS' where sequenceorder IN(6,13);
update lsusergrouprights set modulename = 'IDS_MDL_REPORTS' where sequenceorder=11;
update lsusergrouprights set modulename = 'IDS_MDL_PARSER' where sequenceorder=12;

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



-- Table: public.lsmaterialcategorytype

-- DROP TABLE IF EXISTS public.lsmaterialcategorytype;

CREATE TABLE IF NOT EXISTS public.lsmaterialcategorytype
(
    materialtypecode integer NOT NULL,
    defaultstatus integer,
    sitecode integer,
    status integer,
    materialtypename character varying(100) COLLATE pg_catalog."default",
    CONSTRAINT lsmaterialcategorytype_pkey PRIMARY KEY (materialtypecode)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.lsmaterialcategorytype
    OWNER to postgres;
	
	
	drop table IF EXISTS lsmaterialcategorytype;
	
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


-- Table: public.materialcategory

-- DROP TABLE IF EXISTS public.materialcategory;

CREATE TABLE IF NOT EXISTS public.materialcategory
(
    nmaterialcatcode integer NOT NULL,
    nactivestatus smallint NOT NULL DEFAULT 1,
    nbarcode integer NOT NULL DEFAULT '-1'::integer,
    ncategorybasedflow smallint NOT NULL DEFAULT 4,
    ndefaultstatus smallint NOT NULL DEFAULT 4,
    needsectionwise smallint,
    nmaterialtypecode smallint NOT NULL,
    nsitecode smallint NOT NULL DEFAULT '-1'::integer,
    nstatus smallint NOT NULL DEFAULT 1,
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

INSERT INTO public.materialcategory(
nmaterialcatcode, nmaterialtypecode, nuserrolecode, nbarcode, ncategorybasedflow, nactivestatus, needsectionwise, smaterialcatname, sdescription, ndefaultstatus, nsitecode, nstatus)
VALUES ( 1,	1,	-1,	-1,	4,	0,	4,	'matc-01',null,4,	-1,	1) ON CONFLICT(nmaterialcatcode)DO NOTHING ;
    
INSERT INTO public.materialcategory(
nmaterialcatcode, nmaterialtypecode, nuserrolecode, nbarcode, ncategorybasedflow, nactivestatus, needsectionwise, smaterialcatname, sdescription, ndefaultstatus, nsitecode, nstatus)
VALUES (2,1,-1,-1,4,0,4,'matc-02',null,4,-1,1) ON CONFLICT(nmaterialcatcode)DO NOTHING ;

INSERT INTO public.materialcategory(
nmaterialcatcode, nmaterialtypecode, nuserrolecode, nbarcode, ncategorybasedflow, nactivestatus, needsectionwise, smaterialcatname, sdescription, ndefaultstatus, nsitecode, nstatus)
VALUES (2,1,-1,-1,4,0,4,'matc-02',null,4,-1,1) ON CONFLICT(nmaterialcatcode)DO NOTHING ;
    
INSERT INTO public.materialcategory(
nmaterialcatcode, nmaterialtypecode, nuserrolecode, nbarcode, ncategorybasedflow, nactivestatus, needsectionwise, smaterialcatname, sdescription, ndefaultstatus, nsitecode, nstatus)
VALUES (3,1,-1,	-1,	4,	0,	3,	'matc-03',null,4,-1,1) ON CONFLICT(nmaterialcatcode)DO NOTHING ;
    
INSERT INTO public.materialcategory(
nmaterialcatcode, nmaterialtypecode, nuserrolecode, nbarcode, ncategorybasedflow, nactivestatus, needsectionwise, smaterialcatname, sdescription, ndefaultstatus, nsitecode, nstatus)
VALUES (4,3,-1,	-1,	4,	0,	3,	'Matc-01',null,4,-1,1) ON CONFLICT(nmaterialcatcode)DO NOTHING ;
	
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
