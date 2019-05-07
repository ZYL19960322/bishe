package com.bishe.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.server.ServerEndpoint;

/**
 * Created by ZYL on 2019/3/12.
 */
@ServerEndpoint(value = "/webSocket")
@Component
public class RoomWebSocket {
 Logger log= LoggerFactory.getLogger(RoomWebSocket.class);

    public RoomWebSocket() {
        log.info("RoomWebSocket初始化");
    }

}
