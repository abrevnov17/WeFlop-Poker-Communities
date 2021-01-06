#!/bin/bash

# building sessions service image
docker build -t weflop/sessions-service-app .

# pushing to docker hub
docker push weflop/sessions-service-app:latest