package com.github.windymelt.openapiscalaexperiment

import cats.effect.IOApp.Simple
import cats.effect.IO

object Main extends Simple {
  def run: IO[Unit] = IO.println("IO App")
}
