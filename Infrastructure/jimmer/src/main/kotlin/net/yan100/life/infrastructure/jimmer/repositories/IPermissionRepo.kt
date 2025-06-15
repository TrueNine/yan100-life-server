package net.yan100.life.infrastructure.jimmer.repositories

import net.yan100.compose.rds.IRepo
import net.yan100.life.infrastructure.jimmer.entities.Permission
import org.springframework.stereotype.Repository

@Repository
interface IPermissionRepo : IRepo<Permission, Long> 
