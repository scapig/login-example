## login-example

This is an example of login mechanism to integrate with the API Platform http://scapig.com for the Oauth 2.0 user flow.

## Building
``
sbt clean test it:test component:test
``

## Packaging
``
sbt universal:package-zip-tarball
docker build -t login-example .
``

## Publishing
``
docker tag login-example scapig/login-example
docker login
docker push scapig/login-example
``

## Running
``
docker run -p9040:9040 -d scapig/login-example
``
