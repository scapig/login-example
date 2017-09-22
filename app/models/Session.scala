package models

import org.joda.time.DateTime


case class Session(id: String, createdAt: DateTime = DateTime.now())

