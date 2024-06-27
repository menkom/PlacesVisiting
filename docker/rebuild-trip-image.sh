#!/bin/bash

../trip-service-api/gradlew clean build -p ../trip-service-api/
docker-compose up --build --force-recreate --no-deps -d trip-service-api