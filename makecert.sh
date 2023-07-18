#!/bin/bash

cd api/src/main/resources

openssl req -x509 -newkey rsa:4096 -sha256 -days 365 -nodes \
  -keyout server.key -out server.crt \
  -subj "/CN=example.com/OU=?/O=?/L=?/ST=?/C=??" \
  -addext "subjectAltName=DNS:example.com,DNS:www.example.com,IP:10.0.0.1"

