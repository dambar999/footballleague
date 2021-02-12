#!/bin/sh
set -x
SERVICE_NAME=footballleague
TAG=1.1


#Docker Build And Tag
(docker build -t SERVICE_NAME -f Dockerfile .)
(docker tag SERVICE_NAME:latest $APP_NAME:$TAG)



