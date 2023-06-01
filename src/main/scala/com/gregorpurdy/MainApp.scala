package com.gregorpurdy

import zio.http._
import zio._

import java.io.IOException

object MainApp extends ZIOAppDefault {

  val Port = 8080

  val pingApp: HttpApp[Any, Nothing] = Http.collectZIO[Request] { case Method.GET -> Root / "ping" =>
    ZIO.succeed(Response.text("pong"))
  }

  val sslConfig = SSLConfig.generate(
    behaviour = SSLConfig.HttpBehaviour.Accept
  )

  val configLayer = ZLayer.succeed(
    Server.Config.default
      .port(Port)
      .ssl(sslConfig)
  )

  override def run = for {
    _ <- ZIO.log("Starting server...")
    _ <- Server
      .serve(pingApp)
      .provide(configLayer, Server.live)
  } yield ()

}
