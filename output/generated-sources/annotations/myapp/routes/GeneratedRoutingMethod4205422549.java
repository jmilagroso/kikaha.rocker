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

@Singleton
@Typed( HttpHandler.class )
@WebResource( path="/hazelcast/generate/", method="GET" )
public class GeneratedRoutingMethod4205422549 implements HttpHandler {

	@Inject RoutingMethodResponseWriter responseWriter;
	@Inject RoutingMethodParameterReader methodDataProvider;
	@Inject RoutingMethodExceptionHandler exceptionHandler;
	
	@Inject myapp.routes.HazelCastResource instance;

	@Override
	public void handleRequest( HttpServerExchange exchange ) throws Exception {
			if ( exchange.isInIoThread() ) {
				exchange.dispatch(this);
				return;
			}
			else if ( !exchange.isInIoThread() && !exchange.isBlocking() )
				exchange.startBlocking();
		try {
			final rocker.RockerTemplate response = instance.renderGenerateDummyDataToHazelCast( 
			 );
				responseWriter.write( exchange, "text/html", response );
		} catch ( Throwable cause ) {
			responseWriter.write( exchange, exceptionHandler.handle( cause ) );
		}
	}
}