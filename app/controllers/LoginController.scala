package controllers

import javax.inject.{Inject, Singleton}

import models.InvalidLoginException
import play.api.data.Forms._
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.i18n.MessagesApi
import play.api.mvc.{AbstractController, ControllerComponents, Results, Session}
import services.LoginService
import org.webjars.play.WebJarsUtil

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.Future.successful

@Singleton
class LoginController  @Inject()(
                                  cc: ControllerComponents,
                                  messages: MessagesApi,
                                  loginService: LoginService)(implicit webJarsUtil: WebJarsUtil, assets: AssetsFinder) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  private val loginForm = Form(
    mapping(
      "userId" -> nonEmptyText,
      "password" -> nonEmptyText,
      "continue" -> nonEmptyText
    )(LoginForm.apply)(LoginForm.unapply)
  )

  def showLoginPage(continue: String) = Action.async { implicit request =>
    successful(Ok(views.html.login(loginForm.fill(LoginForm("", "", continue)))))
  }

  def login() = Action.async { implicit request =>
    def loginWithFormErrors(errors: Form[LoginForm]) = {
      Future.successful(Results.BadRequest(views.html.login(errors)))
    }

    def loginWithValidForm(validForm: LoginForm) = {
      loginService.authenticate(validForm.userId, validForm.password) map { session =>
        Redirect(validForm.continue).withSession(playSession(session))
      } recover {
        case _: InvalidLoginException => Results.BadRequest(
          views.html.login(LoginForm.form.fill(validForm).withGlobalError(FormKeys.invalidCredentialsKey)))
      }
    }

    LoginForm.form.bindFromRequest.fold(loginWithFormErrors, loginWithValidForm)
  }

  private def playSession(session: models.Session) = Session(Map(
    "userId" -> session.userId,
    "sessionId" -> session.id,
  ))
}


case class LoginForm(userId: String, password: String, continue: String)

object LoginForm {
  val form: Form[LoginForm] = Form(
    mapping(
      "userId" -> requiredValidator(FormKeys.userIdRequiredKey),
      "password" -> requiredValidator(FormKeys.passwordRequiredKey),
      "continue" -> text
    )(LoginForm.apply)(LoginForm.unapply)
  )
}