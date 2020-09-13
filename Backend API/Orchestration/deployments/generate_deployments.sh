#!/bin/bash

# generating deployments
kubectl apply -f game_service_app_deployment.yaml 
kubectl apply -f accounts_service_app_deployment.yaml 
kubectl apply -f feedback_service_app_deployment.yaml 
kubectl apply -f chat_service_app_deployment.yaml 

# necessary autoscaling
kubectl autoscale deployment game-service-app --cpu-percent=80 --min=1 --max=5