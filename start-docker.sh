#!/bin/sh
SCRIPT=$(find . -type f -name login-example)
rm -f login-example*/RUNNING_PID
exec $SCRIPT -Dhttp.port=9040 -J-Xms16M -J-Xmx64m
