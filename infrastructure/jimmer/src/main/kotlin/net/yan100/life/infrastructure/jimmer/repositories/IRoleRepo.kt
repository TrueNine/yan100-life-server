package net.yan100.life.infrastructure.jimmer.repositories

import net.yan100.compose.RefId
import net.yan100.compose.rds.IRepo
import net.yan100.life.infrastructure.jimmer.entities.Role
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Repository

@Primary
@Repository
interface IRoleRepo : IRepo<Role, RefId>
