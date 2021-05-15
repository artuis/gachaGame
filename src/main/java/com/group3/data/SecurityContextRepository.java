package com.group3.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.group3.util.AuthenticationManager;

import reactor.core.publisher.Mono;

@Component
public class SecurityContextRepository implements ServerSecurityContextRepository {

	@Value("${springbootwebfluxjjwt.jjwt.cookiename}")
	private String cookieKey;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public Mono<Void> save(ServerWebExchange swe, SecurityContext sc) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Mono<SecurityContext> load(ServerWebExchange swe) {
		if (swe.getRequest().getCookies().containsKey(cookieKey)
				&& swe.getRequest().getCookies().getFirst(cookieKey) != null) {
			String authCookie = swe.getRequest().getCookies().getFirst(cookieKey).getValue();
			Authentication auth = new UsernamePasswordAuthenticationToken(authCookie, authCookie);
			return this.authenticationManager.authenticate(auth)
					.map(authentication -> new SecurityContextImpl(authentication));
		} else {
			return Mono.empty();
		}
	}

}
