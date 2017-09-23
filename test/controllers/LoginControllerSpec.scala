package controllers

import models.{InvalidLoginException, Session}
import org.joda.time.DateTimeUtils
import org.mockito.BDDMockito.given
import org.mockito.Matchers.any
import org.mockito.Mockito.verify
import org.scalatest.BeforeAndAfterAll
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.http.Status
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, AnyContentAsEmpty, Result}
import play.api.test.CSRFTokenHelper.CSRFFRequestHeader
import play.api.test.{FakeRequest, Helpers}
import services.LoginService
import utils.UnitSpec

import scala.concurrent.Future
import scala.concurrent.Future.{failed, successful}

class LoginControllerSpec extends UnitSpec with MockitoSugar with BeforeAndAfterAll with GuiceOneServerPerSuite {

  trait Setup {
    val loginService = mock[LoginService]
    val underTest = new LoginController(Helpers.stubControllerComponents(), Helpers.stubMessagesApi(), loginService)

    private val csrfAddToken = app.injector.instanceOf[play.filters.csrf.CSRFAddToken]

    def execute[T <: play.api.mvc.AnyContent](action: Action[AnyContent], request: FakeRequest[T]) = await(csrfAddToken(action)(request))

    val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest()
  }

  "login" should {

    "redirect to the continue url when the credentials are correct" in new Setup {
      given(loginService.authenticate("user1", "password1")).willReturn(successful(Session("user1")))

      val result: Result = execute(underTest.login(), request.withFormUrlEncodedBody("userId" -> "user1", "password" -> "password1", "continue" -> "/continue"))

      status(result) shouldBe Status.SEE_OTHER
      result.header.headers("location") shouldBe "/continue"
    }

    "show the login page when the userId or password are incorrect" in new Setup {
      given(loginService.authenticate("user1", "password1")).willReturn(failed(InvalidLoginException()))

      val result: Result = execute(underTest.login(), request.withFormUrlEncodedBody("userId" -> "user1", "password" -> "password1", "continue" -> "/continue"))

      status(result) shouldBe Status.OK
      bodyOf(result) should include ("Invalid user ID or password. Try again.")
    }
  }
}
