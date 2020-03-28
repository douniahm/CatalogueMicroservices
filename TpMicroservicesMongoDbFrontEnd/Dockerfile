FROM anapsix/alpine-java AS builder
LABEL maintainer="dounia.hm@hotmail.com"
COPY /target/TP_Microservices_Gesion_users-0.0.1-SNAPSHOT /app/users_microservice.jar
CMD ["java","-jar","/users_microservice.jar"]
