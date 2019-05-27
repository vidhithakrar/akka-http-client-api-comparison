package server

import model.Transactions

import scala.Function.tupled
import scala.concurrent.Future

object WebServerWithRequestLevelClientApi extends WebServer {

  override def transactions(accountIds: Seq[String]): Future[Transactions] = {
    accountIds
      .map(accountId => (http.singleRequest(TransactionsDataHttpEntity.request(accountId)), accountId))
      .map(tupled((response, accountId) => response.flatMap(TransactionsDataHttpEntity.parseResponse(_, accountId))))
      .reduce(_.zipWith(_)((t1, t2) => Transactions(t1.elements ++ t2.elements)))
  }

  override def port: Int = 9000

}
