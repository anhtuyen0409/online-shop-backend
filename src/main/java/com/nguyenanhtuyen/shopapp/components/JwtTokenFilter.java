package com.nguyenanhtuyen.shopapp.components;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
	
	@Value("${api.prefix}")
	private String apiPrefix;
	
	private final UserDetailsService userDetailsService;
	
	private final JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
			throws ServletException, IOException {
		
		if(isBypassToken(request)) {
			filterChain.doFilter(request, response); // enable bypass - cho phép thực hiện request ko cần token
			return;
		}
		
		// các request yêu cầu token
		final String authHeader = request.getHeader("Authorization");
		if(authHeader != null && authHeader.startsWith("Bearer ")) {
			final String token = authHeader.substring(7);
			final String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
			
			if(phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(phoneNumber);
				if(jwtTokenUtil.validateToken(token, userDetails)) {
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails,
							null,
							userDetails.getAuthorities()
					);
					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
			}
			filterChain.doFilter(request, response);
		}
		
	}
	
	// kiểm tra các request ko yêu cầu token
	private boolean isBypassToken(@NonNull HttpServletRequest request) {
		
		final List<Pair<String, String>> bypassTokens = Arrays.asList(
				Pair.of(String.format("%s/categories", apiPrefix), "GET"),
				Pair.of(String.format("%s/products", apiPrefix), "GET"),
				Pair.of(String.format("%s/users/register", apiPrefix), "POST"),
				Pair.of(String.format("%s/users/login", apiPrefix), "POST")
		);
		
		for(Pair<String, String> bypassToken : bypassTokens) {
			if(request.getServletPath().contains(bypassToken.getFirst()) && request.getMethod().equals(bypassToken.getSecond())) {
				return true;
			}
		}
		
		return false;
		
	}

}
