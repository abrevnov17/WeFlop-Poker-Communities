#!/bin/bash

# building accounts service image
docker build -t weflop/accounts-service-app .

# pushing to docker hub
docker push weflop/accounts-service-app:latest