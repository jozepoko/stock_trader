package jozepoko.stock_trader.core.infrastructure.http.enum

object MethodEnum {
  object GET extends Method("GET")
  object POST extends Method("POST")
  object PUT extends Method("PUT")
  object DELETE extends Method("DELETE")
}

sealed abstract class Method(val value: String)
