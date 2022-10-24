package com.xxxx.seckill.exception;

import com.xxxx.seckill.vo.ResponseBeanEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Harley
 * @create 2022-10-02 23:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalException extends RuntimeException{
    private ResponseBeanEnum responseBeanEnum;

}
