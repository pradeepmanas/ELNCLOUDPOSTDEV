DO
$do$
DECLARE
   _kind "char";
BEGIN
   SELECT relkind
   FROM   pg_class
   WHERE  relname = 'lsmultiusergroup_multiusergroupcode_seq'  
   -- sequence name, optionally schema-qualified
   INTO  _kind;

   IF NOT FOUND THEN       -- name is free
      CREATE SEQUENCE lsmultiusergroup_multiusergroupcode_seq;
   ELSIF _kind = 'S' THEN  -- sequence exists
      -- do nothing?
   ELSE                    -- object name exists for different kind
      -- do something!
   END IF;
END
$do$;