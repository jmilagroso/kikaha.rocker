# kikaha.rocker
Integrates Kikaha with Rocker. 

## Kikaha
[Kikaha](http://get.kikaha.io/v1.6/docs/what-is-kikaha) aims to be an absurdly fast Java web server designed for microservices. Written over [Undertow](http://undertow.io/) platform (you know, it is really fast), it was designed to be a micro container that handle high throughput demands and provide high scalability. Its internal design is clean-code-driven, because open source software should be easy to be improved. Also, we believe on the JVM communities, what they have done, the goals we already achieved and on its improvements. This make we proud to contribute to JVM, the fastest web platform ever.

## Rocker
[Rocker](https://github.com/fizzed/rocker) is a Java 8 optimized (runtime compat with 6+), near zero-copy rendering, speedy template engine that produces statically typed, plain java object templates that are compiled along with the rest of your project. No more "warm-up" time in production, slow reflection-based logic, or nasty surprises that should have been caught during development.

## Hazelcast
[Hazelcast](https://hazelcast.org/getting-started-with-hazelcast/) is an open source in-memory data grid based on Java.

## Redis
[Redis](https://redis.io/) is an open source (BSD licensed), in-memory data structure store, used as a database, cache and message broker.
[Jedis](https://github.com/xetorthio/jedis) a blazingly small and sane redis java client.


### Setup
```sh
$ git clone git@github.com:jmilagroso/kikaha.rocker.git
$ cd kikaha.rocker
$ curl -s http://download.kikaha.io/installer | bash
$ kikaha run_app
```

### Memory Adjustment
```sh
$ export KIKAHA_JVM_OPTS="-Xms2g -Xmx2g"
```
### URL
Open 
- http://localhost:9000
- http://localhost:9000/hazelcast/
- http://localhost:9000/redis/
- http://localhost:9000/fastpfor/
- http://localhost:9000/undertow/



Special thanks to [jeoffreylim](https://github.com/jeoffreylim) [anaesguerra](https://github.com/anaesguerra)
