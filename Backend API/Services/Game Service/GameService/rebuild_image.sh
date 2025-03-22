#!/bin/bash

# building game service image
docker build -t weflop/game-service-app .

# pushing to docker hub
docker push weflop/game-service-app:latest