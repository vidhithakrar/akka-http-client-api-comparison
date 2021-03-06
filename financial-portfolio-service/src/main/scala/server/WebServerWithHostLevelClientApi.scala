package server

import akka.stream.scaladsl.{Sink, Source}
import model.Transactions
import server.TransactionsDataHttpEntity._

import scala.collection.immutable
import scala.concurrent.Future
import scala.util.{Failure, Success}

object WebServerWithHostLevelClientApi extends WebServer {

  private val connection = http.cachedHostConnectionPool[String](transactionsHost, transactionsPort)

  override def transactions(accountIds: Iterable[String]): Future[Transactions] = {
    Source(accountIds.to[immutable.Iterable])
      .map(accountId => (transactionsRequest(accountId), accountId))
      .via(connection)
      .map {
        case (Success(v), accountId) => parseAsTransactionsResponse(v, accountId)
        case (Failure(e), _)         => Future.failed(e)
      }
      .runWith(Sink.reduce[Future[Transactions]](_.zipWith(_)((t1, t2) => Transactions(t1.elements ++ t2.elements))))
      .flatten
  }

  override def port: Int = 9001
}
