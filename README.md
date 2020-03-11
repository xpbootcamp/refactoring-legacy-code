# 重构「Legacy Code」

## 背景描述
在 cn.xpbootcamp.legacy_code package下有一些遗留系统的代码，WalletTransaction是经过抽象简化之后的一个电商系统的交易类，用来记录每笔订单交易的情况。WalletTransaction类中的execute()函数负责执行转账操作，将钱从买家的钱包转到卖家的钱包中。真正的转账操作是通过调用WalletService服务来完成，WalletService依赖了UserRepository，UserRepository依赖了数据库（不能直接调用，使用异常替代）。除此之外，代码中还涉及一个分布式锁 DistributedLock 单例类，DistributedLock会调用Redis服务（不能直接调用，使用异常替代），用来避免并发执行，导致用户的钱被重复转出。
WalletTransaction类的逻辑有点让人迷惑，你的任务是重构这些遗留代码（当然也包括其他代码）
基础系统代码库：https://github.com/xp-bootcamp/refactoring-legacy-code

## 作业要求
不能修改UserRepository的find方法和DistributedLock的lock和unlock方法
添加单元测试
小步提交