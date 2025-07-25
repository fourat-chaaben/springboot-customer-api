# Use the OpenJDK 17 base image
FROM openjdk:17-bullseye

# Set the working directory to /app
WORKDIR /app

# Copy the compiled JAR file into the container and rename it to app.jar
COPY build/libs/*.jar app.jar

# Copy the start script into the container
COPY start.sh .

# Make the start script executable
RUN chmod 770 start.sh

# Set the start script as the container's entrypoint
CMD ["./start.sh"]
