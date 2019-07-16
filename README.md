# file-connect
Implement file-connect based on openmessaging-connector

###  如何启动connector

#### 一、下载file-connect

    1、git clone https://github.com/odbozhou/file-connect.git

    2、cd file-connect
       mvn package
   
#### 二、下载rocketmq-connect-runtime

    1、git clone https://github.com/apache/rocketmq-externals.git

    2、cd rocketmq-externals/rocketmq-connect-runtime

    3、mvn -Dmaven.test.skip=true package

    4、cd target/distribution/conf

-    a、修改connect.conf配置文件

    #1、rocketmq 配置
    namesrvAddr=127.0.0.1:9876
   
    #2、file-connect jar包路径
    pluginPaths=/home/connect/file-connect/target
   
    #3、runtime持久化文件目录
    storePathRootDir=/home/connect/storeRoot
   
    #4、http服务端口
    httpPort=8081
   
   
-    b、日志相关配置在logback.xml中修改
 
    注：rocketmq需要先创建cluster-topic，config-topic，offset-topic，position-topic
    4个topic，并且为了保证消息有序，每个topic可以只一个queue

### 三、启动Connector

1、启动runtime
回到rocketmq-externals/rocketmq-connect-runtime目录

    ./run_worker.sh

看到日志目录查看connect_runtime.log

如果看到以下日志说明runttiime启动成功了

2019-07-16 10:56:24 INFO RebalanceService - RebalanceService service started
2019-07-16 10:56:24 INFO main - The worker [DEFAULT_WORKER_1] boot success.

2、启动source connector
    
    GET请求
    
    http://localhost:8081/connectors/testSourceConnector1?config={"connector-class":"org.apache.rocketmq.connect.file.FileSourceConnector","topic":"fileTopic","filename":"/root/connect/rocketmq-externals/rocketmq-connect-runtime/source-file.txt","source-record-converter":"org.apache.rocketmq.connect.runtime.converter.JsonConverter"}   
   
   看到一下日志说明file source connector启动成功了
   
   2019-07-16 11:18:39 INFO pool-7-thread-1 - Source task start, config:{"properties":{"source-record-converter":"org.apache.rocketmq.connect.runtime.converter.JsonConverter","filename":"/root/connect/rocketmq-externals/rocketmq-connect-runtime/source-file.txt","task-class":"org.apache.rocketmq.connect.file.FileSourceTask","topic":"fileTopic","connector-class":"org.apache.rocketmq.connect.file.FileSourceConnector","update-timestamp":"1563247119715"}}
   
    注：创建topic："topic":"fileTopic"
    
3、启动sink connector
    
    GET请求
    
    http://localhost:8081/connectors/testSinkConnector1?config={"connector-class":"org.apache.rocketmq.connect.file.FileSinkConnector","topicNames":"fileTopic","filename":"/root/connect/rocketmq-externals/rocketmq-connect-runtime/sink-file.txt","source-record-converter":"org.apache.rocketmq.connect.runtime.converter.JsonConverter"}

看到一下日志说明file sink connector启动成功了

2019-07-16 11:24:58 INFO pool-7-thread-2 - Sink task start, config:{"properties":{"source-record-converter":"org.apache.rocketmq.connect.runtime.converter.JsonConverter","filename":"/root/connect/rocketmq-externals/rocketmq-connect-runtime/sink-file.txt","topicNames":"fileTopic","task-class":"org.apache.rocketmq.connect.file.FileSinkTask","connector-class":"org.apache.rocketmq.connect.file.FileSinkConnector","update-timestamp":"1563247498694"}}


查看配置中"filename":"/root/connect/rocketmq-externals/rocketmq-connect-runtime/sink-file.txt"
如果sink-file.txt生成并且月source-file.txt内容一样，说明整个流程已经跑通


### FAQ
Q：sink-file.txt文件中每行的文本顺序source-file.txt不一致？

A: rocketmq支持顺序消息只有将需要有序消息发送到同一个queue才能保持消息有序，所以实现有序消息有2中方式：1、一个topic创建一个queue（runtime目前只能使用这种方式）2、runtime支持顺序消息，通过消息中指定字段处理发送到rocketmq的同一queue（后续支持）

