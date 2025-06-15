package net.yan100.life.domain.rbac

import net.yan100.life.domain.AggregateId
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class UserPermissionAggregateTest {
    @Test
    fun `test assign and remove role group`() {
        val userId = AggregateId.create()
        val group1 = AggregateId.create()
        val group2 = AggregateId.create()
        val userPerm = UserPermissionAggregate(userId)
        userPerm.assignRoleGroup(group1)
        userPerm.assignRoleGroup(group2)
        assertTrue(userPerm.getRoleGroupIds().contains(group1))
        assertTrue(userPerm.getRoleGroupIds().contains(group2))
        userPerm.removeRoleGroup(group1)
        assertFalse(userPerm.getRoleGroupIds().contains(group1))
        assertTrue(userPerm.getRoleGroupIds().contains(group2))
    }

    @Test
    fun `test revoke and restore permission`() {
        val userId = AggregateId.create()
        val perm1 = AggregateId.create()
        val perm2 = AggregateId.create()
        val userPerm = UserPermissionAggregate(userId)
        userPerm.revokePermission(perm1)
        userPerm.revokePermission(perm2)
        assertTrue(userPerm.getRevokedPermissionIds().contains(perm1))
        assertTrue(userPerm.getRevokedPermissionIds().contains(perm2))
        userPerm.restorePermission(perm1)
        assertFalse(userPerm.getRevokedPermissionIds().contains(perm1))
        assertTrue(userPerm.getRevokedPermissionIds().contains(perm2))
    }
} 
