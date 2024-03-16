package com.ssafypjt.bboard.model.repository;

import com.ssafypjt.bboard.model.domain.solvedacAPI.ProblemAlgorithm;
import com.ssafypjt.bboard.model.entity.Problem;
import com.ssafypjt.bboard.model.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProblemRepository {

    @Select("SELECT id, user_id as userId, problem_num as problemNum, tier, title FROM problem") // * 수정
    public List<Problem> selectAllProblems();

    @Select("SELECT id, user_id as userId, problem_num as problemNum, tier, title FROM problem WHERE id = #{id}") // * 수정
    public Problem selectProblem(int id);

    @Select("SELECT id, user_id as userId, problem_num as problemNum, tier, title FROM problem WHERE problem_num = #{problemNum}") // * 수정
    public Problem selectProblemByNum(int problemNum);

    @Select({
            "<script>",
            "SELECT id, user_id as userId, problem_num as problemNum, tier, title FROM problem",
            "WHERE user_id IN",
            "<foreach item='user' collection='users' open='(' separator=',' close=')'>",
            "#{user.userId}",
            "</foreach>",
            "</script>"
    })
    public List<Problem> selectGroupProblem(@Param("users") List<User> user);

    @Select("SELECT id, user_id as userId, problem_num as problemNum, tier, title FROM problem WHERE user_id = #{userId}")
    public List<Problem> selectUserProblem(int userId);

    @Delete("DELETE from problem")
    public int deleteAll();

    @Insert({
            "<script>",
            "INSERT INTO problem (user_id, tier, problem_num, title) VALUES ",
            "<foreach item='item' collection='list' separator=','>",
            "(#{item.problem.userId}, #{item.problem.tier}, #{item.problem.problemNum}, #{item.problem.title})",
            "</foreach>",
            "</script>"
    })
    public int insertProblems(List<ProblemAlgorithm> list);

}
