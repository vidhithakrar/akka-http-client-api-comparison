package model

import play.api.libs.json.{Json, Writes}

case class TransactionsData(elements: Seq[TransactionData])

object TransactionsData {
  implicit val transactionsDataWrites: Writes[TransactionsData] = Json.writes[TransactionsData]
}
