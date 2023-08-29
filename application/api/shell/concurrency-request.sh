#!/bin/bash

curl -L -v -d '{}' \
  -H "Accept: application/json" \
  -H "Content-Type: application/json" \
  --url http://localhost:9001/reservations/1/max-number &
curl -L -v -d '{}' \
  -H "Accept: application/json" \
  -H "Content-Type: application/json" \
  --url http://localhost:9001/reservations/1/max-number &
curl -L -v -d '{}' \
  -H "Accept: application/json" \
  -H "Content-Type: application/json" \
  --url http://localhost:9001/reservations/1/max-number
