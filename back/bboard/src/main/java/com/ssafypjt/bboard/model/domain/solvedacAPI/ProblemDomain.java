package com.ssafypjt.bboard.model.domain.solvedacAPI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafypjt.bboard.model.entity.Problem;
import com.ssafypjt.bboard.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class ProblemDomain {

    private ObjectMapper mapper;

    @Autowired
    public ProblemDomain(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    // 코드 재활용을 위해 코드 분기
    //합쳐서 list 만드는 과정임 ->
    public void makeProblemAndAlgoDomainObject(List<ProblemAlgorithm> proAndAlgoList, JsonNode aNode, User user) {
        JsonNode arrayNode = aNode.path("items");
        if(!arrayNode.isArray()){
            return;
        }
        proAndAlgoList.addAll(makeProblemAndAlgoDomainList(arrayNode, user));
    }

    public List<ProblemAlgorithm> makeProblemAndAlgoDomainObjectMono(JsonNode aNode, User user) {
        JsonNode arrayNode = aNode.path("items");
        if(!arrayNode.isArray()){
            return null;
        }
        return makeProblemAndAlgoDomainList(arrayNode, user);
    }

    public List<ProblemAlgorithm> makeProblemAndAlgoDomainList(JsonNode arrayNode, User user){
        List<ProblemAlgorithm> tmpList = new ArrayList<>();
        for(JsonNode nodeItem: arrayNode) {
            Problem problem = makeProblemObject(nodeItem, user);
            com.ssafypjt.bboard.model.entity.ProblemAlgorithm problemAlgorithm = makeProblemAlgorithmObject(nodeItem);
            ProblemAlgorithm problemAndAlgoObjectDomain = new ProblemAlgorithm(problem, problemAlgorithm);
            tmpList.add(problemAndAlgoObjectDomain);
        }
        return tmpList;
    }


    //각각 problem & algorithm
    public Problem makeProblemObject(JsonNode nodeItem, User user) {
        Problem problem = null;
        try {
            problem = mapper.treeToValue(nodeItem, Problem.class);
            problem.setUserId(user.getUserId());
        } catch (Exception e ) {
            log.error("error message : {}", e.getMessage());
        }
        return problem;
    }

    public com.ssafypjt.bboard.model.entity.ProblemAlgorithm makeProblemAlgorithmObject(JsonNode nodeItem){
        StringBuilder algorithms = new StringBuilder();
        JsonNode tagsNode = nodeItem.path("tags");
        for (JsonNode tag : tagsNode) {
            if (algorithms.length() > 0) {
                algorithms.append(" ");
            }
            algorithms.append(tag.path("key").asText());
        }

        com.ssafypjt.bboard.model.entity.ProblemAlgorithm problemAlgorithm = new com.ssafypjt.bboard.model.entity.ProblemAlgorithm();
        problemAlgorithm.setProblemNum(nodeItem.path("problemId").asInt());
        problemAlgorithm.setAlgorithm(algorithms.toString());

        return problemAlgorithm;
    }


}

