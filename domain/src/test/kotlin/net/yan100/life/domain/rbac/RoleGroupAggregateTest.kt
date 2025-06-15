package net.yan100.life.domain.rbac

import net.yan100.life.domain.AggregateId
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class RoleGroupAggregateTest {
    @Test
    fun `test create role group`() {
        val group = RoleGroupAggregate.create("group1", "默认组", "默认角色组")
        assertEquals("group1", group.code)
        assertEquals("默认组", group.name)
        assertEquals("默认角色组", group.description)
    }

    @Test
    fun `test add and remove role`() {
        val group = RoleGroupAggregate.create("group2", "测试组")
        val role1 = AggregateId.create()
        val role2 = AggregateId.create()
        group.addRole(role1)
        group.addRole(role2)
        assertTrue(group.getRoleIds().contains(role1))
        assertTrue(group.getRoleIds().contains(role2))
        group.removeRole(role1)
        assertFalse(group.getRoleIds().contains(role1))
        assertTrue(group.getRoleIds().contains(role2))
    }
} 
