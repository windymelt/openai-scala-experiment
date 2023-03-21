package com.github.windymelt.openapiscalaexperiment

import cats.effect.IOApp.Simple
import cats.effect.IO
import org.http4s.Uri

object Main extends Simple {
  import org.http4s.ember.client.EmberClientBuilder
  import org.http4s.client.Client
  import cats.implicits._
  def run: IO[Unit] = {
    // prepare client resource
    val clientResource = EmberClientBuilder.default[IO].build

    // Interpret the endpoint as a request and a response parser.
    import sttp.tapir.client.http4s.Http4sClientInterpreter
    val (req, resParser) =
      Http4sClientInterpreter[IO]()
        .toSecureRequestThrowDecodeFailures(
          OpenAI.completion,
          baseUri = Some(Uri.fromString("https://api.openai.com/").toOption.get)
        )
        .apply("TESTBEARER", "TESTORG")
        .apply(
          OpenAI.CompletionParams(
            "gpt-3.5-turbo",
            Seq(OpenAI.CompletionMessage("user", "Say this is a test!")),
            0.7
          )
        )

    // use client resource
    clientResource.use { c =>
      val fetch = c.fetch[Either[Unit, OpenAI.CompletionResult]](req)(resParser)

      fetch >>= {
        case Left(value)  => IO.println("failure")
        case Right(value) => IO.println("ok")
      }
    }
  }
}
