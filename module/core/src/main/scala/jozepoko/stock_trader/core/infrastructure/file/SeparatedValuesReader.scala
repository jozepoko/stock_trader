package jozepoko.stock_trader.core.infrastructure.file

import java.io.File
import jozepoko.stock_trader.core.infrastructure.exception.SeparatedValuesReaderException
import org.apache.commons.io.FileUtils
import scala.collection.immutable.ListMap
import scala.util.control.NonFatal

class SeparatedValuesReader {
  def headerColumns(
    path: String,
    separator: String = ",",
    enclosure: String = "\"",
    encoding: String = "utf-8",
    headerStartLine: Int = 1
  ): List[String] = {
    val reader = FileUtils.lineIterator(new File(path), encoding)
    try {
      if (!reader.hasNext) {
        reader.close()
        Nil
      }
      for (i <- 0 until headerStartLine - 1) reader.next()
      explode(reader.next(), separator, enclosure)
    } catch {
      case NonFatal(e) => throw new SeparatedValuesReaderException(s"ヘッダーの読み込みに失敗しました。 パス : $path")
    } finally {
      reader.close()
    }
  }

  def read[T](
    path: String,
    separator: String = ",",
    enclosure: String = "\"",
    encoding: String = "utf-8",
    headerStartLine: Int = 1
  )(f: ListMap[String, String] => T): Unit = {
    val reader = FileUtils.lineIterator(new File(path), encoding)
    try {
      if (!reader.hasNext) {
        reader.close()
      }
      for (i <- 0 until headerStartLine) reader.next()
      while(reader.hasNext) {
        val columns = explode(reader.next, separator, enclosure)
        f(ListMap(headerColumns(path, separator, enclosure, encoding, headerStartLine) zip columns: _*))
      }
    } catch {
      case NonFatal(e) => throw new SeparatedValuesReaderException(
        s"""ファイルの読み込みに失敗しました。
           |パス : $path
           |エラーメッセージ : ${e.getMessage}
           |""".stripMargin
      )
    } finally {
      reader.close()
    }
  }

  private def explode(
    string: String,
    separator: String,
    enclosure: String
  ): List[String] = {
    val s = string.substring(enclosure.length, string.length - enclosure.length)
    s.split(s"$enclosure$separator$enclosure").toList
  }
}
