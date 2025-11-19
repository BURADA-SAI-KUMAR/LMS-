package com.student.lms.Service;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	private String secretKey ="";
	
	public JwtService() {
		try {
			KeyGenerator keyGen=KeyGenerator.getInstance("HmacSHA256");
			SecretKey sk=keyGen.generateKey();
			secretKey=Base64.getEncoder().encodeToString(sk.getEncoded());
		}
		catch(NoSuchAlgorithmException  e) {
			throw new RuntimeException(e);
		}
	}

	public String generateToken(String username) {
		
		Map<String,Object> claims=new HashMap<>();
	
		return Jwts.builder().addClaims(claims).setSubject(username)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) 
				.signWith(getkey()).compact();
		
		
	}

	private SecretKey getkey() {
		byte[] keybytes=Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keybytes);
	}
	
	public String extractFirstName(String token) {
		return extractClaim(token,Claims::getSubject);
	}

	private <T> T  extractClaim(String token, Function<Claims,T> claimResolver ) {
       final Claims claims=extractAllClaims(token);
		return claimResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		
		return Jwts.parser()
				.verifyWith(getkey()).build().parseSignedClaims(token).getPayload();
	}



	public boolean validateToken(String token, UserDetails userDetails) {
		final String firstName=extractFirstName(token);
		return(firstName.equals(userDetails.getUsername()) && !isTokenExpired(token));
		
		
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token,Claims::getExpiration);
	}

}
