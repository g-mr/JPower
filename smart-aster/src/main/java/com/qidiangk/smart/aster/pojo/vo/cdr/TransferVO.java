package com.qidiangk.smart.aster.pojo.vo.cdr;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.qidiangk.smart.aster.dbs.entity.cdr.CallTransferInfoDO;

@Data
public class TransferVO extends CallTransferInfoDO {

    @Schema(description = "队列名称")
    private String queueName;

}
