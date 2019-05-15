package model

import java.time.ZonedDateTime

import play.api.libs.json.{Json, Writes}

case class TransactionData(id: String, date: ZonedDateTime, amount: BigDecimal, accountId: String)

object TransactionData {
  implicit val transactionDataWrites: Writes[TransactionData] = Json.writes[TransactionData]
}
