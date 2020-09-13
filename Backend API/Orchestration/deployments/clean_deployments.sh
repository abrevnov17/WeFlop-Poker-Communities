#!/bin/bash

# generating deployments
kubectl delete deployment game-service-app-deployment
kubectl delete deployment accounts-service-app-deployment
kubectl delete deployment feedback-service-app-deployment
kubectl delete deployment chat-service-app-deployment