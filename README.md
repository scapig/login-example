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

## Running
``
docker run -p7050:7050 -i -a stdin -a stdout -a stderr login-example sh start-docker.sh
``