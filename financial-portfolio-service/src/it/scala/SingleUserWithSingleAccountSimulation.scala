import Transactions._
import io.gatling.core.Predef._

class SingleUserWithSingleAccountSimulation extends Simulation {

  setUp(
    scenarioWith(
      name = "Single User With Single Account",
      accountIds = Seq(1)
    ).inject(atOnceUsers(1))
  ).protocols(httpProtocol)

}
