# akka-http-client-api-comparison
Comparison of Request-level and Host-level Akka Http Client API with a specific example mentioned at https://medium.com/@vidhi.thakrar/akka-http-client-api-request-level-vs-host-level-8bcf5da799e1.

## Start `financial-data-service`
  Run `WebServer.scala`
## Start `financial-portfolio-service` with Request-level API 
  Run `WebServerWithRequestLevelClientApi.scala`
## Start `financial-portfolio-service` with Host-level API 
  Run `WebServerWithHostLevelClientApi.scala`
## Run API tests
  There are [gatling](https://gatling.io/) tests inside `financial-portfolio-service/src/it/scala`. A single test can be run with the command: `gatling-it:testOnly <simulation-class-name>`, For eg: `gatling-it:testOnly SingleUserWithTenAccountsSimulation`
  
  Note: Update the port in Transactions.scala to run the test against Host-level API implementation

