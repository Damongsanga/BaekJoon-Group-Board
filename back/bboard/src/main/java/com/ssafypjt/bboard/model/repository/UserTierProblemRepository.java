package com.ssafypjt.bboard.model.repository;

import com.ssafypjt.bboard.model.vo.ProblemAlgorithmVo;
import com.ssafypjt.bboard.model.entity.Problem;
import com.ssafypjt.bboard.model.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserTierProblemRepository {

    @Select("SELECT id, user_id as userId, tier, problem_num as problemTitle, title FROM user_tier_problem WHERE tier = #{tier}")
    public List<Problem> selectTierProblems(@Param("userId") int userId);

    @Select({
            "<script>",
            "SELECT id, user_id as userId, problem_num as problemNum, tier, title FROM user_tier_problem",
            "WHERE user_id IN",
            "<foreach item='user' collection='users' open='(' separator=',' close=')'>",
            "#{user.userId}",
            "</foreach>",
            "</script>"
    })
    public List<Problem> selectGroupTierProblem(@Param("users") List<User> user);

    @Insert("INSERT INTO user_tier_problem (user_id, tier, problem_num, title) VALUES (#{userId}, #{tier}, #{problemNum}, #{title})")
    public int insertTierProblem(Problem problem);

    @Insert({
            "<script>",
            "INSERT INTO user_tier_problem (user_id, tier, problem_num, title) VALUES ",
            "<foreach item='item' collection='list' separator=','>",
            "(#{item.problem.userId}, #{item.problem.tier}, #{item.problem.problemNum}, #{item.problem.title})",
            "</foreach>",
            "</script>"
    })
    public int insertTierProblems(List<ProblemAlgorithmVo> list);

    @Delete("DELETE FROM user_tier_problem")
    public int deleteAll();

}
