import play.api.data.Forms

package object controllers {

  object FormKeys {
    val invalidCredentialsKey = "credentials.error.invalid.field"
    val userIdRequiredKey = "userid.error.required.field"
    val passwordRequiredKey = "password.error.required.field"
  }

  def requiredValidator(errorMessage: String) = Forms.text.verifying(errorMessage, isNotBlankString)
  private def isNotBlankString: String => Boolean = s => s.trim.length > 0

}