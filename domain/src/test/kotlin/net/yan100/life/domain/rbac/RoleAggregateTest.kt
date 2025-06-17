package net.yan100.life.domain.rbac

import net.yan100.life.domain.AggregateId
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.*

class RoleAggregateTest {
  @Nested
  inner class Create {
    @Test
    fun `正常 创建角色`() {
      val role = RoleAggregate.create("admin", "管理员", "系统管理员")
      assertEquals("admin", role.code)
      assertEquals("管理员", role.name)
      assertEquals("系统管理员", role.description)
    }

    @Test
    fun `异常 角色code或name为空`() {
      assertFailsWith<IllegalArgumentException> {
        RoleAggregate.create("", "管理员")
      }
      assertFailsWith<IllegalArgumentException> {
        RoleAggregate.create("admin", "")
      }
    }

    @Test
    fun `正常 角色id唯一`() {
      val role1 = RoleAggregate.create("code1", "name1")
      val role2 = RoleAggregate.create("code2", "name2")
      assertNotEquals(role1.id, role2.id)
    }

    @Test
    fun `正常 角色创建事件`() {
      val code = "admin"
      val name = "管理员"
      val desc = "系统管理员"
      val role = RoleAggregate.create(code, name, desc)
      val events = role.domainEvents
      assertEquals(1, events.size)
      val event = events[0]
      assertTrue(event is RoleCreatedEvent)
      event as RoleCreatedEvent
      assertEquals(role.id, event.aggregateId)
      assertEquals(code, event.code)
      assertEquals(name, event.name)
    }
  }

  @Nested
  inner class Permission {
    @Test
    fun `正常 添加和移除权限`() {
      val role = RoleAggregate.create("user", "普通用户")
      val perm1 = AggregateId.create()
      val perm2 = AggregateId.create()
      assertNotEquals(perm1, perm2, "perm1 $perm1 perm2 $perm2")
      role.addPermission(perm1)
      role.addPermission(perm2)
      assertTrue(role.getPermissionIds().contains(perm1))
      assertTrue(role.getPermissionIds().contains(perm2))
      role.removePermission(perm1)
      assertFalse(role.getPermissionIds().contains(perm1))
      assertTrue(role.getPermissionIds().contains(perm2))
    }
  }
} 
