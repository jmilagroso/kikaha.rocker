# kikaha.rocker [![Build Status](https://travis-ci.org/jmilagroso/kikaha.rocker.svg?branch=master)](https://travis-ci.org/jmilagroso/kikaha.rocker) 
Integrates Kikaha with Rocker. 

## Kikaha
[Kikaha](http://get.kikaha.io/v1.6/docs/what-is-kikaha) aims to be an absurdly fast Java web server designed for microservices. Written over [Undertow](http://undertow.io/) platform (you know, it is really fast), it was designed to be a micro container that handle high throughput demands and provide high scalability. Its internal design is clean-code-driven, because open source software should be easy to be improved. Also, we believe on the JVM communities, what they have done, the goals we already achieved and on its improvements. This make we proud to contribute to JVM, the fastest web platform ever.

## Rocker
[Rocker](https://github.com/fizzed/rocker) is a Java 8 optimized (runtime compat with 6+), near zero-copy rendering, speedy template engine that produces statically typed, plain java object templates that are compiled along with the rest of your project. No more "warm-up" time in production, slow reflection-based logic, or nasty surprises that should have been caught during development.

## Hazelcast
[Hazelcast](https://hazelcast.org/getting-started-with-hazelcast/) is an open source in-memory data grid based on Java.

## Redis
[Redis](https://redis.io/) is an open source (BSD licensed), in-memory data structure store, used as a database, cache and message broker.

[a-redis](http://aredis.sourceforge.net/) is a java client for the Redis Cache server designed for performance and efficiency in terms of utilization of Connections and Threads

## Async Http Client
[Async Http Client](https://github.com/AsyncHttpClient/async-http-client) a asynchronous Http and WebSocket Client library for Java.

## S3 Async Support
[s3cmd](https://github.com/s3tools/s3cmd) S3cmd tool for Amazon Simple Storage Service (S3)

## JWT
[jwt](https://github.com/auth0/java-jwt) Java implementation of JSON Web Token (JWT)

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
#### Examples
```sh
* Async Redis example -> kikaha.app.routes.ARedisResource
* Async HTTP Client example -> kikaha.app.routes.AsyncResource
* Hazelcast example -> kikaha.app.routes.HazelCastResource
* Rocker example -> kikaha.app.routes.HomeResource
* JWT example -> kikaha.app.routes.JWTResource
* S3 example -> kikaha.app.routes.S3Resource, kikaha.app.services.S3
* Undertow and Mongo example -> kikaha.app.routes.UndertowResource
```


#### License
This code is distributed using the Apache license, Version 2.0.

#### Contributors
* [jeoffreylim](https://github.com/jeoffreylim) 
* [anaesguerra](https://github.com/anaesguerra)
