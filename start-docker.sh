#!/bin/sh
SCRIPT=$(find . -type f -name tapi-oauth-login)
exec $SCRIPT -Dhttp.port=7050
