package com.nguyenanhtuyen.shopapp.components;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nguyenanhtuyen.shopapp.exceptions.InvalidParamException;
import com.nguyenanhtuyen.shopapp.models.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

	@Value("${jwt.expiration}")
	private int expiration; // save to an enviroment variable
	
	@Value("${jwt.secretKey}")
	private String secretKey;
	
	public String generateToken(User user) throws Exception {
		// properties -> claims
		Map<String, Object> claims = new HashMap<>();
		// this.generateSecretKey();
		claims.put("phoneNumber", user.getPhoneNumber());
		try {
			String token = Jwts.builder()
					.setClaims(claims)
					.setSubject(user.getPhoneNumber())
					.setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
					.signWith(getSignInKey(), SignatureAlgorithm.HS256)
					.compact();
			return token;
		} catch (Exception e) {
			throw new InvalidParamException("Cannot create jwt token. Error: " + e.getMessage());
		}
	}
	
	private Key getSignInKey() {
		byte[] bytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(bytes);
	}
	
	private String generateSecretKey() {
		SecureRandom random = new SecureRandom();
		byte[] keyBytes = new byte[32]; // 256 bit key
		random.nextBytes(keyBytes);
		String secretKey = Encoders.BASE64.encode(keyBytes);
		return secretKey;
	}
	
	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSignInKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = this.extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	// check expiration
	public boolean isTokenExpired(String token) {
		Date expirationDate = this.extractClaim(token, Claims::getExpiration);
		return expirationDate.before(new Date());
	}
	
}
