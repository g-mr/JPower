package com.qidiangk.smart.aster.tripartite.dianxin;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MessageAsrDTO(
        Integer code,
        Data data,
        Long elpsTime,
        Integer resStatus,
        String message,
        String sid
) {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Data(
            Integer sn,
            List<Result> results
    ) {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Result(
            Long endTime,
            Long beginTime,
            String text
    ) {}
}