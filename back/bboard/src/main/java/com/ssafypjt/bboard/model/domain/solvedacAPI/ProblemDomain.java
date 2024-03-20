package com.ssafypjt.bboard.model.domain.solvedacAPI;

import com.fasterxml.jackson.core.JsonProcessingException;
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
    // 합쳐서 list 만드는 과정
    public void makeProblemAndAlgoDomainObject(List<ProblemAlgorithmVo> proAndAlgoList, JsonNode aNode, User user) {
        JsonNode arrayNode = aNode.path("items");
        if(!arrayNode.isArray()) return;

        proAndAlgoList.addAll(makeProblemAndAlgoDomainList(arrayNode, user));
    }

    public List<ProblemAlgorithmVo> makeProblemAndAlgoDomainObjectMono(JsonNode aNode, User user) {
        JsonNode arrayNode = aNode.path("items");
        if(!arrayNode.isArray()) return null;

        return makeProblemAndAlgoDomainList(arrayNode, user);
    }

    public List<ProblemAlgorithmVo> makeProblemAndAlgoDomainList(JsonNode arrayNode, User user){
        List<ProblemAlgorithmVo> problemAlgorithmVoList = new ArrayList<>();

        // JsonNode를 problemAlgorithmVo 리스트로 변환
        for(JsonNode nodeItem: arrayNode) {
            Problem problem = convertToProblem(nodeItem, user);
            ProblemAlgorithm problemAlgorithm = convertToProblemAlgorithm(nodeItem);

            ProblemAlgorithmVo problemAlgorithmVo = new ProblemAlgorithmVo(problem, problemAlgorithm);
            problemAlgorithmVoList.add(problemAlgorithmVo);
        }

        return problemAlgorithmVoList;
    }


    private Problem convertToProblem(JsonNode nodeItem, User user) {
        Problem problem = null;
        try {
            problem = mapper.treeToValue(nodeItem, Problem.class);
            problem.setUserId(user.getUserId());
        } catch (IllegalArgumentException | JsonProcessingException e) {
            log.error("error message : {}", e.getMessage());
        }
        return problem;
    }

    private ProblemAlgorithm convertToProblemAlgorithm(JsonNode nodeItem){
        StringBuilder algorithms = new StringBuilder();
        JsonNode tagsNode = nodeItem.path("tags");

        // 알고리즘을 띄어쓰기 기준으로 concat
        for (JsonNode tag : tagsNode) {
            if (!algorithms.isEmpty()) {
                algorithms.append(" ");
            }
            algorithms.append(tag.path("key").asText());
        }

        return ProblemAlgorithm.builder()
                .problemNum(nodeItem.path("problemId").asInt())
                .algorithm(algorithms.toString())
                .build();
    }


}

