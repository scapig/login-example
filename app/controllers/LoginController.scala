package controllers

import javax.inject.{Inject, Singleton}

import com.mohiva.play.silhouette.api.{LoginInfo, Silhouette}
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import config.DefaultEnv
import models.InvalidLoginException
import play.api.data.Forms._
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.i18n.MessagesApi
import play.api.mvc.{AbstractController, ControllerComponents, Results, Session}
import services.UserService
import org.webjars.play.WebJarsUtil

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.Future.successful

@Singleton
class LoginController  @Inject()(
                                  cc: ControllerComponents,
                                  messages: MessagesApi,
                                  userService: UserService,
                                  silhouette: Silhouette[DefaultEnv])(implicit webJarsUtil: WebJarsUtil, assets: AssetsFinder) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  def showLoginPage(continue: String) = Action.async { implicit request =>
    successful(Ok(views.html.login(LoginForm.form.fill(LoginForm("", "", continue)))))
  }

  def login() = Action.async { implicit request =>
    def loginWithFormErrors(errors: Form[LoginForm]) = {
      Future.successful(Results.BadRequest(views.html.login(errors)))
    }

    def loginWithValidForm(validForm: LoginForm) = {
      (for {
        sessionResponse <- userService.authenticate(validForm.userId, validForm.password)
        authenticator <- silhouette.env.authenticatorService.create(LoginInfo(CredentialsProvider.ID, sessionResponse.userId))
        result <- silhouette.env.authenticatorService.init(authenticator).flatMap { v =>
          silhouette.env.authenticatorService.embed(v, Results.Redirect(validForm.continue))
        }
      } yield result) recover {
        case _: InvalidLoginException => Results.BadRequest(
          views.html.login(LoginForm.form.fill(validForm).withGlobalError(FormKeys.invalidCredentialsKey)))
      }
    }

    LoginForm.form.bindFromRequest.fold(loginWithFormErrors, loginWithValidForm)
  }
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