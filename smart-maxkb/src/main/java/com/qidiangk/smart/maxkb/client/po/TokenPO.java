package com.qidiangk.smart.maxkb.client.po;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author mr.g
 */
@Data
@Builder
public class TokenPO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7289298788453569708L;


    /**
     * TOKEN
     */
    private String token;

}
