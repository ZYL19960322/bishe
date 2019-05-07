package com.bishe.controller.WebSocket;

import com.bishe.config.WebSocketConfig;
import com.bishe.pojo.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by ZYL on 2019/3/12.
 */

/**
 * @ServerEndpoint 该注解用来指定一个URI，客户端可以通过这个URI来连接到WebSocket。
 * 类似Servlet的注解mapping。无需在web.xml中配置。
 * configurator = SpringConfigurator.class是为了使该类可以通过Spring注入。
 * @Author jiangpeng
 */
@ServerEndpoint(value="/student/webSocket",configurator=WebSocketConfig.class)
@Component
public   class StudentWebSocket {
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentWebSocket.class);
    private static ApplicationContext applicationContext;
    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }
    public StudentWebSocket() {
    }
    // concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<StudentWebSocket> BULLETIN_WEBSOCKETS = new CopyOnWriteArraySet<StudentWebSocket>();
    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    public Session session;
    //存放当前的用户数据
    private String studentId;

    //类加载初始化一个map集合，存放用户的websocket对象
      public static ConcurrentHashMap<String,StudentWebSocket> onliners;
     static {onliners=new ConcurrentHashMap<String, StudentWebSocket>() ;
     }

    //存放学生的线程，以学生的id作为key
    public static ConcurrentHashMap<String,StudentThread> studentThreadMap;
    static {studentThreadMap=new ConcurrentHashMap<String,StudentThread>();}

    /**
     * 连接建立成功调用的方法
     * */
    @OnOpen
    public  void onOpen( Session session,EndpointConfig config) throws IOException {
        this.session = session;
        LOGGER.info("onOpen方法执行了");
        //获取httpsession
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        //通过httpsession获取当前访问者,有可能是管理员也可能是学生
        //是学生时
        Student student = (Student) httpSession.getAttribute("student");
        if(student!=null){
             this.studentId=student.getStudId();
            onliners.put(studentId,this);
            LOGGER.info("学生来了"+"这是学生"+student);
            //创建学生的线程
            StudentThread studentThread=null;
             studentThread = new StudentThread( studentId,session);
             //将学生的线程存放到学生线程map中
             studentThreadMap.put(studentId,studentThread);
             LOGGER.info("学生的线程现在开启");
             studentThread.run();
            try {
                //学生的线程插队到当前main线程
                studentThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOGGER.info("学生线程结束");
        }
    }

    @OnClose
    public void onClose() {
        //*注意*当前方法下的studentIdClose与adminId不是同一个值studentId存储的最后访问这里的管理员id然而studentIdClose存储的是执行onClose方法的管理员的id
        //删除对应的学生线程和webSocket
        String studentIdClose=this.studentId;
        Iterator iterator = onliners.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            if (studentIdClose.equals(key)) {
                onliners.remove(key);
            }
        }
        Iterator threadIterator = studentThreadMap.keySet().iterator();
        while (threadIterator.hasNext()) {
            String threadKey = (String) threadIterator.next();
            if (studentIdClose.equals(threadKey)) {
                studentThreadMap.remove(threadKey);
            }
        }
        LOGGER.info("onClose方法执行了");
        BULLETIN_WEBSOCKETS.remove(this);
        LOGGER.info("有一连接关闭！当前在线人数为"+getOnlineCount()+"==onlines=="+onliners.size()+"threadMap"+studentThreadMap.size(), getOnlineCount());
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
        //*注意*当前方法下的studentIdClose与adminId不是同一个值studentId存储的最后访问这里的管理员id然而studentIdClose存储的是执行onClose方法的管理员的id
        //删除对应的学生线程和webSocket
        String studentIdClose=this.studentId;
        Iterator iterator = onliners.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            if (studentIdClose.equals(key)) {
                onliners.remove(key);
            }
        }
        Iterator threadIterator = studentThreadMap.keySet().iterator();
        while (threadIterator.hasNext()) {
            String threadKey = (String) threadIterator.next();
            if (studentIdClose.equals(threadKey)) {
                studentThreadMap.remove(threadKey);
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
