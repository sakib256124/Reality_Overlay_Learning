#!/bin/sh

APP_HOME=$(cd "${APP_HOME:-./}" && pwd -P) || exit
CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar

DEFAULT_JVM_OPTS="-Xmx64m -Xms64m"

if [ -n "$JAVA_HOME" ]; then
    JAVACMD="$JAVA_HOME/bin/java"
else
    JAVACMD=java
fi

exec "$JAVACMD" $DEFAULT_JVM_OPTS $JAVA_OPTS $GRADLE_OPTS \
    -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
