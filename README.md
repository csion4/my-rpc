方法调用->代理
    动态代理到netty网络传输（这里暂时没有服务发现模块，后面可以加）
通信协议->数据序列化方式
    通信协议用于指定传输格式（类似于http协议）
    序列化方式则是选择数据序列化方式，这里选择json（只要不适用jdk自带的就行）
网络传输
    采用netty