#!/bin/sh
# Gradle wrapper script for Unix

##############################################################################
# Attempt to find JAVA_HOME if not already set
##############################################################################

# Determine the Java command to use
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        JAVACMD="$JAVA_HOME/jre/sh/java"
    else
        JAVACMD="$JAVA_HOME/bin/java"
    fi
    if [ ! -x "$JAVACMD" ] ; then
        die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME"
    fi
else
    JAVACMD="java"
    which java >/dev/null 2>&1 || die "ERROR: JAVA_HOME is not set and no 'java' command could be found."
fi

##############################################################################
# Determine the application directory
##############################################################################

APP_HOME="`pwd -P`"

##############################################################################
# Determine wrapper jar location
##############################################################################

WRAPPER_JAR="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

##############################################################################
# Execute Gradle
##############################################################################

exec "$JAVACMD" \
  -classpath "$WRAPPER_JAR" \
  org.gradle.wrapper.GradleWrapperMain \
  "$@"
