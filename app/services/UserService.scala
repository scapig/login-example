package services

import javax.inject.{Inject, Singleton}

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import models.{InvalidLoginException, User}

import scala.concurrent.Future
import scala.concurrent.Future.{failed, successful}

@Singleton
class UserService @Inject()() extends IdentityService[User] {
  val testUserId = "user1"
  val testUserPassword = "password1"

  def authenticate(userId: String, password: String): Future[User] = {
    (userId, password) match {
      case (`testUserId`, `testUserPassword`) => successful(User(testUserId))
      case _ => failed(InvalidLoginException())
    }
  }

  override def retrieve(loginInfo: LoginInfo): Future[Option[User]] = {
    loginInfo.providerKey match {
      case "user1" => Future.successful(Some(User("user1")))
      case _ => Future.successful(None)
    }
  }
}
