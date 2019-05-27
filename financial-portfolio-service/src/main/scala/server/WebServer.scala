package server

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives.{complete, get, onSuccess, parameter, path, _}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.{Http, HttpExt}
import akka.stream.ActorMaterializer
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import model.Transactions

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.io.StdIn

trait WebServer extends PlayJsonSupport {
  implicit val system: ActorSystem                        = ActorSystem("financial-portfolio-service")
  implicit val materializer: ActorMaterializer            = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  val http: HttpExt                                       = Http()

  def main(args: Array[String]): Unit = {
    val route: Route =
      path("transactions") {
        parameter('accountId.*) { accountIds =>
          get {
            onSuccess(transactions(accountIds)) { t =>
              complete(t)
            }
          }
        }
      }

    val bindingFuture = http.bindAndHandle(route, "localhost", port)

    println(s"Server online at http://localhost:$port/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }

  def transactions(accountIds: Iterable[String]): Future[Transactions]

  def port: Int
}
