package edu.dgut.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //特殊的请求放行
        String requestURI = request.getRequestURI();
        if (requestURI.equalsIgnoreCase("/minioProject/login") || requestURI.equalsIgnoreCase("/minioProject/account/login")
            || requestURI.equalsIgnoreCase("/account/loginOut")){
            return true;
        }

        //1.判断用户是不是登录？
        String user = (String) request.getSession().getAttribute("user");
        if (user!=null){
            //用户已经登录
            request.getSession().setMaxInactiveInterval(60*15);
            return true;
        }else {
            //3、如果没登录
            request.getRequestDispatcher("/login").forward(request,response);
            return false;
        }

    }
}
