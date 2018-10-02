package com.officemaneger.configs;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Component
public class HandlerInterceptorAdapter implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Cookie[] cookies = request.getCookies();
        Boolean gotLang = false;
        if (cookies != null) {
            for (Cookie ck : cookies) {
                if(ck.getName().toString().equals("lang")){
                    gotLang = true;
                }
            }
        }

        if(!gotLang){

            //set bg as default lang
            response.addCookie(new Cookie("lang", "bg"));
            response.setHeader("Content-Language","bg");

/*
            //get user lang
            Locale locale = request.getLocale();
            String language = locale.getLanguage();

            if(language.equalsIgnoreCase("en")){
                response.addCookie(new Cookie("lang", "en"));
                response.setHeader("Content-Language","en");
            } else if(language.equalsIgnoreCase("bg")){
                response.addCookie(new Cookie("lang", "bg"));
                response.setHeader("Content-Language","bg");
            }
            */
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
