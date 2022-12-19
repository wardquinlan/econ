#!/bin/sh

if [ "$ECON_DATABASE" = "" ]; then
  echo $0: ECON_DATABASE not defined
  exit 1
fi

if [ "$ECON_USER" = "" ]; then
  echo $0: ECON_USER not defined
  exit 1
fi

psql --username=$ECON_USER $ECON_DATABASE << EOF

DROP TABLE IF EXISTS SERIES_DATA;
DROP TABLE IF EXISTS SERIES;

CREATE TABLE SERIES(
  ID VARCHAR(50) PRIMARY KEY, -- e.g. 'CORPCCC'
  TITLE VARCHAR(200), -- e.g. 'ICE BofA CCC & Lower US High Yield Index Effective Yield'
  SOURCE VARCHAR(50) NOT NULL, -- e.g. 'FRED'
  SOURCE_ID VARCHAR(50), -- e.g. 'BAMLH0A3HYCEY'
  NOTES TEXT); -- free-form notes 

CREATE TABLE SERIES_DATA(
	SERIES_ID VARCHAR(50) REFERENCES SERIES(ID) NOT NULL,
	DATESTAMP DATE NOT NULL);
EOF

