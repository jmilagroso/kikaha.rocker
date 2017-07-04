package kikaha.app.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.joda.time.DateTime;
import java.util.Date;

/**
 * Created by jay on 5/30/17.
 */
public class Security {

    private static JWTVerifier verifier;

    private Algorithm algorithm;

    private Integer expiresInSeconds;

    private Date now;
    private Date exp;

    public Security(String secret, Integer seconds) {
        try {
            algorithm = Algorithm.HMAC256(secret);
            expiresInSeconds = seconds;
        } catch (Exception e) {
            throw new IllegalStateException(e.getLocalizedMessage());
        }
    }

    /**
     * Generates new token.
     * @param id The id string/integer parameter.
     * @param claim The claim string parameter.
     * @param issuer The issuer string parameter.
     * @return string The token string.
     */
    public String token(String id, String claim, String issuer) {
        String token = null;
        try {
            now = new Date();
            exp = new Date(now.getTime() + (1000*expiresInSeconds));

            token = JWT.create()
                    .withIssuedAt(now)
                    .withNotBefore(now)
                    .withExpiresAt(exp)
                    .withJWTId(id)
                    .withClaim("name", claim)
                    .withIssuer(issuer)
                    .sign(algorithm);
        } catch (Exception e) {
            throw new IllegalStateException(e.getLocalizedMessage());
        }

        return token;
    }

    /**
     * Generates new token.
     * @param id The id string/integer parameter.
     * @param claim The claim string parameter.
     * @param issuer The issuer string parameter.
     * @return string The token string.
     */
    public String token(String id, String claim, String issuer, Integer expiresInSecondsOverride) {
        String token = null;
        try {
            now = new Date();
            exp = new Date(now.getTime() + (1000*expiresInSecondsOverride));

            token = JWT.create()
                    .withIssuedAt(now)
                    .withNotBefore(now)
                    .withExpiresAt(exp)
                    .withJWTId(id)
                    .withClaim("name", claim)
                    .withIssuer(issuer)
                    .sign(algorithm);
        } catch (Exception e) {
            throw new IllegalStateException(e.getLocalizedMessage());
        }

        return token;
    }

    /**
     * Validates token
     * @param token The token string parameter.
     * @param id The id string/integer parameter.
     * @param claim The claim string parameter.
     * @param issuer The issuer string parameter.
     * @return boolean Is valid.
     */
    public boolean valid(String token, String id, String claim, String issuer) {

        boolean valid = false;

        try {
            verifier = JWT.require(algorithm)
                    .withJWTId(id)
                    .withClaim("name", claim)
                    .withIssuer(issuer)
                    .build(); //Reusable verifier instance

            DecodedJWT jwt = verifier.verify(token);
            valid = true;
        } catch (Exception e) {
            throw new IllegalStateException(e.getLocalizedMessage());
        }

        return valid;
    }
}
