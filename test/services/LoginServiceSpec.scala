package services

import models.InvalidLoginException
import org.mockito.BDDMockito.given
import org.mockito.Matchers.any
import org.mockito.Mockito.{verify, verifyZeroInteractions}
import org.scalatest.mockito.MockitoSugar
import repository.SessionRepository
import utils.UnitSpec

class LoginServiceSpec extends UnitSpec with MockitoSugar {

  trait Setup {
    val sessionRepository = mock[SessionRepository]
    val underTest = new LoginService(sessionRepository)

    given(sessionRepository.create(any())).willAnswer(returnSame)
  }

  "authenticate" should {
    "create an return a session when the credentials are user1/password1" in new Setup {
      val session = await(underTest.authenticate("user1", "password1"))

      verify(sessionRepository).create(session)
    }

    "fail with InvalidLoginException when userId is invalid" in new Setup {
      intercept[InvalidLoginException]{await(underTest.authenticate("invalid", "password1"))}

      verifyZeroInteractions(sessionRepository)
    }

    "fail with InvalidLoginException when password is invalid" in new Setup {
      intercept[InvalidLoginException]{await(underTest.authenticate("user1", "invalid"))}

      verifyZeroInteractions(sessionRepository)
    }
  }
}
