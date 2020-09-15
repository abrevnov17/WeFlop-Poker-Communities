#!/bin/bash

# deleting deployments
kubectl delete deployment game-service-app
kubectl delete deployment accounts-service-app
kubectl delete deployment feedback-service-app
kubectl delete deployment chat-service-app

# deleting services
kubectl delete service game-service-app
kubectl delete service accounts-service-app
kubectl delete service feedback-service-app
kubectl delete service chat-service-app

# deleting external services
kubectl delete service game-service-db

# deleting endpoints
kubectl delete endpoints game-service-db

# deleting autoscalers
kubectl delete hpa game-service-app