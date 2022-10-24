package com.xxxx.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.seckill.exception.GlobalException;
import com.xxxx.seckill.mapper.UserMapper;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.IUserService;
import com.xxxx.seckill.utils.CookieUtil;
import com.xxxx.seckill.utils.MD5Util;
import com.xxxx.seckill.utils.UUIDUtil;
import com.xxxx.seckill.utils.ValidatorUtil;
import com.xxxx.seckill.vo.LoginVo;
import com.xxxx.seckill.vo.ResponseBean;
import com.xxxx.seckill.vo.ResponseBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.pattern.PathPattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fenglinhai
 * @since 2022-10-02
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public ResponseBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {

        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
//        //参数校验
//        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
//            return ResponseBean.fail(ResponseBeanEnum.LOGIN_ERROR);
//        }
//        if (!ValidatorUtil.isMobile(mobile)) {
//            return ResponseBean.fail(ResponseBeanEnum.MOBILE_ERROR);
//        }
        User user = userMapper.selectById(mobile);
        if (null==user) {
            return ResponseBean.fail(ResponseBeanEnum.LOGIN_ERROR);
        }
        if (!MD5Util.formPassToDBPass(password, user.getSalt()).equals(user.getPassword())) {
            return ResponseBean.fail(ResponseBeanEnum.LOGIN_ERROR);
        }

        //生成cookie
        String ticket= UUIDUtil.uuid();
        //将用户信息放到session里面
//        request.getSession().setAttribute(ticket,user);
        //将用户信息存入redis中
        redisTemplate.opsForValue().set("user:"+ticket,user);


        CookieUtil.setCookie(request,response,"userTicket",ticket);

        return ResponseBean.success(ticket);

    }

    @Override
    public User getUserByCookie(String userTicket,HttpServletRequest request,HttpServletResponse response) {
        if(StringUtils.isEmpty(userTicket)){
            return null;
        }

        User user = (User) redisTemplate.opsForValue().get("user:" + userTicket);
        if(user!=null){
            CookieUtil.setCookie(request,response,"userTicket",userTicket);
        }
        return user;
    }

    @Override
    public ResponseBean updatePassword(String userTicket,String password,
                                       HttpServletRequest request,HttpServletResponse response) {
        User user = getUserByCookie(userTicket, request, response);
        if(user==null){
            throw new GlobalException(ResponseBeanEnum.MOBILE_NOT_EXIT);
        }
        user.setPassword(MD5Util.inputPassToDBPass(password,user.getSalt()));
        int result = userMapper.updateById(user);
        if(1==result){
            redisTemplate.delete("user:"+userTicket);
            return ResponseBean.success();
        }


        return ResponseBean.fail(ResponseBeanEnum.PASSWORD_UPDATE_FAIL);
    }


}
