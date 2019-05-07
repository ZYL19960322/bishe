package com.bishe.controller.WebSocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * Created by ZYL on 2019/3/18.
 */
@Configuration
public class GetHttpSessionConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        HttpSession httpSession=(HttpSession) request.getHttpSession();

        sec.getUserProperties().put(HttpSession.class.getName(),httpSession);
    }
}
