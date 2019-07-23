- 该项目为ecommerce系统所有子系统共享的java包，包含:

|功能|所在目录|
| --- | --- |
|Spring Boot项目基础配置|configuration|
|通用异常处理|exception|
|通用日志处理|logging|
|分布式锁|distributedlock|
|RabbitMQ基础配置|event|
|所有子系统的领域事件|event|
|常用工具类|utils|

- 命令行用法：

|命令|用途|
| --- | --- |
|`./idea.sh`|生成IntelliJ工程文件|
|`./local-build.sh`|本地构建|
|`./publish.sh`|发布到mymavenrepo.com仓库，发布新版本时需要修改`build.gradle`文件中的`theVersion`变量|

- 本项目使用mymavenrepo.com作为maven发布仓库:[网站地址](https://mymavenrepo.com/app/repos/F0lRvilYH123TUeMr5GN/)
- 发布时使用的URL: https://mymavenrepo.com/repo/Cd07WrKAtJ9Kq7PBaTuf/
- 依赖时使用的URL: https://mymavenrepo.com/repo/2w5k9sU2AsKfaYehyqno/