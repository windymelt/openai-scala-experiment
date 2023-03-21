package com.github.windymelt.openapiscalaexperiment

import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._
import io.circe.generic.auto._

object OpenAI {
  type APIKey = String
  val bearer = auth.bearer[String]()
  lazy val BasicEndpoint = endpoint
    .securityIn(
      bearer
    )
    .securityIn(header[String]("OpenAI-Organization"))

  lazy val completion = BasicEndpoint
    .in("v1" / "chat" / "completions")
    .in(jsonBody[CompletionParams])
    .out(jsonBody[CompletionResult])

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
      promptTokens: Int,
      completionTokens: Int,
      totalTokens: Int
  )
  case class CompletionResultChoice(
      message: CompletionResultChoiceMessage,
      finishReason: String,
      index: Int
  )
  case class CompletionResultChoiceMessage(role: String, content: String)
}
