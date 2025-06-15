package net.yan100.life.domain.rbac

import net.yan100.life.domain.AggregateId
import net.yan100.life.domain.AggregateRoot
import net.yan100.life.domain.DomainEvent

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

/** 角色组创建事件 */
data class RoleGroupCreatedEvent(
    override val aggregateId: AggregateId,
    val code: String,
    val name: String
) : DomainEvent(aggregateId = aggregateId)
/** 角色组添加角色事件 */
data class RoleGroupRoleAddedEvent(
    override val aggregateId: AggregateId,
    val roleId: AggregateId
) : DomainEvent(aggregateId = aggregateId)
/** 角色组移除角色事件 */
data class RoleGroupRoleRemovedEvent(
    override val aggregateId: AggregateId,
    val roleId: AggregateId
) : DomainEvent(aggregateId = aggregateId) 
