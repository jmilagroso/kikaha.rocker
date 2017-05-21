package rocker;

import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;

@Slf4j
@Singleton
public class RockerSerializerFactory {

    @Inject
    RockerSerializer serializer;

    public RockerSerializer serializer() {
        return serializer;
    }
}