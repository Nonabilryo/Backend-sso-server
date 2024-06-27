package nonabili.ssoserver.util.error

import org.springframework.http.HttpStatus

enum class ErrorState(val status: HttpStatus = HttpStatus.OK, val message: String) {

    ERROR_FORMAT(HttpStatus.BAD_REQUEST, "It's test"),
    ACCESSTOKEN_HAS_EXPIRED(HttpStatus.BAD_REQUEST, "AccessToken has expired"),

    ID_IS_ALREADY_USED(HttpStatus.BAD_REQUEST, "Id is already used"),
    NAME_IS_ALREADY_USED(HttpStatus.BAD_REQUEST, "Name is already used"),
    TELL_IS_ALREADY_USED(HttpStatus.BAD_REQUEST, "Tell is already used"),
    EMAIL_IS_ALREADY_USED(HttpStatus.BAD_REQUEST, "Email is already used"),

    NOT_FOUND_ID(HttpStatus.NOT_FOUND, "Not found Id"),
    NOT_FOUND_REFRESHTOKEN(HttpStatus.NOT_FOUND, "Not found RefreshToken"),

    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "Wrong password"),

    NOT_VERIFED_EMAIL(HttpStatus.BAD_REQUEST, "Email not verifed"),
    NOT_VERIFED_TELL(HttpStatus.BAD_REQUEST, "Tell not verifed")
}