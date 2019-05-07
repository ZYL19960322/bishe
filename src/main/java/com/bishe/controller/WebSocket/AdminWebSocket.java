package com.bishe.controller.WebSocket;

import com.bishe.config.WebSocketConfig;
import com.bishe.pojo.Admin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import static java.lang.Thread.currentThread;

/**
 * Created by ZYL on 2019/3/12.
 */
/**
 * @ServerEndpoint 该注解用来指定一个URI，客户端可以通过这个URI来连接到WebSocket。
 * 类似Servlet的注解mapping。无需在web.xml中配置。
 * configurator = SpringConfigurator.class是为了使该类可以通过Spring注入。
 * @Author jiangpeng
 */
@ServerEndpoint(value="/admin/webSocket/{param}",configurator=WebSocketConfig.class)
@Component
public   class AdminWebSocket {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminWebSocket.class);

    private static ApplicationContext applicationContext;
    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    public AdminWebSocket() {

    }
    // concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<AdminWebSocket> BULLETIN_WEBSOCKETS = new CopyOnWriteArraySet<AdminWebSocket>();
    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    public Session session;

    //存放当前的用户数据
   private  String adminId;




    //类加载初始化一个map集合，存放用户的websocket对象
      public static ConcurrentHashMap<String, AdminWebSocket> onliners;
     static {onliners=new ConcurrentHashMap<String, AdminWebSocket>() ;
     }
     //管理员线程组(没有作用。。。。)
      public static ThreadGroup adminsThread;
    static {
         adminsThread=new ThreadGroup("adminsThread");
     }
    //存放管理员的线程，以管理员的id作为key
    public static ConcurrentHashMap<String,AdminThread> adminThreadMap;
    static {adminThreadMap=new ConcurrentHashMap<String,AdminThread>();}



    /**
     * 连接建立成功调用的方法
     * */
    @OnOpen
    public  void onOpen(@PathParam("param") Integer param,Session session, EndpointConfig config ) throws IOException {
        this.session = session;
        //将传来的参赋值给AdminThread的flagParam静态变量，线程根据flagParam执行不同的操作
        //1：查询最新消息 2：查询未处理消息 3：查询未读消息 4：查询已处理消息 5：查询在审核消息
        AdminThread.flagParam = param;

        LOGGER.info("onOpen方法执行了和传来的参数" + param);
        //获取httpsession
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        //通过httpsession获取当前访问者,有可能是管理员也可能是学生
        //是管理员时
        Admin admin = (Admin) httpSession.getAttribute("admin");
        this.adminId=admin.getAdminId();
        if (admin != null) {
            //   onliners.put(admin.getAdminId(),this);
            LOGGER.info("管理员来了");
            LOGGER.info("这是管理员" + admin);
            onliners.put(adminId, this);
            //创建管理员的线程
            AdminThread adminThread = null;
            adminThread = new AdminThread(adminId, session);
            adminThreadMap.put(adminId, adminThread);
            // //将管理员的线程adminThread放入管理员的线程组adminsThread,并使用管理员的id作为线程在线程组中的名字
            //Thread thread = new Thread(adminsThread, adminThread, admin.getAdminId());
            // LOGGER.info("线程组的名字"+adminsThread.getName());
            LOGGER.info("当前线程" + currentThread().getName() + "线程组名" + currentThread().getThreadGroup().getName());
            //使用线程的join方法的效果是使main线程等待当前的adminThread线程执行完之后再执行，不然的话main线程和adminThread会并发执行
            //实现run方法的目的是线程中start的方法只能调用一次，多次调用会发生异常，而run方法可以调用多次
            adminThread.run();
            try {
                adminThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    }

    @OnClose
    public void onClose() {
        //*注意*当前方法下的adminIdClose与adminId不是同一个值adminId存储的最后访问这里的管理员id然而adminIdClose存储的是执行onClose方法的管理员的id
        //删除对应的学生线程和webSocket
       String adminIdClose=this.adminId;
        Iterator iterator = onliners.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            if (adminIdClose.equals(key)) {
                onliners.remove(key);
            }
        }

            Iterator threadIterator = adminThreadMap.keySet().iterator();
            while (threadIterator.hasNext()) {
                String threadKey = (String) threadIterator.next();
                if (adminIdClose.equals(threadKey)) {
                    adminThreadMap.remove(threadKey);
                }
            }


        LOGGER.info("onClose方法执行了"+adminIdClose+"下线了");
        BULLETIN_WEBSOCKETS.remove(this);
        LOGGER.info("有一连接关闭！当前在线人数为"+getOnlineCount()+"==onlines=="+onliners.size()+"threadMap"+adminThreadMap.size(), getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        LOGGER.info("onMessage方法执行了");

        this.session.getBasicRemote().sendText(new Date()+"收到客户端消息后调用方法");

        LOGGER.info("来自客户端的信息：{}", message);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        //*注意*当前方法下的adminIdClose与adminId不是同一个值adminId存储的最后访问这里的管理员id然而adminIdClose存储的是执行onClose方法的管理员的id
        //删除对应的学生线程和webSocket
        String adminIdClose=this.adminId;
        Iterator iterator = onliners.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            if (adminIdClose.equals(key)) {
                onliners.remove(key);
            }
        }

        Iterator threadIterator = adminThreadMap.keySet().iterator();
        while (threadIterator.hasNext()) {
            String threadKey = (String) threadIterator.next();
            if (adminIdClose.equals(threadKey)) {
                adminThreadMap.remove(threadKey);
            }
        }

        LOGGER.info("onError方法执行了");
        LOGGER.error("发生错误：{}", session.toString());
        error.printStackTrace();
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     * 因为使用了Scheduled定时任务，所以方法不是有参数
     * @throws Exception
     */
    @Scheduled(cron = "0/2 * * * * ?")
    public void sendMessage() throws IOException {
        LOGGER.info("sendMessage方法执行了");
        // 所有在线用户广播通知
        BULLETIN_WEBSOCKETS.forEach(socket -> {
            try {
                socket.session.getBasicRemote().sendText(new Date()+"sendMessage发来的信息");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static synchronized int getOnlineCount() {
        LOGGER.info("getOnlineCount方法执行了");
        return BULLETIN_WEBSOCKETS.size();
    }
}
