package com.ssafypjt.bboard.model.repository;

import com.ssafypjt.bboard.model.entity.Problem;
import com.ssafypjt.bboard.model.entity.RecomProblem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RecomProblemRepository {

    @Select("SELECT id, user_id as userId, group_id as groupId, problem_num as problemNum, tier, title FROM recom_problem")
    public List<RecomProblem> selectAllRecomProblems();
    @Select("SELECT id, user_id as userId, group_id as groupId, problem_num as problemNum, tier, title FROM recom_problem WHERE problem_num = #{problemNum} AND group_id = #{groupId}")
    public RecomProblem selectRecomProblem(@Param("problemNum") int problemNum, @Param("groupId") int groupId);

    @Select("SELECT id, user_id as userId, group_id as groupId, problem_num as problemNum, tier, title FROM recom_problem WHERE group_id = #{groupId}")
    public List<RecomProblem> selectGroupRecomProblems(@Param("groupId") int groupId);

    @Insert("INSERT INTO recom_problem (user_id, group_id, problem_num, tier, title) VALUES (#{p.userId}, #{groupId}, #{p.problemNum}, #{p.tier}, #{p.title})")
    public int insertRecomProblem(@Param("p") Problem problem, @Param("groupId") int groupId);

    @Delete("DELETE FROM recom_problem WHERE group_id = #{groupId}")
    public int deleteGroupRecomProblem(@Param("groupId") int groupId);

    @Delete("DELETE FROM recom_problem ORDER BY id LIMIT 1;")
    public int deleteFirstRecomProblem();

}
