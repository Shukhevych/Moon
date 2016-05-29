package moon.frontserver.security.jwt;

import io.jsonwebtoken.*;
import moon.frontserver.database.domain.Player;

import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class TokenHandler {

	private final String secret;

	public TokenHandler(String secret) {
		this.secret = Base64.getEncoder().encodeToString(secret.getBytes());
	}

	public String parseUserFromToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
		String username = Jwts.parser()
			.setSigningKey(secret)
			.parseClaimsJws(token)
			.getBody()
			.getSubject();
		return username;
	}

	public String createTokenForUser(Player user) {
		Date now = new Date();
		Date expiration = new Date(now.getTime() + TimeUnit.HOURS.toMillis(1L));
		return Jwts.builder()
			.setId(UUID.randomUUID().toString())
			.setSubject(user.getEmail())
			.setIssuedAt(now)
			.setExpiration(expiration)
			.signWith(SignatureAlgorithm.HS512, secret)
			.compact();
	}
}
