package com.bipinkish.store.services;

import com.bipinkish.store.entities.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.crypto.SecretKey;
import java.util.Date;

@Data
@AllArgsConstructor
public class Jwt {
    private Claims claims;
    private SecretKey secretKey;

    public boolean isExpired() {
        return claims.getExpiration().before(new Date());
    }

    public Long getUserId(){
        return Long.valueOf(claims.getSubject());
    }

    public Role getRole(){
        return Role.valueOf(claims.get("role").toString());
    }

    public String toString(){
        return Jwts.builder().claims(claims).signWith(secretKey).compact();
    }
}
