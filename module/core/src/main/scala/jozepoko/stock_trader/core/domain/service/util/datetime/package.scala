package jozepoko.stock_trader.core.domain.service.util

import org.joda.time.DateTime

package object datetime {
  implicit def dateTimeToMyDateTime(dateTime: DateTime): MyDateTime = new MyDateTime(dateTime)

  class MyDateTime(val dateTime: DateTime) extends HolidayDistinguisher {
    /**
     * 土日、祝日、年末年始以外であればtrueを返す。
     * @return Boolean
     */
    def isTradableDateTime: Boolean = {
      val isEndOfYear: Boolean = {
        dateTime.isSameMonthDay(12, 31)
      }

      val isStartOfYear: Boolean = {
        dateTime.isSameMonthDay(1, 1) ||
          dateTime.isSameMonthDay(1, 2) ||
          dateTime.isSameMonthDay(1, 3)
      }

      !isEndOfYear && !isStartOfYear && !dateTime.isHoliday
    }

    def getWeek: Week = {
      dateTime.getDayOfWeek match {
        case 1 => WeekEnum.Monday
        case 2 => WeekEnum.Tuesday
        case 3 => WeekEnum.Wednesday
        case 4 => WeekEnum.Thursday
        case 5 => WeekEnum.Friday
        case 6 => WeekEnum.Saturday
        case 7 => WeekEnum.Sunday
      }
    }

    def withLastDayOfMonth: DateTime = {
      dateTime.dayOfMonth.withMaximumValue
    }

    def getLastDayOfMonth: Int = {
      withLastDayOfMonth.getDayOfMonth
    }

    //第2月曜日、みたいなのの「2」を取得する
    def getWeekCount: Int = {
      def constructDateTimeList(dateTime: DateTime): Stream[DateTime] = dateTime #:: constructDateTimeList(dateTime.plusDays(1))
      def constructSameWeekDayList(day: Int): Stream[Int] = day #:: constructSameWeekDayList(day + 7)
      val firstSameWeekDay = constructDateTimeList(dateTime.withDayOfMonth(1)).find(_.getWeek == getWeek).get  //TODO getは良くない！
      constructSameWeekDayList(firstSameWeekDay.getDayOfMonth).takeWhile { sameWeekDay =>
        sameWeekDay <= dateTime.getDayOfMonth
      }.length
    }

    /**
     * 月日が同じ場合true
     * @param month 月
     * @param day 日
     * @return Boolean
     */
    def isSameMonthDay(month: Int, day: Int): Boolean = {
      dateTime.getMonthOfYear == month && dateTime.getDayOfMonth == day
    }

    /**
     * 第２月曜日、みたいなのを調べる。
     * @param week 調べたい曜日
     * @param count 調べたい曜日のカウント (例えば第２月曜日の2)
     * @return Boolean
     */
    def isSameWeekCount(week: Week, count: Int): Boolean = {
      getWeek == week && getWeekCount == count
    }

    /**
     * 2015年01月11日(日) のような形式にフォーマットする。
     * @return String
     */
    def formatToJapanese: String = dateTime.toString("yyyy年MM月dd日") + dateTime.getWeek.shortName
  }

  object WeekEnum {
    object Monday    extends Week("月曜日", "(月)")
    object Tuesday   extends Week("火曜日", "(火)")
    object Wednesday extends Week("水曜日", "(水)")
    object Thursday  extends Week("木曜日", "(木)")
    object Friday    extends Week("金曜日", "(金)")
    object Saturday  extends Week("土曜日", "(土)")
    object Sunday    extends Week("日曜日", "(日)")
  }

  sealed abstract class Week(val name: String, val shortName: String)
}
