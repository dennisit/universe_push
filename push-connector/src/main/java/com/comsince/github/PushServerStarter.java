package com.comsince.github;

import com.comsince.github.handler.PushMessageHandler;
import org.tio.cluster.TioClusterConfig;
import org.tio.server.ServerGroupContext;
import org.tio.server.TioServer;
import org.tio.server.intf.ServerAioHandler;
import org.tio.server.intf.ServerAioListener;

import java.io.IOException;

/**
 * @author comsicne
 *         Copyright (c) [2019] [Meizu.inc]
 * @Time 19-2-14 上午10:21
 **/
public class PushServerStarter{
    //handler, 包括编码、解码、消息处理
    public static ServerAioHandler aioHandler = new PushMessageHandler();

    //事件监听器，可以为null，但建议自己实现该接口，可以参考showcase了解些接口
    public static ServerAioListener aioListener = null;

    //一组连接共用的上下文对象
    public static ServerGroupContext serverGroupContext = new ServerGroupContext("push-conector-tio-server", aioHandler, aioListener);

    //tioServer对象
    public static TioServer tioServer = new TioServer(serverGroupContext);

    //有时候需要绑定ip，不需要则null
    public static String serverIp = null;

    //监听的端口
    public static int serverPort = Const.PORT;

    //集群配置
    public static TioClusterConfig tioClusterConfig;

    /**
     * 启动程序入口
     */
    public static void main(String[] args) throws IOException {
        init();
    }

    public static void init() throws IOException{
        //RedissonClient redissonClient = Redisson.create();
        //tioClusterConfig = new TioClusterConfig(new RedissonTioClusterTopic("push-channel",redissonClient));
        //serverGroupContext.setTioClusterConfig(tioClusterConfig);
        serverGroupContext.setHeartbeatTimeout(Const.TIMEOUT);
        tioServer.start(serverIp, serverPort);
    }

}
