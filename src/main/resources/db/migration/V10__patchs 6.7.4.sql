ALTER TABLE IF Exists elnmaterialinventory ADD Column IF NOT EXISTS inventoryname character varying(255);

ALTER TABLE IF EXISTS RCTCPResultDetails ADD COLUMN IF NOT EXISTS valueloaded INTEGER DEFAULT 0;

ALTER TABLE IF Exists rctcpresultdetails ADD COLUMN IF NOT EXISTS filecode Integer;
    
DO
$do$
declare
  multiusergroupcount integer :=0;
begin
SELECT count(*) into multiusergroupcount FROM
information_schema.table_constraints WHERE constraint_name='fk4lfwyaj6ouryj2dk87wn0ij37'
AND table_name='rctcpresultdetails';
 IF multiusergroupcount =0 THEN
    ALTER TABLE ONLY rctcpresultdetails ADD CONSTRAINT fk4lfwyaj6ouryj2dk87wn0ij37 FOREIGN KEY (filecode) REFERENCES rctcpfiledetails(filecode);
   END IF;
END
$do$; 

ALTER TABLE IF Exists lsprojectmaster ADD Column IF NOT EXISTS duedate character varying(255);
ALTER TABLE IF Exists lsprojectmaster ADD Column IF NOT EXISTS startdate timestamp without time zone;
ALTER TABLE IF Exists lsprojectmaster ADD Column IF NOT EXISTS enddate timestamp without time zone;

DELETE FROM datatype WHERE datatypekey = 2 AND EXISTS ( SELECT 1 FROM datatype WHERE datatypekey = 2);

DO
$do$
BEGIN
    IF NOT EXISTS (
        SELECT 1 
        FROM datatype 
        WHERE datatypekey = 1 AND datatypename = 'string'
    ) THEN
        UPDATE datatype 
        SET datatypename = 'string' 
        WHERE datatypekey = 1;
    END IF;
END
$do$;

DO
$do$
BEGIN
    -- Check if datatypekey = 3 exists
    IF EXISTS (
        SELECT 1 
        FROM datatype 
        WHERE datatypekey = 3
    ) THEN
        -- Check if datatypename is NOT 'Number'
        IF EXISTS (
            SELECT 1 
            FROM datatype 
            WHERE datatypekey = 3 AND datatypename <> 'Number'
        ) THEN
            -- Update datatypename to 'Number'
            UPDATE datatype 
            SET datatypename = 'Number' 
            WHERE datatypekey = 3;
        END IF;
    ELSE
        -- Insert a new record with datatypekey = 3 and datatypename = 'Number'
        INSERT INTO datatype (datatypekey, datatypename)
        VALUES (3, 'Number');
    END IF;
END
$do$;

