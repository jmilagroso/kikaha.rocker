package rocker;

import java.io.IOException;
import javax.enterprise.inject.Typed;
import javax.inject.*;
import io.undertow.server.HttpServerExchange;
import kikaha.core.NotFoundHandler;
import kikaha.core.modules.http.ContentType;
import kikaha.urouting.api.*;

@ContentType( Mimes.HTML )
@Singleton
@Typed( Serializer.class )
public class HTMLRockerSerializer implements Serializer {

    @Inject
    RockerSerializerFactory factory;

    @Inject
    NotFoundHandler notFoundHandler;

    @Override
    public <T> void serialize(final T object, final HttpServerExchange exchange, String encoding) throws IOException {
        try {
            final RockerTemplate template = (RockerTemplate) object;
            String serialized = factory.serializer().serialize(template);
            exchange.getResponseSender().send(serialized);
        } catch ( Exception cause ) {
            cause.printStackTrace();
            handleNotFound( exchange );
        }
    }

    private void handleNotFound( final HttpServerExchange exchange ) throws IOException {
        try {
            notFoundHandler.handleRequest( exchange );
        } catch (Exception e) {
            throw new IOException( e );
        }
    }
}