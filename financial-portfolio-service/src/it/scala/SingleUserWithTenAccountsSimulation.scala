import Transactions.{httpProtocol, scenarioWith}
import io.gatling.core.Predef._

class SingleUserWithTenAccountsSimulation extends Simulation {
  setUp(
    scenarioWith(
      name = "Single User With Ten Accounts",
      accountIds = Seq.range(1, 11)
    ).inject(atOnceUsers(1))
  ).protocols(httpProtocol)
}
