#!/bin/sh
# Shows the server version
export CALLUP_HOME="$PWD"
java -Duser.country=US -Duser.language=en -classpath $CALLUP_HOME/lib/callup-commons* com.phonytive.callup.Version
