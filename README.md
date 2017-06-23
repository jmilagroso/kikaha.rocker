# kikaha.rocker [![Build Status](https://travis-ci.org/jmilagroso/kikaha.rocker.svg?branch=master)](https://travis-ci.org/jmilagroso/kikaha.rocker) 
A Kikaha web application with Rocker template framework, Hazelcast in-memory data grid, Redis in-memory data structure store, Async HTTP client, S3 command-line tool mapping, Swagger, and JWT implementation.

#### Kikaha
[Kikaha](http://get.kikaha.io/v1.6/docs/what-is-kikaha) aims to be an absurdly fast Java web server designed for microservices. Written over [Undertow](http://undertow.io/) platform (you know, it is really fast :zap:), it was designed to be a micro container that handle high throughput demands and provide high scalability. Its internal design is clean-code-driven, because open source software should be easy to be improved. Also, we believe on the JVM communities, what they have done, the goals we already achieved and on its improvements. This make we proud to contribute to JVM, the fastest web platform ever.

#### Rocker
[Rocker](https://github.com/fizzed/rocker) is a Java 8 optimized (runtime compat with 6+), near zero-copy rendering, speedy template engine that produces statically typed, plain java object templates that are compiled along with the rest of your project. No more "warm-up" time in production, slow reflection-based logic, or nasty surprises that should have been caught during development.

### Hazelcast
[Hazelcast](https://hazelcast.org/getting-started-with-hazelcast/) is an open source in-memory data grid based on Java.

#### Redis
[Redis](https://redis.io/) is an open source (BSD licensed), in-memory data structure store, used as a database, cache and message broker.

[a-redis](http://aredis.sourceforge.net/) is a java client for the Redis Cache server designed for performance and efficiency in terms of utilization of Connections and Threads

#### Async Http Client
[Async Http Client](https://github.com/AsyncHttpClient/async-http-client) a asynchronous Http and WebSocket Client library for Java.

#### S3 Async Support
[s3cmd](https://github.com/s3tools/s3cmd) tool for Amazon Simple Storage Service (S3)

#### JWT
[jwt](https://github.com/auth0/java-jwt) Java implementation of JSON Web Token (JWT)

#### Swagger 
[Swagger](http://swagger.io/) is the world’s largest framework of API developer tools for the OpenAPI Specification(OAS), enabling development across the entire API lifecycle, from design and documentation, to test and deployment.

#### Setup Development

```sh
$ git clone git@github.com:jmilagroso/kikaha.rocker.git
$ cd kikaha.rocker
$ curl -s http://download.kikaha.io/installer | bash
$ kikaha run_app
```

#### Setup Staging/~~Production~~
```sh
$ git clone git@github.com:jmilagroso/kikaha.rocker.git
$ cd kikaha.rocker
$ mvn clean package kikaha:package
$ cd output/ && unzip kikaha.rocker-1.0.0.zip
$ cd kikaha.rocker-1.0.0
$ chmod a+x bin/kikaha.sh
$ sh bin/kikaha.sh start
```

#### Memory Adjustment
```sh
$ export KIKAHA_JVM_OPTS="-Xms2g -Xmx2g"
```

#### DataSource Connection Pool Adjustment
```sh
# resources/application.yml (http://www.vibur.org/#configuration-settings)
  ...
  datasources:
    default:
      pool-initial-size: 1000
      pool-max-size: 5000
      pool-fair: true
      ...
  ...
```

#### Examples
```sh
* Async Redis example -> kikaha.app.routes.ARedisResource
* Async HTTP Client example -> kikaha.app.routes.AsyncResource, 
                               kikaha.app.routes.WazeResource
* Hazelcast example -> kikaha.app.routes.HazelCastResource
* Rocker example -> kikaha.app.routes.HomeResource
* JWT example -> kikaha.app.routes.JWTResource
* S3 example -> kikaha.app.routes.S3Resource, 
                kikaha.app.services.S3
* Undertow and Mongo example -> kikaha.app.routes.UndertowResource
* Swagger Integration example -> kikaha.app.routes.SwaggerResource
                                 kikaha.app.routes.SwaggerUIResource
```

#### License
This code is distributed using the Apache license, Version 2.0.

#### Contributors
* [jeoffreylim](https://github.com/jeoffreylim) 
* [anaesguerra](https://github.com/anaesguerra)
