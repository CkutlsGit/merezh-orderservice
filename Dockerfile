FROM eclipse-temurin:21-alpine

WORKDIR /app

RUN addgroup -S orderservice && adduser -S orderservice -G orderservice

COPY target/orderservice-merezh-0.0.1-SNAPSHOT.jar /app/orderservice-merezh.jar

RUN chown -R orderservice:orderservice /app

USER orderservice

ENTRYPOINT ["java", "-jar", "orderservice-merezh.jar"]