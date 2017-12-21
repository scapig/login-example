FROM openjdk:8

COPY target/universal/scapig-oauth-login-*.tgz .
COPY start-docker.sh .
RUN chmod +x start-docker.sh
RUN tar xvf scapig-oauth-login-*.tgz

EXPOSE 7050

CMD ["sh", "start-docker.sh"]