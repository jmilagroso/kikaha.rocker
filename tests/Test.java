import static org.junit.Assert.assertEquals;

import myapp.models.Post;
import myapp.routes.HazelCastResource;
import myapp.routes.RedisResource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jay on 5/11/17.
 */
public class Test {
    RedisResource r = new RedisResource();
    HazelCastResource hc = new HazelCastResource();

    @org.junit.Test
    public void testRedisResource() throws Exception {
        assertEquals(r.renderBot() instanceof rocker.RockerTemplate, true);
    }

    @org.junit.Test
    public void testRedisResourcePage() throws Exception {
        assertEquals(r.renderBotWithPage(1) instanceof rocker.RockerTemplate, true);
    }

    @org.junit.Test
    public void testHazelCastResource() throws Exception {
        assertEquals(hc.renderBot()  instanceof rocker.RockerTemplate, true);
    }

    @org.junit.Test
    public void testHazelCastResourcePage() throws Exception {
        assertEquals(hc.renderBotWithPage(1)  instanceof rocker.RockerTemplate, true);
    }

}
