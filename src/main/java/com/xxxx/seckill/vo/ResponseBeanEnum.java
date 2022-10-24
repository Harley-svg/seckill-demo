package com.xxxx.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.dao.support.PersistenceExceptionTranslationInterceptor;

/**
 * 公共返回枚举
 * @author Harley
 * @create 2022-10-02 17:41
 */
@Getter
@ToString
@AllArgsConstructor
public enum ResponseBeanEnum {
    //通用
    SUCCESS(200,"SUCCESS"),
    ERROR(500,"服务器异常"),
    //登录模块
    LOGIN_ERROR(500210,"用户名或密码错误"),
    MOBILE_ERROR(500211,"手机格式不对"),
    BIND_ERROR(500212, "参数校验异常"),
    MOBILE_NOT_EXIT(500213, "手机号码不存在"),
    PASSWORD_UPDATE_FAIL(500214, "更新密码失败"),
    SESSION_ERROR(500215,"用户session不存在"),
    //秒杀模块
    EMPTY_STOCK(500500,"库存不足"),
    REPEATE_ERROR(500501,"该商品每人限购一件"),
    //订单模块
    ORDER_NOT_EXIST(500300,"订单信息不存在");

    private final Integer code;
    private final String message;
}
