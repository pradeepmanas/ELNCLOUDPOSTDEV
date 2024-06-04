
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