plugins {
  java
  alias(yan100.plugins.com.google.devtools.ksp)
  alias(yan100.plugins.org.jetbrains.kotlin.jvm)
  `repositories-conventions`
}


dependencies {
  ksp(yan100.org.babyfish.jimmer.jimmer.ksp)
  implementation(projects.infrastructure.jimmer)
  implementation(projects.domain)

  implementation(yan100.net.yan100.compose.rds.shared)

  implementation(yan100.org.babyfish.jimmer.jimmer.sql.kotlin)
}
