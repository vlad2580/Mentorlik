#!/bin/bash
# Script to rebuild backend JAR and update Docker image
# Author: AI Assistant
# Date: 2025-04-14

echo -e "\e[1;32m========== Rebuilding Mentorlik Backend ==========\e[0m"

# Step 1: Check if Docker is running
echo -e "\e[1;36mChecking if Docker is running...\e[0m"
if ! docker info > /dev/null 2>&1; then
    echo -e "\e[1;31mERROR: Docker is not running. Please start Docker and try again.\e[0m"
    exit 1
fi
echo -e "\e[1;32mDocker is running!\e[0m"

# Step 2: Build the JAR file
echo -e "\e[1;36mBuilding JAR file...\e[0m"
cd "$(dirname "$0")/backend" || exit 1
./mvnw clean package -DskipTests
if [ $? -ne 0 ]; then
    echo -e "\e[1;31mERROR: Failed to build JAR file.\e[0m"
    exit 1
fi
echo -e "\e[1;32mJAR file built successfully!\e[0m"

# Step 3: Rebuild Docker image
echo -e "\e[1;36mRebuilding Docker image...\e[0m"
cd "$(dirname "$0")/docker" || exit 1
docker-compose build backend
if [ $? -ne 0 ]; then
    echo -e "\e[1;31mERROR: Failed to build Docker image.\e[0m"
    exit 1
fi
echo -e "\e[1;32mDocker image rebuilt successfully!\e[0m"

# Step 4: Return to root directory
cd "$(dirname "$0")" || exit 1

# Step 5: Show instructions
echo ""
echo -e "\e[1;32m========== Next Steps ==========\e[0m"
echo -e "\e[1;33mBackend JAR has been rebuilt and Docker image has been updated.\e[0m"
echo ""
echo -e "\e[1;36mTo restart only the backend container, run:\e[0m"
echo -e "   cd docker"
echo -e "   docker-compose up -d backend"
echo ""
echo -e "\e[1;36mTo restart all containers, run:\e[0m"
echo -e "   cd docker"
echo -e "   docker-compose down"
echo -e "   docker-compose up -d"
echo ""
echo -e "\e[1;36mTo check backend logs, run:\e[0m"
echo -e "   docker logs mentorlik_backend --tail 50"
echo ""
echo -e "\e[1;32m========== Done ==========\e[0m" 