package com.xxxx.seckill.controller;


import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.IOrderService;
import com.xxxx.seckill.vo.OrderDetailVo;
import com.xxxx.seckill.vo.ResponseBean;
import com.xxxx.seckill.vo.ResponseBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fenglinhai
 * @since 2022-10-04
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private IOrderService orderService;


    @RequestMapping("/detail")
    @ResponseBody
    public ResponseBean detail(User user,Long orderId){
        if(user==null){
            return ResponseBean.fail(ResponseBeanEnum.SESSION_ERROR);
        }

        OrderDetailVo detail=orderService.detail(orderId);
        return ResponseBean.success(detail);

    }
}
