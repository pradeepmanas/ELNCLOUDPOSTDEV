
ALTER TABLE IF Exists LSlogilablimsorderdetail ALTER COLUMN  batchcode SET DEFAULT nextval('orderDetail');

DO
$do$
DECLARE
    _kind "char";
    _max_regcode INT;
BEGIN
    -- Check if the sequence exists
    SELECT relkind
    INTO   _kind
    FROM   pg_class
    WHERE  relname = 'lsautoregister_seq';

    IF NOT FOUND THEN
        -- Get the maximum regcode value
        SELECT COALESCE(MAX(regcode), 0) + 100
        INTO   _max_regcode
        FROM   lsautoregister;

        -- Create the sequence starting from the max regcode value
        EXECUTE format('CREATE SEQUENCE lsautoregister_seq START WITH %s', _max_regcode);
    ELSIF _kind = 'S' THEN  
        -- Sequence exists, do nothing
        RAISE NOTICE 'Sequence already exists, doing nothing.';
    ELSE             
        -- Something else with the same name exists, handle it appropriately
        RAISE EXCEPTION 'A non-sequence object with the name "lsautoregister_seq" already exists.';
    END IF;
END
$do$;

ALTER TABLE IF Exists LSlogilabprotocoldetail ALTER COLUMN  protocolordercode SET DEFAULT nextval('orderDetail');
--ALTER SEQUENCE protocolordercode INCREMENT BY 1;

DO
$do$
DECLARE
    _kind "char";
    _max_serialno INT;
BEGIN
    -- Check if the sequence exists
    SELECT relkind
    INTO   _kind
    FROM   pg_class
    WHERE  relname = 'lscfttransaction_sequence';

    IF NOT FOUND THEN
        -- Get the maximum serialno value
        SELECT COALESCE(MAX(serialno), 0) + 100
        INTO   _max_serialno
        FROM   lscfttransaction;

        -- Create the sequence starting from the max serialno value
        EXECUTE format('CREATE SEQUENCE lscfttransaction_sequence START WITH %s', _max_serialno);
    ELSIF _kind = 'S' THEN  
        -- Sequence exists, do nothing
        RAISE NOTICE 'Sequence already exists, doing nothing.';
    ELSE             
        -- Something else with the same name exists, handle it appropriately
        RAISE EXCEPTION 'A non-sequence object with the name "lscfttransaction_sequence" already exists.';
    END IF;
END
$do$;

ALTER TABLE lscfttransaction ALTER COLUMN serialno SET DEFAULT nextval('lscfttransaction_sequence');

ALTER TABLE IF Exists LSSheetOrderStructure ADD Column IF NOT EXISTS parentcodeondefaultfoder double precision;

ALTER TABLE IF Exists Lsprotocolorderstructure ADD Column IF NOT EXISTS parentcodeondefaultfoder double precision;


ALTER TABLE IF Exists LSSheetOrderStructure ADD Column IF NOT EXISTS foldermapping character varying(255) COLLATE pg_catalog."default";

ALTER TABLE IF Exists Lsprotocolorderstructure ADD Column IF NOT EXISTS foldermapping character varying(255) COLLATE pg_catalog."default";


DO
$do$
DECLARE
    _kind "char";
    _max_filesamplecodeversion INT;
BEGIN
    -- Check if the sequence exists
    SELECT relkind
    INTO   _kind
    FROM   pg_class
    WHERE  relname = 'lssamplefileversion_seq';

    IF NOT FOUND THEN
        -- Get the maximum filesamplecodeversion value
        SELECT COALESCE(MAX(filesamplecodeversion), 0) + 100
        INTO   _max_filesamplecodeversion
        FROM   lssamplefileversion;

        -- Create the sequence starting from the max filesamplecodeversion value
        EXECUTE format('CREATE SEQUENCE lssamplefileversion_seq START WITH %s', _max_filesamplecodeversion);
    ELSIF _kind = 'S' THEN  
        -- Sequence exists, do nothing
        RAISE NOTICE 'Sequence already exists, doing nothing.';
    ELSE             
        -- Something else with the same name exists, handle it appropriately
        RAISE EXCEPTION 'A non-sequence object with the name "lssamplefileversion_seq" already exists.';
    END IF;
END
$do$;

ALTER TABLE IF Exists lssamplefileversion ALTER COLUMN  filesamplecodeversion SET DEFAULT nextval('lssamplefileversion_seq');

DO
$do$
DECLARE
    _kind "char";
    _max_filesamplecode INT;
BEGIN
    -- Check if the sequence exists
    SELECT relkind
    INTO   _kind
    FROM   pg_class
    WHERE  relname = 'lssamplefile_seq';

    IF NOT FOUND THEN
        -- Get the maximum filesamplecode value
        SELECT COALESCE(MAX(filesamplecode), 0) + 100
        INTO   _max_filesamplecode
        FROM   lssamplefile;

        -- Create the sequence starting from the max filesamplecode value
        EXECUTE format('CREATE SEQUENCE lssamplefile_seq START WITH %s', _max_filesamplecode);
    ELSIF _kind = 'S' THEN  
        -- Sequence exists, do nothing
        RAISE NOTICE 'Sequence already exists, doing nothing.';
    ELSE             
        -- Something else with the same name exists, handle it appropriately
        RAISE EXCEPTION 'A non-sequence object with the name "lssamplefile_seq" already exists.';
    END IF;
END
$do$;

ALTER TABLE IF Exists lssamplefile ALTER COLUMN  filesamplecode SET DEFAULT nextval('lssamplefile_seq');

DO
$do$
DECLARE
    _kind "char";
    _max_notificationcode INT;
    _max_batchcode INT;
    _start_value INT;
BEGIN
    -- Check if the sequence exists
    SELECT relkind INTO _kind FROM   pg_class WHERE  relname = 'orderDetailProtocol';

    IF NOT FOUND THEN
        -- Get the maximum notificationcode value
        SELECT COALESCE(MAX(protocolordercode), 0) INTO   _max_notificationcode FROM   lslogilabprotocoldetail;
        SELECT COALESCE(MAX(batchcode), 0) INTO   _max_batchcode FROM lslogilablimsorderdetail;

        -- Determine the starting value for the sequence
        _start_value := GREATEST(_max_notificationcode, _max_batchcode) + 1;

        -- Create the sequence starting from the max notificationcode value
        EXECUTE format('CREATE SEQUENCE orderDetailProtocol START WITH %s', _start_value);
    ELSIF _kind = 'S' THEN  
        -- Sequence exists, do nothing
        RAISE NOTICE 'Sequence already exists, doing nothing.';
    ELSE             
        -- Something else with the same name exists, handle it appropriately
        RAISE EXCEPTION 'A non-sequence object with the name "orderDetailProtocol" already exists.';
    END IF;
END
$do$;

ALTER TABLE lslogilabprotocoldetail ALTER COLUMN protocolordercode SET DEFAULT nextval('orderDetailProtocol');