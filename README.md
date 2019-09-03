### 项目简介
- 该项目为Ecommerce系统所有服务共享的Spring基础配置，包含:

|功能|所在目录|
| --- | --- |
|Spring Boot项目基础配置|configuration|
|通用异常处理|exception|
|分布式锁|distributedlock|
|RabbitMQ基础配置|event|
|常用工具类|utils|


Ecommerce项目包括：

|代码库|用途|地址|
| --- | --- | --- |
|order-backend|Order服务|[https://github.com/e-commerce-sample/order-backend](https://github.com/e-commerce-sample/order-backend)|
|product-backend|Product服务|[https://github.com/e-commerce-sample/product-backend](https://github.com/e-commerce-sample/product-backend)|
|inventory-backend|Inventory服务|[https://github.com/e-commerce-sample/inventory-backend](https://github.com/e-commerce-sample/inventory-backend)|
|common|共享依赖包|[https://github.com/e-commerce-sample/common](https://github.com/e-commerce-sample/common)|
|devops|基础设施|[https://github.com/e-commerce-sample/devops](https://github.com/e-commerce-sample/devops)|

# 技术选型
Spring Boot、Gradle、MySQL、Junit 5、Rest Assured、Docker、RabbitMQ


### 命令行用法：

|命令|用途|
| --- | --- |
|`./idea.sh`|生成IntelliJ工程文件|
|`./local-build.sh`|本地构建|
|`./publish.sh`|发布到mymavenrepo.com仓库，发布新版本时需要修改`build.gradle`文件中的`theVersion`变量|

- 本项目使用mymavenrepo.com作为maven发布仓库:[网站地址](https://mymavenrepo.com/app/repos/F0lRvilYH123TUeMr5GN/)
- 发布时使用的URL: https://mymavenrepo.com/repo/Cd07WrKAtJ9Kq7PBaTuf/
- 依赖时使用的URL: https://mymavenrepo.com/repo/2w5k9sU2AsKfaYehyqno/


### 注意事项
- 添加新类型的event类时必须在DomainEvent基类中注册`@JsonSubTypes`，比如OrderPaidEvent：
```java
//...
@JsonSubTypes({
//...
        @Type(value = OrderPaidEvent.class, name = "ORDER_PAID"),
//...
})
public abstract class DomainEvent {
    
}
//...
```