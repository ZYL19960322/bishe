package com.bishe.service.impl;

import com.bishe.dao.AdminDao;
import com.bishe.pojo.Admin;
import com.bishe.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ZYL on 2019/3/17.
 */
@Service
@Transactional
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminDao adminDao;
    @Override
    public List<Admin> selectAllAdmin() {
        return adminDao.selectAllAdmin();
    }

    @Override
    public int addAdmin(String adminId, String adminName, String adminPassword) {
        return adminDao.addAdmin(adminId,adminName,adminPassword);
    }

    @Override
    public Admin selectAdminByNameAndPassword(String adminName, String adminPassword) {
        return adminDao.selectAdminByNameAndPassword(adminName,adminPassword);
    }

}
