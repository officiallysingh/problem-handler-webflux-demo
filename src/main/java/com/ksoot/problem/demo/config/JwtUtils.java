package com.ksoot.problem.demo.config;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.PlainJWT;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.Jwt.Builder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.text.ParseException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

@Log4j2
class JwtUtils {

	static final String PRINCIPLE_NAME_CLAIM_ID = IdentityHelper.ClaimName.CLIENT_NAME.value();

	private static Converter<Map<String, Object>, Map<String, Object>> claimSetConverter = MappedJwtClaimSetConverter
			.withDefaults(Collections.emptyMap());

	static Jwt decodeToken(String token) throws JwtException {
		log.trace("token --->>" + token);
		JWT jwt = parse(token);
		if (jwt instanceof PlainJWT) {
			log.trace("Failed to decode unsigned token");
			throw new BadJwtException("Unsupported algorithm of " + jwt.getHeader().getAlgorithm());
		}
		Jwt createdJwt = createJwt(token, jwt);
		log.trace("---------------- Headers ----------------\n");
		for (Entry<String, Object> entry : createdJwt.getHeaders().entrySet()) {
			log.trace(entry.getKey() + " : " + entry.getValue());
		}
		log.trace("------------------------------------------\n");
		log.trace("---------------- Claims ----------------\n");
		for (Entry<String, Object> entry : createdJwt.getClaims().entrySet()) {
			log.trace(entry.getKey() + " : " + entry.getValue());
		}
		log.trace("------------------------------------------");
		return createdJwt;
	}

	private static JWT parse(String token) {
		try {
			return JWTParser.parse(token);
		} catch (final Exception e) {
			throw new BadJwtException("JWT parse exception", e);
		}
	}

	private static Jwt createJwt(String token, JWT parsedJwt) {
		try {
			JWTClaimsSet jwtClaimsSet = extractJWTClaimsSet(parsedJwt);
			Map<String, Object> headers = new LinkedHashMap<>(parsedJwt.getHeader().toJSONObject());
			Map<String, Object> claims = claimSetConverter.convert(jwtClaimsSet.getClaims());
			// @formatter:off
            Builder jwtBuilder = Jwt.withTokenValue(token)
                    .headers(h -> h.putAll(headers))
                    .claims(c -> c.putAll(claims));
                return jwtBuilder.build();
            // @formatter:on
		} catch (final Exception e) {
			throw new BadJwtException("Failed to process JWT", e);
		}
	}

	private static JWTClaimsSet extractJWTClaimsSet(final JWT jwt) {
		try {
			return jwt.getJWTClaimsSet();
		} catch (final ParseException e) {
			// Payload not a JSON object
			throw new BadJwtException("JWT parse exception", e);
		}
	}

	static JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter() {
		JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
		return jwtGrantedAuthoritiesConverter;
	}

	private JwtUtils() {
		throw new IllegalStateException("Just a utility class, not supposed to be instantiated");
	}
}
