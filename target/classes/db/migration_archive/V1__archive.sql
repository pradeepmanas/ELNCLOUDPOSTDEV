
CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS public.lscfrarchivehistory
(
    archivecode integer NOT NULL,
    archivedate timestamp without time zone,
    archiveusercode integer NOT NULL,
    archiveusername character varying(255) COLLATE pg_catalog."default",
    discription character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT lscfrarchivehistory_pkey PRIMARY KEY (archivecode)
);

CREATE TABLE IF NOT EXISTS public.archivetransactions
(
    serialno integer NOT NULL,
    actions text COLLATE pg_catalog."default",
    affectedclientid character varying(100) COLLATE pg_catalog."default",
    comments text COLLATE pg_catalog."default",
    instrumentid character varying(100) COLLATE pg_catalog."default",
    manipulatetype character varying(100) COLLATE pg_catalog."default",
    modifieddata character varying(100) COLLATE pg_catalog."default",
    modulename text COLLATE pg_catalog."default",
    reason character varying(100) COLLATE pg_catalog."default",
    requestedclientid character varying(100) COLLATE pg_catalog."default",
    reviewedstatus character varying(100) COLLATE pg_catalog."default",
    systemcoments character varying(100) COLLATE pg_catalog."default",
    tablename character varying(100) COLLATE pg_catalog."default",
    transactiondate timestamp without time zone,
    username character varying(255) COLLATE pg_catalog."default",
    lscfrarchivehistory_archivecode integer,
    CONSTRAINT archivetransactions_pkey PRIMARY KEY (serialno),
    CONSTRAINT fkgnkxyqb828u7uc4onvkymaa7p FOREIGN KEY (lscfrarchivehistory_archivecode)
        REFERENCES public.lscfrarchivehistory (archivecode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


CREATE TABLE IF NOT EXISTS public.datasourceconfig
(
    id bigint NOT NULL,
    archivename character varying(255) COLLATE pg_catalog."default",
    archiveurl character varying(255) COLLATE pg_catalog."default",
    driverclassname character varying(255) COLLATE pg_catalog."default",
    initialize boolean NOT NULL,
    isenable boolean NOT NULL,
    name character varying(255) COLLATE pg_catalog."default",
    password character varying(255) COLLATE pg_catalog."default",
    tenantaddress character varying(255) COLLATE pg_catalog."default",
    tenantcity character varying(255) COLLATE pg_catalog."default",
    tenantcontactno character varying(255) COLLATE pg_catalog."default",
    tenantcountry character varying(255) COLLATE pg_catalog."default",
    tenantid character varying(255) COLLATE pg_catalog."default",
    tenantname character varying(255) COLLATE pg_catalog."default",
    tenantpincode character varying(255) COLLATE pg_catalog."default",
    tenantstate character varying(255) COLLATE pg_catalog."default",
    url character varying(255) COLLATE pg_catalog."default",
    username character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT datasourceconfig_pkey PRIMARY KEY (id)
);



