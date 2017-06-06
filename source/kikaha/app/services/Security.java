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

    private Integer expirationInDays;

    public Security(String secret, Integer days) {
        try {
            algorithm = Algorithm.HMAC256(secret);
            expirationInDays = days;
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
            Date dt = new Date();
            DateTime dtOrg = new DateTime(dt);
            DateTime dtPlusNDays = dtOrg.plusDays(expirationInDays);

            token = JWT.create()
                    .withJWTId(id)
                    .withClaim("name", claim)
                    .withIssuer(issuer)
                    .withExpiresAt(dtPlusNDays.toDate())
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
