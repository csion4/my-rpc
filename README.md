# 基于Netty的Rpc调度框架+SpringBootStarter

## 简介
这是一个简单的基于Netty实现的RPC框架，让我们可以摒弃当前市面上高度封装的RPC框架复杂的源码而回归RCP本身；网络搜索RPC会出现成千上万条关于RPC的讲解，但是任何文字的解读都没有代码语言来的直接，跨越语言的翻译是达不到本质的，试问哪国的语言可以翻译出“大江东去浪淘尽,千古风流人物”的宏伟与壮美呢？

SpringBootStarter则是该RPC Demo对SpringBoot的集成，和mybatis-spring-boot-starter是一样的，也可以通过本项目简单的SpringBootStarter来理解mybatis-spring-boot-starter；

## SpringBoot项目使用
### 1，服务端启动引入依赖包
    <!-- 服务端引入my-rpc依赖       -->
    <dependency>
       <groupId>com.csion</groupId>
       <artifactId>my-rpc-server-spring-boot-starter</artifactId>
       <version>1.0-SNAPSHOT</version>
    </dependency>
### 2，服务端配置文件
    myrpc:
      server:
        port: 8101  # rpc服务端端口
        so_backlog: 1024    # netty连接配置，默认128
### 3，客户端引入依赖包
    <!-- 客户端引入my-rpc依赖       -->
    <dependency>
        <groupId>com.csion</groupId>
        <artifactId>my-rpc-client-spring-boot-starter</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
### 4，客户端配置文件
    myrpc:
      client:
        server: 127.0.0.1   # 服务端端口，暂时写死，后面可以结合注册中心中获取
        port: 8101  # 连接服务端端口
### 5，客户端启动类添加注解@EnableMyRpcClient
    @SpringBootApplication
    @EnableMyRpcClient
    public class Application {
        public static void main(String[] args) throws Exception{
    
            SpringApplication.run(Application.class);
        }
    }
### 6，客户端server接口添加注解@MyRpcService
    @MyRpcService
    public interface Services1 {
        String test1();
    }

## My-Rpc流程分析
    1.服务端启动 -> 开启netty服务端连接 -> 实现com.csion.server.invoke.InvokeHandler调用
    2.客户端启动 -> 自动扫描所有的@MyRpcService -> 启动netty客户端 -> 生成代理类 -> 注入到spring容器中
