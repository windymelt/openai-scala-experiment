package com.github.windymelt.openapiscalaexperiment

import cats.data.Kleisli
import cats.effect.IO
import cats.effect.IOApp.Simple
import cats.implicits._
import org.http4s.Uri
import org.http4s.client.Client
import org.http4s.ember.client.EmberClientBuilder
import sttp.tapir.DecodeResult

object Main extends Simple {
  def run: IO[Unit] = {
    // Interpret the tapir endpoint as a http4s request and a response parser.
    import sttp.tapir.client.http4s.Http4sClientInterpreter
    val (req, resParser) =
      Http4sClientInterpreter[IO]()
        .toSecureRequest(
          OpenAI.completion,
          baseUri = Some(Uri.fromString("https://api.openai.com/").toOption.get)
        )
        .apply(
          sys.env.get("OPENAI_APIKEY").getOrElse(""),
          sys.env.get("OPENAI_ORG").getOrElse("")
        )
        .apply(
          OpenAI.CompletionParams(
            "gpt-3.5-turbo",
            Seq(
              OpenAI.CompletionMessage(
                "user",
                "あなたはChatGPTのAPIです。あなたにAPIを介して初めてアクセスに成功したユーザに対して、数センテンスで歓迎の挨拶を述べてください。"
              )
            ),
            0.7
          )
        )
    // https://stackoverflow.com/questions/49649453/how-to-log-all-requests-for-an-http4s-client
    // thank you guys
    // prepare client resource
    val httpClientResource = EmberClientBuilder.default[IO].build
    import org.http4s.client.middleware.Logger
    val liftClientToLog: Client[IO] => Client[IO] =
      Logger(logBody = true, logHeaders = true)(_)
    val loggingClientResource = sys.env.get("DEBUG").getOrElse("") match {
      case "1" => httpClientResource.map(liftClientToLog)
      case _   => httpClientResource
    }

    val requesting: Client[IO] => IO[
      DecodeResult[Either[String, OpenAI.CompletionResult]]
    ] = _.run(req).use(resParser)

    val show
        : DecodeResult[Either[String, OpenAI.CompletionResult]] => IO[Unit] = {
      import sttp.tapir.DecodeResult.Value
      import sttp.tapir.DecodeResult.Missing
      import sttp.tapir.DecodeResult.Multiple
      import sttp.tapir.DecodeResult.Mismatch
      import sttp.tapir.DecodeResult.InvalidValue
      _ match {
        case Value(Right(v)) => IO.println(v.choices.head.message.content)
        case Value(Left(_))  => IO.println("access failure")
        case Missing         => IO.println(s"missing")
        case Multiple(vs)    => IO.println(s"mul $vs")
        case sttp.tapir.DecodeResult.Error(original, error) =>
          IO.println(s"orig: ${original}, err: ${error.getMessage()}")
        case Mismatch(expected, actual) =>
          IO.println(s"missmatch: exp: $expected, act: $actual")
        case InvalidValue(errors) => IO.println(s"invalid: $errors")
      }
    }
    // use client resource
    loggingClientResource.use(
      (Kleisli(requesting) >>> Kleisli(show)).run
    )
  }
}
