--
-- Default database data for TILA
--

INSERT INTO PROXY VALUES (1,"","","",0,"true");

INSERT INTO CACHES VALUES (1,"Google Maps","google","iterate on google servers",10000,"true",1);
INSERT INTO CACHES VALUES (2,"Open Street Map","osm","http://tile.openstreetmap.org",10000,"true",2);

INSERT INTO TYPES VALUES (1,"Google Maps","googleMapsCache");
INSERT INTO TYPES VALUES (2,"Mapnik (OSM)","mapnikCache");
INSERT INTO TYPES VALUES (3,"TMS","tmsCache");
INSERT INTO TYPES VALUES (4,"WMS","wmsCache");
INSERT INTO TYPES VALUES (5,"WTMS","wtmsCache");

INSERT INTO CONFIGS VALUES (1,"cachePath","/tmp/tila");

DROP SEQUENCE "APP"."CACHES_ID_SEQ";
DROP  SEQUENCE "APP"."TYPES_ID_SEQ";

CREATE SEQUENCE "APP"."CACHES_ID_SEQ"
    AS INTEGER
    START WITH 1
    INCREMENT BY 1
    NO CYCLE
;

CREATE SEQUENCE "APP"."TYPES_ID_SEQ"
    AS INTEGER
    START WITH 1
    INCREMENT BY 1
    NO CYCLE
;