package com.ksoot.problem.demo.config;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import reactor.core.publisher.Mono;

class JwtStringDecoder implements ReactiveJwtDecoder {

	@Override
	public Mono<Jwt> decode(final String token) throws JwtException {
		return Mono.just(JwtUtils.decodeToken(token));
	}
}
