FROM adoptopenjdk:16-jdk-hotspot AS build
ENV GRADLE_OPTS="-Dorg.gradle.daemon=false -Dkotlin.incremental=false"
WORKDIR /app

COPY gradlew settings.gradle ./
COPY gradle ./gradle
RUN ./gradlew --version

COPY build.gradle ./
COPY src ./src
RUN ./gradlew build


FROM oznu/s6-alpine:3.11
LABEL maintainer="Jake Wharton <docker@jakewharton.com>"

RUN apk add --no-cache \
      curl \
      openjdk8-jre \
 && rm -rf /var/cache/* \
 && mkdir /var/cache/apk

ENV \
    # Fail if cont-init scripts exit with non-zero code.
    S6_BEHAVIOUR_IF_STAGE2_FAILS=2 \
    CRON="" \
    ACCESS_TOKEN="" \
    ACCESS_SECRET="" \
    API_KEY="" \
    API_SECRET="" \
    HEALTHCHECK_ID="" \
    HEALTHCHECK_HOST="https://hc-ping.com" \
    PUID="" \
    PGID=""
COPY root/ /
WORKDIR /app
COPY --from=build /app/build/install/singular-solution ./
