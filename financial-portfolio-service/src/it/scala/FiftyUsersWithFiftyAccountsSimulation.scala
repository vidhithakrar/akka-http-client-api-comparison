import Transactions.{httpProtocol, scenarioWith}
import io.gatling.core.Predef._

class FiftyUsersWithFiftyAccountsSimulation extends Simulation {
  setUp(
    scenarioWith(
      name = "Fifty Users With Fifty Accounts",
      accountIds = Seq.range(1, 51)
    ).inject(atOnceUsers(50))
  ).protocols(httpProtocol)
}
