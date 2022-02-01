package com.kdemo.springcloud;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;


public class TokenDemo {

    /**
     * 对称加密密钥
     */
    private static final String SECRET_KEY = "dfcf956c-6dac-4608-9015-3f8a17cf986a";

    /**
     * 对称加密得到的算法
     */
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY);


    public static void main(String[] args) {
        System.out.println(UUID.randomUUID());
    }

    public static String generateToken(String content) {
        if (content == null) {
            return null;
        }
        /**
         * jti: jwt唯一标记
         * iss: jwt签发主体
         * exp: 时间戳, 过期时间
         * iat: jwt签发时间
         * sub: jwt所有者
         */

        Date expireDate = Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        JWT.create().withIssuer("Tomcat")
                .withExpiresAt(expireDate)
                .withClaim("name", "dsafdsfadsf")
                .sign(ALGORITHM);

        return "asdf";
    }


}
