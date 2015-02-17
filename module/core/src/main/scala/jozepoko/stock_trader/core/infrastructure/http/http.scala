package jozepoko.stock_trader.core.infrastructure

import java.io.{FileOutputStream, BufferedOutputStream, File}
import jozepoko.stock_trader.core.infrastructure.exception.HttpRequestFailureException
import jozepoko.stock_trader.core.infrastructure.http.enum.Method
import jozepoko.stock_trader.core.infrastructure.http.enum.MethodEnum.GET
import scala.util.control.NonFatal
import scalaj.http.{HttpOptions, Http}

package object http {
  implicit def mapToSeqMap(map: Map[String, String]): Map[String, Seq[String]] = map.map { m => (m._1, Seq(m._2)) }

  case class Request(
    url: String = "",
    method: Method = GET,
    headers: Map[String, Seq[String]] = Map[String, Seq[String]](),
    parameters: Map[String, Seq[String]] = Map[String, Seq[String]](),
    contentType: Option[(String, String)] = None,
    connTimeoutMs: Int = 1000000,
    readTimeoutMs: Int = 1000000
  ) {
    def url(url: String): Request = this.copy(url = url)

    def method(method: Method): Request = this.copy(method = method)

    def headers(headers: Map[String, Seq[String]]): Request = this.copy(headers = headers)

    def parameters(parameters: Map[String, Seq[String]]): Request = this.copy(parameters = parameters)

    def contentType(contentType: (String, String)): Request = this.copy(contentType = Some(contentType))

    def connTimeoutMs(timeoutMs: Int): Request = this.copy(connTimeoutMs = timeoutMs)
    
    def readTimeoutMs(timeoutMs: Int): Request = this.copy(readTimeoutMs = timeoutMs)

    def call(): Response = {
      var request = Http(url)
      request = if (headers.nonEmpty) request.headers(headers) else request
      request = if (parameters.nonEmpty) request.params(parameters) else request
      request = contentType match {
        case Some(v) => request.headers(v :: request.headers)
        case None    => request
      }
      try {
        val (statusCode, header, body) = request.options(HttpOptions.connTimeout(connTimeoutMs), HttpOptions.readTimeout(readTimeoutMs)).asHeadersAndParse(Http.readString)
        Response(statusCode, header, body)
      } catch {
        case NonFatal(e) =>
          throw new HttpRequestFailureException(
            s"""httpリクエストに失敗しました。
               |url: $url
               |method: ${method.value}
               |headers: $headers
               |parameters: $parameters
               |contentType: $contentType
               |エラーメッセージ: ${e.getMessage}
             """.stripMargin,
            e
          )
      }
    }

    def download(file: File): File = {
      val bytes = Http(url).options(HttpOptions.connTimeout(connTimeoutMs), HttpOptions.readTimeout(readTimeoutMs)).asBytes
      val writer = new BufferedOutputStream(new FileOutputStream(file))
      for (b <- bytes) writer.write(b)
      writer.close()  //TODO 例外処理
      file
    }

    def get(): Response = this.copy(method = GET).call()

    private implicit def seqMapToMap(seqMap: Map[String, Seq[String]]): Map[String, String] = {
      seqMap.map { value =>
        val (k, v) = value
        (k, v.mkString(","))
      }
    }
  }

  case class Response(
    statusCode: Int,
    header: Map[String, Seq[String]],
    boby: String
  )
}
