FROM openjdk:8
FROM mongo
COPY ./build/distributions/ /tmp
WORKDIR /tmp
RUN tar -C /tmp/ -zxvf /tmp/githubbot-0.3.tar.gz
ENTRYPOINT ["java","-jar" ,"./githubbot-0.3/githubbot-0.3.jar"]

#"-Dspring.data.mongodb.uri=mongodb://spring-demo-mongo/users","-Djava.security.egd=file:/dev/./urandom","-jar","/springboot-mongo-demo.jar"]