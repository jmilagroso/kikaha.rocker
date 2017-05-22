import static org.junit.Assert.assertEquals;

import myapp.routes.HazelCastResource;
import myapp.routes.RedisResource;
import myapp.services.Builder;
import myapp.services.Paginator;
import org.junit.Before;
import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

/**
 * Created by jay on 5/11/17.
 */

public class KikahaMock {

    @Mock
    Builder builder;
    RedisResource r;
    HazelCastResource hc;
    Paginator paginator;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp(){
        builder = mock(Builder.class);
        r = mock(RedisResource.class);
        hc = mock(HazelCastResource.class);
        paginator = mock(Paginator.class);
    }

    // RedisResource page
    @org.junit.Test
    public void testRedisResource() throws Exception {
        r.render();
        verify(r).render();
    }

    // HazelCastResource page
    @org.junit.Test
    public void testHazelCastResource() throws Exception {
        hc.render();
        verify(hc).render();
    }

    @org.junit.Test
    public void testBuilderClass() {
        // verify interactions
        builder.builder(anyList(), anyInt(), anyInt());
        verify(builder).builder(anyList(), anyInt(), anyInt());
    }

    @org.junit.Test
    public void testPaginatorClass() {
        // verify interactions
        paginator.handle(anyInt(), anyInt(), anyInt());
        verify(paginator).handle(anyInt(), anyInt(), anyInt());
    }

}
