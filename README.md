# zio-quickstart-https

A simple HTTPS server with one endpoint.

In one terminal, cd into the source directory and run:

```
sbt run
```

In another terminal, do this to have curl call the endpoint, allowing self-signed cert:

```
curl -k https://localhost:8080/ping
```
