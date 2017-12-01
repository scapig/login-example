## tapi-oauth-login

## Building
``
sbt clean test it:test component:test
``

## Packaging
``
sbt universal:package-zip-tarball
docker build -t tapi-oauth-login .
``

## Running
``
docker run -p7050:7050 -i -a stdin -a stdout -a stderr tapi-oauth-login sh start-docker.sh
``