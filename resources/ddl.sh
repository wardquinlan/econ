#!/bin/sh

if [ "$ECON_DATABASE" = "" ]; then
  echo $0: ECON_DATABASE not defined
  exit 1
fi

if [ "$ECON_USER" = "" ]; then
  echo $0: ECON_USER not defined
  exit 1
fi

echo ------------------- WARNING!!! ------------------
echo THIS WILL DELETE ALL ECON DATA!!!
echo Are you sure you want to continue?
echo ""
read tt
if [ "$tt" != "y" ]; then
  echo $0: aborting
  exit 1
fi

psql --username=$ECON_USER $ECON_DATABASE << EOF

DROP TABLE IF EXISTS SERIES_DATA;
DROP TABLE IF EXISTS SERIES;

CREATE TABLE SERIES(
  ID SERIAL PRIMARY KEY, -- e.g. 100
  LABEL VARCHAR(100) UNIQUE NOT NULL, -- e.g. 'CORPCCC'
  TITLE VARCHAR(500) NOT NULL, -- e.g. 'ICE BofA CCC & Lower US High Yield Index Effective Yield'
  SOURCE VARCHAR(100) NOT NULL, -- e.g. 'FRED'
  SOURCE_ID VARCHAR(100), -- e.g. 'BAMLH0A3HYCEY'
  NOTES TEXT); -- free-form notes 

CREATE TABLE SERIES_DATA(
	SERIES_ID INTEGER REFERENCES SERIES(ID) NOT NULL,
	DATESTAMP DATE NOT NULL,
    VALUE REAL NOT NULL);
EOF

