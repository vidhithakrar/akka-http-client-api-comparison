import Transactions.{httpProtocol, scenarioWith}
import io.gatling.core.Predef._

class SingleUserWithFiftyAccountsSimulation extends Simulation {
  setUp(
    scenarioWith(
      name = "Single User With Fifty Accounts",
      accountIds = Seq.range(1, 51)
    ).inject(atOnceUsers(1))
  ).protocols(httpProtocol)

}
