package com.qidiangk.smart.aster.tripartite.dianxin;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MessageTtsDTO(
        Integer status,
        String statusMsg,
        String sid,
        Result result
) {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Result(
            String audio,
            Integer audioLen,
            Boolean isEnd
    ) {}

}