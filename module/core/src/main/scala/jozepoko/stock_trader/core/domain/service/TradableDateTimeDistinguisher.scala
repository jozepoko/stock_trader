package jozepoko.stock_trader.core.domain.service

import org.joda.time.DateTime
import jozepoko.stock_trader.core.domain.service.util.datetime._

class TradableDateTimeDistinguisher {
  /**
   * 土日、祝日、年末年始以外であればtrueを返す。
   * @param dateTime 日付
   * @return Boolean
   */
  def isTradableDateTime(dateTime: DateTime): Boolean = {
    !isEndOfYear(dateTime) && isStartOfYear(dateTime) && !dateTime.isHoliday
  }

  private def isEndOfYear(dateTime: DateTime): Boolean = {
    dateTime.isSameMonthDay(12, 31)
  }

  private def isStartOfYear(dateTime: DateTime): Boolean = {
    dateTime.isSameMonthDay(1, 1) ||
      dateTime.isSameMonthDay(1, 2) ||
      dateTime.isSameMonthDay(1, 3)
  }
}
