package controllers

import javax.inject.{Inject, Singleton}

import models.InvalidLoginException
import play.api.data.Forms._
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.i18n.MessagesApi
import play.api.mvc.{AbstractController, ControllerComponents, Session}
import services.LoginService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future.successful

@Singleton
class LoginController  @Inject()(cc: ControllerComponents, messages: MessagesApi, loginService: LoginService) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  case class LoginForm(userId: String, password: String, continue: String)

  private val loginForm = Form(
    mapping(
      "userId" -> nonEmptyText,
      "password" -> nonEmptyText,
      "continue" -> nonEmptyText
    )(LoginForm.apply)(LoginForm.unapply)
  )

  def showLoginPage(continue: String) = Action.async { implicit request =>
    successful(Ok(views.html.login(continue)))
  }

  def login() = Action.async { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => successful(BadRequest("Invalid Parameters")),
      loginForm => loginService.authenticate(loginForm.userId, loginForm.password) map { session =>
        Redirect(loginForm.continue).withSession(playSession(session))
      } recover {
        case _: InvalidLoginException => Ok(views.html.login(loginForm.continue, Some("Invalid user ID or password. Try again.")))
      }
    )
  }

  private def playSession(session: models.Session) = Session(Map(
    "userId" -> session.userId,
    "sessionId" -> session.id,
  ))
}
