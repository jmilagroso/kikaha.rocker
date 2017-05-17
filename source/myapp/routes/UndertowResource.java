package myapp.routes;

import com.fizzed.rocker.Rocker;
import com.mongodb.ServerAddress;
import com.mongodb.async.client.*;
import com.mongodb.connection.ClusterSettings;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import kikaha.core.modules.http.WebResource;
import myapp.models.Chat;
import myapp.services.Builder;
import org.bson.Document;

import javax.inject.Singleton;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@WebResource( path="/undertow", method="GET" )
public class UndertowResource implements HttpHandler {

    ServerAddress server1 = new ServerAddress("localhost", 27017);

    List<ServerAddress> servers = Arrays.asList(new ServerAddress[]{ server1 });

    ClusterSettings clusterSettings = ClusterSettings.builder().hosts(servers).build();
    MongoClientSettings settings = MongoClientSettings.builder().clusterSettings(clusterSettings).build();
    MongoClient mongoClient = MongoClients.create(settings);

    MongoDatabase db = mongoClient.getDatabase("chat");
    MongoCollection<Document> messagesCollection = db.getCollection("messages");

    Logger logger = LoggerFactory.getLogger(UndertowResource.class);

    Builder builder = new Builder();
    String controller = "mongo";
    String title = "Mongo title";
    String subtitle = "Mongo subtitle";

    @Override
    public void handleRequest(final HttpServerExchange exchange) throws Exception {

        try {
            final List<Chat> chatCollection = new ArrayList<Chat>();

            messagesCollection.find().forEach(document -> {
                Chat chat = new Chat();
                chat.message = document.get("message").toString();
                chat.createdAt = document.get("created_at").toString();
                chatCollection.add(chat);
            }, (result, t) -> {
                String rendered = Rocker.template("views/undetow.rocker.html", builder.builder(chatCollection, 5, 1),
                        title,
                        subtitle,
                        controller,
                        builder.paginator.currentPage,
                        builder.paginator.pageCount).render().toString();

                exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html");
                exchange.getResponseSender().send(rendered);
            });

            exchange.dispatch();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
    }
}
