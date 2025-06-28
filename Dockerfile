FROM openjdk:17-bullseye

WORKDIR /app

# Copy the compiled jar (update with actual jar name)
COPY build/libs/customerapp.jar app.jar

# Copy the start script
COPY start.sh start.sh

# Make the script executable
RUN chmod +x start.sh

# Set the start command
CMD ["./start.sh"]
