import java.time.LocalDateTime

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.server.Directives.{complete, get, path, _}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import model.Transactions

import scala.Function.tupled
import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.io.StdIn

object WebServerWithRequestLevelClientApi extends PlayJsonSupport {
  implicit val system: ActorSystem = ActorSystem("financial-portfolio-service-with-request-level-client-api")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  lazy val http = Http()

  def main(args: Array[String]): Unit = {
    val route: Route =
      path("transactions") {
        parameter('accountId.*) { accountIds =>
          get {
            onSuccess(transactions(accountIds.toSeq)) { t =>
              complete(t)
            }
          }
        }
      }

    val bindingFuture = http.bindAndHandle(route, "localhost", 9000)

    println(s"Server online at http://localhost:9000/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }

  private def transactions(accountIds: Seq[String]): Future[Transactions] = {
    accountIds
      .map(accountId => (http.singleRequest(transactionsRequest(accountId)), accountId))
      .map(tupled((response, accountId) => parseAsTransactionsResponse(response, accountId)))
      .reduce(_.zipWith(_)((t1, t2) => Transactions(t1.elements ++ t2.elements)))
  }

  private def parseAsTransactionsResponse(eventualResponse: Future[HttpResponse], accountId: String): Future[Transactions] = {
    eventualResponse.flatMap(r => {
      println(s"[${LocalDateTime.now}] Transactions received for accountId: $accountId")
      Unmarshal(r.entity).to[Transactions]
    })
  }

  private def transactionsRequest(accountId: String): HttpRequest = {
    println(s"[${LocalDateTime.now}] Requesting transactions for accountId: $accountId")
    HttpRequest(uri = s"http://localhost:8080/$accountId/transactions")
  }
}
