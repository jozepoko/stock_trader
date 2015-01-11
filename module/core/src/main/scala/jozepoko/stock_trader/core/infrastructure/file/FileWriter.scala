package jozepoko.stock_trader.core.infrastructure.file

/**
 * 簡易なファイルライター。
 * closeを勝手にやってくれる程度。
 */
class FileWriter {
  def write(path: String, body: String): Unit = {
    val writer = new java.io.FileWriter(path)
    writer.write(body)
    writer.close()
  }

  def write(path: String, body: Seq[String]): Unit = {
    val writer = new java.io.FileWriter(path)
    body.foreach(writer.write)
    writer.close()
  }
}
