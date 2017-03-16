## 功能介绍

### 持久化事件框架
基于google的EventBus封装的同步事件框架，可以持久化事件。

* **持久化事件：** 可以选择是否持久化指定事件，并通过任务调度系统执行持久化的事件。如果将业务操作与事件发送置于同一个事务中，可确保业务操作和事件的一致性。

### 消息中间件客户端API
基于消息中件间特点抽象出一套统一的消息发送、接收接口，简化使用。

* **消息两阶段提交：* ** 基于持久化事件框架的模拟消息两阶段提交，可以确保DB操作和消息发送的一致性。

