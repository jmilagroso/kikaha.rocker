package views.common;

import java.io.IOException;
import com.fizzed.rocker.ForIterator;
import com.fizzed.rocker.RenderingException;
import com.fizzed.rocker.RockerContent;
import com.fizzed.rocker.RockerOutput;
import com.fizzed.rocker.runtime.DefaultRockerTemplate;
import com.fizzed.rocker.runtime.PlainTextUnloadedClassLoader;

/*
 * Auto generated code to render template views/common/main.rocker.html
 * Do not edit this file. Changes will eventually be overwritten by Rocker parser!
 */
public class main extends com.fizzed.rocker.runtime.DefaultRockerModel {

    static public final com.fizzed.rocker.ContentType CONTENT_TYPE = com.fizzed.rocker.ContentType.HTML;
    static public final String TEMPLATE_NAME = "main.rocker.html";
    static public final String TEMPLATE_PACKAGE_NAME = "views.common";
    static public final String HEADER_HASH = "-635070772";
    static public final long MODIFIED_AT = 1494386898000L;
    static public final String[] ARGUMENT_NAMES = { "title" };

    // argument @ [1:2]
    private String title;
    // argument @ [1:2]
    private RockerContent content;

    public main title(String title) {
        this.title = title;
        return this;
    }

    public String title() {
        return this.title;
    }

    public main __body(RockerContent content) {
        this.content = content;
        return this;
    }

    public RockerContent __body() {
        return this.content;
    }

    static public main template(String title) {
        return new main()
            .title(title);
    }

    @Override
    protected DefaultRockerTemplate buildTemplate() throws RenderingException {
        // optimized for convenience (runtime auto reloading enabled if rocker.reloading=true)
        return com.fizzed.rocker.runtime.RockerRuntime.getInstance().getBootstrap().template(this.getClass(), this);
    }

    static public class Template extends com.fizzed.rocker.runtime.DefaultRockerTemplate {

        // <!DOCTYPE html>\n<html lang=\"en\">\n<head>\n    <meta charset=\"utf-8\">\n    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->\n    <meta name=\"description\" content=\"\">\n    <meta name=\"author\" content=\"\">\n\n    <title>
        static private final byte[] PLAIN_TEXT_0_0;
        // </title>\n\n    <!-- Bootstrap core CSS -->\n    <link rel=\"stylesheet\"\n          href=\"/assets/css/bootstrap.min.css\">\n\n    <!-- Custom styles for this template -->\n    <link href=\"/assets/css/starter-template.css\" rel=\"stylesheet\">\n\n</head>\n\n<body>\n\n<nav class=\"navbar navbar-inverse navbar-fixed-top\">\n    <div class=\"container\">\n        <div class=\"navbar-header\">\n            <button type=\"button\" class=\"navbar-toggle collapsed\" data-toggle=\"collapse\" data-t...
        static private final byte[] PLAIN_TEXT_1_0;
        // \n    </div>\n</div><!-- /.container -->\n\n\n<!-- Bootstrap core JavaScript\n================================================== -->\n<!-- Placed at the end of the document so the pages load faster -->\n<script src=\"/assets/js/jquery.min.js\"></script>\n</body>\n</html>\n
        static private final byte[] PLAIN_TEXT_2_0;

        static {
            PlainTextUnloadedClassLoader loader = PlainTextUnloadedClassLoader.tryLoad(main.class.getClassLoader(), main.class.getName() + "$PlainText", "UTF-8");
            PLAIN_TEXT_0_0 = loader.tryGet("PLAIN_TEXT_0_0");
            PLAIN_TEXT_1_0 = loader.tryGet("PLAIN_TEXT_1_0");
            PLAIN_TEXT_2_0 = loader.tryGet("PLAIN_TEXT_2_0");
        }

        // argument @ [1:2]
        protected final String title;
        // argument @ [1:2]
        protected final RockerContent content;

        public Template(main model) {
            super(model);
            __internal.setCharset("UTF-8");
            __internal.setContentType(CONTENT_TYPE);
            __internal.setTemplateName(TEMPLATE_NAME);
            __internal.setTemplatePackageName(TEMPLATE_PACKAGE_NAME);
            this.title = model.title();
            this.content = model.__body();
        }

        @Override
        protected void __doRender() throws IOException, RenderingException {
            // PlainText @ [1:41]
            __internal.aboutToExecutePosInTemplate(1, 41);
            __internal.writeValue(PLAIN_TEXT_0_0);
            // ValueExpression @ [13:12]
            __internal.aboutToExecutePosInTemplate(13, 12);
            __internal.renderValue(title, false);
            // PlainText @ [13:18]
            __internal.aboutToExecutePosInTemplate(13, 18);
            __internal.writeValue(PLAIN_TEXT_1_0);
            // ValueExpression @ [58:9]
            __internal.aboutToExecutePosInTemplate(58, 9);
            __internal.renderValue(content, false);
            // PlainText @ [58:17]
            __internal.aboutToExecutePosInTemplate(58, 17);
            __internal.writeValue(PLAIN_TEXT_2_0);
        }
    }

    private static class PlainText {

        static private final String PLAIN_TEXT_0_0 = "<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n    <meta charset=\"utf-8\">\n    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->\n    <meta name=\"description\" content=\"\">\n    <meta name=\"author\" content=\"\">\n\n    <title>";
        static private final String PLAIN_TEXT_1_0 = "</title>\n\n    <!-- Bootstrap core CSS -->\n    <link rel=\"stylesheet\"\n          href=\"/assets/css/bootstrap.min.css\">\n\n    <!-- Custom styles for this template -->\n    <link href=\"/assets/css/starter-template.css\" rel=\"stylesheet\">\n\n</head>\n\n<body>\n\n<nav class=\"navbar navbar-inverse navbar-fixed-top\">\n    <div class=\"container\">\n        <div class=\"navbar-header\">\n            <button type=\"button\" class=\"navbar-toggle collapsed\" data-toggle=\"collapse\" data-target=\"#navbar\" aria-expanded=\"false\" aria-controls=\"navbar\">\n                <span class=\"sr-only\">Toggle navigation</span>\n                <span class=\"icon-bar\"></span>\n                <span class=\"icon-bar\"></span>\n                <span class=\"icon-bar\"></span>\n            </button>\n            <a class=\"navbar-brand\" href=\"http://get.kikaha.io/\">Kikaha</a>\n        </div>\n        <div id=\"navbar\" class=\"collapse navbar-collapse\">\n            <ul class=\"nav navbar-nav\">\n                <li class=\"active\"><a href=\"/\">Home</a></li>\n                <li><a href=\"/hazelcast\">HazelCast</a></li>\n                <li><a href=\"/redis\">Redis</a></li>\n                <li><a href=\"/fastpfor\">FastPFor (JSON output BufferEncoder)</a></li>\n            </ul>\n        </div><!--/.nav-collapse -->\n    </div>\n</nav>\n\n<div class=\"container\">\n    <div class=\"row\"></div>\n    <div class=\"starter-template\">\n        <h4>Kikaka (<a href=\"https://github.com/Skullabs/kikaha\">https://github.com/Skullabs/kikaha</a>)</h4>\n        <h4>Rocker (<a href=\"https://github.com/fizzed/rocker\">https://github.com/fizzed/rocker</a>)</h4>\n        <h4>HazelCast (<a href=\"http://hazelcast.org/\">http://hazelcast.org/</a>)</h4>\n        <h4>Redis (<a href=\"https://redis.io/\">https://redis.io/</a>)</h4>\n        <h4>Jedis (<a href=\"https://github.com/xetorthio/jedis\">https://github.com/xetorthio/jedis</a>)<h4>\n        <h4>FastPFor (<a href=\"https://github.com/lemire/FastPFor\">https://github.com/lemire/FastPFor</a>)<h4>\n\n        ";
        static private final String PLAIN_TEXT_2_0 = "\n    </div>\n</div><!-- /.container -->\n\n\n<!-- Bootstrap core JavaScript\n================================================== -->\n<!-- Placed at the end of the document so the pages load faster -->\n<script src=\"/assets/js/jquery.min.js\"></script>\n</body>\n</html>\n";

    }

}
