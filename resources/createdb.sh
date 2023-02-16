#!/bin/sh

if [ `whoami` != 'postgres' ]; then
  echo $0: must run as \'postgres\'
  exit 1
fi

if [ "$ES_DATABASE" = "" ]; then
  echo $0: ES_DATABASE not defined
  exit 1
fi

if [ "$ES_USERNAME" = "" ]; then
  echo $0: ES_USERNAME not defined
  exit 1
fi

if [ "$ES_PASSWORD" = "" ]; then
  echo $0: ES_PASSWORD not defined
  exit 1
fi

echo $0: attempting to drop database \'$ES_DATABASE\'
dropdb $ES_DATABASE 2>/dev/null
if [ $? != 1 ]; then
  echo $0: database \'$ES_DATABASE\' not found';' skipping drop
fi

echo $0: attempting to create database \'$ES_DATABASE\'
createdb $ES_DATABASE
if [ $? != 0 ]; then
  echo $0: could not create database \'$ES_DATABASE\'
  exit 1
fi

echo $0: attempting to create user \'$ES_USERNAME\'
psql << EOF
  DROP USER IF EXISTS $ES_USERNAME;
  CREATE USER $ES_USERNAME WITH PASSWORD '$ES_PASSWORD';
EOF

echo $0: database $ES_DATABASE created

