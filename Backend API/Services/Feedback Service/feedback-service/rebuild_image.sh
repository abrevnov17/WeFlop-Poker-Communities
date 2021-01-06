#!/bin/bash

# building feedback service image
docker build -t weflop/feedback-service-app .

# pushing to docker hub
docker push weflop/feedback-service-app:latest