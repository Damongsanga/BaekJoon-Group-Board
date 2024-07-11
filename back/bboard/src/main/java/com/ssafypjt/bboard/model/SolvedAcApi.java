package com.ssafypjt.bboard.model;

import com.ssafypjt.bboard.model.vo.ProblemAlgorithmVo;
import com.ssafypjt.bboard.model.entity.Problem;
import com.ssafypjt.bboard.model.entity.User;
import com.ssafypjt.bboard.model.entity.UserTier;

public enum SolvedAcApi {
    USER("/api/v3/user/show", new String[] {"handle="}, User.class),
    TIER("/api/v3/user/problem_stats", new String[] {"handle="},UserTier.class),
    USERTIERPROBLEM("/api/v3/search/problem",
            new String[] {"query=@","&sort=level&direction=desc&page="}, Problem.class),
    PROBLEMANDALGO("/api/v3/user/top_100",
            new String[] {"handle="} , ProblemAlgorithmVo.class);

    private final String path;
    private final String[] query;
    private final Class<?> rtnClass;

    SolvedAcApi(String path, String[] query, Class<?> rtnClass) {
        this.path = path;
        this.query = query;
        this.rtnClass = rtnClass;
    }


    public String getPath() {
        return path;
    }

    public String getQuery(String param){
        return query[0] + param;
    }

    public String getQuery(String param1, int param2){
        return query[0] + param1 + query[1] + param2;
    }

}
