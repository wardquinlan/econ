#!/bin/sh

if [ "$ECON_HOME" == "" ]; then
  echo ECON_HOME not defined
  exit 1
fi

CLASSPATH=$ECON_HOME/econ.d
CLASSPATH=$CLASSPATH:$ECON_HOME/econ.d/econ.jar
CLASSPATH=$CLASSPATH:$ECON_HOME/econ.d/log4j-1.2.14.jar
CLASSPATH=$CLASSPATH:$ECON_HOME/econ.d/commons-logging-1.1.jar
CLASSPATH=$CLASSPATH:$ECON_HOME/econ.d/postgresql-9.3-1104.jdbc41.jar
CMD="$JAVA_HOME/bin/java -classpath $CLASSPATH -Xmx128m econ.Main $@"
eval $CMD
