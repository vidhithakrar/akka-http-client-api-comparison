package request_level_api

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class SingleUserWithSingleAccountTransactionsSimulation extends Simulation {

  private val httpProtocol = http
    .baseUrl("http://localhost:9000")


  private val scn = scenario("Single User With Single Account")
    .exec(
      http("Transactions")
        .get("/transactions")
        .multivaluedQueryParam("accountId", Seq.fill(1)(1000000000))
        .check(status.is(200)))

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpProtocol)

}
