package jozepoko.stock_trader.core.infrastructure

import com.ning.http.client.Response
import dispatch.Http
import java.io.File
import jozepoko.stock_trader.core.infrastructure.exception.FileNotFoundException
import jozepoko.stock_trader.core.infrastructure.http.enum.Method
import jozepoko.stock_trader.core.infrastructure.http.enum.MethodEnum.{PUT, DELETE, POST, GET}
import scala.concurrent.Future

package object http {
  implicit def mapToSeqMap(map: Map[String, String]): Map[String, Seq[String]] = map.map { m => (m._1, Seq(m._2)) }

  class Request {
    def call(
      url: String,
      method: Method,
      headers: Map[String, Seq[String]] = Map[String, Seq[String]](),
      parameters: Map[String, Seq[String]] = Map[String, Seq[String]](),
      queryParameters: Map[String, Seq[String]] = Map[String, Seq[String]](),
      contentType: Option[(String, String)] = None,
      body: Option[String] = None,
      file: Option[File] = None
    ): Future[Response] = {
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

    def call(
      url: String,
      method: Method,
      headers: Map[String, Seq[String]] = Map[String, Seq[String]](),
      parameters: Map[String, Seq[String]] = Map[String, Seq[String]](),
      queryParameters: Map[String, Seq[String]] = Map[String, Seq[String]](),
      contentType: (String, String)
    ): Future[Response] = {
      call(url, method, headers, parameters, queryParameters, Some(contentType), None, None)
    }

    def call(
      url: String,
      method: Method,
      headers: Map[String, Seq[String]] = Map[String, Seq[String]](),
      parameters: Map[String, Seq[String]] = Map[String, Seq[String]](),
      queryParameters: Map[String, Seq[String]] = Map[String, Seq[String]](),
      body: String
    ): Future[Response] = {
      call(url, method, headers, parameters, queryParameters, None, Some(body), None)
    }

    def call(
      url: String,
      method: Method,
      headers: Map[String, Seq[String]] = Map[String, Seq[String]](),
      parameters: Map[String, Seq[String]] = Map[String, Seq[String]](),
      queryParameters: Map[String, Seq[String]] = Map[String, Seq[String]](),
      file: File
    ): Future[Response] = {
      call(url, method, headers, parameters, queryParameters, None, None, Some(file))
    }

    def call(
      url: String,
      method: Method,
      headers: Map[String, Seq[String]] = Map[String, Seq[String]](),
      parameters: Map[String, Seq[String]] = Map[String, Seq[String]](),
      queryParameters: Map[String, Seq[String]] = Map[String, Seq[String]](),
      contentType: (String, String),
      body: String
    ): Future[Response] = {
      call(url, method, headers, parameters, queryParameters, Some(contentType), Some(body), None)
    }

    def call(
      url: String,
      method: Method,
      headers: Map[String, Seq[String]] = Map[String, Seq[String]](),
      parameters: Map[String, Seq[String]] = Map[String, Seq[String]](),
      queryParameters: Map[String, Seq[String]] = Map[String, Seq[String]](),
      contentType: (String, String),
      file: File
    ): Future[Response] = {
      call(url, method, headers, parameters, queryParameters, Some(contentType), None, Some(file))
    }

    def call(
      url: String,
      method: Method,
      headers: Map[String, Seq[String]] = Map[String, Seq[String]](),
      parameters: Map[String, Seq[String]] = Map[String, Seq[String]](),
      queryParameters: Map[String, Seq[String]] = Map[String, Seq[String]](),
      body: String,
      file: File
    ): Future[Response] = {
      call(url, method, headers, parameters, queryParameters, None, Some(body), Some(file))
    }

    def call(
      url: String,
      method: Method,
      headers: Map[String, Seq[String]] = Map[String, Seq[String]](),
      parameters: Map[String, Seq[String]] = Map[String, Seq[String]](),
      queryParameters: Map[String, Seq[String]] = Map[String, Seq[String]](),
      contentType: (String, String),
      body: String,
      file: File
    ): Future[Response] = {
      call(url, method, headers, parameters, queryParameters, Some(contentType), Some(body), Some(file))
    }

    def get(
      url: String,
      headers: Map[String, Seq[String]] = Map[String, Seq[String]](),
      parameters: Map[String, Seq[String]] = Map[String, Seq[String]](),
      queryParameters: Map[String, Seq[String]] = Map[String, Seq[String]](),
      contentType: Option[(String, String)] = None,
      body: Option[String] = None,
      file: Option[File] = None
    ): Future[Response] = call(
      url,
      GET,
      headers,
      parameters,
      queryParameters,
      contentType,
      body,
      file
    )

    def get(
      url: String,
      headers: Map[String, Seq[String]] = Map[String, Seq[String]](),
      parameters: Map[String, Seq[String]] = Map[String, Seq[String]](),
      queryParameters: Map[String, Seq[String]] = Map[String, Seq[String]](),
      contentType: (String, String)
    ): Future[Response] = {
      get(url, headers, parameters, queryParameters, Some(contentType), None, None)
    }

    def get(
      url: String,
      headers: Map[String, Seq[String]] = Map[String, Seq[String]](),
      parameters: Map[String, Seq[String]] = Map[String, Seq[String]](),
      queryParameters: Map[String, Seq[String]] = Map[String, Seq[String]](),
      body: String
    ): Future[Response] = {
      get(url, headers, parameters, queryParameters, None, Some(body), None)
    }

    def get(
      url: String,
      headers: Map[String, Seq[String]] = Map[String, Seq[String]](),
      parameters: Map[String, Seq[String]] = Map[String, Seq[String]](),
      queryParameters: Map[String, Seq[String]] = Map[String, Seq[String]](),
      file: File
    ): Future[Response] = {
      get(url, headers, parameters, queryParameters, None, None, Some(file))
    }

    def get(
      url: String,
      headers: Map[String, Seq[String]] = Map[String, Seq[String]](),
      parameters: Map[String, Seq[String]] = Map[String, Seq[String]](),
      queryParameters: Map[String, Seq[String]] = Map[String, Seq[String]](),
      contentType: (String, String),
      body: String
    ): Future[Response] = {
      get(url, headers, parameters, queryParameters, Some(contentType), Some(body), None)
    }

    def get(
      url: String,
      headers: Map[String, Seq[String]] = Map[String, Seq[String]](),
      parameters: Map[String, Seq[String]] = Map[String, Seq[String]](),
      queryParameters: Map[String, Seq[String]] = Map[String, Seq[String]](),
      contentType: (String, String),
      file: File
    ): Future[Response] = {
      get(url, headers, parameters, queryParameters, Some(contentType), None, Some(file))
    }

    def get(
      url: String,
      headers: Map[String, Seq[String]] = Map[String, Seq[String]](),
      parameters: Map[String, Seq[String]] = Map[String, Seq[String]](),
      queryParameters: Map[String, Seq[String]] = Map[String, Seq[String]](),
      body: String,
      file: File
    ): Future[Response] = {
      get(url, headers, parameters, queryParameters, None, Some(body), Some(file))
    }

    def get(
      url: String,
      headers: Map[String, Seq[String]] = Map[String, Seq[String]](),
      parameters: Map[String, Seq[String]] = Map[String, Seq[String]](),
      queryParameters: Map[String, Seq[String]] = Map[String, Seq[String]](),
      contentType: (String, String),
      body: String,
      file: File
    ): Future[Response] = {
      get(url, headers, parameters, queryParameters, Some(contentType), Some(body), Some(file))
    }
  }
}
