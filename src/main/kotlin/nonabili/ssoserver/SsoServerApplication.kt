package nonabili.ssoserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class SsoServerApplication

fun main(args: Array<String>) {
    runApplication<SsoServerApplication>(*args)
}
