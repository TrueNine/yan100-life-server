package net.yan100.life.domain.rbac

import net.yan100.life.domain.AggregateId
import net.yan100.life.domain.AggregateRoot

/**
 * 角色组聚合根
 */
class RoleGroupAggregate(
    override var id: AggregateId,
    var code: String, // 角色组唯一标识
    var name: String, // 角色组名称
    var description: String? = null,
    private val roleIds: MutableSet<AggregateId> = mutableSetOf()
) : AggregateRoot(id) {
    companion object {
        fun create(code: String, name: String, description: String? = null): RoleGroupAggregate {
            val id = AggregateId.create()
            val group = RoleGroupAggregate(id, code, name, description)
            group.raiseEvent(RoleGroupCreatedEvent(id, code, name))
            return group
        }
    }
    fun addRole(roleId: AggregateId) {
        if (roleIds.add(roleId)) {
            raiseEvent(RoleGroupRoleAddedEvent(id, roleId))
        }
    }
    fun removeRole(roleId: AggregateId) {
        if (roleIds.remove(roleId)) {
            raiseEvent(RoleGroupRoleRemovedEvent(id, roleId))
        }
    }
    fun getRoleIds(): Set<AggregateId> = roleIds.toSet()
} 
