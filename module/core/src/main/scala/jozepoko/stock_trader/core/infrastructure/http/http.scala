package jozepoko.stock_trader.core.infrastructure

import com.ning.http.client.Response
import dispatch._, Defaults._
import java.io.File
import jozepoko.stock_trader.core.infrastructure.exception.{FileNotAssignedException, FileNotFoundException}
import jozepoko.stock_trader.core.infrastructure.http.enum.Method
import jozepoko.stock_trader.core.infrastructure.http.enum.MethodEnum.{PUT, DELETE, POST, GET}
import scala.concurrent.Future

package object http {
  implicit def mapToSeqMap(map: Map[String, String]): Map[String, Seq[String]] = map.map { m => (m._1, Seq(m._2)) }

  case class Request(
    url: String = "",
    method: Method = GET,
    headers: Map[String, Seq[String]] = Map[String, Seq[String]](),
    parameters: Map[String, Seq[String]] = Map[String, Seq[String]](),
    queryParameters: Map[String, Seq[String]] = Map[String, Seq[String]](),
    contentType: Option[(String, String)] = None,
    body: Option[String] = None,
    file: Option[File] = None
  ) {
    val request = dispatch.url("")

    def url(url: String): Request = this.copy(url = url)

    def method(method: Method): Request = this.copy(method = method)

    def headers(headers: Map[String, Seq[String]]): Request = this.copy(headers = headers)

    def parameters(parameters: Map[String, Seq[String]]): Request = this.copy(parameters = parameters)

    def queryParameters(queryParameters: Map[String, Seq[String]]): Request = this.copy(queryParameters = queryParameters)

    def contentType(contentType: (String, String)): Request = this.copy(contentType = Some(contentType))

    def body(body: String): Request = this.copy(body = Some(body))

    def file(file: File): Request = this.copy(file = Some(file))

    def call(): Future[Response] = {
      val request = dispatch.url(url)
      request.setHeaders(headers)
      for ((k, v) <- parameters) request.addParameter(k, v.mkString(","))
      for ((k, v) <- queryParameters) request.addQueryParameter(k, v.mkString(","))
      body foreach { b => request.setBody(b)}
      method match {
        case GET => request.GET
        case POST => request.POST
        case DELETE => request.DELETE
        case PUT =>
          file foreach { f=>
            if (!f.exists) throw new FileNotFoundException(s"ファイルをPUTしようとしましたが見つかりません。パス : ${f.getAbsolutePath}")
            request <<< f
          }
      }
      Http(request)
    }

    def download(): Future[Any] = {
      val f = file.getOrElse(throw new FileNotAssignedException("ダウンロードした結果を保存するファイルが指定されていません"))
      Http(dispatch.url(url) > as.File(f))
    }

    def get(): Future[Response] = this.copy(method = GET).call()
  }
}
