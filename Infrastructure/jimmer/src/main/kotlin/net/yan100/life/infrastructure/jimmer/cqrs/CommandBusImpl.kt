package net.yan100.life.infrastructure.jimmer.cqrs

import net.yan100.life.application.Command
import net.yan100.life.application.CommandBus
import net.yan100.life.application.CommandHandler
import net.yan100.life.application.CommandHandlerWithResult
import net.yan100.life.application.CommandWithResult
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import kotlin.reflect.KClass
import kotlin.reflect.full.allSupertypes
import kotlin.reflect.full.isSubclassOf

@Component
class CommandBusImpl(
  private val applicationContext: ApplicationContext,
) : CommandBus {

  override suspend fun <TCommand : Command> send(command: TCommand) {
    val handler = findHandler<CommandHandler<TCommand>>(command::class)
      ?: error("未找到命令处理器: ${command::class.simpleName}")
    handler.handle(command)
  }

  override suspend fun <TCommand : CommandWithResult<TResult>, TResult> send(command: TCommand): TResult {
    @Suppress("UNCHECKED_CAST")
    val handler = findHandler<CommandHandlerWithResult<TCommand, TResult>>(command::class)
      ?: error("未找到命令处理器: ${command::class.simpleName}")
    return handler.handle(command)
  }

  private inline fun <reified T> findHandler(commandClass: KClass<*>): T? {
    val beans = applicationContext.getBeansOfType(T::class.java)
    return beans.values.firstOrNull { handler ->
      val handlerClass = handler!!::class
      val commandType = getHandledCommandType(handlerClass)
      commandType != null && commandClass.isSubclassOf(commandType)
    }
  }

  private fun getHandledCommandType(handlerClass: KClass<*>): KClass<*>? {
    // 查找实现的处理器接口
    val handlerInterface = handlerClass.allSupertypes.firstOrNull { type ->
      val classifier = type.classifier as? KClass<*>
      classifier != null && (
        classifier == CommandHandler::class ||
          classifier == CommandHandlerWithResult::class
        )
    }

    // 获取泛型参数（命令类型）
    return handlerInterface?.arguments?.firstOrNull()?.type?.classifier as? KClass<*>
  }
} 
