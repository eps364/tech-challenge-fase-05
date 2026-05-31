#!/bin/bash
set -e

echo "Waiting for Keycloak to be ready..."
sleep 10

# Check if realm already exists
REALM_EXISTS=$(curl -s -o /dev/null -w "%{http_code}" \
  -H "Authorization: Bearer $(curl -s -X POST \
    -H "Content-Type: application/x-www-form-urlencoded" \
    -d "username=admin&password=admin&client_id=admin-cli&grant_type=password" \
    http://localhost:8080/realms/master/protocol/openid-connect/token | \
    jq -r '.access_token')" \
  http://localhost:8080/admin/realms/sus-connect)

if [ "$REALM_EXISTS" = "404" ]; then
  echo "Creating sus-connect realm..."
  
  # Get access token
  TOKEN=$(curl -s -X POST \
    -H "Content-Type: application/x-www-form-urlencoded" \
    -d "username=admin&password=admin&client_id=admin-cli&grant_type=password" \
    http://localhost:8080/realms/master/protocol/openid-connect/token | \
    jq -r '.access_token')
  
  # Import realm
  curl -X POST \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d @/realm-export.json \
    http://localhost:8080/admin/realms
  
  echo "Realm created successfully!"
else
  echo "Realm sus-connect already exists, skipping creation"
fi
