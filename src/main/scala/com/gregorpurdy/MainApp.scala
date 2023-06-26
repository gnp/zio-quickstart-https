/*
 * Copyright 2023 Gregor Purdy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gregorpurdy

import zio._
import zio.http._

object MainApp extends ZIOAppDefault {

  val Port = 8080

  val pingApp: HttpApp[Any, Nothing] = Http.collectZIO[Request] { case Method.GET -> Root / "ping" =>
    ZIO.succeed(Response.text("pong"))
  }

  // val sslConfig = SSLConfig.generate(
  //   behaviour = SSLConfig.HttpBehaviour.Accept
  // )

  val sslConfig = SSLConfig.fromResource(
    behaviour = SSLConfig.HttpBehaviour.Accept,
    certPath = "server.crt",
    keyPath = "server.key"
  )

  val configLayer = ZLayer.succeed(
    Server.Config.default
      .port(Port)
      .ssl(sslConfig)
  )

  @annotation.nowarn("msg=dead code following this construct")
  override def run = for {
    _ <- ZIO.log("Starting server...")
    _ <- Server
      .serve(pingApp)
      .provide(configLayer, Server.live)
  } yield ()

}
