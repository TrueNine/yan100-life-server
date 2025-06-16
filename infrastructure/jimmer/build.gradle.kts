plugins {
  java
  alias(yan100.plugins.org.jetbrains.kotlin.jvm)
  alias(yan100.plugins.org.jetbrains.kotlin.kapt)
  alias(yan100.plugins.com.google.devtools.ksp)
  alias(yan100.plugins.org.jetbrains.kotlin.plugin.spring)
  `repositories-conventions`
  `jacoco-convention`
}

dependencies {
  implementation(yan100.org.springframework.boot.spring.boot.autoconfigure)
  annotationProcessor(yan100.org.springframework.spring.boot.configuration.processor)
  kapt(yan100.org.springframework.spring.boot.configuration.processor)

  implementation(projects.domain)
  implementation(projects.application)
  implementation(yan100.net.yan100.compose.shared)
  implementation(yan100.net.yan100.compose.rds.shared)
  implementation(yan100.org.babyfish.jimmer.jimmer.spring.boot.starter)
  implementation(yan100.org.babyfish.jimmer.jimmer.sql.kotlin)

  implementation(yan100.org.springframework.spring.context)

  implementation(yan100.org.jetbrains.kotlinx.kotlinx.coroutines.core)

  runtimeOnly(yan100.org.flywaydb.flyway.core)
  runtimeOnly(yan100.net.yan100.compose.rds.flyway.migration.postgresql)
  runtimeOnly(yan100.org.flywaydb.flyway.database.postgresql)
  runtimeOnly(yan100.org.postgresql.postgresql)


  ksp(yan100.org.babyfish.jimmer.jimmer.ksp)

  testImplementation(yan100.net.yan100.compose.testtoolkit)
}

kotlin {
  sourceSets.main {
    kotlin.srcDir("build/generated/ksp/main/kotlin")
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}
