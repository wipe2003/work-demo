package com.wipe.commonmodel.util;


import cn.hutool.core.lang.UUID;
import com.wipe.commonmodel.enums.EnumStatusCode;
import com.wipe.commonmodel.exception.ServiceException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

/**
 * @author wipe
 * @date 2025/6/16 下午6:00
 */
@Slf4j
@Getter
@Component
@ConditionalOnProperty(prefix = "jwt", value = "enable", havingValue = "true")
public class JwtUtil implements InitializingBean {

    public JwtUtil() {
    }

    private SecretKey key;

    @Value("${jwt.enable}")
    private boolean enabled;

    @Value("${jwt.ttl:259200}")
    private int ttl;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.subject}")
    private String subject;

    @Value("${jwt.secret-key}")
    private String secretKey;


    public String createJwt(Map<String, Object> claim) {
        // 令牌id
        String uuid = UUID.randomUUID().toString(true);
        Date ttl = Date.from(Instant.now().plusSeconds(this.ttl));

        try {
            return Jwts.builder()
                    .id(uuid)
                    // 设置自定义负载信息payload
                    .claims(claim)
                    // 过期日期
                    .expiration(ttl)
                    // 签发时间
                    .issuedAt(new Date())
                    // 主题
                    .subject(subject)
                    // 签发者
                    .issuer(issuer)
                    // 签名
                    .signWith(key)
                    .compact();
        } catch (InvalidKeyException e) {
            throw new ServiceException(EnumStatusCode.ERROR_OPERATION, "令牌生成失败" + e.getMessage());
        }
    }

    /**
     * 解析token
     *
     * @param token token
     * @return Jws<Claims>
     */
    public Jws<Claims> parse(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
        } catch (JwtException | IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(EnumStatusCode.ERROR_NOT_LOGIN);
        }
    }

    public JwsHeader parseHeader(String token) {
        return parse(token).getHeader();
    }

    public Claims parsePayload(String token) {
        return parse(token).getPayload();
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        if (!enabled) {
            throw new ServiceException(EnumStatusCode.ERROR_OPERATION, "JWT未启用");
        }
        key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
