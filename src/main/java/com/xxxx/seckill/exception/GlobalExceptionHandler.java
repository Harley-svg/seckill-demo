package com.xxxx.seckill.exception;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.xxxx.seckill.vo.ResponseBean;
import com.xxxx.seckill.vo.ResponseBeanEnum;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



/**
 * @author Harley
 * @create 2022-10-02 23:53
 */
@RestControllerAdvice//处理全局异常
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseBean ExceptionHandler(Exception e){
        if(e instanceof GlobalException){
            GlobalException ex=(GlobalException) e;
            return ResponseBean.fail(ex.getResponseBeanEnum());
        }else if(e instanceof BindException) {
            BindException ex=(BindException) e;
            ResponseBean responseBean=ResponseBean.fail(ResponseBeanEnum.BIND_ERROR);
            responseBean.setMessage("参数校验异常："+ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
            return responseBean;
        }
        return ResponseBean.fail(ResponseBeanEnum.ERROR);

    }
}
