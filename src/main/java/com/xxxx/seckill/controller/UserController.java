package com.xxxx.seckill.controller;


import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.vo.ResponseBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fenglinhai
 * @since 2022-10-02
 */
@RestController
@RequestMapping("/user")
public class UserController {
    /**
     * 用户信息，测试
     * @param user
     * @return
     */
    @RequestMapping("/info")
    @ResponseBody
    public ResponseBean info(User user){
        return ResponseBean.success();
    }

}
