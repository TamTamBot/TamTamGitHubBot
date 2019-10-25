FROM openjdk:8
COPY ./build/distributions/ /tmp
WORKDIR /tmp
RUN apt-get install xz-utils && tar -C /tmp/ -xzvf /tmp/githubbot-0.3.tar.xz && cd githubbot-0.3
ENTRYPOINT ["java","com.github.testbot.Main"]