package wtssg.xdly.user.dao;

import wtssg.xdly.user.entity.Permission;

public interface PermissionMapper {
    int deleteByPrimaryKey(Long permissionid);

    int insert(Permission record);

    int insertSelective(Permission record);

    Permission selectByPrimaryKey(Long permissionid);

    int updateByPrimaryKeySelective(Permission record);

    int updateByPrimaryKey(Permission record);
}