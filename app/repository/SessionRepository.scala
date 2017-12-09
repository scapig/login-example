package repository

import javax.inject.{Inject, Singleton}

import models.Session
import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.commands.UpdateWriteResult
import reactivemongo.play.json.collection.JSONCollection
import reactivemongo.play.json._
import models.JsonFormatters._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class SessionRepository @Inject()(val reactiveMongoApi: ReactiveMongoApi)  {

  val repository: Future[JSONCollection] =
    reactiveMongoApi.database.map(_.collection[JSONCollection]("scapig-auth-login"))

  def create(session: Session): Future[Session] = {
    repository.flatMap(collection =>
      collection.update(
        Json.obj("id"-> session.id), session, upsert = true) map {
        case result: UpdateWriteResult if result.ok => session
        case error => throw new RuntimeException(s"Failed to save session ${error.errmsg}")
      }
    )
  }

  def fetch(id: String): Future[Option[Session]] = {
    repository.flatMap(collection =>
      collection.find(Json.obj("id"-> id)).one[Session]
    )
  }
}
