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
    Object paramContent;

    public RockerTemplate setParamContent(Object ... args) {
        this.paramContent = args;

        return this;
    }

}