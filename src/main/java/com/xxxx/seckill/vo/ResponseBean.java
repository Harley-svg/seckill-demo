package com.xxxx.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Harley
 * @create 2022-10-02 17:41
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBean {

    private long code;
    private String message;
    private Object obj;

    public static ResponseBean success(){
        return new ResponseBean(ResponseBeanEnum.SUCCESS.getCode(),ResponseBeanEnum.SUCCESS.getMessage(),null);
    }
    public static ResponseBean success(Object obj){
        return new ResponseBean(ResponseBeanEnum.SUCCESS.getCode(), ResponseBeanEnum.SUCCESS.getMessage(),obj);
    }
    public static ResponseBean fail(ResponseBeanEnum responseBeanEnum){
        return new ResponseBean(responseBeanEnum.getCode(), responseBeanEnum.getMessage(),null);
    }
    public static ResponseBean fail(ResponseBeanEnum responseBeanEnum,Object obj){
        return new ResponseBean(responseBeanEnum.getCode(), responseBeanEnum.getMessage(),obj);
    }
}
