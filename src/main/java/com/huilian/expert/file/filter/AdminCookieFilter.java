package com.huilian.expert.file.filter;

import com.huilian.expert.common.bean.entity.BasUser;
import com.huilian.expert.common.utils.UserThreadLocalContext;
import com.huilian.expert.file.service.BasUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Component
public class AdminCookieFilter extends OncePerRequestFilter {

    @Autowired
    private BasUserService basUserService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {

            //获取cookie
            Cookie[] cookies = httpServletRequest.getCookies();
            if  (cookies == null || cookies.length == 0) {
                return;
            }
            //       存储cookie到map中
            Map<String, String> cookieMap = new HashMap<String, String>();
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie.getValue());
            }

            if (!cookieMap.containsKey("szesUserGUID")) {
                return;
            }

            String szesUserGUID = cookieMap.get("szesUserGUID");
//            String szesUserGUID = "B13F9C38-B714-11B2-ACB2-9DCC0BDFFB48";
            BasUser user = basUserService.getById(szesUserGUID);
            if (Objects.isNull(user)) {
                return;
            }
            UserThreadLocalContext.setAdminUser(user);
            UserThreadLocalContext.setAdminCookie(szesUserGUID);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(szesUserGUID, null, null);
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        } finally {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            // 防止oom
            UserThreadLocalContext.removeAdminUser();
            UserThreadLocalContext.removeAdminCookie();
        }
    }
}
