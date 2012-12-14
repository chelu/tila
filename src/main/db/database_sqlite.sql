-- ----------------------------------------------
-- Tables
-- ----------------------------------------------

CREATE TABLE CACHES (
       "ID"  	    INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, 
       "NAME" 	    VARCHAR(100), 
       "PATH" 	    VARCHAR(100), 
       "URL" 	    VARCHAR(200), 
       "SIZE" 	    INTEGER DEFAULT 1000, 
       "ACTIVE"	    BOOLEAN DEFAULT true, 
       "TYPE" 	    INTEGER, 
       "AGE" 	    INTEGER DEFAULT 30, 
       FOREIGN KEY (TYPE) REFERENCES TYPES (ID)
);

CREATE TABLE CONFIG (
       "ID"  	    INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, 
       "NAME" 	    VARCHAR(100), 
       "VALUE" 	    VARCHAR(100)
);

CREATE TABLE TYPES (
       "ID"  	   INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, 
       "NAME" 	   VARCHAR(100), 
       "BEANNAME"  VARCHAR(100) NOT NULL DEFAULT 'google'
);

CREATE TABLE PROXY (
       "ID"		  INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, 
       "HOST" 	  	  VARCHAR(250), 
       "USERNAME"  	  VARCHAR(250), 
       "PASSWORD"  	  VARCHAR(250), 
       "PORT" 	   	  INTEGER, 
       "DIRECTCONNECTION" BOOLEAN DEFAULT true
);


