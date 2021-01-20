package com.kebo.es.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @Author: kb
 * @Date: 2021-01-20 14:53
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultData {
    private boolean status=true;
    private String code ="200";
    private int dataSize=0;
    private Object data;

}
