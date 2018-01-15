package controllers

import com.google.inject.{AbstractModule, TypeLiteral}
import com.mohiva.play.silhouette.api.{Environment, LoginInfo}
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import com.mohiva.play.silhouette.test.FakeEnvironment
import config.DefaultEnv
import models.{InvalidLoginException, User}
import org.mockito.BDDMockito.given
import org.scalatest.BeforeAndAfterAll
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.http.Status
import play.api.inject
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.CSRFTokenHelper.addCSRFToken
import play.api.test.FakeRequest
import services.UserService
import utils.UnitSpec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future.{failed, successful}

class LoginControllerSpec extends UnitSpec with MockitoSugar with BeforeAndAfterAll with GuiceOneServerPerSuite {

  val user = User("user1")
  val loginInfo = LoginInfo(CredentialsProvider.ID, user.userId)
  implicit val env: FakeEnvironment[DefaultEnv] = FakeEnvironment[DefaultEnv](Seq(loginInfo -> user))

  class FakeModule extends AbstractModule {
    def configure(): Unit = {
      bind(new TypeLiteral[Environment[DefaultEnv]]{}).toInstance(env)
    }
  }

  trait Setup {
    val userService = mock[UserService]

    val playApplication = new GuiceApplicationBuilder()
      .overrides(new FakeModule())
      .bindings(inject.bind[UserService].to(userService))
      .build()

    val underTest = playApplication.injector.instanceOf[LoginController]
  }

  "login" should {

    "redirect to the continue url when the credentials are correct" in new Setup {
      given(userService.authenticate("user1", "password1")).willReturn(successful(User("user1")))

      val result = await(underTest.login()(addCSRFToken(FakeRequest().withFormUrlEncodedBody("userId" -> "user1", "password" -> "password1", "continue" -> "/continue"))))

      status(result) shouldBe Status.SEE_OTHER
      result.header.headers("location") shouldBe "/continue"
    }

    "show the login page when the userId or password are incorrect" in new Setup {
      given(userService.authenticate("user1", "password1")).willReturn(failed(InvalidLoginException()))

      val result = await(underTest.login()(addCSRFToken(FakeRequest().withFormUrlEncodedBody("userId" -> "user1", "password" -> "password1", "continue" -> "/continue"))))

      status(result) shouldBe Status.BAD_REQUEST
      bodyOf(result) should include ("Invalid username or password")
    }
  }
}
