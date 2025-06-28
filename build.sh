# Compile the project
./gradlew clean build

# Build the Docker image
docker build -t springboot-customer-api .

# Fire up the containers
docker compose up -d
