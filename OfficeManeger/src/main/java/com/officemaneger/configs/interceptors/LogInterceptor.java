package com.officemaneger.configs.interceptors;

import com.officemaneger.areas.computers.services.ComputerService;
import com.officemaneger.areas.log.models.dtoModels.LogDto;
import com.officemaneger.areas.log.services.LogService;
import com.officemaneger.areas.user.entities.User;
import com.officemaneger.areas.user.repositories.UserRepository;
import com.officemaneger.areas.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LogInterceptor implements HandlerInterceptor {

    private LogService logService;

    private ComputerService computerService;

    private UserRepository userRepository;

    public LogInterceptor() {
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        httpServletRequest.setAttribute("preHandleTime", System.currentTimeMillis());

        //next(except last row) is only for computers filter
        String ipAddress = httpServletRequest.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = httpServletRequest.getRemoteAddr();
        }

        Object object = null;
        if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null) {
            object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }

        String user = "анонимен";
        User activeUser = null;
        if (object != null && !object.equals("anonymousUser")) {
            activeUser = (User) object;
            user = activeUser.getUsername();
        }

        HandlerMethod handlerMethod = (HandlerMethod) o;
        String classMethod = handlerMethod.getMethod().getName();

        Long computerId = this.computerService.getComputerIdByIP(ipAddress);
        if (computerId == null && !classMethod.equals("getLoginPage") && !classMethod.equals("addComputerPage") && !classMethod.equals("getUnauthorizedPage")) {
            if (activeUser == null && !classMethod.equals("getLoginPage")) {
                httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/login");
                return false;
            } else if(activeUser != null){
                String username = activeUser.getUsername();
                String roleAsString = this.userRepository.getUserAuthorities(username).get(0).getAuthority();
                if (!roleAsString.equals("ROLE_ADMIN")) {
                    httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/unauthorized");
                    return false;
                } else {
                    if (classMethod.equals("addComputer")) {
                        httpServletRequest.setAttribute("ip", ipAddress);
                        return true;
                    }

                    httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/addComputer");
                    return false;
                }
            } else {
                httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/unauthorized");
                return false;
            }
        }

        //add attribute computerId
        if (computerId != null) {
            httpServletRequest.setAttribute("computerId", computerId);
        }

        if (classMethod.equals("getHomePage")) {
            Long businessUnitId = this.computerService.getBusinessUnitId(ipAddress);
            httpServletRequest.setAttribute("businessUnitId", businessUnitId);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        httpServletRequest.setAttribute("postHandleTime", System.currentTimeMillis());

        Boolean isInvalidCredentials = false;
        if (modelAndView != null && modelAndView.getModel() != null && modelAndView.getModel().get("error") != null &&
                modelAndView.getModel().get("error").equals("error.login.invalidCredentials")) {
            isInvalidCredentials = true;
        }

        httpServletRequest.setAttribute("isInvalidCredentials", isInvalidCredentials);
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, Exception e) throws Exception {
        long currentTime = System.currentTimeMillis();
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        if (httpServletRequest.getAttribute("postHandleTime") == null) {
            return;
        }

        long preHandleTime = (long) httpServletRequest.getAttribute("preHandleTime");
        long postHandleTime = (long) httpServletRequest.getAttribute("postHandleTime");

        long actionExecution = postHandleTime - preHandleTime;
        long overallExecution = currentTime - preHandleTime;

        String ipAddress = httpServletRequest.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = httpServletRequest.getRemoteAddr();
        }

        Object object = null;
        if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null) {
            object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }

        String user = "анонимен";
        if (object != null && !object.equals("anonymousUser")) {
            User activeUser = (User) object;
            user = activeUser.getUsername();
        }

        String[] classNameArr = String.valueOf(handlerMethod.getBean().getClass()).split("\\.");
        String className = classNameArr[classNameArr.length - 1];
        String classMethod = handlerMethod.getMethod().getName();
        boolean isGetPage = false;
        if (classMethod.length() > 4 && classMethod.substring(classMethod.length() - 4).equals("Page")) {
            isGetPage = true;
        }

        Boolean isInvalidCredentials = (Boolean) httpServletRequest.getAttribute("isInvalidCredentials");

        LogDto logDto = new LogDto(user, ipAddress, className, classMethod, actionExecution, overallExecution, isGetPage, isInvalidCredentials);

        this.logService.create(logDto);
    }

    @Autowired
    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    @Autowired
    public void setComputerService(ComputerService computerService) {
        this.computerService = computerService;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}


