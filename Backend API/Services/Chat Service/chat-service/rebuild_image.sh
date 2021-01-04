#!/bin/bash

# building chat service image
docker build -t weflop/chat-service-app .

# pushing to docker hub
docker push weflop/chat-service-app:latest