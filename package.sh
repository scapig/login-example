#!/bin/sh
sbt universal:package-zip-tarball
docker build -t login-example .
docker tag login-example scapig/login-example
docker push scapig/login-example
