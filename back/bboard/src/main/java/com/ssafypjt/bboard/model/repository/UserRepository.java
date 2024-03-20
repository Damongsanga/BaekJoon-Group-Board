package com.ssafypjt.bboard.model.repository;

import com.ssafypjt.bboard.model.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserRepository {

    @Select("SELECT user_id as userId, user_name as userName, tier, solved_rank as solvedRank, img_url as imgUrl\n" +
            " FROM users")
    public List<User> selectAllUsers();

    @Select("SELECT user_id as userId, user_name as userName, tier, solved_rank as solvedRank, img_url as imgUrl\n" +
            " FROM users WHERE user_id = #{userId}")
    public User selectUser(@Param("userId") int userId);

    @Insert("INSERT INTO users (user_name, tier, solved_rank, img_url) VALUES (#{userName}, #{tier}, #{solvedRank}, #{imgUrl})")
    public int insertUser(User user);


    @Delete("DELETE FROM users WHERE user_id = #{userId}")
    public int deleteUser(int userId);

    @Update("UPDATE users SET user_name = #{userName}, tier = #{tier}, solved_rank = #{solvedRank}, img_url = #{imgUrl} WHERE user_name = #{userName}")
    public int updateUser(User user);

    @Select("SELECT user_id as userId, user_name as userName, tier, solved_rank as solvedRank, img_url as imgUrl\n\n" +
            " FROM users WHERE user_Name = #{userName}")
    public User selectUserByName(@Param("userName") String userName);

}
