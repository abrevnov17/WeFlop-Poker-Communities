#!/bin/bash

# creating namespaces
for FILE in namespaces; do kubectl apply -f $FILE; done

# establishing context
kubectl config set-context --current --namespace=production

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

# create hosts
for FILE in hosts; do kubectl apply -f $FILE; done

# creating modules
for FILE in modules; do kubectl apply -f $FILE; done

# setting up ambassador
# kubectl apply -f https://www.getambassador.io/yaml/aes-crds.yaml && \
# kubectl wait --for condition=established --timeout=90s crd -lproduct=aes && \
# kubectl apply -f https://www.getambassador.io/yaml/aes.yaml && \
# kubectl -n ambassador wait --for condition=available --timeout=90s deploy -lproduct=aes