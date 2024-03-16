package com.ssafypjt.bboard.model.domain.solvedacAPI;

import com.fasterxml.jackson.databind.JsonNode;
import com.ssafypjt.bboard.model.entity.UserTier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class FetchDomain {
    private final WebClient webClient;

    public FetchDomain(WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<JsonNode> fetchOneQueryData(String pathQuery, String query) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(pathQuery)
                        .query(query)
                        .build()).retrieve()
                .bodyToFlux(JsonNode.class);
    }

    public Mono<JsonNode> fetchOneQueryDataMono(String pathQuery, String query) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(pathQuery)
                        .query(query)
                        .build()).retrieve()
                .bodyToMono(JsonNode.class);
    }

    public Flux<UserTier> fetchOneQueryDataUserTier(String pathQuery, String query) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(pathQuery)
                        .query(query)
                        .build()).retrieve()
                .bodyToFlux(UserTier.class);
    }

}
