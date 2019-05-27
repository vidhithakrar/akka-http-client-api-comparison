package service

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.{complete, get, onSuccess, parameter, path, _}
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import model.Transactions

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.io.StdIn

trait WebServer extends PlayJsonSupport {
  implicit val system: ActorSystem                        = ActorSystem(systemName)
  implicit val materializer: ActorMaterializer            = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  val http                                                = Http()

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

    val bindingFuture = http.bindAndHandle(route, "localhost", port)

    println(s"Server online at http://localhost:$port/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }

  def transactions(accountIds: Seq[String]): Future[Transactions]

  def systemName: String

  def port: Int
}
