## login-example

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
docker tag login-example scapig/login-example:VERSION
docker login
docker push scapig/login-example:VERSION
``

## Running
``
docker run -p9040:9040 -d scapig/login-example:VERSION
``
