plugins {
  java
  alias(yan100.plugins.org.jetbrains.kotlin.jvm)
  alias(yan100.plugins.org.jetbrains.kotlin.plugin.spring)
  alias(yan100.plugins.org.springframework.boot)
  alias(yan100.plugins.com.google.devtools.ksp)
  `repositories-conventions`
  `jacoco-convention`
}

dependencies {
  implementation(projects.application)
  implementation(projects.domain)
  implementation(yan100.org.babyfish.jimmer.jimmer.client)
  implementation(yan100.net.yan100.compose.shared)
  implementation(yan100.org.springframework.boot.spring.boot.starter.web)
  implementation(yan100.org.babyfish.jimmer.jimmer.spring.boot.starter)

  runtimeOnly(yan100.org.springframework.data.spring.data.commons)
  runtimeOnly(yan100.org.springframework.spring.jdbc)
  implementation(projects.infrastructure.jimmer)

  ksp(yan100.org.babyfish.jimmer.jimmer.ksp)

  testImplementation(yan100.net.yan100.compose.testtoolkit)
}
