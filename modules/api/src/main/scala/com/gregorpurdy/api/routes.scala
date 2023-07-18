package com.gregorpurdy.api

import com.gregorpurdy.cli.api.endpoints
import zio.ZIO
import zio.http.App

object routes {

  private val hello = endpoints.hello.implement(_ => ZIO.succeed("Hello world!"))

  private val helloYou = endpoints.helloYou.implement(name => ZIO.succeed(s"Hello $name"))

  val app: App[Any] = (hello ++ helloYou).toApp

}
