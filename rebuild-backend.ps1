# Script to rebuild backend JAR and update Docker image
# Author: AI Assistant
# Date: 2025-04-14

Write-Host "========== Rebuilding Mentorlik Backend ==========" -ForegroundColor Green

# Step 1: Check if Docker is running
Write-Host "Checking if Docker is running..." -ForegroundColor Cyan
docker info | Out-Null
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Docker is not running. Please start Docker and try again." -ForegroundColor Red
    exit 1
}
Write-Host "Docker is running!" -ForegroundColor Green

# Step 2: Build the JAR file
Write-Host "Building JAR file..." -ForegroundColor Cyan
Set-Location "$PSScriptRoot\backend"
./mvnw clean package -DskipTests
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Failed to build JAR file." -ForegroundColor Red
    exit 1
}
Write-Host "JAR file built successfully!" -ForegroundColor Green

# Step 3: Rebuild Docker image
Write-Host "Rebuilding Docker image..." -ForegroundColor Cyan
Set-Location "$PSScriptRoot\docker"
docker-compose build backend
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Failed to build Docker image." -ForegroundColor Red
    exit 1
}
Write-Host "Docker image rebuilt successfully!" -ForegroundColor Green

# Step 4: Return to root directory
Set-Location $PSScriptRoot

# Step 5: Show instructions
Write-Host ""
Write-Host "========== Next Steps ==========" -ForegroundColor Green
Write-Host "Backend JAR has been rebuilt and Docker image has been updated." -ForegroundColor Yellow
Write-Host ""
Write-Host "To restart only the backend container, run:" -ForegroundColor Cyan
Write-Host "   cd docker" -ForegroundColor White
Write-Host "   docker-compose up -d backend" -ForegroundColor White
Write-Host ""
Write-Host "To restart all containers, run:" -ForegroundColor Cyan
Write-Host "   cd docker" -ForegroundColor White
Write-Host "   docker-compose down" -ForegroundColor White
Write-Host "   docker-compose up -d" -ForegroundColor White
Write-Host ""
Write-Host "To check backend logs, run:" -ForegroundColor Cyan
Write-Host "   docker logs mentorlik_backend --tail 50" -ForegroundColor White
Write-Host ""
Write-Host "========== Done ==========" -ForegroundColor Green 