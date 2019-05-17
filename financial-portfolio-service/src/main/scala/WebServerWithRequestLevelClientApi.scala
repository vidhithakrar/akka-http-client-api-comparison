import model.Transactions
import service.{TransactionsDataHttpEntity, WebServer}

import scala.Function.tupled
import scala.concurrent.Future

object WebServerWithRequestLevelClientApi extends WebServer {

  override def transactions(accountIds: Seq[String]): Future[Transactions] = {
    accountIds
      .map(accountId => (http.singleRequest(TransactionsDataHttpEntity.request(accountId)), accountId))
      .map(tupled((response, accountId) => response.flatMap(TransactionsDataHttpEntity.parseResponse(_, accountId))))
      .reduce(_.zipWith(_)((t1, t2) => Transactions(t1.elements ++ t2.elements)))
  }

  override def systemName: String = "financial-portfolio-service-with-request-level-client-api"

  override def port: Int = 9000

}
