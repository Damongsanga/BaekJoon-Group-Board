package com.ssafypjt.bboard.model.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private int userId;
    @JsonProperty("handle")
    private String userName;
    private int tier;
    @JsonProperty("rank")
    private int solvedRank;
    @JsonProperty("profileImageUrl")
    private String imgUrl;
}

