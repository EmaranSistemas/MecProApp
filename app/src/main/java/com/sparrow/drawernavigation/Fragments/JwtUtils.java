package com.sparrow.drawernavigation.Fragments;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    private static final String METABASE_SECRET_KEY = "54d47deb4e56d2f55e345944302bdf1c7374b58cfca16e633a1061262ba2f20d";

    public static String generateMetabaseToken(int dashboardId) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        Map<String, Object> payload = new HashMap<>();
        payload.put("resource", new HashMap<String, Object>() {{
            put("dashboard", dashboardId);
        }});
        payload.put("params", new HashMap<String, Object>());
        payload.put("exp", (nowMillis / 1000) + (10 * 60)); // 10 minute expiration

        JwtBuilder builder = Jwts.builder()
                .setClaims(payload)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, METABASE_SECRET_KEY);

        return builder.compact();
    }

}