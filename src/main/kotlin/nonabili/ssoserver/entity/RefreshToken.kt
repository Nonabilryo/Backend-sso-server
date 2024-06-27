package nonabili.ssoserver.entity

import jakarta.persistence.*

@Entity
data class RefreshToken(
    @Id
    val token: String,
    @OneToOne
    @JoinColumn(name = "user_idx")
    val user: User
)
