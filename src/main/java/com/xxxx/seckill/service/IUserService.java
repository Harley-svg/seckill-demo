package com.xxxx.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.vo.LoginVo;
import com.xxxx.seckill.vo.ResponseBean;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fenglinhai
 * @since 2022-10-02
 */

public interface IUserService extends IService<User> {


    ResponseBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    User getUserByCookie(String userTicket,HttpServletRequest request,HttpServletResponse response);
    /**
     * 更新密码
     */
    ResponseBean updatePassword(String userTicket,String password,HttpServletRequest request,HttpServletResponse response);
}
