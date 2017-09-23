package services

import javax.inject.{Inject, Singleton}

import models.{InvalidLoginException, Session}
import repository.SessionRepository

import scala.concurrent.Future
import scala.concurrent.Future.failed

@Singleton
class LoginService @Inject()(sessionRepository: SessionRepository) {

  def authenticate(userId: String, password: String): Future[Session] = {
    (userId, password) match {
      case ("user1", "password1") => sessionRepository.create(Session("user1"))
      case _ => failed(InvalidLoginException())
    }
  }
}
