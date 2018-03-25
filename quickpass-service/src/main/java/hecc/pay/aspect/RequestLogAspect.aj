package hecc.pay.aspect;

import com.mongodb.BasicDBObject;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther xuhoujun
 * @Description: 请求切面
 * @Date: Created In 下午4:57 on 2018/3/18.
 */
@Aspect
@Order(1)
@Component
public class RequestLogAspect {

    private Logger logger = Logger.getLogger(RequestLogAspect.class);

    @Pointcut("execution(public * hecc.pay.controller..*.*(..))")
    public void requestLog() {
    }

    @Before("requestLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        /**
         * 获取HttpServletRequest
         */
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        /**
         * 获取要记录的日志内容
         */
        BasicDBObject logInfo = getBasicDBObject(request, joinPoint);
        logger.info(logInfo);
    }

    private BasicDBObject getBasicDBObject(HttpServletRequest request, JoinPoint joinPoint) {
        /**
         * 基本信息
         */
        BasicDBObject result = new BasicDBObject();
        result.append("requestURL", request.getRequestURL().toString());
        result.append("requestURI", request.getRequestURI());
        result.append("queryString", request.getQueryString());
        result.append("remoteAddress", request.getRemoteAddr());
        result.append("remoteHost", request.getRemoteHost());
        result.append("remotePort", request.getRemotePort());
        result.append("localAddress", request.getLocalAddr());
        result.append("localName", request.getLocalName());
        result.append("method", request.getMethod());
        result.append("headers", getHeadersInfo(request));
        result.append("parameters", request.getParameterMap());
        result.append("classMethod", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        result.append("args", Arrays.toString(joinPoint.getArgs()));
        return result;
    }

    /**
     * 获取头信息
     *
     * @param request 请求
     * @return 请求头信息
     */
    private Map<String, String> getHeadersInfo(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>(4);
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

}

