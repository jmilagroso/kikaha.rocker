package myapp.services;

import javax.inject.*;
import java.util.concurrent.atomic.*;

import com.hazelcast.core.*;

@Singleton
public class SampleService {

    @Inject
    @Named( "atomic-booleans" )
    IMap<Long, AtomicBoolean> map;

    @Inject
    @Named( "atomic-booleans" )
    MultiMap<Long, AtomicBoolean> multimap;

    @Inject
    @Named( "atomic-booleans" )
    IQueue<AtomicBoolean> queue;

    @Inject
    @Named( "atomic-booleans" )
    ISet<AtomicBoolean> set;

    @Inject
    @Named( "atomic-booleans" )
    IList<AtomicBoolean> list;

    @Inject
    @Named( "atomic-booleans" )
    ITopic<AtomicBoolean> topic;

    @Inject
    @Named( "atomic-booleans" )
    ReplicatedMap<Long,AtomicBoolean> replicatedMap;

    @Inject
    @Named( "executor-service" )
    IExecutorService executorService;

    @Inject
    @Named( "ilock" )
    ILock lock;

    @Inject
    @Named( "id-generator" )
    IdGenerator idGenerator;
}