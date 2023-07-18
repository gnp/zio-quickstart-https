package com.gregorpurdy.cli

import com.gregorpurdy.cli.api.endpoints.*
import zio.cli.HelpDoc.Span
import zio.cli.{CliApp, HelpDoc, ZIOCliDefault}
import zio.http.endpoint.cli.*
import zio.{Chunk, Scope, ZIOAppArgs}

object MyCLI extends ZIOCliDefault {

  override def cliApp: CliApp[Any & ZIOAppArgs & Scope, Any, Any] =
    HttpCliApp
      .fromEndpoints(
        name = "mycli",
        version = CliBuildInfo.version,
        summary = Span.text("Hello There :)"),
        footer = HelpDoc.p("Ceci n'est pas une pipe"),
        host = "localhost",
        port = 8080,
        endpoints = Chunk(
          hello,
          helloYou,
        ),
      )
      .cliApp
}
