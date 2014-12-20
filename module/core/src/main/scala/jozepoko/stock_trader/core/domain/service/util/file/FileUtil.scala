package jozepoko.stock_trader.core.domain.service.util.file

import java.io.{FileOutputStream, File}
import jozepoko.stock_trader.core.domain.service.util.file.exception.{FileNotDeletedException, UnzipFailException}
import org.apache.commons.compress.archivers.zip.{ZipArchiveEntry, ZipFile}
import org.apache.commons.io.IOUtils
import scala.util.control.NonFatal

class FileUtil {
  def unzip(inputFile: File, outputFile: File): List[File] = {
    val zipFile = new ZipFile(inputFile)
    val entries = zipFile.getEntries
    def stream: Stream[ZipArchiveEntry] = entries.hasMoreElements match {
      case true => entries.nextElement() #:: stream
      case false => Stream.empty
    }
    try {
      val list = for (entry <- stream) yield {
        val output = new File(outputFile, entry.getName)
        if (entry.isDirectory) {
          output.mkdir()
          output
        } else {
          if (!output.getParentFile.exists) {
            output.getParentFile.mkdirs()
          }
          val in = zipFile.getInputStream(entry)
          val out = new FileOutputStream(output)
          IOUtils.copy(in, out)
          in.close()
          out.close()
          output
        }
      }
      val result = list.toList  //closeの前にリスト変換しないとIOException(Stream Closed)の例外がでる。
      zipFile.close()
      result
    } catch {
      case NonFatal(e) => throw new UnzipFailException(
        s"""zip形式のファイルの解凍に失敗しました。
           |ファイルパス : ${inputFile.getAbsolutePath}
           |エラーメッセージ : ${e.getMessage}
           |""".stripMargin
      )
    }
  }

  def delete(file: File): File = {
    try {
      if (file.isFile) file.delete()
      if (file.isDirectory) {
        file.listFiles foreach delete
        file.delete()
      }
      file
    } catch {
      case NonFatal(e) => throw new FileNotDeletedException(s"ファイルの削除に失敗しました。パス : ${file.getAbsolutePath}")
    }
  }
}
