package service

import java.time.LocalDateTime

import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.Materializer
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import model.Transactions

import scala.concurrent.Future

object TransactionsDataHttpEntity extends PlayJsonSupport {

  def host: String = "localhost"

  def port: Int = 8080

  def request(accountId: String): HttpRequest = {
    println(s"[${LocalDateTime.now}] Requesting transactions for accountId: $accountId")
    HttpRequest(uri = s"http://$host:$port/$accountId/transactions")
  }

  def parseResponse(httpResponse: HttpResponse, accountId: String)(implicit materialize: Materializer): Future[Transactions] = {
    println(s"[${LocalDateTime.now}] Transactions received for accountId: $accountId")
    Unmarshal(httpResponse.entity).to[Transactions]
  }
}
