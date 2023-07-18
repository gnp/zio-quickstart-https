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

package com.gregorpurdy.api

import zio.*
import zio.http.*
import zio.logging.backend.SLF4J

object MainApp extends ZIOAppDefault {

  /**
   * See `zio-logging` documentation: https://zio.github.io/zio-logging/docs/overview/overview_slf4j
   */
  override val bootstrap: ZLayer[ZIOAppArgs, Any, Any] =
    zio.Runtime.removeDefaultLoggers >>> SLF4J.slf4j

  val Port = 8080

  val pingApp: HttpApp[Any, Nothing] = Http.collectZIO[Request] { case Method.GET -> Root / "ping" =>
    ZIO.succeed(Response.text("pong"))
  }

  // val sslConfig = SSLConfig.generate(
  //   behaviour = SSLConfig.HttpBehaviour.Accept
  // )

  val sslConfig: SSLConfig = SSLConfig.fromResource(
    behaviour = SSLConfig.HttpBehaviour.Accept,
    certPath = "server.crt",
    keyPath = "server.key",
  )

  val configLayer: ULayer[Server.Config] = ZLayer.succeed(
    Server.Config.default
      .port(Port)
      // .ssl(sslConfig)
  )

  @annotation.nowarn("msg=dead code following this construct")
  override def run: ZIO[Environment with ZIOAppArgs with Scope, Any, Any] = for {
    _ <- ZIO.log("Starting server...")
    _ <- Server
           .serve(pingApp ++ routes.app)
           .provide(configLayer, Server.live)
  } yield ()

}
