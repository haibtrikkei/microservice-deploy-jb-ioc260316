#!/usr/bin/env bash
set -e

echo "=== Building all microservices sequentially ==="

SERVICES=("eureka-service" "config-service" "gateway-service" "user-service" "product-service" "order-service")

for service in "${SERVICES[@]}"; do
    echo "----------------------------------------"
    echo "Building $service..."
    echo "----------------------------------------"
    (cd "$service" && ./gradlew bootJar -x test --no-daemon)
done

echo "========================================"
echo "All microservices built successfully!"
echo "========================================"

