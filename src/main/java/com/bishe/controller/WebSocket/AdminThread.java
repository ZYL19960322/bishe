package com.bishe.controller.WebSocket;

import com.bishe.dao.AdminMessageDao;
import com.bishe.dao.RemindDao;
import com.bishe.dto.CheckAgainActivity;
import com.bishe.dto.RemindMessage;
import com.bishe.pojo.AdminMessage;
import com.bishe.pojo.Remind;
import com.bishe.service.AdminMessageService;
import com.bishe.service.RemindService;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;
import java.util.List;

/**
 * Created by ZYL on 2019/3/18.
 */
public  class  AdminThread extends Thread {
  Logger log=  LoggerFactory.getLogger(AdminThread.class);
     private   AdminMessageService adminMessageService;
     private Integer listCount;
    //管理员id
    private String adminId;
    //将传来的参赋值给AdminThread的flagParam静态变量，线程根据flagParam执行不同的操作
    //1：查询最新消息 2：查询未处理消息 3：查询已完成消息 4：查询提醒型消息 5：查询在审核消息
    public static Integer flagParam;
    private Session session;
    public AdminThread() {
    }
    public AdminThread(String adminId) {
        this.adminId = adminId;
    }

    public AdminThread(String adminId, Session session) {
        this.adminId = adminId;
        this.session = session;
    }
    @Override
    public void run() {
        synchronized (AdminThread.class) {
            AdminWebSocket adminWebSocket = AdminWebSocket.onliners.get(adminId);
            if(flagParam==1) {
                adminMessageService = (AdminMessageService) GetBeanUtil.getBean(AdminMessageService.class);
                List<AdminMessage> list = adminMessageService.selectAllUnreadMessageByAdminId(adminId);
                listCount=list.size();
            }
            if(flagParam==2){
                adminMessageService = (AdminMessageService) GetBeanUtil.getBean(AdminMessageService.class);
                List<CheckAgainActivity> list= adminMessageService.selectUndealMessageByAdminId(adminId);
                listCount=list.size();
            }
            if(flagParam==3) {
                adminMessageService = (AdminMessageService) GetBeanUtil.getBean(AdminMessageService.class);
                List<CheckAgainActivity> list = adminMessageService.selectFinishedMessageByAdminId(adminId);
                listCount=list.size();

            }if(flagParam==4){
                RemindDao  remindDao = (RemindDao) GetBeanUtil.getBean(RemindDao.class);
                List<Remind> list = remindDao.selectAllRemindByAdminId(adminId);
                listCount=list.size();
            }
            if(flagParam==5) {
                AdminMessageDao adminMessageDao = (AdminMessageDao) GetBeanUtil.getBean(AdminMessageDao.class);
                List<AdminMessage> list = adminMessageDao.selectAllCheckAgainMessageByAdminId(adminId);
                listCount=list.size();
            }
            log.info("当前线程的名字" + currentThread().getName() + "线程组名" + currentThread().getThreadGroup().getName() + "共有onLiners管理员在线" + AdminWebSocket.onliners.size()+"共有adminThreadMap管理员在线" + AdminWebSocket.adminThreadMap.size());
                if (listCount == 0 || listCount==null) {
                    log.info("数据库查询消息为空");
                }
                if (adminWebSocket == null) {
                    log.info("bullwebSocket为空");
                }
                try {
                    try {
                        if(listCount!=0){
                            adminWebSocket.session.getBasicRemote().sendObject(listCount);
                        }
                    } catch (EncodeException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                log.info("管理员线程结束");

            }
    }
}
