package com.bishe.service;

import com.bishe.pojo.Activity;
import com.bishe.pojo.Message;

/**
 * Created by ZYL on 2019/3/18.
 */
public interface MessageService {
    void addAMessage(Message message);
    void addMessagesByChangeActivity(Activity activity);
}
