# akka-http-client-api-comparison
Comparison of Request-level and Host-level Akka Http Client API with a specific example.

## Start `financial-data-service`
  Run `WebServer.scala`
## Start `financial-portfolio-service` with Request-level API 
  Run `WebServerWithRequestLevelClientApi.scala`
## Start `financial-portfolio-service` with Host-level API 
  Run `WebServerWithHostLevelClientApi.scala`
## Run API tests
  There are [gatling](https://gatling.io/) tests inside `financial-portfolio-service/src/it/scala`. A single test can be run with the command: `gatling-it:testOnly <simulation-class-name>`, For eg: `gatling-it:testOnly SingleUserWithTenAccountsSimulation`

