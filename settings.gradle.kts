pluginManagement {
  val yunxiaoUrl = extra["repositories.yunxiao"].toString()
  val yunxiaoUsername = extra["credentials.yunxiao.username"].toString()
  val yunxiaoPassword = extra["credentials.yunxiao.password"].toString()

  repositories {
    mavenLocal()
    maven {
      url = uri(yunxiaoUrl)
      credentials {
        username = yunxiaoUsername
        password = yunxiaoPassword
      }
    }
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    gradlePluginPortal()
  }
}

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "yan100-life-server"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

val yunxiaoUrl = extra["repositories.yunxiao"].toString()
val yunxiaoUsername = extra["credentials.yunxiao.username"].toString()
val yunxiaoPassword = extra["credentials.yunxiao.password"].toString()

dependencyResolutionManagement {
  repositories {
    mavenLocal()
    maven {
      url = uri(yunxiaoUrl)
      credentials {
        username = yunxiaoUsername
        password = yunxiaoPassword
      }
    }
  }
  versionCatalogs {
    create("yan100") { from("net.yan100.compose:version-catalog:3.1.1") }
  }
}

include("application", "shared", "domain", "rest")

include("infrastructure:jimmer")
findProject(":infrastructure:jimmer")?.name = "infrastructure-jimmer"
