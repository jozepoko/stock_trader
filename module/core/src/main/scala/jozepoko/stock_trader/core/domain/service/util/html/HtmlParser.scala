package jozepoko.stock_trader.core.domain.service.util.html

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class HtmlParserBuilder {
  def apply(html: String): HtmlParser = new HtmlParser(html)
}

class HtmlParser(val html: String) {
  private val parser = Jsoup.parse(html)

  def getElementsByClass(cls: String): Elements = {
    parser.getElementsByClass(cls)
  }
  
  def getElementById(id: String): Element = {
    parser.getElementById(id)
  }
}
