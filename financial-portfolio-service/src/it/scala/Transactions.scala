import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._

object Transactions {
  private val requestLevelAPIPort = 9000
  private val HostLevelAPIPort    = 9001

  val httpProtocol = http.baseUrl(s"http://localhost:$requestLevelAPIPort")

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
