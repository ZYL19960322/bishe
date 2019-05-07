package com.bishe.dao;

import com.bishe.pojo.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ZYL on 2019/3/17.
 */
@Mapper
public interface AdminDao {
    List<Admin> selectAllAdmin();
    int addAdmin(@Param("adminId")String adminId,@Param("adminName")String adminName,@Param("adminPassword")String adminPassword);
    Admin selectAdminByNameAndPassword(@Param("adminName")String adminName,@Param("adminPassword")String adminPassword);
}
