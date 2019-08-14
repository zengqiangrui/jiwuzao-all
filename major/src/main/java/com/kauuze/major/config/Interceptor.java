package com.kauuze.major.config;

import com.jiwuzao.common.domain.common.MongoUtil;
import com.jiwuzao.common.domain.mongo.entity.userBastic.UserToken;
import com.jiwuzao.common.include.DateTimeUtil;
import com.jiwuzao.common.include.StateModel;
import com.jiwuzao.common.include.StringUtil;
import com.kauuze.major.config.permission.*;
import com.kauuze.major.include.TokenUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;

/**
 * 拦截器用于权限验证
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-02-24 12:30
 */
public class Interceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        //绿色通道放行
        GreenWay greenWay = handlerMethod.getMethod().getAnnotation(GreenWay.class);
        if(greenWay != null){
            return true;
        }
        //不是POST请求方式
        if(!StringUtil.isEq(request.getMethod(),"POST")){
            response403(response,new StateModel("method not POST"));
            return false;
        }

        //需要登录验证
        Authorization authorization = handlerMethod.getMethod().getAnnotation(Authorization.class);
        //需要发言
        Speak speak = handlerMethod.getMethod().getAnnotation(Speak.class);
        //vip用户
        Vip vip = handlerMethod.getMethod().getAnnotation(Vip.class);
        //系统管理员
        Root root = handlerMethod.getMethod().getAnnotation(Root.class);
        //内容管理员
        Cms cms = handlerMethod.getMethod().getAnnotation(Cms.class);
        //商家
        Merchant merchant = handlerMethod.getMethod().getAnnotation(Merchant.class);
        //不需要登录验证
        if(authorization == null && speak == null && vip == null && root == null
                && cms == null && merchant == null
                ){
            return true;
        }

        String accessToken = request.getHeader("Authorization");
        UserToken userToken = TokenUtil.validAccessToken(accessToken);
        //登录验证失败
        if(userToken == null){
            response401(response,new StateModel("Authorization mismatch"));
            return false;
        }
        //用户被封禁
        if(TokenUtil.isBan(userToken)){
            response403(response,new StateModel("封禁直到:",DateTimeUtil.covertDateView(userToken.getUserStateEndTime())));
            return false;
        }
        //用户被禁言
        if (speak != null && TokenUtil.isShutup(userToken)){
            response403(response,new StateModel("禁言直到:", DateTimeUtil.covertDateView(userToken.getUserStateEndTime())));
            return false;
        }
        //用户不是vip
        if (vip != null && !TokenUtil.isVip(userToken)){
            response403(response,new StateModel("您不是会员"));
            return false;
        }
        //不是系统管理员
        if(root != null && !TokenUtil.isRoot(userToken)){
            response401(response,new StateModel("not root"));
            return false;
        }
        //不是内容管理员
        if(cms != null && !TokenUtil.isCms(userToken)){
            response401(response,new StateModel("not cms"));
            return false;
        }
        //不是商家
        if(merchant != null && !TokenUtil.isMerchant(userToken)){
            response403(response,new StateModel("您不是商家"));
            return false;
        }

        UserToken userTokenUp = new UserToken();
        userTokenUp.setUid(userToken.getUid());
        userTokenUp.setLastAccessTime(System.currentTimeMillis());
        MongoUtil.updateNotNon("uid",userTokenUp,UserToken.class);
        request.setAttribute("uid",userToken.getUid());
        request.setAttribute("ip", getIpAddr(request));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control","no-cache");
        response.setHeader("Content-type", "application/json;charset=utf-8");
    }

    /**
     * 需要强制重登
     * @param response
     */
    private void response401(HttpServletResponse response, StateModel stateModel){
        try {
            response.setHeader("Content-type", "application/json;charset=utf-8");
            response.setStatus(401);
            response.getWriter().write(stateModel.toJsonString());
        } catch (IOException e) {
            throw new RuntimeException("Interceptor response401 error");
        }
    }

    /**
     * 不需要强制重登：用中文提示
     * @param response
     */
    private void response403(HttpServletResponse response,StateModel stateModel){
        try {
            response.setHeader("Content-type", "application/json;charset=utf-8");
            response.setStatus(403);
            response.getWriter().write(stateModel.toJsonString());
        } catch (IOException e) {
            throw new RuntimeException("Interceptor response403 error");
        }
    }


    /**
     * @description: 获取客户端IP地址
     */
    private String getIpAddr(HttpServletRequest request) {
           String ip = request.getHeader("x-forwarded-for");
           if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
               ip = request.getHeader("Proxy-Client-IP");
           }
           if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
               ip = request.getHeader("WL-Proxy-Client-IP");
           }
           if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
               ip = request.getRemoteAddr();
               if(ip.equals("127.0.0.1")){
                   //根据网卡取本机配置的IP
                   InetAddress inet=null;
                   try {
                       inet = InetAddress.getLocalHost();
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
                   ip= inet.getHostAddress();
               }
           }
           // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
           if(ip != null && ip.length() > 15){
               if(ip.indexOf(",")>0){
                   ip = ip.substring(0,ip.indexOf(","));
               }
           }
           return ip;
    }
}
