package co.sribhakti.api.common;

import co.sribhakti.api.auth.models.TokenPayload;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenizer {
    String secretKey = "secret_key"; // TODO : remove hardcode
    Algorithm algorithm = Algorithm.HMAC256(secretKey);
    JWTVerifier verifier = JWT.require(algorithm).build();

    public String JWTTokenCreation(TokenPayload payload) {


        // Define the algorithm for token creation (e.g., HMAC256)
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date()); // Set the current time
        calendar.add(Calendar.HOUR, 1); // Set expiration time to 1 hour from now
        Date expirationTime = calendar.getTime();

        // Create the JWT token
        String token = JWT.create()
                .withClaim("id", payload.getId())
                .withClaim("name", payload.getName())
                .withClaim("phone", payload.getPhone())
                .withClaim("permissions", payload.getPermissions())
                .withIssuedAt(new Date()) // Set the token's issuance time (current time)
                .withExpiresAt(expirationTime)
                .sign(algorithm);

        return token;
    }

    TokenPayload TokenDecoder(String token) {
        TokenPayload payload = new TokenPayload();

        DecodedJWT decodedJWT = verifier.verify(token);
        Map<String, Claim> claims = decodedJWT.getClaims();

        if (claims.get("id") != null)
            payload.setId(decodedJWT.getClaim("id").asString());
        if (claims.get("name") != null)
            payload.setName(claims.get("name").asString());
        if (claims.get("phone") != null)
            payload.setName(claims.get("phone").asString());
        if(claims.get("permission") != null)
            payload.setPermissions(claims.get("permission").asList(String.class));

        return payload;

    }
}
