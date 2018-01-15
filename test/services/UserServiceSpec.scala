package services

import com.mohiva.play.silhouette.api.LoginInfo
import models.{InvalidLoginException, User}
import org.scalatest.mockito.MockitoSugar
import utils.UnitSpec

class UserServiceSpec extends UnitSpec with MockitoSugar {

  trait Setup {
    val underTest = new UserService()
  }

  "authenticate" should {
    "return the user when the credentials are user1/password2" in new Setup {
      val user = await(underTest.authenticate("user1", "password1"))

      user shouldBe User("user1")
    }

    "fail with InvalidLoginException when userId is invalid" in new Setup {
      intercept[InvalidLoginException] {
        await(underTest.authenticate("invalid", "password1"))
      }
    }

    "fail with InvalidLoginException when password is invalid" in new Setup {
      intercept[InvalidLoginException] {
        await(underTest.authenticate("user1", "invalid"))
      }
    }
  }

  "retrieve" should {
    "return the user" in new Setup {
      val user = await(underTest.retrieve(LoginInfo("provider", "user1")))

      user shouldBe Some(User("user1"))
    }
  }
}