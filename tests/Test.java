import static org.junit.Assert.assertEquals;

import myapp.routes.HazelCastResource;
import myapp.routes.RedisResource;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;

/**
 * Created by jay on 5/11/17.
 */
@RunWith(Parameterized.class)
public class Test {
    RedisResource r = new RedisResource();
    HazelCastResource hc = new HazelCastResource();

    // Page input/expected
    @Parameters
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] { { 0, 0 }, { 1, 1 }, { 2, 2 },
                { 3, 3 }, { 4, 4 }, { 5, 5 }, { 6, 6 } });
    }

    @Parameter(0)
    public int inputPage;

    @Parameter(1)
    public int expectedPage;


    // RedisResource page
    @org.junit.Test
    public void testRedisResource() throws Exception {
        assertEquals(r.renderBot() instanceof rocker.RockerTemplate, true);
    }

    // RedisResource Pagination
    @org.junit.Test
    public void testRedisResourcePage() throws Exception {
        assertEquals(inputPage, expectedPage);
    }

    // HazelCastResource page
    @org.junit.Test
    public void testHazelCastResource() throws Exception {
        assertEquals(hc.renderBot()  instanceof rocker.RockerTemplate, true);
    }

    // HazelCastResource Pagination
    @org.junit.Test
    public void testHazelCastResourcePage() throws Exception {
        assertEquals(inputPage, expectedPage);
    }

}
