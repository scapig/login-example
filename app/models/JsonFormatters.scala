package models

import org.joda.time.DateTime
import play.api.libs.json._

object JsonFormatters {
  val datePattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
  implicit val dateRead: Reads[DateTime] = JodaReads.jodaDateReads(datePattern)
  implicit val dateWrite: Writes[DateTime] = JodaWrites.jodaDateWrites(datePattern)
  implicit val dateFormat: Format[DateTime] = Format[DateTime](dateRead, dateWrite)

  implicit val formatSession = Json.format[Session]
}
