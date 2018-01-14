#!/bin/sh
SCRIPT=$(find . -type f -name scapig-oauth-login)
rm -f scapig-oauth-login*/RUNNING_PID
exec $SCRIPT -Dhttp.port=7050 -J-Xms128M -J-Xmx512m
