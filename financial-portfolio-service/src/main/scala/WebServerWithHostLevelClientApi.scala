import akka.stream.scaladsl.{Sink, Source}
import model.Transactions
import service.{TransactionsDataHttpEntity, WebServer}

import scala.concurrent.Future
import scala.util.{Failure, Success}

object WebServerWithHostLevelClientApi extends WebServer {

  private val connection = http.cachedHostConnectionPool[String](TransactionsDataHttpEntity.host, TransactionsDataHttpEntity.port)

  override def transactions(accountIds: Seq[String]): Future[Transactions] = {
    Source(accountIds.to[collection.immutable.Seq])
      .map(accountId => (TransactionsDataHttpEntity.request(accountId), accountId))
      .via(connection)
      .map {
        case (Success(v), accountId) => TransactionsDataHttpEntity.parseResponse(v, accountId)
        case (Failure(e), _)         => Future.failed(e)
      }
      .runWith(Sink.reduce[Future[Transactions]](_.zipWith(_)((t1, t2) => Transactions(t1.elements ++ t2.elements))))
      .flatten
  }

  override def port: Int = 9001

}
