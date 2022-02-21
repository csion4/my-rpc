# 基于Netty的Rpc调度框架+SpringBootStarter

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
        so_backlog: 1024    # netty连接配置，没人128
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
