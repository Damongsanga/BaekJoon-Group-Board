package com.ssafypjt.bboard.model.repository;

import io.swagger.v3.oas.annotations.Hidden;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserGroupRepository {

    @Select("SELECT group_id as groupId FROM user_group WHERE user_id = #{userId}")
    public List<Integer> selectGroupId(int userId);

    @Select("SELECT user_id as userId FROM user_group WHERE group_id = #{groupId}")
    public List<Integer> selectUserId(int GroupId);

    @Insert("INSERT INTO user_group (user_id, group_id) VALUES (#{userId}, #{groupId});") // 그룹-유저 관계 삽입
    public int insertUserGroup(@Param("userId") int userId, @Param("groupId") int groupId);

    @Delete("DELETE FROM user_group WHERE user_id = #{userId} AND group_id = #{groupId}") // 그룹-유저 관계 삭제
    public int removeUserGroup(@Param("userId") int userId, @Param("groupId") int groupId);

    @Delete("DELETE FROM user_group WHERE group_id = #{groupId}") // 그룹-유저 관계 삭제
    public int removeAllUserGroup(@Param("groupId") int groupId);

}
