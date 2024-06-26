package com.ssafypjt.bboard.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserTier {
    private int id;
    @Setter
    private int userId;
    @JsonProperty("level")
    private int tier;
    @JsonProperty("solved")
    private int problemCount;
    @Setter
    private int pageNo = 1;
    @Setter
    private int pageIdx;

}

