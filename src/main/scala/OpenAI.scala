package com.github.windymelt.openapiscalaexperiment

import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._
import sttp.model.Header
import sttp.model.MediaType
import io.circe.generic.auto._

object OpenAI {
  lazy val BasicEndpoint = endpoint
    .securityIn(auth.bearer[String]())
    .securityIn(header[String]("OpenAI-Organization"))

  lazy val completion =
    BasicEndpoint.post // Watch! you may forget to write `post` here
      .in("v1" / "chat" / "completions")
      .in(jsonBody[CompletionParams])
      .out(jsonBody[CompletionResult])
      .errorOut(stringBody)

  case class CompletionParams(
      model: String,
      messages: Seq[CompletionMessage],
      temperature: Double
  )

  case class CompletionMessage(role: String, content: String)

  case class CompletionResult(
      id: String,
      `object`: String,
      created: Long,
      model: String,
      usage: CompletionResultUsage,
      choices: Seq[CompletionResultChoice]
  )
  case class CompletionResultUsage(
      prompt_tokens: Int,
      completion_tokens: Int,
      total_tokens: Int
  )
  case class CompletionResultChoice(
      message: CompletionResultChoiceMessage,
      finish_reason: String,
      index: Int
  )
  case class CompletionResultChoiceMessage(role: String, content: String)
}
