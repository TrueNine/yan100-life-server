package net.yan100.life.infrastructure.jimmer.repositories

import net.yan100.compose.rds.IRepo
import net.yan100.life.infrastructure.jimmer.entities.Role
import org.springframework.stereotype.Repository

@Repository
interface IRoleRepo : IRepo<Role, Long> 
