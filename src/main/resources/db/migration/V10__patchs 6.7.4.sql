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
