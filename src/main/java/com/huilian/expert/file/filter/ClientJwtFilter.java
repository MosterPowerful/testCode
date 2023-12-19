package com.huilian.expert.file.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huilian.expert.common.bean.entity.BizExpert;
import com.huilian.expert.common.bean.response.Result;
import com.huilian.expert.common.config.ApplicationProperties;
import com.huilian.expert.common.enums.BusinessExceptionEnum;
import com.huilian.expert.common.utils.JwtUtil;
import com.huilian.expert.common.utils.UserThreadLocalContext;
import com.huilian.expert.file.service.BizExpertService;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Optional;

@Component
public class ClientJwtFilter extends OncePerRequestFilter {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BizExpertService bizExpertService;

    @Autowired
    private Environment env;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
        @NotNull HttpServletResponse httpServletResponse, @NotNull FilterChain filterChain)
        throws ServletException, IOException {

        try {
            if (this.checkToken(httpServletRequest)) {
                Object o = this.validateToken(httpServletRequest);
                if (o instanceof String && StringUtils.isNotBlank(o.toString())) {
                    BizExpert expert = bizExpertService.getById(o.toString());
                    if (null == expert) {
                        return;
                    }
                    UserThreadLocalContext.setClientUser(expert);
                    String jwt = httpServletRequest.getHeader(applicationProperties.getJwt().getHeader());
                    UserThreadLocalContext.setClientJwt(jwt);
                    // TODO  权限表
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        o, null, null);
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        } finally {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            // 防止oom
            UserThreadLocalContext.removeClientUser();
            UserThreadLocalContext.removeClientJwt();
        }
    }


    private void buildResponse(HttpServletResponse httpServletResponse, String NOT_FOUND) throws IOException {
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");
        PrintWriter printWriter = httpServletResponse.getWriter();
        printWriter.write(new ObjectMapper().writeValueAsString(
            Result.success(BusinessExceptionEnum.LOGIN_INFORMATION_FAILED.getCode(), NOT_FOUND, null)));
        printWriter.flush();
        printWriter.close();
    }

    /**
     * 检查请求头是否包含token
     *
     * @param request 请求
     * @return boolean
     */
    private boolean checkToken(HttpServletRequest request) {
        // TODO  前缀是否检查？
        String header = request.getHeader(applicationProperties.getJwt().getHeader());
        if (StringUtils.isBlank(header)){
            header= request.getParameter("Authorization");
        }
        return (null != header);
    }

    private Object validateToken(HttpServletRequest request) {
        String header = request.getHeader(applicationProperties.getJwt().getHeader());
        if (StringUtils.isBlank(header)){
            header= request.getParameter("Authorization");
        }
        // TODO  前缀处理

        // 解析token
        try {
            String id = jwtUtil.parseToken(header, String.class);
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }


}
