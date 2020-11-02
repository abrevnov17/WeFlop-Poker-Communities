#!/bin/bash

# establishing context
kubectl config set-context --current --namespace=production

# deleting deployments
kubectl delete deployment game-service-app
kubectl delete deployment accounts-service-app
kubectl delete deployment feedback-service-app
kubectl delete deployment chat-service-app
kubectl delete deployment gateway-service-app
kubectl delete deployment sessions-service-app
kubectl delete deployment redis

# deleting services
kubectl delete service game-service-app
kubectl delete service accounts-service-app
kubectl delete service feedback-service-app
kubectl delete service chat-service-app
kubectl delete service gateway-service-app
kubectl delete service sessions-service-app
kubectl delete service redis

kubectl delete service game-service-app

# deleting external services
kubectl delete service game-service-db
kubectl delete service accounts-service-db
kubectl delete service feedback-service-db

# deleting endpoints
# kubectl delete endpoints game-service-db

# deleting autoscalers
# kubectl delete hpa game-service-app

# deleting mappings
# kubectl delete mappings game-service-mapping

# deleting namespaces
kubectl delete namespaces production