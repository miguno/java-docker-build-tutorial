# Multi-stage build setup (https://docs.docker.com/develop/develop-images/multistage-build/)

# Stage 1 (to create a "build" image, ~140MB)
FROM adoptopenjdk/openjdk11:latest
ARG MAVEN_VERSION="3.8.1"
ARG USER_HOME_DIR="/root"
ARG SHA="b98a1905eb554d07427b2e5509ff09bd53e2f1dd7a0afa38384968b113abef02"
ARG BASE_URL="https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries"

RUN mkdir -p /usr/share/maven \
    && curl -Lso  /tmp/maven.tar.gz ${BASE_URL}/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
    && echo "${SHA}  /tmp/maven.tar.gz" | sha256sum -c - \
    && tar -xzC /usr/share/maven --strip-components=1 -f /tmp/maven.tar.gz \
    && rm -v /tmp/maven.tar.gz \
    && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

ENV MAVEN_HOME /usr/share/maven
ENV MAVEN_CONFIG "${USER_HOME_DIR}/.m2"

CMD ["/usr/bin/mvn"]
