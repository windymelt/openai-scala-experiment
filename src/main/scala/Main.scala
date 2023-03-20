package com.github.windymelt.openapiscalaexperiment

import cats.effect.IOApp.Simple
import cats.effect.IO

object Main extends Simple {
  import org.http4s.ember.client.EmberClientBuilder
  import org.http4s.client.Client
  import cats.implicits._
  def run: IO[Unit] = {
    // prepare client resource
    val clientResource = EmberClientBuilder.default[IO].build

    // use client resource
    clientResource.use { c =>
      // expect returns IO[String] and we pass it to IO.println
      c.expect[String]("https://www.3qe.us/") >>= IO.println
    }
  }
}
