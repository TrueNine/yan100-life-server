package net.yan100.life.rest

import org.babyfish.jimmer.client.EnableImplicitApi
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@EnableImplicitApi
@SpringBootApplication
class ApplicationRunner

fun main(args: Array<String>) {
  runApplication<ApplicationRunner>(*args)
}
