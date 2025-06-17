package com.wipe.userservice.util;


import cn.hutool.core.lang.UUID;
import com.wipe.commonmodel.enums.EnumStatusCode;
import com.wipe.commonmodel.exception.ServiceException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.InvalidKeyException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

/**
 * @author wipe
 * @date 2025/6/16 下午6:00
 */
@Slf4j
@Component
@Getter
public class JwtUtil {

    private JwtUtil() {
    }

    private static final SecretKey KEY = Jwts.SIG.HS256.key().build();

    @Value("${jwt.ttl}")
    private int ttl;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.subject}")
    private String subject;


    public String createJwt(Map<String, String> claim) {
        // 令牌id
        String uuid = UUID.randomUUID().toString(true);
        Date ttl = Date.from(Instant.now().plusSeconds(this.ttl));

        try {
            return Jwts.builder()
                    .id(uuid)
                    // 设置头部信息header
                    .header()
                    .add("typ", "JWT")
                    .add("alg", "HS256")
                    .and()
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
                    .signWith(KEY)
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
                    .verifyWith(KEY)
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


}
