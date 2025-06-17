plugins {
  java
  alias(yan100.plugins.org.jetbrains.kotlin.jvm)
  alias(yan100.plugins.org.jetbrains.kotlin.kapt)
  alias(yan100.plugins.org.jetbrains.kotlin.plugin.spring)
  `repositories-conventions`
  `jacoco-convention`
}

dependencies {
  implementation(yan100.org.springframework.boot.spring.boot.autoconfigure)
  annotationProcessor(yan100.org.springframework.spring.boot.configuration.processor)
  kapt(yan100.org.springframework.spring.boot.configuration.processor)


  implementation(projects.domain)
  implementation(yan100.net.yan100.compose.shared)
  implementation(yan100.net.yan100.compose.security.crypto)

  // Spring Framework
  implementation(yan100.org.springframework.spring.context)
  implementation(yan100.org.springframework.security.spring.security.crypto)

  // Kotlin Coroutines
  implementation(yan100.org.jetbrains.kotlinx.kotlinx.coroutines.core)
  implementation(yan100.org.jetbrains.kotlinx.kotlinx.coroutines.reactor)

  // Logging
  implementation(yan100.org.slf4j.slf4j.api)

  testImplementation(yan100.net.yan100.compose.testtoolkit)
}
