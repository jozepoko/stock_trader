package jozepoko.stock_trader.core.infrastructure.http.enum

object MethodEnum {
  object GET extends Method
  object POST extends Method
  object PUT extends Method
  object DELETE extends Method
}

sealed abstract class Method
