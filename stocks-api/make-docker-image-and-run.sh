#!/bin/bash

echo "make backend docker image"
cd backend
mvn clean package
docker rmi stock-app-backend:0.0.1
docker build --tag=stock-app-backend:0.0.1 .
cd ..

echo "make frontend docker image"
cd frontend
npm install
npm run build
docker rmi stock-app-frontend:0.0.1
docker build --tag=stock-app-frontend:0.0.1 .
cd ..

echo "docker compose both images together"
docker-compose up
