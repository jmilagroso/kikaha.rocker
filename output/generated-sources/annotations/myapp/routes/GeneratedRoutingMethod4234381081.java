package myapp.routes;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.enterprise.inject.Typed;
import kikaha.core.modules.http.WebResource;
import kikaha.urouting.RoutingMethodResponseWriter;
import kikaha.urouting.RoutingMethodParameterReader;
import kikaha.urouting.RoutingMethodExceptionHandler;
import kikaha.urouting.api.AsyncResponse;

@Singleton
@Typed( HttpHandler.class )
@WebResource( path="/decompress/", method="GET" )
public class GeneratedRoutingMethod4234381081 implements HttpHandler {

	@Inject RoutingMethodResponseWriter responseWriter;
	@Inject RoutingMethodParameterReader methodDataProvider;
	@Inject RoutingMethodExceptionHandler exceptionHandler;
	
	@Inject myapp.routes.HomeResource instance;

	@Override
	public void handleRequest( HttpServerExchange exchange ) throws Exception {
			if ( exchange.isInIoThread() ) {
				exchange.dispatch(this);
				return;
			}
			else if ( !exchange.isInIoThread() && !exchange.isBlocking() )
				exchange.startBlocking();
		try {
			final AsyncResponse asyncResponse = new AsyncResponse( exchange, responseWriter, exceptionHandler, "application/json" );
			exchange.dispatch();
			instance.renderDecompress( 
			asyncResponse
			 );
		} catch ( Throwable cause ) {
			responseWriter.write( exchange, exceptionHandler.handle( cause ) );
			exchange.endExchange();
		}
	}
}