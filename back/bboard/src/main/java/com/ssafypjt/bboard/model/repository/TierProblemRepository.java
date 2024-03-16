package com.ssafypjt.bboard.model.repository;

import com.ssafypjt.bboard.model.entity.UserTier;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TierProblemRepository {

    @Select("SELECT id, user_id as userId, tier, problem_count as problemCount FROM tier_problem")
    public List<UserTier> selectAllUserTiers();

    @Select("SELECT id, user_id as userId, tier, problem_count as problemCount FROM tier_problem WHERE user_id = #{userId}")
    public List<UserTier> selectUserTiers(int userId);

    @Insert({
            "<script>",
            "INSERT INTO tier_problem (user_id, tier, problem_count, page_no, page_idx) VALUES ",
            "<foreach item='item' collection='list' separator=','>",
            "(#{item.userId}, #{item.tier}, #{item.problemCount}, #{item.pageNo}, #{item.pageIdx})",
            "</foreach>",
            "</script>"
    })
    public int insertUserTiers(List<UserTier> userTierList);


    @Delete("DELETE from tier_problem")
    public int deleteAll();
}
