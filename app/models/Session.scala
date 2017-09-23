package models

import java.util.UUID

import org.joda.time.DateTime

case class Session(userId: String, id: String = UUID.randomUUID().toString, createdAt: DateTime = DateTime.now())
