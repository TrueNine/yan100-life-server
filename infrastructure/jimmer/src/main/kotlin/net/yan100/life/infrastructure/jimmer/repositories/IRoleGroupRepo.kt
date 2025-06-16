package net.yan100.life.infrastructure.jimmer.repositories

import net.yan100.compose.rds.IRepo
import net.yan100.life.infrastructure.jimmer.entities.RoleGroup
import org.springframework.stereotype.Repository

@Repository
interface IRoleGroupRepo : IRepo<RoleGroup, Long> 
