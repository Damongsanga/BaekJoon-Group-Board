package com.ssafypjt.bboard.model.domain.solvedacAPI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafypjt.bboard.model.entity.Problem;
import com.ssafypjt.bboard.model.entity.ProblemAlgorithm;
import com.ssafypjt.bboard.model.entity.User;
import com.ssafypjt.bboard.model.vo.ProblemAlgorithmVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProblemDomain {

    private final ObjectMapper mapper;

    // 코드 재활용을 위해 코드 분기
    //합쳐서 list 만드는 과정임 ->
    public void makeProblemAndAlgoDomainObject(List<ProblemAlgorithmVo> proAndAlgoList, JsonNode aNode, User user) {
        JsonNode arrayNode = aNode.path("items");
        if(!arrayNode.isArray()) return;

        proAndAlgoList.addAll(makeProblemAndAlgoDomainList(arrayNode, user));
    }

    public List<ProblemAlgorithmVo> makeProblemAndAlgoDomainObjectMono(JsonNode aNode, User user) {
        JsonNode arrayNode = aNode.path("items");
        if(!arrayNode.isArray()){
            return null;
        }
        return makeProblemAndAlgoDomainList(arrayNode, user);
    }

    public List<ProblemAlgorithmVo> makeProblemAndAlgoDomainList(JsonNode arrayNode, User user){
        List<ProblemAlgorithmVo> tmpList = new ArrayList<>();
        for(JsonNode nodeItem: arrayNode) {
            Problem problem = makeProblemObject(nodeItem, user);
            ProblemAlgorithm problemAlgorithm = makeProblemAlgorithmObject(nodeItem);
            ProblemAlgorithmVo problemAndAlgoObjectDomain = new ProblemAlgorithmVo(problem, problemAlgorithm);
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

    public ProblemAlgorithm makeProblemAlgorithmObject(JsonNode nodeItem){
        StringBuilder algorithms = new StringBuilder();
        JsonNode tagsNode = nodeItem.path("tags");
        for (JsonNode tag : tagsNode) {
            if (!algorithms.isEmpty()) {
                algorithms.append(" ");
            }
            algorithms.append(tag.path("key").asText());
        }

        ProblemAlgorithm problemAlgorithm = new ProblemAlgorithm();
        problemAlgorithm.setProblemNum(nodeItem.path("problemId").asInt());
        problemAlgorithm.setAlgorithm(algorithms.toString());

        return problemAlgorithm;
    }


}

