import java.time.ZonedDateTime

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import model.{TransactionData, TransactionsData}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.io.StdIn
import scala.util.Random

object WebServer extends PlayJsonSupport {
  implicit val system: ActorSystem                        = ActorSystem("financial-data-service")
  implicit val materializer: ActorMaterializer            = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  val random                                              = new Random(1234567890)

  def main(args: Array[String]): Unit = {
    val route: Route =
      path(Segment / "transactions") { accountId =>
        get {
          onSuccess(randomTransactionsFor(accountId)) { t =>
            complete(t)
          }
        }
      }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }

  private def randomTransactionsFor(accountId: String): Future[TransactionsData] = {
    Future {
      // Simulating slow operation
      Thread.sleep(2000)

      TransactionsData(Seq.fill(10)(randomTransactionFor(accountId)))
    }
  }

  private def randomTransactionFor(accountId: String): TransactionData = {
    TransactionData(
      id = random.nextInt(1234567890).toString,
      date = ZonedDateTime.now(),
      amount = BigDecimal(random.nextDouble() * 100),
      accountId = accountId
    )
  }
}
