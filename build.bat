@REM # Compile the project
call ./gradlew clean build

@REM # Build the image
call docker build --progress plain -t springboot-customer-api .

@REM # start the containers
call docker compose up -d
