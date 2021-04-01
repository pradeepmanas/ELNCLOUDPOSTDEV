--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.0
-- Dumped by pg_dump version 10.1

-- Started on 2021-02-11 12:06:29

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 1 (class 3079 OID 12387)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2988 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

--
-- TOC entry 185 (class 1259 OID 225434)
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE hibernate_sequence OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 187 (class 1259 OID 225438)
-- Name: l11instrumentmaster; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE l11instrumentmaster (
    l11instrumentid character(20) NOT NULL,
    l11createdby character(40),
    l11createdon date,
    l11instrumentaliasname character varying(200),
    l11instrumentmake character varying(200),
    l11instrumentmodel character varying(200),
    l11instrumentname character varying(200),
    l11instrumentstatus integer,
    l11interfacestatus integer,
    l11llprostatus character(20),
    l11modifiedby character(40),
    l11modifiedon date,
    l11parsertype integer,
    l11sitecode character(40)
);


ALTER TABLE l11instrumentmaster OWNER TO postgres;

--
-- TOC entry 188 (class 1259 OID 225446)
-- Name: llresultdetails; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE llresultdetails (
    resultid numeric(20,0) NOT NULL,
    aiifreport character varying(250),
    batchid text,
    fileref character varying(250),
    limsinstrumentname character varying(250),
    limsparamname character varying(250),
    limsreferencecode character varying(200),
    limstestname character varying(250),
    parsersplitsequence character varying(250),
    replicateid character varying(100),
    result text,
    sampleid character varying(100),
    splitparserseqnumber integer
);


ALTER TABLE llresultdetails OWNER TO postgres;

--
-- TOC entry 189 (class 1259 OID 225454)
-- Name: llresultdetails_resultfieldvalues; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE llresultdetails_resultfieldvalues (
    lsresultdetails_resultid numeric(20,0) NOT NULL,
    lsresultvalues_resultdetid numeric(20,0) NOT NULL,
    lsresultvalues_resseqno integer NOT NULL
);


ALTER TABLE llresultdetails_resultfieldvalues OWNER TO postgres;

--
-- TOC entry 190 (class 1259 OID 225457)
-- Name: logilablimsorder; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE logilablimsorder (
    orderid numeric(17,0) NOT NULL,
    batchid character varying(250),
    completedtimestamp timestamp without time zone,
    createdtimestamp timestamp without time zone,
    instrumentcode character varying(100),
    instrumentname character varying(100),
    methodcode character varying(100),
    orderflag character(10),
    parserflag character(10),
    replicateid character varying(100),
    sampleid character varying(250),
    testcode character varying(100)
);


ALTER TABLE logilablimsorder OWNER TO postgres;

--
-- TOC entry 191 (class 1259 OID 225465)
-- Name: lsactiveuser; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsactiveuser (
    activeusercode integer NOT NULL,
    clientname character varying(255),
    "timestamp" timestamp without time zone,
    lssitemaster_sitecode integer,
    lsusermaster_usercode integer
);


ALTER TABLE lsactiveuser OWNER TO postgres;

--
-- TOC entry 192 (class 1259 OID 225470)
-- Name: lsactivity; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsactivity (
    activitycode integer NOT NULL,
    activity character varying(255),
    activitydate timestamp without time zone,
    activityby_usercode integer
);


ALTER TABLE lsactivity OWNER TO postgres;

--
-- TOC entry 193 (class 1259 OID 225475)
-- Name: lsaudittrailconfigmaster; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsaudittrailconfigmaster (
    serialno integer NOT NULL,
    manualaudittrail integer,
    modulename character varying(255),
    ordersequnce integer NOT NULL,
    screenname character varying(255),
    taskname character varying(255)
);


ALTER TABLE lsaudittrailconfigmaster OWNER TO postgres;

--
-- TOC entry 194 (class 1259 OID 225483)
-- Name: lsaudittrailconfiguration; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsaudittrailconfiguration (
    auditcofigcode integer NOT NULL,
    manualaudittrail integer,
    modulename character varying(255),
    screenname character varying(255),
    taskname character varying(255),
    lssitemaster_sitecode integer,
    lsusermaster_usercode integer
);


ALTER TABLE lsaudittrailconfiguration OWNER TO postgres;

--
-- TOC entry 195 (class 1259 OID 225491)
-- Name: lsbatchdetails; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsbatchdetails (
    batchdetailcode bigint NOT NULL,
    batchcode numeric(17,0),
    limsorderid numeric(17,0),
    orderid numeric(17,0),
    sampleid character varying(250)
);


ALTER TABLE lsbatchdetails OWNER TO postgres;

--
-- TOC entry 196 (class 1259 OID 225496)
-- Name: lscfrreasons; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lscfrreasons (
    reasoncode integer NOT NULL,
    comments character varying(255),
    lssitemaster_sitecode integer
);


ALTER TABLE lscfrreasons OWNER TO postgres;

--
-- TOC entry 197 (class 1259 OID 225501)
-- Name: lscfrsettings; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lscfrsettings (
    cfrsettingcode integer NOT NULL,
    l49sitecode character varying(255),
    archivenodays integer,
    audittrail integer,
    cfrarchive integer,
    enablerecords integer,
    lssitemaster_sitecode integer NOT NULL
);


ALTER TABLE lscfrsettings OWNER TO postgres;

--
-- TOC entry 198 (class 1259 OID 225506)
-- Name: lscfttransaction; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lscfttransaction (
    serialno integer NOT NULL,
    actions character varying(250),
    affectedclientid character varying(100),
    comments character varying(250),
    instrumentid character varying(100),
    lssitemaster_sitecode integer,
    lsusermaster_usercode integer,
    manipulatetype character varying(100),
    modifieddata character varying(100),
    modulename character varying(250),
    reason character varying(100),
    requestedclientid character varying(100),
    reviewedstatus character varying(100),
    systemcoments character varying(100),
    tablename character varying(100),
    transactiondate timestamp without time zone
);


ALTER TABLE lscfttransaction OWNER TO postgres;

--
-- TOC entry 199 (class 1259 OID 225514)
-- Name: lsconfiguration; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsconfiguration (
    serialno integer NOT NULL,
    configactive integer,
    configgrouptype character varying(25),
    configname character varying(255),
    configpath character varying(150),
    configusername character varying(255),
    configuserpass character varying(255),
    connectionstatus integer,
    status integer
);


ALTER TABLE lsconfiguration OWNER TO postgres;

--
-- TOC entry 200 (class 1259 OID 225522)
-- Name: lscontrol; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lscontrol (
    controlcode integer NOT NULL,
    controlid character(10),
    dbversion character varying(120),
    licence bytea,
    llpro integer,
    llprodatasource character varying(120),
    llprodbname character varying(120),
    llpropassword character varying(120),
    llprousername character varying(120),
    norder integer,
    sitecode character(10)
);


ALTER TABLE lscontrol OWNER TO postgres;

--
-- TOC entry 201 (class 1259 OID 225530)
-- Name: lsdocdirectory; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsdocdirectory (
    docdirectorycode integer NOT NULL,
    createdate timestamp without time zone,
    createdby integer,
    directoryname character varying(120),
    directorytype integer,
    lssitemaster_sitecode integer,
    parentdirectory integer,
    status integer
);


ALTER TABLE lsdocdirectory OWNER TO postgres;

--
-- TOC entry 202 (class 1259 OID 225535)
-- Name: lsdocmanager; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsdocmanager (
    docmanagercode integer NOT NULL,
    status integer NOT NULL,
    createdate date,
    createdby integer NOT NULL,
    extention character varying(120),
    filehashname character varying(120),
    filename character varying(120),
    fileurl character varying(120)
);


ALTER TABLE lsdocmanager OWNER TO postgres;

--
-- TOC entry 203 (class 1259 OID 225540)
-- Name: lsdocreports; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsdocreports (
    docreportscode integer NOT NULL,
    createdby integer,
    createdate date,
    docdirectorycode integer,
    extention character varying(10),
    filehashname character varying(120),
    filename character varying(120),
    istemplate integer,
    isdraft integer,
    ismultiplesheet integer,
    isreport integer,
    lssitemaster_sitecode integer,
    sheetfilecodestring character varying(255),
    status integer,
    streamid character varying(120),
    versionno integer
);


ALTER TABLE lsdocreports OWNER TO postgres;

--
-- TOC entry 204 (class 1259 OID 225548)
-- Name: lsdocreportsversionhistory; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsdocreportsversionhistory (
    docreportsversionhistorycode integer NOT NULL,
    docreportscode integer,
    filename character varying(120),
    parentversionno integer,
    status integer,
    versionno integer
);


ALTER TABLE lsdocreportsversionhistory OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 225553)
-- Name: lsdomainmaster; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsdomainmaster (
    domaincode integer NOT NULL,
    categories character varying(255),
    createdby character varying(255),
    createdon timestamp without time zone,
    domainname character varying(255),
    domainstatus integer,
    modifiedby character varying(255),
    modifiedon timestamp without time zone,
    lssitemaster_sitecode integer
);


ALTER TABLE lsdomainmaster OWNER TO postgres;

--
-- TOC entry 206 (class 1259 OID 225561)
-- Name: lselninstfieldmapping; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lselninstfieldmapping (
    instfieldmapcode integer NOT NULL,
    fieldcode integer,
    lsmethodfields_t23_strfieldkey character varying(12)
);


ALTER TABLE lselninstfieldmapping OWNER TO postgres;

--
-- TOC entry 207 (class 1259 OID 225566)
-- Name: lselninstrumentfields; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lselninstrumentfields (
    fieldcode integer NOT NULL,
    byteallign smallint,
    bytecoltype smallint,
    bytedeleteable integer,
    bytedimension smallint,
    byteselected integer,
    bytesellloginrpt integer,
    bytesequence integer,
    bytevalue integer,
    datatype character varying(255),
    elnfieldname character varying(30),
    fieldkey character varying(255),
    fieldname character varying(255),
    format character varying(255),
    instrumentcode integer,
    instrumentid character varying(255),
    limsfieldname character varying(30),
    methodname character varying(255),
    parsername character varying(255),
    sortsequence smallint
);


ALTER TABLE lselninstrumentfields OWNER TO postgres;

--
-- TOC entry 208 (class 1259 OID 225574)
-- Name: lselninstrumentmapping; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lselninstrumentmapping (
    instrumentmapcode integer NOT NULL,
    instrumentcode integer,
    lsinstruments_t06_strinstid character varying(50)
);


ALTER TABLE lselninstrumentmapping OWNER TO postgres;

--
-- TOC entry 209 (class 1259 OID 225579)
-- Name: lselninstrumentmaster; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lselninstrumentmaster (
    instrumentcode integer NOT NULL,
    instrumentname character varying(255),
    status integer,
    lssitemaster_sitecode integer,
    modifiedby_usercode integer
);


ALTER TABLE lselninstrumentmaster OWNER TO postgres;

--
-- TOC entry 210 (class 1259 OID 225584)
-- Name: lsequipmentmap; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsequipmentmap (
    equipmentcode integer NOT NULL,
    instrumentcode integer,
    testcode integer,
    lsinstrument_instrumentcode integer
);


ALTER TABLE lsequipmentmap OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 225589)
-- Name: lsfeature; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsfeature (
    modulecode integer NOT NULL,
    modulename character varying(150),
    moduleid character(10),
    norder integer,
    senum character varying(150),
    status integer
);


ALTER TABLE lsfeature OWNER TO postgres;

--
-- TOC entry 212 (class 1259 OID 225594)
-- Name: lsfields; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsfields (
    fieldcode integer NOT NULL,
    createby integer,
    createdate date,
    level04code character varying(120),
    level04name character varying(120),
    fieldorderno integer,
    fieldtypecode integer,
    level01code character varying(50),
    isactive integer,
    level02code character varying(120),
    level03code character varying(120),
    level02name character varying(120),
    level03name character varying(120),
    siteid integer,
    level01name character varying(120)
);


ALTER TABLE lsfields OWNER TO postgres;

--
-- TOC entry 213 (class 1259 OID 225602)
-- Name: lsfile; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsfile (
    filecode integer NOT NULL,
    approved integer,
    createdate timestamp without time zone,
    extension character varying(10),
    filecontent text,
    filenameuser character varying(100),
    filenameuuid character varying(100),
    isactive integer,
    modifieddate timestamp without time zone,
    rejected integer,
    versionno integer,
    createby_usercode integer,
    lssheetworkflow_workflowcode integer,
    lssitemaster_sitecode integer,
    modifiedby_usercode integer
);


ALTER TABLE lsfile OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 225610)
-- Name: lsfilemethod; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsfilemethod (
    filemethodcode integer NOT NULL,
    filecode integer,
    instrumentid character varying(120),
    methodid character varying(120)
);


ALTER TABLE lsfilemethod OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 225615)
-- Name: lsfileparameter; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsfileparameter (
    fileparametercode integer NOT NULL,
    filecode integer,
    isactive integer,
    limsparameterid integer,
    limsparametername character varying(255),
    parameter character varying(255),
    sheetcolno integer,
    sheetid integer,
    sheetrowno integer,
    versionno integer
);


ALTER TABLE lsfileparameter OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 225623)
-- Name: lsfileparametermapping; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsfileparametermapping (
    fileparametermappingcode integer NOT NULL,
    createby integer,
    createdate date,
    filecode integer,
    fileparametercode integer,
    isactive integer,
    nparametercode integer,
    versionno integer
);


ALTER TABLE lsfileparametermapping OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 225628)
-- Name: lsfiletest; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsfiletest (
    filetestcode integer NOT NULL,
    filecode integer,
    testcode integer,
    testtype integer
);


ALTER TABLE lsfiletest OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 225633)
-- Name: lsfileversion; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsfileversion (
    fileversioncode integer NOT NULL,
    approved integer,
    createdate timestamp without time zone,
    extension character varying(10),
    filecode integer,
    filecontent character varying(255),
    filenameuser character varying(100),
    filenameuuid character varying(100),
    isactive integer,
    modifieddate timestamp without time zone,
    rejected integer,
    versionname character varying(255),
    versionno integer,
    createby_usercode integer,
    lssheetworkflow_workflowcode integer,
    lssitemaster_sitecode integer,
    modifiedby_usercode integer
);


ALTER TABLE lsfileversion OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 225641)
-- Name: lsinstrument; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsinstrument (
    instrumentcode integer NOT NULL,
    calibrationduedate character varying(255),
    calibrationstatus integer,
    defaultstatus integer,
    description character varying(255),
    expirydate character varying(255),
    installationdate character varying(255),
    instrumentcatcode integer,
    instrumentcatname character varying(255),
    instrumentid character varying(255),
    instrumentname character varying(255),
    instrumentstatus integer,
    maintenanceduedate character varying(255),
    maintenancestatus integer,
    manufcode integer,
    manufacdate character varying(255),
    manufname character varying(100),
    masterauditcode integer,
    modelnumber character varying(255),
    podate character varying(255),
    receiveddate character varying(255),
    remarks character varying(255),
    serialno character varying(255),
    servicecode integer,
    sitecode integer,
    status integer,
    suppliercode integer,
    usercode integer,
    validationdate character varying(255),
    validationstatus integer,
    windowsperiodminus integer,
    windowsperiodminusunit integer,
    windowsperiodplus integer,
    windowsperiodplusunit integer
);


ALTER TABLE lsinstrument OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 225649)
-- Name: lsinstrumentcategory; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsinstrumentcategory (
    instrumentcatcode integer NOT NULL,
    calibrationreq integer,
    categorybasedflow integer,
    componentrequired integer,
    defaultstatus integer,
    interfacetype integer,
    masterauditcode integer,
    sitecode integer,
    status integer,
    techniquecode integer,
    description character varying(255),
    erpcode character varying(255),
    instrumentcatname character varying(255)
);


ALTER TABLE lsinstrumentcategory OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 225657)
-- Name: lsinstrumentsection; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsinstrumentsection (
    instrumentsectioncode integer NOT NULL,
    defaultstatus integer,
    instrumentcode integer,
    masterauditcode integer,
    sectioncode integer,
    status integer,
    usercode integer
);


ALTER TABLE lsinstrumentsection OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 225662)
-- Name: lsinstrumenttype; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsinstrumenttype (
    instrumenttypecode integer NOT NULL,
    description text,
    instrumenttypename character varying(100),
    masterauditcode integer,
    status integer
);


ALTER TABLE lsinstrumenttype OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 225670)
-- Name: lsintrumentresultmap; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsintrumentresultmap (
    instrumentcode integer NOT NULL,
    instrumentid character varying(150),
    testcode integer
);


ALTER TABLE lsintrumentresultmap OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 225675)
-- Name: lslogilablimsorderdetail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lslogilablimsorderdetail (
    batchcode numeric(17,0) NOT NULL,
    approved integer,
    approvelstatus integer,
    batchid character varying(250),
    completedtimestamp timestamp without time zone,
    createdtimestamp timestamp without time zone,
    filetype integer,
    instrumentcode character varying(100),
    lockeduser integer,
    methodcode character varying(100),
    orderflag character(10),
    testcode integer,
    testname character varying(255),
    assignedto_usercode integer,
    lsfile_filecode integer,
    lsprojectmaster_projectcode integer,
    lssamplefile_filesamplecode integer,
    lssamplemaster_samplecode integer,
    lsusermaster_usercode integer,
    lsworkflow_workflowcode integer
);


ALTER TABLE lslogilablimsorderdetail OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 225683)
-- Name: lsmanufacturer; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsmanufacturer (
    manufcode integer NOT NULL,
    masterauditcode integer,
    sitecode integer,
    status integer,
    transactionstatus integer,
    description character varying(250),
    manufname character varying(50)
);


ALTER TABLE lsmanufacturer OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 225688)
-- Name: lsmappedtags; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsmappedtags (
    tagcode integer NOT NULL,
    nonhierarchystatus integer,
    tagid integer,
    tagname character varying(100),
    templatecode integer
);


ALTER TABLE lsmappedtags OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 225693)
-- Name: lsmappedtemplate; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsmappedtemplate (
    templatecode integer NOT NULL,
    templatename character varying(100)
);


ALTER TABLE lsmappedtemplate OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 225698)
-- Name: lsmaterial; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsmaterial (
    materialcode integer NOT NULL,
    expirypolicy integer,
    expirypolicyperiodcode integer,
    masterauditcode integer,
    materialcatcode integer,
    materialtypecode integer,
    nextvalidation integer,
    nextvalidationperiodcode integer,
    noexpiry integer,
    openexpiry integer,
    openexpiryperiodcode integer,
    qarentainstatus integer,
    reorderlevel character varying(255),
    reusable integer,
    status integer,
    storagaeconditioncode integer,
    suppliercode integer,
    totalqty integer,
    unitcode integer,
    description character varying(255),
    erpcode character varying(255),
    internalcatnum character varying(255),
    manfactcatnum character varying(255),
    materialcatname character varying(255),
    materialname character varying(255),
    materialtypename character varying(255),
    prefix character varying(255),
    remarks character varying(255)
);


ALTER TABLE lsmaterial OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 225706)
-- Name: lsmaterialcategory; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsmaterialcategory (
    materialcatcode integer NOT NULL,
    barcode integer,
    categorybasedflow integer,
    componentrequired integer,
    defaultstatus integer,
    masterauditcode integer,
    materialtypecode integer,
    qarentainstatus integer,
    sitecode integer,
    status integer,
    userrolecode integer,
    description character varying(250),
    erpcode character varying(30),
    materialcatname character varying(100)
);


ALTER TABLE lsmaterialcategory OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 225711)
-- Name: lsmaterialgrade; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsmaterialgrade (
    materialgradecode integer NOT NULL,
    defaultstatus integer,
    masterauditcode integer,
    sitecode integer,
    status integer,
    description character varying(255),
    materialgradename character varying(255)
);


ALTER TABLE lsmaterialgrade OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 225719)
-- Name: lsmaterialinventory; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsmaterialinventory (
    materialinventorycode integer NOT NULL,
    expirydate date,
    expirypolicydate date,
    manufacdate date,
    opendate date,
    openexpirydate date,
    preparedate date,
    receiveddate date,
    releasedate date,
    retestdate date,
    retireddate date,
    cost integer,
    jqprojectcode integer,
    manufcode integer,
    masterauditcode integer,
    materialcode integer,
    materialgradecode integer,
    packagetypecode integer,
    projectcode integer,
    qtyreceived integer,
    receivedqty character varying(255),
    reusablestatus integer,
    sectioncode integer,
    status integer,
    storagelocationcode integer,
    storagemethodcode integer,
    suborderno integer,
    suppliercode integer,
    taskcode integer,
    transactionstatus integer,
    transauditcode integer,
    unitcode integer,
    usagecount integer,
    batchno character varying(255),
    catalogno character varying(255),
    containerid character varying(255),
    erpcode character varying(255),
    internalprefix character varying(255),
    internalreferenceno character varying(255),
    inventorycode character varying(255),
    invoiceno character varying(255),
    lotno character varying(255),
    orderreferenceno character varying(255),
    preparedby character varying(255),
    remarks character varying(255)
);


ALTER TABLE lsmaterialinventory OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 225727)
-- Name: lsmaterialinventorytransaction; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsmaterialinventorytransaction (
    materialinventtranscode integer NOT NULL,
    opendate date,
    openexpirydate date,
    transactiondate date,
    amountleft character varying(50),
    inventorytranscode integer,
    masterauditcode integer,
    materialinventorycode integer,
    preregno integer,
    qtyissued character varying(50),
    qtyreceived character varying(50),
    resultusedmaterialcode integer,
    sectioncode integer,
    sitecode integer,
    status integer,
    transactiontype integer,
    transauditcode integer,
    arno character varying(120),
    description character varying(250)
);


ALTER TABLE lsmaterialinventorytransaction OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 225735)
-- Name: lsmaterialmap; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsmaterialmap (
    lsmaterialcode integer NOT NULL,
    materialcode integer,
    testcode integer,
    lsmaterial_materialcode integer
);


ALTER TABLE lsmaterialmap OWNER TO postgres;

--
-- TOC entry 234 (class 1259 OID 225740)
-- Name: lsmaterialsection; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsmaterialsection (
    materialsectioncode integer NOT NULL,
    materialcode integer,
    reorderlevel integer,
    sectioncode integer,
    status integer
);


ALTER TABLE lsmaterialsection OWNER TO postgres;

--
-- TOC entry 235 (class 1259 OID 225745)
-- Name: lsmaterialtype; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsmaterialtype (
    materialtypecode integer NOT NULL,
    defaultstatus integer,
    masterauditcode integer,
    sitecode integer,
    status integer,
    description character varying(250),
    materialtypename character varying(100),
    matnameids character varying(100)
);


ALTER TABLE lsmaterialtype OWNER TO postgres;

--
-- TOC entry 236 (class 1259 OID 225750)
-- Name: lsmultidocument; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsmultidocument (
    filecode integer NOT NULL,
    stepcount integer,
    createddate date,
    filename character varying(100),
    filetypecode integer,
    userid character varying(100),
    versionno integer
);


ALTER TABLE lsmultidocument OWNER TO postgres;

--
-- TOC entry 237 (class 1259 OID 225755)
-- Name: lsmultisteps; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsmultisteps (
    stepcode integer NOT NULL,
    multistepcode integer,
    stepdescription text,
    stepname character varying(100)
);


ALTER TABLE lsmultisteps OWNER TO postgres;

--
-- TOC entry 238 (class 1259 OID 225763)
-- Name: lsnotification; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsnotification (
    notificationcode numeric(17,0) NOT NULL,
    isnewnotification integer NOT NULL,
    notification character varying(120),
    createdtimestamp date,
    notificationdetils character varying(400),
    notificationpath character varying(400),
    notifationfrom_usercode integer,
    notifationto_usercode integer
);


ALTER TABLE lsnotification OWNER TO postgres;

--
-- TOC entry 239 (class 1259 OID 225771)
-- Name: lsorderattachmentfiles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsorderattachmentfiles (
    id integer NOT NULL,
    file bytea
);


ALTER TABLE lsorderattachmentfiles OWNER TO postgres;

--
-- TOC entry 240 (class 1259 OID 225779)
-- Name: lsorderattachments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsorderattachments (
    attachmentcode bigint NOT NULL,
    batchcode numeric(17,0),
    createdate timestamp without time zone,
    fileextension character varying(10),
    fileid character varying(50),
    filename character varying(250),
    islargefile integer,
    modifieddate timestamp without time zone,
    createby_usercode integer,
    modifiedby_usercode integer
);


ALTER TABLE lsorderattachments OWNER TO postgres;

--
-- TOC entry 241 (class 1259 OID 225784)
-- Name: lsordercreationfiles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsordercreationfiles (
    id bigint NOT NULL,
    content jsonb,
    contentparameter jsonb,
    contentvalues jsonb
);


ALTER TABLE lsordercreationfiles OWNER TO postgres;

--
-- TOC entry 242 (class 1259 OID 225792)
-- Name: lsorderversionfiles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsorderversionfiles (
    id bigint NOT NULL,
    content jsonb
);


ALTER TABLE lsorderversionfiles OWNER TO postgres;

--
-- TOC entry 243 (class 1259 OID 225800)
-- Name: lsorderworkflowhistory; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsorderworkflowhistory (
    historycode integer NOT NULL,
    action character varying(250),
    approvelstatus integer,
    batchcode numeric(17,0),
    comment character varying(250),
    createdate timestamp without time zone,
    createby_usercode integer,
    currentworkflow_workflowcode integer
);


ALTER TABLE lsorderworkflowhistory OWNER TO postgres;

--
-- TOC entry 244 (class 1259 OID 225808)
-- Name: lsorderworkflowtransaction; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsorderworkflowtransaction (
    workflowtransactioncode integer NOT NULL,
    approvelstatus integer,
    orderid numeric(17,0),
    usercode character varying(50),
    workflowstep integer
);


ALTER TABLE lsorderworkflowtransaction OWNER TO postgres;

--
-- TOC entry 245 (class 1259 OID 225813)
-- Name: lsparsedparameters; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsparsedparameters (
    parsedparametercode integer NOT NULL,
    analysedate character varying(255),
    batchcode numeric(17,0),
    batchid character varying(255),
    col integer,
    fieldcode character varying(255),
    orderid bigint,
    parameter character varying(255),
    resultvalue character varying(255),
    "row" integer,
    sheet integer,
    value text
);


ALTER TABLE lsparsedparameters OWNER TO postgres;

--
-- TOC entry 246 (class 1259 OID 225821)
-- Name: lspasswordhistory; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lspasswordhistory (
    userid character(20) NOT NULL,
    fifthpassword character varying(100),
    firstpassword character varying(100),
    fourthpassword character varying(100),
    secondpassword character varying(100),
    sitecode character(10),
    thirdpassword character varying(100)
);


ALTER TABLE lspasswordhistory OWNER TO postgres;

--
-- TOC entry 247 (class 1259 OID 225829)
-- Name: lspasswordhistorydetails; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lspasswordhistorydetails (
    passwordcode integer NOT NULL,
    password character varying(255),
    passwordcreatedate timestamp without time zone,
    lsusermaster_usercode integer
);


ALTER TABLE lspasswordhistorydetails OWNER TO postgres;

--
-- TOC entry 248 (class 1259 OID 225834)
-- Name: lspasswordpolicy; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lspasswordpolicy (
    policycode integer NOT NULL,
    complexpasswrd integer,
    dbbased integer,
    lockpolicy integer,
    maxpasswrdlength integer,
    mincapitalchar integer,
    minnumericchar integer,
    minpasswrdlength integer,
    minsmallchar integer,
    minspecialchar integer,
    passwordexpiry integer,
    passwordhistory integer,
    lssitemaster_sitecode integer,
    lsusermaster_usercode integer
);


ALTER TABLE lspasswordpolicy OWNER TO postgres;

--
-- TOC entry 249 (class 1259 OID 225839)
-- Name: lspreferences; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lspreferences (
    serialno integer NOT NULL,
    tasksettings character varying(100),
    valuesettings character varying(100)
);


ALTER TABLE lspreferences OWNER TO postgres;

--
-- TOC entry 250 (class 1259 OID 225844)
-- Name: lsprofilepicture; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsprofilepicture (
    id integer NOT NULL,
    image bytea,
    name character varying(255)
);


ALTER TABLE lsprofilepicture OWNER TO postgres;

--
-- TOC entry 251 (class 1259 OID 225852)
-- Name: lsprojectmaster; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsprojectmaster (
    projectcode integer NOT NULL,
    projectname character varying(100),
    status integer,
    lssitemaster_sitecode integer,
    lsusersteam_teamcode integer,
    modifiedby_usercode integer
);


ALTER TABLE lsprojectmaster OWNER TO postgres;

--
-- TOC entry 252 (class 1259 OID 225857)
-- Name: lsprotocolmaster; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsprotocolmaster (
    protocolmastercode integer NOT NULL,
    createdate timestamp without time zone,
    createdby integer,
    createdbyusername character varying(120),
    lssitemaster_sitecode integer,
    protocolmastername character varying(120),
    protocolstatus integer,
    sharewithteam integer,
    status integer
);


ALTER TABLE lsprotocolmaster OWNER TO postgres;

--
-- TOC entry 253 (class 1259 OID 225862)
-- Name: lsprotocolstep; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsprotocolstep (
    protocolstepcode integer NOT NULL,
    createdby integer,
    createdbyusername character varying(255),
    createddate timestamp without time zone,
    protocolmastercode integer,
    protocolstepname character varying(255),
    status integer,
    stepno integer
);


ALTER TABLE lsprotocolstep OWNER TO postgres;

--
-- TOC entry 254 (class 1259 OID 225870)
-- Name: lsreviewdetails; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsreviewdetails (
    serialno integer NOT NULL,
    reviewcomments character varying(100),
    reviewdate timestamp without time zone,
    reviewedby character varying(100),
    reviewedstatus character varying(100),
    reviewstatus character varying(100),
    lscfttransaction_usercode integer,
    lsreviewdetails_usercode integer,
    lssitemaster_sitecode integer,
    lsusermaster_usercode integer
);


ALTER TABLE lsreviewdetails OWNER TO postgres;

--
-- TOC entry 255 (class 1259 OID 225875)
-- Name: lssamplefile; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lssamplefile (
    filesamplecode integer NOT NULL,
    batchcode numeric(17,0),
    createby integer,
    createdate timestamp without time zone,
    filecontent text,
    modifieddate timestamp without time zone,
    processed integer,
    resetflag character varying(150),
    testid integer,
    versionno integer,
    createbyuser_usercode integer,
    modifiedby_usercode integer
);


ALTER TABLE lssamplefile OWNER TO postgres;

--
-- TOC entry 256 (class 1259 OID 225883)
-- Name: lssamplefileversion; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lssamplefileversion (
    filesamplecodeversion integer NOT NULL,
    batchcode numeric(17,0),
    createby integer,
    createdate timestamp without time zone,
    filecontent text,
    modifieddate timestamp without time zone,
    processed integer,
    resetflag character varying(150),
    testid integer,
    versionname character varying(255),
    versionno integer,
    createbyuser_usercode integer,
    filesamplecode_filesamplecode integer,
    modifiedby_usercode integer
);


ALTER TABLE lssamplefileversion OWNER TO postgres;

--
-- TOC entry 257 (class 1259 OID 225891)
-- Name: lssamplemaster; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lssamplemaster (
    samplecode integer NOT NULL,
    samplename character varying(100),
    status integer,
    lssitemaster_sitecode integer,
    modifiedby_usercode integer
);


ALTER TABLE lssamplemaster OWNER TO postgres;

--
-- TOC entry 258 (class 1259 OID 225896)
-- Name: lssampleresult; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lssampleresult (
    sampleresultcode integer NOT NULL,
    batchcode numeric(17,0),
    createby character varying(255),
    createdate timestamp without time zone,
    fileparametercode integer,
    filesamplecode integer,
    isactive integer,
    orderid numeric(17,0),
    parametercode integer,
    parametertypecode numeric(18,0),
    result character varying(100),
    testcode integer,
    lssitemaster_sitecode integer
);


ALTER TABLE lssampleresult OWNER TO postgres;

--
-- TOC entry 259 (class 1259 OID 225901)
-- Name: lssection; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lssection (
    sectioncode integer NOT NULL,
    defaultstatus integer,
    masterauditcode integer,
    sectiontypecode integer,
    sitecode integer,
    status integer,
    description character varying(250),
    erpcode character varying(120),
    sectionname character varying(120)
);


ALTER TABLE lssection OWNER TO postgres;

--
-- TOC entry 260 (class 1259 OID 225906)
-- Name: lssequencenogenerator; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lssequencenogenerator (
    settingcode integer NOT NULL,
    nformattype character varying(50),
    nseqnolength integer,
    prefix character varying(50),
    sequenceno integer,
    tablename character varying(50)
);


ALTER TABLE lssequencenogenerator OWNER TO postgres;

--
-- TOC entry 261 (class 1259 OID 225911)
-- Name: lssettings; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lssettings (
    settingid integer NOT NULL,
    settingname character varying(100),
    settingstring text,
    status integer
);


ALTER TABLE lssettings OWNER TO postgres;

--
-- TOC entry 262 (class 1259 OID 225919)
-- Name: lssheetcreationfiles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lssheetcreationfiles (
    id bigint NOT NULL,
    content jsonb
);


ALTER TABLE lssheetcreationfiles OWNER TO postgres;

--
-- TOC entry 263 (class 1259 OID 225927)
-- Name: lssheetupdates; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lssheetupdates (
    sheetcode integer NOT NULL,
    filecode integer,
    sheetcomment text,
    sheetmodifieddate timestamp without time zone,
    modifiedby_usercode integer
);


ALTER TABLE lssheetupdates OWNER TO postgres;

--
-- TOC entry 264 (class 1259 OID 225935)
-- Name: lssheetversionfiles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lssheetversionfiles (
    id bigint NOT NULL,
    content jsonb
);


ALTER TABLE lssheetversionfiles OWNER TO postgres;

--
-- TOC entry 265 (class 1259 OID 225943)
-- Name: lssheetworkflow; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lssheetworkflow (
    workflowcode integer NOT NULL,
    workflowname character varying(120),
    lssitemaster_sitecode integer
);


ALTER TABLE lssheetworkflow OWNER TO postgres;

--
-- TOC entry 266 (class 1259 OID 225948)
-- Name: lssheetworkflowgroupmap; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lssheetworkflowgroupmap (
    workflowmapid integer NOT NULL,
    workflowcode integer,
    lsusergroup_usergroupcode integer
);


ALTER TABLE lssheetworkflowgroupmap OWNER TO postgres;

--
-- TOC entry 267 (class 1259 OID 225953)
-- Name: lssheetworkflowhistory; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lssheetworkflowhistory (
    historycode integer NOT NULL,
    action character varying(250),
    approvelstatus integer,
    comment character varying(250),
    createdate timestamp without time zone,
    filecode integer,
    createby_usercode integer,
    currentworkflow_workflowcode integer
);


ALTER TABLE lssheetworkflowhistory OWNER TO postgres;

--
-- TOC entry 268 (class 1259 OID 225961)
-- Name: lssitemaster; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lssitemaster (
    sitecode integer NOT NULL,
    contactperson character varying(255),
    email character varying(255),
    faxno character varying(255),
    istatus integer,
    phoneno character varying(255),
    siteaddress text,
    sitename character varying(255)
);


ALTER TABLE lssitemaster OWNER TO postgres;

--
-- TOC entry 269 (class 1259 OID 225969)
-- Name: lstestmapping; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lstestmapping (
    testid integer NOT NULL,
    createby integer,
    createdate date,
    isactive integer,
    siteid integer,
    testcodemapping integer,
    testnamemapping character varying(100)
);


ALTER TABLE lstestmapping OWNER TO postgres;

--
-- TOC entry 270 (class 1259 OID 225974)
-- Name: lstestmaster; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lstestmaster (
    testcode integer NOT NULL,
    modifieddate character varying(255),
    accredited integer,
    checklistversioncode integer,
    ncost real,
    masterauditcode integer,
    sitecode integer,
    status integer,
    testcategorycode integer,
    transactionstatus integer,
    description character varying(255),
    testname character varying(255),
    testsynonym character varying(255)
);


ALTER TABLE lstestmaster OWNER TO postgres;

--
-- TOC entry 271 (class 1259 OID 225982)
-- Name: lstestmasterlocal; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lstestmasterlocal (
    testcode integer NOT NULL,
    status integer,
    testname character varying(100),
    lssitemaster_sitecode integer,
    modifiedby_usercode integer
);


ALTER TABLE lstestmasterlocal OWNER TO postgres;

--
-- TOC entry 272 (class 1259 OID 225987)
-- Name: lstestparameter; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lstestparameter (
    testparametercode integer NOT NULL,
    isadhocparameter integer,
    isvisible integer,
    masterauditcode integer,
    parametertypecode integer,
    roundingdigits integer,
    status integer,
    testcode integer,
    unitcode numeric(17,0),
    parametername character varying(255),
    parametersynonym character varying(255)
);


ALTER TABLE lstestparameter OWNER TO postgres;

--
-- TOC entry 273 (class 1259 OID 225995)
-- Name: lstransactionstatus; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lstransactionstatus (
    transcode integer NOT NULL,
    status integer,
    transdisplaystatus character varying(100),
    transstatus character varying(100)
);


ALTER TABLE lstransactionstatus OWNER TO postgres;

--
-- TOC entry 274 (class 1259 OID 226000)
-- Name: lsunit; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsunit (
    unitcode integer NOT NULL,
    defaultstatus integer,
    masterauditcode integer,
    sitecode integer,
    status integer,
    description text,
    unitname character varying(100)
);


ALTER TABLE lsunit OWNER TO postgres;

--
-- TOC entry 275 (class 1259 OID 226008)
-- Name: lsunmappedtags; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsunmappedtags (
    tagcode integer NOT NULL,
    nonhierarchystatus integer,
    tagid integer,
    tagname character varying(100),
    templatecode integer
);


ALTER TABLE lsunmappedtags OWNER TO postgres;

--
-- TOC entry 277 (class 1259 OID 226015)
-- Name: lsunmappedtemplate; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsunmappedtemplate (
    templatecode integer NOT NULL,
    templatename character varying(100)
);


ALTER TABLE lsunmappedtemplate OWNER TO postgres;

--
-- TOC entry 276 (class 1259 OID 226013)
-- Name: lsunmappedtemplate_templatecode_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE lsunmappedtemplate_templatecode_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE lsunmappedtemplate_templatecode_seq OWNER TO postgres;

--
-- TOC entry 2989 (class 0 OID 0)
-- Dependencies: 276
-- Name: lsunmappedtemplate_templatecode_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE lsunmappedtemplate_templatecode_seq OWNED BY lsunmappedtemplate.templatecode;


--
-- TOC entry 279 (class 1259 OID 226023)
-- Name: lsuseractions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsuseractions (
    useractioncode integer NOT NULL,
    assignedordershowall integer,
    assignedordershowcompleted integer,
    assignedordershowpending integer,
    myordershowall integer,
    myordershowcompleted integer,
    myordershowpending integer,
    sitecode integer,
    usercode integer
);


ALTER TABLE lsuseractions OWNER TO postgres;

--
-- TOC entry 278 (class 1259 OID 226021)
-- Name: lsuseractions_useractioncode_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE lsuseractions_useractioncode_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE lsuseractions_useractioncode_seq OWNER TO postgres;

--
-- TOC entry 2990 (class 0 OID 0)
-- Dependencies: 278
-- Name: lsuseractions_useractioncode_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE lsuseractions_useractioncode_seq OWNED BY lsuseractions.useractioncode;


--
-- TOC entry 280 (class 1259 OID 226029)
-- Name: lsusergroup; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsusergroup (
    usergroupcode integer NOT NULL,
    createdby character(50),
    createdon timestamp without time zone,
    lssitemaster_sitecode integer,
    modifieduser integer,
    modifiedusername character varying(255),
    modifiedby character(50),
    modifiedon timestamp without time zone,
    usergroupname character varying(100),
    usergroupstatus character(50),
    createby_usercode integer
);


ALTER TABLE lsusergroup OWNER TO postgres;

--
-- TOC entry 281 (class 1259 OID 226037)
-- Name: lsusergrouprights; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsusergrouprights (
    orderno integer NOT NULL,
    createdby character(20),
    createdon timestamp without time zone,
    displaytopic character varying(100),
    modifiedby character(20),
    modifiedon timestamp without time zone,
    modulename character varying(100),
    sallow character(10),
    screate character(10),
    sdelete character(10),
    sedit character(10),
    lssitemaster_sitecode integer,
    usergroupid_usergroupcode integer
);


ALTER TABLE lsusergrouprights OWNER TO postgres;

--
-- TOC entry 282 (class 1259 OID 226042)
-- Name: lsusergrouprightsmaster; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsusergrouprightsmaster (
    orderno integer NOT NULL,
    displaytopic character varying(100),
    modulename character varying(255),
    sallow character(10),
    screate character(10),
    sdelete character(10),
    sedit character(10),
    sequenceorder integer,
    status character(10)
);


ALTER TABLE lsusergrouprightsmaster OWNER TO postgres;

--
-- TOC entry 283 (class 1259 OID 226047)
-- Name: lsusermaster; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsusermaster (
    usercode integer NOT NULL,
    createdby character varying(255),
    createddate timestamp without time zone,
    emailid character varying(255),
    labsheet integer,
    lastloggedon timestamp without time zone,
    lockcount integer,
    modifieduser integer,
    modifiedby character varying(255),
    modifieddate timestamp without time zone,
    password character varying(255),
    passwordexpirydate timestamp without time zone,
    profileimage character varying(255),
    profileimagename character varying(255),
    userfullname character varying(255),
    username character varying(255),
    userstatus character varying(10),
    verificationcode integer,
    lssitemaster_sitecode integer,
    lsuseractions_useractioncode integer,
    lsusergroup_usergroupcode integer
);


ALTER TABLE lsusermaster OWNER TO postgres;

--
-- TOC entry 284 (class 1259 OID 226055)
-- Name: lsusersteam; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsusersteam (
    teamcode integer NOT NULL,
    createdate date,
    modifieddate date,
    status integer,
    teamname character varying(100),
    createby_usercode integer,
    lssitemaster_sitecode integer,
    modifiedby_usercode integer
);


ALTER TABLE lsusersteam OWNER TO postgres;

--
-- TOC entry 285 (class 1259 OID 226060)
-- Name: lsuserteammapping; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsuserteammapping (
    userteammapcode integer NOT NULL,
    teamcode integer,
    lsusermaster_usercode integer
);


ALTER TABLE lsuserteammapping OWNER TO postgres;

--
-- TOC entry 286 (class 1259 OID 226065)
-- Name: lsworkflow; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsworkflow (
    workflowcode integer NOT NULL,
    workflowname character varying(120),
    lssitemaster_sitecode integer
);


ALTER TABLE lsworkflow OWNER TO postgres;

--
-- TOC entry 287 (class 1259 OID 226070)
-- Name: lsworkflowgroupmapping; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE lsworkflowgroupmapping (
    workflowmapid integer NOT NULL,
    workflowcode integer,
    lsusergroup_usergroupcode integer
);


ALTER TABLE lsworkflowgroupmapping OWNER TO postgres;

--
-- TOC entry 186 (class 1259 OID 225436)
-- Name: orderdetail; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE orderdetail
    START WITH 1000000
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE orderdetail OWNER TO postgres;

--
-- TOC entry 288 (class 1259 OID 226075)
-- Name: resultfieldvalues; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE resultfieldvalues (
    resultdetid numeric(20,0) NOT NULL,
    resseqno integer NOT NULL,
    fieldname character varying(150),
    fieldvalue text
);


ALTER TABLE resultfieldvalues OWNER TO postgres;

--
-- TOC entry 289 (class 1259 OID 226083)
-- Name: t06instruments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE t06instruments (
    t06_strinstid character varying(50) NOT NULL,
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
    t06_caliblastsetby character varying(50),
    t06_calibstatus character varying(50),
    t06_calibtime character varying(6),
    t06_int_usedby smallint NOT NULL,
    t06_lastcalibon date,
    t06_strcalibtest character varying(50),
    t06_strsched1 character varying(10),
    t06_strsched2 character varying(15),
    t06_strinstname character varying(50),
    t06_striopnumber character varying(20),
    t06_strelectrodeno character varying(50),
    t06_strmake character varying(15),
    t06_strmodel character varying(15)
);


ALTER TABLE t06instruments OWNER TO postgres;

--
-- TOC entry 290 (class 1259 OID 226088)
-- Name: t23fields; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE t23fields (
    t23_strfieldkey character varying(12) NOT NULL,
    t23_bytealign smallint,
    t23_bytecoltype smallint,
    t23_bytedeletable smallint,
    t23_bytedimension smallint,
    t23_byteselected smallint,
    t23_byteselloginrpt smallint,
    t23_bytesequence smallint,
    t23_bytevalue smallint,
    t23_strdatatype character varying(12),
    t23_strelnfieldname character varying(30),
    t23_strfieldname character varying(30),
    t23_strformat character varying(25),
    t23_strinstrumentid character varying(50) NOT NULL,
    t23_strlimsfieldname character varying(30),
    t23_strmethodname character varying(50) NOT NULL,
    t23_strparsername character varying(30) NOT NULL,
    t23_intsortsequence smallint
);


ALTER TABLE t23fields OWNER TO postgres;

--
-- TOC entry 2458 (class 2604 OID 226018)
-- Name: lsunmappedtemplate templatecode; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsunmappedtemplate ALTER COLUMN templatecode SET DEFAULT nextval('lsunmappedtemplate_templatecode_seq'::regclass);


--
-- TOC entry 2459 (class 2604 OID 226026)
-- Name: lsuseractions useractioncode; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsuseractions ALTER COLUMN useractioncode SET DEFAULT nextval('lsuseractions_useractioncode_seq'::regclass);


--
-- TOC entry 2878 (class 0 OID 225438)
-- Dependencies: 187
-- Data for Name: l11instrumentmaster; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY l11instrumentmaster (l11instrumentid, l11createdby, l11createdon, l11instrumentaliasname, l11instrumentmake, l11instrumentmodel, l11instrumentname, l11instrumentstatus, l11interfacestatus, l11llprostatus, l11modifiedby, l11modifiedon, l11parsertype, l11sitecode) FROM stdin;
\.


--
-- TOC entry 2879 (class 0 OID 225446)
-- Dependencies: 188
-- Data for Name: llresultdetails; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY llresultdetails (resultid, aiifreport, batchid, fileref, limsinstrumentname, limsparamname, limsreferencecode, limstestname, parsersplitsequence, replicateid, result, sampleid, splitparserseqnumber) FROM stdin;
\.


--
-- TOC entry 2880 (class 0 OID 225454)
-- Dependencies: 189
-- Data for Name: llresultdetails_resultfieldvalues; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY llresultdetails_resultfieldvalues (lsresultdetails_resultid, lsresultvalues_resultdetid, lsresultvalues_resseqno) FROM stdin;
\.


--
-- TOC entry 2881 (class 0 OID 225457)
-- Dependencies: 190
-- Data for Name: logilablimsorder; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY logilablimsorder (orderid, batchid, completedtimestamp, createdtimestamp, instrumentcode, instrumentname, methodcode, orderflag, parserflag, replicateid, sampleid, testcode) FROM stdin;
\.


--
-- TOC entry 2882 (class 0 OID 225465)
-- Dependencies: 191
-- Data for Name: lsactiveuser; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsactiveuser (activeusercode, clientname, "timestamp", lssitemaster_sitecode, lsusermaster_usercode) FROM stdin;
\.


--
-- TOC entry 2883 (class 0 OID 225470)
-- Dependencies: 192
-- Data for Name: lsactivity; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsactivity (activitycode, activity, activitydate, activityby_usercode) FROM stdin;
\.


--
-- TOC entry 2884 (class 0 OID 225475)
-- Dependencies: 193
-- Data for Name: lsaudittrailconfigmaster; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsaudittrailconfigmaster (serialno, manualaudittrail, modulename, ordersequnce, screenname, taskname) FROM stdin;
1	1	Register Orders & Execute	1	Register Orders & Execute	Process Order
2	1	Register Orders & Execute	2	Register Orders & Execute	Register
3	1	Sheet Creation	8	Sheet Creation	Save
4	1	Base Master	10	Test Master	Save
5	1	Base Master	11	Test Master	Delete
6	1	Base Master	12	Project Master	Delete
7	1	Base Master	13	Project Master	Save
8	1	Base Master	15	Sample Master	Delete
9	1	Base Master	14	Sample Master	Save
10	1	Sheet Setting	18	Sheet Setting	Save
11	1	User Management	21	User Group	ADD NEW GROUP
12	1	User Management	22	User Group	Edit
13	1	User Management	23	User Group	Active/Deactive
14	1	User Management	24	User Master	ADD USER
15	1	User Management	25	User Master	Edit
16	1	User Management	26	User Master	Unlock
17	1	User Management	27	User Master	Active/Deactive
18	1	User Management	28	User Master	Reset Password
19	1	User Management	31	User Rights	Save
20	1	User Management	32	Project Team	Save
21	1	User Management	33	Project Team	Delete
22	1	User Management	34	Order Workflow	Save
23	1	Audit Trail	42	Audit Trail History	Review History
24	1	Audit Trail	43	Audit Trail History	Create Archive
25	1	Audit Trail	44	Audit Trail History	Open Archive
26	1	Audit Trail	45	Audit Trail History	Export
27	1	Audit Trail	46	CFR Settings	Save
28	1	Audit Trail	47	CFR Settings	Delete
30	1	Base Master	16	Instrument Master	Save
31	1	Base Master	17	Instrument Master	Delete
32	1	User Management	36	Sheet Workflow	Save
33	1	User Management	38	Domain	Save
34	1	User Management	39	Domain	Delete
35	1	User Management	40	Password Policy	Save
36	1	User Management	41	Change Password	Submit
38	1	Register Orders & Execute	3	Register Orders & Execute	Save
39	1	Register Orders & Execute	4	Register Orders & Execute	Send to Lims
40	1	Register Orders & Execute	5	Register Orders & Execute	Complete task
41	1	Sheet Creation	9	Sheet Creation	Save As
42	1	Audit Trail	48	Audit Trial Configuration	Save
43	1	Reports	50	Reports	Save As
45	1	Reports	52	Reports	New Template
46	1	Reports	53	Reports	Generate Report
47	1	Reports	54	Reports	Configure
48	1	Reports	49	Reports	Save
49	1	Reports	51	Reports	Directory Save
50	1	User Management	19	Site Master	Save
51	1	User Management	20	Site Master	Active/Deactive
52	1	Register Orders & Execute	6	Register Orders & Execute	Lock
53	1	Register Orders & Execute	7	Register Orders & Execute	Unlock
54	1	User Management	29	User Master	Import ADS
55	1	User Management	30	User Master	Connect
56	1	User Management	35	Order Workflow	Delete
57	1	User Management	37	Sheet Workflow	Delete
58	1	Protocols	55	Add Protocol	Submit
59	1	Protocols	56	New Step	Submit
60	1	Protocols	57	Share with Team	Submit
61	1	Protocols	58	Delete	Submit
\.


--
-- TOC entry 2885 (class 0 OID 225483)
-- Dependencies: 194
-- Data for Name: lsaudittrailconfiguration; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsaudittrailconfiguration (auditcofigcode, manualaudittrail, modulename, screenname, taskname, lssitemaster_sitecode, lsusermaster_usercode) FROM stdin;
\.


--
-- TOC entry 2886 (class 0 OID 225491)
-- Dependencies: 195
-- Data for Name: lsbatchdetails; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsbatchdetails (batchdetailcode, batchcode, limsorderid, orderid, sampleid) FROM stdin;
\.


--
-- TOC entry 2887 (class 0 OID 225496)
-- Dependencies: 196
-- Data for Name: lscfrreasons; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lscfrreasons (reasoncode, comments, lssitemaster_sitecode) FROM stdin;
1	Reviewed	1
2	Declained	1
\.


--
-- TOC entry 2888 (class 0 OID 225501)
-- Dependencies: 197
-- Data for Name: lscfrsettings; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lscfrsettings (cfrsettingcode, l49sitecode, archivenodays, audittrail, cfrarchive, enablerecords, lssitemaster_sitecode) FROM stdin;
\.


--
-- TOC entry 2889 (class 0 OID 225506)
-- Dependencies: 198
-- Data for Name: lscfttransaction; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lscfttransaction (serialno, actions, affectedclientid, comments, instrumentid, lssitemaster_sitecode, lsusermaster_usercode, manipulatetype, modifieddata, modulename, reason, requestedclientid, reviewedstatus, systemcoments, tablename, transactiondate) FROM stdin;
\.


--
-- TOC entry 2890 (class 0 OID 225514)
-- Dependencies: 199
-- Data for Name: lsconfiguration; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsconfiguration (serialno, configactive, configgrouptype, configname, configpath, configusername, configuserpass, connectionstatus, status) FROM stdin;
\.


--
-- TOC entry 2891 (class 0 OID 225522)
-- Dependencies: 200
-- Data for Name: lscontrol; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lscontrol (controlcode, controlid, dbversion, licence, llpro, llprodatasource, llprodbname, llpropassword, llprousername, norder, sitecode) FROM stdin;
\.


--
-- TOC entry 2892 (class 0 OID 225530)
-- Dependencies: 201
-- Data for Name: lsdocdirectory; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsdocdirectory (docdirectorycode, createdate, createdby, directoryname, directorytype, lssitemaster_sitecode, parentdirectory, status) FROM stdin;
1	\N	1	root	0	1	\N	1
2	\N	1	Generated Reports	1	1	1	1
3	\N	1	Draft Reports	1	1	1	1
4	\N	1	My Space	1	1	1	1
5	\N	1	Team Space	1	1	1	1
\.


--
-- TOC entry 2893 (class 0 OID 225535)
-- Dependencies: 202
-- Data for Name: lsdocmanager; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsdocmanager (docmanagercode, status, createdate, createdby, extention, filehashname, filename, fileurl) FROM stdin;
\.


--
-- TOC entry 2894 (class 0 OID 225540)
-- Dependencies: 203
-- Data for Name: lsdocreports; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsdocreports (docreportscode, createdby, createdate, docdirectorycode, extention, filehashname, filename, istemplate, isdraft, ismultiplesheet, isreport, lssitemaster_sitecode, sheetfilecodestring, status, streamid, versionno) FROM stdin;
\.


--
-- TOC entry 2895 (class 0 OID 225548)
-- Dependencies: 204
-- Data for Name: lsdocreportsversionhistory; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsdocreportsversionhistory (docreportsversionhistorycode, docreportscode, filename, parentversionno, status, versionno) FROM stdin;
\.


--
-- TOC entry 2896 (class 0 OID 225553)
-- Dependencies: 205
-- Data for Name: lsdomainmaster; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsdomainmaster (domaincode, categories, createdby, createdon, domainname, domainstatus, modifiedby, modifiedon, lssitemaster_sitecode) FROM stdin;
1	DB	U1	\N	ELN	1	\N	\N	1
\.


--
-- TOC entry 2897 (class 0 OID 225561)
-- Dependencies: 206
-- Data for Name: lselninstfieldmapping; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lselninstfieldmapping (instfieldmapcode, fieldcode, lsmethodfields_t23_strfieldkey) FROM stdin;
\.


--
-- TOC entry 2898 (class 0 OID 225566)
-- Dependencies: 207
-- Data for Name: lselninstrumentfields; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lselninstrumentfields (fieldcode, byteallign, bytecoltype, bytedeleteable, bytedimension, byteselected, bytesellloginrpt, bytesequence, bytevalue, datatype, elnfieldname, fieldkey, fieldname, format, instrumentcode, instrumentid, limsfieldname, methodname, parsername, sortsequence) FROM stdin;
\.


--
-- TOC entry 2899 (class 0 OID 225574)
-- Dependencies: 208
-- Data for Name: lselninstrumentmapping; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lselninstrumentmapping (instrumentmapcode, instrumentcode, lsinstruments_t06_strinstid) FROM stdin;
\.


--
-- TOC entry 2900 (class 0 OID 225579)
-- Dependencies: 209
-- Data for Name: lselninstrumentmaster; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lselninstrumentmaster (instrumentcode, instrumentname, status, lssitemaster_sitecode, modifiedby_usercode) FROM stdin;
\.


--
-- TOC entry 2901 (class 0 OID 225584)
-- Dependencies: 210
-- Data for Name: lsequipmentmap; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsequipmentmap (equipmentcode, instrumentcode, testcode, lsinstrument_instrumentcode) FROM stdin;
\.


--
-- TOC entry 2902 (class 0 OID 225589)
-- Dependencies: 211
-- Data for Name: lsfeature; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsfeature (modulecode, modulename, moduleid, norder, senum, status) FROM stdin;
\.


--
-- TOC entry 2903 (class 0 OID 225594)
-- Dependencies: 212
-- Data for Name: lsfields; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsfields (fieldcode, createby, createdate, level04code, level04name, fieldorderno, fieldtypecode, level01code, isactive, level02code, level03code, level02name, level03name, siteid, level01name) FROM stdin;
2	\N	\N	F2	Material Type	1	1	I1	1	2	1	ID_INVENT	ID_INVENT	1	ID_INVENT
3	\N	\N	F3	Material Category	2	1	I1	1	3	2	ID_INVENT	ID_INVENT	1	ID_INVENT
4	\N	\N	F4	Material Name	3	1	I1	1	4	3	ID_INVENT	ID_INVENT	1	ID_INVENT
5	\N	\N	F5	Inventory ID	4	1	I1	0	5	4	ID_INVENT	ID_INVENT	1	ID_INVENT
6	\N	\N	F6	Available Quantity	7	1	I1	0	6	6	ID_INVENT	ID_INVENT	1	ID_INVENT
7	\N	\N	F7	Used Quantity	8	1	I1	1	7	7	ID_INVENT	ID_INVENT	1	ID_INVENT
8	\N	\N	F8	Add Inventory	8	1	I1	0	8	8	ID_INVENT	ID_INVENT	1	ID_INVENT
9	\N	\N	F5	Inventory ID	4	1	I1	0	9	9	ID_INVENT	ID_INVENT	1	ID_INVENT
12	\N	\N	F12	Received Date	12	1	I1	1	12	12	ID_INVENT	ID_INVENT	1	ID_INVENT
13	\N	\N	F13	Expiry Date	13	1	I1	1	13	13	ID_INVENT	ID_INVENT	1	ID_INVENT
14	\N	\N	F14	Quantity	6	1	I1	1	14	14	ID_INVENT	ID_INVENT	1	ID_INVENT
15	\N	\N	G1	Loged User	1	3	G1	1	1	1	ID_GENERAL	ID_GENERAL	1	ID_GENERAL
16	\N	\N	G2	Current Date	2	3	G1	1	2	2	ID_GENERAL	ID_GENERAL	1	ID_GENERAL
17	\N	\N	G3	Order No	3	3	G1	1	3	3	ID_GENERAL	ID_GENERAL	1	ID_GENERAL
18	\N	\N	G4	Manual Field	4	3	G1	1	4	4	ID_GENERAL	ID_GENERAL	1	ID_GENERAL
19	\N	\N	G5	ComboBox	5	3	G1	1	5	5	ID_GENERAL	ID_GENERAL	1	ID_GENERAL
20	\N	\N	G6	Manual Date	6	3	G1	1	6	6	ID_GENERAL	ID_GENERAL	1	ID_GENERAL
21	\N	\N	F15	Inventory ID	5	1	I1	1	15	15	ID_INVENT	ID_INVENT	1	ID_INVENT
22	\N	\N	F16	Internal Referance	10	1	I1	1	16	16	ID_INVENT	ID_INVENT	1	ID_INVENT
23	\N	\N	F17	Grade	17	1	I1	1	17	17	ID_INVENT	ID_INVENT	1	ID_INVENT
24	\N	\N	F18	Received Quantity	9	1	I1	1	18	18	ID_INVENT	ID_INVENT	1	ID_INVENT
25	\N	\N	F19	Batch No	19	1	I1	1	19	19	ID_INVENT	ID_INVENT	1	ID_INVENT
26	\N	\N	F20	Lot No	20	1	I1	1	20	20	ID_INVENT	ID_INVENT	1	ID_INVENT
33	\N	\N	IC1	Is Calibrated	5	2	IC1	1	5	5	ID_EQUIP	ID_EQUIP	1	ID_EQUIP
34	\N	\N	IC2	Equipments Type	1	2	IC1	0	1	1	ID_EQUIP	ID_EQUIP	1	ID_EQUIP
35	\N	\N	IC3	Equipments Category	2	2	IC1	1	2	2	ID_EQUIP	ID_EQUIP	1	ID_EQUIP
36	\N	\N	IC4	Equipments ID	3	2	IC1	1	3	3	ID_EQUIP	ID_EQUIP	1	ID_EQUIP
38	\N	\N	IC6	Is Maintained	6	2	IC1	1	6	6	ID_EQUIP	ID_EQUIP	1	ID_EQUIP
39	\N	\N	IC7	Remarks	12	2	IC1	1	7	7	ID_EQUIP	ID_EQUIP	1	ID_EQUIP
40	\N	\N	IC8	Is Validated	8	2	IC1	1	8	8	ID_EQUIP	ID_EQUIP	1	ID_EQUIP
41	\N	\N	IC9	Calibration Due Date	9	2	IC1	1	9	9	ID_EQUIP	ID_EQUIP	1	ID_EQUIP
42	\N	\N	IC10	Maintenance Due Date	10	2	IC1	1	10	10	ID_EQUIP	ID_EQUIP	1	ID_EQUIP
43	\N	\N	IC11	Validation Due Date	11	2	IC1	1	11	11	ID_EQUIP	ID_EQUIP	1	ID_EQUIP
44	\N	\N	G7	Manual Time	7	3	G1	1	7	7	ID_GENERAL	ID_GENERAL	1	ID_GENERAL
45	\N	\N	G8	Manual Date & Time	8	3	G1	1	8	8	ID_GENERAL	ID_GENERAL	1	ID_GENERAL
46	\N	\N	G9	Mandatory Field	9	3	G1	1	9	9	ID_GENERAL	ID_GENERAL	1	ID_GENERAL
47	\N	\N	G10	Login Time	10	3	G1	1	10	10	ID_GENERAL	ID_GENERAL	1	ID_GENERAL
48	\N	\N	G11	Test Name	11	3	G1	1	11	11	ID_GENERAL	ID_GENERAL	1	ID_GENERAL
49	\N	\N	G12	Sample Name	12	3	G1	1	12	12	ID_GENERAL	ID_GENERAL	1	ID_GENERAL
50	\N	\N	G13	Numeric Field	13	3	G1	1	13	13	ID_GENERAL	ID_GENERAL	1	ID_GENERAL
51	\N	\N	G14	Long Field	14	3	G1	0	14	14	ID_GENERAL	ID_GENERAL	1	ID_GENERAL
52	\N	\N	G15	Signature	15	3	G1	1	15	15	ID_GENERAL	ID_GENERAL	1	ID_GENERAL
53	\N	\N	F9	Material Section	4	1	I1	1	4	4	ID_INVENT	ID_INVENT	1	ID_INVENT
54	\N	\N	G16	Dynamic ComboBox	16	3	G1	1	16	16	ID_GENERAL	ID_GENERAL	1	ID_GENERAL
55	\N	\N	IC12	Equipments Name	4	2	IC1	1	4	4	ID_EQUIP	ID_EQUIP	1	ID_EQUIP
56	\N	\N	G17	Project Master	17	3	G1	1	17	17	ID_GENERAL	ID_GENERAL	1	ID_GENERAL
\.


--
-- TOC entry 2904 (class 0 OID 225602)
-- Dependencies: 213
-- Data for Name: lsfile; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsfile (filecode, approved, createdate, extension, filecontent, filenameuser, filenameuuid, isactive, modifieddate, rejected, versionno, createby_usercode, lssheetworkflow_workflowcode, lssitemaster_sitecode, modifiedby_usercode) FROM stdin;
1	1	2021-02-11 12:05:29.04272	.txt	\N	\N	\N	1	2021-02-11 12:05:29.04272	0	0	1	1	1	1
\.


--
-- TOC entry 2905 (class 0 OID 225610)
-- Dependencies: 214
-- Data for Name: lsfilemethod; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsfilemethod (filemethodcode, filecode, instrumentid, methodid) FROM stdin;
\.


--
-- TOC entry 2906 (class 0 OID 225615)
-- Dependencies: 215
-- Data for Name: lsfileparameter; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsfileparameter (fileparametercode, filecode, isactive, limsparameterid, limsparametername, parameter, sheetcolno, sheetid, sheetrowno, versionno) FROM stdin;
\.


--
-- TOC entry 2907 (class 0 OID 225623)
-- Dependencies: 216
-- Data for Name: lsfileparametermapping; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsfileparametermapping (fileparametermappingcode, createby, createdate, filecode, fileparametercode, isactive, nparametercode, versionno) FROM stdin;
\.


--
-- TOC entry 2908 (class 0 OID 225628)
-- Dependencies: 217
-- Data for Name: lsfiletest; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsfiletest (filetestcode, filecode, testcode, testtype) FROM stdin;
\.


--
-- TOC entry 2909 (class 0 OID 225633)
-- Dependencies: 218
-- Data for Name: lsfileversion; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsfileversion (fileversioncode, approved, createdate, extension, filecode, filecontent, filenameuser, filenameuuid, isactive, modifieddate, rejected, versionname, versionno, createby_usercode, lssheetworkflow_workflowcode, lssitemaster_sitecode, modifiedby_usercode) FROM stdin;
\.


--
-- TOC entry 2910 (class 0 OID 225641)
-- Dependencies: 219
-- Data for Name: lsinstrument; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsinstrument (instrumentcode, calibrationduedate, calibrationstatus, defaultstatus, description, expirydate, installationdate, instrumentcatcode, instrumentcatname, instrumentid, instrumentname, instrumentstatus, maintenanceduedate, maintenancestatus, manufcode, manufacdate, manufname, masterauditcode, modelnumber, podate, receiveddate, remarks, serialno, servicecode, sitecode, status, suppliercode, usercode, validationdate, validationstatus, windowsperiodminus, windowsperiodminusunit, windowsperiodplus, windowsperiodplusunit) FROM stdin;
\.


--
-- TOC entry 2911 (class 0 OID 225649)
-- Dependencies: 220
-- Data for Name: lsinstrumentcategory; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsinstrumentcategory (instrumentcatcode, calibrationreq, categorybasedflow, componentrequired, defaultstatus, interfacetype, masterauditcode, sitecode, status, techniquecode, description, erpcode, instrumentcatname) FROM stdin;
\.


--
-- TOC entry 2912 (class 0 OID 225657)
-- Dependencies: 221
-- Data for Name: lsinstrumentsection; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsinstrumentsection (instrumentsectioncode, defaultstatus, instrumentcode, masterauditcode, sectioncode, status, usercode) FROM stdin;
\.


--
-- TOC entry 2913 (class 0 OID 225662)
-- Dependencies: 222
-- Data for Name: lsinstrumenttype; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsinstrumenttype (instrumenttypecode, description, instrumenttypename, masterauditcode, status) FROM stdin;
\.


--
-- TOC entry 2914 (class 0 OID 225670)
-- Dependencies: 223
-- Data for Name: lsintrumentresultmap; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsintrumentresultmap (instrumentcode, instrumentid, testcode) FROM stdin;
\.


--
-- TOC entry 2915 (class 0 OID 225675)
-- Dependencies: 224
-- Data for Name: lslogilablimsorderdetail; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lslogilablimsorderdetail (batchcode, approved, approvelstatus, batchid, completedtimestamp, createdtimestamp, filetype, instrumentcode, lockeduser, methodcode, orderflag, testcode, testname, assignedto_usercode, lsfile_filecode, lsprojectmaster_projectcode, lssamplefile_filesamplecode, lssamplemaster_samplecode, lsusermaster_usercode, lsworkflow_workflowcode) FROM stdin;
\.


--
-- TOC entry 2916 (class 0 OID 225683)
-- Dependencies: 225
-- Data for Name: lsmanufacturer; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsmanufacturer (manufcode, masterauditcode, sitecode, status, transactionstatus, description, manufname) FROM stdin;
\.


--
-- TOC entry 2917 (class 0 OID 225688)
-- Dependencies: 226
-- Data for Name: lsmappedtags; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsmappedtags (tagcode, nonhierarchystatus, tagid, tagname, templatecode) FROM stdin;
\.


--
-- TOC entry 2918 (class 0 OID 225693)
-- Dependencies: 227
-- Data for Name: lsmappedtemplate; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsmappedtemplate (templatecode, templatename) FROM stdin;
\.


--
-- TOC entry 2919 (class 0 OID 225698)
-- Dependencies: 228
-- Data for Name: lsmaterial; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsmaterial (materialcode, expirypolicy, expirypolicyperiodcode, masterauditcode, materialcatcode, materialtypecode, nextvalidation, nextvalidationperiodcode, noexpiry, openexpiry, openexpiryperiodcode, qarentainstatus, reorderlevel, reusable, status, storagaeconditioncode, suppliercode, totalqty, unitcode, description, erpcode, internalcatnum, manfactcatnum, materialcatname, materialname, materialtypename, prefix, remarks) FROM stdin;
\.


--
-- TOC entry 2920 (class 0 OID 225706)
-- Dependencies: 229
-- Data for Name: lsmaterialcategory; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsmaterialcategory (materialcatcode, barcode, categorybasedflow, componentrequired, defaultstatus, masterauditcode, materialtypecode, qarentainstatus, sitecode, status, userrolecode, description, erpcode, materialcatname) FROM stdin;
\.


--
-- TOC entry 2921 (class 0 OID 225711)
-- Dependencies: 230
-- Data for Name: lsmaterialgrade; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsmaterialgrade (materialgradecode, defaultstatus, masterauditcode, sitecode, status, description, materialgradename) FROM stdin;
\.


--
-- TOC entry 2922 (class 0 OID 225719)
-- Dependencies: 231
-- Data for Name: lsmaterialinventory; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsmaterialinventory (materialinventorycode, expirydate, expirypolicydate, manufacdate, opendate, openexpirydate, preparedate, receiveddate, releasedate, retestdate, retireddate, cost, jqprojectcode, manufcode, masterauditcode, materialcode, materialgradecode, packagetypecode, projectcode, qtyreceived, receivedqty, reusablestatus, sectioncode, status, storagelocationcode, storagemethodcode, suborderno, suppliercode, taskcode, transactionstatus, transauditcode, unitcode, usagecount, batchno, catalogno, containerid, erpcode, internalprefix, internalreferenceno, inventorycode, invoiceno, lotno, orderreferenceno, preparedby, remarks) FROM stdin;
\.


--
-- TOC entry 2923 (class 0 OID 225727)
-- Dependencies: 232
-- Data for Name: lsmaterialinventorytransaction; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsmaterialinventorytransaction (materialinventtranscode, opendate, openexpirydate, transactiondate, amountleft, inventorytranscode, masterauditcode, materialinventorycode, preregno, qtyissued, qtyreceived, resultusedmaterialcode, sectioncode, sitecode, status, transactiontype, transauditcode, arno, description) FROM stdin;
\.


--
-- TOC entry 2924 (class 0 OID 225735)
-- Dependencies: 233
-- Data for Name: lsmaterialmap; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsmaterialmap (lsmaterialcode, materialcode, testcode, lsmaterial_materialcode) FROM stdin;
\.


--
-- TOC entry 2925 (class 0 OID 225740)
-- Dependencies: 234
-- Data for Name: lsmaterialsection; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsmaterialsection (materialsectioncode, materialcode, reorderlevel, sectioncode, status) FROM stdin;
\.


--
-- TOC entry 2926 (class 0 OID 225745)
-- Dependencies: 235
-- Data for Name: lsmaterialtype; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsmaterialtype (materialtypecode, defaultstatus, masterauditcode, sitecode, status, description, materialtypename, matnameids) FROM stdin;
\.


--
-- TOC entry 2927 (class 0 OID 225750)
-- Dependencies: 236
-- Data for Name: lsmultidocument; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsmultidocument (filecode, stepcount, createddate, filename, filetypecode, userid, versionno) FROM stdin;
\.


--
-- TOC entry 2928 (class 0 OID 225755)
-- Dependencies: 237
-- Data for Name: lsmultisteps; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsmultisteps (stepcode, multistepcode, stepdescription, stepname) FROM stdin;
\.


--
-- TOC entry 2929 (class 0 OID 225763)
-- Dependencies: 238
-- Data for Name: lsnotification; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsnotification (notificationcode, isnewnotification, notification, createdtimestamp, notificationdetils, notificationpath, notifationfrom_usercode, notifationto_usercode) FROM stdin;
\.


--
-- TOC entry 2930 (class 0 OID 225771)
-- Dependencies: 239
-- Data for Name: lsorderattachmentfiles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsorderattachmentfiles (id, file) FROM stdin;
\.


--
-- TOC entry 2931 (class 0 OID 225779)
-- Dependencies: 240
-- Data for Name: lsorderattachments; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsorderattachments (attachmentcode, batchcode, createdate, fileextension, fileid, filename, islargefile, modifieddate, createby_usercode, modifiedby_usercode) FROM stdin;
\.


--
-- TOC entry 2932 (class 0 OID 225784)
-- Dependencies: 241
-- Data for Name: lsordercreationfiles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsordercreationfiles (id, content, contentparameter, contentvalues) FROM stdin;
\.


--
-- TOC entry 2933 (class 0 OID 225792)
-- Dependencies: 242
-- Data for Name: lsorderversionfiles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsorderversionfiles (id, content) FROM stdin;
\.


--
-- TOC entry 2934 (class 0 OID 225800)
-- Dependencies: 243
-- Data for Name: lsorderworkflowhistory; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsorderworkflowhistory (historycode, action, approvelstatus, batchcode, comment, createdate, createby_usercode, currentworkflow_workflowcode) FROM stdin;
\.


--
-- TOC entry 2935 (class 0 OID 225808)
-- Dependencies: 244
-- Data for Name: lsorderworkflowtransaction; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsorderworkflowtransaction (workflowtransactioncode, approvelstatus, orderid, usercode, workflowstep) FROM stdin;
\.


--
-- TOC entry 2936 (class 0 OID 225813)
-- Dependencies: 245
-- Data for Name: lsparsedparameters; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsparsedparameters (parsedparametercode, analysedate, batchcode, batchid, col, fieldcode, orderid, parameter, resultvalue, "row", sheet, value) FROM stdin;
\.


--
-- TOC entry 2937 (class 0 OID 225821)
-- Dependencies: 246
-- Data for Name: lspasswordhistory; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lspasswordhistory (userid, fifthpassword, firstpassword, fourthpassword, secondpassword, sitecode, thirdpassword) FROM stdin;
\.


--
-- TOC entry 2938 (class 0 OID 225829)
-- Dependencies: 247
-- Data for Name: lspasswordhistorydetails; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lspasswordhistorydetails (passwordcode, password, passwordcreatedate, lsusermaster_usercode) FROM stdin;
\.


--
-- TOC entry 2939 (class 0 OID 225834)
-- Dependencies: 248
-- Data for Name: lspasswordpolicy; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lspasswordpolicy (policycode, complexpasswrd, dbbased, lockpolicy, maxpasswrdlength, mincapitalchar, minnumericchar, minpasswrdlength, minsmallchar, minspecialchar, passwordexpiry, passwordhistory, lssitemaster_sitecode, lsusermaster_usercode) FROM stdin;
1	0	\N	5	10	0	0	4	0	0	90	5	1	\N
\.


--
-- TOC entry 2940 (class 0 OID 225839)
-- Dependencies: 249
-- Data for Name: lspreferences; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lspreferences (serialno, tasksettings, valuesettings) FROM stdin;
\.


--
-- TOC entry 2941 (class 0 OID 225844)
-- Dependencies: 250
-- Data for Name: lsprofilepicture; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsprofilepicture (id, image, name) FROM stdin;
\.


--
-- TOC entry 2942 (class 0 OID 225852)
-- Dependencies: 251
-- Data for Name: lsprojectmaster; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsprojectmaster (projectcode, projectname, status, lssitemaster_sitecode, lsusersteam_teamcode, modifiedby_usercode) FROM stdin;
\.


--
-- TOC entry 2943 (class 0 OID 225857)
-- Dependencies: 252
-- Data for Name: lsprotocolmaster; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsprotocolmaster (protocolmastercode, createdate, createdby, createdbyusername, lssitemaster_sitecode, protocolmastername, protocolstatus, sharewithteam, status) FROM stdin;
\.


--
-- TOC entry 2944 (class 0 OID 225862)
-- Dependencies: 253
-- Data for Name: lsprotocolstep; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsprotocolstep (protocolstepcode, createdby, createdbyusername, createddate, protocolmastercode, protocolstepname, status, stepno) FROM stdin;
\.


--
-- TOC entry 2945 (class 0 OID 225870)
-- Dependencies: 254
-- Data for Name: lsreviewdetails; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsreviewdetails (serialno, reviewcomments, reviewdate, reviewedby, reviewedstatus, reviewstatus, lscfttransaction_usercode, lsreviewdetails_usercode, lssitemaster_sitecode, lsusermaster_usercode) FROM stdin;
\.


--
-- TOC entry 2946 (class 0 OID 225875)
-- Dependencies: 255
-- Data for Name: lssamplefile; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lssamplefile (filesamplecode, batchcode, createby, createdate, filecontent, modifieddate, processed, resetflag, testid, versionno, createbyuser_usercode, modifiedby_usercode) FROM stdin;
\.


--
-- TOC entry 2947 (class 0 OID 225883)
-- Dependencies: 256
-- Data for Name: lssamplefileversion; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lssamplefileversion (filesamplecodeversion, batchcode, createby, createdate, filecontent, modifieddate, processed, resetflag, testid, versionname, versionno, createbyuser_usercode, filesamplecode_filesamplecode, modifiedby_usercode) FROM stdin;
\.


--
-- TOC entry 2948 (class 0 OID 225891)
-- Dependencies: 257
-- Data for Name: lssamplemaster; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lssamplemaster (samplecode, samplename, status, lssitemaster_sitecode, modifiedby_usercode) FROM stdin;
\.


--
-- TOC entry 2949 (class 0 OID 225896)
-- Dependencies: 258
-- Data for Name: lssampleresult; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lssampleresult (sampleresultcode, batchcode, createby, createdate, fileparametercode, filesamplecode, isactive, orderid, parametercode, parametertypecode, result, testcode, lssitemaster_sitecode) FROM stdin;
\.


--
-- TOC entry 2950 (class 0 OID 225901)
-- Dependencies: 259
-- Data for Name: lssection; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lssection (sectioncode, defaultstatus, masterauditcode, sectiontypecode, sitecode, status, description, erpcode, sectionname) FROM stdin;
\.


--
-- TOC entry 2951 (class 0 OID 225906)
-- Dependencies: 260
-- Data for Name: lssequencenogenerator; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lssequencenogenerator (settingcode, nformattype, nseqnolength, prefix, sequenceno, tablename) FROM stdin;
\.


--
-- TOC entry 2952 (class 0 OID 225911)
-- Dependencies: 261
-- Data for Name: lssettings; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lssettings (settingid, settingname, settingstring, status) FROM stdin;
\.


--
-- TOC entry 2953 (class 0 OID 225919)
-- Dependencies: 262
-- Data for Name: lssheetcreationfiles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lssheetcreationfiles (id, content) FROM stdin;
\.


--
-- TOC entry 2954 (class 0 OID 225927)
-- Dependencies: 263
-- Data for Name: lssheetupdates; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lssheetupdates (sheetcode, filecode, sheetcomment, sheetmodifieddate, modifiedby_usercode) FROM stdin;
\.


--
-- TOC entry 2955 (class 0 OID 225935)
-- Dependencies: 264
-- Data for Name: lssheetversionfiles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lssheetversionfiles (id, content) FROM stdin;
\.


--
-- TOC entry 2956 (class 0 OID 225943)
-- Dependencies: 265
-- Data for Name: lssheetworkflow; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lssheetworkflow (workflowcode, workflowname, lssitemaster_sitecode) FROM stdin;
1	New	\N
\.


--
-- TOC entry 2957 (class 0 OID 225948)
-- Dependencies: 266
-- Data for Name: lssheetworkflowgroupmap; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lssheetworkflowgroupmap (workflowmapid, workflowcode, lsusergroup_usergroupcode) FROM stdin;
\.


--
-- TOC entry 2958 (class 0 OID 225953)
-- Dependencies: 267
-- Data for Name: lssheetworkflowhistory; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lssheetworkflowhistory (historycode, action, approvelstatus, comment, createdate, filecode, createby_usercode, currentworkflow_workflowcode) FROM stdin;
\.


--
-- TOC entry 2959 (class 0 OID 225961)
-- Dependencies: 268
-- Data for Name: lssitemaster; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lssitemaster (sitecode, contactperson, email, faxno, istatus, phoneno, siteaddress, sitename) FROM stdin;
1	\N	\N	\N	1	\N	\N	Default
\.


--
-- TOC entry 2960 (class 0 OID 225969)
-- Dependencies: 269
-- Data for Name: lstestmapping; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lstestmapping (testid, createby, createdate, isactive, siteid, testcodemapping, testnamemapping) FROM stdin;
\.


--
-- TOC entry 2961 (class 0 OID 225974)
-- Dependencies: 270
-- Data for Name: lstestmaster; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lstestmaster (testcode, modifieddate, accredited, checklistversioncode, ncost, masterauditcode, sitecode, status, testcategorycode, transactionstatus, description, testname, testsynonym) FROM stdin;
\.


--
-- TOC entry 2962 (class 0 OID 225982)
-- Dependencies: 271
-- Data for Name: lstestmasterlocal; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lstestmasterlocal (testcode, status, testname, lssitemaster_sitecode, modifiedby_usercode) FROM stdin;
\.


--
-- TOC entry 2963 (class 0 OID 225987)
-- Dependencies: 272
-- Data for Name: lstestparameter; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lstestparameter (testparametercode, isadhocparameter, isvisible, masterauditcode, parametertypecode, roundingdigits, status, testcode, unitcode, parametername, parametersynonym) FROM stdin;
\.


--
-- TOC entry 2964 (class 0 OID 225995)
-- Dependencies: 273
-- Data for Name: lstransactionstatus; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lstransactionstatus (transcode, status, transdisplaystatus, transstatus) FROM stdin;
\.


--
-- TOC entry 2965 (class 0 OID 226000)
-- Dependencies: 274
-- Data for Name: lsunit; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsunit (unitcode, defaultstatus, masterauditcode, sitecode, status, description, unitname) FROM stdin;
\.


--
-- TOC entry 2966 (class 0 OID 226008)
-- Dependencies: 275
-- Data for Name: lsunmappedtags; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsunmappedtags (tagcode, nonhierarchystatus, tagid, tagname, templatecode) FROM stdin;
\.


--
-- TOC entry 2968 (class 0 OID 226015)
-- Dependencies: 277
-- Data for Name: lsunmappedtemplate; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsunmappedtemplate (templatecode, templatename) FROM stdin;
\.


--
-- TOC entry 2970 (class 0 OID 226023)
-- Dependencies: 279
-- Data for Name: lsuseractions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsuseractions (useractioncode, assignedordershowall, assignedordershowcompleted, assignedordershowpending, myordershowall, myordershowcompleted, myordershowpending, sitecode, usercode) FROM stdin;
\.


--
-- TOC entry 2971 (class 0 OID 226029)
-- Dependencies: 280
-- Data for Name: lsusergroup; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsusergroup (usergroupcode, createdby, createdon, lssitemaster_sitecode, modifieduser, modifiedusername, modifiedby, modifiedon, usergroupname, usergroupstatus, createby_usercode) FROM stdin;
1	administrator                                     	2021-02-11 12:05:29.027604	1	\N	\N	administrator                                     	2021-02-11 12:05:29.027604	Administrator	A                                                 	\N
\.


--
-- TOC entry 2972 (class 0 OID 226037)
-- Dependencies: 281
-- Data for Name: lsusergrouprights; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsusergrouprights (orderno, createdby, createdon, displaytopic, modifiedby, modifiedon, modulename, sallow, screate, sdelete, sedit, lssitemaster_sitecode, usergroupid_usergroupcode) FROM stdin;
1	administrator       	2020-02-21 00:00:00	Parameter OverView	\N	\N	Dash Board	1         	NA        	NA        	NA        	1	1
2	administrator       	2020-02-21 00:00:00	Order Overview	\N	\N	Dash Board	1         	NA        	NA        	NA        	1	1
3	administrator       	2020-02-21 00:00:00	Activities	\N	\N	Dash Board	1         	NA        	NA        	NA        	1	1
4	administrator       	2020-02-21 00:00:00	Parameter Usage	\N	\N	Dash Board	1         	NA        	NA        	NA        	1	1
5	administrator       	2020-02-21 00:00:00	Pending Work	\N	\N	Register Task Orders & Execute	1         	1         	NA        	NA        	1	1
6	administrator       	2020-02-21 00:00:00	Completed Work	\N	\N	Register Task Orders & Execute	1         	1         	NA        	NA        	1	1
7	administrator       	2020-02-21 00:00:00	LIMS Task Order	\N	\N	Register Task Orders & Execute	1         	1         	NA        	NA        	1	1
8	administrator       	2020-02-21 00:00:00	ELN Task Order	\N	\N	Register Task Orders & Execute	1         	1         	NA        	NA        	1	1
9	administrator       	2020-02-21 00:00:00	Research Activity Order	\N	\N	Register Task Orders & Execute	1         	1         	NA        	NA        	1	1
10	administrator       	2020-02-21 00:00:00	Manage Excel	\N	\N	Register Task Orders & Execute	1         	1         	NA        	NA        	1	1
11	administrator       	2020-02-21 00:00:00	Sheet Evaluation	\N	\N	Register Task Orders & Execute	1         	1         	NA        	NA        	1	1
12	administrator       	2020-02-21 00:00:00	Sheet Creation	\N	\N	Sheet Creation	1         	1         	NA        	1         	1	1
13	administrator       	2020-02-21 00:00:00	Test Master	\N	\N	Base Master	1         	1         	1         	NA        	1	1
14	administrator       	2020-02-21 00:00:00	Project Master	\N	\N	Base Master	1         	1         	1         	NA        	1	1
15	administrator       	2020-02-21 00:00:00	Sample Master	\N	\N	Base Master	1         	1         	1         	NA        	1	1
16	administrator       	2020-02-21 00:00:00	Instrument Master	\N	\N	Base Master	1         	1         	1         	NA        	1	1
17	administrator       	2020-02-21 00:00:00	LIMS Test  Order	\N	\N	Sheet Settings	1         	1         	NA        	NA        	1	1
18	administrator       	2020-02-21 00:00:00	ELN & Research Activity Test Order	\N	\N	Sheet Settings	1         	1         	NA        	NA        	1	1
19	administrator       	2020-02-21 00:00:00	User Master	\N	\N	UserManagement	1         	1         	NA        	1         	1	1
20	administrator       	2020-02-21 00:00:00	User Group	\N	\N	UserManagement	1         	1         	NA        	NA        	1	1
21	administrator       	2020-02-21 00:00:00	User Rights	\N	\N	UserManagement	1         	1         	NA        	NA        	1	1
22	administrator       	2020-02-21 00:00:00	Project Team	\N	\N	UserManagement	1         	1         	1         	NA        	1	1
23	administrator       	2020-02-21 00:00:00	Order Workflow	\N	\N	UserManagement	1         	1         	NA        	NA        	1	1
24	administrator       	2020-02-21 00:00:00	Active User	\N	\N	UserManagement	1         	NA        	NA        	NA        	1	1
25	administrator       	2020-02-21 00:00:00	Sheet Workflow	\N	\N	UserManagement	1         	1         	NA        	NA        	1	1
26	administrator       	2020-02-21 00:00:00	Domain	\N	\N	UserManagement	1         	1         	1         	NA        	1	1
27	administrator       	2020-02-21 00:00:00	Password Policy	\N	\N	UserManagement	1         	1         	NA        	NA        	1	1
28	administrator       	2020-02-21 00:00:00	ACTIVATE/DEACTIVATE	\N	\N	User Group	1         	NA        	NA        	NA        	1	1
29	administrator       	2020-02-21 00:00:00	ACTIVATE/DEACTIVATE	\N	\N	User Master	1         	NA        	NA        	NA        	1	1
30	administrator       	2020-02-21 00:00:00	Unlock	\N	\N	User Master	1         	NA        	NA        	NA        	1	1
31	administrator       	2020-02-21 00:00:00	Reset Password	\N	\N	User Master	1         	NA        	NA        	NA        	1	1
32	administrator       	2020-02-21 00:00:00	Import ADS	\N	\N	User Master	1         	1         	NA        	NA        	1	1
33	administrator       	2020-02-21 00:00:00	Audit Trail History	\N	\N	AuditTrail History	1         	NA        	NA        	NA        	1	1
34	administrator       	2020-02-21 00:00:00	CFR Settings	\N	\N	AuditTrail History	1         	1         	1         	NA        	1	1
35	administrator       	2020-02-21 00:00:00	Audit Trail Configuration	\N	\N	AuditTrail History	1         	1         	NA        	NA        	1	1
36	administrator       	2020-02-21 00:00:00	Review History	\N	\N	AuditTrail History	1         	NA        	NA        	NA        	1	1
37	administrator       	2020-02-21 00:00:00	Review	\N	\N	AuditTrail History	1         	NA        	NA        	NA        	1	1
38	administrator       	2020-02-21 00:00:00	Create Archive	\N	\N	AuditTrail History	1         	NA        	NA        	NA        	1	1
39	administrator       	2020-02-21 00:00:00	Open Archive	\N	\N	AuditTrail History	1         	NA        	NA        	NA        	1	1
40	administrator       	2020-02-21 00:00:00	Export	\N	\N	AuditTrail History	1         	NA        	NA        	NA        	1	1
42	administrator       	2020-02-21 00:00:00	Reports	\N	\N	Reports	1         	1         	NA        	NA        	1	1
43	administrator       	2020-02-21 00:00:00	Generate Reports	\N	\N	Reports	1         	1         	NA        	NA        	1	1
44	administrator       	2020-02-21 00:00:00	Template Designing	\N	\N	Reports	1         	1         	NA        	NA        	1	1
\.


--
-- TOC entry 2973 (class 0 OID 226042)
-- Dependencies: 282
-- Data for Name: lsusergrouprightsmaster; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsusergrouprightsmaster (orderno, displaytopic, modulename, sallow, screate, sdelete, sedit, sequenceorder, status) FROM stdin;
1	Parameter OverView	Dash Board	1         	NA        	NA        	NA        	1	0,0,0     
2	Order Overview	Dash Board	1         	NA        	NA        	NA        	2	0,0,0     
3	Activities	Dash Board	1         	NA        	NA        	NA        	3	0,0,0     
4	Parameter Usage	Dash Board	1         	NA        	NA        	NA        	4	0,0,0     
5	Pending Work	Register Task Orders & Execute	1         	1         	NA        	NA        	5	1,0,0     
6	Completed Work	Register Task Orders & Execute	1         	1         	NA        	NA        	6	1,0,0     
7	LIMS Task Order	Register Task Orders & Execute	1         	1         	NA        	NA        	7	1,0,0     
8	ELN Task Order	Register Task Orders & Execute	1         	1         	NA        	NA        	8	1,0,0     
9	Research Activity Order	Register Task Orders & Execute	1         	1         	NA        	NA        	9	1,0,0     
10	Sheet Creation	Sheet Creation	1         	1         	NA        	1         	12	1,1,0     
11	Test Master	Base Master	1         	1         	1         	NA        	13	1,1,0     
12	Project Master	Base Master	1         	1         	1         	NA        	14	1,1,0     
13	Sample Master	Base Master	1         	1         	1         	NA        	15	1,1,0     
14	LIMS Test  Order	Sheet Settings	1         	1         	NA        	NA        	17	1,0,0     
15	ELN & Research Activity Test Order	Sheet Settings	1         	1         	NA        	NA        	18	1,0,0     
16	User Master	UserManagement	1         	1         	NA        	1         	20	1,0,1     
17	User Group	UserManagement	1         	1         	NA        	NA        	19	1,0,0     
18	User Rights	UserManagement	1         	1         	NA        	NA        	21	1,0,0     
19	Project Team	UserManagement	1         	1         	1         	NA        	22	1,1,0     
20	Order Workflow	UserManagement	1         	1         	NA        	NA        	23	1,0,0     
21	ACTIVATE/DEACTIVATE	User Group	1         	NA        	NA        	NA        	28	0,0,0     
22	ACTIVATE/DEACTIVATE	User Master	1         	NA        	NA        	NA        	31	0,0,0     
23	Unlock	User Master	1         	NA        	NA        	NA        	29	0,0,0     
24	Reset Password	User Master	1         	NA        	NA        	NA        	30	0,0,0     
25	Audit Trail History	AuditTrail History	1         	NA        	NA        	NA        	33	0,0,0     
26	CFR Settings	AuditTrail History	1         	1         	1         	NA        	39	1,1,0     
27	Audit Trail Configuration	AuditTrail History	1         	1         	NA        	NA        	40	1,0,0     
28	Review History	AuditTrail History	1         	NA        	NA        	NA        	35	0,0,0     
29	Review	AuditTrail History	1         	NA        	NA        	NA        	34	0,0,0     
30	Create Archive	AuditTrail History	1         	NA        	NA        	NA        	36	0,0,0     
31	Open Archive	AuditTrail History	1         	NA        	NA        	NA        	37	0,0,0     
32	Export	AuditTrail History	1         	NA        	NA        	NA        	38	0,0,0     
34	Reports	Reports	1         	1         	NA        	NA        	41	1,0,0     
35	Generate Reports	Reports	1         	1         	NA        	NA        	42	1,0,0     
37	Manage Excel	Register Task Orders & Execute	1         	1         	NA        	NA        	11	1,0,0     
38	Sheet Evaluation	Register Task Orders & Execute	1         	1         	NA        	NA        	10	1,0,0     
39	Instrument Master	Base Master	1         	1         	1         	NA        	16	1,1,0     
40	Import ADS	User Master	1         	1         	NA        	NA        	32	1,0,0     
41	Sheet Workflow	UserManagement	1         	1         	NA        	NA        	25	1,0,0     
42	Domain	UserManagement	1         	1         	1         	NA        	26	1,1,0     
43	Password Policy	UserManagement	1         	1         	NA        	NA        	27	1,0,0     
44	Active User	UserManagement	1         	1         	NA        	NA        	24	1,0,0     
45	Template Designing	Reports	1         	1         	NA        	NA        	43	1,0,0     
\.


--
-- TOC entry 2974 (class 0 OID 226047)
-- Dependencies: 283
-- Data for Name: lsusermaster; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsusermaster (usercode, createdby, createddate, emailid, labsheet, lastloggedon, lockcount, modifieduser, modifiedby, modifieddate, password, passwordexpirydate, profileimage, profileimagename, userfullname, username, userstatus, verificationcode, lssitemaster_sitecode, lsuseractions_useractioncode, lsusergroup_usergroupcode) FROM stdin;
1	\N	2020-01-21 00:00:00	\N	0	2020-03-18 00:00:00	0	\N	\N	\N	gyX57EHs08KuakyVhFVvGQ==	\N	\N	\N	Administrator	Administrator	A	\N	1	\N	1
\.


--
-- TOC entry 2975 (class 0 OID 226055)
-- Dependencies: 284
-- Data for Name: lsusersteam; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsusersteam (teamcode, createdate, modifieddate, status, teamname, createby_usercode, lssitemaster_sitecode, modifiedby_usercode) FROM stdin;
\.


--
-- TOC entry 2976 (class 0 OID 226060)
-- Dependencies: 285
-- Data for Name: lsuserteammapping; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsuserteammapping (userteammapcode, teamcode, lsusermaster_usercode) FROM stdin;
\.


--
-- TOC entry 2977 (class 0 OID 226065)
-- Dependencies: 286
-- Data for Name: lsworkflow; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsworkflow (workflowcode, workflowname, lssitemaster_sitecode) FROM stdin;
1	New	\N
\.


--
-- TOC entry 2978 (class 0 OID 226070)
-- Dependencies: 287
-- Data for Name: lsworkflowgroupmapping; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lsworkflowgroupmapping (workflowmapid, workflowcode, lsusergroup_usergroupcode) FROM stdin;
\.


--
-- TOC entry 2979 (class 0 OID 226075)
-- Dependencies: 288
-- Data for Name: resultfieldvalues; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY resultfieldvalues (resultdetid, resseqno, fieldname, fieldvalue) FROM stdin;
\.


--
-- TOC entry 2980 (class 0 OID 226083)
-- Dependencies: 289
-- Data for Name: t06instruments; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY t06instruments (t06_strinstid, t06_bytecaliber, t06_byte_clientbound, t06_bytedatasource, t06_bytemultisid, t06_byte_peakdetection, t06_bytesplitsid, t06_byteselected, t06_bytesid, t06_bytestationid, t06_calibduedate, t06_caliblastmodifiedon, t06_caliblastsetby, t06_calibstatus, t06_calibtime, t06_int_usedby, t06_lastcalibon, t06_strcalibtest, t06_strsched1, t06_strsched2, t06_strinstname, t06_striopnumber, t06_strelectrodeno, t06_strmake, t06_strmodel) FROM stdin;
\.


--
-- TOC entry 2981 (class 0 OID 226088)
-- Dependencies: 290
-- Data for Name: t23fields; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY t23fields (t23_strfieldkey, t23_bytealign, t23_bytecoltype, t23_bytedeletable, t23_bytedimension, t23_byteselected, t23_byteselloginrpt, t23_bytesequence, t23_bytevalue, t23_strdatatype, t23_strelnfieldname, t23_strfieldname, t23_strformat, t23_strinstrumentid, t23_strlimsfieldname, t23_strmethodname, t23_strparsername, t23_intsortsequence) FROM stdin;
\.


--
-- TOC entry 2991 (class 0 OID 0)
-- Dependencies: 185
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('hibernate_sequence', 1, false);


--
-- TOC entry 2992 (class 0 OID 0)
-- Dependencies: 276
-- Name: lsunmappedtemplate_templatecode_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('lsunmappedtemplate_templatecode_seq', 1, false);


--
-- TOC entry 2993 (class 0 OID 0)
-- Dependencies: 278
-- Name: lsuseractions_useractioncode_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('lsuseractions_useractioncode_seq', 1, false);


--
-- TOC entry 2994 (class 0 OID 0)
-- Dependencies: 186
-- Name: orderdetail; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('orderdetail', 1000000, false);


--
-- TOC entry 2461 (class 2606 OID 225445)
-- Name: l11instrumentmaster l11instrumentmaster_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY l11instrumentmaster
    ADD CONSTRAINT l11instrumentmaster_pkey PRIMARY KEY (l11instrumentid);


--
-- TOC entry 2463 (class 2606 OID 225453)
-- Name: llresultdetails llresultdetails_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY llresultdetails
    ADD CONSTRAINT llresultdetails_pkey PRIMARY KEY (resultid);


--
-- TOC entry 2467 (class 2606 OID 225464)
-- Name: logilablimsorder logilablimsorder_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY logilablimsorder
    ADD CONSTRAINT logilablimsorder_pkey PRIMARY KEY (orderid);


--
-- TOC entry 2469 (class 2606 OID 225469)
-- Name: lsactiveuser lsactiveuser_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsactiveuser
    ADD CONSTRAINT lsactiveuser_pkey PRIMARY KEY (activeusercode);


--
-- TOC entry 2471 (class 2606 OID 225474)
-- Name: lsactivity lsactivity_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsactivity
    ADD CONSTRAINT lsactivity_pkey PRIMARY KEY (activitycode);


--
-- TOC entry 2473 (class 2606 OID 225482)
-- Name: lsaudittrailconfigmaster lsaudittrailconfigmaster_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsaudittrailconfigmaster
    ADD CONSTRAINT lsaudittrailconfigmaster_pkey PRIMARY KEY (serialno);


--
-- TOC entry 2475 (class 2606 OID 225490)
-- Name: lsaudittrailconfiguration lsaudittrailconfiguration_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsaudittrailconfiguration
    ADD CONSTRAINT lsaudittrailconfiguration_pkey PRIMARY KEY (auditcofigcode);


--
-- TOC entry 2477 (class 2606 OID 225495)
-- Name: lsbatchdetails lsbatchdetails_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsbatchdetails
    ADD CONSTRAINT lsbatchdetails_pkey PRIMARY KEY (batchdetailcode);


--
-- TOC entry 2479 (class 2606 OID 225500)
-- Name: lscfrreasons lscfrreasons_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lscfrreasons
    ADD CONSTRAINT lscfrreasons_pkey PRIMARY KEY (reasoncode);


--
-- TOC entry 2481 (class 2606 OID 225505)
-- Name: lscfrsettings lscfrsettings_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lscfrsettings
    ADD CONSTRAINT lscfrsettings_pkey PRIMARY KEY (cfrsettingcode);


--
-- TOC entry 2483 (class 2606 OID 225513)
-- Name: lscfttransaction lscfttransaction_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lscfttransaction
    ADD CONSTRAINT lscfttransaction_pkey PRIMARY KEY (serialno);


--
-- TOC entry 2485 (class 2606 OID 225521)
-- Name: lsconfiguration lsconfiguration_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsconfiguration
    ADD CONSTRAINT lsconfiguration_pkey PRIMARY KEY (serialno);


--
-- TOC entry 2487 (class 2606 OID 225529)
-- Name: lscontrol lscontrol_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lscontrol
    ADD CONSTRAINT lscontrol_pkey PRIMARY KEY (controlcode);


--
-- TOC entry 2489 (class 2606 OID 225534)
-- Name: lsdocdirectory lsdocdirectory_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsdocdirectory
    ADD CONSTRAINT lsdocdirectory_pkey PRIMARY KEY (docdirectorycode);


--
-- TOC entry 2491 (class 2606 OID 225539)
-- Name: lsdocmanager lsdocmanager_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsdocmanager
    ADD CONSTRAINT lsdocmanager_pkey PRIMARY KEY (docmanagercode);


--
-- TOC entry 2493 (class 2606 OID 225547)
-- Name: lsdocreports lsdocreports_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsdocreports
    ADD CONSTRAINT lsdocreports_pkey PRIMARY KEY (docreportscode);


--
-- TOC entry 2495 (class 2606 OID 225552)
-- Name: lsdocreportsversionhistory lsdocreportsversionhistory_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsdocreportsversionhistory
    ADD CONSTRAINT lsdocreportsversionhistory_pkey PRIMARY KEY (docreportsversionhistorycode);


--
-- TOC entry 2497 (class 2606 OID 225560)
-- Name: lsdomainmaster lsdomainmaster_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsdomainmaster
    ADD CONSTRAINT lsdomainmaster_pkey PRIMARY KEY (domaincode);


--
-- TOC entry 2499 (class 2606 OID 225565)
-- Name: lselninstfieldmapping lselninstfieldmapping_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lselninstfieldmapping
    ADD CONSTRAINT lselninstfieldmapping_pkey PRIMARY KEY (instfieldmapcode);


--
-- TOC entry 2501 (class 2606 OID 225573)
-- Name: lselninstrumentfields lselninstrumentfields_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lselninstrumentfields
    ADD CONSTRAINT lselninstrumentfields_pkey PRIMARY KEY (fieldcode);


--
-- TOC entry 2503 (class 2606 OID 225578)
-- Name: lselninstrumentmapping lselninstrumentmapping_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lselninstrumentmapping
    ADD CONSTRAINT lselninstrumentmapping_pkey PRIMARY KEY (instrumentmapcode);


--
-- TOC entry 2505 (class 2606 OID 225583)
-- Name: lselninstrumentmaster lselninstrumentmaster_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lselninstrumentmaster
    ADD CONSTRAINT lselninstrumentmaster_pkey PRIMARY KEY (instrumentcode);


--
-- TOC entry 2507 (class 2606 OID 225588)
-- Name: lsequipmentmap lsequipmentmap_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsequipmentmap
    ADD CONSTRAINT lsequipmentmap_pkey PRIMARY KEY (equipmentcode);


--
-- TOC entry 2509 (class 2606 OID 225593)
-- Name: lsfeature lsfeature_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsfeature
    ADD CONSTRAINT lsfeature_pkey PRIMARY KEY (modulecode);


--
-- TOC entry 2511 (class 2606 OID 225601)
-- Name: lsfields lsfields_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsfields
    ADD CONSTRAINT lsfields_pkey PRIMARY KEY (fieldcode);


--
-- TOC entry 2513 (class 2606 OID 225609)
-- Name: lsfile lsfile_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsfile
    ADD CONSTRAINT lsfile_pkey PRIMARY KEY (filecode);


--
-- TOC entry 2515 (class 2606 OID 225614)
-- Name: lsfilemethod lsfilemethod_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsfilemethod
    ADD CONSTRAINT lsfilemethod_pkey PRIMARY KEY (filemethodcode);


--
-- TOC entry 2517 (class 2606 OID 225622)
-- Name: lsfileparameter lsfileparameter_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsfileparameter
    ADD CONSTRAINT lsfileparameter_pkey PRIMARY KEY (fileparametercode);


--
-- TOC entry 2519 (class 2606 OID 225627)
-- Name: lsfileparametermapping lsfileparametermapping_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsfileparametermapping
    ADD CONSTRAINT lsfileparametermapping_pkey PRIMARY KEY (fileparametermappingcode);


--
-- TOC entry 2521 (class 2606 OID 225632)
-- Name: lsfiletest lsfiletest_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsfiletest
    ADD CONSTRAINT lsfiletest_pkey PRIMARY KEY (filetestcode);


--
-- TOC entry 2523 (class 2606 OID 225640)
-- Name: lsfileversion lsfileversion_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsfileversion
    ADD CONSTRAINT lsfileversion_pkey PRIMARY KEY (fileversioncode);


--
-- TOC entry 2525 (class 2606 OID 225648)
-- Name: lsinstrument lsinstrument_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsinstrument
    ADD CONSTRAINT lsinstrument_pkey PRIMARY KEY (instrumentcode);


--
-- TOC entry 2527 (class 2606 OID 225656)
-- Name: lsinstrumentcategory lsinstrumentcategory_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsinstrumentcategory
    ADD CONSTRAINT lsinstrumentcategory_pkey PRIMARY KEY (instrumentcatcode);


--
-- TOC entry 2529 (class 2606 OID 225661)
-- Name: lsinstrumentsection lsinstrumentsection_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsinstrumentsection
    ADD CONSTRAINT lsinstrumentsection_pkey PRIMARY KEY (instrumentsectioncode);


--
-- TOC entry 2531 (class 2606 OID 225669)
-- Name: lsinstrumenttype lsinstrumenttype_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsinstrumenttype
    ADD CONSTRAINT lsinstrumenttype_pkey PRIMARY KEY (instrumenttypecode);


--
-- TOC entry 2533 (class 2606 OID 225674)
-- Name: lsintrumentresultmap lsintrumentresultmap_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsintrumentresultmap
    ADD CONSTRAINT lsintrumentresultmap_pkey PRIMARY KEY (instrumentcode);


--
-- TOC entry 2535 (class 2606 OID 225682)
-- Name: lslogilablimsorderdetail lslogilablimsorderdetail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lslogilablimsorderdetail
    ADD CONSTRAINT lslogilablimsorderdetail_pkey PRIMARY KEY (batchcode);


--
-- TOC entry 2537 (class 2606 OID 225687)
-- Name: lsmanufacturer lsmanufacturer_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsmanufacturer
    ADD CONSTRAINT lsmanufacturer_pkey PRIMARY KEY (manufcode);


--
-- TOC entry 2539 (class 2606 OID 225692)
-- Name: lsmappedtags lsmappedtags_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsmappedtags
    ADD CONSTRAINT lsmappedtags_pkey PRIMARY KEY (tagcode);


--
-- TOC entry 2541 (class 2606 OID 225697)
-- Name: lsmappedtemplate lsmappedtemplate_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsmappedtemplate
    ADD CONSTRAINT lsmappedtemplate_pkey PRIMARY KEY (templatecode);


--
-- TOC entry 2543 (class 2606 OID 225705)
-- Name: lsmaterial lsmaterial_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsmaterial
    ADD CONSTRAINT lsmaterial_pkey PRIMARY KEY (materialcode);


--
-- TOC entry 2545 (class 2606 OID 225710)
-- Name: lsmaterialcategory lsmaterialcategory_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsmaterialcategory
    ADD CONSTRAINT lsmaterialcategory_pkey PRIMARY KEY (materialcatcode);


--
-- TOC entry 2547 (class 2606 OID 225718)
-- Name: lsmaterialgrade lsmaterialgrade_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsmaterialgrade
    ADD CONSTRAINT lsmaterialgrade_pkey PRIMARY KEY (materialgradecode);


--
-- TOC entry 2549 (class 2606 OID 225726)
-- Name: lsmaterialinventory lsmaterialinventory_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsmaterialinventory
    ADD CONSTRAINT lsmaterialinventory_pkey PRIMARY KEY (materialinventorycode);


--
-- TOC entry 2551 (class 2606 OID 225734)
-- Name: lsmaterialinventorytransaction lsmaterialinventorytransaction_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsmaterialinventorytransaction
    ADD CONSTRAINT lsmaterialinventorytransaction_pkey PRIMARY KEY (materialinventtranscode);


--
-- TOC entry 2553 (class 2606 OID 225739)
-- Name: lsmaterialmap lsmaterialmap_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsmaterialmap
    ADD CONSTRAINT lsmaterialmap_pkey PRIMARY KEY (lsmaterialcode);


--
-- TOC entry 2555 (class 2606 OID 225744)
-- Name: lsmaterialsection lsmaterialsection_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsmaterialsection
    ADD CONSTRAINT lsmaterialsection_pkey PRIMARY KEY (materialsectioncode);


--
-- TOC entry 2557 (class 2606 OID 225749)
-- Name: lsmaterialtype lsmaterialtype_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsmaterialtype
    ADD CONSTRAINT lsmaterialtype_pkey PRIMARY KEY (materialtypecode);


--
-- TOC entry 2559 (class 2606 OID 225754)
-- Name: lsmultidocument lsmultidocument_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsmultidocument
    ADD CONSTRAINT lsmultidocument_pkey PRIMARY KEY (filecode);


--
-- TOC entry 2561 (class 2606 OID 225762)
-- Name: lsmultisteps lsmultisteps_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsmultisteps
    ADD CONSTRAINT lsmultisteps_pkey PRIMARY KEY (stepcode);


--
-- TOC entry 2563 (class 2606 OID 225770)
-- Name: lsnotification lsnotification_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsnotification
    ADD CONSTRAINT lsnotification_pkey PRIMARY KEY (notificationcode);


--
-- TOC entry 2565 (class 2606 OID 225778)
-- Name: lsorderattachmentfiles lsorderattachmentfiles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsorderattachmentfiles
    ADD CONSTRAINT lsorderattachmentfiles_pkey PRIMARY KEY (id);


--
-- TOC entry 2567 (class 2606 OID 225783)
-- Name: lsorderattachments lsorderattachments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsorderattachments
    ADD CONSTRAINT lsorderattachments_pkey PRIMARY KEY (attachmentcode);


--
-- TOC entry 2569 (class 2606 OID 225791)
-- Name: lsordercreationfiles lsordercreationfiles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsordercreationfiles
    ADD CONSTRAINT lsordercreationfiles_pkey PRIMARY KEY (id);


--
-- TOC entry 2571 (class 2606 OID 225799)
-- Name: lsorderversionfiles lsorderversionfiles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsorderversionfiles
    ADD CONSTRAINT lsorderversionfiles_pkey PRIMARY KEY (id);


--
-- TOC entry 2573 (class 2606 OID 225807)
-- Name: lsorderworkflowhistory lsorderworkflowhistory_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsorderworkflowhistory
    ADD CONSTRAINT lsorderworkflowhistory_pkey PRIMARY KEY (historycode);


--
-- TOC entry 2575 (class 2606 OID 225812)
-- Name: lsorderworkflowtransaction lsorderworkflowtransaction_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsorderworkflowtransaction
    ADD CONSTRAINT lsorderworkflowtransaction_pkey PRIMARY KEY (workflowtransactioncode);


--
-- TOC entry 2577 (class 2606 OID 225820)
-- Name: lsparsedparameters lsparsedparameters_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsparsedparameters
    ADD CONSTRAINT lsparsedparameters_pkey PRIMARY KEY (parsedparametercode);


--
-- TOC entry 2579 (class 2606 OID 225828)
-- Name: lspasswordhistory lspasswordhistory_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lspasswordhistory
    ADD CONSTRAINT lspasswordhistory_pkey PRIMARY KEY (userid);


--
-- TOC entry 2581 (class 2606 OID 225833)
-- Name: lspasswordhistorydetails lspasswordhistorydetails_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lspasswordhistorydetails
    ADD CONSTRAINT lspasswordhistorydetails_pkey PRIMARY KEY (passwordcode);


--
-- TOC entry 2583 (class 2606 OID 225838)
-- Name: lspasswordpolicy lspasswordpolicy_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lspasswordpolicy
    ADD CONSTRAINT lspasswordpolicy_pkey PRIMARY KEY (policycode);


--
-- TOC entry 2585 (class 2606 OID 225843)
-- Name: lspreferences lspreferences_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lspreferences
    ADD CONSTRAINT lspreferences_pkey PRIMARY KEY (serialno);


--
-- TOC entry 2587 (class 2606 OID 225851)
-- Name: lsprofilepicture lsprofilepicture_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsprofilepicture
    ADD CONSTRAINT lsprofilepicture_pkey PRIMARY KEY (id);


--
-- TOC entry 2589 (class 2606 OID 225856)
-- Name: lsprojectmaster lsprojectmaster_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsprojectmaster
    ADD CONSTRAINT lsprojectmaster_pkey PRIMARY KEY (projectcode);


--
-- TOC entry 2591 (class 2606 OID 225861)
-- Name: lsprotocolmaster lsprotocolmaster_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsprotocolmaster
    ADD CONSTRAINT lsprotocolmaster_pkey PRIMARY KEY (protocolmastercode);


--
-- TOC entry 2593 (class 2606 OID 225869)
-- Name: lsprotocolstep lsprotocolstep_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsprotocolstep
    ADD CONSTRAINT lsprotocolstep_pkey PRIMARY KEY (protocolstepcode);


--
-- TOC entry 2595 (class 2606 OID 225874)
-- Name: lsreviewdetails lsreviewdetails_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsreviewdetails
    ADD CONSTRAINT lsreviewdetails_pkey PRIMARY KEY (serialno);


--
-- TOC entry 2597 (class 2606 OID 225882)
-- Name: lssamplefile lssamplefile_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lssamplefile
    ADD CONSTRAINT lssamplefile_pkey PRIMARY KEY (filesamplecode);


--
-- TOC entry 2599 (class 2606 OID 225890)
-- Name: lssamplefileversion lssamplefileversion_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lssamplefileversion
    ADD CONSTRAINT lssamplefileversion_pkey PRIMARY KEY (filesamplecodeversion);


--
-- TOC entry 2601 (class 2606 OID 225895)
-- Name: lssamplemaster lssamplemaster_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lssamplemaster
    ADD CONSTRAINT lssamplemaster_pkey PRIMARY KEY (samplecode);


--
-- TOC entry 2603 (class 2606 OID 225900)
-- Name: lssampleresult lssampleresult_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lssampleresult
    ADD CONSTRAINT lssampleresult_pkey PRIMARY KEY (sampleresultcode);


--
-- TOC entry 2605 (class 2606 OID 225905)
-- Name: lssection lssection_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lssection
    ADD CONSTRAINT lssection_pkey PRIMARY KEY (sectioncode);


--
-- TOC entry 2607 (class 2606 OID 225910)
-- Name: lssequencenogenerator lssequencenogenerator_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lssequencenogenerator
    ADD CONSTRAINT lssequencenogenerator_pkey PRIMARY KEY (settingcode);


--
-- TOC entry 2609 (class 2606 OID 225918)
-- Name: lssettings lssettings_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lssettings
    ADD CONSTRAINT lssettings_pkey PRIMARY KEY (settingid);


--
-- TOC entry 2611 (class 2606 OID 225926)
-- Name: lssheetcreationfiles lssheetcreationfiles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lssheetcreationfiles
    ADD CONSTRAINT lssheetcreationfiles_pkey PRIMARY KEY (id);


--
-- TOC entry 2613 (class 2606 OID 225934)
-- Name: lssheetupdates lssheetupdates_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lssheetupdates
    ADD CONSTRAINT lssheetupdates_pkey PRIMARY KEY (sheetcode);


--
-- TOC entry 2615 (class 2606 OID 225942)
-- Name: lssheetversionfiles lssheetversionfiles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lssheetversionfiles
    ADD CONSTRAINT lssheetversionfiles_pkey PRIMARY KEY (id);


--
-- TOC entry 2617 (class 2606 OID 225947)
-- Name: lssheetworkflow lssheetworkflow_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lssheetworkflow
    ADD CONSTRAINT lssheetworkflow_pkey PRIMARY KEY (workflowcode);


--
-- TOC entry 2619 (class 2606 OID 225952)
-- Name: lssheetworkflowgroupmap lssheetworkflowgroupmap_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lssheetworkflowgroupmap
    ADD CONSTRAINT lssheetworkflowgroupmap_pkey PRIMARY KEY (workflowmapid);


--
-- TOC entry 2621 (class 2606 OID 225960)
-- Name: lssheetworkflowhistory lssheetworkflowhistory_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lssheetworkflowhistory
    ADD CONSTRAINT lssheetworkflowhistory_pkey PRIMARY KEY (historycode);


--
-- TOC entry 2623 (class 2606 OID 225968)
-- Name: lssitemaster lssitemaster_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lssitemaster
    ADD CONSTRAINT lssitemaster_pkey PRIMARY KEY (sitecode);


--
-- TOC entry 2625 (class 2606 OID 225973)
-- Name: lstestmapping lstestmapping_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lstestmapping
    ADD CONSTRAINT lstestmapping_pkey PRIMARY KEY (testid);


--
-- TOC entry 2627 (class 2606 OID 225981)
-- Name: lstestmaster lstestmaster_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lstestmaster
    ADD CONSTRAINT lstestmaster_pkey PRIMARY KEY (testcode);


--
-- TOC entry 2629 (class 2606 OID 225986)
-- Name: lstestmasterlocal lstestmasterlocal_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lstestmasterlocal
    ADD CONSTRAINT lstestmasterlocal_pkey PRIMARY KEY (testcode);


--
-- TOC entry 2631 (class 2606 OID 225994)
-- Name: lstestparameter lstestparameter_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lstestparameter
    ADD CONSTRAINT lstestparameter_pkey PRIMARY KEY (testparametercode);


--
-- TOC entry 2633 (class 2606 OID 225999)
-- Name: lstransactionstatus lstransactionstatus_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lstransactionstatus
    ADD CONSTRAINT lstransactionstatus_pkey PRIMARY KEY (transcode);


--
-- TOC entry 2635 (class 2606 OID 226007)
-- Name: lsunit lsunit_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsunit
    ADD CONSTRAINT lsunit_pkey PRIMARY KEY (unitcode);


--
-- TOC entry 2637 (class 2606 OID 226012)
-- Name: lsunmappedtags lsunmappedtags_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsunmappedtags
    ADD CONSTRAINT lsunmappedtags_pkey PRIMARY KEY (tagcode);


--
-- TOC entry 2639 (class 2606 OID 226020)
-- Name: lsunmappedtemplate lsunmappedtemplate_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsunmappedtemplate
    ADD CONSTRAINT lsunmappedtemplate_pkey PRIMARY KEY (templatecode);


--
-- TOC entry 2641 (class 2606 OID 226028)
-- Name: lsuseractions lsuseractions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsuseractions
    ADD CONSTRAINT lsuseractions_pkey PRIMARY KEY (useractioncode);


--
-- TOC entry 2643 (class 2606 OID 226036)
-- Name: lsusergroup lsusergroup_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsusergroup
    ADD CONSTRAINT lsusergroup_pkey PRIMARY KEY (usergroupcode);


--
-- TOC entry 2645 (class 2606 OID 226041)
-- Name: lsusergrouprights lsusergrouprights_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsusergrouprights
    ADD CONSTRAINT lsusergrouprights_pkey PRIMARY KEY (orderno);


--
-- TOC entry 2647 (class 2606 OID 226046)
-- Name: lsusergrouprightsmaster lsusergrouprightsmaster_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsusergrouprightsmaster
    ADD CONSTRAINT lsusergrouprightsmaster_pkey PRIMARY KEY (orderno);


--
-- TOC entry 2649 (class 2606 OID 226054)
-- Name: lsusermaster lsusermaster_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsusermaster
    ADD CONSTRAINT lsusermaster_pkey PRIMARY KEY (usercode);


--
-- TOC entry 2651 (class 2606 OID 226059)
-- Name: lsusersteam lsusersteam_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsusersteam
    ADD CONSTRAINT lsusersteam_pkey PRIMARY KEY (teamcode);


--
-- TOC entry 2653 (class 2606 OID 226064)
-- Name: lsuserteammapping lsuserteammapping_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsuserteammapping
    ADD CONSTRAINT lsuserteammapping_pkey PRIMARY KEY (userteammapcode);


--
-- TOC entry 2655 (class 2606 OID 226069)
-- Name: lsworkflow lsworkflow_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsworkflow
    ADD CONSTRAINT lsworkflow_pkey PRIMARY KEY (workflowcode);


--
-- TOC entry 2657 (class 2606 OID 226074)
-- Name: lsworkflowgroupmapping lsworkflowgroupmapping_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsworkflowgroupmapping
    ADD CONSTRAINT lsworkflowgroupmapping_pkey PRIMARY KEY (workflowmapid);


--
-- TOC entry 2659 (class 2606 OID 226082)
-- Name: resultfieldvalues resultfieldvalues_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY resultfieldvalues
    ADD CONSTRAINT resultfieldvalues_pkey PRIMARY KEY (resultdetid, resseqno);


--
-- TOC entry 2661 (class 2606 OID 226087)
-- Name: t06instruments t06instruments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t06instruments
    ADD CONSTRAINT t06instruments_pkey PRIMARY KEY (t06_strinstid);


--
-- TOC entry 2663 (class 2606 OID 226092)
-- Name: t23fields t23fields_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t23fields
    ADD CONSTRAINT t23fields_pkey PRIMARY KEY (t23_strfieldkey);


--
-- TOC entry 2465 (class 2606 OID 226094)
-- Name: llresultdetails_resultfieldvalues uk_rdw0q80x9qbok7kep5i1dhegm; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY llresultdetails_resultfieldvalues
    ADD CONSTRAINT uk_rdw0q80x9qbok7kep5i1dhegm UNIQUE (lsresultvalues_resultdetid, lsresultvalues_resseqno);


--
-- TOC entry 2758 (class 2606 OID 226565)
-- Name: lsworkflowgroupmapping fk163s1nh3pu2utbr9q8tgfh4gr; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsworkflowgroupmapping
    ADD CONSTRAINT fk163s1nh3pu2utbr9q8tgfh4gr FOREIGN KEY (workflowcode) REFERENCES lsworkflow(workflowcode);


--
-- TOC entry 2712 (class 2606 OID 226335)
-- Name: lsorderworkflowhistory fk18tvvee4s1bblcx5muteq6t7i; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsorderworkflowhistory
    ADD CONSTRAINT fk18tvvee4s1bblcx5muteq6t7i FOREIGN KEY (currentworkflow_workflowcode) REFERENCES lsworkflow(workflowcode);


--
-- TOC entry 2709 (class 2606 OID 226320)
-- Name: lsorderattachments fk19w7n13uus538gnb93fwj8y7p; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsorderattachments
    ADD CONSTRAINT fk19w7n13uus538gnb93fwj8y7p FOREIGN KEY (modifiedby_usercode) REFERENCES lsusermaster(usercode);


--
-- TOC entry 2674 (class 2606 OID 226145)
-- Name: lsdomainmaster fk1wc6tmtfd978ss6blubjuwq67; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsdomainmaster
    ADD CONSTRAINT fk1wc6tmtfd978ss6blubjuwq67 FOREIGN KEY (lssitemaster_sitecode) REFERENCES lssitemaster(sitecode);


--
-- TOC entry 2694 (class 2606 OID 226245)
-- Name: lsfileversion fk1yk72j8r2286axm14ikcm37yq; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsfileversion
    ADD CONSTRAINT fk1yk72j8r2286axm14ikcm37yq FOREIGN KEY (modifiedby_usercode) REFERENCES lsusermaster(usercode);


--
-- TOC entry 2665 (class 2606 OID 226100)
-- Name: llresultdetails_resultfieldvalues fk2c3e37wq754sykcjkkjuet3og; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY llresultdetails_resultfieldvalues
    ADD CONSTRAINT fk2c3e37wq754sykcjkkjuet3og FOREIGN KEY (lsresultdetails_resultid) REFERENCES llresultdetails(resultid);


--
-- TOC entry 2670 (class 2606 OID 226125)
-- Name: lsaudittrailconfiguration fk2hd9ldpnj0nxtt9yucg61f2ca; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsaudittrailconfiguration
    ADD CONSTRAINT fk2hd9ldpnj0nxtt9yucg61f2ca FOREIGN KEY (lsusermaster_usercode) REFERENCES lsusermaster(usercode);


--
-- TOC entry 2742 (class 2606 OID 226485)
-- Name: lstestmasterlocal fk2rcpoeejioiswoorrdarkuec2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lstestmasterlocal
    ADD CONSTRAINT fk2rcpoeejioiswoorrdarkuec2 FOREIGN KEY (modifiedby_usercode) REFERENCES lsusermaster(usercode);


--
-- TOC entry 2669 (class 2606 OID 226120)
-- Name: lsaudittrailconfiguration fk30mdgiq4vposbv4c9nlgmxgn8; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsaudittrailconfiguration
    ADD CONSTRAINT fk30mdgiq4vposbv4c9nlgmxgn8 FOREIGN KEY (lssitemaster_sitecode) REFERENCES lssitemaster(sitecode);


--
-- TOC entry 2692 (class 2606 OID 226235)
-- Name: lsfileversion fk3h8hrld2ih0xwsbgkgfmkvic6; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsfileversion
    ADD CONSTRAINT fk3h8hrld2ih0xwsbgkgfmkvic6 FOREIGN KEY (lssheetworkflow_workflowcode) REFERENCES lssheetworkflow(workflowcode);


--
-- TOC entry 2735 (class 2606 OID 226450)
-- Name: lssheetworkflow fk3orqy240t3edo5cj5iuxcvkai; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lssheetworkflow
    ADD CONSTRAINT fk3orqy240t3edo5cj5iuxcvkai FOREIGN KEY (lssitemaster_sitecode) REFERENCES lssitemaster(sitecode);


--
-- TOC entry 2719 (class 2606 OID 226370)
-- Name: lsprojectmaster fk4faa3eo1ltutdxfipnhhj3hfh; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsprojectmaster
    ADD CONSTRAINT fk4faa3eo1ltutdxfipnhhj3hfh FOREIGN KEY (lsusersteam_teamcode) REFERENCES lsusersteam(teamcode);


--
-- TOC entry 2682 (class 2606 OID 226185)
-- Name: lsequipmentmap fk4prfv2pbj9p1paummc68g7p7t; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsequipmentmap
    ADD CONSTRAINT fk4prfv2pbj9p1paummc68g7p7t FOREIGN KEY (lsinstrument_instrumentcode) REFERENCES lsinstrument(instrumentcode);


--
-- TOC entry 2696 (class 2606 OID 226255)
-- Name: lslogilablimsorderdetail fk4vltk0oi6r82aki1vna0vkd9u; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lslogilablimsorderdetail
    ADD CONSTRAINT fk4vltk0oi6r82aki1vna0vkd9u FOREIGN KEY (assignedto_usercode) REFERENCES lsusermaster(usercode);


--
-- TOC entry 2677 (class 2606 OID 226160)
-- Name: lselninstrumentfields fk4vqjd39626aidj1ib1ly04csk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lselninstrumentfields
    ADD CONSTRAINT fk4vqjd39626aidj1ib1ly04csk FOREIGN KEY (instrumentcode) REFERENCES lselninstrumentmaster(instrumentcode);


--
-- TOC entry 2720 (class 2606 OID 226375)
-- Name: lsprojectmaster fk4xqu1pox5hij3mbw29v0n990n; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsprojectmaster
    ADD CONSTRAINT fk4xqu1pox5hij3mbw29v0n990n FOREIGN KEY (modifiedby_usercode) REFERENCES lsusermaster(usercode);


--
-- TOC entry 2746 (class 2606 OID 226505)
-- Name: lsusergrouprights fk4yqgrsisatfci8nd2h5or2ybh; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsusergrouprights
    ADD CONSTRAINT fk4yqgrsisatfci8nd2h5or2ybh FOREIGN KEY (lssitemaster_sitecode) REFERENCES lssitemaster(sitecode);


--
-- TOC entry 2745 (class 2606 OID 226500)
-- Name: lsusergroup fk515mq8wis2wag7b79q7lcokbn; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsusergroup
    ADD CONSTRAINT fk515mq8wis2wag7b79q7lcokbn FOREIGN KEY (createby_usercode) REFERENCES lsusermaster(usercode);


--
-- TOC entry 2750 (class 2606 OID 226525)
-- Name: lsusermaster fk5p6srnqdps6ueuf5ot19enkxo; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsusermaster
    ADD CONSTRAINT fk5p6srnqdps6ueuf5ot19enkxo FOREIGN KEY (lsusergroup_usergroupcode) REFERENCES lsusergroup(usergroupcode);


--
-- TOC entry 2729 (class 2606 OID 226420)
-- Name: lssamplefileversion fk5s0hwe5os89w8fho21q0vmwil; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lssamplefileversion
    ADD CONSTRAINT fk5s0hwe5os89w8fho21q0vmwil FOREIGN KEY (modifiedby_usercode) REFERENCES lsusermaster(usercode);


--
-- TOC entry 2686 (class 2606 OID 226205)
-- Name: lsfile fk5vbab3w8dqiundweiax3fw4vl; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsfile
    ADD CONSTRAINT fk5vbab3w8dqiundweiax3fw4vl FOREIGN KEY (lssitemaster_sitecode) REFERENCES lssitemaster(sitecode);


--
-- TOC entry 2698 (class 2606 OID 226265)
-- Name: lslogilablimsorderdetail fk62rg8im08r615alah6oda1aj2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lslogilablimsorderdetail
    ADD CONSTRAINT fk62rg8im08r615alah6oda1aj2 FOREIGN KEY (lsprojectmaster_projectcode) REFERENCES lsprojectmaster(projectcode);


--
-- TOC entry 2734 (class 2606 OID 226445)
-- Name: lssheetupdates fk64qmk8tcajwo36ntsvllvipcj; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lssheetupdates
    ADD CONSTRAINT fk64qmk8tcajwo36ntsvllvipcj FOREIGN KEY (modifiedby_usercode) REFERENCES lsusermaster(usercode);


--
-- TOC entry 2738 (class 2606 OID 226465)
-- Name: lssheetworkflowhistory fk6m5ctgs00y28xamam4hh0vvoh; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lssheetworkflowhistory
    ADD CONSTRAINT fk6m5ctgs00y28xamam4hh0vvoh FOREIGN KEY (createby_usercode) REFERENCES lsusermaster(usercode);


--
-- TOC entry 2699 (class 2606 OID 226270)
-- Name: lslogilablimsorderdetail fk6p8uc39h61qm26aucs0qfqd30; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lslogilablimsorderdetail
    ADD CONSTRAINT fk6p8uc39h61qm26aucs0qfqd30 FOREIGN KEY (lssamplefile_filesamplecode) REFERENCES lssamplefile(filesamplecode);


--
-- TOC entry 2730 (class 2606 OID 226425)
-- Name: lssamplemaster fk6rce4865ot4ebtd9abdhu9t2a; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lssamplemaster
    ADD CONSTRAINT fk6rce4865ot4ebtd9abdhu9t2a FOREIGN KEY (lssitemaster_sitecode) REFERENCES lssitemaster(sitecode);


--
-- TOC entry 2713 (class 2606 OID 226340)
-- Name: lsorderworkflowhistory fk6satcotyjt3ssh0eycrt31p9k; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsorderworkflowhistory
    ADD CONSTRAINT fk6satcotyjt3ssh0eycrt31p9k FOREIGN KEY (batchcode) REFERENCES lslogilablimsorderdetail(batchcode);


--
-- TOC entry 2667 (class 2606 OID 226110)
-- Name: lsactiveuser fk74hwovrqh4m11n1s3vpgsrwvc; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsactiveuser
    ADD CONSTRAINT fk74hwovrqh4m11n1s3vpgsrwvc FOREIGN KEY (lsusermaster_usercode) REFERENCES lsusermaster(usercode);


--
-- TOC entry 2683 (class 2606 OID 226190)
-- Name: lsequipmentmap fk7hrm872cl3lmxylvt2mugr6hd; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsequipmentmap
    ADD CONSTRAINT fk7hrm872cl3lmxylvt2mugr6hd FOREIGN KEY (testcode) REFERENCES lstestmasterlocal(testcode);


--
-- TOC entry 2732 (class 2606 OID 226435)
-- Name: lssampleresult fk7r3x4c1qkgl6b0hpi4hke8m3p; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lssampleresult
    ADD CONSTRAINT fk7r3x4c1qkgl6b0hpi4hke8m3p FOREIGN KEY (lssitemaster_sitecode) REFERENCES lssitemaster(sitecode);


--
-- TOC entry 2697 (class 2606 OID 226260)
-- Name: lslogilablimsorderdetail fk855k6xqajtsjwdcdixaqx79if; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lslogilablimsorderdetail
    ADD CONSTRAINT fk855k6xqajtsjwdcdixaqx79if FOREIGN KEY (lsfile_filecode) REFERENCES lsfile(filecode);


--
-- TOC entry 2673 (class 2606 OID 226140)
-- Name: lscfrsettings fk8c7ahvsiy9cx63vm2rgopr2is; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lscfrsettings
    ADD CONSTRAINT fk8c7ahvsiy9cx63vm2rgopr2is FOREIGN KEY (lssitemaster_sitecode) REFERENCES lssitemaster(sitecode);


--
-- TOC entry 2687 (class 2606 OID 226210)
-- Name: lsfile fk8t1yybxqqdrldt59uskrs0thm; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsfile
    ADD CONSTRAINT fk8t1yybxqqdrldt59uskrs0thm FOREIGN KEY (modifiedby_usercode) REFERENCES lsusermaster(usercode);


--
-- TOC entry 2685 (class 2606 OID 226200)
-- Name: lsfile fk96987slqv8ubvpk53k4kp70do; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsfile
    ADD CONSTRAINT fk96987slqv8ubvpk53k4kp70do FOREIGN KEY (lssheetworkflow_workflowcode) REFERENCES lssheetworkflow(workflowcode);


--
-- TOC entry 2731 (class 2606 OID 226430)
-- Name: lssamplemaster fk96bdvg0hwhqxwqitafwrmd5c3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lssamplemaster
    ADD CONSTRAINT fk96bdvg0hwhqxwqitafwrmd5c3 FOREIGN KEY (modifiedby_usercode) REFERENCES lsusermaster(usercode);


--
-- TOC entry 2708 (class 2606 OID 226315)
-- Name: lsorderattachments fk9m36tjv4e767440yabcsup621; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsorderattachments
    ADD CONSTRAINT fk9m36tjv4e767440yabcsup621 FOREIGN KEY (createby_usercode) REFERENCES lsusermaster(usercode);


--
-- TOC entry 2733 (class 2606 OID 226440)
-- Name: lssampleresult fka4v62cgn14haofe9bpxjpc441; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lssampleresult
    ADD CONSTRAINT fka4v62cgn14haofe9bpxjpc441 FOREIGN KEY (filesamplecode) REFERENCES lssamplefile(filesamplecode);


--
-- TOC entry 2664 (class 2606 OID 226095)
-- Name: llresultdetails_resultfieldvalues fkbb35qq80vx2asjtkt68jpju3a; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY llresultdetails_resultfieldvalues
    ADD CONSTRAINT fkbb35qq80vx2asjtkt68jpju3a FOREIGN KEY (lsresultvalues_resultdetid, lsresultvalues_resseqno) REFERENCES resultfieldvalues(resultdetid, resseqno);


--
-- TOC entry 2702 (class 2606 OID 226285)
-- Name: lslogilablimsorderdetail fkc5qq7421s680pl4nai183322a; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lslogilablimsorderdetail
    ADD CONSTRAINT fkc5qq7421s680pl4nai183322a FOREIGN KEY (lsworkflow_workflowcode) REFERENCES lsworkflow(workflowcode);


--
-- TOC entry 2726 (class 2606 OID 226405)
-- Name: lssamplefile fkcbowolsdq6qvdaj3dqjwgaji3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lssamplefile
    ADD CONSTRAINT fkcbowolsdq6qvdaj3dqjwgaji3 FOREIGN KEY (modifiedby_usercode) REFERENCES lsusermaster(usercode);


--
-- TOC entry 2671 (class 2606 OID 226130)
-- Name: lsbatchdetails fkcdgsg3mwms67ca549spd4v619; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsbatchdetails
    ADD CONSTRAINT fkcdgsg3mwms67ca549spd4v619 FOREIGN KEY (batchcode) REFERENCES lslogilablimsorderdetail(batchcode);


--
-- TOC entry 2703 (class 2606 OID 226290)
-- Name: lsmappedtags fkcmawu6cg31lvo17rink8uejx8; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsmappedtags
    ADD CONSTRAINT fkcmawu6cg31lvo17rink8uejx8 FOREIGN KEY (templatecode) REFERENCES lsmappedtemplate(templatecode);


--
-- TOC entry 2740 (class 2606 OID 226475)
-- Name: lssheetworkflowhistory fkcn03n3mikriwvk22pvddn88x9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lssheetworkflowhistory
    ADD CONSTRAINT fkcn03n3mikriwvk22pvddn88x9 FOREIGN KEY (filecode) REFERENCES lsfile(filecode);


--
-- TOC entry 2757 (class 2606 OID 226560)
-- Name: lsworkflowgroupmapping fkcocpnt82y9jmf3w9yg8ess49p; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsworkflowgroupmapping
    ADD CONSTRAINT fkcocpnt82y9jmf3w9yg8ess49p FOREIGN KEY (lsusergroup_usergroupcode) REFERENCES lsusergroup(usergroupcode);


--
-- TOC entry 2681 (class 2606 OID 226180)
-- Name: lselninstrumentmaster fkd0a2uv5k6t4ujvhvr25q6t5i9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lselninstrumentmaster
    ADD CONSTRAINT fkd0a2uv5k6t4ujvhvr25q6t5i9 FOREIGN KEY (modifiedby_usercode) REFERENCES lsusermaster(usercode);


--
-- TOC entry 2716 (class 2606 OID 226355)
-- Name: lspasswordpolicy fkd2n5fjp116ebymw062su4kqb9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lspasswordpolicy
    ADD CONSTRAINT fkd2n5fjp116ebymw062su4kqb9 FOREIGN KEY (lssitemaster_sitecode) REFERENCES lssitemaster(sitecode);


--
-- TOC entry 2675 (class 2606 OID 226150)
-- Name: lselninstfieldmapping fkeipclv6tq8fb8ib3pardxt2k5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lselninstfieldmapping
    ADD CONSTRAINT fkeipclv6tq8fb8ib3pardxt2k5 FOREIGN KEY (lsmethodfields_t23_strfieldkey) REFERENCES t23fields(t23_strfieldkey);


--
-- TOC entry 2707 (class 2606 OID 226310)
-- Name: lsnotification fkeuymed6pd9ti7wlgw6lnnfg4t; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsnotification
    ADD CONSTRAINT fkeuymed6pd9ti7wlgw6lnnfg4t FOREIGN KEY (notifationto_usercode) REFERENCES lsusermaster(usercode);


--
-- TOC entry 2743 (class 2606 OID 226490)
-- Name: lstestparameter fkf2p1k5jsx75tfv66mtog97wsm; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lstestparameter
    ADD CONSTRAINT fkf2p1k5jsx75tfv66mtog97wsm FOREIGN KEY (testcode) REFERENCES lstestmaster(testcode);


--
-- TOC entry 2706 (class 2606 OID 226305)
-- Name: lsnotification fkf3729l7bjv8invf14dfxeittn; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsnotification
    ADD CONSTRAINT fkf3729l7bjv8invf14dfxeittn FOREIGN KEY (notifationfrom_usercode) REFERENCES lsusermaster(usercode);


--
-- TOC entry 2748 (class 2606 OID 226515)
-- Name: lsusermaster fkf7xt9ndydo5rfuj1joldfrps7; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsusermaster
    ADD CONSTRAINT fkf7xt9ndydo5rfuj1joldfrps7 FOREIGN KEY (lssitemaster_sitecode) REFERENCES lssitemaster(sitecode);


--
-- TOC entry 2721 (class 2606 OID 226380)
-- Name: lsreviewdetails fkf8tly798dhqpr7ahxdcariefm; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsreviewdetails
    ADD CONSTRAINT fkf8tly798dhqpr7ahxdcariefm FOREIGN KEY (lscfttransaction_usercode) REFERENCES lsusermaster(usercode);


--
-- TOC entry 2695 (class 2606 OID 226250)
-- Name: lsfileversion fkfmiskrtmhpfxubafnt0ao74qg; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsfileversion
    ADD CONSTRAINT fkfmiskrtmhpfxubafnt0ao74qg FOREIGN KEY (filecode) REFERENCES lsfile(filecode);


--
-- TOC entry 2727 (class 2606 OID 226410)
-- Name: lssamplefileversion fkfyw6frlo57djrt7vf5ca00ptm; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lssamplefileversion
    ADD CONSTRAINT fkfyw6frlo57djrt7vf5ca00ptm FOREIGN KEY (createbyuser_usercode) REFERENCES lsusermaster(usercode);


--
-- TOC entry 2679 (class 2606 OID 226170)
-- Name: lselninstrumentmapping fkgs4ex5fvjoyi0aabo1wxs6jd4; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lselninstrumentmapping
    ADD CONSTRAINT fkgs4ex5fvjoyi0aabo1wxs6jd4 FOREIGN KEY (instrumentcode) REFERENCES lselninstrumentmaster(instrumentcode);


--
-- TOC entry 2710 (class 2606 OID 226325)
-- Name: lsorderattachments fkgvc5xh9u7fs3sb2d6w8acxvt8; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsorderattachments
    ADD CONSTRAINT fkgvc5xh9u7fs3sb2d6w8acxvt8 FOREIGN KEY (batchcode) REFERENCES lslogilablimsorderdetail(batchcode);


--
-- TOC entry 2715 (class 2606 OID 226350)
-- Name: lspasswordhistorydetails fkgyf8obj737ka7kvnd3d0jcub2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lspasswordhistorydetails
    ADD CONSTRAINT fkgyf8obj737ka7kvnd3d0jcub2 FOREIGN KEY (lsusermaster_usercode) REFERENCES lsusermaster(usercode);


--
-- TOC entry 2704 (class 2606 OID 226295)
-- Name: lsmaterialmap fkhnt1xsspcpujt9wsq1kolqp43; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsmaterialmap
    ADD CONSTRAINT fkhnt1xsspcpujt9wsq1kolqp43 FOREIGN KEY (lsmaterial_materialcode) REFERENCES lsmaterial(materialcode);


--
-- TOC entry 2678 (class 2606 OID 226165)
-- Name: lselninstrumentmapping fkhr3ddde0llla0uyhk7ktjpttv; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lselninstrumentmapping
    ADD CONSTRAINT fkhr3ddde0llla0uyhk7ktjpttv FOREIGN KEY (lsinstruments_t06_strinstid) REFERENCES t06instruments(t06_strinstid);


--
-- TOC entry 2693 (class 2606 OID 226240)
-- Name: lsfileversion fkhrxowhem70526dnqxy2r64sif; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsfileversion
    ADD CONSTRAINT fkhrxowhem70526dnqxy2r64sif FOREIGN KEY (lssitemaster_sitecode) REFERENCES lssitemaster(sitecode);


--
-- TOC entry 2756 (class 2606 OID 226555)
-- Name: lsworkflow fkhu7gi01xelpckpgnfd5vxbg56; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsworkflow
    ADD CONSTRAINT fkhu7gi01xelpckpgnfd5vxbg56 FOREIGN KEY (lssitemaster_sitecode) REFERENCES lssitemaster(sitecode);


--
-- TOC entry 2711 (class 2606 OID 226330)
-- Name: lsorderworkflowhistory fkhv81jp72qs43ivpvq24uyp3xn; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsorderworkflowhistory
    ADD CONSTRAINT fkhv81jp72qs43ivpvq24uyp3xn FOREIGN KEY (createby_usercode) REFERENCES lsusermaster(usercode);


--
-- TOC entry 2700 (class 2606 OID 226275)
-- Name: lslogilablimsorderdetail fkhw7lgcjjw89mo5myqmiwnlhrk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lslogilablimsorderdetail
    ADD CONSTRAINT fkhw7lgcjjw89mo5myqmiwnlhrk FOREIGN KEY (lssamplemaster_samplecode) REFERENCES lssamplemaster(samplecode);


--
-- TOC entry 2666 (class 2606 OID 226105)
-- Name: lsactiveuser fkigl6mqurrvjcvewr6loshxk33; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsactiveuser
    ADD CONSTRAINT fkigl6mqurrvjcvewr6loshxk33 FOREIGN KEY (lssitemaster_sitecode) REFERENCES lssitemaster(sitecode);


--
-- TOC entry 2747 (class 2606 OID 226510)
-- Name: lsusergrouprights fkij5tvapohsj3d7lo41qj6mkg2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsusergrouprights
    ADD CONSTRAINT fkij5tvapohsj3d7lo41qj6mkg2 FOREIGN KEY (usergroupid_usergroupcode) REFERENCES lsusergroup(usergroupcode);


--
-- TOC entry 2737 (class 2606 OID 226460)
-- Name: lssheetworkflowgroupmap fkiuo92bmlt2daeco3j23ft0pmh; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lssheetworkflowgroupmap
    ADD CONSTRAINT fkiuo92bmlt2daeco3j23ft0pmh FOREIGN KEY (workflowcode) REFERENCES lssheetworkflow(workflowcode);


--
-- TOC entry 2754 (class 2606 OID 226545)
-- Name: lsuserteammapping fkj5vgwf1c07kgdbev7ab070tj9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsuserteammapping
    ADD CONSTRAINT fkj5vgwf1c07kgdbev7ab070tj9 FOREIGN KEY (lsusermaster_usercode) REFERENCES lsusermaster(usercode);


--
-- TOC entry 2749 (class 2606 OID 226520)
-- Name: lsusermaster fkk6iy2wrs0iv2wo31g5hatxn2w; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsusermaster
    ADD CONSTRAINT fkk6iy2wrs0iv2wo31g5hatxn2w FOREIGN KEY (lsuseractions_useractioncode) REFERENCES lsuseractions(useractioncode);


--
-- TOC entry 2751 (class 2606 OID 226530)
-- Name: lsusersteam fkkfkbfkft40rf9dhpjswkbwdlx; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsusersteam
    ADD CONSTRAINT fkkfkbfkft40rf9dhpjswkbwdlx FOREIGN KEY (createby_usercode) REFERENCES lsusermaster(usercode);


--
-- TOC entry 2672 (class 2606 OID 226135)
-- Name: lscfrreasons fkkwp5lyy42540ud7juxeghcgym; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lscfrreasons
    ADD CONSTRAINT fkkwp5lyy42540ud7juxeghcgym FOREIGN KEY (lssitemaster_sitecode) REFERENCES lssitemaster(sitecode);


--
-- TOC entry 2714 (class 2606 OID 226345)
-- Name: lsparsedparameters fkkyobqpxnm05kcf9pvwqlhxt06; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsparsedparameters
    ADD CONSTRAINT fkkyobqpxnm05kcf9pvwqlhxt06 FOREIGN KEY (batchcode) REFERENCES lslogilablimsorderdetail(batchcode);


--
-- TOC entry 2718 (class 2606 OID 226365)
-- Name: lsprojectmaster fkl27vlkn1seemxwvwrrt43nhnj; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsprojectmaster
    ADD CONSTRAINT fkl27vlkn1seemxwvwrrt43nhnj FOREIGN KEY (lssitemaster_sitecode) REFERENCES lssitemaster(sitecode);


--
-- TOC entry 2723 (class 2606 OID 226390)
-- Name: lsreviewdetails fkl6gsh56s62pv3gctriiukhi9q; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsreviewdetails
    ADD CONSTRAINT fkl6gsh56s62pv3gctriiukhi9q FOREIGN KEY (lssitemaster_sitecode) REFERENCES lssitemaster(sitecode);


--
-- TOC entry 2691 (class 2606 OID 226230)
-- Name: lsfileversion fkm9auqubrjoiv7mwobbfx8kmb0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsfileversion
    ADD CONSTRAINT fkm9auqubrjoiv7mwobbfx8kmb0 FOREIGN KEY (createby_usercode) REFERENCES lsusermaster(usercode);


--
-- TOC entry 2753 (class 2606 OID 226540)
-- Name: lsusersteam fkm9b019pp80372xbx0xdem3tt; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsusersteam
    ADD CONSTRAINT fkm9b019pp80372xbx0xdem3tt FOREIGN KEY (modifiedby_usercode) REFERENCES lsusermaster(usercode);


--
-- TOC entry 2717 (class 2606 OID 226360)
-- Name: lspasswordpolicy fkmk42loasx2peiaq0sokp1cl47; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lspasswordpolicy
    ADD CONSTRAINT fkmk42loasx2peiaq0sokp1cl47 FOREIGN KEY (lsusermaster_usercode) REFERENCES lsusermaster(usercode);


--
-- TOC entry 2755 (class 2606 OID 226550)
-- Name: lsuserteammapping fkn9bt7wnl84xxxpr272tdw1q9p; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsuserteammapping
    ADD CONSTRAINT fkn9bt7wnl84xxxpr272tdw1q9p FOREIGN KEY (teamcode) REFERENCES lsusersteam(teamcode);


--
-- TOC entry 2668 (class 2606 OID 226115)
-- Name: lsactivity fknlpilkgdv1xti49gwrykp6jbj; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsactivity
    ADD CONSTRAINT fknlpilkgdv1xti49gwrykp6jbj FOREIGN KEY (activityby_usercode) REFERENCES lsusermaster(usercode);


--
-- TOC entry 2739 (class 2606 OID 226470)
-- Name: lssheetworkflowhistory fko0vq85fq8xy2di7ce8oadcvni; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lssheetworkflowhistory
    ADD CONSTRAINT fko0vq85fq8xy2di7ce8oadcvni FOREIGN KEY (currentworkflow_workflowcode) REFERENCES lssheetworkflow(workflowcode);


--
-- TOC entry 2689 (class 2606 OID 226220)
-- Name: lsfileparameter fkoe14hfotdkcimsflx8lanxpix; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsfileparameter
    ADD CONSTRAINT fkoe14hfotdkcimsflx8lanxpix FOREIGN KEY (filecode) REFERENCES lsfile(filecode);


--
-- TOC entry 2736 (class 2606 OID 226455)
-- Name: lssheetworkflowgroupmap fkq5hlumsd6p7ospyk460c3dks4; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lssheetworkflowgroupmap
    ADD CONSTRAINT fkq5hlumsd6p7ospyk460c3dks4 FOREIGN KEY (lsusergroup_usergroupcode) REFERENCES lsusergroup(usergroupcode);


--
-- TOC entry 2752 (class 2606 OID 226535)
-- Name: lsusersteam fkq7ysimmdiiut1ma6wb2x7q2gq; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsusersteam
    ADD CONSTRAINT fkq7ysimmdiiut1ma6wb2x7q2gq FOREIGN KEY (lssitemaster_sitecode) REFERENCES lssitemaster(sitecode);


--
-- TOC entry 2684 (class 2606 OID 226195)
-- Name: lsfile fkqci4gixen9jfwdiqyfqy32ylq; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsfile
    ADD CONSTRAINT fkqci4gixen9jfwdiqyfqy32ylq FOREIGN KEY (createby_usercode) REFERENCES lsusermaster(usercode);


--
-- TOC entry 2725 (class 2606 OID 226400)
-- Name: lssamplefile fkqn1fqn5wwcf64c5h084r5vufw; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lssamplefile
    ADD CONSTRAINT fkqn1fqn5wwcf64c5h084r5vufw FOREIGN KEY (createbyuser_usercode) REFERENCES lsusermaster(usercode);


--
-- TOC entry 2680 (class 2606 OID 226175)
-- Name: lselninstrumentmaster fkqrom94fmugl8jpw3f0fv5cuo5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lselninstrumentmaster
    ADD CONSTRAINT fkqrom94fmugl8jpw3f0fv5cuo5 FOREIGN KEY (lssitemaster_sitecode) REFERENCES lssitemaster(sitecode);


--
-- TOC entry 2688 (class 2606 OID 226215)
-- Name: lsfilemethod fkqsln3ah9iegm6csuchm2n76om; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsfilemethod
    ADD CONSTRAINT fkqsln3ah9iegm6csuchm2n76om FOREIGN KEY (filecode) REFERENCES lsfile(filecode);


--
-- TOC entry 2705 (class 2606 OID 226300)
-- Name: lsmaterialmap fkqwcd2yhl2bx4jhuub5t9n525a; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsmaterialmap
    ADD CONSTRAINT fkqwcd2yhl2bx4jhuub5t9n525a FOREIGN KEY (testcode) REFERENCES lstestmasterlocal(testcode);


--
-- TOC entry 2744 (class 2606 OID 226495)
-- Name: lsunmappedtags fkqwhvhix3lxsj03wxt4xnxpykk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsunmappedtags
    ADD CONSTRAINT fkqwhvhix3lxsj03wxt4xnxpykk FOREIGN KEY (templatecode) REFERENCES lsunmappedtemplate(templatecode);


--
-- TOC entry 2741 (class 2606 OID 226480)
-- Name: lstestmasterlocal fkqwws7lt1iu0inf335qbkxt9l5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lstestmasterlocal
    ADD CONSTRAINT fkqwws7lt1iu0inf335qbkxt9l5 FOREIGN KEY (lssitemaster_sitecode) REFERENCES lssitemaster(sitecode);


--
-- TOC entry 2676 (class 2606 OID 226155)
-- Name: lselninstfieldmapping fkr2whd8h3j12jt7usq95ttb1m4; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lselninstfieldmapping
    ADD CONSTRAINT fkr2whd8h3j12jt7usq95ttb1m4 FOREIGN KEY (fieldcode) REFERENCES lselninstrumentfields(fieldcode);


--
-- TOC entry 2724 (class 2606 OID 226395)
-- Name: lsreviewdetails fkrgqwl5ytp6ychkl7s3nsmbq4a; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsreviewdetails
    ADD CONSTRAINT fkrgqwl5ytp6ychkl7s3nsmbq4a FOREIGN KEY (lsusermaster_usercode) REFERENCES lsusermaster(usercode);


--
-- TOC entry 2690 (class 2606 OID 226225)
-- Name: lsfiletest fks1rp8lpcg8ol5obwqurjw35br; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsfiletest
    ADD CONSTRAINT fks1rp8lpcg8ol5obwqurjw35br FOREIGN KEY (filecode) REFERENCES lsfile(filecode);


--
-- TOC entry 2728 (class 2606 OID 226415)
-- Name: lssamplefileversion fkskj3iq360rjhk095sgmwvof6o; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lssamplefileversion
    ADD CONSTRAINT fkskj3iq360rjhk095sgmwvof6o FOREIGN KEY (filesamplecode_filesamplecode) REFERENCES lssamplefile(filesamplecode);


--
-- TOC entry 2722 (class 2606 OID 226385)
-- Name: lsreviewdetails fksqo9wr51a8wdh04nq4625530u; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lsreviewdetails
    ADD CONSTRAINT fksqo9wr51a8wdh04nq4625530u FOREIGN KEY (lsreviewdetails_usercode) REFERENCES lsusermaster(usercode);


--
-- TOC entry 2701 (class 2606 OID 226280)
-- Name: lslogilablimsorderdetail fkt356x9359k0tecr2pjv43c0lr; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lslogilablimsorderdetail
    ADD CONSTRAINT fkt356x9359k0tecr2pjv43c0lr FOREIGN KEY (lsusermaster_usercode) REFERENCES lsusermaster(usercode);


-- Completed on 2021-02-11 12:06:31

--
-- PostgreSQL database dump complete
--

