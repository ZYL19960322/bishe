package com.bishe.controller.WebSocket;

import com.bishe.pojo.AdminMessage;
import com.bishe.pojo.StudentMessage;
import com.bishe.service.AdminMessageService;
import com.bishe.service.StudentMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;
import java.util.List;

/**
 * Created by ZYL on 2019/3/18.
 */
public class  StudentThread extends Thread {

    private List<StudentMessage> studentMessageList;

    //1:查询学生的所有最新消息
    //是否有最新消息标准
    Logger log=  LoggerFactory.getLogger(StudentThread.class);

    //学生id
    private String studId;
    private Session session;
    public StudentThread(String studId,Session session){
        this.studId=studId;
        this.session=session;
    }
    @Override
    public void run() {
        synchronized (StudentThread.class){
            //通过工具类获得service层
            StudentMessageService studentMessageService = (StudentMessageService) GetBeanUtil.getBean(StudentMessageService.class);
            studentMessageList = studentMessageService.selectAllUnreadMessageByStudId(studId);
            log.info("当前线程的名字" + currentThread().getName() + "线程组名" + currentThread().getThreadGroup().getName() + "共有onLiners学生在线" + StudentWebSocket.onliners.size()+"共有studentThreadMap学生在线" + StudentWebSocket.studentThreadMap.size());
            //获得学学生的webSocket
            StudentWebSocket studentWebSocket =StudentWebSocket.onliners.get(studId);
            if (studentWebSocket==null){
                log.info("学生的webSocket为空");
            }
            //给学生发送消息
            try {
                if(studentMessageList.size()!=0) {
                    studentWebSocket.session.getBasicRemote().sendObject(studentMessageList.size());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (EncodeException e) {
                e.printStackTrace();
            }
            log.info("给学生发送消息完成");
        }
    }
}
