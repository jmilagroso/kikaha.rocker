package rocker;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.fizzed.rocker.BindableRockerModel;
import com.fizzed.rocker.Rocker;
import com.fizzed.rocker.RockerModel;
import com.fizzed.rocker.TemplateBindException;
import com.fizzed.rocker.runtime.RockerRuntime;
import kikaha.config.Config;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.Delegate;

//import com.github.mustachejava.DefaultMustacheFactory;
//import com.github.mustachejava.Mustache;
//import com.github.mustachejava.MustacheFactory;

@Singleton
@Getter
@Accessors( fluent = true )
public class RockerSerializer {
    private boolean shouldCacheTemplates;

    @Inject
    Config config;

    @PostConstruct
    public void readConfiguration() {
        shouldCacheTemplates = config.getBoolean( "server.rocker.cache-templates" );
    }

    public String serialize( final RockerTemplate object ) {
        final Writer writer = new StringWriter();

        serialize( object, writer );
        return writer.toString();
    }

    public void serialize( final RockerTemplate object, final Writer writer ) {
        final String templateName = object.templateName();
        /*final Mustache compiled = mustacheFactory.compile( formatFileName( templateName ) );
        compiled.execute( writer, object.paramObject() );*/

        String rendered = Rocker.template(templateName, object.paramContent())
                .render()
                .toString();
        try {
            writer.write(rendered);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
