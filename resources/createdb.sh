#!/bin/sh

if [ `whoami` != 'postgres' ]; then
  echo $0: must run as \'postgres\'
  exit 1
fi

if [ "$ECON_DATABASE" = "" ]; then
  echo $0: ECON_DATABASE not defined
  exit 1
fi

if [ "$ECON_USERNAME" = "" ]; then
  echo $0: ECON_USERNAME not defined
  exit 1
fi

if [ "$ECON_PASSWORD" = "" ]; then
  echo $0: ECON_PASSWORD not defined
  exit 1
fi

echo $0: attempting to drop database \'$ECON_DATABASE\'
dropdb $ECON_DATABASE 2>/dev/null
if [ $? != 1 ]; then
  echo $0: database \'$ECON_DATABASE\' not found';' skipping drop
fi

echo $0: attempting to create database \'$ECON_DATABASE\'
createdb $ECON_DATABASE
if [ $? != 0 ]; then
  echo $0: could not create database \'$ECON_DATABASE\'
  exit 1
fi

echo $0: attempting to create user \'$ECON_USERNAME\'
psql << EOF
  DROP USER IF EXISTS $ECON_USERNAME;
  CREATE USER $ECON_USERNAME WITH PASSWORD '$ECON_PASSWORD';
EOF

echo $0: database $ECON_DATABASE created

