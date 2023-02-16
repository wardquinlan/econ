#!/bin/sh

if [ "$ES_HOME" == "" ]; then
  echo ES_HOME not defined
  exit 1
fi

CLASSPATH=$ES_HOME/es.d
CLASSPATH=$CLASSPATH:$ES_HOME/es.d/es.jar
CLASSPATH=$CLASSPATH:$ES_HOME/es.d/log4j-1.2.14.jar
CLASSPATH=$CLASSPATH:$ES_HOME/es.d/commons-logging-1.1.jar
CLASSPATH=$CLASSPATH:$ES_HOME/es.d/postgresql-9.3-1104.jdbc41.jar
CLASSPATH=$CLASSPATH:$ES_HOME/es.d/commons-cli-1.4.jar
CMD="$JAVA_HOME/bin/java -classpath $CLASSPATH -Xmx128m $JAVA_OPTS econ.core.Main $@"
eval $CMD
