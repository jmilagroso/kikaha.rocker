# kikaha.rocker [![Build Status](https://travis-ci.org/jmilagroso/kikaha.rocker.svg?branch=master)](https://travis-ci.org/jmilagroso/kikaha.rocker) [![CII Best Practices](https://bestpractices.coreinfrastructure.org/projects/970/badge)](https://bestpractices.coreinfrastructure.org/projects/970) 
Integrates Kikaha with Rocker. 

## Kikaha
[Kikaha](http://get.kikaha.io/v1.6/docs/what-is-kikaha) aims to be an absurdly fast Java web server designed for microservices. Written over [Undertow](http://undertow.io/) platform (you know, it is really fast), it was designed to be a micro container that handle high throughput demands and provide high scalability. Its internal design is clean-code-driven, because open source software should be easy to be improved. Also, we believe on the JVM communities, what they have done, the goals we already achieved and on its improvements. This make we proud to contribute to JVM, the fastest web platform ever.

## Rocker
[Rocker](https://github.com/fizzed/rocker) is a Java 8 optimized (runtime compat with 6+), near zero-copy rendering, speedy template engine that produces statically typed, plain java object templates that are compiled along with the rest of your project. No more "warm-up" time in production, slow reflection-based logic, or nasty surprises that should have been caught during development.

## Hazelcast
[Hazelcast](https://hazelcast.org/getting-started-with-hazelcast/) is an open source in-memory data grid based on Java.

## Redis
[Redis](https://redis.io/) is an open source (BSD licensed), in-memory data structure store, used as a database, cache and message broker.
[lettuce-core](https://github.com/lettuce-io/lettuce-core) advanced Redis client for thread-safe sync, async, and reactive usage. Supports Cluster, Sentinel, Pipelining, and codecs.

## Async Http Client
[Async Http Client](https://github.com/AsyncHttpClient/async-http-client) a asynchronous Http and WebSocket Client library for Java.

### Setup

```sh
$ git clone git@github.com:jmilagroso/kikaha.rocker.git
$ cd kikaha.rocker
$ curl -s http://download.kikaha.io/installer | bash
$ kikaha run_app
```

#### Memory Adjustment
```sh
$ export KIKAHA_JVM_OPTS="-Xms2g -Xmx2g"
```
#### URL
Open 
- [Home Resource](http://localhost:9000)
- [HazelCast Resource](http://localhost:9000/hazelcast/)
- [Redis Resource](http://localhost:9000/redis/)
- [Undertow Resource with MongoDB](http://localhost:9000/undertow/)
- [Async Http Client](http://localhost:9000/async)

#### License
This code is distributed using the Apache license, Version 2.0.

#### Contributors
[jeoffreylim](https://github.com/jeoffreylim) 
[anaesguerra](https://github.com/anaesguerra)
