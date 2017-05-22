package rocker;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.fizzed.rocker.BindableRockerModel;
import com.fizzed.rocker.RockerModel;
import com.fizzed.rocker.RockerOutput;
import com.fizzed.rocker.TemplateBindException;
import com.fizzed.rocker.runtime.RockerRuntime;
import kikaha.config.Config;
import lombok.Getter;

import com.fizzed.rocker.runtime.ArrayOfByteArraysOutput;

import lombok.experimental.Accessors;


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
        final String templateName = object.getTemplateName();

        BindableRockerModel template = this.template(templateName, (Object[]) object.getObjects());
        ArrayOfByteArraysOutput output = template.render(ArrayOfByteArraysOutput.FACTORY);

        // convert to array of byte buffers
        // Reference: https://github.com/fizzed/rocker/blob/master/rocker-test-java8/src/test/java/com/fizzed/rocker/bin/UndertowMain.java
        List<byte[]> byteArrays = output.getArrays();
        int size = byteArrays.size();
        ByteBuffer[] byteBuffers = new ByteBuffer[size];
        for (int i = 0; i < size; i++) {
            byteBuffers[i] = ByteBuffer.wrap(byteArrays.get(i));
        }

        try {
            writer.write(output.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static private BindableRockerModel template(String templatePath, Object [] arguments) {
        // load model from bootstrap (which may recompile if needed)
        RockerModel model = RockerRuntime.getInstance().getBootstrap().model(templatePath);

        BindableRockerModel bindableRockerModel = new BindableRockerModel(templatePath, model.getClass().getCanonicalName(), model);

        if (arguments != null && arguments.length > 0) {
            String[] argumentNames = getModelArgumentNames(templatePath, model);

            if (arguments.length != argumentNames.length) {
                throw new TemplateBindException(templatePath, model.getClass().getCanonicalName(), "Template requires " + argumentNames.length + " arguments but " + arguments.length + " provided");
            }

            for (int i = 0; i < arguments.length; i++) {
                String name = argumentNames[i];
                Object value = arguments[i];
                bindableRockerModel.bind(name, value);
            }
        }

        return bindableRockerModel;
    }

    static private String[] getModelArgumentNames(String templatePath, RockerModel model) {
        try {
            Field f = model.getClass().getField("ARGUMENT_NAMES");
            return (String[])f.get(null);
        } catch (Exception e) {
            throw new TemplateBindException(templatePath, model.getClass().getCanonicalName(), "Unable to read ARGUMENT_NAMES static field from template");
        }
    }

}
