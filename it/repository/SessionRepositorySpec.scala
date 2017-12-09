package repository

import java.util.UUID

import models.Session
import org.joda.time.DateTime
import org.scalatest.BeforeAndAfterEach
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import utils.UnitSpec
import scala.concurrent.ExecutionContext.Implicits.global

class SessionRepositorySpec extends UnitSpec with BeforeAndAfterEach {

  lazy val fakeApplication: Application = new GuiceApplicationBuilder()
    .configure("mongodb.uri" -> "mongodb://localhost:27017/scapig-oauth-login-test")
    .build()

  val session = Session(UUID.randomUUID().toString)

  lazy val underTest = fakeApplication.injector.instanceOf[SessionRepository]

  override def afterEach {
    await(underTest.repository).drop(failIfNotFound = false)
  }

  "fetch" should {
    "return the session when it exists" in {
      await(underTest.create(session))

      await(underTest.fetch(session.id)) shouldBe Some(session)
    }

    "return None when it does not exist" in {
      await(underTest.fetch(session.id)) shouldBe None
    }
  }
}
