#!/bin/bash

# generating deployments
for FILE in deployments; do kubectl apply -f $FILE; done

# creating services
for FILE in services; do kubectl apply -f $FILE; done

# creating external endpoints
for FILE in endpoints; do kubectl apply -f $FILE; done

# necessary autoscaling
kubectl autoscale deployment game-service-app --cpu-percent=80 --min=1 --max=5

# creating mappings
for FILE in mappings; do kubectl apply -f $FILE; done
