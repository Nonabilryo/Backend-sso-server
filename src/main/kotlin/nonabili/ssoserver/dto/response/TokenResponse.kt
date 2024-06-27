package nonabili.ssoserver.dto.response

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)
