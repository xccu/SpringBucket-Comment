package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class PerformanceInteceptor implements HandlerInterceptor {
    private ThreadLocal<StopWatch> stopWatch = new ThreadLocal<>();

    /**
     * 方法调用前预处理
     * @param request
     * @param response
     * @param handler
     * @return true：继续后续的处理；false：终止方法处理
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        StopWatch sw = new StopWatch(); //StopWatch类用于记录运行时间
        stopWatch.set(sw);
        sw.start();            //启动用于记录请求的处理时间
        return true;
    }

    /**
     * 方法调用完成（视图呈现前）
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //重新启动StopWatch
        stopWatch.get().stop();
        stopWatch.get().start();
    }

    /**
     * 法调用完成（视图呈现后）
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //获取整个spring mvc是请求并显示
        StopWatch sw = stopWatch.get();
        sw.stop();
        String method = handler.getClass().getSimpleName();
        if (handler instanceof HandlerMethod) {
            String beanType = ((HandlerMethod) handler).getBeanType().getName();
            String methodName = ((HandlerMethod) handler).getMethod().getName();
            method = beanType + "." + methodName;
        }
        log.info("{};{};{};{};{}ms;{}ms;{}ms",
                request.getRequestURI(),                                    //请求的URI
                method,                                                     //请求的方法名称
                response.getStatus(),                                       //http状态码
                ex == null ? "-" : ex.getClass().getSimpleName(),           //异常信息
                sw.getTotalTimeMillis(),                                    //总计时长
                sw.getTotalTimeMillis() - sw.getLastTaskTimeMillis(),       //处理方法的耗时
                sw.getLastTaskTimeMillis());                                //结束时长
        stopWatch.remove();
    }
}
