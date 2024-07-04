#!/bin/bash

../libraries/user-info/gradlew clean build publishToMavenLocal -p ../libraries/user-info/
../libraries/security/gradlew clean build publishToMavenLocal -p ../libraries/security/
../libraries/rabbitmq/gradlew clean build publishToMavenLocal -p ../libraries/rabbitmq/
../libraries/email/gradlew clean build publishToMavenLocal -p ../libraries/email/
../libraries/google-calendar-event/gradlew clean build publishToMavenLocal -p ../libraries/google-calendar-event/
../auth-service-api/gradlew clean build -p ../auth-service-api/
../email-service/gradlew clean build -p ../email-service/
../image-service-api/gradlew clean build -p ../image-service-api/
../trip-service-api/gradlew clean build -p ../trip-service-api/
../mock-notification-consumer/gradlew clean build -p ../mock-notification-consumer/
docker-compose up -d --build --force-recreate --no-deps