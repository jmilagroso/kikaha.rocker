# kikaha.rocker
Integrates Kikaha with Rocker. 

## Kikaha
[Kikaha](http://get.kikaha.io/v1.6/docs/what-is-kikaha) aims to be an absurdly fast Java web server designed for microservices. Written over [Undertow](http://undertow.io/) platform (you know, it is really fast), it was designed to be a micro container that handle high throughput demands and provide high scalability. Its internal design is clean-code-driven, because open source software should be easy to be improved. Also, we believe on the JVM communities, what they have done, the goals we already achieved and on its improvements. This make we proud to contribute to JVM, the fastest web platform ever.

## Rocker
[Rocker](https://github.com/fizzed/rocker) is a Java 8 optimized (runtime compat with 6+), near zero-copy rendering, speedy template engine that produces statically typed, plain java object templates that are compiled along with the rest of your project. No more "warm-up" time in production, slow reflection-based logic, or nasty surprises that should have been caught during development.

### Setup
1. $ git clone git@github.com:jmilagroso/kikaha.rocker.git
2. $ cd kikaha.rocker
3. $ curl -s http://download.kikaha.io/installer | bash
4. $ kikaha run_app
5. Open http://localhost:9000
