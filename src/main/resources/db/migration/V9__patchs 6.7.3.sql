
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
    _kind CHAR;
    _max_notificationcode INT;
    _max_batchcode INT;
    _start_value INT;
BEGIN
    -- Check if the sequence exists
    SELECT relkind INTO _kind 
    FROM pg_class 
    WHERE relname = 'orderDetailProtocol';

    IF NOT FOUND THEN
        -- Get the maximum notificationcode value
        SELECT COALESCE(MAX(protocolordercode), 0) INTO _max_notificationcode 
        FROM lslogilabprotocoldetail;

        -- Get the maximum batchcode value
        SELECT COALESCE(MAX(batchcode), 0) INTO _max_batchcode 
        FROM lslogilablimsorderdetail;

        -- Determine the starting value for the sequence
        IF GREATEST(_max_notificationcode, _max_batchcode) + 1 = 1 THEN
            _start_value := 1000000;
        ELSE
            _start_value := GREATEST(_max_notificationcode, _max_batchcode) + 1;
        END IF;

        -- Create the sequence starting from the max notificationcode value
        EXECUTE format('CREATE SEQUENCE orderDetailProtocol START WITH %s', _start_value);
    ELSIF _kind = 'S' THEN
        -- Sequence exists, do nothing
        RAISE NOTICE 'Sequence already exists, doing nothing.';
    ELSE
        -- Something else with the same name exists, handle it appropriately
        RAISE EXCEPTION 'A non-sequence object with the name "orderDetailProtocol" already exists.';
    END IF;
EXCEPTION
    WHEN duplicate_table THEN
        RAISE NOTICE 'Sequence orderDetailProtocol already exists, skipping creation.';
END
$do$;

-- Set the default value for protocolordercode to use the new sequence
ALTER TABLE lslogilabprotocoldetail ALTER COLUMN protocolordercode SET DEFAULT nextval('orderDetailProtocol');

ALTER TABLE IF Exists LSlogilablimsorderdetail ADD COLUMN IF NOT EXISTS autoregistercount integer;

ALTER TABLE IF Exists lslogilabprotocoldetail ADD COLUMN IF NOT EXISTS autoregistercount integer;

ALTER TABLE IF Exists lslogilablimsorderdetail ALTER COLUMN approvelaccept TYPE character varying;

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename,screenname, sallow, screate,sdelete, sedit, status,sequenceorder) VALUES (126, 'IDS_TSK_EQUIPMENTTYPE', 'IDS_MDL_INVENTORY', 'IDS_SCN_EQUIPMENT','0', '0', 'NA', 'NA', '0,0,0',82) ON CONFLICT(orderno)DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 82 AND orderno <> 126;
    END IF;
END $$;

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename,screenname, sallow, screate,sdelete, sedit, status,sequenceorder) VALUES (127, 'IDS_SCN_EQUIPMENTCATEGORY', 'IDS_MDL_INVENTORY', 'IDS_SCN_EQUIPMENT','0', '0', '0', '0', '0,0,0',83) ON CONFLICT(orderno)DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 83 AND orderno <> 127;
    END IF;
END $$;

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT into lsusergrouprightsmaster(orderno, displaytopic, modulename,screenname, sallow, screate,sdelete, sedit, status,sequenceorder) VALUES (128, 'IDS_TSK_EQUIPMENTMASTER', 'IDS_MDL_INVENTORY', 'IDS_SCN_EQUIPMENTMASTER','0', '0', '0', '0', '0,0,0',84) ON CONFLICT(orderno)DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 84 AND orderno <> 128;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_EQUIPMENTTYPE', 'IDS_MDL_INVENTORY', 'administrator', '1', '1', 'NA', 'NA', 1,1,'IDS_SCN_EQUIPMENT'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_EQUIPMENTTYPE' and screenname='IDS_SCN_EQUIPMENT' and usergroupid_usergroupcode = 1); 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_SCN_EQUIPMENTCATEGORY', 'IDS_MDL_INVENTORY', 'administrator', '1', '1', '1', '1', 1,1,'IDS_SCN_EQUIPMENT'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_SCN_EQUIPMENTCATEGORY' and screenname='IDS_SCN_EQUIPMENT' and usergroupid_usergroupcode = 1); 
INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_EQUIPMENTMASTER', 'IDS_MDL_INVENTORY', 'administrator', '1', '1', '1', '1', 1,1,'IDS_SCN_EQUIPMENTMASTER'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_EQUIPMENTMASTER' and screenname='IDS_SCN_EQUIPMENTMASTER' and usergroupid_usergroupcode = 1); 

--update lsusergrouprightsmaster set screenname = 'IDS_SCN_INVENTORY' where screenname != 'IDS_SCN_EQUIPMENT' and screenname != 'IDS_SCN_EQUIPMENTMASTER' and modulename = 'IDS_MDL_INVENTORY';
--update lsusergrouprights set screenname = 'IDS_SCN_INVENTORY' where screenname != 'IDS_SCN_EQUIPMENT' and screenname != 'IDS_SCN_EQUIPMENTMASTER' and modulename = 'IDS_MDL_INVENTORY';

update lsusergrouprightsmaster set screenname = 'IDS_SCN_MATERIAL' where displaytopic = 'IDS_SCN_MATERIAL';
update lsusergrouprightsmaster set screenname = 'IDS_SCN_MATERIALCATEGORY' where displaytopic = 'IDS_SCN_MATERIALCATEGORY';
update lsusergrouprightsmaster set screenname = 'IDS_SCN_STORAGELOCATION' where displaytopic = 'IDS_SCN_STORAGELOCATION';
update lsusergrouprightsmaster set screenname = 'IDS_SCN_SECTIONMASTER' where displaytopic = 'IDS_SCN_SECTIONMASTER';
update lsusergrouprightsmaster set screenname = 'IDS_SCN_UNITMASTER' where displaytopic = 'IDS_SCN_UNITMASTER';
update lsusergrouprightsmaster set screenname = 'IDS_SCN_MATERIALTYPE' where displaytopic = 'IDS_SCN_MATERIALTYPE';
update lsusergrouprightsmaster set screenname = 'IDS_SCN_MATERIALINVENTORY' where displaytopic = 'IDS_SCN_MATERIALINVENTORY';
update lsusergrouprightsmaster set screenname = 'IDS_SCN_GRADEMASTER' where displaytopic = 'IDS_SCN_GRADEMASTER';
update lsusergrouprightsmaster set screenname = 'IDS_SCN_SUPPLIER' where displaytopic = 'IDS_SCN_SUPPLIER';
update lsusergrouprightsmaster set screenname = 'IDS_SCN_MANUFACTURER' where displaytopic = 'IDS_SCN_MANUFACTURER';

update lsusergrouprights set screenname = 'IDS_SCN_MATERIAL' where displaytopic = 'IDS_SCN_MATERIAL';
update lsusergrouprights set screenname = 'IDS_SCN_MATERIALCATEGORY' where displaytopic = 'IDS_SCN_MATERIALCATEGORY';
update lsusergrouprights set screenname = 'IDS_SCN_STORAGELOCATION' where displaytopic = 'IDS_SCN_STORAGELOCATION';
update lsusergrouprights set screenname = 'IDS_SCN_SECTIONMASTER' where displaytopic = 'IDS_SCN_SECTIONMASTER';
update lsusergrouprights set screenname = 'IDS_SCN_UNITMASTER' where displaytopic = 'IDS_SCN_UNITMASTER';
update lsusergrouprights set screenname = 'IDS_SCN_MATERIALTYPE' where displaytopic = 'IDS_SCN_MATERIALTYPE';
update lsusergrouprights set screenname = 'IDS_SCN_MATERIALINVENTORY' where displaytopic = 'IDS_SCN_MATERIALINVENTORY';
update lsusergrouprights set screenname = 'IDS_SCN_GRADEMASTER' where displaytopic = 'IDS_SCN_GRADEMASTER';
update lsusergrouprights set screenname = 'IDS_SCN_SUPPLIER' where displaytopic = 'IDS_SCN_SUPPLIER';
update lsusergrouprights set screenname = 'IDS_SCN_MANUFACTURER' where displaytopic = 'IDS_SCN_MANUFACTURER';

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (133, 'IDS_TSK_ACTIVIWIDGET', 'IDS_MDL_DASHBOARD', 'IDS_SCN_DASHBOARD', '0', 'NA', 'NA', 'NA', '0,0,0', 4) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 4 AND orderno <> 133;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_ACTIVIWIDGET', 'IDS_MDL_DASHBOARD', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_DASHBOARD'  WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_ACTIVIWIDGET' and screenname='IDS_SCN_DASHBOARD' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (134, 'IDS_TSK_CANCELORDER', 'IDS_MDL_ORDERS', 'IDS_SCN_SHEETORDERS', '0', 'NA', 'NA', 'NA', '0,0,0', 6) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 6 AND orderno <> 134;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_CANCELORDER', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_SHEETORDERS' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_CANCELORDER' and screenname='IDS_SCN_SHEETORDERS' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (135, 'IDS_TSK_AUTOREGORDER', 'IDS_MDL_ORDERS', 'IDS_SCN_SHEETORDERS', '0', 'NA', 'NA', 'NA', '0,0,0', 7) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 7 AND orderno <> 135;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_AUTOREGORDER', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_SHEETORDERS' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_AUTOREGORDER' and screenname='IDS_SCN_SHEETORDERS' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (136, 'IDS_TSK_DUEDATE', 'IDS_MDL_ORDERS', 'IDS_SCN_SHEETORDERS', '0', 'NA', 'NA', 'NA', '0,0,0', 8) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 8 AND orderno <> 136;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_DUEDATE', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_SHEETORDERS' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_DUEDATE' and screenname='IDS_SCN_SHEETORDERS' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (137, 'IDS_TSK_NEWFOLDER', 'IDS_MDL_ORDERS', 'IDS_SCN_SHEETORDERS', '0', 'NA', 'NA', 'NA', '0,0,0', 9) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 9 AND orderno <> 137;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_NEWFOLDER', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_SHEETORDERS' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_NEWFOLDER' and screenname='IDS_SCN_SHEETORDERS' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (138, 'IDS_TSK_CANCELORDER', 'IDS_MDL_ORDERS', 'IDS_SCN_PROTOCOLORDERS', '0', 'NA', 'NA', 'NA', '0,0,0', 23) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 23 AND orderno <> 138;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_CANCELORDER', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLORDERS' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_CANCELORDER' and screenname='IDS_SCN_PROTOCOLORDERS' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (139, 'IDS_TSK_AUTOREGORDER', 'IDS_MDL_ORDERS', 'IDS_SCN_PROTOCOLORDERS', '0', 'NA', 'NA', 'NA', '0,0,0', 24) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 24 AND orderno <> 139;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_AUTOREGORDER', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLORDERS' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_AUTOREGORDER' and screenname='IDS_SCN_PROTOCOLORDERS' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (140, 'IDS_TSK_DUEDATE', 'IDS_MDL_ORDERS', 'IDS_SCN_PROTOCOLORDERS', '0', 'NA', 'NA', 'NA', '0,0,0', 25) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 25 AND orderno <> 140;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_DUEDATE', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLORDERS' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_DUEDATE' and screenname='IDS_SCN_PROTOCOLORDERS' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (141, 'IDS_TSK_SHARE', 'IDS_MDL_ORDERS', 'IDS_SCN_PROTOCOLORDERS', '0', 'NA', 'NA', 'NA', '0,0,0', 26) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 26 AND orderno <> 141;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_SHARE', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLORDERS' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_SHARE' and screenname='IDS_SCN_PROTOCOLORDERS' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (142, 'IDS_TSK_COPYLINK', 'IDS_MDL_ORDERS', 'IDS_SCN_PROTOCOLORDERS', '0', 'NA', 'NA', 'NA', '0,0,0', 27) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 27 AND orderno <> 142;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_COPYLINK', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLORDERS' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_COPYLINK' and screenname='IDS_SCN_PROTOCOLORDERS' and usergroupid_usergroupcode = 1); 

update lsusergrouprightsmaster set  screate='0',sedit = '0', sdelete = 'NA' where displaytopic = 'IDS_TSK_FOLDERCREATIONPROTOCOL' and screate ='NA';
update lsusergrouprights set  screate='0',sedit = '0', sdelete = 'NA' where displaytopic = 'IDS_TSK_FOLDERCREATIONPROTOCOL' and screate ='NA';

update lsusergrouprightsmaster set  screate='0',sedit = '0', sdelete = 'NA' where displaytopic = 'IDS_TSK_FOLDERCREATION' and screate ='NA';
update lsusergrouprights set  screate='0',sedit = '0', sdelete = 'NA' where displaytopic = 'IDS_TSK_FOLDERCREATION' and screate ='NA';

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (143, 'IDS_TSK_SHARE', 'IDS_MDL_ORDERS', 'IDS_SCN_SHEETORDERS', '0', 'NA', 'NA', 'NA', '0,0,0', 10) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 10 AND orderno <> 143;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_SHARE', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_SHEETORDERS' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_SHARE' and screenname='IDS_SCN_SHEETORDERS' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (144, 'IDS_TSK_COPYLINK', 'IDS_MDL_ORDERS', 'IDS_SCN_SHEETORDERS', '0', 'NA', 'NA', 'NA', '0,0,0', 11) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 11 AND orderno <> 144;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_COPYLINK', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_SHEETORDERS' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_COPYLINK' and screenname='IDS_SCN_SHEETORDERS' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (145, 'IDS_TSK_FOLDERDEL', 'IDS_MDL_ORDERS', 'IDS_SCN_PROTOCOLORDERS', '0', 'NA', 'NA', 'NA', '0,0,0', 28) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 28 AND orderno <> 145;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_FOLDERDEL', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLORDERS' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_FOLDERDEL' and screenname='IDS_SCN_PROTOCOLORDERS' and usergroupid_usergroupcode = 1); 


DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (146, 'IDS_TSK_FOLDERDEL', 'IDS_MDL_ORDERS', 'IDS_SCN_SHEETORDERS', '0', 'NA', 'NA', 'NA', '0,0,0', 12) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 12 AND orderno <> 146;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_FOLDERDEL', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_SHEETORDERS' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_FOLDERDEL' and screenname='IDS_SCN_SHEETORDERS' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (147, 'IDS_TSK_ADDSEC', 'IDS_MDL_TEMPLATES', 'IDS_SCN_PROTOCOLTEMPLATE', '0', 'NA', 'NA', 'NA', '0,0,0', 48) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 48 AND orderno <> 147;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_ADDSEC', 'IDS_MDL_TEMPLATES', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLTEMPLATE' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_ADDSEC' and screenname='IDS_SCN_PROTOCOLTEMPLATE' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (148, 'IDS_TSK_ADDRESULT', 'IDS_MDL_TEMPLATES', 'IDS_SCN_PROTOCOLTEMPLATE', '0', 'NA', 'NA', 'NA', '0,0,0', 49) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 49 AND orderno <> 148;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_ADDRESULT', 'IDS_MDL_TEMPLATES', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLTEMPLATE' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_ADDRESULT' and screenname='IDS_SCN_PROTOCOLTEMPLATE' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (149, 'IDS_TSK_ADDREFF', 'IDS_MDL_TEMPLATES', 'IDS_SCN_PROTOCOLTEMPLATE', '0', 'NA', 'NA', 'NA', '0,0,0', 50) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 50 AND orderno <> 149;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_ADDREFF', 'IDS_MDL_TEMPLATES', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLTEMPLATE' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_ADDREFF' and screenname='IDS_SCN_PROTOCOLTEMPLATE' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (150, 'IDS_TSK_ADDRESMATERIAL', 'IDS_MDL_TEMPLATES', 'IDS_SCN_PROTOCOLTEMPLATE', '0', 'NA', 'NA', 'NA', '0,0,0', 51) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 51 AND orderno <> 150;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_ADDRESMATERIAL', 'IDS_MDL_TEMPLATES', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLTEMPLATE' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_ADDRESMATERIAL' and screenname='IDS_SCN_PROTOCOLTEMPLATE' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (151, 'IDS_TSK_ADDRESEQUIPMENT', 'IDS_MDL_TEMPLATES', 'IDS_SCN_PROTOCOLTEMPLATE', '0', 'NA', 'NA', 'NA', '0,0,0', 52) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 52 AND orderno <> 151;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_ADDRESEQUIPMENT', 'IDS_MDL_TEMPLATES', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLTEMPLATE' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_ADDRESEQUIPMENT' and screenname='IDS_SCN_PROTOCOLTEMPLATE' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (152, 'IDS_TSK_ADDSTEP', 'IDS_MDL_TEMPLATES', 'IDS_SCN_PROTOCOLTEMPLATE', '0', 'NA', 'NA', 'NA', '0,0,0', 53) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 53 AND orderno <> 152;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_ADDSTEP', 'IDS_MDL_TEMPLATES', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLTEMPLATE' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_ADDSTEP' and screenname='IDS_SCN_PROTOCOLTEMPLATE' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (153, 'IDS_TSK_ADDEDITOR', 'IDS_MDL_TEMPLATES', 'IDS_SCN_PROTOCOLTEMPLATE', '0', 'NA', 'NA', 'NA', '0,0,0', 54) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 54 AND orderno <> 153;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_ADDEDITOR', 'IDS_MDL_TEMPLATES', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLTEMPLATE' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_ADDEDITOR' and screenname='IDS_SCN_PROTOCOLTEMPLATE' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (154, 'IDS_TSK_IMPORTJSON', 'IDS_MDL_TEMPLATES', 'IDS_SCN_PROTOCOLTEMPLATE', '0', 'NA', 'NA', 'NA', '0,0,0', 55) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 55 AND orderno <> 154;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_IMPORTJSON', 'IDS_MDL_TEMPLATES', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLTEMPLATE' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_IMPORTJSON' and screenname='IDS_SCN_PROTOCOLTEMPLATE' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (155, 'IDS_TSK_EXPORTJSON', 'IDS_MDL_TEMPLATES', 'IDS_SCN_PROTOCOLTEMPLATE', '0', 'NA', 'NA', 'NA', '0,0,0', 56) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 56 AND orderno <> 155;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_EXPORTJSON', 'IDS_MDL_TEMPLATES', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLTEMPLATE' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_EXPORTJSON' and screenname='IDS_SCN_PROTOCOLTEMPLATE' and usergroupid_usergroupcode = 1); 


DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (156, 'IDS_TSK_ADDRESULT', 'IDS_MDL_ORDERS', 'IDS_SCN_PROTOCOLORDERS', '0', 'NA', 'NA', 'NA', '0,0,0', 29) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 29 AND orderno <> 156;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_ADDRESULT', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLORDERS' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_ADDRESULT' and screenname='IDS_SCN_PROTOCOLORDERS' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (157, 'IDS_TSK_ADDREFF', 'IDS_MDL_ORDERS', 'IDS_SCN_PROTOCOLORDERS', '0', 'NA', 'NA', 'NA', '0,0,0', 30) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 30 AND orderno <> 157;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_ADDREFF', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLORDERS' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_ADDREFF' and screenname='IDS_SCN_PROTOCOLORDERS' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (158, 'IDS_TSK_ADDRESMATERIAL', 'IDS_MDL_ORDERS', 'IDS_SCN_PROTOCOLORDERS', '0', 'NA', 'NA', 'NA', '0,0,0', 31) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 31 AND orderno <> 158;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_ADDRESMATERIAL', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLORDERS' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_ADDRESMATERIAL' and screenname='IDS_SCN_PROTOCOLORDERS' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (159, 'IDS_TSK_ADDRESEQUIPMENT', 'IDS_MDL_ORDERS', 'IDS_SCN_PROTOCOLORDERS', '0', 'NA', 'NA', 'NA', '0,0,0', 32) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 32 AND orderno <> 159;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_ADDRESEQUIPMENT', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLORDERS' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_ADDRESEQUIPMENT' and screenname='IDS_SCN_PROTOCOLORDERS' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (160, 'IDS_TSK_ADDSTEP', 'IDS_MDL_ORDERS', 'IDS_SCN_PROTOCOLORDERS', '0', 'NA', 'NA', 'NA', '0,0,0', 33) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 33 AND orderno <> 160;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_ADDSTEP', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLORDERS' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_ADDSTEP' and screenname='IDS_SCN_PROTOCOLORDERS' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (161, 'IDS_TSK_ADDEDITOR', 'IDS_MDL_ORDERS', 'IDS_SCN_PROTOCOLORDERS', '0', 'NA', 'NA', 'NA', '0,0,0', 34) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 34 AND orderno <> 161;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_ADDEDITOR', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLORDERS' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_ADDEDITOR' and screenname='IDS_SCN_PROTOCOLORDERS' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (162, 'IDS_TSK_EXPORTJSON', 'IDS_MDL_ORDERS', 'IDS_SCN_PROTOCOLORDERS', '0', 'NA', 'NA', 'NA', '0,0,0', 35) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 35 AND orderno <> 162;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_EXPORTJSON', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLORDERS' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_EXPORTJSON' and screenname='IDS_SCN_PROTOCOLORDERS' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (163, 'IDS_TSK_ADDSEC', 'IDS_MDL_ORDERS', 'IDS_SCN_PROTOCOLORDERS', '0', 'NA', 'NA', 'NA', '0,0,0', 36) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 36 AND orderno <> 163;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_ADDSEC', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLORDERS' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_ADDSEC' and screenname='IDS_SCN_PROTOCOLORDERS' and usergroupid_usergroupcode = 1); 

ALTER TABLE IF Exists lsautoregister ADD Column IF NOT EXISTS stoptime timestamp without time zone;

CREATE OR REPLACE FUNCTION insert_audittrail(
    p_serialno INT,
    p_manualaudittrail INT,
    p_modulename TEXT,
    p_screenname TEXT,
    p_taskname TEXT
) RETURNS VOID AS $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsaudittrailconfigmaster (serialno, manualaudittrail, modulename, ordersequnce, screenname, taskname) 
    VALUES (p_serialno, p_manualaudittrail, p_modulename, 1, p_screenname, p_taskname) ON CONFLICT (serialno) DO NOTHING;

END $$ LANGUAGE plpgsql;


DO $$
BEGIN
    -- Insert multiple records using the insert_audittrail function
    PERFORM insert_audittrail(205,0,'IDS_MDL_ORDERS','IDS_SCN_PROTOCOLORDERS','IDS_TSK_CANCELORDER');
    PERFORM insert_audittrail(206,0,'IDS_MDL_ORDERS','IDS_SCN_PROTOCOLORDERS','IDS_TSK_AUTOREGORDERCANCEL');
    PERFORM insert_audittrail(207,0,'IDS_MDL_ORDERS','IDS_SCN_PROTOCOLORDERS','IDS_TSK_FOLDERDEL');
    PERFORM insert_audittrail(208,0,'IDS_MDL_ORDERS','IDS_SCN_SHEETORDERS','IDS_TSK_FOLDERDEL');
    PERFORM insert_audittrail(209,0,'IDS_MDL_ORDERS','IDS_SCN_SHEETORDERS','IDS_TSK_CANCELORDER');
    PERFORM insert_audittrail(210,0,'IDS_MDL_ORDERS','IDS_SCN_SHEETORDERS','IDS_TSK_AUTOREGORDERCANCEL');
END $$;

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (164, 'IDS_TSK_ACTDEACT', 'IDS_MDL_INVENTORY', 'IDS_TSK_EQUIPMENTMASTER', '0', 'NA', 'NA', 'NA', '0,0,0', 115) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 115 AND orderno <> 164;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_ACTDEACT', 'IDS_MDL_INVENTORY', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_TSK_EQUIPMENTMASTER' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_ACTDEACT' and screenname='IDS_TSK_EQUIPMENTMASTER' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (165, 'IDS_TSK_CALIBRATE', 'IDS_MDL_INVENTORY', 'IDS_TSK_EQUIPMENTMASTER', '0', 'NA', 'NA', 'NA', '0,0,0', 116) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 116 AND orderno <> 165;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_CALIBRATE', 'IDS_MDL_INVENTORY', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_TSK_EQUIPMENTMASTER' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_CALIBRATE' and screenname='IDS_TSK_EQUIPMENTMASTER' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (166, 'IDS_TSK_MAINTANN', 'IDS_MDL_INVENTORY', 'IDS_TSK_EQUIPMENTMASTER', '0', 'NA', 'NA', 'NA', '0,0,0', 117) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 117 AND orderno <> 166;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_MAINTANN', 'IDS_MDL_INVENTORY', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_TSK_EQUIPMENTMASTER' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_MAINTANN' and screenname='IDS_TSK_EQUIPMENTMASTER' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (167, 'IDS_SCN_MATERIALTYPEPARAMS', 'IDS_MDL_INVENTORY', 'IDS_SCN_MATERIALTYPEPARAMS', '0', 'NA', 'NA', 'NA', '0,0,0', 112) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 112 AND orderno <> 167;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_SCN_MATERIALTYPEPARAMS', 'IDS_MDL_INVENTORY', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_MATERIALTYPEPARAMS' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_SCN_MATERIALTYPEPARAMS' and screenname='IDS_SCN_MATERIALTYPEPARAMS' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (168, 'IDS_SCN_SUPPLIER', 'IDS_MDL_INVENTORY', 'IDS_SCN_SUPPLIER', '0', '0', '0', '0', '0,0,0', 113) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 113 AND orderno <> 168;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_SCN_SUPPLIER', 'IDS_MDL_INVENTORY', 'administrator', '1', '1', '1', '1', 1,1,'IDS_SCN_SUPPLIER' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_SCN_SUPPLIER' and screenname='IDS_SCN_SUPPLIER' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (169, 'IDS_SCN_MANUFACTURER', 'IDS_MDL_INVENTORY', 'IDS_SCN_MANUFACTURER', '0', '0', '0', '0', '0,0,0', 114) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 114 AND orderno <> 169;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_SCN_MANUFACTURER', 'IDS_MDL_INVENTORY', 'administrator', '1', '1', '1', '1', 1,1,'IDS_SCN_MANUFACTURER' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_SCN_MANUFACTURER' and screenname='IDS_SCN_MANUFACTURER' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) VALUES (170, 'IDS_SCN_GRADEMASTER', 'IDS_MDL_INVENTORY', 'IDS_SCN_GRADEMASTER', '0', '0', '0', '0', '0,0,0', 115) ON CONFLICT (orderno) DO NOTHING;

    -- Check if the record was inserted
    IF FOUND THEN
        -- If the insert was successful, update the sequenceorder of other records
        UPDATE lsusergrouprightsmaster SET sequenceorder = sequenceorder + 1 WHERE sequenceorder >= 115 AND orderno <> 170;
    END IF;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_SCN_GRADEMASTER', 'IDS_MDL_INVENTORY', 'administrator', '1', '1', '1', '1', 1,1,'IDS_SCN_GRADEMASTER' WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_SCN_GRADEMASTER' and screenname='IDS_SCN_GRADEMASTER' and usergroupid_usergroupcode = 1); 


DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder)  VALUES (171, 'IDS_TSK_SHARE', 'IDS_MDL_TEMPLATES', 'IDS_SCN_SHEETTEMPLATE', '0', 'NA', 'NA', 'NA', '0,0,0', 115) ON CONFLICT (orderno) DO NOTHING;

   
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_SHARE', 'IDS_MDL_TEMPLATES', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_SHEETTEMPLATE' 
WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_SHARE' and screenname='IDS_SCN_SHEETTEMPLATE' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) 
    VALUES (172, 'IDS_TSK_SHOWVERSION', 'IDS_MDL_TEMPLATES', 'IDS_SCN_SHEETTEMPLATE', '0', 'NA', 'NA', 'NA', '0,0,0', 115) ON CONFLICT (orderno) DO NOTHING;

   
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_SHOWVERSION', 'IDS_MDL_TEMPLATES', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_SHEETTEMPLATE' 
WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_SHOWVERSION' and screenname='IDS_SCN_SHEETTEMPLATE' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) 
    VALUES (173, 'IDS_TSK_TRANHISTORY', 'IDS_MDL_TEMPLATES', 'IDS_SCN_SHEETTEMPLATE', '0', 'NA', 'NA', 'NA', '0,0,0', 115) ON CONFLICT (orderno) DO NOTHING;

   
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_TRANHISTORY', 'IDS_MDL_TEMPLATES', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_SHEETTEMPLATE' 
WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_TRANHISTORY' and screenname='IDS_SCN_SHEETTEMPLATE' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) 
    VALUES (174, 'IDS_TSK_SHARE', 'IDS_MDL_TEMPLATES', 'IDS_SCN_PROTOCOLTEMPLATE', '0', 'NA', 'NA', 'NA', '0,0,0', 115) ON CONFLICT (orderno) DO NOTHING;

   
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_SHARE', 'IDS_MDL_TEMPLATES', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLTEMPLATE' 
WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_SHARE' and screenname='IDS_SCN_PROTOCOLTEMPLATE' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) 
    VALUES (175, 'IDS_TSK_SHOWVERSION', 'IDS_MDL_TEMPLATES', 'IDS_SCN_PROTOCOLTEMPLATE', '0', 'NA', 'NA', 'NA', '0,0,0', 115) ON CONFLICT (orderno) DO NOTHING;

   
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_SHOWVERSION', 'IDS_MDL_TEMPLATES', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLTEMPLATE' 
WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_SHOWVERSION' and screenname='IDS_SCN_PROTOCOLTEMPLATE' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) 
    VALUES (176, 'IDS_TSK_TRANHISTORY', 'IDS_MDL_TEMPLATES', 'IDS_SCN_PROTOCOLTEMPLATE', '0', 'NA', 'NA', 'NA', '0,0,0', 115) ON CONFLICT (orderno) DO NOTHING;

   
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_TRANHISTORY', 'IDS_MDL_TEMPLATES', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLTEMPLATE' 
WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_TRANHISTORY' and screenname='IDS_SCN_PROTOCOLTEMPLATE' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) 
    VALUES (177, 'IDS_TSK_SHARE', 'IDS_MDL_ORDERS', 'IDS_SCN_SHEETORDERS', '0', 'NA', 'NA', 'NA', '0,0,0', 115) ON CONFLICT (orderno) DO NOTHING;

   
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_SHARE', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_SHEETORDERS' 
WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_SHARE' and screenname='IDS_SCN_SHEETORDERS' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) 
    VALUES (178, 'IDS_TSK_SHOWVERSION', 'IDS_MDL_ORDERS', 'IDS_SCN_SHEETORDERS', '0', 'NA', 'NA', 'NA', '0,0,0', 115) ON CONFLICT (orderno) DO NOTHING;

   
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_SHOWVERSION', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_SHEETORDERS' 
WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_SHOWVERSION' and screenname='IDS_SCN_SHEETORDERS' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) 
    VALUES (179, 'IDS_TSK_TRANHISTORY', 'IDS_MDL_ORDERS', 'IDS_SCN_SHEETORDERS', '0', 'NA', 'NA', 'NA', '0,0,0', 115) ON CONFLICT (orderno) DO NOTHING;

   
END $$;

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) 
    VALUES (180, 'IDS_TSK_SHARE', 'IDS_MDL_ORDERS', 'IDS_SCN_PROTOCOLORDERS', '0', 'NA', 'NA', 'NA', '0,0,0', 115) ON CONFLICT (orderno) DO NOTHING;

   
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_SHARE', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLORDERS' 
WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_SHARE' and screenname='IDS_SCN_PROTOCOLORDERS' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) 
    VALUES (181, 'IDS_TSK_SHOWVERSION', 'IDS_MDL_ORDERS', 'IDS_SCN_PROTOCOLORDERS', '0', 'NA', 'NA', 'NA', '0,0,0', 115) ON CONFLICT (orderno) DO NOTHING;

   
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_SHOWVERSION', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLORDERS' 
WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_SHOWVERSION' and screenname='IDS_SCN_PROTOCOLORDERS' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) 
    VALUES (182, 'IDS_TSK_TRANHISTORY', 'IDS_MDL_ORDERS', 'IDS_SCN_PROTOCOLORDERS', '0', 'NA', 'NA', 'NA', '0,0,0', 115) ON CONFLICT (orderno) DO NOTHING;

   
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_TRANHISTORY', 'IDS_MDL_ORDERS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_PROTOCOLORDERS' 
WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_TRANHISTORY' and screenname='IDS_SCN_PROTOCOLORDERS' and usergroupid_usergroupcode = 1); 

ALTER TABLE IF Exists elnresultusedmaterial ADD Column IF NOT EXISTS isreturn Integer;

update elnresultusedmaterial set isreturn = 0 where isreturn is null;

--update lsusergrouprightsmaster set screate = '0',sedit= '0' where  screenname = 'IDS_SCN_MATERIALTYPEPARAMS' and displaytopic = 'IDS_SCN_MATERIALTYPEPARAMS';
--update lsusergrouprights set screate = '0',sedit= '0' where  screenname = 'IDS_SCN_MATERIALTYPEPARAMS' and displaytopic = 'IDS_SCN_MATERIALTYPEPARAMS';

ALTER TABLE IF Exists lslogilabprotocoldetail ALTER COLUMN approvelaccept TYPE character varying;
ALTER TABLE IF Exists lssitemaster DROP Column IF EXISTS destinationname;
ALTER TABLE IF Exists lsusermaster ADD Column IF NOT EXISTS designationname character varying(255);

update lsusergrouprightsmaster set screenname = 'IDS_SCN_PARSER' where modulename = 'IDS_MDL_SETUP' and displaytopic = 'IDS_SCN_DELIMITER';
update lsusergrouprightsmaster set screenname = 'IDS_SCN_PARSER' where modulename = 'IDS_MDL_SETUP' and displaytopic = 'IDS_SCN_METHODDELIMITER';
update lsusergrouprightsmaster set screenname = 'IDS_SCN_PARSER' where modulename = 'IDS_MDL_SETUP' and displaytopic = 'IDS_SCN_METHODMASTER';
update lsusergrouprightsmaster set screenname = 'IDS_SCN_PARSER' where modulename = 'IDS_MDL_SETUP' and displaytopic = 'IDS_SCN_INSTRUMENTCATEGORY';
update lsusergrouprightsmaster set screenname = 'IDS_SCN_PARSER' where modulename = 'IDS_MDL_SETUP' and displaytopic = 'IDS_SCN_INSTRUMENTMASTER';

update lsusergrouprights set screenname = 'IDS_SCN_PARSER' where modulename = 'IDS_MDL_SETUP' and displaytopic = 'IDS_SCN_DELIMITER';
update lsusergrouprights set screenname = 'IDS_SCN_PARSER' where modulename = 'IDS_MDL_SETUP' and displaytopic = 'IDS_SCN_METHODDELIMITER';
update lsusergrouprights set screenname = 'IDS_SCN_PARSER' where modulename = 'IDS_MDL_SETUP' and displaytopic = 'IDS_SCN_METHODMASTER';
update lsusergrouprights set screenname = 'IDS_SCN_PARSER' where modulename = 'IDS_MDL_SETUP' and displaytopic = 'IDS_SCN_INSTRUMENTCATEGORY';
update lsusergrouprights set screenname = 'IDS_SCN_PARSER' where modulename = 'IDS_MDL_SETUP' and displaytopic = 'IDS_SCN_INSTRUMENTMASTER';

ALTER TABLE IF Exists lsusermaster ADD Column IF NOT EXISTS edulevel character varying(255);

CREATE TABLE IF NOT EXISTS public.lsresultfortemplate
(
    id bigint NOT NULL,
    content jsonb,
    contentstored integer,
    CONSTRAINT lsresultfortemplate_pkey PRIMARY KEY (id)
)
WITH (OIDS = FALSE)TABLESPACE pg_default;

ALTER TABLE public.lsresultfortemplate OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.Lstagfortemplate
(
    id bigint NOT NULL,
    content jsonb,
    contentstored integer,
    CONSTRAINT Lstagfortemplate_pkey PRIMARY KEY (id)
)
WITH (OIDS = FALSE)TABLESPACE pg_default;

ALTER TABLE public.Lstagfortemplate OWNER to postgres;

ALTER TABLE IF Exists Reporttemplate ADD Column IF NOT EXISTS  fileuid character varying(255) COLLATE pg_catalog."default";

ALTER TABLE IF Exists Reporttemplate ADD Column IF NOT EXISTS  fileuri character varying(255) COLLATE pg_catalog."default";


ALTER TABLE IF Exists Reporttemplate ADD Column IF NOT EXISTS    containerstored integer;

ALTER TABLE IF Exists Reporttemplate ADD Column IF NOT EXISTS  fileextention character varying(255) COLLATE pg_catalog."default";

ALTER TABLE IF Exists Reporttemplate ADD Column IF NOT EXISTS   approvedby_usercode integer;

DO
$do$
DECLARE
  multiusergroupcount INTEGER := 0;
BEGIN
  SELECT count(*)
  INTO multiusergroupcount
  FROM information_schema.table_constraints 
  WHERE constraint_name = 'fkm2lrbimlxl8xpuiwsn1wjckva'
    AND table_name = 'reporttemplate';

  IF multiusergroupcount = 0 THEN
    ALTER TABLE ONLY reporttemplate
    ADD CONSTRAINT fkm2lrbimlxl8xpuiwsn1wjckva FOREIGN KEY (approvedby_usercode) REFERENCES lsusermaster (usercode);
  END IF;
END
$do$;

ALTER TABLE IF Exists Reporttemplate ADD Column IF NOT EXISTS completeddate timestamp without time zone;


 DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'reporttemplatemapping_templatemapid_seq' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE reporttemplatemapping_templatemapid_seq;
   ELSIF _kind = 'S' THEN  
     
   ELSE                  
    
   END IF;
END
$do$;


CREATE TABLE IF NOT EXISTS public.reporttemplatemapping
(
    templatemapid bigint NOT NULL DEFAULT nextval('reporttemplatemapping_templatemapid_seq'::regclass),
    templatecode bigint,
    lsusersteam_teamcode integer,
    CONSTRAINT reporttemplatemapping_pkey PRIMARY KEY (templatemapid),
    CONSTRAINT fk63td7sw75singebqfyskunwer FOREIGN KEY (lsusersteam_teamcode)
        REFERENCES public.lsusersteam (teamcode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fklr0cs90si471e6rmigg0odops FOREIGN KEY (templatecode)
        REFERENCES public.reporttemplate (templatecode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.reporttemplatemapping
    OWNER to postgres;

ALTER TABLE IF Exists lsfile ADD Column IF NOT EXISTS tagsheet integer;

update lsfile set tagsheet = 0 where tagsheet is null;

ALTER TABLE IF Exists reporttemplatemapping ADD COLUMN IF NOT EXISTS lsprojectmaster_projectcode integer;

DO
$do$
declare
  multiusergroupcount integer :=0;
begin
SELECT count(*) into multiusergroupcount FROM
information_schema.table_constraints WHERE constraint_name='fkms9ayvec5kyd981y26m8hb5pj'
AND table_name='reporttemplatemapping';
 IF multiusergroupcount =0 THEN
    ALTER TABLE ONLY reporttemplatemapping ADD CONSTRAINT fkms9ayvec5kyd981y26m8hb5pj FOREIGN KEY (lsprojectmaster_projectcode) REFERENCES lsprojectmaster (projectcode);
   END IF;
END
$do$; 

ALTER TABLE IF Exists Reporttemplate ADD Column IF NOT EXISTS versionno integer DEFAULT 1;

CREATE TABLE IF NOT EXISTS public.reporttemplateversion
(
    templateversioncode integer NOT NULL,
    createdate timestamp without time zone,
    createdby integer,
    fileuid character varying(255) COLLATE pg_catalog."default",
    fileuri character varying(255) COLLATE pg_catalog."default",
    modifieddate timestamp without time zone,
    sitecode integer,
    templatecode bigint,
    templatename character varying(255) COLLATE pg_catalog."default",
    templatetype integer,
    versionname character varying(255) COLLATE pg_catalog."default",
    versionno integer,
    CONSTRAINT reporttemplateversion_pkey PRIMARY KEY (templateversioncode)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.reporttemplateversion  OWNER to postgres;

ALTER TABLE IF Exists lsreportfile ADD COLUMN IF NOT EXISTS batchcode numeric(17,0);

ALTER TABLE IF Exists Reports ADD Column IF NOT EXISTS fileuid character varying(255) COLLATE pg_catalog."default";

ALTER TABLE IF Exists Reports ADD Column IF NOT EXISTS fileuri character varying(255) COLLATE pg_catalog."default";

ALTER TABLE IF Exists Reports ADD Column IF NOT EXISTS containerstored integer;

ALTER TABLE IF Exists Reports ADD Column IF NOT EXISTS fileextention character varying(255) COLLATE pg_catalog."default";

ALTER TABLE IF Exists Reports ADD Column IF NOT EXISTS approvedby_usercode integer;

ALTER TABLE IF Exists Reports ADD Column IF NOT EXISTS versionno integer;

ALTER TABLE IF Exists Reports ADD Column IF NOT EXISTS completeddate timestamp without time zone;

CREATE TABLE IF NOT EXISTS ReportsVersion (
    reportversioncode integer NOT NULL,
    reportcode bigint,
    versionno integer,
    datecreated timestamp without time zone,
    datemodified timestamp without time zone,
    fileuid character varying(255) COLLATE pg_catalog."default",
    fileuri character varying(255) COLLATE pg_catalog."default",
    containerstored integer,
    fileextention VARCHAR,
    sitecode integer,
    createdby integer,
    CONSTRAINT reportsversion_pkey PRIMARY KEY (reportversioncode)
);

INSERT INTO lsaudittrailconfigmaster (serialno, manualaudittrail, modulename, ordersequnce, screenname, taskname) VALUES (211, 0, 'IDS_MDL_REPORTS', 1, 'IDS_SCN_REPORTMAPPING', 'IDS_TSK_SAVE') ON CONFLICT (serialno) DO NOTHING;
INSERT INTO lsaudittrailconfigmaster (serialno, manualaudittrail, modulename, ordersequnce, screenname, taskname) VALUES (212, 0, 'IDS_MDL_REPORTS', 1, 'IDS_SCN_REPORTVIEWER', 'IDS_TSK_RDOPEN') ON CONFLICT (serialno) DO NOTHING;

UPDATE lsaudittrailconfigmaster
SET ordersequnce = CASE
    WHEN screenname = 'IDS_SCN_SHEETORDERS' THEN 1
    WHEN screenname = 'IDS_SCN_PROTOCOLORDERS' THEN 2
    WHEN screenname = 'IDS_SCN_UNLOCKORDERS' THEN 3

    WHEN screenname = 'IDS_SCN_SHEETTEMPLATES' THEN 4
    WHEN screenname = 'IDS_SCN_PROTOCOLTEMP' THEN 5
    WHEN screenname = 'IDS_SCN_TEMPLATEMAPPING' THEN 6

    WHEN screenname = 'IDS_SCN_SAMPLEMASTER' THEN 7

    WHEN screenname = 'IDS_SCN_USERGROUP' THEN 8
    WHEN screenname = 'IDS_SCN_SITEMASTER' THEN 9
    WHEN screenname = 'IDS_SCN_USERMASTER' THEN 10
    WHEN screenname = 'IDS_SCN_USERRIGHTS' THEN 11
    WHEN screenname = 'IDS_SCN_PROJECTMASTER' THEN 12
    WHEN screenname = 'IDS_SCN_PROJECTTEAM' THEN 13
    WHEN screenname = 'IDS_SCN_TASKMASTER' THEN 14
    WHEN screenname = 'IDS_SCN_ORDERWORKFLOW' THEN 15
    WHEN screenname = 'IDS_SCN_TEMPLATEWORKFLOW' THEN 16
    WHEN screenname = 'IDS_SCN_DOMAIN' THEN 17
    WHEN screenname = 'IDS_SCN_PASSWORDPOLICY' THEN 18

    WHEN screenname = 'IDS_SCN_INSTRUMENTCATEGORY' THEN 19
    WHEN screenname = 'IDS_SCN_INSTRUMENTMASTER' THEN 20
    WHEN screenname = 'IDS_SCN_DELIMITER' THEN 21
    WHEN screenname = 'IDS_SCN_METHODDELIMITER' THEN 22
    WHEN screenname = 'IDS_SCN_METHODMASTER' THEN 23

    WHEN screenname = 'IDS_SCN_AUDITTRAILHIS' THEN 24
    WHEN screenname = 'IDS_SCN_AUDITTRAILCONFIG' THEN 25
    WHEN screenname = 'IDS_SCN_CFRSETTINGS' THEN 26

    WHEN screenname = 'IDS_SCN_REPORTDESIGNER' THEN 27
    WHEN screenname = 'IDS_SCN_REPORTVIEWER' THEN 28
    WHEN screenname = 'IDS_SCN_REPORTMAPPING' THEN 29

    WHEN screenname = 'IDS_SCN_MATERIALCATEGORY' THEN 30
    WHEN screenname = 'IDS_SCN_STORAGELOCATION' THEN 31
    WHEN screenname = 'IDS_SCN_MATERIALTYPE' THEN 32
    WHEN screenname = 'IDS_SCN_UNITMASTER' THEN 33
    WHEN screenname = 'IDS_SCN_GRADEMASTER' THEN 34
    WHEN screenname = 'IDS_SCN_SUPPLIER' THEN 35
    WHEN screenname = 'IDS_SCN_MANUFACTURER' THEN 36
    WHEN screenname = 'IDS_SCN_SECTIONMASTER' THEN 37
    WHEN screenname = 'IDS_SCN_INVENTORY' THEN 38
    WHEN screenname = 'IDS_SCN_MATERIAL' THEN 39
    WHEN screenname = 'IDS_SCN_MATERIALINVENTORY' THEN 40

    WHEN screenname = 'IDS_SCN_EQUIPMENTTYPE' THEN 41
    WHEN screenname = 'IDS_SCN_EQUIPMENTCATEGORY' THEN 42
    WHEN screenname = 'IDS_SCN_EQUIPMENT' THEN 43

    WHEN screenname = 'IDS_MDL_LOGBOOK' THEN 44
    ELSE ordersequnce -- Retain the current value if no match
END;

UPDATE lsaudittrailconfiguration
SET ordersequnce = CASE
    WHEN screenname = 'IDS_SCN_SHEETORDERS' THEN 1
    WHEN screenname = 'IDS_SCN_PROTOCOLORDERS' THEN 2
    WHEN screenname = 'IDS_SCN_UNLOCKORDERS' THEN 3

    WHEN screenname = 'IDS_SCN_SHEETTEMPLATES' THEN 4
    WHEN screenname = 'IDS_SCN_PROTOCOLTEMP' THEN 5
    WHEN screenname = 'IDS_SCN_TEMPLATEMAPPING' THEN 6

    WHEN screenname = 'IDS_SCN_SAMPLEMASTER' THEN 7

    WHEN screenname = 'IDS_SCN_USERGROUP' THEN 8
    WHEN screenname = 'IDS_SCN_SITEMASTER' THEN 9
    WHEN screenname = 'IDS_SCN_USERMASTER' THEN 10
    WHEN screenname = 'IDS_SCN_USERRIGHTS' THEN 11
    WHEN screenname = 'IDS_SCN_PROJECTMASTER' THEN 12
    WHEN screenname = 'IDS_SCN_PROJECTTEAM' THEN 13
    WHEN screenname = 'IDS_SCN_TASKMASTER' THEN 14
    WHEN screenname = 'IDS_SCN_ORDERWORKFLOW' THEN 15
    WHEN screenname = 'IDS_SCN_TEMPLATEWORKFLOW' THEN 16
    WHEN screenname = 'IDS_SCN_DOMAIN' THEN 17
    WHEN screenname = 'IDS_SCN_PASSWORDPOLICY' THEN 18

    WHEN screenname = 'IDS_SCN_INSTRUMENTCATEGORY' THEN 19
    WHEN screenname = 'IDS_SCN_INSTRUMENTMASTER' THEN 20
    WHEN screenname = 'IDS_SCN_DELIMITER' THEN 21
    WHEN screenname = 'IDS_SCN_METHODDELIMITER' THEN 22
    WHEN screenname = 'IDS_SCN_METHODMASTER' THEN 23

    WHEN screenname = 'IDS_SCN_AUDITTRAILHIS' THEN 24
    WHEN screenname = 'IDS_SCN_AUDITTRAILCONFIG' THEN 25
    WHEN screenname = 'IDS_SCN_CFRSETTINGS' THEN 26

    WHEN screenname = 'IDS_SCN_REPORTDESIGNER' THEN 27
    WHEN screenname = 'IDS_SCN_REPORTVIEWER' THEN 28
    WHEN screenname = 'IDS_SCN_REPORTMAPPING' THEN 29

    WHEN screenname = 'IDS_SCN_MATERIALCATEGORY' THEN 30
    WHEN screenname = 'IDS_SCN_STORAGELOCATION' THEN 31
    WHEN screenname = 'IDS_SCN_MATERIALTYPE' THEN 32
    WHEN screenname = 'IDS_SCN_UNITMASTER' THEN 33
    WHEN screenname = 'IDS_SCN_GRADEMASTER' THEN 34
    WHEN screenname = 'IDS_SCN_SUPPLIER' THEN 35
    WHEN screenname = 'IDS_SCN_MANUFACTURER' THEN 36
    WHEN screenname = 'IDS_SCN_SECTIONMASTER' THEN 37
    WHEN screenname = 'IDS_SCN_INVENTORY' THEN 38
    WHEN screenname = 'IDS_SCN_MATERIAL' THEN 39
    WHEN screenname = 'IDS_SCN_MATERIALINVENTORY' THEN 40

    WHEN screenname = 'IDS_SCN_EQUIPMENTTYPE' THEN 41
    WHEN screenname = 'IDS_SCN_EQUIPMENTCATEGORY' THEN 42
    WHEN screenname = 'IDS_SCN_EQUIPMENT' THEN 43

    WHEN screenname = 'IDS_MDL_LOGBOOK' THEN 44
    ELSE ordersequnce -- Retain the current value if no match
END;

ALTER TABLE IF Exists reporttemplateversion ADD Column IF NOT EXISTS  createdbyname character varying(255) COLLATE pg_catalog."default";

ALTER TABLE IF Exists lsorderattachments ADD Column IF NOT EXISTS version integer;
ALTER TABLE IF Exists lsprotocolfiles ADD Column IF NOT EXISTS version integer;
ALTER TABLE IF Exists lsprotocolfiles ADD Column IF NOT EXISTS editoruuid character varying(255) COLLATE pg_catalog."default";
ALTER TABLE IF Exists lssheetfolderfiles ADD Column IF NOT EXISTS version integer;
ALTER TABLE IF Exists lsprotocolfolderfiles ADD Column IF NOT EXISTS version integer;


ALTER TABLE lsordernotification ALTER COLUMN notificationcode TYPE numeric(17,0);
ALTER TABLE lsautoregister ALTER COLUMN regcode TYPE numeric(17,0);

update lsusergrouprightsmaster set screenname = 'IDS_SCN_MATERIALTYPEPARAMS' where displaytopic = 'IDS_SCN_MATERIALTYPEPARAMS';
update lsusergrouprights set screenname = 'IDS_SCN_MATERIALTYPEPARAMS' where displaytopic = 'IDS_SCN_MATERIALTYPEPARAMS';

ALTER TABLE IF Exists Reporttemplate ADD Column IF NOT EXISTS  keyword character varying(255) COLLATE pg_catalog."default";

ALTER TABLE IF Exists reporttemplate ADD COLUMN IF NOT EXISTS lockeduser INTEGER;

ALTER TABLE IF Exists reporttemplate ADD COLUMN IF NOT EXISTS lockedusername character varying(255);

--BEGIN INSERT [dbo].[LSusergrouprights] ([createdby], [createdon], [displaytopic], [modifiedby], [modifiedon], [modulename], [screenname], [sallow], [screate], [sdelete], [sedit], [lssitemaster_sitecode], [usergroupid_usergroupcode],[screenname]) VALUES (N'administrator', CAST(N'2020-02-21T14:50:55.727' AS DateTime), N'IDS_TSK_RETIRE', NULL, NULL, N'IDS_MDL_SETUP', N'IDS_SCN_USERMASTER', N'1', N'NA', N'NA', N'NA', 1, 1,'IDS_SCN_USERMASTER') END 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) 
    VALUES (183, 'IDS_TSK_REPORTMAPPING', 'IDS_MDL_REPORTS', 'IDS_SCN_REPORTMAPPER', '0', '0', 'NA', 'NA', '0,0,0', 27) ON CONFLICT (orderno) DO NOTHING;

   
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) SELECT 'IDS_TSK_REPORTMAPPING', 'IDS_MDL_REPORTS', 'administrator', '1', '1', 'NA', 'NA', 1,1,'IDS_SCN_REPORTMAPPER' 
WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_REPORTMAPPING' and screenname='IDS_SCN_REPORTMAPPER' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) 
    VALUES (184, 'IDS_TSK_SAVE', 'IDS_MDL_REPORTS', 'IDS_SCN_REPORTS', '0', 'NA', 'NA', 'NA', '0,0,0', 27) ON CONFLICT (orderno) DO NOTHING;

   
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) 
SELECT 'IDS_TSK_SAVE', 'IDS_MDL_REPORTS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_REPORTS' 
WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_SAVE' and screenname='IDS_SCN_REPORTS' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) 
    VALUES (185, 'IDS_TSK_INSERT', 'IDS_MDL_REPORTS', 'IDS_SCN_REPORTS', '0', 'NA', 'NA', 'NA', '0,0,0', 27) ON CONFLICT (orderno) DO NOTHING;

   
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) 
SELECT 'IDS_TSK_INSERT', 'IDS_MDL_REPORTS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_REPORTS' 
WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_INSERT' and screenname='IDS_SCN_REPORTS' and usergroupid_usergroupcode = 1); 

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) 
    VALUES (186, 'IDS_TSK_SAVE', 'IDS_MDL_REPORTS', 'IDS_SCN_REPORTVIEVER', '0', 'NA', 'NA', 'NA', '0,0,0', 27) ON CONFLICT (orderno) DO NOTHING;

   
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname) 
SELECT 'IDS_TSK_SAVE', 'IDS_MDL_REPORTS', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_REPORTS' 
WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_SAVE' and screenname='IDS_SCN_REPORTVIEVER' and usergroupid_usergroupcode = 1); 

UPDATE lsusergrouprights SET screenname = 'IDS_SCN_REPORTVIEVER' WHERE screenname = 'IDS_SCN_REPORTS' AND (displaytopic = 'IDS_SCN_REPORTVIEWER' OR displaytopic = 'IDS_SCN_REPORTVIEWERNEWFOLDER');
update lsusergrouprights set screenname = 'IDS_SCN_REPORTMAPPER' where screenname = 'IDS_SCN_REPORTS' and displaytopic = 'IDS_TSK_REPORTMAPPING';

UPDATE lsusergrouprightsmaster SET screenname = 'IDS_SCN_REPORTVIEVER' WHERE screenname = 'IDS_SCN_REPORTS' AND (displaytopic = 'IDS_SCN_REPORTVIEWER' OR displaytopic = 'IDS_SCN_REPORTVIEWERNEWFOLDER');
update lsusergrouprightsmaster set screenname = 'IDS_SCN_REPORTMAPPER' where screenname = 'IDS_SCN_REPORTVIEWER' and displaytopic = 'IDS_TSK_REPORTMAPPING';


CREATE TABLE IF NOT EXISTS public.screenmaster
(
    screencode integer NOT NULL,
    screenname character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT screenmaster_pkey PRIMARY KEY (screencode)
)

TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS public.barcodemaster
(
    barcodeno integer NOT NULL,
    barcodefileid character varying(255) COLLATE pg_catalog."default",
    barcodename character varying(255) COLLATE pg_catalog."default",
    createdon timestamp without time zone,
    status integer,
    createdby_usercode integer,
    screen_screencode integer,
    lssitemaster_sitecode integer,
    CONSTRAINT barcodemaster_pkey PRIMARY KEY (barcodeno),
    CONSTRAINT fk74xnn83meuhenxwn86fuxtn6q FOREIGN KEY (lssitemaster_sitecode)
        REFERENCES public.lssitemaster (sitecode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkapaj7g1ioh34oquegovh7qr5x FOREIGN KEY (createdby_usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkiqalqug5ecbnce224xtipcvjo FOREIGN KEY (screen_screencode)
        REFERENCES public.screenmaster (screencode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

INSERT INTO public.screenmaster(screencode, screenname) VALUES (1, 'IDS_SCN_MATERIALTYPE') ON CONFLICT (screencode) DO NOTHING;

ALTER TABLE IF Exists LSSheetOrderStructure ADD COLUMN IF NOT EXISTS projectfolder boolean;

ALTER TABLE IF Exists lsprotocolorderstructure ADD COLUMN IF NOT EXISTS projectfolder boolean;

update LSusergrouprightsmaster set screenname = 'IDS_SCN_REPORTS' where modulename = 'IDS_MDL_REPORTS' and screenname = 'IDS_SCN_REPORTDESIGNER';
update LSusergrouprights set screenname = 'IDS_SCN_REPORTS' where modulename = 'IDS_MDL_REPORTS' and screenname = 'IDS_SCN_REPORTDESIGNER';

DO $$
BEGIN
    -- Attempt to insert the new record
    INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) 
    VALUES (187, 'IDS_TSK_BARCODEMASTER', 'IDS_MDL_SETUP', 'IDS_SCN_BARCODEMASTER', '0', '0', '0', '0', '0,0,0', 20) ON CONFLICT (orderno) DO NOTHING;
END $$;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname,sequenceorder) 
SELECT 'IDS_TSK_BARCODEMASTER', 'IDS_MDL_SETUP', 'administrator', '1', '1', '1', '1', 1,1,'IDS_SCN_BARCODEMASTER' ,20
WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_BARCODEMASTER' and screenname='IDS_SCN_BARCODEMASTER' and usergroupid_usergroupcode = 1); 

INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) 
VALUES (193, 'IDS_TSK_BARCODESCANNER', 'IDS_MDL_INVENTORY', 'IDS_SCN_MATERIALINVENTORY', '0', 'NA', 'NA', 'NA', '0,0,0', 31) ON CONFLICT (orderno) DO NOTHING;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname,sequenceorder) 
SELECT 'IDS_TSK_BARCODESCANNER', 'IDS_MDL_INVENTORY', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_MATERIALINVENTORY' ,31
WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_BARCODESCANNER' and screenname='IDS_SCN_MATERIALINVENTORY' and usergroupid_usergroupcode = 1); 

INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) 
VALUES (189, 'IDS_TSK_RESTOCK', 'IDS_MDL_INVENTORY', 'IDS_SCN_MATERIALINVENTORY', '0', 'NA', 'NA', 'NA', '0,0,0', 31) ON CONFLICT (orderno) DO NOTHING;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname,sequenceorder) 
SELECT 'IDS_TSK_RESTOCK', 'IDS_MDL_INVENTORY', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_MATERIALINVENTORY' ,31
WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_RESTOCK' and screenname='IDS_SCN_MATERIALINVENTORY' and usergroupid_usergroupcode = 1); 

INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) 
VALUES (190, 'IDS_TSK_OPEN', 'IDS_MDL_INVENTORY', 'IDS_SCN_MATERIALINVENTORY', '0', 'NA', 'NA', 'NA', '0,0,0', 31) ON CONFLICT (orderno) DO NOTHING;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname,sequenceorder) 
SELECT 'IDS_TSK_OPEN', 'IDS_MDL_INVENTORY', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_MATERIALINVENTORY' ,31
WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_OPEN' and screenname='IDS_SCN_MATERIALINVENTORY' and usergroupid_usergroupcode = 1); 

INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) 
VALUES (191, 'IDS_TSK_RELEASE', 'IDS_MDL_INVENTORY', 'IDS_SCN_MATERIALINVENTORY', '0', 'NA', 'NA', 'NA', '0,0,0', 31) ON CONFLICT (orderno) DO NOTHING;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname,sequenceorder) 
SELECT 'IDS_TSK_RELEASE', 'IDS_MDL_INVENTORY', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_MATERIALINVENTORY' ,31
WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_RELEASE' and screenname='IDS_SCN_MATERIALINVENTORY' and usergroupid_usergroupcode = 1); 

INSERT INTO lsusergrouprightsmaster(orderno, displaytopic, modulename, screenname, sallow, screate, sdelete, sedit, status, sequenceorder) 
VALUES (192, 'IDS_TSK_DISPOSE', 'IDS_MDL_INVENTORY', 'IDS_SCN_MATERIALINVENTORY', '0', 'NA', 'NA', 'NA', '0,0,0', 31) ON CONFLICT (orderno) DO NOTHING;

INSERT into lsusergrouprights(displaytopic,modulename,createdby, sallow, screate, sdelete, sedit,lssitemaster_sitecode, usergroupid_usergroupcode,screenname,sequenceorder) 
SELECT 'IDS_TSK_DISPOSE', 'IDS_MDL_INVENTORY', 'administrator', '1', 'NA', 'NA', 'NA', 1,1,'IDS_SCN_MATERIALINVENTORY' ,31
WHERE NOT EXISTS (select * from lsusergrouprights where displaytopic = 'IDS_TSK_DISPOSE' and screenname='IDS_SCN_MATERIALINVENTORY' and usergroupid_usergroupcode = 1); 

ALTER TABLE IF Exists lsusermaster ADD Column IF NOT EXISTS getstart character varying(255);

ALTER TABLE IF Exists barcodemaster ADD COLUMN IF NOT EXISTS barcodefilename character varying(255);

ALTER TABLE IF Exists LsAutoregister ADD Column IF NOT EXISTS timerIdname character varying(255);

ALTER TABLE IF Exists materialtype ADD COLUMN IF NOT EXISTS barcode_barcodeno integer;

DO
$do$
declare
  barcodecount integer :=0;
begin
SELECT count(*) into barcodecount FROM
information_schema.table_constraints WHERE constraint_name='fkgv6y6qb9lh0sos5hkblhhmb5u'
AND table_name='materialtype';
 IF barcodecount =0 THEN
    ALTER TABLE ONLY materialtype ADD CONSTRAINT fkgv6y6qb9lh0sos5hkblhhmb5u FOREIGN KEY (barcode_barcodeno) REFERENCES barcodemaster (barcodeno);
   END IF;
END
$do$; 

update lsusergrouprightsmaster set screenname = 'IDS_SCN_MATERIALINVENTORY' where screenname = 'IDS_SCN_INVENTORY' and displaytopic = 'IDS_TSK_DISPOSE' and modulename = 'IDS_MDL_INVENTORY';
update lsusergrouprights set screenname = 'IDS_SCN_MATERIALINVENTORY' where screenname = 'IDS_SCN_INVENTORY' and displaytopic = 'IDS_TSK_DISPOSE' and modulename = 'IDS_MDL_INVENTORY';

update lsusergrouprightsmaster set screenname = 'IDS_SCN_MATERIALINVENTORY' where screenname = 'IDS_SCN_INVENTORY' and displaytopic = 'IDS_TSK_BARCODESCANNER' and modulename = 'IDS_MDL_INVENTORY';
update lsusergrouprights set screenname = 'IDS_SCN_MATERIALINVENTORY' where screenname = 'IDS_SCN_INVENTORY' and displaytopic = 'IDS_TSK_BARCODESCANNER' and modulename = 'IDS_MDL_INVENTORY';

update lsusergrouprightsmaster set screenname = 'IDS_SCN_MATERIALINVENTORY' where screenname = 'IDS_SCN_INVENTORY' and displaytopic = 'IDS_TSK_RELEASE' and modulename = 'IDS_MDL_INVENTORY';
update lsusergrouprights set screenname = 'IDS_SCN_MATERIALINVENTORY' where screenname = 'IDS_SCN_INVENTORY' and displaytopic = 'IDS_TSK_RELEASE' and modulename = 'IDS_MDL_INVENTORY';

update lsusergrouprightsmaster set screenname = 'IDS_SCN_MATERIALINVENTORY' where screenname = 'IDS_SCN_INVENTORY' and displaytopic = 'IDS_TSK_OPEN' and modulename = 'IDS_MDL_INVENTORY';
update lsusergrouprights set screenname = 'IDS_SCN_MATERIALINVENTORY' where screenname = 'IDS_SCN_INVENTORY' and displaytopic = 'IDS_TSK_OPEN' and modulename = 'IDS_MDL_INVENTORY';

update lsusergrouprightsmaster set screenname = 'IDS_SCN_MATERIALINVENTORY' where screenname = 'IDS_SCN_INVENTORY' and displaytopic = 'IDS_TSK_RESTOCK' and modulename = 'IDS_MDL_INVENTORY';
update lsusergrouprights set screenname = 'IDS_SCN_MATERIALINVENTORY' where screenname = 'IDS_SCN_INVENTORY' and displaytopic = 'IDS_TSK_RESTOCK' and modulename = 'IDS_MDL_INVENTORY';

update lsusergrouprightsmaster set screenname = 'IDS_SCN_EQUIPMENTMASTER' where screenname = 'IDS_SCN_INVENTORY' and displaytopic = 'IDS_TSK_ACTDEACT' and modulename = 'IDS_MDL_INVENTORY';
update lsusergrouprights set screenname = 'IDS_SCN_EQUIPMENTMASTER' where screenname = 'IDS_SCN_INVENTORY' and displaytopic = 'IDS_TSK_ACTDEACT' and modulename = 'IDS_MDL_INVENTORY';

update lsusergrouprightsmaster set screenname = 'IDS_SCN_EQUIPMENTMASTER' where screenname = 'IDS_SCN_INVENTORY' and displaytopic = 'IDS_TSK_CALIBRATE' and modulename = 'IDS_MDL_INVENTORY';
update lsusergrouprights set screenname = 'IDS_SCN_EQUIPMENTMASTER' where screenname = 'IDS_SCN_INVENTORY' and displaytopic = 'IDS_TSK_CALIBRATE' and modulename = 'IDS_MDL_INVENTORY';

update lsusergrouprightsmaster set screenname = 'IDS_SCN_EQUIPMENTMASTER' where screenname = 'IDS_SCN_INVENTORY' and displaytopic = 'IDS_TSK_MAINTANN' and modulename = 'IDS_MDL_INVENTORY';
update lsusergrouprights set screenname = 'IDS_SCN_EQUIPMENTMASTER' where screenname = 'IDS_SCN_INVENTORY' and displaytopic = 'IDS_TSK_MAINTANN' and modulename = 'IDS_MDL_INVENTORY';

INSERT INTO public.screenmaster(screencode, screenname) VALUES (2, 'IDS_SCN_SHEETTEMPLATE') ON CONFLICT (screencode) DO NOTHING;

CREATE TABLE IF NOT EXISTS public.lsfilemapbarcode
(
    filebarcode integer NOT NULL,
    content jsonb,
    filecode integer,
    labelparameter character varying(255) COLLATE pg_catalog."default",
    barcodeno integer,
    CONSTRAINT lsfilemapbarcode_pkey PRIMARY KEY (filebarcode)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.lsfilemapbarcode
    OWNER to postgres;
    
ALTER TABLE IF Exists LsfilemapBarcode ADD Column IF NOT EXISTS tagname character varying(255);

ALTER TABLE IF Exists LsfilemapBarcode ADD Column IF NOT EXISTS barocodename character varying(255);

ALTER TABLE IF EXISTS lscfrreasons ADD COLUMN IF NOT EXISTS createddate TIMESTAMP;
ALTER TABLE IF EXISTS lscfrreasons ADD COLUMN IF NOT EXISTS modifieddate TIMESTAMP;
ALTER TABLE IF EXISTS lscfrreasons ADD COLUMN IF NOT EXISTS status INTEGER;

UPDATE LScfrreasons SET createddate = CURRENT_TIMESTAMP WHERE createddate IS NULL;
UPDATE LScfrreasons SET status = 1 WHERE status IS NULL;

ALTER TABLE IF EXISTS lscfrreasons ADD COLUMN IF NOT EXISTS createdby_usercode INTEGER;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint 
        WHERE conname = 'fkahf5bxsaoc13n98igb65qquno'
    ) THEN
        ALTER TABLE lscfrreasons 
        ADD CONSTRAINT fkahf5bxsaoc13n98igb65qquno 
        FOREIGN KEY (createdby_usercode) REFERENCES public.lsusermaster (usercode);
    END IF;
END $$;

ALTER TABLE IF EXISTS lscfrreasons ADD COLUMN IF NOT EXISTS modifiedby_usercode INTEGER;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint 
        WHERE conname = 'fkc4kvyprk3q2cwje336e5gsw3i'
    ) THEN
        ALTER TABLE lscfrreasons 
        ADD CONSTRAINT fkc4kvyprk3q2cwje336e5gsw3i 
        FOREIGN KEY (modifiedby_usercode) REFERENCES public.lsusermaster (usercode);
    END IF;
END $$;

update lsusergrouprights set screate = '1', sedit = '1' where modulename = 'IDS_MDL_INVENTORY' and screenname = 'IDS_SCN_MATERIALTYPEPARAMS' and displaytopic = 'IDS_SCN_MATERIALTYPEPARAMS' and usergroupid_usergroupcode = 1;
ALTER TABLE IF Exists lsusermaster ADD Column IF NOT EXISTS forgetstatus INTEGER;

ALTER TABLE IF EXISTS LsAutoregister ADD COLUMN IF NOT EXISTS delayinminutes bigint;

update lsusergrouprightsmaster set screate = '0' where displaytopic = 'IDS_SCN_MATERIALTYPEPARAMS';

delete from LSusergrouprights where screenname = 'IDS_SCN_MATERIALTYPE';
delete from LSusergrouprights where screenname = 'IDS_SCN_SAMPLEMASTER';
delete from LSusergrouprightsmaster where screenname = 'IDS_SCN_MATERIALTYPE';
delete from LSusergrouprightsmaster where screenname = 'IDS_SCN_SAMPLEMASTER';

UPDATE lsusergrouprightsmaster SET screenname = 'IDS_SCN_EQUIPMENTMASTER' WHERE screenname = 'IDS_TSK_EQUIPMENTMASTER';
UPDATE lsusergrouprights SET screenname = 'IDS_SCN_EQUIPMENTMASTER' WHERE screenname = 'IDS_TSK_EQUIPMENTMASTER';
update LSusergrouprightsmaster set screenname = 'IDS_SCN_REPORTVIEVER' where screenname = 'IDS_SCN_REPORTVIEWER';
update LSusergrouprights set screenname = 'IDS_SCN_REPORTVIEVER' where screenname = 'IDS_SCN_REPORTVIEWER';

update lsusergrouprights  set screate = '1',sedit = '1', sdelete = '1' where displaytopic = 'IDS_TSK_EQUIPMENTMASTER' and usergroupid_usergroupcode = 1;
update lsusergrouprights  set screate = '1' where displaytopic = 'IDS_TSK_EQUIPMENTTYPE' and usergroupid_usergroupcode = 1;;
update lsusergrouprights  set sedit = 'NA', sdelete = 'NA' where displaytopic = 'IDS_TSK_REPORTMAPPING' and usergroupid_usergroupcode = 1;;
update lsusergrouprights  set screate = '1',sedit = '1', sdelete = '1' where displaytopic = 'IDS_SCN_EQUIPMENTCATEGORY' and usergroupid_usergroupcode = 1;

UPDATE lsusergrouprightsmaster SET sequenceorder = CASE
    WHEN screenname = 'IDS_SCN_DASHBOARD' THEN 1

    WHEN screenname = 'IDS_SCN_SHEETORDERS' THEN 2
    WHEN screenname = 'IDS_SCN_PROTOCOLORDERS' THEN 3
    WHEN screenname = 'IDS_SCN_UNLOCKORDERS' THEN 4

    WHEN screenname = 'IDS_SCN_SHEETTEMPLATE' THEN 5
    WHEN screenname = 'IDS_SCN_PROTOCOLTEMPLATE' THEN 6
    WHEN screenname = 'IDS_SCN_TEMPLATEMAPPING' THEN 7

    WHEN screenname = 'IDS_SCN_USERGROUP' THEN 8
    WHEN screenname = 'IDS_SCN_SITEMASTER' THEN 9
    WHEN screenname = 'IDS_SCN_ACTIVEUSER' THEN 9
    WHEN screenname = 'IDS_SCN_USERMASTER' THEN 10
    WHEN screenname = 'IDS_SCN_USERRIGHTS' THEN 11
    WHEN screenname = 'IDS_SCN_PROJECTMASTER' THEN 12
    WHEN screenname = 'IDS_SCN_PROJECTTEAM' THEN 13
    WHEN screenname = 'IDS_SCN_TASKMASTER' THEN 14
    WHEN screenname = 'IDS_SCN_ORDERWORKLOW' THEN 15
    WHEN screenname = 'IDS_SCN_TEMPLATEWORKFLOW' THEN 16
    WHEN screenname = 'IDS_SCN_DOMAIN' THEN 17
    WHEN screenname = 'IDS_SCN_PASSWORDPOLICY' THEN 18
    WHEN screenname = 'IDS_SCN_BARCODEMASTER' THEN 18

    WHEN screenname = 'IDS_SCN_PARSER' THEN 19
    WHEN screenname = 'IDS_SCN_DELIMITER' THEN 19
    WHEN screenname = 'IDS_SCN_INSTRUMENTCATEGORY' THEN 20
    WHEN screenname = 'IDS_SCN_INSTRUMENTMASTER' THEN 21
    WHEN screenname = 'IDS_SCN_METHODDELIMITER' THEN 22
    WHEN screenname = 'IDS_SCN_METHODMASTER' THEN 23

    WHEN screenname = 'IDS_SCN_AUDITTRAILHIS' THEN 24
    WHEN screenname = 'IDS_SCN_CFRSETTINGS' THEN 25
    WHEN screenname = 'IDS_SCN_AUDITTRAILCONFIG' THEN 26

    WHEN screenname = 'IDS_SCN_REPORTS' THEN 27
    WHEN screenname = 'IDS_SCN_REPORTVIEVER' THEN 28
    WHEN screenname = 'IDS_SCN_REPORTMAPPER' THEN 29

    WHEN screenname = 'IDS_SCN_MATERIAL' THEN 30
    WHEN screenname = 'IDS_SCN_MATERIALINVENTORY' THEN 31
    WHEN screenname = 'IDS_SCN_MATERIALTYPEPARAMS' THEN 32
    WHEN screenname = 'IDS_SCN_MATERIALCATEGORY' THEN 33
    WHEN screenname = 'IDS_SCN_GRADEMASTER' THEN 34
    WHEN screenname = 'IDS_SCN_SUPPLIER' THEN 35
    WHEN screenname = 'IDS_SCN_STORAGELOCATION' THEN 36
    WHEN screenname = 'IDS_SCN_SECTIONMASTER' THEN 37
    WHEN screenname = 'IDS_SCN_MANUFACTURER' THEN 38
    WHEN screenname = 'IDS_SCN_UNITMASTER' THEN 39
    WHEN screenname = 'IDS_SCN_MATERIAL' THEN 40
    WHEN screenname = 'IDS_SCN_MATERIALINVENTORY' THEN 41

    WHEN screenname = 'IDS_SCN_EQUIPMENT' THEN 42
    WHEN screenname = 'IDS_SCN_EQUIPMENTMASTER' THEN 43
    WHEN screenname = 'IDS_TSK_EQUIPMENTMASTER' THEN 44

    WHEN screenname = 'IDS_SCN_LOGBOOK' THEN 45
    ELSE sequenceorder -- Retain the current value if no match
END;

UPDATE lsusergrouprights SET sequenceorder = CASE
    WHEN screenname = 'IDS_SCN_DASHBOARD' THEN 1

    WHEN screenname = 'IDS_SCN_SHEETORDERS' THEN 2
    WHEN screenname = 'IDS_SCN_PROTOCOLORDERS' THEN 3
    WHEN screenname = 'IDS_SCN_UNLOCKORDERS' THEN 4

    WHEN screenname = 'IDS_SCN_SHEETTEMPLATE' THEN 5
    WHEN screenname = 'IDS_SCN_PROTOCOLTEMPLATE' THEN 6
    WHEN screenname = 'IDS_SCN_TEMPLATEMAPPING' THEN 7

    WHEN screenname = 'IDS_SCN_USERGROUP' THEN 8
    WHEN screenname = 'IDS_SCN_SITEMASTER' THEN 9
    WHEN screenname = 'IDS_SCN_ACTIVEUSER' THEN 9
    WHEN screenname = 'IDS_SCN_USERMASTER' THEN 10
    WHEN screenname = 'IDS_SCN_USERRIGHTS' THEN 11
    WHEN screenname = 'IDS_SCN_PROJECTMASTER' THEN 12
    WHEN screenname = 'IDS_SCN_PROJECTTEAM' THEN 13
    WHEN screenname = 'IDS_SCN_TASKMASTER' THEN 14
    WHEN screenname = 'IDS_SCN_ORDERWORKLOW' THEN 15
    WHEN screenname = 'IDS_SCN_TEMPLATEWORKFLOW' THEN 16
    WHEN screenname = 'IDS_SCN_DOMAIN' THEN 17
    WHEN screenname = 'IDS_SCN_PASSWORDPOLICY' THEN 18
    WHEN screenname = 'IDS_SCN_BARCODEMASTER' THEN 18

    WHEN screenname = 'IDS_SCN_PARSER' THEN 19
    WHEN screenname = 'IDS_SCN_DELIMITER' THEN 19
    WHEN screenname = 'IDS_SCN_INSTRUMENTCATEGORY' THEN 20
    WHEN screenname = 'IDS_SCN_INSTRUMENTMASTER' THEN 21
    WHEN screenname = 'IDS_SCN_METHODDELIMITER' THEN 22
    WHEN screenname = 'IDS_SCN_METHODMASTER' THEN 23

    WHEN screenname = 'IDS_SCN_AUDITTRAILHIS' THEN 24
    WHEN screenname = 'IDS_SCN_CFRSETTINGS' THEN 25
    WHEN screenname = 'IDS_SCN_AUDITTRAILCONFIG' THEN 26

    WHEN screenname = 'IDS_SCN_REPORTS' THEN 27
    WHEN screenname = 'IDS_SCN_REPORTVIEVER' THEN 28
    WHEN screenname = 'IDS_SCN_REPORTMAPPER' THEN 29

    WHEN screenname = 'IDS_SCN_MATERIAL' THEN 30
    WHEN screenname = 'IDS_SCN_MATERIALINVENTORY' THEN 31
    WHEN screenname = 'IDS_SCN_MATERIALTYPEPARAMS' THEN 32
    WHEN screenname = 'IDS_SCN_MATERIALCATEGORY' THEN 33
    WHEN screenname = 'IDS_SCN_GRADEMASTER' THEN 34
    WHEN screenname = 'IDS_SCN_SUPPLIER' THEN 35
    WHEN screenname = 'IDS_SCN_STORAGELOCATION' THEN 36
    WHEN screenname = 'IDS_SCN_SECTIONMASTER' THEN 37
    WHEN screenname = 'IDS_SCN_MANUFACTURER' THEN 38
    WHEN screenname = 'IDS_SCN_UNITMASTER' THEN 39
    WHEN screenname = 'IDS_SCN_MATERIAL' THEN 40
    WHEN screenname = 'IDS_SCN_MATERIALINVENTORY' THEN 41

    WHEN screenname = 'IDS_SCN_EQUIPMENT' THEN 42
    WHEN screenname = 'IDS_SCN_EQUIPMENTMASTER' THEN 43
    WHEN screenname = 'IDS_TSK_EQUIPMENTMASTER' THEN 44

    WHEN screenname = 'IDS_SCN_LOGBOOK' THEN 45
    ELSE sequenceorder -- Retain the current value if no match
END;

insert into LSpreferences (serialno,tasksettings,valuesettings) values(6,'RegulatedIndustry','1') on conflict(serialno) do nothing;


 DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'lsfileelnmethod_seq' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE lsfileelnmethod_seq;
   ELSIF _kind = 'S' THEN  
     
   ELSE                  
    
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.lsfileelnmethod
(
    fileelnmethodcode integer NOT NULL,
    filecode integer,
    instmasterkey integer NOT NULL,
    methodkey integer NOT NULL,
    CONSTRAINT lsfileelnmethod_pkey PRIMARY KEY (fileelnmethodcode),
    CONSTRAINT fk8dce8e4ym594ljg5mnn7r6ywo FOREIGN KEY (instmasterkey)
        REFERENCES public.instrumentmaster (instmasterkey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk8dh6t6caqk9rx3reij6b6apa8 FOREIGN KEY (methodkey)
        REFERENCES public.method (methodkey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkjbido3fgbr49nh8nd6p2immq6 FOREIGN KEY (filecode)
        REFERENCES public.lsfile (filecode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.lsfileelnmethod
    OWNER to postgres;
    
    
 DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'lsorderelnmethod_seq' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE lsorderelnmethod_seq;
   ELSIF _kind = 'S' THEN  
     
   ELSE                  
    
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.lsorderelnmethod
(
    orderelnmethodcode integer NOT NULL,
    batchcode numeric(17,0),
    batchid character varying(250) COLLATE pg_catalog."default",
    createdtimestamp timestamp without time zone,
    usercode integer NOT NULL,
    instmasterkey integer NOT NULL,
    methodkey integer NOT NULL,
    CONSTRAINT lsorderelnmethod_pkey PRIMARY KEY (orderelnmethodcode),
    CONSTRAINT fk1pcnevo3a6ggq9avocyios7oc FOREIGN KEY (instmasterkey)
        REFERENCES public.instrumentmaster (instmasterkey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk2rk7hkiwbsvtqtgv59667trw FOREIGN KEY (batchcode)
        REFERENCES public.lslogilablimsorderdetail (batchcode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkaglu06hria5eifsjq1c7ahd1e FOREIGN KEY (methodkey)
        REFERENCES public.method (methodkey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fko9srm7ji0pyfb85v5pgrvybr8 FOREIGN KEY (usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.lsorderelnmethod
    OWNER to postgres;
    
DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'rctcpresultdetails_resultid_seq' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE rctcpresultdetails_resultid_seq;
   ELSIF _kind = 'S' THEN  
     
   ELSE                  
    
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.rctcpresultdetails
(
    resultid integer NOT NULL DEFAULT nextval('rctcpresultdetails_resultid_seq'::regclass),
    createddate timestamp without time zone,
    instmastkey integer,
    methodkey integer,
    paramname character varying(255) COLLATE pg_catalog."default",
    parserblockkey integer,
    parserfieldkey integer,
    results character varying(255) COLLATE pg_catalog."default",
    seqnumber integer,
    status integer,
    usercode integer NOT NULL,
    sitecode integer NOT NULL,
    fieldname character varying(255) COLLATE pg_catalog."default",
    instmasterkey integer NOT NULL,
    CONSTRAINT rctcpresultdetails_pkey PRIMARY KEY (resultid),
    CONSTRAINT fk7hhsu6pfqtofl28l3r6wmt2se FOREIGN KEY (methodkey)
        REFERENCES public.method (methodkey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkl2xh6s2w8fpyeaf71h9y2hylr FOREIGN KEY (sitecode)
        REFERENCES public.lssitemaster (sitecode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkric7owd4wwscqecu6dh85kqeu FOREIGN KEY (usercode)
        REFERENCES public.lsusermaster (usercode) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fksm696qjhmpowab2v55grca3u4 FOREIGN KEY (instmasterkey)
        REFERENCES public.instrumentmaster (instmasterkey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fksy4d35but1667y3839kcnks5b FOREIGN KEY (instmastkey)
        REFERENCES public.instrumentmaster (instmasterkey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT rctcpresultdetails_status_check CHECK (status >= '-1'::integer AND status <= 1)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.rctcpresultdetails
    OWNER to postgres;
    
    
DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'rctcpresultfieldvalues_sno_seq' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE rctcpresultfieldvalues_sno_seq;
   ELSIF _kind = 'S' THEN  
     
   ELSE                  
    
   END IF;
END
$do$;


CREATE TABLE IF NOT EXISTS public.rctcpresultfieldvalues
(
    sno integer NOT NULL DEFAULT nextval('rctcpresultfieldvalues_sno_seq'::regclass),
    fieldname character varying(100) COLLATE pg_catalog."default",
    fieldvalue character varying(100) COLLATE pg_catalog."default",
    resseqno integer,
    resultid integer,
    CONSTRAINT rctcpresultfieldvalues_pkey PRIMARY KEY (sno),
    CONSTRAINT fkllo5l7pce04r3yvkuxv07w8s6 FOREIGN KEY (resultid)
        REFERENCES public.rctcpresultdetails (resultid) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.rctcpresultfieldvalues
    OWNER to postgres;

ALTER TABLE IF Exists method ADD COLUMN IF NOT EXISTS nequipmentcode Integer;
    
DO
$do$
declare
  multiusergroupcount integer :=0;
begin
SELECT count(*) into multiusergroupcount FROM
information_schema.table_constraints WHERE constraint_name='fka9b6b740h2ntpqvyhohcyc9ry'
AND table_name='method';
 IF multiusergroupcount =0 THEN
    ALTER TABLE ONLY method ADD CONSTRAINT fka9b6b740h2ntpqvyhohcyc9ry FOREIGN KEY (nequipmentcode) REFERENCES equipment(nequipmentcode);
   END IF;
END
$do$; 

ALTER TABLE IF Exists equipment ADD Column IF NOT EXISTS sequipmentelectrodeno character varying(255);
ALTER TABLE IF Exists lsactiveuser ADD Column IF NOT EXISTS usergroupcode integer;
ALTER TABLE IF Exists lsactiveuser ADD Column IF NOT EXISTS usergroupname character varying(100);

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns 
        WHERE table_name = 'method' 
        AND column_name = 'instmasterkey'
        AND is_nullable = 'NO'
    ) THEN
        ALTER TABLE public.method
        ALTER COLUMN instmasterkey DROP NOT NULL;
    END IF;
END $$;

UPDATE delimiter SET delimiterstatus = 'A' WHERE delimiterkey = 10 AND delimiterstatus IS NULL;

update instrumenttype set status = 1 where instrumenttype.insttypename='RS232'  and status =-1;
update instrumenttype set status = 1 where instrumenttype.insttypename='TCP/IP' and status =-1;

ALTER TABLE IF Exists lsorderelnmethod ADD COLUMN IF NOT EXISTS nequipmentcode Integer;
    
DO
$do$
declare
  multiusergroupcount integer :=0;
begin
SELECT count(*) into multiusergroupcount FROM
information_schema.table_constraints WHERE constraint_name='fkkldigbgplb5ffv1f7p4uua2mp'
AND table_name='lsorderelnmethod';
 IF multiusergroupcount =0 THEN
    ALTER TABLE ONLY lsorderelnmethod ADD CONSTRAINT fkkldigbgplb5ffv1f7p4uua2mp FOREIGN KEY (nequipmentcode) REFERENCES equipment(nequipmentcode);
   END IF;
END
$do$; 

ALTER TABLE IF Exists lsfileelnmethod ADD COLUMN IF NOT EXISTS nequipmentcode Integer;
    
DO
$do$
declare
  multiusergroupcount integer :=0;
begin
SELECT count(*) into multiusergroupcount FROM
information_schema.table_constraints WHERE constraint_name='fkathu1pdk00m8o6r2ah31p3xce'
AND table_name='lsfileelnmethod';
 IF multiusergroupcount =0 THEN
    ALTER TABLE ONLY lsfileelnmethod ADD CONSTRAINT fkathu1pdk00m8o6r2ah31p3xce FOREIGN KEY (nequipmentcode) REFERENCES equipment(nequipmentcode);
   END IF;
END
$do$; 

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns 
        WHERE table_name = 'lsfileelnmethod' 
        AND column_name = 'instmasterkey'
        AND is_nullable = 'NO'
    ) THEN
        ALTER TABLE public.lsfileelnmethod
        ALTER COLUMN instmasterkey DROP NOT NULL;
    END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns 
        WHERE table_name = 'lsorderelnmethod' 
        AND column_name = 'instmasterkey'
        AND is_nullable = 'NO'
    ) THEN
        ALTER TABLE public.lsorderelnmethod
        ALTER COLUMN instmasterkey DROP NOT NULL;
    END IF;
END $$;

ALTER TABLE IF Exists lslogilabprotocoldetail ADD COLUMN IF NOT EXISTS lstestmasterlocal_testcode integer;

ALTER TABLE IF Exists lslogilabprotocoldetail DROP CONSTRAINT IF EXISTS fk701k777d2da33pkkl6lnasathis;

ALTER TABLE lslogilabprotocoldetail ADD CONSTRAINT fk701k777d2da33pkkl6lnasathis FOREIGN KEY (lstestmasterlocal_testcode) 
REFERENCES lstestmasterlocal (testcode);

UPDATE lslogilabprotocoldetail
SET lstestmasterlocal_testcode = (
    SELECT testcode
    FROM lstestmasterlocal
    WHERE lstestmasterlocal.testcode = lslogilabprotocoldetail.testcode
)
WHERE testcode IS NOT NULL
AND EXISTS (
    SELECT 1
    FROM lstestmasterlocal
    WHERE lstestmasterlocal.testcode = lslogilabprotocoldetail.testcode
);

 DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'communicationsetting_cmmsettingcode_seq' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE communicationsetting_cmmsettingcode_seq;
   ELSIF _kind = 'S' THEN  
     
   ELSE                  
    
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
   WHERE  relname = 'parity_paritykey_seq' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE parity_paritykey_seq;
   ELSIF _kind = 'S' THEN  
     
   ELSE                  
    
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.parity
(
    paritykey integer NOT NULL DEFAULT nextval('parity_paritykey_seq'::regclass),
    parityname character varying(255) COLLATE pg_catalog."default",
    status integer,
    CONSTRAINT parity_pkey PRIMARY KEY (paritykey)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.parity
    OWNER to postgres;

 DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'resultsamplefrom_resultsamplekey_seq' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE resultsamplefrom_resultsamplekey_seq;
   ELSIF _kind = 'S' THEN  
     
   ELSE                  
    
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.resultsamplefrom
(
    resultsamplekey integer NOT NULL DEFAULT nextval('resultsamplefrom_resultsamplekey_seq'::regclass),
    resultsamplename character varying(255) COLLATE pg_catalog."default",
    status integer NOT NULL,
    CONSTRAINT resultsamplefrom_pkey PRIMARY KEY (resultsamplekey)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.resultsamplefrom
    OWNER to postgres;

 DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'handshake_handshakekey_seq' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE handshake_handshakekey_seq;
   ELSIF _kind = 'S' THEN  
     
   ELSE                  
    
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.handshake
(
    handshakekey integer NOT NULL DEFAULT nextval('handshake_handshakekey_seq'::regclass),
    handshakename character varying(255) COLLATE pg_catalog."default",
    status integer NOT NULL,
    CONSTRAINT handshake_pkey PRIMARY KEY (handshakekey)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.handshake
    OWNER to postgres;

 DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'stopbits_stopbitkey_seq' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE stopbits_stopbitkey_seq;
   ELSIF _kind = 'S' THEN  
     
   ELSE                  
    
   END IF;
END
$do$;
    
CREATE TABLE IF NOT EXISTS public.stopbits
(
    stopbitkey integer NOT NULL DEFAULT nextval('stopbits_stopbitkey_seq'::regclass),
    status integer NOT NULL,
    stopbitname character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT stopbits_pkey PRIMARY KEY (stopbitkey)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.stopbits
    OWNER to postgres;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'conversiontype_conversiontypekey_seq' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE conversiontype_conversiontypekey_seq;
   ELSIF _kind = 'S' THEN  
     
   ELSE                  
    
   END IF;
END
$do$;


CREATE TABLE IF NOT EXISTS public.conversiontype
(
    conversiontypekey integer NOT NULL DEFAULT nextval('conversiontype_conversiontypekey_seq'::regclass),
    conversiontypename character varying(255) COLLATE pg_catalog."default",
    status integer NOT NULL,
    CONSTRAINT conversiontype_pkey PRIMARY KEY (conversiontypekey)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.conversiontype OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.communicationsetting
(
    cmmsettingcode bigint NOT NULL DEFAULT nextval('communicationsetting_cmmsettingcode_seq'::regclass),
    baudrate character varying(255) COLLATE pg_catalog."default",
    channalno character varying(255) COLLATE pg_catalog."default",
    comportno integer,
    databits character varying(255) COLLATE pg_catalog."default",
    ipaddress character varying(255) COLLATE pg_catalog."default",
    maxcurrent character varying(255) COLLATE pg_catalog."default",
    maxdatapoint character varying(255) COLLATE pg_catalog."default",
    mincurrent character varying(255) COLLATE pg_catalog."default",
    mindatapoint character varying(255) COLLATE pg_catalog."default",
    nequipmentcode integer,
    tcpportno integer,
    tidlesecond integer,
    conversiontype integer,
    handshake integer,
    cmmtype integer,
    parity integer,
    rsampleidf integer,
    stopbit integer,
    CONSTRAINT communicationsetting_pkey PRIMARY KEY (cmmsettingcode),
    CONSTRAINT fkan3ncrik40dltk3tahhkf6xyd FOREIGN KEY (conversiontype)
        REFERENCES public.conversiontype (conversiontypekey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fklt7oga656tydp353wsjdmf803 FOREIGN KEY (stopbit)
        REFERENCES public.stopbits (stopbitkey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkmycp1vklhof9v8uuodj6p82wt FOREIGN KEY (handshake)
        REFERENCES public.handshake (handshakekey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkn8c3kpfip02ejxekn6nerajyj FOREIGN KEY (cmmtype)
        REFERENCES public.instrumenttype (insttypekey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkofrf7vemj4xgut7cxdtg9dvnw FOREIGN KEY (parity)
        REFERENCES public.parity (paritykey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkrvmpsh14ba2mpfteq0nt401ww FOREIGN KEY (rsampleidf)
        REFERENCES public.resultsamplefrom (resultsamplekey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.communicationsetting
    OWNER to postgres;
ALTER TABLE IF Exists equipment ADD COLUMN IF NOT EXISTS fk_cmmsettingcode Integer;
    
DO
$do$
declare
  multiusergroupcount integer :=0;
begin
SELECT count(*) into multiusergroupcount FROM
information_schema.table_constraints WHERE constraint_name='fk2esxrtlkh4sju5fb40xhbltf9'
AND table_name='equipment';
 IF multiusergroupcount =0 THEN
    ALTER TABLE ONLY equipment ADD CONSTRAINT fk2esxrtlkh4sju5fb40xhbltf9 FOREIGN KEY (fk_cmmsettingcode) REFERENCES communicationsetting(cmmsettingcode);
   END IF;
END
$do$;

    
INSERT into stopbits(stopbitkey,stopbitname,status) VALUES(1,'-1',1) ON CONFLICT(stopbitkey)DO NOTHING;
INSERT into stopbits(stopbitkey,stopbitname,status) VALUES(2,'0',1) ON CONFLICT(stopbitkey)DO NOTHING;
INSERT into stopbits(stopbitkey,stopbitname,status) VALUES(3,'1',1) ON CONFLICT(stopbitkey)DO NOTHING;
INSERT into stopbits(stopbitkey,stopbitname,status) VALUES(4,'1.5',1) ON CONFLICT(stopbitkey)DO NOTHING;
INSERT into stopbits(stopbitkey,stopbitname,status) VALUES(5,'2',1) ON CONFLICT(stopbitkey)DO NOTHING;

INSERT into parity(paritykey,parityname,status) VALUES(1,'NONE',1) ON CONFLICT(paritykey)DO NOTHING;
INSERT into parity(paritykey,parityname,status) VALUES(2,'ODD',1) ON CONFLICT(paritykey)DO NOTHING;
INSERT into parity(paritykey,parityname,status) VALUES(3,'EVEN',1) ON CONFLICT(paritykey)DO NOTHING;

INSERT into handshake(handshakekey,handshakename,status) VALUES(1,'NONE',1) ON CONFLICT(handshakekey)DO NOTHING;
INSERT into handshake(handshakekey,handshakename,status) VALUES(2,'Xon_Xoff',1) ON CONFLICT(handshakekey)DO NOTHING;
INSERT into handshake(handshakekey,handshakename,status) VALUES(3,'RTS_CTS',1) ON CONFLICT(handshakekey)DO NOTHING;
INSERT into handshake(handshakekey,handshakename,status) VALUES(4,'BOTH',1) ON CONFLICT(handshakekey)DO NOTHING;

INSERT into resultsamplefrom(resultsamplekey,resultsamplename,status) VALUES(1,'IFACER',1) ON CONFLICT(resultsamplekey)DO NOTHING;
INSERT into resultsamplefrom(resultsamplekey,resultsamplename,status) VALUES(2,'LimsTestOrder',1) ON CONFLICT(resultsamplekey)DO NOTHING;
INSERT into resultsamplefrom(resultsamplekey,resultsamplename,status) VALUES(3,'DataFileName',1) ON CONFLICT(resultsamplekey)DO NOTHING;

INSERT into instrumenttype(insttypekey,insttypename,status) VALUES(4,'TCP_SERVER',1) ON CONFLICT(insttypekey)DO NOTHING;
INSERT into instrumenttype(insttypekey,insttypename,status) VALUES(5,'ICPMODBUS',1) ON CONFLICT(insttypekey)DO NOTHING;

INSERT into conversiontype(conversiontypekey,conversiontypename,status) VALUES(1,'NONE',1) ON CONFLICT(conversiontypekey)DO NOTHING;
INSERT into conversiontype(conversiontypekey,conversiontypename,status) VALUES(2,'Temperature_Celcius',1) ON CONFLICT(conversiontypekey)DO NOTHING;
INSERT into conversiontype(conversiontypekey,conversiontypename,status) VALUES(3,'Temperature_Farenheit',1) ON CONFLICT(conversiontypekey)DO NOTHING;

ALTER TABLE IF EXISTS equipment ADD COLUMN IF NOT EXISTS cmmsetting boolean;



DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'rctcpfiledetails_seq' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE rctcpfiledetails_seq;
   ELSIF _kind = 'S' THEN  
     
   ELSE                  
    
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.rctcpfiledetails
(
    filecode integer NOT NULL,
    fileuuid character varying(255) COLLATE pg_catalog."default",
    filename character varying(255) COLLATE pg_catalog."default",
    instrumentkey integer,
    methodkey integer,
    nequipmentcode integer,
    CONSTRAINT rctcpfiledetails_pkey PRIMARY KEY (filecode)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.rctcpfiledetails
    OWNER to postgres;
    
ALTER TABLE IF Exists rctcpresultdetails ADD COLUMN IF NOT EXISTS equipment_nequipmentcode Integer;
    
DO
$do$
declare
  multiusergroupcount integer :=0;
begin
SELECT count(*) into multiusergroupcount FROM
information_schema.table_constraints WHERE constraint_name='fk9f8bj6t04wgt00a09foqbdcjo'
AND table_name='rctcpresultdetails';
 IF multiusergroupcount =0 THEN
    ALTER TABLE ONLY rctcpresultdetails ADD CONSTRAINT fk9f8bj6t04wgt00a09foqbdcjo FOREIGN KEY (equipment_nequipmentcode) REFERENCES equipment(nequipmentcode);
   END IF;
END
$do$; 
    
DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns 
        WHERE table_name = 'rctcpresultdetails' 
        AND column_name = 'instmasterkey'
        AND is_nullable = 'NO'
    ) THEN
        ALTER TABLE public.rctcpresultdetails
        ALTER COLUMN instmasterkey DROP NOT NULL;
    END IF;
END $$;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'parity_paritykey_seq' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE parity_paritykey_seq;
   ELSIF _kind = 'S' THEN  
     
   ELSE                  
    
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.parity
(
    paritykey integer NOT NULL DEFAULT nextval('parity_paritykey_seq'::regclass),
    parityname character varying(255) COLLATE pg_catalog."default",
    status integer NOT NULL,
    CONSTRAINT parity_pkey PRIMARY KEY (paritykey)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.parity
    OWNER to postgres;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'resultsamplefrom_resultsamplekey_seq' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE resultsamplefrom_resultsamplekey_seq;
   ELSIF _kind = 'S' THEN  
     
   ELSE                  
    
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.resultsamplefrom
(
    resultsamplekey integer NOT NULL DEFAULT nextval('resultsamplefrom_resultsamplekey_seq'::regclass),
    resultsamplename character varying(255) COLLATE pg_catalog."default",
    status integer NOT NULL,
    CONSTRAINT resultsamplefrom_pkey PRIMARY KEY (resultsamplekey)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.resultsamplefrom
    OWNER to postgres;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'handshake_handshakekey_seq' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE handshake_handshakekey_seq;
   ELSIF _kind = 'S' THEN  
     
   ELSE                  
    
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.handshake
(
    handshakekey integer NOT NULL DEFAULT nextval('handshake_handshakekey_seq'::regclass),
    handshakename character varying(255) COLLATE pg_catalog."default",
    status integer NOT NULL,
    CONSTRAINT handshake_pkey PRIMARY KEY (handshakekey)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.handshake
    OWNER to postgres;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'stopbits_stopbitkey_seq' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE stopbits_stopbitkey_seq;
   ELSIF _kind = 'S' THEN  
     
   ELSE                  
    
   END IF;
END
$do$;
    
CREATE TABLE IF NOT EXISTS public.stopbits
(
    stopbitkey integer NOT NULL DEFAULT nextval('stopbits_stopbitkey_seq'::regclass),
    status integer NOT NULL,
    stopbitname character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT stopbits_pkey PRIMARY KEY (stopbitkey)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.stopbits
    OWNER to postgres;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'conversiontype_conversiontypekey_seq' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE conversiontype_conversiontypekey_seq;
   ELSIF _kind = 'S' THEN  
     
   ELSE                  
    
   END IF;
END
$do$;


CREATE TABLE IF NOT EXISTS public.conversiontype
(
    conversiontypekey integer NOT NULL DEFAULT nextval('conversiontype_conversiontypekey_seq'::regclass),
    conversiontypename character varying(255) COLLATE pg_catalog."default",
    status integer NOT NULL,
    CONSTRAINT conversiontype_pkey PRIMARY KEY (conversiontypekey)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.conversiontype
    OWNER to postgres;

DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'communicationsetting_cmmsettingcode_seq' 
   INTO  _kind;

   IF NOT FOUND THEN CREATE SEQUENCE communicationsetting_cmmsettingcode_seq;
   ELSIF _kind = 'S' THEN  
     
   ELSE                  
    
   END IF;
END
$do$;

CREATE TABLE IF NOT EXISTS public.communicationsetting
(
    cmmsettingcode bigint NOT NULL DEFAULT nextval('communicationsetting_cmmsettingcode_seq'::regclass),
    baudrate character varying(255) COLLATE pg_catalog."default",
    channalno character varying(255) COLLATE pg_catalog."default",
    comportno integer,
    databits character varying(255) COLLATE pg_catalog."default",
    ipaddress character varying(255) COLLATE pg_catalog."default",
    maxcurrent character varying(255) COLLATE pg_catalog."default",
    maxdatapoint character varying(255) COLLATE pg_catalog."default",
    mincurrent character varying(255) COLLATE pg_catalog."default",
    mindatapoint character varying(255) COLLATE pg_catalog."default",
    nequipmentcode integer,
    tcpportno integer,
    tidlesecond integer,
    conversiontype integer,
    handshake integer,
    cmmtype integer,
    parity integer,
    rsampleidf integer,
    stopbit integer,
    CONSTRAINT communicationsetting_pkey PRIMARY KEY (cmmsettingcode),
    CONSTRAINT fkan3ncrik40dltk3tahhkf6xyd FOREIGN KEY (conversiontype)
        REFERENCES public.conversiontype (conversiontypekey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fklt7oga656tydp353wsjdmf803 FOREIGN KEY (stopbit)
        REFERENCES public.stopbits (stopbitkey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkmycp1vklhof9v8uuodj6p82wt FOREIGN KEY (handshake)
        REFERENCES public.handshake (handshakekey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkn8c3kpfip02ejxekn6nerajyj FOREIGN KEY (cmmtype)
        REFERENCES public.instrumenttype (insttypekey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkofrf7vemj4xgut7cxdtg9dvnw FOREIGN KEY (parity)
        REFERENCES public.parity (paritykey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkrvmpsh14ba2mpfteq0nt401ww FOREIGN KEY (rsampleidf)
        REFERENCES public.resultsamplefrom (resultsamplekey) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.communicationsetting OWNER to postgres;

ALTER TABLE IF Exists equipment ADD COLUMN IF NOT EXISTS fk_cmmsettingcode Integer;
    
DO
$do$
declare
  multiusergroupcount integer :=0;
begin
SELECT count(*) into multiusergroupcount FROM
information_schema.table_constraints WHERE constraint_name='fk2esxrtlkh4sju5fb40xhbltf9'
AND table_name='equipment';
 IF multiusergroupcount =0 THEN
    ALTER TABLE ONLY equipment ADD CONSTRAINT fk2esxrtlkh4sju5fb40xhbltf9 FOREIGN KEY (fk_cmmsettingcode) REFERENCES communicationsetting(cmmsettingcode);
   END IF;
END
$do$;
 
INSERT into stopbits(stopbitkey,stopbitname,status) VALUES(1,'-1',1) ON CONFLICT(stopbitkey)DO NOTHING;
INSERT into stopbits(stopbitkey,stopbitname,status) VALUES(2,'0',1) ON CONFLICT(stopbitkey)DO NOTHING;
INSERT into stopbits(stopbitkey,stopbitname,status) VALUES(3,'1',1) ON CONFLICT(stopbitkey)DO NOTHING;
INSERT into stopbits(stopbitkey,stopbitname,status) VALUES(4,'1.5',1) ON CONFLICT(stopbitkey)DO NOTHING;
INSERT into stopbits(stopbitkey,stopbitname,status) VALUES(5,'2',1) ON CONFLICT(stopbitkey)DO NOTHING;

INSERT into parity(paritykey,parityname,status) VALUES(1,'NONE',1) ON CONFLICT(paritykey)DO NOTHING;
INSERT into parity(paritykey,parityname,status) VALUES(2,'ODD',1) ON CONFLICT(paritykey)DO NOTHING;
INSERT into parity(paritykey,parityname,status) VALUES(3,'EVEN',1) ON CONFLICT(paritykey)DO NOTHING;

INSERT into handshake(handshakekey,handshakename,status) VALUES(1,'NONE',1) ON CONFLICT(handshakekey)DO NOTHING;
INSERT into handshake(handshakekey,handshakename,status) VALUES(2,'Xon_Xoff',1) ON CONFLICT(handshakekey)DO NOTHING;
INSERT into handshake(handshakekey,handshakename,status) VALUES(3,'RTS_CTS',1) ON CONFLICT(handshakekey)DO NOTHING;
INSERT into handshake(handshakekey,handshakename,status) VALUES(4,'BOTH',1) ON CONFLICT(handshakekey)DO NOTHING;

INSERT into resultsamplefrom(resultsamplekey,resultsamplename,status) VALUES(1,'IFACER',1) ON CONFLICT(resultsamplekey)DO NOTHING;
INSERT into resultsamplefrom(resultsamplekey,resultsamplename,status) VALUES(2,'LimsTestOrder',1) ON CONFLICT(resultsamplekey)DO NOTHING;
INSERT into resultsamplefrom(resultsamplekey,resultsamplename,status) VALUES(3,'DataFileName',1) ON CONFLICT(resultsamplekey)DO NOTHING;

INSERT into instrumenttype(insttypekey,insttypename,status) VALUES(4,'TCP_SERVER',1) ON CONFLICT(insttypekey)DO NOTHING;
INSERT into instrumenttype(insttypekey,insttypename,status) VALUES(5,'ICPMODBUS',1) ON CONFLICT(insttypekey)DO NOTHING;

INSERT into conversiontype(conversiontypekey,conversiontypename,status) VALUES(1,'NONE',1) ON CONFLICT(conversiontypekey)DO NOTHING;
INSERT into conversiontype(conversiontypekey,conversiontypename,status) VALUES(2,'Temperature_Celcius',1) ON CONFLICT(conversiontypekey)DO NOTHING;
INSERT into conversiontype(conversiontypekey,conversiontypename,status) VALUES(3,'Temperature_Farenheit',1) ON CONFLICT(conversiontypekey)DO NOTHING;

ALTER TABLE IF EXISTS equipment ADD COLUMN IF NOT EXISTS cmmsetting boolean;

DO
$do$
BEGIN
   -- Try to create the sequence and catch the exception if it already exists
   BEGIN
      CREATE SEQUENCE LSusershowhidecolumns_usergroupedcolcode_seq;
   EXCEPTION
      WHEN duplicate_table THEN
         -- Sequence already exists, so we do nothing
         RAISE NOTICE 'Sequence LSusershowhidecolumns_usergroupedcolcode_seq already exists.';
   END;
END
$do$;

CREATE TABLE IF NOT EXISTS public.LSusershowhidecolumns
(
    usergroupedcolcode integer NOT NULL DEFAULT nextval('LSusershowhidecolumns_usergroupedcolcode_seq'::regclass),
    gridcolumns text COLLATE pg_catalog."default",
    gridname character varying(100) COLLATE pg_catalog."default",
    sitecode integer,
    usercode integer,
    CONSTRAINT LSusershowhidecolumns_pkey PRIMARY KEY (usergroupedcolcode)
)
WITH (OIDS = FALSE) TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.LSusershowhidecolumns OWNER to postgres;

update instrumenttype set insttypename='TCP_CLIENT' where instrumenttype.insttypename='TCP\IP';
