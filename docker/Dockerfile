# Set the base image
FROM openjdk:8-jre-alpine

MAINTAINER aohan.yang@cityfruit.io

# Copy files
RUN  mkdir /var/lib/mozi/
WORKDIR /var/lib/mozi/
COPY run.sh  ./
COPY score.json ./
COPY mozi-0.0.1-SNAPSHOT.jar app.jar

CMD ["/var/lib/mozi/run.sh"]
EXPOSE 9877
