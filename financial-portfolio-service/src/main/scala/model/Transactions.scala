package model

import play.api.libs.json.{Json, OFormat}

case class Transactions(elements: Seq[Transaction] = Seq.empty)

object Transactions {
  implicit val transactionsFormat: OFormat[Transactions] = Json.format[Transactions]
}
