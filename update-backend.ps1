# Quick script to rebuild and restart backend
# Author: AI Assistant

# Build JAR
Write-Host "Building JAR file..." -ForegroundColor Cyan
Push-Location "$PSScriptRoot\backend"
./mvnw clean package -DskipTests
Pop-Location

# Rebuild and restart Docker container
Write-Host "Rebuilding and restarting backend..." -ForegroundColor Cyan
Push-Location "$PSScriptRoot\docker"
docker-compose build backend
docker-compose up -d backend
Pop-Location

Write-Host "Done! Backend has been updated and restarted." -ForegroundColor Green
Write-Host "Check logs with: docker logs mentorlik_backend --tail 50" -ForegroundColor Yellow 