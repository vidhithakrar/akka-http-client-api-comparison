package model

import java.time.ZonedDateTime

import play.api.libs.json.{Json, OFormat}

case class Transaction(id: String, date: ZonedDateTime, amount: BigDecimal, accountId: String)

object Transaction {
  implicit val transactionFormat: OFormat[Transaction] = Json.format[Transaction]
}
