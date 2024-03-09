package com.arts.work.common.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.arts.work.common.pojo.entity.UserInfo;
import com.arts.work.common.utils.ErrorCode;
import com.arts.work.common.utils.JwtUtils;
import com.arts.work.common.utils.RestResponse;
import com.arts.work.common.utils.ResultUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

//自定义拦截器
public class TokenInterceptor implements HandlerInterceptor {


    /**
     * 发起请求前调用
     * 该方法返回false的话，将不会往下执行
     */
    //拦截每个请求
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        response.setCharacterEncoding("utf-8");
        String token = request.getHeader("token");
        System.out.println("preHandle....." + token);
        RestResponse responseData;
        //token存在
        if (null != token) {
            String verifyUserId = JwtUtils.verifyToken(token, String.class);//解码
            String userId = request.getHeader("userId");//获取userId
            if (null != userId && null != verifyUserId) {
                //一致，返回true 正常执行
                if (userId.equals(verifyUserId)) {
                    return true;
                } else {
                    responseData = ResultUtils.error(ErrorCode.NO_LOGIN, "token过期，请重新登录！");
                    responseMessage(response, response.getWriter(), responseData);
                    return false;
                }
            } else {
                responseData = ResultUtils.error(ErrorCode.PARAMS_ERROR, "token无效！");
                responseMessage(response, response.getWriter(), responseData);
                return false;
            }
        } else {
            responseData = ResultUtils.error(ErrorCode.PARAMS_ERROR, "token不存在！");
            responseMessage(response, response.getWriter(), responseData);
            return false;
        }
    }

    /**
     * preHandle()返回true后，afterCompletion()才会执行
     */
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception arg3) throws Exception {
    }

    /**
     * 该方法在调用controller方法后，DispatcherServlet渲染视图之前被执行
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView model) throws Exception {
    }

    //请求不通过，返回错误信息给客户端
    private void responseMessage(HttpServletResponse response, PrintWriter out, RestResponse responseData) {
        response.setContentType("application/json; charset=utf-8");
        String json = JSONObject.toJSONString(responseData);
        out.print(json);
        out.flush();
        out.close();
    }
}
