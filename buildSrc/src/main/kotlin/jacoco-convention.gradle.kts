plugins {
  id("jacoco")
}

tasks.withType<Test>().configureEach {
  useJUnitPlatform()
  jvmArgs = listOf("-XX:+EnableDynamicAgentLoading")
  finalizedBy("jacocoTestReport")
}

tasks.withType<JacocoReport>().configureEach {
  dependsOn(tasks.withType<Test>())
  reports {
    xml.required.set(false)
    html.required.set(true)
    csv.required.set(true)
  }
  val mainSrc = listOf("src/main/java", "src/main/kotlin")
  classDirectories.setFrom(
    files(classDirectories.files.map {
      fileTree(it) {
        exclude("**/generated/**")
      }
    })
  )
  sourceDirectories.setFrom(files(mainSrc))
  executionData.setFrom(fileTree(project.layout.buildDirectory).include("**/jacoco/test.exec", "**/jacoco.exec"))
}
