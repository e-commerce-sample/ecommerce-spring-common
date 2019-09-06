### 项目简介
- 该项目为Ecommerce系统所有服务共享的Spring基础配置，包含:

|功能|所在目录|
| --- | --- |
|Spring Boot项目基础配置|`src/main/java/com/ecommerce/spring/common`|
|通用异常处理|`src/main/java/com/ecommerce/spring/common/exception`|
|领域事件相关|`src/main/java/com/ecommerce/spring/common/event`|


Ecommerce项目包括：

|代码库|用途|地址|
| --- | --- | --- |
|ecommerce-order-service|Order服务|[https://github.com/e-commerce-sample/ecommerce-order-service](https://github.com/e-commerce-sample/ecommerce-order-service)|
|ecommerce-product-service|Product服务|[https://github.com/e-commerce-sample/ecommerce-product-service](https://github.com/e-commerce-sample/ecommerce-product-service)|
|ecommerce-inventory-service|Inventory服务|[https://github.com/e-commerce-sample/ecommerce-inventory-service](https://github.com/e-commerce-sample/ecommerce-inventory-service)|
|ecommerce-shared-model|共享模型，不含Spring|[https://github.com/e-commerce-sample/ecommerce-shared-model](https://github.com/e-commerce-sample/ecommerce-shared-model)|
|ecommerce-spring-common|Spring共享基础配置|[https://github.com/e-commerce-sample/ecommerce-spring-common](https://github.com/e-commerce-sample/ecommerce-spring-common)|
|ecommerce-devops|基础设施|[https://github.com/e-commerce-sample/ecommerce-devops](https://github.com/e-commerce-sample/ecommerce-devops)|

# 技术选型
Spring Boot、Gradle、MySQL、Junit 5、Rest Assured、Docker、RabbitMQ/Kafka


### 命令行用法：

|命令|用途|
| --- | --- |
|`./idea.sh`|生成IntelliJ工程文件|
|`./local-build.sh`|本地构建|
|`./publish.sh`|发布到mymavenrepo.com仓库，发布新版本时需要修改`gradle.properties`文件中的`version`变量|

- 本项目使用`mymavenrepo.com`作为maven发布仓库:[网站地址](https://mymavenrepo.com/app/repos/F0lRvilYH123TUeMr5GN/)
- 发布时使用的URL: https://mymavenrepo.com/repo/Cd07WrKAtJ9Kq7PBaTuf/
- 依赖时使用的URL: https://mymavenrepo.com/repo/2w5k9sU2AsKfaYehyqno/
