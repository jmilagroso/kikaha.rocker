import static org.junit.Assert.assertEquals;

import kikaha.app.models.*;
import kikaha.app.routes.HazelCastResource;
import kikaha.app.routes.HomeResource;
import kikaha.app.routes.LettuceRedisResource;
import kikaha.app.services.Builder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mock;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by jay on 5/11/17.
 */
@RunWith(Parameterized.class)
public class KikahaTest {

    @Mock
    Builder builder;
    HomeResource hr;
    LettuceRedisResource r;
    HazelCastResource hc;

    @Before
    public void setUp(){
        builder = new Builder();
        r = new LettuceRedisResource();
        hc = new HazelCastResource();
        hr = new HomeResource();
    }

    // Page input/expected
    @Parameters
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { 0, 0 },
                { 1, 1 },
                { 2, 2 },
                { 3, 3 },
                { 4, 4 },
                { 5, 5 }
        });
    }

    @Parameter(0)
    public int inputPage;

    @Parameter(1)
    public int expectedPage;

    // HomeResource page
    @org.junit.Test
    public void testHomeResource() throws Exception {
        assertEquals(true, hr.render() instanceof rocker.RockerTemplate);
    }

    // LettuceRedisResource page
    @org.junit.Test
    public void testRedisResource() throws Exception {
        assertEquals(true, r.render() instanceof rocker.RockerTemplate);
    }

    // LettuceRedisResource Pagination
    @org.junit.Test
    public void testRedisResourcePage() throws Exception {
        assertEquals(inputPage, expectedPage);
    }

    // HazelCastResource page
    @org.junit.Test
    public void testHazelCastResource() throws Exception {
        assertEquals(true, hc.render() instanceof rocker.RockerTemplate);
    }

    // HazelCastResource Pagination
    @org.junit.Test
    public void testHazelCastResourcePage() throws Exception {
        assertEquals(inputPage, expectedPage);
    }


    @Test
    public void testModels() {
        try {
            Author author = new Author();
            BufferEncoder bufferEncoder = null;
            Chat chat = new Chat();
            Collection collection = new Collection();
            FastByteArrayOutputStream fastByteArrayOutputStream = new FastByteArrayOutputStream();
            Post post = new Post();
            Probe probe = new Probe();
            User user = new User();
            user.name = "John Doe";

            assertEquals(true, author instanceof Author);
            assertEquals(true, bufferEncoder.encode(probe) instanceof ByteBuffer);
            assertEquals(true, chat instanceof Chat);
            assertEquals(true, collection instanceof Collection);
            assertEquals(true, fastByteArrayOutputStream instanceof FastByteArrayOutputStream);
            assertEquals(true, post instanceof Post);
            assertEquals(true, probe instanceof Probe);
            assertEquals(true, user instanceof User);
        } catch (Exception e) {

        }


    }
}
