#!/bin/sh
# use for local building the container 

mvn clean install
docker build --tag weather:1.0

docker run --publish 8080:8080 --detach --name ww weather:1.0