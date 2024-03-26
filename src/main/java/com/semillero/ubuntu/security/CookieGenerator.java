package com.semillero.ubuntu.security;

import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class CookieGenerator {
    @Autowired
    JwtUtil jwtUtil;

    public Cookie generateCookie(String data) {
        String token = jwtUtil.generateToken(data);
        Cookie cookie = new Cookie("AuthorizedUser", token);
        cookie.setSecure(true);
        cookie.setAttribute("SameSite", "none");
        cookie.setAttribute("Partitioned", "true");
        cookie.setMaxAge(604800);
        cookie.setPath("/");
        return cookie;
    }
}
