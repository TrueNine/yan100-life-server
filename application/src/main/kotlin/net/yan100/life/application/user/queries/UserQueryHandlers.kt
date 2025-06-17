package net.yan100.life.application.user.queries

import net.yan100.compose.Pr
import net.yan100.life.application.QueryHandler
import net.yan100.life.application.dto.UserDetailDto
import net.yan100.life.application.dto.UserView
import org.springframework.stereotype.Service

@Service
class GetUserDetailQueryHandler : QueryHandler<GetUserDetailQuery, UserDetailDto?> {
  override suspend fun handle(query: GetUserDetailQuery): UserDetailDto? {
    // TODO: 实现用户详情查询逻辑
    // 这里应该调用查询仓储或读模型服务
    return null
  }
}

@Service
class GetUserByAccountQueryHandler : QueryHandler<GetUserByAccountQuery, UserView?> {
  override suspend fun handle(query: GetUserByAccountQuery): UserView? {
    // TODO: 实现通过账号查询用户逻辑
    // 这里应该调用查询仓储或读模型服务
    return null
  }
}

@Service
class GetUserByPhoneQueryHandler : QueryHandler<GetUserByPhoneQuery, UserView?> {
  override suspend fun handle(query: GetUserByPhoneQuery): UserView? {
    // TODO: 实现通过手机号查询用户逻辑
    // 这里应该调用查询仓储或读模型服务
    return null
  }
}

@Service
class SearchUsersQueryHandler : QueryHandler<SearchUsersQuery, Pr<UserView>> {
  override suspend fun handle(query: SearchUsersQuery): Pr<UserView> {
    // TODO: 实现用户搜索逻辑
    // 这里应该调用查询仓储或读模型服务
    @Suppress("UNCHECKED_CAST")
    return Pr.empty() as Pr<UserView>
  }
} 