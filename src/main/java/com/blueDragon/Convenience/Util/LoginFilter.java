package com.blueDragon.Convenience.Util;

import com.blueDragon.Convenience.Code.ErrorCode;
import com.blueDragon.Convenience.Code.ResponseCode;
import com.blueDragon.Convenience.Dto.Response.ErrorResponseDTO;
import com.blueDragon.Convenience.Dto.Response.ResponseDTO;
import com.blueDragon.Convenience.Dto.User.CustomUserDetails;
import com.blueDragon.Convenience.Model.User;
import com.blueDragon.Convenience.Repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        // 추후 삭제 필요, 확인 용
        System.out.println(username + password);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        System.out.println(authToken);
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();
        String nickname = customUserDetails.getNickname();

        String accessToken = jwtUtil.createJwt("accessToken", username, nickname, 86400000L);
        String refreshToken = jwtUtil.createJwt("refreshToken", username, nickname, 86400000L);

        response.setHeader("accessToken", "Bearer " + accessToken);
        response.setHeader("refreshToken", "Bearer " + refreshToken);

        User user = userRepository.findByUsername(username).orElse(null);

        ResponseDTO<?> responseDTO = new ResponseDTO<>(ResponseCode.SUCCESS_LOGIN, null);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(responseDTO);
        response.getWriter().write(jsonResponse);
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {

        response.setStatus(401);

        ErrorResponseDTO responseDTO = new ErrorResponseDTO(ErrorCode.USER_NOT_FOUND);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(responseDTO);
        response.getWriter().write(jsonResponse);
    }
}