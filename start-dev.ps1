# Check if Docker is running
docker info 2>&1 | Out-Null
if ($LASTEXITCODE -ne 0) {
    Write-Host "Docker is not running. Please start Docker and try again."
    exit 1
}

# Function to check if port is in use
function Test-PortInUse {
    param($port)
    $listener = New-Object System.Net.Sockets.TcpListener([System.Net.IPAddress]::Loopback, $port)
    try {
        $listener.Start()
        $listener.Stop()
        return $false
    } catch {
        return $true
    }
}

# Start database and pgAdmin
Write-Host "Starting database and pgAdmin..."
Set-Location docker
docker-compose -f docker-compose.dev.yml up -d
Set-Location ..

# Wait for PostgreSQL to be ready
Write-Host "Waiting for PostgreSQL to be ready..."
$maxRetries = 30
$retryCount = 0
$isPostgresReady = $false

while (-not $isPostgresReady -and $retryCount -lt $maxRetries) {
    $pgStatus = docker ps --filter "name=mentorlik_postgres" --format "{{.Status}}" 2>&1
    if ($pgStatus -like "*healthy*" -or $pgStatus -like "*Up*") {
        $isPostgresReady = $true
        Write-Host "PostgreSQL is ready."
    } else {
        $retryCount++
        Write-Host "Waiting for PostgreSQL... ($retryCount/$maxRetries)"
        Start-Sleep -Seconds 2
    }
}

if (-not $isPostgresReady) {
    Write-Host "PostgreSQL did not start properly. Please check Docker logs: docker logs mentorlik_postgres"
    exit 1
}

# Check if ports are available
if (Test-PortInUse -port 4200) {
    Write-Host "Port 4200 is already in use. Please free the port and try again."
    exit 1
}

# Create application-local.properties file for Spring Boot
Write-Host "Creating application-local.properties..."
$propertiesContent = @"
spring.datasource.url=jdbc:postgresql://localhost:5434/mentorlik_db
spring.datasource.username=mentorlik_user
spring.datasource.password=mentorlik_password
"@
$propertiesContent | Out-File -FilePath "backend\src\main\resources\application-local.properties" -Encoding utf8

Write-Host "⚠️ IMPORTANT: Backend is NOT started automatically!"
Write-Host "Start the backend through your IDE in debug mode:"
Write-Host "1. Open MentorlikBackendApplication.java"
Write-Host "2. Right-click → Debug"
Write-Host "3. Make sure 'dev,local' profiles are activated"

# Start Angular application in a new window
Write-Host "Starting Angular application..."
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd frontend; npm install; ng serve"

Write-Host "Local development environment is running!"
Write-Host "After starting the backend through IDE it will be available at: http://localhost:8080"
Write-Host "Frontend is available at: http://localhost:4200"
Write-Host "pgAdmin is available at: http://localhost:5050"
Write-Host "Database is available at: localhost:5434" 