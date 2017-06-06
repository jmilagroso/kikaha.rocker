import static org.junit.Assert.assertEquals;

import kikaha.app.routes.ARedisResource;
import kikaha.app.routes.HazelCastResource;
import kikaha.app.routes.JWTResource;
import kikaha.app.services.Builder;
import kikaha.app.services.Paginator;
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
    ARedisResource ar;
    Builder builder;
    HazelCastResource hc;
    Paginator paginator;
    JWTResource jwt;
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp(){
        ar = mock(ARedisResource.class);
        builder = mock(Builder.class);
        hc = mock(HazelCastResource.class);
        jwt = mock(JWTResource.class);
        paginator = mock(Paginator.class);
    }

    // HazelCastResource page
    @org.junit.Test
    public void testHazelCastResource() throws Exception {
        hc.render();
        verify(hc).render();
    }

    // JWTResource page
    @org.junit.Test
    public void testJWTResource() throws Exception {
        jwt.render("1", "John Doe", "MyAPP");
        verify(jwt).render("1", "John Doe", "MyAPP");
    }

    // Builder class
    @org.junit.Test
    public void testBuilderClass() {
        // verify interactions
        builder.builder(anyList(), anyInt(), anyInt());
        verify(builder).builder(anyList(), anyInt(), anyInt());
    }

    // Paginator class
    @org.junit.Test
    public void testPaginatorClass() {
        // verify interactions
        paginator.handle(anyInt(), anyInt(), anyInt());
        verify(paginator).handle(anyInt(), anyInt(), anyInt());
    }

}
