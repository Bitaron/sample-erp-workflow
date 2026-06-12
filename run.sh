#!/bin/bash

echo "Building backend..."

cd ErpWorkFlow_backend
mvn clean package -DskipTests

cd ..

echo "Starting applications..."

docker compose up --build
