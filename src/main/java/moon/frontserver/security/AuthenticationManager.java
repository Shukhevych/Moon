package moon.frontserver.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import moon.frontserver.database.domain.Player;
import moon.frontserver.security.jwt.TokenHandler;

public class AuthenticationManager {

	TokenHandler tokenHandler;

	public AuthenticationManager() {
		tokenHandler = new TokenHandler("secret");
	}

	// login
	public String tryLogin(Player player, String password) {
		if (player != null && player.getPassword().equals(password)) {
			String token = tokenHandler.createTokenForUser(player);
			return token;
		}
		return null;
	}

	public String tokenAuth(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
		String userEmail = tokenHandler.parseUserFromToken(token);
		return userEmail;
	}
}
