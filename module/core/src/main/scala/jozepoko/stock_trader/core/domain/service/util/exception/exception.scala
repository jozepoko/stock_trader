package jozepoko.stock_trader.core.domain.service.util

import java.io.{StringWriter, PrintWriter}

package object exception {
  def getStackTraceString(exception: Throwable): String = {
    val sw = new StringWriter
    val pw = new PrintWriter(sw)
    exception.printStackTrace(pw)
    pw.flush()
    sw.toString
  }
}
