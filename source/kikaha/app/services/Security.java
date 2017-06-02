package kikaha.app.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import kikaha.app.routes.JWTResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by jay on 5/30/17.
 */
public class Security {

    public static Algorithm algorithm;

    private final static String secret = "EFFA813E0EE1340533837C5E96FAEF220DDAE85B5E41EFC32EF210F367F48BFE";

    private static Logger logger = LoggerFactory.getLogger(Security.class);

    private static JWTVerifier verifier;

    static {
        try {
            algorithm = Algorithm.HMAC256(secret);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean valid(String token, String id, String claim, String issuer) {

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
            logger.error(e.getLocalizedMessage().toString());
        }

        return valid;
    }
}
