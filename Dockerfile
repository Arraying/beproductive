FROM maven
WORKDIR /opt/bot
COPY pom.xml pom.xml
RUN mvn dependency:go-offline -B
COPY src src
RUN mvn package
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.5.0/wait /wait
RUN chmod +x /wait
COPY entrypoint.sh /usr/local/bin/
ENTRYPOINT ["entrypoint.sh"]