#!/bin/bash

# building gateway service image
docker build -t weflop/gateway-service-app ../Services/Gateway\ Service/gateway-service/

# building accounts service image
docker build -t weflop/accounts-service-app ../Services/Accounts\ Service/accounts-service/

# building feedback service image
docker build -t weflop/feedback-service-app ../Services/Feedback\ Service/feedback-service/

# building chat service image
docker build -t weflop/chat-service-app ../Services/Chat\ Service/chat-service/

# building game service image
docker build -t weflop/game-service-app ../Services/Game\ Service/GameService/