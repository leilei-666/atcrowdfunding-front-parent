package com.leilei.atcrowdfunding.user.component;

import com.leilei.front.constant.AppConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Servlet3.0以后，servlet，filter，listener都有注解
 */

@Slf4j
@WebFilter(urlPatterns = "/*")
public class LoginKeyExpireFilter implements Filter {
    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.debug("LoginKeyExpireFilter进行拦截，对登录信息进行自动续期");
        HttpServletRequest req = (HttpServletRequest) request;
        String accessToken = req.getParameter("accessToken");
        if (StringUtils.isEmpty(accessToken)) {
            //非登录后发送的请求，不用做任何操作
            chain.doFilter(req, response);
        } else {
            //续期，每个操作过来，重新设置超时
            redisTemplate.expire(AppConstant.MEMBER_LOGIN_CACHE_PREFIX, 30, TimeUnit.MINUTES);
            chain.doFilter(req, response);
        }
    }

    @Override
    public void destroy() {

    }
}
