package net.yan100.life.domain.rbac

import net.yan100.life.domain.AggregateId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PermissionAggregateTest {
  @Test
  fun `test create permission`() {
    val permission = PermissionAggregate.create("perm.read", "读取权限", "允许读取数据")
    assertEquals("perm.read", permission.code)
    assertEquals("读取权限", permission.name)
    assertEquals("允许读取数据", permission.description)
  }

  @Test
  fun `test update permission`() {
    val permission = PermissionAggregate.create("perm.write", "写入权限")
    permission.active(AggregateId.change(123))
    permission.update(name = "写权限", description = "允许写入数据")
    assertEquals("写权限", permission.name)
    assertEquals("允许写入数据", permission.description)
  }
} 
