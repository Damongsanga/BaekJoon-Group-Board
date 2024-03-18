package com.ssafypjt.bboard.model.repository;

import com.ssafypjt.bboard.model.vo.ProblemAlgorithmVo;
import com.ssafypjt.bboard.model.entity.ProblemAlgorithm;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProblemAlgorithmRepository {

    @Delete("DELETE FROM problem_algorithm")
    public int deleteAll();

    @Insert({
            "<script>",
            "INSERT IGNORE INTO problem_algorithm (problem_num, algorithm) VALUES ",
            "<foreach item='item' collection='list' separator=','>",
            "(#{item.problemAlgorithm.problemNum}, #{item.problemAlgorithm.algorithm})",
            "</foreach>",
            "</script>"
    })
    public int insertAlgorithms(List<ProblemAlgorithmVo> list);

    @Select("SELECT problem_num as problemNum, algorithm FROM problem_algorithm WHERE problem_num = #{problemNum}")
    public ProblemAlgorithm selectAlgorithm(int problemNum);

    @Select("SELECT problem_num as problemNum, algorithm FROM problem_algorithm ORDER BY problem_num ASC")
    public List<ProblemAlgorithm> selectAllAlgorithm();

}
