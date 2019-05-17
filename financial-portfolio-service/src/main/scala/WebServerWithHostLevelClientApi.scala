import java.time.LocalDateTime

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import com.typesafe.config.ConfigFactory
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import model.Transactions

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.io.StdIn
import scala.util.{Failure, Success, Try}

object WebServerWithHostLevelClientApi extends PlayJsonSupport {
  private implicit val system: ActorSystem =
    ActorSystem("financial-portfolio-service-with-host-level-client-api", ConfigFactory.load())
  private implicit val materializer: ActorMaterializer            = ActorMaterializer()
  private implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  private val http                                                = Http()
  private val connection: Flow[(HttpRequest, String), (Try[HttpResponse], String), Http.HostConnectionPool] =
    http.cachedHostConnectionPool[String]("localhost", 8080)

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

    val bindingFuture = http.bindAndHandle(route, "localhost", 9001)

    println(s"Server online at http://localhost:9001/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }

  private def transactions(accountIds: Seq[String]): Future[Transactions] = {
    Source(accountIds.to[collection.immutable.Seq])
      .map(accountId => (transactionsRequest(accountId), accountId))
      .via(connection)
      .map {
        case (Success(v), accountId) => parseAsTransactionsResponse(v, accountId)
        case (Failure(e), _) =>
          println(e.getStackTrace)
          Future.failed(e)
      }
      .runWith(Sink.reduce[Future[Transactions]](_.zipWith(_)((t1, t2) => Transactions(t1.elements ++ t2.elements))))
      .flatten
  }

  private def parseAsTransactionsResponse(httpResponse: HttpResponse, accountId: String): Future[Transactions] = {
    println(s"[${LocalDateTime.now}] Transactions received for accountId: $accountId")
    Unmarshal(httpResponse.entity).to[Transactions]
  }

  private def transactionsRequest(accountId: String): HttpRequest = {
    println(s"[${LocalDateTime.now}] Requesting transactions for accountId: $accountId")
    HttpRequest(uri = s"http://localhost:8080/$accountId/transactions")
  }

}
