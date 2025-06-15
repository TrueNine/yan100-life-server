package net.yan100.life.application.autoconfig

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = ["net.yan100.life.application"])
open class AutoConfigEntrance {
  // 可在此定义 @Bean 或导入其他配置
} 
