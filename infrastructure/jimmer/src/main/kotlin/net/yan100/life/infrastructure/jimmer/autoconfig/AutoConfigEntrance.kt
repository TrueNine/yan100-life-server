package net.yan100.life.infrastructure.jimmer.autoconfig

import org.babyfish.jimmer.spring.repository.EnableJimmerRepositories
import org.springframework.context.annotation.ComponentScan

@ComponentScan(
  value = [
    "net.yan100.infrastructure.life.jimmer.autoconfig",
    "net.yan100.life.infrastructure.jimmer.cqrs",
    "net.yan100.life.infrastructure.jimmer.events",
    "net.yan100.life.infrastructure.jimmer.repositories",
  ]
)
@EnableJimmerRepositories(
  basePackages = [
    "net.yan100.life.infrastructure.jimmer.repositories"
  ]
)
class AutoConfigEntrance
