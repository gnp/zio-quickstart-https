package com.gregorpurdy.cli.api

import zio.ZNothing
import zio.http.codec.HttpCodec.*
import zio.http.endpoint.{Endpoint, EndpointMiddleware}

object endpoints {

  val hello: Endpoint[Unit, ZNothing, String, EndpointMiddleware.None] =
    Endpoint.get(path = "hello").out[String]

  val helloYou: Endpoint[String, ZNothing, String, EndpointMiddleware.None] =
    Endpoint.get(path = "hello" / string("name")).out[String]

}
