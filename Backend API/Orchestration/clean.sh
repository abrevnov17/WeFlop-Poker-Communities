#!/bin/bash

# deleting deployments
kubectl delete deployment game-service-app-deployment
kubectl delete deployment accounts-service-app-deployment
kubectl delete deployment feedback-service-app-deployment
kubectl delete deployment chat-service-app-deployment

# deleting services
kubectl delete service game-service-app-service
kubectl delete service accounts-service-app-service
kubectl delete service feedback-service-app-service
kubectl delete service chat-service-app-service

# deleting external services
kubectl delete service game-service-db-service

# deleting endpoints
kubectl delete endpoints game-service-db-endpoint

# deleting autoscalers
kubectl delete hpa game-service-app-deployment