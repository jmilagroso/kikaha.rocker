package rocker;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors( fluent = true )
@NoArgsConstructor
public class RockerTemplate {

    @NonNull
    String templateName;

    @NonNull
    Object objects;

    public RockerTemplate setObjects(Object ... args) {
        this.objects = args;

        return this;
    }

    public RockerTemplate setTemplateName(String name) {
        this.templateName = name;

        return this;
    }
}