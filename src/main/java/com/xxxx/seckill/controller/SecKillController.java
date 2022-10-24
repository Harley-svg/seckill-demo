package com.xxxx.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.xxxx.seckill.pojo.Order;
import com.xxxx.seckill.pojo.SeckillGoods;
import com.xxxx.seckill.pojo.SeckillOrder;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.IGoodsService;
import com.xxxx.seckill.service.IOrderService;
import com.xxxx.seckill.service.ISeckillGoodsService;
import com.xxxx.seckill.service.ISeckillOrderService;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.ResponseBean;
import com.xxxx.seckill.vo.ResponseBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Harley
 * @create 2022-10-05 21:37
 */
@Controller
@RequestMapping("/seckill")
public class SecKillController {
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/doSeckill2")
    public String doSecKill2(Model model, User user, Long goodsId){
        if(user==null){
            return "login";
        }
        model.addAttribute("user",user);
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);

        //判断库存
        if(goods.getStockCount()<1){
            model.addAttribute("errmsg", ResponseBeanEnum.EMPTY_STOCK.getMessage());
             return "secKillFail";
        }
        //判断是否重复抢购
//        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>()
//                .eq("user_id", user.getId())
//                .eq("goods_Id", goodsId));

        Order seckillOrder = (Order) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if(seckillOrder!=null){
            model.addAttribute("errmsg",ResponseBeanEnum.REPEATE_ERROR.getMessage());
            return "secKillFail";
        }

        Order order=orderService.seckill(user,goods);

        model.addAttribute("order",order);
        model.addAttribute("goods",goods);
        System.out.println("fenglinhai");
        return "orderDetail";

    }




    @RequestMapping(value = "/doSeckill",method = RequestMethod.POST)
    @ResponseBody
    public ResponseBean doSecKill(Model model, User user, Long goodsId){
        if(user==null){
            return ResponseBean.fail(ResponseBeanEnum.SESSION_ERROR);
        }
        model.addAttribute("user",user);
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);

        //判断库存
        if(goods.getStockCount()<1){
            model.addAttribute("errmsg", ResponseBeanEnum.EMPTY_STOCK.getMessage());
            return ResponseBean.fail(ResponseBeanEnum.EMPTY_STOCK);
        }
        //判断是否重复抢购
        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>()
                .eq("user_id", user.getId())
                .eq("goods_Id", goodsId));
        if(seckillOrder!=null){
            model.addAttribute("errmsg",ResponseBeanEnum.REPEATE_ERROR.getMessage());
            return ResponseBean.fail(ResponseBeanEnum.REPEATE_ERROR);
        }

        Order order=orderService.seckill(user,goods);

        model.addAttribute("order",order);
        model.addAttribute("goods",goods);
        System.out.println("fenglinhai");
        return ResponseBean.success(order);

    }
}
