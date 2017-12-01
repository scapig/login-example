FROM openjdk:8

COPY target/universal/tapi-oauth-login-*.tgz .
COPY start-docker.sh .
RUN chmod +x start-docker.sh
RUN tar xvf tapi-oauth-login-*.tgz

EXPOSE 7050