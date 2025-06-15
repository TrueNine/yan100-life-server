# yan100-life-server 架构设计

## 概述

本项目采用领域驱动设计（DDD）、六边形架构（Hexagonal Architecture）、CQRS（命令查询职责分离）和事件驱动架构（EDA）来构建一个可扩展、可维护的生活服务平台。

## 架构层次

### 1. 领域层（Domain Layer）

位置：`domain/`

核心业务逻辑所在层，包含：

- **聚合根（Aggregate Root）**：`UserAccountAggregate`、`PostContentAggregate`
- **领域事件（Domain Events）**：各种业务事件如 `UserAccountCreatedEvent`、`PostContentApprovedEvent`
- **仓储接口（Repository Interface）**：定义数据访问契约
- **枚举和值对象**：业务常量和值对象

### 2. 应用层（Application Layer）

位置：`application/`

协调领域层完成用例，包含：

- **命令（Commands）**：表示系统状态变更的意图
- **查询（Queries）**：表示数据查询请求
- **命令处理器（Command Handlers）**：处理命令并调用领域逻辑
- **查询处理器（Query Handlers）**：处理查询请求
- **事件处理器（Event Handlers）**：响应领域事件
- **DTO**：数据传输对象

### 3. 基础设施层（Infrastructure Layer）

位置：`Infrastructure/jimmer/`

技术实现细节，包含：

- **仓储实现**：基于 Jimmer ORM 的数据访问实现
- **事件发布器**：基于 Spring Events 的事件发布实现
- **CQRS 总线**：命令和查询总线的实现
- **实体映射**：Jimmer 实体定义

### 4. 适配器层（Adapter Layer）

位置：`rest/`

外部接口适配，包含：

- **REST API**：HTTP 接口适配器
- **DTO 转换**：请求/响应模型
- **配置类**：Spring Boot 配置

## 核心设计模式

### 六边形架构

- 核心业务逻辑位于中心（领域层）
- 通过端口（接口）与外部交互
- 适配器负责协议转换

### CQRS（命令查询职责分离）

- **命令端**：负责状态变更
  - 命令 → 命令处理器 → 聚合根 → 领域事件
- **查询端**：负责数据读取
  - 查询 → 查询处理器 → 直接数据库查询

### 事件驱动架构（EDA）

- 聚合根产生领域事件
- 事件通过事件发布器分发
- 事件处理器响应事件执行副作用

## 业务流程示例

### 用户注册流程

1. REST API 接收注册请求
2. 创建 `CreateUserAccountCommand`
3. 命令总线路由到 `CreateUserAccountCommandHandler`
4. 处理器创建 `UserAccountAggregate`
5. 聚合根产生 `UserAccountCreatedEvent`
6. 仓储保存聚合根
7. 事件发布器发布事件
8. 事件处理器执行后续操作（如发送欢迎邮件）

### 帖子审核流程

1. 用户创建帖子，状态为 `PRE_AUDIT`
2. 审核员调用审核接口
3. 创建 `ApprovePostCommand` 或 `RejectPostCommand`
4. 命令处理器更新帖子状态
5. 产生相应的领域事件
6. 事件处理器通知用户审核结果

## 技术栈

- **语言**：Kotlin
- **框架**：Spring Boot 3.x
- **ORM**：Jimmer
- **数据库**：PostgreSQL
- **构建工具**：Gradle

## 扩展点

### 添加新的聚合根

1. 在 `domain` 层创建聚合根类
2. 定义相关的领域事件
3. 创建仓储接口
4. 在基础设施层实现仓储

### 添加新的用例

1. 定义命令/查询类
2. 实现对应的处理器
3. 在适配器层暴露接口

### 集成新的外部系统

1. 在基础设施层创建适配器
2. 定义防腐层接口
3. 在应用层调用

## 优势

1. **高内聚低耦合**：各层职责明确，依赖关系清晰
2. **易于测试**：核心业务逻辑独立于技术细节
3. **灵活扩展**：新功能可以通过添加新的命令/查询实现
4. **事件溯源就绪**：基于事件的架构便于后续实现事件溯源
5. **技术无关**：核心业务逻辑不依赖具体技术实现 
