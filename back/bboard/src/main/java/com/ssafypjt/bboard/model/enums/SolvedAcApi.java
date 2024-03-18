package com.ssafypjt.bboard.model.enums;

import com.ssafypjt.bboard.model.vo.ProblemAlgorithmVo;
import com.ssafypjt.bboard.model.entity.Problem;
import com.ssafypjt.bboard.model.entity.User;
import com.ssafypjt.bboard.model.entity.UserTier;

public enum SolvedAcApi {
    USER("user", "/api/v3/user/show", new String[] {"handle="}, User.class),
    TIER("tier", "/api/v3/user/problem_stats", new String[] {"handle="},UserTier.class),
    USERTIERPROBLEM("tierProblem", "/api/v3/search/problem",
            new String[] {"query=@","&sort=level&direction=desc&page="}, Problem.class),
    PROBLEMANDALGO("problemAndAlgo", "/api/v3/user/top_100",
            new String[] {"handle="} , ProblemAlgorithmVo.class);

    private final String name;
    private final String path;
    private final String[] query;
    private final Class<?> rtnClass;

    SolvedAcApi(String name, String path, String[] query, Class<?> rtnClass) {
        this.name = name;
        this.path = path;
        this.query = query;
        this.rtnClass = rtnClass;
    }


    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String[] getQuery() {
        return query;
    }

    public String getQuery(String param){
        return query[0] + param;
    }

    public String getQuery(String param1, int param2){
        return query[0] + param1 + query[1] + param2;
    }

    public Class<?> getRtnClass() {
        return rtnClass;
    }

}
