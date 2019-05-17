import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._

object Transactions {
  val httpProtocol = http
    .baseUrl("http://localhost:9000")

  def scenarioWith(name: String, accountIds: Seq[Int]): ScenarioBuilder = {
    scenario(name)
      .exec(
        http("Transactions")
          .get("/transactions")
          .multivaluedQueryParam("accountId", accountIds)
          .check(status.is(200))
      )
  }
}
