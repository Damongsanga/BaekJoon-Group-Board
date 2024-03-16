package com.ssafypjt.bboard.model.repository;

import com.ssafypjt.bboard.model.entity.Group;
import org.apache.ibatis.annotations.*;

@Mapper
public interface GroupRepository {

    // group without adminValidation
    @Select("SELECT id, group_name as groupName, password FROM `group` WHERE id = #{id}")
    public Group selectGroup(int id);

    @Select("SELECT id, group_name as groupName, password FROM `group` WHERE group_name = #{groupName}")
    public Group selectGroupByName(String groupName);

    // getting adminValidation
    @Select("SELECT id, group_name as groupName, password FROM `group` WHERE id = #{id} AND password = #{password}") // 비번 권한 확인용
    public Group selectGroupByPassword(@Param("id") int id, @Param("password") String password);

    // group with adminValidation
    @Insert("INSERT INTO `group` (group_name, password) VALUES (#{groupName}, #{password})")
    public int insertGroup(Group group);

    @Delete("DELETE FROM `group` where id = #{id}")
    public int deleteGroup(int id);

}
