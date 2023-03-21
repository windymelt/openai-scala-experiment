## OpenAI & Scala 3

This code defines fundamental [OpenAI text completion API](https://platform.openai.com/docs/guides/completion)
via [Tapir](https://tapir.softwaremill.com) and make HTTP request it via [Http4s](https://http4s.org).

### Usage

```sh
OPENAI_APIKEY="..." OPENAI_ORG="..." sbt run
```

### What is Tapir?

Tapir is *Endpoint definition library*. You can define library(framework)-agnostic endpoint definition.

Tapir can define just an endpoint. So you can make use of it for both client code and server code.

See [OpenAI.scala](./src/main/scala/OpenAI.scala) for actual endpoint definition.

### What is Http4s?

Http4s is an *HTTP server/client library*, highly integrated with Cats and other functional components.
