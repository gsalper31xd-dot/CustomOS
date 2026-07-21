#!/bin/sh
SCRIPT_DIR=$(dirname "$0")
exec "$SCRIPT_DIR/gradle/wrapper/gradlew" "$@" 2>/dev/null || exec gradle "$@"
