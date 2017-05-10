package myapp.models;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by jay on 5/9/17.
 */

// https://github.com/spinn3r/artemis-framework/blob/b1d5fdb3d8398431fe043d025d52170472a69625/artemis-json/src/test/java/com/spinn3r/artemis/json/JacksonFooEncoder.java

public class BufferEncoder {
    private static final JsonFactory JSON_FACTORY = new JsonFactory();

    private final FastByteArrayOutputStream fastByteArrayOutputStream;


    public BufferEncoder(FastByteArrayOutputStream fastByteArrayOutputStream) {
        this.fastByteArrayOutputStream = fastByteArrayOutputStream;
    }

    public ByteBuffer encode(List<Probe> probes) throws IOException {

        try (JsonGenerator generator = JSON_FACTORY.createGenerator( fastByteArrayOutputStream, JsonEncoding.UTF8 ); ) {
            generator.writeStartArray();
            generator.enable(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT);
            generator.enable( JsonGenerator.Feature.AUTO_CLOSE_TARGET );

            for(Probe probe: probes) {
                generator.writeStartObject();
                if (probe.getSex() != 0) {
                    generator.writeNumberField("sex", probe.getSex());
                }

                if (probe.getAge() != 0) {
                    generator.writeNumberField("age", probe.getAge());
                }

                if (probe.getX() != 0) {
                    generator.writeNumberField("x", probe.getX());
                }

                if (probe.getY() != 0) {
                    generator.writeNumberField("y", probe.getY());
                }

                if (probe.getLat() != 0) {
                    generator.writeNumberField("lat", probe.getLat());
                }

                if (probe.getLng() != 0) {
                    generator.writeNumberField("lng", probe.getLng());
                }

                if (probe.getAssociated() != 0) {
                    generator.writeNumberField("associated", probe.getAssociated());
                }
                generator.writeEndObject();
            }
            generator.writeEndArray();
        }

        return fastByteArrayOutputStream.toByteBuffer();

    }

    public ByteBuffer encode(Probe probe) throws IOException {

        try (JsonGenerator generator = JSON_FACTORY.createGenerator( fastByteArrayOutputStream, JsonEncoding.UTF8 ); ) {

            generator.disable( JsonGenerator.Feature.AUTO_CLOSE_TARGET );

            generator.writeStartObject();

            if (probe.getSex() != 0) {
                generator.writeNumberField("sex", probe.getSex());
            }

            if (probe.getAge() != 0) {
                generator.writeNumberField("age", probe.getAge());
            }

            if (probe.getX() != 0) {
                generator.writeNumberField("x", probe.getX());
            }

            if (probe.getY() != 0) {
                generator.writeNumberField("y", probe.getY());
            }

            if (probe.getLat() != 0) {
                generator.writeNumberField("lat", probe.getLat());
            }

            if (probe.getLng() != 0) {
                generator.writeNumberField("lng", probe.getLng());
            }

            if (probe.getAssociated() != 0) {
                generator.writeNumberField("associated", probe.getAssociated());
            }

            generator.writeEndObject();

        }

        return fastByteArrayOutputStream.toByteBuffer();

    }
}
