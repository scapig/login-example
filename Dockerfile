FROM openjdk:8

COPY target/universal/login-example-*.tgz .
COPY start-docker.sh .
RUN chmod +x start-docker.sh
RUN tar xvf login-example-*.tgz

EXPOSE 7050

CMD ["sh", "start-docker.sh"]