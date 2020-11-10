#!/bin/bash

# pulls latest images of all backend applications
docker pull weflop/gateway-service-app
docker pull weflop/accounts-service-app
docker pull weflop/feedback-service-app
docker pull weflop/game-service-app
docker pull weflop/sessions-service-app