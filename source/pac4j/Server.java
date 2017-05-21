package pac4j;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;

import io.undertow.server.session.InMemorySessionManager;
import io.undertow.server.session.SessionAttachmentHandler;
import io.undertow.server.session.SessionCookieConfig;
import org.pac4j.core.config.Config;
import org.pac4j.undertow.handler.ApplicationLogoutHandler;
import org.pac4j.undertow.handler.CallbackHandler;
import org.pac4j.undertow.handler.SecurityHandler;


/**
 * Undertow demo server demonstrating how to integrate pac4j.
 *
 * @author Michael Remond
 * @since 1.0.0
 *
 * https://github.com/pac4j/undertow-pac4j-demo
 */
public class Server {

    public final static String JWT_SALT = "12345678901234567890123456789012";

    public static void main(final String[] args) {

        final Config config = new CustomConfigFactory().build();

        PathHandler path = new PathHandler();

        path.addExactPath("/", SecurityHandler.build(CustomHandlers.indexHandler(), config, "AnonymousClient"));
        path.addExactPath("/index.html", SecurityHandler.build(CustomHandlers.indexHandler(), config, "AnonymousClient"));

        path.addExactPath("/facebook/notprotected.html", CustomHandlers.protectedIndex);
        path.addExactPath("/facebook/index.html", SecurityHandler.build(CustomHandlers.protectedIndex, config, "FacebookClient"));
        path.addExactPath("/facebook/notprotected.html", SecurityHandler.build(CustomHandlers.notProtectedIndex, config, "AnonymousClient"));
        path.addExactPath("/facebookadmin/index.html", SecurityHandler.build(CustomHandlers.protectedIndex, config, "FacebookClient", "admin"));
        path.addExactPath("/facebookcustom/index.html", SecurityHandler.build(CustomHandlers.protectedIndex, config, "FacebookClient", "custom"));
        path.addExactPath("/twitter/index.html", SecurityHandler.build(CustomHandlers.protectedIndex, config, "TwitterClient,FacebookClient"));
        path.addExactPath("/form/index.html", SecurityHandler.build(CustomHandlers.protectedIndex, config, "FormClient"));
        path.addExactPath("/form/index.html.json", SecurityHandler.build(CustomHandlers.authenticatedJsonHandler, config, "FormClient"));
        path.addExactPath("/basicauth/index.html", SecurityHandler.build(CustomHandlers.protectedIndex, config, "IndirectBasicAuthClient"));
        path.addExactPath("/cas/index.html", SecurityHandler.build(CustomHandlers.protectedIndex, config, "CasClient"));
        path.addExactPath("/saml2/index.html", SecurityHandler.build(CustomHandlers.protectedIndex, config, "SAML2Client"));
        path.addExactPath("/oidc/index.html", SecurityHandler.build(CustomHandlers.protectedIndex, config, "OidcClient"));
        path.addExactPath("/protected/index.html", SecurityHandler.build(CustomHandlers.protectedIndex, config));

        path.addExactPath("/dba/index.html", SecurityHandler.build(CustomHandlers.protectedIndex, config, "DirectBasicAuthClient,ParameterClient"));
        path.addExactPath("/rest-jwt/index.html", SecurityHandler.build(CustomHandlers.protectedIndex, config, "ParameterClient"));

        path.addExactPath("/callback", CallbackHandler.build(config, null, true));
        path.addExactPath("/logout", new ApplicationLogoutHandler(config, "/?defaulturlafterlogout"));

        //path.addPrefixPath("/assets/js", CustomHandlers.resource(new ClassPathResourceManager(DemoServer.class.getClassLoader())));

        path.addExactPath("/loginForm.html", CustomHandlers.loginFormHandler(config));
        path.addExactPath("/jwt.html", SecurityHandler.build(CustomHandlers.jwtHandler(), config, "AnonymousClient"));
        path.addExactPath("/forceLogin", CustomHandlers.forceLoginHandler(config));

        Undertow server = Undertow.builder().addHttpListener(8080, "localhost")
                .setHandler(new SessionAttachmentHandler(new ErrorHandler(path), new InMemorySessionManager("SessionManager"), new SessionCookieConfig())).build();
        server.start();
    }
}