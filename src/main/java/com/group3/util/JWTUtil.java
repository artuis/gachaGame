package com.group3.util;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.group3.beans.Gamer;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {
	
	@Value("${springbootwebfluxjjwt.jjwt.secret}")
	private String secret;
	
	@Value("${springbootwebfluxjjwt.jjwt.expiration}")
	private String expirationTime;

	private Key key;

	@PostConstruct
	public void init(){
		this.key = Keys.hmacShaKeyFor(secret.getBytes());
	}
	
	public void setExpirationTime(String expTime) {
		this.expirationTime = expTime;
	}
	
	public void setSecret(String secret) {
		this.secret = secret;
	}
	
	public void setKey(Key key) {
		this.key = key;
	}

	public Claims getAllClaimsFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}
	
	public String getUsernameFromToken(String token) {
		return getAllClaimsFromToken(token).getSubject();
	}
	
	public Date getExpirationDateFromToken(String token) {
		return getAllClaimsFromToken(token).getExpiration();
	}
	
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	
	public String generateToken(Gamer gamer) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("role", gamer.getAuthorities());
		claims.put("id", gamer.getGamerId());
		return doGenerateToken(claims, gamer.getUsername());
	}

	private String doGenerateToken(Map<String, Object> claims, String username) {
		Long expirationTimeLong = Long.parseLong(expirationTime); //in second
		
		final Date createdDate = new Date();
		final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong * 1000);

		return Jwts.builder()
				.setClaims(claims)
				.setSubject(username)
				.setIssuedAt(createdDate)
				.setExpiration(expirationDate)
				.signWith(key)
				.compact();
	}
	
	public Boolean validateToken(String token) {
		return !isTokenExpired(token);
	}

}
