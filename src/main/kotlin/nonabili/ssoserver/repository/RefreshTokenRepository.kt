package nonabili.ssoserver.repository

import nonabili.ssoserver.entity.RefreshToken
import nonabili.ssoserver.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface RefreshTokenRepository: JpaRepository<RefreshToken, String> {
    fun findRefreshTokenByUser(user: User): RefreshToken?
    fun findRefreshTokenByToken(token: String): RefreshToken?
}