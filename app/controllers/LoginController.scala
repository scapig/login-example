package controllers

import javax.inject.{Inject, Singleton}

import play.api.data.Forms._
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.i18n.MessagesApi
import play.api.mvc.{AbstractController, Action, ControllerComponents}

import scala.concurrent.Future.successful

@Singleton
class LoginController  @Inject()(cc: ControllerComponents, messages: MessagesApi) extends AbstractController(cc) {

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
      loginForm => successful(Redirect(loginForm.continue))
    )
  }
}
