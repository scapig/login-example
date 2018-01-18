#!/bin/sh
SCRIPT=$(find . -type f -name login-example)
rm -f login-example*/RUNNING_PID
exec $SCRIPT -Dhttp.port=7050 -J-Xms128M -J-Xmx512m
