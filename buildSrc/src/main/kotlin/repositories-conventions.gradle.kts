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
  maven { url = uri("https://repo.spring.io/milestone") }
  mavenCentral()
}
