1、app-parent 父项目，用来给各个子模块继承，方便管理，一般对它下的pom文件做clean、install等操作
2、app-train-common 公共组件，打成jar包，不需要单独部署
3、app-train-dal 数据访问层，打成jar包，不需要单独部署
4、app-train-service 业务逻辑处理，打成jar包，不需要单独部署
5、app-train-manage 后台管理，需要打成war单独部署
6、app-train-portal	门户，需要打成war单独部署
7、app-train-gateway 提供给外部的接口，需要打成war单独部署
8、app-train-test 测试代码，不需要打包发布
