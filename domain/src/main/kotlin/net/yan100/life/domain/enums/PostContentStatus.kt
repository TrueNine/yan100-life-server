package net.yan100.life.domain.enums

import net.yan100.compose.typing.IntTyping
import org.babyfish.jimmer.sql.EnumItem
import org.babyfish.jimmer.sql.EnumType

@EnumType(EnumType.Strategy.ORDINAL)
enum class PostContentStatus(
  override val value: Int,
) : IntTyping {
  @EnumItem(ordinal = 0)
  NONE(0),

  @EnumItem(ordinal = 1)
  PRE_AUDIT(1),

  @EnumItem(ordinal = 2)
  AUDIT_PASS(2),

  @EnumItem(ordinal = 3)
  AUDIT_FAIL(3),

  @EnumItem(ordinal = 4)
  REMOVED(4),

  @EnumItem(ordinal = 9999)
  OTHER(9999);

  companion object {
    @JvmStatic
    operator fun get(value: Int): PostContentStatus? = entries.find { it.value == value }
  }
}
