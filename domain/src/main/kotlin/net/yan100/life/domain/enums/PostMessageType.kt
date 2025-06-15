package net.yan100.life.domain.enums

import net.yan100.compose.typing.StringTyping
import org.babyfish.jimmer.sql.EnumItem
import org.babyfish.jimmer.sql.EnumType

@EnumType(value = EnumType.Strategy.NAME)
enum class PostMessageType(
  override val value: String,
) : StringTyping {
  @EnumItem(name = "SYS")
  SYS("SYS"),

  @EnumItem(name = "JOB")
  JOB("JOB"),

  @EnumItem(name = "HOUSE")
  HOUSE("HOUSE"),

  @EnumItem(name = "REPAIR")
  REPAIR("REPAIR"),

  @EnumItem(name = "HELP")
  HELP("HELP");

  companion object {
    @JvmStatic
    operator fun get(value: String): PostMessageType? = entries.find { it.value == value }
  }
}
