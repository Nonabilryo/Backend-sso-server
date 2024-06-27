package nonabili.ssoserver.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.jayway.jsonpath.JsonPath
import jakarta.servlet.http.HttpSession
import nonabili.ssoserver.dto.request.*
import nonabili.ssoserver.service.SSOService
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpSession
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultHandler
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext


@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(RestDocumentationExtension::class, SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class SSOControllerTest {
    var mockMvc: MockMvc? = null
    val log = LoggerFactory.getLogger(javaClass)

    private val objectMapper = ObjectMapper()

    private var accessToken: String? = null
    private var refreshToken: String? = null

    @Autowired
    private val wac: WebApplicationContext? = null

    @Autowired
    private val ssoService: SSOService? = null

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider?) {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac!!)
            .apply<DefaultMockMvcBuilder>(documentationConfiguration(restDocumentation))
            .build()
    }

    @Test
    @Order(1)
    fun signUp() {
        var session = MockHttpSession()
        var emailVerifyCode = ssoService?.verifyEmail(VerifyEmailRequest(email = "ansrkdls@dgsw.hs.kr"), session)
        var tellVerifyCode = ssoService?.verifyTell(VerifyTellRequest(tell = "01064586659"), session)
        var content = objectMapper.writeValueAsString(SignUpRequest(
            name = "문가인",
            id = "ansrkdls",
            password = "ansrkdls",
            email = "ansrkdls@dgsw.hs.kr",
            tell = "01064586659",
            emailVerifyCode = emailVerifyCode!!,
            tellVerifyCode = tellVerifyCode!!,
            adress = null
        ))
        mockMvc?.perform(
            post("/sso/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .session(session)
        )
            ?.andExpect(MockMvcResultMatchers.status().isOk)
            ?.andDo(document("signUp/success"))

        session = MockHttpSession()  // test account
        emailVerifyCode = ssoService?.verifyEmail(VerifyEmailRequest(email = "ansrkdls2@dgsw.hs.kr"), session)
        tellVerifyCode = ssoService?.verifyTell(VerifyTellRequest(tell = "01064586652"), session)
        content = objectMapper.writeValueAsString(SignUpRequest(
            name = "문가인2",
            id = "ansrkdls2",
            password = "ansrkdls2",
            email = "ansrkdls2@dgsw.hs.kr",
            tell = "01064586652",
            emailVerifyCode = emailVerifyCode!!,
            tellVerifyCode = tellVerifyCode!!,
            adress = null
        ))
        mockMvc?.perform(
            post("/sso/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .session(session)
        )
    }

    @Test
    @Order(2)
    fun login() {
        val content = objectMapper.writeValueAsString(LoginRequest(
            id = "ansrkdls",
            password = "ansrkdls"
        ))
        mockMvc?.perform(
            post("/sso/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
            ?.andExpect(MockMvcResultMatchers.status().isOk)
            ?.andDo(document("login/success"))
            ?.andDo(ResultHandler { result ->
                log.info(result.response.contentAsString)
                accessToken = JsonPath.parse(result.response.contentAsString).read("$.data.accessToken")
                refreshToken = JsonPath.parse(result.response.contentAsString).read("$.data.refreshToken")
            })
    }


    @Test
    @Order(3)
    fun refresh() {
        val content = objectMapper.writeValueAsString(
            refreshToken?.let {
                RefreshRequest(
                    refreshToken = it
                )
            }
        )
        mockMvc?.perform(
            post("/sso/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
            ?.andExpect(MockMvcResultMatchers.status().isOk)
            ?.andExpect(MockMvcResultMatchers.jsonPath("$.data.accessToken").exists())
            ?.andExpect(MockMvcResultMatchers.jsonPath("$.data.refreshToken").exists())
            ?.andDo(document("refresh/success"))
            ?.andDo(ResultHandler { result ->
                log.info(result.response.contentAsString)
                accessToken = JsonPath.parse(result.response.contentAsString).read("$.data.accessToken")
                refreshToken = JsonPath.parse(result.response.contentAsString).read("$.data.refreshToken")
            })
    }

    @Test
    @Order(4)
    fun verifyName() {
        var content = objectMapper.writeValueAsString(
            VerifyNameRequest(
                name = "내가문가인이다"
            )
        )
        mockMvc?.perform(
            post("/sso/verify/name")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
            ?.andExpect(MockMvcResultMatchers.status().isOk)
            ?.andDo(document("verify/name/success"))
        content = objectMapper.writeValueAsString(
            VerifyNameRequest(
                name = "문가인"
            )
        )
        mockMvc?.perform(
            post("/sso/verify/name")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
            ?.andExpect(MockMvcResultMatchers.status().`is`(400))
            ?.andDo(document("verify/name/failure"))
    }

    @Test
    @Order(5)
    fun verifyTell() {
        val content = objectMapper.writeValueAsString(
            VerifyTellRequest(
                tell = "01064586658"
            )
        )
        mockMvc?.perform(
            post("/sso/verify/tell")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
            ?.andExpect(MockMvcResultMatchers.status().isOk)
            ?.andDo(document("verify/tell/success"))
    }

    @Test
    @Order(6)
    fun verifyEmail() {
        val content = objectMapper.writeValueAsString(
            VerifyEmailRequest(
                email = "yumeobun@gmail.com"
            )
        )
        mockMvc?.perform(
            post("/sso/verify/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
            ?.andExpect(MockMvcResultMatchers.status().isOk)
            ?.andDo(document("verify/email/success"))
    }
}