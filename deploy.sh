#!/bin/sh
# --------------------------------------------------------
# this build script will be triggered from a jenkins job

git clone "need to copy the url here"
cd weather

#build number need to be specified in jenkins file 
docker build -t jenkins-demo:${BUILD_NUMBER} . 

docker tag jenkins-demo:${BUILD_NUMBER} jenkins-demo:latest 

docker run --publish 8000:8080 --detach --name weather jenkins-demo:latest

