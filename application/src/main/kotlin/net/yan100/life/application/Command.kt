package net.yan100.life.application

import kotlinx.coroutines.flow.Flow

/**
 * 命令标记接口
 */
interface Command

/**
 * 带返回结果的命令
 */
interface CommandWithResult<out TResult> : Command

/**
 * 命令处理器接口
 */
interface CommandHandler<in TCommand : Command> {
  suspend fun handle(command: TCommand)
}

/**
 * 带返回结果的命令处理器
 */
interface CommandHandlerWithResult<in TCommand : CommandWithResult<TResult>, out TResult> {
  suspend fun handle(command: TCommand): TResult
}

/**
 * 命令总线接口
 */
interface CommandBus {
  suspend fun <TCommand : Command> send(command: TCommand)
  suspend fun <TCommand : CommandWithResult<TResult>, TResult> send(command: TCommand): TResult
}

/**
 * 查询标记接口
 */
interface Query<out TResult>

/**
 * 流式查询标记接口
 */
interface StreamQuery<out TResult> : Query<Flow<TResult>>

/**
 * 查询处理器接口
 */
interface QueryHandler<in TQuery : Query<TResult>, out TResult> {
  suspend fun handle(query: TQuery): TResult
}

/**
 * 查询总线接口
 */
interface QueryBus {
  suspend fun <TQuery : Query<TResult>, TResult> send(query: TQuery): TResult
} 
