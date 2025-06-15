package net.yan100.life.infrastructure.jimmer.entities

import net.yan100.compose.rds.entities.IEntity
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.ManyToMany

@Entity
interface Role : IEntity {
    val code: String
    val name: String
    val description: String?
    @ManyToMany
    val permissions: List<Permission>
} 
