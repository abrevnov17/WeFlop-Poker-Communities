#!/bin/bash

# generating deployments
kubectl apply -f deployments/game_service_app_deployment.yaml
kubectl apply -f deployments/accounts_service_app_deployment.yaml
kubectl apply -f deployments/feedback_service_app_deployment.yaml
kubectl apply -f deployments/chat_service_app_deployment.yaml

# creating services
kubectl apply -f services/game_service_app_service.yaml
kubectl apply -f services/accounts_service_app_service.yaml
kubectl apply -f services/feedback_service_app_service.yaml
kubectl apply -f services/chat_service_app_service.yaml

# creating external services
kubectl apply -f services/game_service_db_service.yaml

# creating external endpoints
kubectl apply -f endpoints/game_service_db_endpoint.yaml

# necessary autoscaling
kubectl autoscale deployment game-service-app-deployment --cpu-percent=80 --min=1 --max=5