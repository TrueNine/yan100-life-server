import com.diffplug.spotless.LineEnding

plugins {
  idea
  base
  alias(yan100.plugins.org.jetbrains.kotlin.jvm)
  alias(yan100.plugins.org.jetbrains.kotlin.kapt)
  alias(yan100.plugins.com.diffplug.spotless)
}

repositories { mavenCentral() }

idea {
  module {
    isDownloadSources = true
    isDownloadJavadoc = true
  }
}

spotless {
  kotlinGradle {
    ktfmt().googleStyle().configure {
      it.setMaxWidth(80)
      it.setBlockIndent(2)
      it.setContinuationIndent(2)
      it.setRemoveUnusedImports(true)
    }
  }
  kotlin {
    licenseHeader("")
    ktfmt().googleStyle().configure {
      it.setMaxWidth(80)
      it.setBlockIndent(2)
      it.setContinuationIndent(2)
      it.setRemoveUnusedImports(true)
    }
  }
  sql {
    lineEndings = LineEnding.UNIX
    target("**/**.sql")
    dbeaver()
      .configFile(
        file(
          rootProject.layout.projectDirectory.file(
            "buildSrc/.compose-config/.spotless_format_config.properties"
          )
        )
      )
  }
}

fun loadDotenv(): Map<String, String> {
  val dotenvPath = project.findProperty("dotenv")?.toString()
  if (dotenvPath.isNullOrBlank()) {
    logger.warn("[dotenv] gradle.properties 未配置 dotenvFile 属性，未加载 .env 文件")
    return emptyMap()
  }
  val envFile = file(dotenvPath)
  if (!envFile.exists()) {
    logger.warn("[dotenv] 指定的 .env 文件不存在: $dotenvPath")
    return emptyMap()
  }
  return envFile
    .readLines()
    .filter {
      it.isNotBlank() && !it.trim().startsWith("#") && it.contains("=")
    }
    .associate {
      val (k, v) = it.split("=", limit = 2)
      k.trim() to v.trim()
    }
}

val dotenv = loadDotenv()

tasks.withType<JavaExec>().configureEach { environment(dotenv) }

tasks.withType<Test>().configureEach { environment(dotenv) }
