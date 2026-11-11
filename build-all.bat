@echo off
setlocal enabledelayedexpansion

echo === Building all microservices sequentially ===

set SERVICES=eureka-service config-service gateway-service user-service product-service order-service

for %%s in (%SERVICES%) do (
    echo ----------------------------------------
    echo Building %%s...
    echo ----------------------------------------
    cd %%s
    call gradlew.bat bootJar -x test --no-daemon
    if !errorlevel! neq 0 (
        echo Failed to build %%s
        exit /b !errorlevel!
    )
    cd ..
)

echo ========================================
echo All microservices built successfully!
echo ========================================

