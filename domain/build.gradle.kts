plugins {
  java
  alias(yan100.plugins.org.jetbrains.kotlin.jvm)
  `repositories-conventions`
  `jacoco-convention`
}

dependencies {
  implementation(yan100.net.yan100.compose.shared)
  implementation(yan100.org.springframework.spring.context)
  implementation(yan100.org.babyfish.jimmer.jimmer.core)

  testImplementation(yan100.net.yan100.compose.testtoolkit)
}

tasks.withType<Test> {
  useJUnitPlatform()
}
