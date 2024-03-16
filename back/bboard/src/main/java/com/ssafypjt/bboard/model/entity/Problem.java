package com.ssafypjt.bboard.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Problem {
    private int id = 0;
    private int userId = 0;
    @JsonProperty("problemId")
    private int problemNum;
    @JsonProperty("level")
    private int tier;
    @JsonProperty("titleKo")
    private String title;
}
