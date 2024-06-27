//package nonabili.ssoserver.entity
//
//import jakarta.persistence.*
//import java.util.UUID
//
//@Entity
//@Table(name = "follow")
//data class Follow(
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    val uuid: UUID = UUID.randomUUID(),
//    @ManyToOne
//    @JoinColumn(name = "follower")
//    val follower: User,
//    @ManyToOne
//    @JoinColumn(name = "following")
//    val following: User
//)
