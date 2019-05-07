package com.bishe.service;

import com.bishe.pojo.Admin;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by ZYL on 2019/3/17.
 */
public interface AdminService {
    List<Admin> selectAllAdmin();
    int addAdmin(@Param("adminId")String adminId, @Param("adminName")String adminName, @Param("adminPassword")String adminPassword);
    Admin selectAdminByNameAndPassword(@Param("adminName")String adminName,@Param("adminPassword")String adminPassword);
}
