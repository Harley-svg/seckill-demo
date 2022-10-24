package com.xxxx.seckill.controller;

import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.IGoodsService;
import com.xxxx.seckill.service.IUserService;
import com.xxxx.seckill.vo.DetailVo;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.ResponseBean;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author Harley
 * @create 2022-10-03 16:21
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    /**
     * 跳转商品列表项
     * windows优化前qps 112.9/sec
     * linux优化前 24.8/sec
     *
     * 缓存优化后windows qps 85.7 数据库为本地时qps
     *
     * @param
     * @param model
     * @param
     * @return
     */
    @RequestMapping(value = "/toList", produces = "text/html;charset=utf-8")
    @ResponseBody
    //user是从容器中获取的
    public String toList(User user, Model model, HttpServletRequest request, HttpServletResponse response) {
        //redis中获取页面，如果不为空，直接返回页面
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsList");
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
//        if(StringUtils.isEmpty((ticket))){
//            return "login";
//        }
//
////        User user = (User)session.getAttribute(ticket);
//
//        User user = userService.getUserByCookie(ticket, request, response);


//        if (null == user) {
//            return "login";
//        }

        model.addAttribute("user", user);
        model.addAttribute("goodsList", goodsService.findGoodsVo());

        //如果为空，手动渲染，存入redis并返回
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale(),
                model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList", context);
        if (!StringUtils.isEmpty(html)) {
            valueOperations.set("goodsList", html, 60, TimeUnit.SECONDS);
        }

        return html;
    }

    /**
     * windows压测
     *
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/toDetail/{goodsId}", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toDetail2(Model model, User user, @PathVariable long goodsId,
                           HttpServletRequest request,HttpServletResponse response) {

        ValueOperations valueOperations = redisTemplate.opsForValue();
        //redis中获取页面，如果不为空，直接范围页面
        String html = (String) valueOperations.get("goodsDetail:" + goodsId);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }



        model.addAttribute("user", user);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date now = new Date();
        //秒杀状态
        int secKillStatus = 0;
        int remainSecond = 0;//秒杀倒计时

        if (now.before(startDate)) {//未开始
            secKillStatus = 0;
            remainSecond = (int) (startDate.getTime() - now.getTime()) / 1000;
        } else if (now.after(endDate)) {//结束
            secKillStatus = 2;
            remainSecond = -1;
        } else {
            secKillStatus = 1;//进行中
            remainSecond = 0;
        }
        model.addAttribute("goods", goodsVo);
        model.addAttribute("secKillStatus", secKillStatus);
        model.addAttribute("remainSeconds", remainSecond);
//        return "goodsDetail";
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale(),
                model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail", context);
        if (!StringUtils.isEmpty(html)) {
            valueOperations.set("goodsDetail:"+goodsId, html, 60, TimeUnit.SECONDS);
        }

        return html;


    }


    /**
     * windows压测
     *
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public ResponseBean toDetail(Model model, User user, @PathVariable long goodsId) {

        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date now = new Date();
        //秒杀状态
        int secKillStatus = 0;
        int remainSecond = 0;//秒杀倒计时

        if (now.before(startDate)) {//未开始
            secKillStatus = 0;
            remainSecond = (int) (startDate.getTime() - now.getTime()) / 1000;
        } else if (now.after(endDate)) {//结束
            secKillStatus = 2;
            remainSecond = -1;
        } else {
            secKillStatus = 1;//进行中
            remainSecond = 0;
        }
        DetailVo detailVo = new DetailVo();
        detailVo.setGoodsVo(goodsVo);
        detailVo.setUser(user);
        detailVo.setRemainSeconds(remainSecond);
        detailVo.setSecKillStatus(secKillStatus);
//        return "goodsDetail";
        return ResponseBean.success(detailVo);


    }



}
